package to.joe.j2mc.harass.command;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.exceptions.BadPlayerMatchException;
import to.joe.j2mc.harass.J2MC_Harass;

public class SlapCommand extends MasterCommand {

    public SlapCommand(J2MC_Harass harass) {
        super(harass);
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        float force = 0;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /slap player force");
        } else {
            if (args.length == 1) {
                force = 5;
            } else {
                force = new Float(args[1]);
                if (force > 100) {
                    force = 100;
                }
                if (force < 0) {
                    force = 5;
                }
            }
            Player target = null;
            try {
                target = J2MC_Manager.getVisibility().getPlayer(args[0], null);
            } catch (final BadPlayerMatchException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
                return;
            }
            final Random randomGen = new Random();
            final Vector newVelocity = new Vector(((randomGen.nextFloat() * 1.5) - 0.75) * force, (randomGen.nextFloat() / 2.5) + (0.4 * force), ((randomGen.nextFloat() * 1.5) - 0.75) * force);
            target.setVelocity(newVelocity);
            J2MC_Manager.getCore().adminAndLog(ChatColor.RED + sender.getName() + " slapped " + target.getName());
        }
    }

}
