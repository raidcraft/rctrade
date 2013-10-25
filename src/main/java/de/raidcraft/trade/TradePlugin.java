package de.raidcraft.trade;

import de.raidcraft.api.BasePlugin;
import de.raidcraft.trade.commands.TradeCommands;

/**
 * @author Silthus
 */
public class TradePlugin extends BasePlugin {

    @Override
    public void enable() {

        registerCommands(TradeCommands.class);
    }

    @Override
    public void disable() {
    }
}
