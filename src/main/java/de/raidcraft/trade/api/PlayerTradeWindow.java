package de.raidcraft.trade.api;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * @author Philip Urban
 */
public class PlayerTradeWindow extends AbstractTradeWindow implements Listener {

    public PlayerTradeWindow(TradePartner leftPartner, TradePartner rightPartner) {

        inventory = Bukkit.createInventory(null, 54, "Spielerhandel");
        ItemStack separator = new ItemStack(Material.PUMPKIN_STEM);
        separator.getItemMeta().setDisplayName("");
        inventory.setItem(4, separator);
        inventory.setItem(13, separator.clone());
        inventory.setItem(22, separator.clone());
        inventory.setItem(31, separator.clone());
        inventory.setItem(40, separator.clone());
        inventory.setItem(49, separator.clone());
    }

    @Override
    public void open() {


    }
}
