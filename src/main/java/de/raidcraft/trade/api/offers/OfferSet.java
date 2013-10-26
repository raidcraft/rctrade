package de.raidcraft.trade.api.offers;

import java.util.List;

/**
 * @author Philip Urban
 */
public interface OfferSet {

    public String getName();

    public List<Offer> getOffers();
}
