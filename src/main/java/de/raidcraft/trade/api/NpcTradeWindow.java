package de.raidcraft.trade.api;

import de.raidcraft.RaidCraft;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
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

    private void sell(ItemStack itemStack, int slotNumber) {

//        Economy economy = RaidCraft.getEconomy();
//        double price = itemStack.getItem().getSellPrice() * itemStack.getAmount();
//
//        economy.add(partner.getPlayer().getName(), price, BalanceSource.TRADE, "Item " + itemStack.getAmount() + "x" + itemStack.getItem().getName() + " verkauft");
        inventory.setItem(slotNumber, new ItemStack(Material.AIR));
        RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().addSale(itemStack, partner.getPlayer());
        refreshSaleHistory();
    }

    private void refreshSaleHistory() {

        List<SoldItem> soldItems = RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().getSales(partner.getPlayer());
        int slotNumber = 45;
        for(SoldItem soldItem : soldItems) {
            ItemStack itemStack = soldItem.getItemStack();

//            CustomItemStack customItemStack = RaidCraft.getCustomItem(itemStack);
//            if(customItemStack == null) {
//                //TODO delete sold item
//                continue;
//            }
//            customItemStack.setTooltip(new SingleLineTooltip(TooltipSlot.NAME, "Verkauft am " + soldItem.getDate()));
            inventory.setItem(slotNumber, itemStack);

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
//        else if(event.getRawSlot() > 53 && CustomItemUtil.isCustomItem(event.getCurrentItem())) {
//            sell(RaidCraft.getCustomItem(event.getCurrentItem()), event.getSlot());
//        }

        else if(event.getRawSlot() > 53) {
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
