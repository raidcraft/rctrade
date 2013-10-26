package de.raidcraft.trade.api.offers;

import de.raidcraft.api.items.CustomItemStack;

/**
 * @author Philip Urban
 */
public interface CustomItemOffer extends Offer {

    public CustomItemStack getCustomItemStack();
}
