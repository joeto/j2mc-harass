package to.joe.j2mc.harass.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.event.MessageEvent;
import to.joe.j2mc.core.exceptions.BadPlayerMatchException;
import to.joe.j2mc.harass.J2MC_Harass;

public class HarassCommand extends MasterCommand<J2MC_Harass> {

    public HarassCommand(J2MC_Harass harass) {
        super(harass);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "Missing a name!");
            return;
        }
        Player target = null;
        try {
            target = J2MC_Manager.getVisibility().getPlayer(args[0], null);
        } catch (final BadPlayerMatchException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }
        String message;
        if (!this.plugin.isHarassed(target)) {
            this.plugin.harass(target.getName());
            message = ChatColor.AQUA + "[HARASS] Target Acquired: " + ChatColor.DARK_AQUA + target.getName() + ChatColor.AQUA + ". Thanks, " + sender.getName() + "!";
        } else {
            this.plugin.remove(target.getName());
            message = ChatColor.AQUA + "[HARASS] Target Removed: " + ChatColor.DARK_AQUA + target.getName() + ChatColor.AQUA + ". Thanks, " + sender.getName() + "!";
        }
        J2MC_Manager.getCore().adminAndLog(message);
        this.plugin.getServer().getPluginManager().callEvent(new MessageEvent(MessageEvent.compile("ADMININFO"), message));
    }

}
