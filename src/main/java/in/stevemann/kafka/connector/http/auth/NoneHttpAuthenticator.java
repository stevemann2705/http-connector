package in.stevemann.kafka.connector.http.auth;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class NoneHttpAuthenticator implements HttpAuthenticator {

  @Override
  public Optional<String> getAuthorizationHeader() {
    return Optional.empty();
  }
}

