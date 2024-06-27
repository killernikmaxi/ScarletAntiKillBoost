package it.killernik.scarletantikillboost.cmds;

import it.killernik.scarletantikillboost.ScarletAntiKillBoost;
import it.killernik.scarletantikillboost.manager.ChecksManager;
import it.killernik.scarletantikillboost.manager.ConfigManager;
import it.killernik.scarletantikillboost.utils.MU;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCMD implements CommandExecutor, TabCompleter {

    private static final ConfigManager config = ScarletAntiKillBoost.get().getConfigManager();
    private static final ChecksManager checksManager = ScarletAntiKillBoost.get().getChecksManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("sakb.command")) {
            sender.sendMessage(MU.color("&4&lSCARLETANTIKILLBOOST"));
            sender.sendMessage(MU.color("&c&oPlugin for ScarletMC by @killernik <3"));
            return true;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "help":
                    config.getHelpMessages().forEach(message -> sender.sendMessage(MU.color(message)));
                    break;
                case "reload":
                    config.reloadConfig();
                    sender.sendMessage(MU.color(config.getConfigReloadedMessage()));
                    break;
                default:
                    sender.sendMessage(MU.color(config.getIncorrectSyntaxMessage()));
                    break;
            }
            return true;
        }

        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(MU.color(config.getSpecifyPlayerMessage()));
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "resetflags":
                    checksManager.setPlayerFlags(target, 0);
                    sender.sendMessage(MU.color(config.getFlagsResetMessage().replace("%player%", target.getName())));
                    break;
                case "viewflags":
                    int flags = checksManager.getPlayerFlags(target);
                    sender.sendMessage(MU.color(config.getViewFlagsMessage()
                            .replace("%player%", target.getName())
                            .replace("%flags%", String.valueOf(flags))));
                    break;
                default:
                    sender.sendMessage(MU.color(config.getIncorrectSyntaxMessage()));
                    break;
            }
            return true;
        }

        config.getHelpMessages().forEach(message -> sender.sendMessage(MU.color(message)));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("help");
            arguments.add("reload");
            arguments.add("viewflags");
            arguments.add("resetflags");
            return arguments;
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("resetflags") || args[0].equalsIgnoreCase("viewflags"))) {
            List<String> playerNames = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));
            return playerNames;
        }

        return Collections.emptyList();
    }
}
