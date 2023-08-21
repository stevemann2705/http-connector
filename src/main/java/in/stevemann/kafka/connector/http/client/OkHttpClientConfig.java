package in.stevemann.kafka.connector.http.client;

import in.stevemann.kafka.connector.http.auth.ConfigurableHttpAuthenticator;
import in.stevemann.kafka.connector.http.auth.HttpAuthenticator;
import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.types.Password;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Importance.MEDIUM;
import static org.apache.kafka.common.config.ConfigDef.Type.*;

@Getter
public class OkHttpClientConfig extends AbstractConfig {

  private static final String CONNECTION_TIMEOUT_MILLIS = "http.client.connection.timeout.millis";
  private static final String READ_TIMEOUT_MILLIS = "http.client.read.timeout.millis";
  private static final String CONNECTION_KEEP_ALIVE_DURATION_MILLIS = "http.client.ttl.millis";
  private static final String CONNECTION_MAX_IDLE = "http.client.max-idle";
  private static final String AUTHENTICATOR = "http.auth";
  private static final String PROXY_HOST = "http.client.proxy.host";
  private static final String PROXY_PORT = "http.client.proxy.port";
  private static final String PROXY_USERNAME = "http.client.proxy.username";
  private static final String PROXY_PASSWORD = "http.client.proxy.password";
  private static final String KEYSTORE = "http.client.keystore";
  private static final String KEYSTORE_PASSWORD = "http.client.keystore.password";

  private final Long connectionTimeoutMillis;
  private final Long readTimeoutMillis;
  private final Long keepAliveDuration;
  private final Integer maxIdleConnections;
  private final HttpAuthenticator authenticator;
  private final String proxyHost;
  private final Integer proxyPort;
  private final String proxyUsername;
  private final String proxyPassword;
  private final String keyStore;
  private final Password keyStorePassword;

  OkHttpClientConfig(Map<String, ?> originals) {
    super(config(), originals);
    connectionTimeoutMillis = getLong(CONNECTION_TIMEOUT_MILLIS);
    readTimeoutMillis = getLong(READ_TIMEOUT_MILLIS);
    keepAliveDuration = getLong(CONNECTION_KEEP_ALIVE_DURATION_MILLIS);
    maxIdleConnections = getInt(CONNECTION_MAX_IDLE);
    authenticator = getConfiguredInstance(AUTHENTICATOR, HttpAuthenticator.class);
    proxyHost = getString(PROXY_HOST);
    proxyPort = getInt(PROXY_PORT);
    proxyUsername = getString(PROXY_USERNAME);
    proxyPassword = getString(PROXY_PASSWORD);
    keyStore = getString(KEYSTORE);
    keyStorePassword = getPassword(KEYSTORE_PASSWORD);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(CONNECTION_TIMEOUT_MILLIS, LONG, 2000, HIGH, "Connection Timeout Millis")
        .define(READ_TIMEOUT_MILLIS, LONG, 2000, HIGH, "Read Timeout Millis")
        .define(CONNECTION_KEEP_ALIVE_DURATION_MILLIS, LONG, 300000, HIGH, "Keep Alive Duration Millis")
        .define(CONNECTION_MAX_IDLE, INT, 1, HIGH, "Max Idle Connections")
        .define(AUTHENTICATOR, CLASS, ConfigurableHttpAuthenticator.class, MEDIUM, "Custom Authenticator")
        .define(PROXY_HOST, STRING, "", MEDIUM, "Proxy host")
        .define(PROXY_PORT, INT, 3128, MEDIUM, "Proxy port")
        .define(PROXY_USERNAME, STRING, "", MEDIUM, "Proxy username")
        .define(PROXY_PASSWORD, STRING, "", MEDIUM, "Proxy password")
        .define(KEYSTORE, STRING, "", MEDIUM, "Keystore")
        .define(KEYSTORE_PASSWORD, PASSWORD, "", MEDIUM, "Keystore password")
        ;
  }
}
