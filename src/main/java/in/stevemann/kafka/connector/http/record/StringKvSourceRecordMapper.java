package in.stevemann.kafka.connector.http.record;

import in.stevemann.kafka.connector.http.model.Offset;
import in.stevemann.kafka.connector.http.record.model.KvRecord;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyMap;
import static org.apache.kafka.connect.data.SchemaBuilder.string;

/**
 * @deprecated The same can be achieved with Kafka Connect's SMT ExtractKey/ExtractValue. e.g.
 * <p>
 * "transforms": "ExtractKey,ExtractValue",
 * "transforms.ExtractKey.type": "org.apache.kafka.connect.transforms.ExtractField$Key",
 * "transforms.ExtractKey.field": "key",
 * "transforms.ExtractValue.type": "org.apache.kafka.connect.transforms.ExtractField$Value",
 * "transforms.ExtractValue.field": "value"
 */
@Deprecated
@RequiredArgsConstructor
public class StringKvSourceRecordMapper implements KvSourceRecordMapper {

  private static Map<String, ?> sourcePartition = emptyMap();

  private static final Schema keySchema = string().build();

  private static final Schema valueSchema = string().build();

  private final Function<Map<String, ?>, SourceRecordMapperConfig> configFactory;

  private SourceRecordMapperConfig config;

  public StringKvSourceRecordMapper() {
    this(SourceRecordMapperConfig::new);
  }

  @Override
  public void configure(Map<String, ?> settings) {
    config = configFactory.apply(settings);
  }

  @Override
  public SourceRecord map(KvRecord record) {

    Offset offset = record.getOffset();

    return new SourceRecord(
        sourcePartition,
        offset.toMap(),
        config.getTopic(),
        null,
        keySchema,
        record.getKey(),
        valueSchema,
        record.getValue(),
        offset.getTimestamp().map(Instant::toEpochMilli).orElseGet(System::currentTimeMillis));
  }
}
