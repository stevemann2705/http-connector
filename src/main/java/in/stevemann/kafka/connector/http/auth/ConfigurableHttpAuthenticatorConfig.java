package in.stevemann.kafka.connector.http.auth;

import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.MEDIUM;
import static org.apache.kafka.common.config.ConfigDef.Type.STRING;

@Getter
public class ConfigurableHttpAuthenticatorConfig extends AbstractConfig {

  private static final String AUTH_TYPE = "http.auth.type";

  private final HttpAuthenticator authenticator;

  ConfigurableHttpAuthenticatorConfig(Map<String, ?> originals) {
    super(config(), originals);
    authenticator = getAuthenticator(originals);
  }

  private HttpAuthenticator getAuthenticator(Map<String, ?> originals) {
    switch (HttpAuthenticationType.valueOf(getString(AUTH_TYPE).toUpperCase())) {
      case BASIC:
        BasicHttpAuthenticator auth = new BasicHttpAuthenticator();
        auth.configure(originals);
        return auth;
      default:
        return new NoneHttpAuthenticator();
    }
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(AUTH_TYPE, STRING, HttpAuthenticationType.NONE.name(), MEDIUM, "Authentication Type");
  }
}

