package de.raidcraft.trade.tables;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
@Entity
@Table(name = "rctrade_sales")
public class TSoldItem {

    @Id
    private int id;
    private int storageId;
    private String player;
    private Timestamp date;
    private String world;

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getStorageId() {

        return storageId;
    }

    public void setStorageId(int storageId) {

        this.storageId = storageId;
    }

    public String getPlayer() {

        return player;
    }

    public void setPlayer(String player) {

        this.player = player;
    }

    public Timestamp getDate() {

        return date;
    }

    public void setDate(Timestamp date) {

        this.date = date;
    }

    public String getWorld() {

        return world;
    }

    public void setWorld(String world) {

        this.world = world;
    }
}
