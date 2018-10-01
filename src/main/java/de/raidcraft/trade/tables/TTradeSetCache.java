package de.raidcraft.trade.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mdoering
 */
@Entity
@Getter
@Setter
@Table(name = "rc_trade_tradeset_cache")
public class TTradeSetCache {

    @Id
    private int id;
    private String tradeSet;
    private Timestamp lastUpdate;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cache_id")
    private List<TTradeSetCacheItem> items = new ArrayList<>();
}
