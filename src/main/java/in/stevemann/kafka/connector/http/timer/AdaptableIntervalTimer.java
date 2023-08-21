package in.stevemann.kafka.connector.http.timer;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import static java.time.Instant.EPOCH;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MILLIS;

public class AdaptableIntervalTimer implements Timer {

  private final Function<Map<String, ?>, AdaptableIntervalTimerConfig> configFactory;

  private FixedIntervalTimer tailTimer;

  private FixedIntervalTimer catchupTimer;

  private Long intervalMillis;

  private Instant previousLatest = EPOCH;

  private Instant currentLatest = EPOCH.plus(1, MILLIS);

  public AdaptableIntervalTimer() {
    this(AdaptableIntervalTimerConfig::new);
  }

  public AdaptableIntervalTimer(Function<Map<String, ?>, AdaptableIntervalTimerConfig> configFactory) {
    this.configFactory = configFactory;
  }

  @Override
  public void configure(Map<String, ?> settings) {
    AdaptableIntervalTimerConfig config = configFactory.apply(settings);
    tailTimer = config.getTailTimer();
    catchupTimer = config.getCatchupTimer();
    intervalMillis = config.getTailTimer().getIntervalMillis();
  }

  @Override
  public void reset(Instant lastZero) {
    tailTimer.reset(lastZero);
    catchupTimer.reset(lastZero);
    previousLatest = currentLatest;
    currentLatest = lastZero;
  }

  @Override
  public Long getRemainingMillis() {
    return resolveTimer().getRemainingMillis();
  }

  private Timer resolveTimer() {
    return isCatchingUp() ? catchupTimer : tailTimer;
  }

  private boolean isCatchingUp() {
    boolean thereWereNewItems = !previousLatest.equals(currentLatest);
    boolean longAgoSinceLastItem = currentLatest.isBefore(now().minus(intervalMillis, MILLIS));
    return thereWereNewItems && longAgoSinceLastItem;
  }
}