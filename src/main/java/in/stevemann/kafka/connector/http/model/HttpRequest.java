package in.stevemann.kafka.connector.http.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

import static in.stevemann.kafka.connector.http.model.HttpRequest.HttpMethod.GET;
import static java.util.Collections.emptyMap;

@Value
@Builder
public class HttpRequest {

  @Builder.Default
  HttpMethod method = GET;

  String url;

  @Builder.Default
  Map<String, List<String>> queryParams = emptyMap();

  @Builder.Default
  Map<String, List<String>> headers = emptyMap();

  byte[] body;

  public enum HttpMethod {
    GET, HEAD, POST, PUT, PATCH
  }
}

