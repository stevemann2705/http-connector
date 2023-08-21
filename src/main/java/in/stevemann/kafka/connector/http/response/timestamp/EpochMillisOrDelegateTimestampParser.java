package in.stevemann.kafka.connector.http.response.timestamp;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Long.parseLong;

@RequiredArgsConstructor
public class EpochMillisOrDelegateTimestampParser implements TimestampParser {

  private final Function<Map<String, ?>, EpochMillisOrDelegateTimestampParserConfig> configFactory;

  private TimestampParser delegate;

  public EpochMillisOrDelegateTimestampParser() {
    this(EpochMillisOrDelegateTimestampParserConfig::new);
  }

  @Override
  public void configure(Map<String, ?> settings) {
    EpochMillisOrDelegateTimestampParserConfig config = configFactory.apply(settings);
    delegate = config.getDelegateParser();
  }

  @Override
  public Instant parse(String timestamp) {
    try {
      return Instant.ofEpochMilli(parseLong(timestamp));
    } catch (NumberFormatException e) {
      return delegate.parse(timestamp);
    }
  }
}
