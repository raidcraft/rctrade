package de.raidcraft.trade.api.offers;

import java.util.List;

/**
 * @author Philip Urban
 */
public interface TradeSet {

    public String getName();

    public List<Offer> getOffers();

    public boolean doesPurchase();
}
