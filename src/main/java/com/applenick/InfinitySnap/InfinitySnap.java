package com.applenick.InfinitySnap;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.applenick.InfinitySnap.command.SnapCommand;
import com.applenick.InfinitySnap.command.SoulsCommand;
import com.applenick.InfinitySnap.command.tab.SnapTabCompleter;
import com.applenick.InfinitySnap.command.tab.SoulsTabCompleter;
import com.applenick.InfinitySnap.files.SoulStone;
import com.applenick.InfinitySnap.snap.SnapListener;
import com.applenick.InfinitySnap.snap.SnapManager;

public class InfinitySnap extends JavaPlugin {

    public static final String PLUGIN_NAME = "&7[&6Infinity&5Snap&7]&8 ";
	public static final String IGNORE_SNAP_PERM = "snap.ignore";

	private static InfinitySnap plugin;
	public static InfinitySnap get() {
		return plugin;
	}
	
    private SoulStone   soulStone;

    private SnapOptions config;
    public SnapOptions getOptions() {
        return config;
    }
    
    private SnapManager snapManager;
    public SnapManager getSnapManager() {
    	return snapManager;
    }

    @Override public void onEnable() {
    	plugin = this;
    	
        this.saveDefaultConfig();
        this.config = new SnapOptions(getConfig());
        
        try {
            this.soulStone   = new SoulStone(this, config);
            this.snapManager = new SnapManager();
            registerListeners();
            setupCommands();
            
        } catch (IOException e) {
            printf("&cPlugin was unable to initialize. This is most likely due to an error with your souls.txt file.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;            
        }
        
    }

    @Override public void onDisable() {
        if(soulStone != null) {
            try {
                soulStone.saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<Player> getOnlinePlayers(){
        return getServer().getOnlinePlayers().stream().filter(this::isIncluded).collect(Collectors.toList());
    }
    
    public Optional<Player> getPlayer(String name) {
    	return getOnlinePlayers().stream().filter(n -> n.getName().equalsIgnoreCase(name)).findAny();
    }
    
    public boolean isIncluded(Player player){
    	return config.getBasicOptions().isIgnorePermsAllowed() ? !player.hasPermission(IGNORE_SNAP_PERM) : true;
    }
    
    private void registerListeners() {
    	getServer().getPluginManager().registerEvents(new SnapListener(snapManager, config), this);
        getServer().getPluginManager().registerEvents(soulStone, this);
    }

    private void setupCommands() {
        PluginCommand snapCommand = getCommand(SnapCommand.COMMAND_NAME);
        PluginCommand soulCommand = getCommand(SoulsCommand.COMMAND_NAME);
        snapCommand.setExecutor(new SnapCommand(this, snapManager, config));
        snapCommand.setTabCompleter(new SnapTabCompleter());  
        
        soulCommand.setExecutor(new SoulsCommand(soulStone, config));
        soulCommand.setTabCompleter(new SoulsTabCompleter(soulStone));
    }

    public static void printf(String format, Object... args) {
        Bukkit.getServer().getConsoleSender()
        .sendMessage(format(PLUGIN_NAME + format, args));
    }

    public static String format(String format, Object... args) {
        return translateColorCodes(format, args);
    }
    
    private static String translateColorCodes(String format, Object... args) {
    	String thanosColors = format.replaceAll("&t", getRandomInfinityStone().toString());
    	return ChatColor.translateAlternateColorCodes('&', String.format(thanosColors, args));
    }
    
    public static ChatColor getRandomInfinityStone() {        
        switch((int)(Math.random() * 6) + 1) {
            case 1:  return ChatColor.AQUA;
            case 2:  return ChatColor.DARK_PURPLE;
            case 3:  return ChatColor.DARK_RED;
            case 4:  return ChatColor.GOLD;
            case 5:  return ChatColor.GREEN;
            case 6:  return ChatColor.YELLOW;
            default: return ChatColor.WHITE;
        }
    }
}
