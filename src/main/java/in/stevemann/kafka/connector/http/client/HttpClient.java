package in.stevemann.kafka.connector.http.client;

import in.stevemann.kafka.connector.http.model.HttpRequest;
import in.stevemann.kafka.connector.http.model.HttpResponse;
import org.apache.kafka.common.Configurable;

import java.io.IOException;
import java.util.Map;

@FunctionalInterface
public interface HttpClient extends Configurable {

  HttpResponse execute(HttpRequest request) throws IOException;

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}

