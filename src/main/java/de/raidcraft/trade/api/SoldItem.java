package de.raidcraft.trade.api;

import de.raidcraft.api.items.CustomItemStack;

import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
public class SoldItem {

    private String player;
    private CustomItemStack itemStack;
    private int databaseId;
    private Timestamp date;

    public SoldItem(String player, CustomItemStack itemStack, int databaseId, Timestamp date) {

        this.player = player;
        this.itemStack = itemStack;
        this.databaseId = databaseId;
        this.date = date;
    }

    public String getPlayer() {

        return player;
    }

    public CustomItemStack getItemStack() {

        return itemStack;
    }

    public int getDatabaseId() {

        return databaseId;
    }

    public Timestamp getDate() {

        return date;
    }
}
