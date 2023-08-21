package in.stevemann.kafka.connector.http.response;

import in.stevemann.kafka.connector.http.model.HttpResponse;
import in.stevemann.kafka.connector.http.record.KvSourceRecordMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class KvHttpResponseParser implements HttpResponseParser {

  private final Function<Map<String, ?>, KvHttpResponseParserConfig> configFactory;

  private KvRecordHttpResponseParser recordParser;

  private KvSourceRecordMapper recordMapper;

  public KvHttpResponseParser() {
    this(KvHttpResponseParserConfig::new);
  }

  @Override
  public void configure(Map<String, ?> configs) {
    KvHttpResponseParserConfig config = configFactory.apply(configs);
    recordParser = config.getRecordParser();
    recordMapper = config.getRecordMapper();
  }

  @Override
  public List<SourceRecord> parse(HttpResponse response) {
    return recordParser.parse(response).stream()
        .map(recordMapper::map)
        .collect(toList());
  }
}

