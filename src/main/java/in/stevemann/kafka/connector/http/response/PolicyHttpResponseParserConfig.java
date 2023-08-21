package in.stevemann.kafka.connector.http.response;

import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Type.CLASS;

@Getter
public class PolicyHttpResponseParserConfig extends AbstractConfig {

  private static final String PARSER_DELEGATE = "http.response.policy.parser";
  private static final String POLICY = "http.response.policy";

  private final HttpResponseParser delegateParser;

  private final HttpResponsePolicy policy;

  public PolicyHttpResponseParserConfig(Map<String, ?> originals) {
    super(config(), originals);
    delegateParser = getConfiguredInstance(PARSER_DELEGATE, HttpResponseParser.class);
    policy = getConfiguredInstance(POLICY, HttpResponsePolicy.class);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(PARSER_DELEGATE, CLASS, KvHttpResponseParser.class, HIGH, "Response Parser Delegate Class")
        .define(POLICY, CLASS, StatusCodeHttpResponsePolicy.class, HIGH, "Response Policy Class");
  }
}
