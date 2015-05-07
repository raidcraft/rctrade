package de.raidcraft.trade.tradesets;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.items.CustomItemException;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.trade.offers.ItemOffer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * @author Philip Urban
 */
public class ConfigTradeSet extends SimpleTradeSet {

    public ConfigTradeSet(String name, ConfigurationSection config) {

        super(name);

        ConfigurationSection settingsSection = config.getConfigurationSection("settings");
        if(settingsSection != null) {
            setPurchasing(settingsSection.getBoolean("purchase", true));
            setRepairing(settingsSection.getBoolean("repairing", false));
            setWindowName(settingsSection.getString("window-name", "Händler"));
        }

        ConfigurationSection offersSection = config.getConfigurationSection("offers");
        if(offersSection == null) return;
        for(String key : offersSection.getKeys(false)) {
            ConfigurationSection offerSection = offersSection.getConfigurationSection(key);
            String itemIdentifier = offerSection.getString("item");
            double price = offerSection.getDouble("price");
            int amount = offerSection.getInt("amount");
            try {
                ItemStack item = RaidCraft.getItem(itemIdentifier);
                if (item instanceof CustomItemStack) {
                    if(price != 0 && price < ((CustomItemStack) item).getItem().getSellPrice() * item.getAmount()) {
                        RaidCraft.LOGGER.warning("[RCTrade] Fehler in Trade Config '" + name + "'. Der Preis ist niedriger als der Item-Verkaufspreis (Item:" + itemIdentifier + ")");
                        continue;
                    }
                }
                item.setAmount(amount);
                addOffer(new ItemOffer(price, item));
            } catch (CustomItemException e) {
                e.printStackTrace();
            }
        }
    }
}
