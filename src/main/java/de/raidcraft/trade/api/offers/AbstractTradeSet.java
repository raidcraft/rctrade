package de.raidcraft.trade.api.offers;

/**
 * @author Philip Urban
 */
public abstract class AbstractTradeSet implements TradeSet {

    private String name;
    private boolean purchase = true;
    private boolean repairing = false;
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

    protected void setRepairing(boolean repairing) {

        this.repairing = repairing;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean doesPurchase() {

        return purchase;
    }

    public boolean isRepairing() {

        return repairing;
    }

    public String getWindowName() {

        return windowName;
    }
}
