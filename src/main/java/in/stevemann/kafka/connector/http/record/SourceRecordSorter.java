package in.stevemann.kafka.connector.http.record;

import org.apache.kafka.common.Configurable;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface SourceRecordSorter extends Configurable {

  List<SourceRecord> sort(List<SourceRecord> records);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}
