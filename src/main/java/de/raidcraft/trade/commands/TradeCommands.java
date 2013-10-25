package de.raidcraft.trade.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.RaidCraft;
import de.raidcraft.api.items.CustomItemException;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.sales.SimpleCustomItemOffer;
import de.raidcraft.trade.api.window.NpcTradeWindow;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.trade.api.partner.SimplePlayerTradePartner;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class TradeCommands {

    private TradePlugin plugin;

    public TradeCommands(TradePlugin plugin) {

        this.plugin = plugin;
    }

    @Command(
            aliases = {"trade"},
            desc = "Trade commands"
    )
    @NestedCommand(value = NestedCommands.class)
    public void trade(CommandContext args, CommandSender sender) throws CommandException {
    }

    public static class NestedCommands {

        private final TradePlugin plugin;

        public NestedCommands(TradePlugin plugin) {

            this.plugin = plugin;
        }

        @Command(
                aliases = {"reload"},
                desc = "Plugin reload"
        )
        @CommandPermissions("rctrade.cmd.reload")
        public void reload(CommandContext args, CommandSender sender) throws CommandException {

            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "RCTrade wurde neugeladen!");
        }

        @Command(
                aliases = {"test"},
                desc = "Test command"
        )
        @CommandPermissions("rctrade.cmd.test")
        public void info(CommandContext args, CommandSender sender) throws CommandException {

            Player player = (Player)sender;
            PlayerTradePartner playerTradePartner = new SimplePlayerTradePartner(player);
            NpcTradeWindow tradeWindow = new NpcTradeWindow(playerTradePartner);
            try {
                tradeWindow.addOffer(new SimpleCustomItemOffer(123.42, RaidCraft.getCustomItemStack(43)));
                tradeWindow.addOffer(new SimpleCustomItemOffer(0.42, RaidCraft.getCustomItemStack(816)));
                tradeWindow.addOffer(new SimpleCustomItemOffer(13.42, RaidCraft.getCustomItemStack(768)));
                tradeWindow.addOffer(new SimpleCustomItemOffer(46.00, RaidCraft.getCustomItemStack(864)));
                tradeWindow.addOffer(new SimpleCustomItemOffer(2.00, RaidCraft.getCustomItemStack(1196)));
            } catch (CustomItemException e) {
                RaidCraft.LOGGER.info("RCTRade: " + e.getMessage());
            }
            tradeWindow.open();
        }
    }
}
