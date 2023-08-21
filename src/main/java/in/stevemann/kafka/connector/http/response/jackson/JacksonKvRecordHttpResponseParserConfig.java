package in.stevemann.kafka.connector.http.response.jackson;

import in.stevemann.kafka.connector.http.response.timestamp.EpochMillisOrDelegateTimestampParser;
import in.stevemann.kafka.connector.http.response.timestamp.TimestampParser;
import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;
import static org.apache.kafka.common.config.ConfigDef.Type.CLASS;

@Getter
public class JacksonKvRecordHttpResponseParserConfig extends AbstractConfig {

  private static final String RECORD_TIMESTAMP_PARSER_CLASS = "http.response.record.timestamp.parser";

  private final JacksonResponseRecordParser responseParser;
  private final TimestampParser timestampParser;

  JacksonKvRecordHttpResponseParserConfig(Map<String, ?> originals) {
    super(config(), originals);
    JacksonSerializer serializer = new JacksonSerializer();
    JacksonRecordParser recordParser = new JacksonRecordParser(serializer);
    recordParser.configure(originals);
    responseParser = new JacksonResponseRecordParser(recordParser, serializer);
    responseParser.configure(originals);
    timestampParser = getConfiguredInstance(RECORD_TIMESTAMP_PARSER_CLASS, TimestampParser.class);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(RECORD_TIMESTAMP_PARSER_CLASS, CLASS, EpochMillisOrDelegateTimestampParser.class, LOW, "Record Timestamp parser class");
  }
}
