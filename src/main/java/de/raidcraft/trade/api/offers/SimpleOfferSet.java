package de.raidcraft.trade.api.offers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class SimpleOfferSet extends AbstractOfferSet {

    private List<Offer> offers = new ArrayList<>();

    public SimpleOfferSet(String name) {

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
