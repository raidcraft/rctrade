package de.raidcraft.trade.api.offers;

import org.bukkit.inventory.ItemStack;

/**
 * @author Philip Urban
 */
public interface Offer {

    ItemStack getItemStack();

    double getPrice();

    String getPriceString();
}
