package de.raidcraft.trade.api.offers;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.items.CustomItemException;
import de.raidcraft.api.items.CustomItemStack;
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
            setPurchase(settingsSection.getBoolean("purchase", true));
            setWindowName(settingsSection.getString("window-name", "Händler"));
        }

        ConfigurationSection offersSection = config.getConfigurationSection("offers");
        if(offersSection == null) return;
        for(String key : offersSection.getKeys(false)) {
            ConfigurationSection offerSection = offersSection.getConfigurationSection(key);
            String itemIdentifier = offerSection.getString("item");
            double price = offerSection.getDouble("price");
            int amount = offerSection.getInt("amount");
            // custom-item
            if(itemIdentifier.startsWith("rci")) {
                CustomItemStack customItemStack;
                try {
                    int customItemId = Integer.valueOf(itemIdentifier.substring(3));
                    customItemStack = RaidCraft.getCustomItemStack(customItemId);
                } catch (NumberFormatException | CustomItemException e) {

                    RaidCraft.LOGGER.warning("[RCTrade] Fehler in Trade Config '" + name + "'. Es gibt kein Custom-Item mit der ID:" + itemIdentifier.substring(3));
                    continue;
                }
                if(price != 0 && price < customItemStack.getItem().getSellPrice() * customItemStack.getAmount()) {
                    RaidCraft.LOGGER.warning("[RCTrade] Fehler in Trade Config '" + name + "'. Der Preis ist niedriger als der Item-Verkaufspreis (Item:" + customItemStack.getItem().getId() + ")");
                    continue;
                }
                customItemStack.setAmount(amount);
                addOffer(new SimpleCustomItemOffer(price, customItemStack));
                continue;
            }
            // vanilla-item
            else {
                ItemStack itemStack = RaidCraft.getUnsafeItem(itemIdentifier);
                if(itemStack == null) {
                    RaidCraft.LOGGER.warning("[RCTrade] Fehler in Trade Config '" + name + "'. Es gibt kein Vanilla-Item mit der ID:" + itemIdentifier);
                    continue;
                }
                itemStack.setAmount(amount);
                addOffer(new SimpleOffer(price, itemStack));
            }
        }
    }
}
