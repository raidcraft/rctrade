package de.raidcraft.trade.api.offers;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
public class ConfigOfferSet extends SimpleOfferSet {

    public ConfigOfferSet(String name, ConfigurationSection config) {

        super(name);

        ConfigurationSection offersSection = config.getConfigurationSection("offers");
        if(offersSection == null) return;
        for(String key : offersSection.getKeys(false)) {
            ConfigurationSection
        }

        //TODO check if price is higher or equal sell price!
    }
}
