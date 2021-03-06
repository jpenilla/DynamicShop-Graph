package xyz.jpenilla.dsgraph.config;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class GraphConfig {
    @Getter
    private final String name;
    @Getter
    private final StockConfig stockConfig;
    @Getter
    private final int graphLengthMinutes;
    @Getter
    private final int graphRefreshTimeSeconds;
    @Getter
    private final GraphType type;

    public GraphConfig(String name, StockConfig config, int length, GraphType type, int refreshTime) {
        this.name = name;
        this.stockConfig = config;
        this.graphLengthMinutes = length;
        this.graphRefreshTimeSeconds = refreshTime;
        this.type = type;
    }

    public enum GraphType {
        PRICE,
        STOCK,
        MEDIAN
    }
}
