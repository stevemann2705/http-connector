package in.stevemann.kafka.connector.http.auth;

import okhttp3.Credentials;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class BasicHttpAuthenticator implements HttpAuthenticator {

  private final Function<Map<String, ?>, BasicHttpAuthenticatorConfig> configFactory;

  Optional<String> header;

  public BasicHttpAuthenticator() {
    this(BasicHttpAuthenticatorConfig::new);
  }

  public BasicHttpAuthenticator(Function<Map<String, ?>, BasicHttpAuthenticatorConfig> configFactory) {
    this.configFactory = configFactory;
  }

  @Override
  public void configure(Map<String, ?> configs) {

    BasicHttpAuthenticatorConfig config = configFactory.apply(configs);

    if (!isEmpty(config.getUser()) || !isEmpty(config.getPassword().value())) {
      header = Optional.of(Credentials.basic(config.getUser(), config.getPassword().value()));
    } else {
      header = Optional.empty();
    }
  }

  @Override
  public Optional<String> getAuthorizationHeader() {
    return header;
  }
}
