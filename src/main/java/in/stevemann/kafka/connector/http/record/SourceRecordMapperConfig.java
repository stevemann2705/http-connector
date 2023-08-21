package in.stevemann.kafka.connector.http.record;

import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Type.STRING;

@Getter
public class SourceRecordMapperConfig extends AbstractConfig {

  private static final String TOPIC = "kafka.topic";

  private final String topic;

  SourceRecordMapperConfig(Map<String, ?> originals) {
    super(config(), originals);
    topic = getString(TOPIC);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(TOPIC, STRING, HIGH, "Kafka Topic");
  }
}
