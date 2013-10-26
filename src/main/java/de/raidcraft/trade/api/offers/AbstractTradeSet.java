package de.raidcraft.trade.api.offers;

/**
 * @author Philip Urban
 */
public abstract class AbstractTradeSet implements TradeSet {

    private String name;

    protected AbstractTradeSet(String name) {

        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }
}
