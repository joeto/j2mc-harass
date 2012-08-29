package to.joe.j2mc.harass.command;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.PlayerInventory;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.core.exceptions.BadPlayerMatchException;
import to.joe.j2mc.harass.J2MC_Harass;

public class WoofCommand extends MasterCommand{
    
    J2MC_Harass plugin;
    
    public WoofCommand (J2MC_Harass harass){
        super(harass);
        this.plugin = harass;
    }
    
    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if(args.length > -1){
            sender.sendMessage(ChatColor.GRAY + "BARK BARK BARK BARK BARK!");
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /woof player");
            return;
        }
        Player target;
        try{
            target = J2MC_Manager.getVisibility().getPlayer(args[0], sender);
        } catch(BadPlayerMatchException e){
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }
        final PlayerInventory targetInventory = target.getInventory();
        target.setGameMode(GameMode.SURVIVAL);
        targetInventory.clear(36);
        targetInventory.clear(37);
        targetInventory.clear(38);
        targetInventory.clear(39);
        targetInventory.clear();
        final ArrayList<Wolf> wlist = new ArrayList<Wolf>();
        for (int x = 0; x < 10; x++) {
            final Wolf wolf = (Wolf) target.getWorld().spawnEntity(target.getLocation(), EntityType.WOLF);
            wlist.add(wolf);
            wolf.setAngry(true);
            wolf.setTarget(target);
            wolf.damage(0, target);
        }
        plugin.wolves.put(target.getName(), wlist);
    }

}
