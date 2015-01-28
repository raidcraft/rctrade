package de.raidcraft.trade.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Philip Urban
 */
@Getter
@Setter
@Entity
@Table(name = "rctrade_sales")
public class TSoldItem {

    @Id
    private int id;
    private int storageId;
    @Deprecated
    private String player;
    private UUID playerId;
    private Timestamp date;
    @Deprecated
    private String world;
    private UUID worldId;

}
