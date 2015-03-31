package de.raidcraft.trade.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author mdoering
 */
@Getter
@Setter
@Entity
@Table(name = "rctrade_sales_log")
public class TSaleLog {

    private int id;
    private UUID uuid;
    private int soldItemId;
    private String item;
    private int amount;
    private double price;
    private String action;
    private Timestamp timestamp;
}
