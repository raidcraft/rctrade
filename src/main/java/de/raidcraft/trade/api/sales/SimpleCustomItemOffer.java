package de.raidcraft.trade.api.sales;

import de.raidcraft.api.items.CustomItemStack;

/**
 * @author Philip Urban
 */
public class SimpleCustomItemOffer extends AbstractOffer implements CustomItemOffer {

    private CustomItemStack customItemStack;

    public SimpleCustomItemOffer(double price, CustomItemStack customItemStack) {

        super(price, customItemStack);
    }

    @Override
    public CustomItemStack getCustomItemStack() {

        return customItemStack;
    }
}
