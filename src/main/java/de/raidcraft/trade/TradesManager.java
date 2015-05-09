package de.raidcraft.trade;

import de.raidcraft.api.config.SimpleConfiguration;
import de.raidcraft.api.random.RDS;
import de.raidcraft.api.random.RDSTable;
import de.raidcraft.trade.tradesets.ConfigTradeSet;
import de.raidcraft.trade.api.offers.TradeSet;
import de.raidcraft.trade.tradesets.RandomTradeSet;
import de.raidcraft.util.CaseInsensitiveMap;
import de.raidcraft.util.ConfigUtil;
import de.raidcraft.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Map;
import java.util.Optional;

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

        long interval = TimeUtil.secondsToTicks(plugin.getConfiguration().trade_refresh_interval);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> tradeSets.values().forEach(TradeSet::reloadOffers), interval, interval);
    }

    public void reload() {

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
                ConfigurationSection configurationSection = plugin.configure(new SimpleConfiguration<>(plugin, file));
                registerTradeSet(name, configurationSection);
            }
        }
    }

    public void registerTradeSet(String name, ConfigurationSection config) {

        String type = config.getString("type", "config");
        switch (type) {
            case "random":
                Optional<RDSTable> table = RDS.getTable(config.getString("loot-table"));
                if (table.isPresent()) {
                    tradeSets.put(name, new RandomTradeSet(name, config, table.get()));
                } else {
                    plugin.getLogger().warning("loot-table " + config.getString("loot-table")
                            + " in random trade set " + ConfigUtil.getFileName(config) + " does not exist!");
                }
                break;
            case "config":
            default:
                tradeSets.put(name, new ConfigTradeSet(name, config));
                break;
        }
    }

    public TradeSet getTradeSet(String tradeSetName) {

        return tradeSets.get(tradeSetName);
    }
}
