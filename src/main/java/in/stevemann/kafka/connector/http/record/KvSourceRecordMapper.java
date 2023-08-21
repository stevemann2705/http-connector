package in.stevemann.kafka.connector.http.record;

import in.stevemann.kafka.connector.http.record.model.KvRecord;
import org.apache.kafka.common.Configurable;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.Map;

@FunctionalInterface
public interface KvSourceRecordMapper extends Configurable {

  SourceRecord map(KvRecord record);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}
