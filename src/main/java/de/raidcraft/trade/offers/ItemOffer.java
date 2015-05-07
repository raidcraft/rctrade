package de.raidcraft.trade.offers;

import de.raidcraft.RaidCraft;
import de.raidcraft.trade.api.offers.Offer;
import org.bukkit.inventory.ItemStack;

/**
 * @author Philip Urban
 */
public class ItemOffer implements Offer {

    private double price;
    private ItemStack itemStack;

    public ItemOffer(double price, ItemStack itemStack) {

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
