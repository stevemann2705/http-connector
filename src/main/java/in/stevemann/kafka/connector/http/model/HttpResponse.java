package in.stevemann.kafka.connector.http.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

@With
@Value
@Builder
public class HttpResponse {

  Integer code;

  byte[] body;

  @Builder.Default
  Map<String, List<String>> headers = emptyMap();
}

