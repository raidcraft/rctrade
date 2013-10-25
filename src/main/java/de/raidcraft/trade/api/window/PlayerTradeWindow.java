package de.raidcraft.trade.api.window;

import de.raidcraft.RaidCraft;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Philip Urban
 */
public class PlayerTradeWindow extends AbstractTradeWindow implements Listener {

    private static final Integer[] BLOCKED_SLOTS = {4, 13, 22, 31, 40, 49};

    private PlayerTradePartner leftPartner;
    private PlayerTradePartner rightPartner;

    public PlayerTradeWindow(PlayerTradePartner leftPartner, PlayerTradePartner rightPartner) {

        this.leftPartner = leftPartner;
        this.rightPartner = rightPartner;

        inventory = Bukkit.createInventory(null, 54, "Spielerhandel");
        ItemStack separator = new ItemStack(Material.PUMPKIN_STEM);
        separator.getItemMeta().setDisplayName("");
        inventory.setItem(4, separator);
        inventory.setItem(13, separator.clone());
        inventory.setItem(22, separator.clone());
        inventory.setItem(31, separator.clone());
        inventory.setItem(40, separator.clone());
        inventory.setItem(49, separator.clone());

        RaidCraft.getComponent(TradePlugin.class).registerEvents(this);
    }

    @Override
    public void open() {

        leftPartner.getPlayer().openInventory(inventory);
        rightPartner.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!event.getInventory().equals(inventory)) return;

        // cancel separator clicks
        if(Arrays.asList(BLOCKED_SLOTS).contains(event.getSlot())) {
            event.setCancelled(true);
            return;
        }

        // register button clicks
        //TODO
    }
}
