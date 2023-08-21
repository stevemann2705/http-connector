package in.stevemann.kafka.connector.http.response;

import in.stevemann.kafka.connector.http.model.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static in.stevemann.kafka.connector.http.response.HttpResponsePolicy.HttpResponseOutcome.*;

@Slf4j
@RequiredArgsConstructor
public class StatusCodeHttpResponsePolicy implements HttpResponsePolicy {

  private final Function<Map<String, ?>, StatusCodeHttpResponsePolicyConfig> configFactory;

  private Set<Integer> processCodes;

  private Set<Integer> skipCodes;

  public StatusCodeHttpResponsePolicy() {
    this(StatusCodeHttpResponsePolicyConfig::new);
  }

  @Override
  public void configure(Map<String, ?> settings) {
    StatusCodeHttpResponsePolicyConfig config = configFactory.apply(settings);
    processCodes = config.getProcessCodes();
    skipCodes = config.getSkipCodes();
  }

  @Override
  public HttpResponseOutcome resolve(HttpResponse response) {
    if (processCodes.contains(response.getCode())) {
      return PROCESS;
    } else if (skipCodes.contains(response.getCode())) {
      log.warn("Unexpected HttpResponse status code: {}, continuing with no records", response.getCode());
      return SKIP;
    } else {
      return FAIL;
    }
  }
}

