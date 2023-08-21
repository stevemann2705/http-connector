package in.stevemann.kafka.connector.http.response.timestamp;

import org.apache.kafka.common.Configurable;

import java.time.Instant;
import java.util.Map;

@FunctionalInterface
public interface TimestampParser extends Configurable {

  Instant parse(String timestamp);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}

