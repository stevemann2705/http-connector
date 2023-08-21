package in.stevemann.kafka.connector.http.request.template;

import in.stevemann.kafka.connector.http.model.HttpRequest;
import in.stevemann.kafka.connector.http.model.Offset;
import in.stevemann.kafka.connector.http.request.HttpRequestFactory;

import java.util.Map;

import static in.stevemann.kafka.connector.http.utils.ConfigUtils.breakDownHeaders;
import static in.stevemann.kafka.connector.http.utils.ConfigUtils.breakDownQueryParams;

public class TemplateHttpRequestFactory implements HttpRequestFactory {

  private String method;

  private Template urlTpl;

  private Template headersTpl;

  private Template queryParamsTpl;

  private Template bodyTpl;

  @Override
  public void configure(Map<String, ?> configs) {
    TemplateHttpRequestFactoryConfig config = new TemplateHttpRequestFactoryConfig(configs);
    TemplateFactory templateFactory = config.getTemplateFactory();

    method = config.getMethod();
    urlTpl = templateFactory.create(config.getUrl());
    headersTpl = templateFactory.create(config.getHeaders());
    queryParamsTpl = templateFactory.create(config.getQueryParams());
    bodyTpl = templateFactory.create(config.getBody());
  }

  @Override
  public HttpRequest createRequest(Offset offset) {
    return HttpRequest.builder()
        .method(HttpRequest.HttpMethod.valueOf(method))
        .url(urlTpl.apply(offset))
        .headers(breakDownHeaders(headersTpl.apply(offset)))
        .queryParams(breakDownQueryParams(queryParamsTpl.apply(offset)))
        .body(bodyTpl.apply(offset).getBytes())
        .build();
  }
}