package de.raidcraft.trade.api.sales;

import org.bukkit.inventory.ItemStack;

/**
 * @author Philip Urban
 */
public interface Offer {

    public ItemStack getItemStack();

    public double getPrice();

    public String getPriceString();
}
