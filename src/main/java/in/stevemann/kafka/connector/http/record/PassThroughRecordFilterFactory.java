package in.stevemann.kafka.connector.http.record;

import in.stevemann.kafka.connector.http.model.Offset;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.function.Predicate;

public class PassThroughRecordFilterFactory implements SourceRecordFilterFactory {

  @Override
  public Predicate<SourceRecord> create(Offset offset) {
    return __ -> true;
  }
}

