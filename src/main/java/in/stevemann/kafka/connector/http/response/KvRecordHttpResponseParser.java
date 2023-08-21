package in.stevemann.kafka.connector.http.response;

import in.stevemann.kafka.connector.http.model.HttpResponse;
import in.stevemann.kafka.connector.http.record.model.KvRecord;
import org.apache.kafka.common.Configurable;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface KvRecordHttpResponseParser extends Configurable {

  List<KvRecord> parse(HttpResponse response);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}