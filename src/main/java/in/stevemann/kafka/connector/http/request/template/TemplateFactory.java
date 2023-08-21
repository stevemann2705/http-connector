package in.stevemann.kafka.connector.http.request.template;

@FunctionalInterface
public interface TemplateFactory {
  Template create(String template);
}

