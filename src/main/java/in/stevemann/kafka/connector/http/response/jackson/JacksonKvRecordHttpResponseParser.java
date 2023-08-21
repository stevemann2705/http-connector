package in.stevemann.kafka.connector.http.response.jackson;

import in.stevemann.kafka.connector.http.model.HttpResponse;
import in.stevemann.kafka.connector.http.model.Offset;
import in.stevemann.kafka.connector.http.record.model.KvRecord;
import in.stevemann.kafka.connector.http.response.KvRecordHttpResponseParser;
import in.stevemann.kafka.connector.http.response.jackson.model.JacksonRecord;
import in.stevemann.kafka.connector.http.response.timestamp.TimestampParser;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.UUID.nameUUIDFromBytes;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class JacksonKvRecordHttpResponseParser implements KvRecordHttpResponseParser {

  private final Function<Map<String, ?>, JacksonKvRecordHttpResponseParserConfig> configFactory;

  private JacksonResponseRecordParser responseParser;

  private TimestampParser timestampParser;

  public JacksonKvRecordHttpResponseParser() {
    this(JacksonKvRecordHttpResponseParserConfig::new);
  }

  @Override
  public void configure(Map<String, ?> configs) {
    JacksonKvRecordHttpResponseParserConfig config = configFactory.apply(configs);
    responseParser = config.getResponseParser();
    timestampParser = config.getTimestampParser();
  }

  @Override
  public List<KvRecord> parse(HttpResponse response) {
    return responseParser.getRecords(response.getBody())
        .map(this::map)
        .collect(toList());
  }

  private KvRecord map(JacksonRecord record) {

    Map<String, Object> offsets = record.getOffset();

    String key = ofNullable(record.getKey())
        .map(Optional::of)
        .orElseGet(() -> ofNullable(offsets.get("key")).map(String.class::cast))
        .orElseGet(() -> generateConsistentKey(record.getBody()));

    Optional<Instant> timestamp = ofNullable(record.getTimestamp())
        .map(Optional::of)
        .orElseGet(() -> ofNullable(offsets.get("timestamp")).map(String.class::cast))
        .map(timestampParser::parse);

    Offset offset = timestamp
        .map(ts -> Offset.of(offsets, key, ts))
        .orElseGet(() -> Offset.of(offsets, key));

    return KvRecord.builder()
        .key(key)
        .value(record.getBody())
        .offset(offset)
        .build();
  }

  private static String generateConsistentKey(String body) {
    return nameUUIDFromBytes(body.getBytes()).toString();
  }
}

