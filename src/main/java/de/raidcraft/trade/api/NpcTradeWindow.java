package de.raidcraft.trade.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.util.CustomItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Philip Urban
 */
public class NpcTradeWindow extends AbstractTradeWindow implements Listener {

    private PlayerTradePartner partner;

    public NpcTradeWindow(PlayerTradePartner partner) {

        this.partner = partner;

        inventory = Bukkit.createInventory(null, 54, "Händler");
        ItemStack separator = new ItemStack(Material.PUMPKIN_STEM);
        separator.getItemMeta().setDisplayName("");
        inventory.setItem(27, separator);
        inventory.setItem(28, separator.clone());
        inventory.setItem(29, separator.clone());
        inventory.setItem(30, separator.clone());
        inventory.setItem(31, separator.clone());
        inventory.setItem(32, separator.clone());
        inventory.setItem(33, separator.clone());
        inventory.setItem(34, separator.clone());
        inventory.setItem(35, separator.clone());

        RaidCraft.getComponent(TradePlugin.class).registerEvents(this);
    }

    @Override
    public void open() {

        partner.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!event.getInventory().equals(inventory)) return;

        // buy item from npc
        if(event.getSlot() <= 27) {

        }

        // undo last sell/buy
        else if(event.getSlot() > 35 && event.getSlot() < 54) {

        }

        // sell owned custom item
        else if(event.getSlot() > 53 && CustomItemUtil.isCustomItem(event.getCurrentItem())) {

        }

        RaidCraft.LOGGER.info("DEBUG: Clicked Slot: " + event.getSlot());
        event.setCancelled(true);
    }
}
