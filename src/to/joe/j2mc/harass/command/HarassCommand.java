package to.joe.j2mc.harass.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.exceptions.BadPlayerMatchException;
import to.joe.j2mc.harass.J2MC_Harass;

public class HarassCommand extends MasterCommand {

	public HarassCommand(J2MC_Harass harass){
		super(harass);
	}
	
	@Override
	public void exec(CommandSender sender, String commandName, String[] args,
			Player player, boolean isPlayer) {
		
		if(!isPlayer || player.hasPermission("j2mc.harass.abuse")){
            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "Missing a name!");
                return;
            }
            Player target = null;
			try {
				target = J2MC_Manager.getVisibility().getPlayer(args[0], player);
			} catch (BadPlayerMatchException e) {
				player.sendMessage(ChatColor.RED + e.getMessage());
				return;
			}
            CraftualHarassmentPanda methods = new CraftualHarassmentPanda();
            if (!methods.panda(target)) {
            	methods.harass(target.getName());
            	J2MC_Manager.getCore().adminAndLog(ChatColor.AQUA + "[HARASS] Target Acquired: " + ChatColor.DARK_AQUA + target.getName() + ChatColor.AQUA + ". Thanks, " + player.getName() + "!");
                //this.j2.irc.messageAdmins("[HARASS] Target Acquired: " + target.getName() + ". Thanks, " + playerName + "!");
            } else {
                methods.remove(target.getName());
                J2MC_Manager.getCore().adminAndLog(ChatColor.AQUA + "[HARASS] Target Removed: " + ChatColor.DARK_AQUA + target.getName() + ChatColor.AQUA + ". Thanks, " + player.getName() + "!");
                //this.j2.irc.messageAdmins("[HARASS] Target Removed: " + target.getName() + ". Thanks, " + playerName + "!");
            }
		}
	}


}
