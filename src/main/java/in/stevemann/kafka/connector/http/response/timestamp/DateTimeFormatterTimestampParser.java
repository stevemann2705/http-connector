package in.stevemann.kafka.connector.http.response.timestamp;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class DateTimeFormatterTimestampParser implements TimestampParser {

  private final Function<Map<String, ?>, DateTimeFormatterTimestampParserConfig> configFactory;

  private DateTimeFormatter timestampFormatter;

  public DateTimeFormatterTimestampParser() {
    this(DateTimeFormatterTimestampParserConfig::new);
  }

  @Override
  public void configure(Map<String, ?> settings) {
    timestampFormatter = configFactory.apply(settings).getRecordTimestampFormatter();
  }

  @Override
  public Instant parse(String timestamp) {
    return OffsetDateTime.parse(timestamp, timestampFormatter).toInstant();
  }
}