package de.raidcraft.trade.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.api.items.tooltip.SingleLineTooltip;
import de.raidcraft.api.items.tooltip.TooltipSlot;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
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

    private void sell(ItemStack itemStack, int slotNumber) {

        CustomItemStack customItemStack = RaidCraft.getCustomItem(itemStack);
        if(customItemStack == null) return;

        Economy economy = RaidCraft.getEconomy();
        double price = customItemStack.getItem().getSellPrice() * itemStack.getAmount();
        economy.add(partner.getPlayer().getName(), price, BalanceSource.TRADE, "Item " + itemStack.getAmount() + "x" + customItemStack.getItem().getName() + " verkauft");

        partner.getPlayer().getInventory().setItem(slotNumber, new ItemStack(Material.AIR));
        partner.getPlayer().updateInventory();

        RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().addSale(itemStack, partner.getPlayer());
        refreshSaleHistory();
    }

    private void rebuy(int slotNumber) {

        int rebuySlot = slotNumber - 45;

        int currentSlot = 0;
        List<SoldItem> soldItems = RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().getSales(partner.getPlayer());
        for(SoldItem soldItem : soldItems) {
            if(currentSlot == rebuySlot) {
                // check if player inventory has empty slot
                if(partner.getPlayer().getInventory().firstEmpty() == -1) {
                    partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Du hast keinen freien Slot im Inventar für den Rückkauf!");
                    break;
                }
                // create custom item
                ItemStack itemStack = soldItem.getItemStack();
                CustomItemStack customItemStack = RaidCraft.getCustomItem(itemStack);
                if(customItemStack == null) {
                    RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().removeSale(soldItem.getDatabaseId());
                    continue;
                }
                // take money
                Economy economy = RaidCraft.getEconomy();
                double price = customItemStack.getItem().getSellPrice() * itemStack.getAmount();
                economy.substract(partner.getPlayer().getName(), price, BalanceSource.TRADE, "Rückkauf von " + itemStack.getAmount() + "x" + customItemStack.getItem().getName());
                //refresh history
                refreshSaleHistory();
                // give item
                Inventory playerInventory = partner.getPlayer().getInventory();
                playerInventory.setItem(playerInventory.firstEmpty(), customItemStack);
                break;
            }
            currentSlot++;
        }
    }

    private void refreshSaleHistory() {

        List<SoldItem> soldItems = RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().getSales(partner.getPlayer());
        int slotNumber = 45;
        for(SoldItem soldItem : soldItems) {
            ItemStack itemStack = soldItem.getItemStack();
            CustomItemStack customItemStack = RaidCraft.getCustomItem(itemStack);
            if(customItemStack == null) {
                RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().removeSale(soldItem.getDatabaseId());
                continue;
            }
            customItemStack.setTooltip(new SingleLineTooltip(TooltipSlot.NAME, ChatColor.DARK_PURPLE + "Verkauft am " + soldItem.getDate()));
            customItemStack.setTooltip(new SingleLineTooltip(TooltipSlot.NAME, ChatColor.LIGHT_PURPLE + "Klicken um Verkauf rückgängig zu machen!"));
            inventory.setItem(slotNumber, customItemStack);

            slotNumber++;
            if(slotNumber > 53) break;
        }
    }

    @Override
    public void open() {

        partner.getPlayer().openInventory(inventory);
        refreshSaleHistory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!event.getInventory().getViewers().contains(partner.getPlayer())) return;

        // buy item from npc
        if(event.getRawSlot() <= 35) {

        }

        // undo last sell/buy
        else if(event.getRawSlot() > 44 && event.getRawSlot() < 54) {
            rebuy(event.getSlot());
        }

        else if(event.getRawSlot() > 53 && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            sell(event.getCurrentItem(), event.getSlot());
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
