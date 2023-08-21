package in.stevemann.kafka.connector.http.response;

import in.stevemann.kafka.connector.http.record.KvSourceRecordMapper;
import in.stevemann.kafka.connector.http.record.SchemedKvSourceRecordMapper;
import in.stevemann.kafka.connector.http.response.jackson.JacksonKvRecordHttpResponseParser;
import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;
import static org.apache.kafka.common.config.ConfigDef.Type.CLASS;

@Getter
public class KvHttpResponseParserConfig extends AbstractConfig {

  private static final String RECORD_PARSER_CLASS = "http.response.record.parser";
  private static final String RECORD_MAPPER_CLASS = "http.response.record.mapper";

  private final KvRecordHttpResponseParser recordParser;
  private final KvSourceRecordMapper recordMapper;

  KvHttpResponseParserConfig(Map<String, ?> originals) {
    super(config(), originals);
    recordParser = getConfiguredInstance(RECORD_PARSER_CLASS, KvRecordHttpResponseParser.class);
    recordMapper = getConfiguredInstance(RECORD_MAPPER_CLASS, KvSourceRecordMapper.class);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(RECORD_PARSER_CLASS, CLASS, JacksonKvRecordHttpResponseParser.class, LOW, "Key-Value Record Parser class")
        .define(RECORD_MAPPER_CLASS, CLASS, SchemedKvSourceRecordMapper.class, LOW, "Key-Value Record Factory class");
  }
}