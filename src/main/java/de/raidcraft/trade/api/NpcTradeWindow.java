package de.raidcraft.trade.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.util.CustomItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Philip Urban
 */
public class NpcTradeWindow extends AbstractTradeWindow implements Listener {

    private PlayerTradePartner partner;

    public NpcTradeWindow(PlayerTradePartner partner) {

        this.partner = partner;

        inventory = Bukkit.createInventory(null, 54, "Händler");
        ItemStack separator = new ItemStack(Material.PUMPKIN_STEM);
        ItemMeta itemMeta = separator.getItemMeta();
        itemMeta.setDisplayName("~");
        separator.setItemMeta(itemMeta);
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

    private void sell(CustomItemStack item, int slotNumber) {

        Economy economy = RaidCraft.getEconomy();
        double price = item.getItem().getSellPrice();

        if(item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        }
        else {
            inventory.setItem(slotNumber, null);
        }
        economy.add(partner.getPlayer().getName(), price, BalanceSource.TRADE, "Item '" + item.getItem().getName() + "' verkauft");
        //TODO add to sell history
    }

    @Override
    public void open() {

        partner.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!event.getInventory().getViewers().contains(partner.getPlayer())) return;

        // buy item from npc
        if(event.getRawSlot() <= 27) {

        }

        // undo last sell/buy
        else if(event.getRawSlot() > 35 && event.getRawSlot() < 54) {

        }

        // sell owned custom item
        else if(event.getRawSlot() > 53 && CustomItemUtil.isCustomItem(event.getCurrentItem())) {
            sell(RaidCraft.getCustomItem(event.getCurrentItem()), event.getRawSlot());
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if(!event.getInventory().getViewers().contains(partner.getPlayer())) return;

        //XXX important to unregister all listener!!!
        //TODO: add new implemented listeners!
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }
}
