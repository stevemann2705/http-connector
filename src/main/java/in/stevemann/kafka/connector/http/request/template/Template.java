package in.stevemann.kafka.connector.http.request.template;

import in.stevemann.kafka.connector.http.model.Offset;

@FunctionalInterface
public interface Template {
  String apply(Offset offset);
}
