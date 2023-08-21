package in.stevemann.kafka.connector.http.response.jackson.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Map;

import static java.util.Collections.emptyMap;

@With
@Value
@Builder
public class JacksonRecord {

  /**
   * @deprecated To be integrated in offset
   */
  @Deprecated
  String key;

  /**
   * @deprecated To be integrated in offset
   */
  @Deprecated
  String timestamp;

  @Builder.Default
  Map<String, Object> offset = emptyMap();

  String body;
}

