package in.stevemann.kafka.connector.http.timer;

@FunctionalInterface
public interface Throttler {
  void throttle() throws InterruptedException;
}
