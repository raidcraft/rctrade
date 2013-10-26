package de.raidcraft.trade.api.offers;

/**
 * @author Philip Urban
 */
public abstract class AbstractOfferSet implements OfferSet {

    private String name;

    protected AbstractOfferSet(String name) {

        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }
}
