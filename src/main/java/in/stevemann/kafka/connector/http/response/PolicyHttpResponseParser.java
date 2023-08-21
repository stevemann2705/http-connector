package in.stevemann.kafka.connector.http.response;

import in.stevemann.kafka.connector.http.model.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class PolicyHttpResponseParser implements HttpResponseParser {

  private final Function<Map<String, ?>, PolicyHttpResponseParserConfig> configFactory;

  private HttpResponseParser delegate;

  private HttpResponsePolicy policy;

  public PolicyHttpResponseParser() {
    this(PolicyHttpResponseParserConfig::new);
  }

  @Override
  public void configure(Map<String, ?> settings) {
    PolicyHttpResponseParserConfig config = configFactory.apply(settings);
    delegate = config.getDelegateParser();
    policy = config.getPolicy();
  }

  @Override
  public List<SourceRecord> parse(HttpResponse response) {
    switch (policy.resolve(response)) {
      case PROCESS:
        return delegate.parse(response);
      case SKIP:
        return emptyList();
      case FAIL:
      default:
        throw new IllegalStateException(String.format("Policy failed for response code: %s, body: %s", response.getCode(), ofNullable(response.getBody()).map(String::new).orElse("")));
    }
  }
}

