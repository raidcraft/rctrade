package de.raidcraft.trade.tradesets;

import de.raidcraft.trade.api.offers.AbstractTradeSet;
import de.raidcraft.trade.api.offers.Offer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class SimpleTradeSet extends AbstractTradeSet {

    private List<Offer> offers = new ArrayList<>();

    public SimpleTradeSet(String name) {

        super(name);
    }

    public void addOffer(Offer offer) {

        offers.add(offer);
    }

    @Override
    public List<Offer> getOffers() {

        return offers;
    }
}
