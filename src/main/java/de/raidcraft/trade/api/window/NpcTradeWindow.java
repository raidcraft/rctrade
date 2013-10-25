package de.raidcraft.trade.api.window;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.api.items.tooltip.FixedMultilineTooltip;
import de.raidcraft.api.items.tooltip.TooltipSlot;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.SoldItem;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.trade.api.sales.CustomItemOffer;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class NpcTradeWindow extends AbstractTradeWindow implements Listener {

    private PlayerTradePartner partner;
    private List<CustomItemOffer> offerList = new ArrayList<>();

    public NpcTradeWindow(PlayerTradePartner partner) {

        this.partner = partner;

        inventory = Bukkit.createInventory(null, 54, "Händler");
        ItemStack separator = new ItemStack(Material.PUMPKIN_STEM);
        ItemMeta itemMeta = separator.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_GRAY + "Oben Verkauf | Unten Rückkauf");
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
        if(customItemStack == null) {
            partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Es können nur Sepzial-Items verkauft werden!");
            return;
        }

        Economy economy = RaidCraft.getEconomy();
        double price = customItemStack.getItem().getSellPrice() * itemStack.getAmount();

        if(price == 0) {
            partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Dieses Item ist nichts Wert!");
            return;
        }

        economy.add(partner.getPlayer().getName(), price,
                BalanceSource.TRADE, "Verkauf von " + itemStack.getAmount() + "x" + customItemStack.getItem().getName());

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
                    partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Du hast für den Rückkauf keinen freien Slot im Inventar!");
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
                economy.substract(partner.getPlayer().getName(), price,
                        BalanceSource.TRADE, "Rückkauf von " + itemStack.getAmount() + "x" + customItemStack.getItem().getName());
                //refresh history
                RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().removeSale(soldItem.getDatabaseId());
                refreshSaleHistory();
                // give item
                Inventory playerInventory = partner.getPlayer().getInventory();
                playerInventory.setItem(playerInventory.firstEmpty(), customItemStack);
                break;
            }
            currentSlot++;
        }
    }

    private void buy(int slotNumber) {

        int slotCount = 0;
        for(CustomItemOffer offer : offerList) {

            if(slotCount == slotNumber) {
                // check if player inventory has empty slot
                if(partner.getPlayer().getInventory().firstEmpty() == -1) {
                    partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Du hast für den Kauf keinen freien Slot im Inventar!");
                    break;
                }
                // take money
                Economy economy = RaidCraft.getEconomy();
                economy.substract(partner.getPlayer().getName(), offer.getPrice(),
                        BalanceSource.TRADE, "Kauf von " + offer.getCustomItemStack().getAmount() + "x" + offer.getCustomItemStack().getItem().getName());
                // give item
                Inventory playerInventory = partner.getPlayer().getInventory();
                playerInventory.setItem(playerInventory.firstEmpty(), offer.getCustomItemStack().clone());
                break;
            }

            slotCount++;
            if(slotCount > 35) {
                break;
            }
        }
    }

    private void refreshSaleHistory() {

        List<SoldItem> soldItems = RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().getSales(partner.getPlayer());
        for(int i = 45; i <= 53; i++) {
            if(soldItems.size() < i - 44) {
                inventory.setItem(i, null);
                continue;
            }
            SoldItem soldItem = soldItems.get(i - 45);
            ItemStack itemStack = soldItem.getItemStack();
            CustomItemStack customItemStack = RaidCraft.getCustomItem(itemStack);
            if(customItemStack == null) {
                RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().removeSale(soldItem.getDatabaseId());
                continue;
            }
            customItemStack.setTooltip(new FixedMultilineTooltip(TooltipSlot.MISC,
                    ChatColor.DARK_PURPLE + "Verkauft am " + soldItem.getDate(),
                    ChatColor.LIGHT_PURPLE + "Klicken um Verkauf rückgängig zu machen!"));
            customItemStack.rebuild();
            inventory.setItem(i, customItemStack);
        }
    }

    private void refreshOffers() {

        int slotCount = 0;
        for(CustomItemOffer offer : offerList) {

            CustomItemStack displayItem = offer.getCustomItemStack().clone();
            displayItem.setTooltip(new FixedMultilineTooltip(TooltipSlot.MISC,
                    ChatColor.DARK_GRAY + "-----------------------------",
                    ChatColor.DARK_RED + "Kaufpreis: " + offer.getPriceString(),
                    ChatColor.LIGHT_PURPLE + "Item anklicken um es zu kaufen!"));
            displayItem.rebuild();
            inventory.setItem(slotCount, displayItem);

            slotCount++;
            if(slotCount > 35) {
                break;
            }
        }
    }

    public void addOffer(CustomItemOffer offer) {

        offerList.add(offer);
    }

    @Override
    public void open() {

        partner.getPlayer().openInventory(inventory);
        refreshSaleHistory();
        refreshOffers();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!event.getInventory().getViewers().contains(partner.getPlayer())) return;

        // buy item from npc
        if(event.getRawSlot() <= 35) {
            buy(event.getSlot());
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
        //TODO: add new implemented handlers!
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }
}
