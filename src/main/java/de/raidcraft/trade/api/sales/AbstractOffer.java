package de.raidcraft.trade.api.sales;

import de.raidcraft.RaidCraft;
import org.bukkit.inventory.ItemStack;

/**
 * @author Philip Urban
 */
public abstract class AbstractOffer implements Offer {

    private double price;
    private ItemStack itemStack;

    protected AbstractOffer(double price, ItemStack itemStack) {

        this.price = price;
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItemStack() {

        return itemStack;
    }

    @Override
    public double getPrice() {

        return price;
    }

    @Override
    public String getPriceString() {

        return RaidCraft.getEconomy().getFormattedAmount(price);
    }
}
