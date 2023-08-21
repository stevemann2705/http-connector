package in.stevemann.kafka.connector.http.request.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.SneakyThrows;
import lombok.Value;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import static java.util.UUID.randomUUID;

public class FreeMarkerTemplateFactory implements TemplateFactory {

  private final Configuration configuration = new Configuration(new Version(2, 3, 32)) {{
    setNumberFormat("computer");
  }};

  @Override
  public Template create(String template) {
    return offset -> apply(createTemplate(template), new TemplateModel(offset.toMap()));
  }

  @SneakyThrows(IOException.class)
  private freemarker.template.Template createTemplate(String template) {
    return new freemarker.template.Template(randomUUID().toString(), new StringReader(template), configuration);
  }

  @SneakyThrows({TemplateException.class, IOException.class})
  private String apply(freemarker.template.Template template, TemplateModel model) {
    Writer writer = new StringWriter();
    template.process(model, writer);
    return writer.toString();
  }

  @Value
  public static class TemplateModel {
    Map<String, ?> offset;
  }
}
