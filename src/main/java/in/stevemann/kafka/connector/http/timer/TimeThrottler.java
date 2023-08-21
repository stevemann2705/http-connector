package in.stevemann.kafka.connector.http.timer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class TimeThrottler implements Throttler {

  @Getter
  private final Timer timer;

  private final Sleeper sleeper;

  public TimeThrottler(Timer timer) {
    this(timer, Thread::sleep);
  }

  @Override
  public void throttle() throws InterruptedException {
    Long remainingMillis = timer.getRemainingMillis();
    if (remainingMillis > 0)
      sleeper.sleep(remainingMillis);
  }

  public void throttle(Instant lastZero) throws InterruptedException {
    timer.reset(lastZero);
    throttle();
  }
}
