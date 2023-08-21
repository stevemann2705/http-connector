package in.stevemann.kafka.connector.http.record;

import in.stevemann.kafka.connector.http.model.Offset;
import org.apache.kafka.common.Configurable;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.Map;
import java.util.function.Predicate;

@FunctionalInterface
public interface SourceRecordFilterFactory extends Configurable {

  Predicate<SourceRecord> create(Offset offset);

  default void configure(Map<String, ?> map) {
    // Do nothing
  }
}
