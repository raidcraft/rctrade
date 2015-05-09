package de.raidcraft.trade.api.offers;

import java.util.List;

/**
 * @author Philip Urban
 */
public interface TradeSet {

    public String getName();

    public void reloadOffers();

    public List<Offer> getOffers();

    public boolean isPurchasing();

    public boolean isRepairing();

    public String getWindowName();
}
