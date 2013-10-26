package de.raidcraft.trade;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.trade.commands.TradeCommands;
import de.raidcraft.trade.tables.TSoldItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Silthus
 */
public class TradePlugin extends BasePlugin {

    private SaleHistoryManager saleHistoryManager;
    private TradesManager tradeConfigManager;
    private TradeProviderImpl tradeProvider;

    @Override
    public void enable() {

        registerCommands(TradeCommands.class);

        saleHistoryManager = new SaleHistoryManager(this);
        tradeConfigManager = new TradesManager(this);
        tradeProvider = new TradeProviderImpl(this);

        RaidCraft.setupTradeProvider(tradeProvider);
    }

    @Override
    public void disable() {
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {

        List<Class<?>> databases = new ArrayList<>();
        databases.add(TSoldItem.class);
        return databases;
    }

    public SaleHistoryManager getSaleHistoryManager() {

        return saleHistoryManager;
    }

    public TradesManager getTradesManager() {

        return tradeConfigManager;
    }
}
