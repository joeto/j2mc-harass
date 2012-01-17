package to.joe.j2mc.harass.command;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.server.Block;
import net.minecraft.server.EntityChicken;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet60Explosion;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.harass.J2MC_Harass;

public class CraftualHarassmentPanda {
	
	private Random random = new Random();
    private final Object sync = new Object();
    String[] pandaLines;

	
    /**
     * Query for if the player is being harassed
     * 
     * @param player
     * @return true if on harassment list
     */
    public boolean panda(Player player) {
        synchronized (this.sync) {
            return J2MC_Harass.harassees.contains(player.getName().toLowerCase());
        }
    }
    
    /**
     * LEAVE BRITTANY ALONE! Er, uh, removes player from harassment list
     * 
     * @param name
     */
    public void remove(String name) {
        synchronized (this.sync) {
        	J2MC_Harass.harassees.remove(name.toLowerCase());
        }
    }
    
    /**
     * Adds player to harassment list
     * 
     * @param player
     */
    public void harass(String name) {
        synchronized (this.sync) {
        	J2MC_Harass.harassees.add(name.toLowerCase());
        }
    }
	
    /**
     * Read named file
     * 
     * @param filename
     * @return array of lines
     */
    public String[] readDaFile(String filename) {

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filename);
        } catch (final FileNotFoundException e2) {
            // e2.printStackTrace();
        	J2MC_Manager.getLog().severe("File not found: " + filename);
            final String[] uhOh = new String[1];
            uhOh[0] = "";
            return uhOh;
        }
        final BufferedReader rulesBuffer = new BufferedReader(fileReader);
        final List<String> fileLines = new ArrayList<String>();
        String line = null;
        try {
            while ((line = rulesBuffer.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
        try {
            rulesBuffer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return fileLines.toArray(new String[fileLines.size()]);
    }
	
    
    /**
     * Reset system.
     */
    public void restartManager() {
        //this.harassees = new ArrayList<String>();
        try {
            this.pandaLines = this.readDaFile("panda.txt");
        } catch (final Exception e) {
            this.pandaLines = new String[1];
            this.pandaLines[0] = "ololol imma griefer ban me plz";
        }
    }
    
    /**
     * Triggers on block damage. If player is being harassed, tell their client
     * it's now sponge.
     * 
     * @param player
     *            Player
     * @param location
     *            Location of the block
     * @return true if it's safe to allow the block break, false if harassed
     */
    public boolean blockHurt(Player player, Location location) {
        if (!this.panda(player)) {
            return true;
        }
        player.sendBlockChange(location, Material.SPONGE, (byte) 0);
        return false;
    }
    
    /**
     * Triggers on block placement. If player is being harassed, tell their
     * client it's now a chicken and explode.
     * 
     * @param player
     *            Player
     * @param location
     *            Location of new block
     * @return true if safe to allow, false if harassed
     */
    public boolean blockPlace(Player player, Location location) {
        if (!this.panda(player)) {
            return true;
        }
        final Inventory i = player.getInventory();
        i.remove(player.getItemInHand().getType());
        final EntityChicken bawk = new EntityChicken(((CraftWorld) player.getWorld()).getHandle());
        bawk.setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
        final Packet24MobSpawn pack1 = new Packet24MobSpawn(bawk);
        final Packet60Explosion pack2 = new Packet60Explosion(location.getX(), location.getY(), location.getZ(), 10, new HashSet<Block>());
        ((CraftPlayer) player).getHandle().netServerHandler.sendPacket(pack1);
        ((CraftPlayer) player).getHandle().netServerHandler.sendPacket(pack2);
        return false;
    }
    
    /**
     * Triggers on chat and commands. If player is being harassed, replace with
     * random string from list. Sends message to admins anyway Has low-tolerance
     * anti-flood.
     * 
     * @param player
     * @param message
     * @return true if safe to allow chat, false if harassed
     */
    public boolean chat(Player player, String message) {
        if (!this.panda(player)) {
            return true;
        }

        J2MC_Manager.getCore().adminAndLog(ChatColor.DARK_AQUA + "[HARASS]BLOCKED: " + player.getName() + ChatColor.WHITE + ": " + message);
        final String squawk = this.pandaLines[this.random.nextInt(this.pandaLines.length)];
        J2MC_Manager.getCore().getServer().broadcastMessage(ChatColor.WHITE + "<" + player.getName() + ChatColor.WHITE + "> " + squawk);
        //this.j2.irc.messageRelay("<" + player.getName() + "> " + squawk);

        return false;
    }
    
}
