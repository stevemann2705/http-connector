package in.stevemann.kafka.connector.http;

import in.stevemann.kafka.connector.http.client.HttpClient;
import in.stevemann.kafka.connector.http.client.OkHttpClient;
import in.stevemann.kafka.connector.http.record.OffsetRecordFilterFactory;
import in.stevemann.kafka.connector.http.record.OrderDirectionSourceRecordSorter;
import in.stevemann.kafka.connector.http.record.SourceRecordFilterFactory;
import in.stevemann.kafka.connector.http.record.SourceRecordSorter;
import in.stevemann.kafka.connector.http.request.HttpRequestFactory;
import in.stevemann.kafka.connector.http.request.template.TemplateHttpRequestFactory;
import in.stevemann.kafka.connector.http.response.HttpResponseParser;
import in.stevemann.kafka.connector.http.response.PolicyHttpResponseParser;
import in.stevemann.kafka.connector.http.timer.AdaptableIntervalTimer;
import in.stevemann.kafka.connector.http.timer.TimeThrottler;
import in.stevemann.kafka.connector.http.timer.Timer;
import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static in.stevemann.kafka.connector.http.utils.ConfigUtils.breakDownMap;
import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;
import static org.apache.kafka.common.config.ConfigDef.Type.CLASS;
import static org.apache.kafka.common.config.ConfigDef.Type.STRING;

@Getter
class HttpSourceConnectorConfig extends AbstractConfig {

  private static final String TIMER = "http.timer";
  private static final String CLIENT = "http.client";
  private static final String REQUEST_FACTORY = "http.request.factory";
  private static final String RESPONSE_PARSER = "http.response.parser";
  private static final String RECORD_SORTER = "http.record.sorter";
  private static final String RECORD_FILTER_FACTORY = "http.record.filter.factory";
  private static final String OFFSET_INITIAL = "http.offset.initial";

  private final TimeThrottler throttler;
  private final HttpRequestFactory requestFactory;
  private final HttpClient client;
  private final HttpResponseParser responseParser;
  private final SourceRecordFilterFactory recordFilterFactory;
  private final SourceRecordSorter recordSorter;
  private final Map<String, String> initialOffset;

  HttpSourceConnectorConfig(Map<String, ?> originals) {
    super(config(), originals);
    Timer timer = getConfiguredInstance(TIMER, Timer.class);
    throttler = new TimeThrottler(timer);
    requestFactory = getConfiguredInstance(REQUEST_FACTORY, HttpRequestFactory.class);
    client = getConfiguredInstance(CLIENT, HttpClient.class);
    responseParser = getConfiguredInstance(RESPONSE_PARSER, HttpResponseParser.class);
    recordSorter = getConfiguredInstance(RECORD_SORTER, SourceRecordSorter.class);
    recordFilterFactory = getConfiguredInstance(RECORD_FILTER_FACTORY, SourceRecordFilterFactory.class);
    initialOffset = breakDownMap(getString(OFFSET_INITIAL));
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(TIMER, CLASS, AdaptableIntervalTimer.class, HIGH, "Poll Timer Class")
        .define(CLIENT, CLASS, OkHttpClient.class, HIGH, "Request Client Class")
        .define(REQUEST_FACTORY, CLASS, TemplateHttpRequestFactory.class, HIGH, "Request Factory Class")
        .define(RESPONSE_PARSER, CLASS, PolicyHttpResponseParser.class, HIGH, "Response Parser Class")
        .define(RECORD_SORTER, CLASS, OrderDirectionSourceRecordSorter.class, LOW, "Record Sorter Class")
        .define(RECORD_FILTER_FACTORY, CLASS, OffsetRecordFilterFactory.class, LOW, "Record Filter Factory Class")
        .define(OFFSET_INITIAL, STRING, "", HIGH, "Starting offset");
  }
}
