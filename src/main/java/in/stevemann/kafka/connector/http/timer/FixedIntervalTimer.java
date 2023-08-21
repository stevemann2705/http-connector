package in.stevemann.kafka.connector.http.timer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.Math.max;
import static java.lang.System.currentTimeMillis;

@Slf4j
public class FixedIntervalTimer implements Timer {

  private final Function<Map<String, ?>, FixedIntervalTimerConfig> configFactory;

  @Getter
  private Long intervalMillis;

  private Long lastPollMillis;

  public FixedIntervalTimer() {
    this(FixedIntervalTimerConfig::new);
  }

  FixedIntervalTimer(Function<Map<String, ?>, FixedIntervalTimerConfig> configFactory) {
    this(configFactory, System::currentTimeMillis);
  }

  FixedIntervalTimer(Function<Map<String, ?>, FixedIntervalTimerConfig> configFactory, Supplier<Long> lastPollMillisInitializer) {
    this.configFactory = configFactory;
    this.lastPollMillis = lastPollMillisInitializer.get();
  }

  @Override
  public void configure(Map<String, ?> settings) {
    FixedIntervalTimerConfig config = configFactory.apply(settings);
    intervalMillis = config.getPollIntervalMillis();
  }

  @Override
  public Long getRemainingMillis() {
    long now = currentTimeMillis();
    long sinceLastMillis = now - lastPollMillis;
    return max(intervalMillis - sinceLastMillis, 0);
  }

  @Override
  public void reset(Instant lastZero) {
    lastPollMillis = currentTimeMillis();
  }
}