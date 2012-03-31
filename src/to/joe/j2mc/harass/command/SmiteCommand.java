package to.joe.j2mc.harass.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.exceptions.BadPlayerMatchException;
import to.joe.j2mc.harass.J2MC_Harass;

public class SmiteCommand extends MasterCommand {

    public SmiteCommand(J2MC_Harass harass) {
        super(harass);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "I can't kill anyone if you don't tell me whom");
            return;
        }
        Player target = null;
        try {
            target = J2MC_Manager.getVisibility().getPlayer(args[0], null);
        } catch (final BadPlayerMatchException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        final boolean weather = target.getWorld().isThundering();
        //J2MC_Manager.getPermissions().setPermissions("j2mc.damage.take", true);
        //this.j2.damage.addToTimer(target.getName());
        target.getWorld().strikeLightningEffect(target.getLocation());
        target.damage(20);
        target.setFireTicks(200);
        J2MC_Manager.getCore().adminAndLog(ChatColor.RED + sender.getName() + " has zapped " + target.getName());
        target.sendMessage(ChatColor.RED + "You have been judged");
        target.getWorld().setStorm(weather);
    }
}
