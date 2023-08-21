package in.stevemann.kafka.connector.http.record;

import in.stevemann.kafka.connector.http.model.Offset;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.source.SourceRecord;

import java.time.Instant;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class OffsetTimestampRecordFilterFactory implements SourceRecordFilterFactory {
  @Override
  public Predicate<SourceRecord> create(Offset offset) {
    Long offsetTimestampMillis = offset.getTimestamp().map(Instant::toEpochMilli).orElse(0L);
    return record -> record.timestamp() > offsetTimestampMillis;
  }
}
