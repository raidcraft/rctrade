package de.raidcraft.trade;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.Setting;
import de.raidcraft.trade.commands.TradeCommands;
import de.raidcraft.trade.tables.TSaleLog;
import de.raidcraft.trade.tables.TSoldItem;
import de.raidcraft.trade.tables.TTradeSetCache;
import de.raidcraft.trade.tables.TTradeSetCacheItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Silthus
 */
public class TradePlugin extends BasePlugin {

    private SaleHistoryManager saleHistoryManager;
    private TradesManager tradeConfigManager;
    private TradeProviderImpl tradeProvider;
    private LocalConfiguration configuration;

    @Override
    public void enable() {

        registerCommands(TradeCommands.class);

        configuration = configure(new LocalConfiguration(this));
        saleHistoryManager = new SaleHistoryManager(this);
        tradeConfigManager = new TradesManager(this);
        tradeProvider = new TradeProviderImpl(this);

        RaidCraft.setupTradeProvider(tradeProvider);

        // ActionManager.registerAction(new OpenTradeAction());
    }

    @Override
    public void disable() {
    }

    public static class LocalConfiguration extends ConfigurationBase<TradePlugin> {

        @Setting("repair-cost.poor")
        public double poor_repair_cost = 0.1;
        @Setting("repair-cost.common")
        public double common_repair_cost = 0.15;
        @Setting("repair-cost.uncommon")
        public double uncommon_repair_cost = 0.2;
        @Setting("repair-cost.rare")
        public double rare_repair_cost = 0.25;
        @Setting("repair-cost.epic")
        public double epic_repair_cost = 0.5;
        @Setting("trade-refresh-interval")
        public double trade_refresh_interval = 60;

        public LocalConfiguration(TradePlugin plugin) {

            super(plugin, "config.yml");
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {

        List<Class<?>> databases = new ArrayList<>();
        databases.add(TSoldItem.class);
        databases.add(TSaleLog.class);
        databases.add(TTradeSetCache.class);
        databases.add(TTradeSetCacheItem.class);
        return databases;
    }

    public SaleHistoryManager getSaleHistoryManager() {

        return saleHistoryManager;
    }

    public TradesManager getTradesManager() {

        return tradeConfigManager;
    }

    public LocalConfiguration getConfiguration() {

        return configuration;
    }
}
