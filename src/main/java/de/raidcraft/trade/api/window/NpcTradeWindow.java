package de.raidcraft.trade.api.window;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.api.items.CustomItemException;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.api.items.tooltip.FixedMultilineTooltip;
import de.raidcraft.api.items.tooltip.TooltipSlot;
import de.raidcraft.api.language.Translator;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.SoldItem;
import de.raidcraft.trade.api.offers.CustomItemOffer;
import de.raidcraft.trade.api.offers.Offer;
import de.raidcraft.trade.api.offers.TradeSet;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.util.CustomItemUtil;
import de.raidcraft.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EntityEquipment;
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
    private TradeSet tradeSet;

    public NpcTradeWindow(PlayerTradePartner partner, TradeSet tradeSet) {

        this.partner = partner;
        this.tradeSet = tradeSet;

        inventory = Bukkit.createInventory(null, 54, tradeSet.getWindowName());
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

        if (tradeSet.isRepairing()) {
            Player player = partner.getPlayer();
            ItemStack itemStack = new ItemStack(Material.ANVIL);
            ItemMeta repairItemMeta = itemStack.getItemMeta();
            repairItemMeta.setDisplayName(ChatColor.GOLD + "Zum Reparieren aller Items anklicken.");
            ArrayList<String> repairItemLore = new ArrayList<>();
            repairItemLore.add(Translator.tr(TradePlugin.class, player, "trade.repair.items-needing-repair", "These items need to be repaired:"));
            repairItemLore.add(ChatColor.DARK_PURPLE + "--------------------------");
            // lets list what costs what to repair
            EntityEquipment equipment = player.getEquipment();
            double repairCost;
            for (ItemStack item : equipment.getArmorContents()) {
                repairCost = getRepairCost(item);
                if (repairCost > 0.0) {
                    CustomItemStack customItem = RaidCraft.getCustomItem(item);
                    repairItemLore.add(customItem.getItem().getQuality().getColor() + customItem.getItem().getName() + ": "
                            + ChatColor.RED + "-" + RaidCraft.getEconomy().getFormattedAmount(repairCost));
                }
            }
            // mainhand and offhand
            repairCost = getRepairCost(player.getInventory().getItem(CustomItemUtil.MAIN_WEAPON_SLOT));
            if (repairCost > 0.0) {
                CustomItemStack customItem = RaidCraft.getCustomItem(player.getInventory().getItem(CustomItemUtil.MAIN_WEAPON_SLOT));
                repairItemLore.add(customItem.getItem().getQuality().getColor() + customItem.getItem().getName() + ": "
                        + ChatColor.RED + "-" + RaidCraft.getEconomy().getFormattedAmount(repairCost));
            }
            repairCost = getRepairCost(player.getInventory().getItem(CustomItemUtil.OFFHAND_WEAPON_SLOT));
            if (repairCost > 0.0) {
                CustomItemStack customItem = RaidCraft.getCustomItem(player.getInventory().getItem(CustomItemUtil.OFFHAND_WEAPON_SLOT));
                repairItemLore.add(customItem.getItem().getQuality().getColor() + customItem.getItem().getName() + ": "
                        + ChatColor.RED + "-" + RaidCraft.getEconomy().getFormattedAmount(repairCost));
            }
            repairItemLore.add(ChatColor.RED + "Reparatur Kosten: " + ChatColor.RED + "-"
                    + RaidCraft.getEconomy().getFormattedAmount(getTotalRepairCost(player)));

            repairItemMeta.setLore(repairItemLore);
            itemStack.setItemMeta(repairItemMeta);
            inventory.setItem(35, itemStack);
        }

        RaidCraft.getComponent(TradePlugin.class).registerEvents(this);
    }

    private void sell(ItemStack itemStack, int slotNumber) {

        if(!tradeSet.doesPurchase()) {
            partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Dieser Händler kauft keine Items an!");
            return;
        }

        CustomItemStack customItemStack = RaidCraft.getCustomItem(itemStack);
        if(customItemStack == null) {
            partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Es können nur Spezial-Items verkauft werden!");
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

        // check if player inventory has empty slot
        if(partner.getPlayer().getInventory().firstEmpty() == -1) {
            partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Du hast für den Rückkauf keinen freien Slot im Inventar!");
            return;
        }

        int rebuySlot = slotNumber - 45;
        int currentSlot = 0;
        List<SoldItem> soldItems = RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().getSales(partner.getPlayer());
        for(SoldItem soldItem : soldItems) {
            if(currentSlot == rebuySlot) {
                // create custom item
                ItemStack itemStack = soldItem.getItemStack();
                CustomItemStack customItemStack = RaidCraft.getCustomItem(itemStack);
                if(customItemStack == null) {
                    RaidCraft.getComponent(TradePlugin.class).getSaleHistoryManager().removeSale(soldItem.getDatabaseId());
                    continue;
                }
                Economy economy = RaidCraft.getEconomy();
                double price = customItemStack.getItem().getSellPrice() * itemStack.getAmount();
                // check money
                if(!economy.hasEnough(partner.getPlayer().getName(), price)) {
                    partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Du hast nicht genügend Geld auf dem Konto!");
                    return;
                }
                // take money
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

        // check if player inventory has empty slot
        if(partner.getPlayer().getInventory().firstEmpty() == -1) {
            partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Du hast für den Kauf keinen freien Slot im Inventar!");
            return;
        }

        int slotCount = 0;
        for(Offer offer : tradeSet.getOffers()) {

            if(slotCount == slotNumber) {
                Economy economy = RaidCraft.getEconomy();
                // check money
                if(!economy.hasEnough(partner.getPlayer().getName(), offer.getPrice())) {
                    partner.getPlayer().sendMessage(ChatColor.DARK_RED + "Du hast nicht genügend Geld auf dem Konto!");
                    return;
                }
                // take money and give item
                Inventory playerInventory = partner.getPlayer().getInventory();
                String itemName;
                if(offer instanceof CustomItemOffer) {
                    CustomItemStack clonedCustomItemStack = ((CustomItemOffer) offer).getCustomItemStack().clone();
                    try {
                        clonedCustomItemStack.rebuild(partner.getPlayer());
                    } catch (CustomItemException ignored) {}
                    itemName = clonedCustomItemStack.getItem().getName();
                    playerInventory.addItem(clonedCustomItemStack);
                }
                else {
                    itemName = ItemUtils.getFriendlyName(offer.getItemStack().getType());
                    playerInventory.addItem(offer.getItemStack().clone());
                }
                economy.substract(partner.getPlayer().getName(), offer.getPrice(),
                        BalanceSource.TRADE, "Kauf von " + offer.getItemStack().getAmount() + "x" + itemName);
                break;
            }

            slotCount++;
            if(slotCount > 35) {
                break;
            }
        }
    }

    private void repair(Player player) {

        ItemStack[] armorContents = player.getEquipment().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            if (CustomItemUtil.isArmor(armorContents[i])) {
                armorContents[i] = repairItem(player, armorContents[i]);
            }
        }
        player.getInventory().setArmorContents(armorContents);
        // lets repair the main and offhand weapon
        player.getInventory().setItem(CustomItemUtil.MAIN_WEAPON_SLOT, repairItem(player, player.getInventory().getItem(CustomItemUtil.MAIN_WEAPON_SLOT)));
        player.getInventory().setItem(CustomItemUtil.OFFHAND_WEAPON_SLOT, repairItem(player, player.getInventory().getItem(CustomItemUtil.OFFHAND_WEAPON_SLOT)));
    }

    private ItemStack repairItem(Player player, ItemStack itemStack) {

        if (!CustomItemUtil.isCustomItem(itemStack)) {
            return itemStack;
        }
        CustomItemStack customItem = RaidCraft.getCustomItem(itemStack);
        double balance = RaidCraft.getEconomy().getBalance(player.getName());
        double repairCost = getRepairCost(customItem);
        if (balance - repairCost < 0) {
            player.sendMessage(ChatColor.RED + "Du hast nicht genügend Geld um deine Items zu reparieren.");
        } else {
            try {
                customItem.setCustomDurability(customItem.getMaxDurability());
                customItem.rebuild(player);
                RaidCraft.getEconomy().substract(player.getName(), repairCost, BalanceSource.REPAIR_ITEM, "Reparatur von " + customItem.getItem().getName());
            } catch (CustomItemException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
            }
        }
        return customItem;
    }

    private double getTotalRepairCost(Player player) {

        double totalRepairCost = 0.0;
        // add all equiped armor items to the cost
        for (ItemStack armorContent : player.getEquipment().getArmorContents()) {
            if (CustomItemUtil.isArmor(armorContent)) {
                totalRepairCost += getRepairCost(armorContent);
            }
        }
        // add main- and offhand weapons
        totalRepairCost += getRepairCost(player.getInventory().getItem(CustomItemUtil.MAIN_WEAPON_SLOT));
        totalRepairCost += getRepairCost(player.getInventory().getItem(CustomItemUtil.OFFHAND_WEAPON_SLOT));
        return totalRepairCost;
    }

    private double getRepairCost(ItemStack itemStack) {

        if (!CustomItemUtil.isCustomItem(itemStack)) {
            return 0;
        }
        TradePlugin.LocalConfiguration config = RaidCraft.getComponent(TradePlugin.class).getConfiguration();
        CustomItemStack customItem = RaidCraft.getCustomItem(itemStack);
        if(customItem == null) { return config.epic_repair_cost; }
        int missingDurability = customItem.getMaxDurability() - customItem.getCustomDurability();
        double repairCost = 0.0;
        if (missingDurability > 0) {
            repairCost = missingDurability * (customItem.getItem().getItemLevel() - 32.5);
            if (repairCost < 0) repairCost = 1.0;
            switch (customItem.getItem().getQuality()) {

                case POOR:
                    repairCost *= config.poor_repair_cost;
                    break;
                case COMMON:
                    repairCost *= config.common_repair_cost;
                    break;
                case UNCOMMON:
                    repairCost *= config.uncommon_repair_cost;
                    break;
                case RARE:
                    repairCost *= config.rare_repair_cost;
                    break;
                case EPIC:
                    repairCost *= config.epic_repair_cost;
                    break;
            }
        }
        return repairCost;
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
                    ChatColor.DARK_GRAY + "--------------------------",
                    ChatColor.DARK_PURPLE + "Verkauft am " + soldItem.getDate(),
                    ChatColor.LIGHT_PURPLE + "Klicken um Verkauf rückgängig zu machen!"));
            try {
                customItemStack.rebuild(partner.getPlayer());
            } catch (CustomItemException ignored) {}
            inventory.setItem(i, customItemStack);
        }
    }

    private void refreshOffers() {

        int slotCount = 0;
        for(Offer offer : tradeSet.getOffers()) {

            if(offer instanceof CustomItemOffer) {
                CustomItemStack displayItem = ((CustomItemOffer)offer).getCustomItemStack().clone();
                displayItem.setTooltip(new FixedMultilineTooltip(TooltipSlot.MISC,
                        ChatColor.DARK_GRAY + "--------------------------",
                        ChatColor.DARK_PURPLE + "Kaufpreis: " + offer.getPriceString(),
                        ChatColor.LIGHT_PURPLE + "Item anklicken um es zu kaufen!"));
                try {
                    displayItem.rebuild(partner.getPlayer());
                } catch (CustomItemException ignored) {}
                inventory.setItem(slotCount, displayItem);
            }
            else {
                ItemStack displayItem = offer.getItemStack().clone();
                ItemMeta itemMeta = displayItem.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.DARK_GRAY + "--------------------------");
                lore.add(ChatColor.DARK_PURPLE + "Kaufpreis: " + offer.getPriceString());
                lore.add(ChatColor.LIGHT_PURPLE + "Item anklicken um es zu kaufen!");
                itemMeta.setLore(lore);
                displayItem.setItemMeta(itemMeta);
                inventory.setItem(slotCount, displayItem);
            }

            slotCount++;
            if(slotCount > 35) {
                break;
            }
        }
        partner.getPlayer().updateInventory();
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
            if (tradeSet.isRepairing() && event.getRawSlot() == 35) {
                repair((Player) event.getWhoClicked());
            } else {
                buy(event.getSlot());
            }
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