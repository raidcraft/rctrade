package de.raidcraft.trade.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author mdoering
 */
@Entity
@Getter
@Setter
@Table(name = "rctrade_tradeset_cache_items")
public class TTradeSetCacheItem {

    @Id
    private int id;
    @ManyToOne
    private TTradeSetCache cache;
    private String item;
    private int amount;
    private double price;
}
