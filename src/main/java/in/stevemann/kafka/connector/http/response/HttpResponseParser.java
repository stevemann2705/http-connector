package in.stevemann.kafka.connector.http.response;

import in.stevemann.kafka.connector.http.model.HttpResponse;
import org.apache.kafka.common.Configurable;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface HttpResponseParser extends Configurable {

  List<SourceRecord> parse(HttpResponse response);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}
