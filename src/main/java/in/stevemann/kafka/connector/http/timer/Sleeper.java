package in.stevemann.kafka.connector.http.timer;

@FunctionalInterface
public interface Sleeper {
  void sleep(Long milliseconds) throws InterruptedException;
}

