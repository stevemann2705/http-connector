package in.stevemann.kafka.connector.http.response.jackson;

import com.fasterxml.jackson.core.JsonPointer;
import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.core.JsonPointer.compile;
import static in.stevemann.kafka.connector.http.utils.ConfigUtils.breakDownList;
import static in.stevemann.kafka.connector.http.utils.ConfigUtils.breakDownMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Importance.MEDIUM;
import static org.apache.kafka.common.config.ConfigDef.Type.STRING;

@Getter
public class JacksonRecordParserConfig extends AbstractConfig {

  private static final String LIST_POINTER = "http.response.list.pointer";
  private static final String ITEM_POINTER = "http.response.record.pointer";
  private static final String ITEM_KEY_POINTER = "http.response.record.key.pointer";
  private static final String ITEM_TIMESTAMP_POINTER = "http.response.record.timestamp.pointer";
  private static final String ITEM_OFFSET_VALUE_POINTER = "http.response.record.offset.pointer";

  private final JsonPointer recordsPointer;
  private final List<JsonPointer> keyPointer;
  private final JsonPointer valuePointer;
  private final Optional<JsonPointer> timestampPointer;
  private final Map<String, JsonPointer> offsetPointers;

  JacksonRecordParserConfig(Map<String, ?> originals) {
    super(config(), originals);
    recordsPointer = compile(getString(LIST_POINTER));
    keyPointer = breakDownList(ofNullable(getString(ITEM_KEY_POINTER)).orElse("")).stream().map(JsonPointer::compile).collect(Collectors.toList());
    valuePointer = compile(getString(ITEM_POINTER));
    timestampPointer = ofNullable(getString(ITEM_TIMESTAMP_POINTER)).map(JsonPointer::compile);
    offsetPointers = breakDownMap(getString(ITEM_OFFSET_VALUE_POINTER)).entrySet().stream()
        .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), compile(entry.getValue())))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(LIST_POINTER, STRING, "/", HIGH, "Item List JsonPointer")
        .define(ITEM_POINTER, STRING, "/", HIGH, "Item JsonPointer")
        .define(ITEM_KEY_POINTER, STRING, null, HIGH, "Item Key JsonPointers")
        .define(ITEM_TIMESTAMP_POINTER, STRING, null, MEDIUM, "Item Timestamp JsonPointer")
        .define(ITEM_OFFSET_VALUE_POINTER, STRING, "", MEDIUM, "Item Offset JsonPointers");
  }
}
