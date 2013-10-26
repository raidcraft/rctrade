package de.raidcraft.trade.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rcconversations.api.action.AbstractAction;
import de.raidcraft.rcconversations.api.action.ActionArgumentList;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.rcconversations.api.action.WrongArgumentValueException;
import de.raidcraft.rcconversations.api.conversation.Conversation;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.offers.TradeSet;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.trade.api.partner.SimplePlayerTradePartner;
import de.raidcraft.trade.api.window.NpcTradeWindow;

/**
 * @author Philip Urban
 */
@ActionInformation(name = "OPEN_TRADE")
public class OpenTradeAction extends AbstractAction {

    @Override
    public void run(Conversation conversation, ActionArgumentList actionArgumentList) throws RaidCraftException {

        String tradeSetName = actionArgumentList.getString("trade");
        TradeSet tradeSet = RaidCraft.getComponent(TradePlugin.class).getTradesManager().getTradeSet(tradeSetName);
        if(tradeSet == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': Trade Set '" + tradeSetName + "' does not exists!");
        }

        PlayerTradePartner playerTradePartner = new SimplePlayerTradePartner(conversation.getPlayer());
        NpcTradeWindow tradeWindow = new NpcTradeWindow(playerTradePartner, tradeSet);
        tradeWindow.open();
    }
}
