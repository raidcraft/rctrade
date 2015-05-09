package de.raidcraft.trade.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mdoering
 */
@Entity
@Getter
@Setter
@Table(name = "rctrade_tradeset_cache")
public class TTradeSetCache {

    @Id
    private int id;
    private String tradeSet;
    private Timestamp lastUpdate;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cache_id")
    private List<TTradeSetCacheItem> items = new ArrayList<>();
}
