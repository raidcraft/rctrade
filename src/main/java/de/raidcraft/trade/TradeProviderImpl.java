package de.raidcraft.trade;

import de.raidcraft.api.trades.TradeProvider;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
public class TradeProviderImpl implements TradeProvider {

    private TradePlugin plugin;

    public TradeProviderImpl(TradePlugin plugin) {

        this.plugin = plugin;
    }

    @Override
    public void registerTradeSet(String tradeSetName, ConfigurationSection tradeSetConfig) {

        plugin.getTradesManager().registerTradeSet(tradeSetName, tradeSetConfig);
    }

    @Override
    public boolean tradeSetExists(String tradeSetName) {

        return plugin.getTradesManager().getTradeSet(tradeSetName) != null;
    }
}
