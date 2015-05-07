package de.raidcraft.trade.api.offers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Philip Urban
 */
@Setter(AccessLevel.PROTECTED)
@Getter
public abstract class AbstractTradeSet implements TradeSet {

    private final String name;
    private boolean purchasing = true;
    private boolean repairing = false;
    private String windowName = "Händler";

    protected AbstractTradeSet(String name) {

        this.name = name;
    }
}
