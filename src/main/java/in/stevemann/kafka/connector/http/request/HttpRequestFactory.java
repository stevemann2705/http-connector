package in.stevemann.kafka.connector.http.request;

import in.stevemann.kafka.connector.http.model.HttpRequest;
import in.stevemann.kafka.connector.http.model.Offset;
import org.apache.kafka.common.Configurable;

import java.util.Map;

@FunctionalInterface
public interface HttpRequestFactory extends Configurable {

  HttpRequest createRequest(Offset offset);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}