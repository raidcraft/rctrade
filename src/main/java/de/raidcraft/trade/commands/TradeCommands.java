package de.raidcraft.trade.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.trade.TradePlugin;
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
            tradeWindow.open();
        }
    }
}
