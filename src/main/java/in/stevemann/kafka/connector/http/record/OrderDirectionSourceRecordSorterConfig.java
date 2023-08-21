package in.stevemann.kafka.connector.http.record;

import lombok.Getter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;
import static org.apache.kafka.common.config.ConfigDef.Type.STRING;

@Getter
public class OrderDirectionSourceRecordSorterConfig extends AbstractConfig {

  private static final String ORDER_DIRECTION = "http.response.list.order.direction";

  private final OrderDirectionSourceRecordSorter.OrderDirection orderDirection;

  OrderDirectionSourceRecordSorterConfig(Map<String, ?> originals) {
    super(config(), originals);
    orderDirection = OrderDirectionSourceRecordSorter.OrderDirection.valueOf(getString(ORDER_DIRECTION).toUpperCase());
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(ORDER_DIRECTION, STRING, "IMPLICIT", LOW, "Order direction of the results in the list, either ASC, DESC or IMPLICIT");
  }
}

