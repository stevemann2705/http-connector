package in.stevemann.kafka.connector.http.record;

import in.stevemann.kafka.connector.http.model.Offset;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class OffsetRecordFilterFactory implements SourceRecordFilterFactory {

  private final SourceRecordFilterFactory delegate;

  public OffsetRecordFilterFactory() {
    this(new OffsetTimestampRecordFilterFactory());
  }

  @Override
  public Predicate<SourceRecord> create(Offset offset) {
    AtomicBoolean lastSeenReached = new AtomicBoolean(false);
    return delegate.create(offset).or(record -> {
      boolean result = lastSeenReached.get();
      if (!result && Offset.of(record.sourceOffset()).equals(offset)) {
        lastSeenReached.set(true);
      }
      return result;
    });
  }
}
