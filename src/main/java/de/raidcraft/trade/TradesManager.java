package de.raidcraft.trade;

import de.raidcraft.api.config.SimpleConfiguration;
import de.raidcraft.trade.api.offers.ConfigTradeSet;
import de.raidcraft.trade.api.offers.TradeSet;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class TradesManager {

    private static final String DEFAULT_CONFIG_DIR = "trades";

    private TradePlugin plugin;
    private Map<String, TradeSet> tradeSets = new CaseInsensitiveMap<>();

    public TradesManager(TradePlugin plugin) {

        this.plugin = plugin;
        load();
    }

    private void load() {

        loadConversations(DEFAULT_CONFIG_DIR);
    }

    public void loadConversations(String dirName) {

        File configFolder = new File(plugin.getDataFolder(), dirName);
        configFolder.mkdirs();
        loadConfig(configFolder);
    }

    private void loadConfig(File dir) {

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                // recursive loading of all sub directories
                loadConfig(file);
            }
            if (file.getName().endsWith(".yml")) {

                String name = file.getName().replace(".yml", "");
                ConfigurationSection configurationSection = plugin.configure(new SimpleConfiguration<>(plugin, file), false);
                registerTradeSet(name, configurationSection);
            }
        }
    }

    public void registerTradeSet(String name, ConfigurationSection config) {

        TradeSet tradeSet = new ConfigTradeSet(name, config);
        tradeSets.put(name, tradeSet);
    }

    public TradeSet getTradeSet(String tradeSetName) {

        return tradeSets.get(tradeSetName);
    }
}
