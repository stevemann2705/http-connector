package in.stevemann.kafka.connector.http.response;

import in.stevemann.kafka.connector.http.model.HttpResponse;
import org.apache.kafka.common.Configurable;

import java.util.Map;

@FunctionalInterface
public interface HttpResponsePolicy extends Configurable {

  HttpResponseOutcome resolve(HttpResponse response);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }

  enum HttpResponseOutcome {
    PROCESS, SKIP, FAIL
  }
}

