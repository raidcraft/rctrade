package de.raidcraft.trade.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.offers.TradeSet;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.trade.api.partner.SimplePlayerTradePartner;
import de.raidcraft.trade.api.window.NpcTradeWindow;
import de.raidcraft.util.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
@ActionInformation(name = "OPEN_TRADE")
public class OpenTradeAction implements Action<Player> {

    @Override
    @Information(
            value = "trade.open",
            desc = "Opens the given trade for the player",
            conf = "trade: trade id to open",
            aliases = {"open-trade", "opentrade"}
    )
    public void accept(Player player, ConfigurationSection config) {

        String tradeSetName = config.getString("trade");
        TradeSet tradeSet = RaidCraft.getComponent(TradePlugin.class).getTradesManager().getTradeSet(tradeSetName);
        if(tradeSet == null) {
            RaidCraft.LOGGER.warning("invalid trade set " + tradeSetName + " in " + ConfigUtil.getFileName(config));
            return;
        }

        PlayerTradePartner playerTradePartner = new SimplePlayerTradePartner(player);
        NpcTradeWindow tradeWindow = new NpcTradeWindow(playerTradePartner, tradeSet);
        tradeWindow.open();
    }
}
