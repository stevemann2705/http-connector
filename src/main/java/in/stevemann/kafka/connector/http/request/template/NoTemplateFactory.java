package in.stevemann.kafka.connector.http.request.template;

public class NoTemplateFactory implements TemplateFactory {
  @Override
  public Template create(String template) {
    return offset -> template;
  }
}