package in.stevemann.kafka.connector.http;

import edu.emory.mathcs.backport.java.util.Collections;
import in.stevemann.kafka.connector.http.ack.ConfirmationWindow;
import in.stevemann.kafka.connector.http.client.HttpClient;
import in.stevemann.kafka.connector.http.model.HttpRequest;
import in.stevemann.kafka.connector.http.model.HttpResponse;
import in.stevemann.kafka.connector.http.model.Offset;
import in.stevemann.kafka.connector.http.record.SourceRecordFilterFactory;
import in.stevemann.kafka.connector.http.record.SourceRecordSorter;
import in.stevemann.kafka.connector.http.request.HttpRequestFactory;
import in.stevemann.kafka.connector.http.response.HttpResponseParser;
import in.stevemann.kafka.connector.http.timer.TimeThrottler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static in.stevemann.kafka.connector.http.utils.VersionUtils.getVersion;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
public class HttpSourceTask extends SourceTask {

  private final Function<Map<String, String>, HttpSourceConnectorConfig> configFactory;

  private TimeThrottler throttler;

  private HttpRequestFactory requestFactory;

  private HttpClient requestExecutor;

  private HttpResponseParser responseParser;

  private SourceRecordSorter recordSorter;

  private SourceRecordFilterFactory recordFilterFactory;

  private ConfirmationWindow<Map<String, ?>> confirmationWindow = new ConfirmationWindow<>(emptyList());

  @Getter
  private Offset offset;

  HttpSourceTask(Function<Map<String, String>, HttpSourceConnectorConfig> configFactory) {
    this.configFactory = configFactory;
  }

  public HttpSourceTask() {
    this(HttpSourceConnectorConfig::new);
  }

  @Override
  public String version() {
    return getVersion();
  }

  @Override
  public void start(Map<String, String> settings) {
    HttpSourceConnectorConfig config = configFactory.apply(settings);

    throttler = config.getThrottler();
    requestFactory = config.getRequestFactory();
    requestExecutor = config.getClient();
    responseParser = config.getResponseParser();
    recordSorter = config.getRecordSorter();
    recordFilterFactory = config.getRecordFilterFactory();
    offset = initializeOffset(config.getInitialOffset());
  }

  private Offset initializeOffset(Map<String, String> initialOffset) {
    Map<String, Object> restoredOffset = ofNullable(context.offsetStorageReader().offset(emptyMap())).orElseGet(Collections::emptyMap);
    return Offset.of(!restoredOffset.isEmpty() ? restoredOffset : initialOffset);
  }

  private Offset loadOffset() {
    return confirmationWindow.getLowWatermarkOffset()
        .map(Offset::of)
        .orElse(offset);
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {

    throttler.throttle(offset.getTimestamp().orElseGet(Instant::now));

    offset = loadOffset();

    HttpRequest request = requestFactory.createRequest(offset);

    HttpResponse response = execute(request);

    List<SourceRecord> records = responseParser.parse(response);

    List<SourceRecord> unseenRecords = recordSorter.sort(records).stream()
        .filter(recordFilterFactory.create(offset))
        .collect(toList());

    log.info("Request for offset {} yields {}/{} new records", offset.toMap(), unseenRecords.size(), records.size());

    confirmationWindow = new ConfirmationWindow<>(extractOffsets(unseenRecords));

    return unseenRecords;
  }

  private HttpResponse execute(HttpRequest request) {
    try {
      return requestExecutor.execute(request);
    } catch (IOException e) {
      throw new RetriableException(e);
    }
  }

  private static List<Map<String, ?>> extractOffsets(List<SourceRecord> recordsToSend) {
    return recordsToSend.stream()
        .map(SourceRecord::sourceOffset)
        .collect(toList());
  }

  @Override
  public void stop() {
    // do nothing
  }
}
