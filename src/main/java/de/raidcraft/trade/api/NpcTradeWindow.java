package de.raidcraft.trade.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.api.items.tooltip.SingleLineTooltip;
import de.raidcraft.api.items.tooltip.TooltipSlot;
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

import java.util.List;

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
        inventory.setItem(36, separator);
        inventory.setItem(37, separator.clone());
        inventory.setItem(38, separator.clone());
        inventory.setItem(39, separator.clone());
        inventory.setItem(40, separator.clone());
        inventory.setItem(41, separator.clone());
        inventory.setItem(42, separator.clone());
        inventory.setItem(43, separator.clone());
        inventory.setItem(44, separator.clone());

        RaidCraft.getComponent(TradePlugin.class).registerEvents(this);
    }

    private void sell(CustomItemStack customItemStack, int slotNumber) {

        Economy economy = RaidCraft.getEconomy();
        double price = customItemStack.getItem().getSellPrice() * customItemStack.getAmount();

        inventory.setItem(slotNumber, new ItemStack(Material.AIR));
        economy.add(partner.getPlayer().getName(), price, BalanceSource.TRADE, "Item " + customItemStack.getAmount() + "x" + customItemStack.getItem().getName() + " verkauft");
        RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().addSale(customItemStack, partner.getPlayer());
        refreshSaleHistory();
    }

    private void refreshSaleHistory() {

        List<SoldItem> soldItems = RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().getSales(partner.getPlayer());
        int slotNumber = 45;
        for(SoldItem soldItem : soldItems) {
            CustomItemStack customItemStack = soldItem.getItemStack();
            customItemStack.setTooltip(new SingleLineTooltip(TooltipSlot.NAME, "Verkauft am " + soldItem.getDate()));
            inventory.setItem(slotNumber, customItemStack);

            slotNumber++;
            if(slotNumber > 53) break;
        }
        partner.getPlayer().updateInventory();
    }

    @Override
    public void open() {

        partner.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!event.getInventory().getViewers().contains(partner.getPlayer())) return;

        // buy item from npc
        if(event.getRawSlot() <= 35) {

        }

        // undo last sell/buy
        else if(event.getRawSlot() > 44 && event.getRawSlot() < 54) {

        }

        // sell owned custom item
        else if(event.getRawSlot() > 53 && CustomItemUtil.isCustomItem(event.getCurrentItem())) {
            sell(RaidCraft.getCustomItem(event.getCurrentItem()), event.getSlot());
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
