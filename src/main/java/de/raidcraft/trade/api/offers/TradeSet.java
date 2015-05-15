package de.raidcraft.trade.api.offers;

import java.util.List;

/**
 * @author Philip Urban
 */
public interface TradeSet {

    String getName();

    void reloadOffers();

    List<Offer> getOffers();

    boolean isPurchasing();

    boolean isRepairing();

    String getWindowName();
}
