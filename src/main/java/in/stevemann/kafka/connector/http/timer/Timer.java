package in.stevemann.kafka.connector.http.timer;

import org.apache.kafka.common.Configurable;

import java.time.Instant;
import java.util.Map;

@FunctionalInterface
public interface Timer extends Configurable {

  Long getRemainingMillis();

  default void reset(Instant lastZero) {
    // Do nothing
  }

  @Override
  default void configure(Map<String, ?> configs) {
    // Do nothing
  }
}
