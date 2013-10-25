package de.raidcraft.trade.api.partner;

import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public interface PlayerTradePartner extends TradePartner {

    public Player getPlayer();
}
