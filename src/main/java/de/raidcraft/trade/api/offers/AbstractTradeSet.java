package de.raidcraft.trade.api.offers;

/**
 * @author Philip Urban
 */
public abstract class AbstractTradeSet implements TradeSet {

    private String name;
    private boolean purchase = true;
    private String windowName = "Händler";

    protected AbstractTradeSet(String name) {

        this.name = name;
    }

    protected void setPurchase(boolean purchase) {

        this.purchase = purchase;
    }

    protected void setWindowName(String windowName) {

        this.windowName = windowName;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean doesPurchase() {

        return purchase;
    }

    public String getWindowName() {

        return windowName;
    }
}
