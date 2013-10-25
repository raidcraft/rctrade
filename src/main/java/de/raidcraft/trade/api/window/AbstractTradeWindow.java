package de.raidcraft.trade.api.window;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

/**
 * @author Philip Urban
 */
public abstract class AbstractTradeWindow implements TradeWindow {

    protected Inventory inventory;

    @Override
    public void close() {

        for(HumanEntity entity : inventory.getViewers()) {
            entity.closeInventory();
        }
    }
}
