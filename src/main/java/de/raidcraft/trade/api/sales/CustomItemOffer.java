package de.raidcraft.trade.api.sales;

import de.raidcraft.api.items.CustomItemStack;

/**
 * @author Philip Urban
 */
public interface CustomItemOffer extends Offer {

    public CustomItemStack getCustomItemStack();
}
