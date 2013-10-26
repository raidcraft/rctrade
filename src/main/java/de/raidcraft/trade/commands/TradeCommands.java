package de.raidcraft.trade.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.api.offers.TradeSet;
import de.raidcraft.trade.api.partner.PlayerTradePartner;
import de.raidcraft.trade.api.partner.SimplePlayerTradePartner;
import de.raidcraft.trade.api.window.NpcTradeWindow;
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
                aliases = {"open"},
                desc = "Opens an existing configured trade",
                min = 1,
                usage = "<Trade Set>"
        )
        @CommandPermissions("rctrade.cmd.open")
        public void info(CommandContext args, CommandSender sender) throws CommandException {

            Player player = (Player)sender;
            PlayerTradePartner playerTradePartner = new SimplePlayerTradePartner(player);
            TradeSet tradeSet = plugin.getTradesManager().getTradeSet(args.getString(0));
            if(tradeSet == null) {
                throw new CommandException("Es gibt kein Trade-Set mit diesem Name!");
            }
            NpcTradeWindow tradeWindow = new NpcTradeWindow(playerTradePartner, tradeSet);
            tradeWindow.open();
        }
    }
}
