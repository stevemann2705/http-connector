package in.stevemann.kafka.connector.http.auth;

import org.apache.kafka.common.Configurable;

import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface HttpAuthenticator extends Configurable {

  Optional<String> getAuthorizationHeader();

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}
