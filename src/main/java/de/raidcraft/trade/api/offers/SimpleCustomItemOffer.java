package de.raidcraft.trade.api.offers;

import de.raidcraft.api.items.CustomItemStack;

/**
 * @author Philip Urban
 */
public class SimpleCustomItemOffer extends AbstractOffer implements CustomItemOffer {

    private CustomItemStack customItemStack;

    public SimpleCustomItemOffer(double price, CustomItemStack customItemStack) {

        super(price, customItemStack);
        this.customItemStack = customItemStack;
    }

    @Override
    public CustomItemStack getCustomItemStack() {

        return customItemStack;
    }
}
