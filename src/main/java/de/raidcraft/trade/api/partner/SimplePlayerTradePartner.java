package de.raidcraft.trade.api.partner;

import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class SimplePlayerTradePartner implements PlayerTradePartner {

    private Player player;

    public SimplePlayerTradePartner(Player player) {

        this.player = player;
    }

    @Override
    public Player getPlayer() {

        return player;
    }

    @Override
    public void accept() {

        //TODO: implement
    }
}
