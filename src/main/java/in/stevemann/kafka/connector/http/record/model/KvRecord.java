package in.stevemann.kafka.connector.http.record.model;

import in.stevemann.kafka.connector.http.model.Offset;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
public class KvRecord {

  String key;

  String value;

  Offset offset;
}
