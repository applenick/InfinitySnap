package com.applenick.InfinitySnap.command;

import java.io.IOException;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.SnapOptions;
import com.applenick.InfinitySnap.files.SoulStone;

public class SoulsCommand implements InfinityCommand, CommandExecutor {


	public static final String PERMISSION   = "soul.command";
	public static final String COMMAND_NAME = "souls";

	public static final String ADD_SUB_COMMAND    = "add";
	public static final String REMOVE_SUB_COMMAND = "remove";
	public static final String CLEAR_SUB_COMMAND  = "clear";

	public static final String[] SUB_COMMANDS = {ADD_SUB_COMMAND, REMOVE_SUB_COMMAND, CLEAR_SUB_COMMAND};

	private final SoulStone    souls;
	private final SnapOptions  options;

	public SoulsCommand(SoulStone souls, SnapOptions options) {
		this.souls   = souls;
		this.options = options;
	}


	@Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {    	
		if(!sender.hasPermission(PERMISSION)) {
			sender.sendMessage(format("&cYou do not have permission for this command."));
			return true;
		}

		// /souls clear
		if(args.length == 1 && args[0].equalsIgnoreCase(CLEAR_SUB_COMMAND)) {
			int removed = 0;
			try {
				removed = souls.clear();
			} catch (IOException e) {
				if(options.getBasicOptions().isDev()) {
					e.printStackTrace();						
				}else {
					InfinitySnap.printf("&7There was an error saving to the souls.txt file");
				}
			}
			sender.sendMessage(format("&7Removed &a%d&7 player%s from the soul stone.", removed, removed != 1 ? "s" : ""));
		}else if(args.length >= 2) {
			if(args[0].equalsIgnoreCase(ADD_SUB_COMMAND)) {
				String target = args[1];
				try {
					if(souls.add(target)) {
						sender.sendMessage(format("&a%s &7has been added to the souls.txt file.", target));
					}else {
						sender.sendMessage(format("&c%s &7is already on the souls.txt file.", target));
					}
				} catch (IOException e) {
					if(options.getBasicOptions().isDev()) {
						e.printStackTrace();
					}else {
						InfinitySnap.printf("&7There was an error saving to the souls.txt file");
					}
				}
			} else if(args[0].equalsIgnoreCase(REMOVE_SUB_COMMAND)) {
				String target = args[1];

				if(souls.getNames().isEmpty()) {
					sender.sendMessage(format("&cThere are no players to remove from the soul stone."));
				}else {
					try {
						if(souls.remove(target)) {
							sender.sendMessage(format("&a%s &7has been removed from the souls.txt file.", target));
						}else {
							sender.sendMessage(format("&c%s &7is not on the souls.txt file.", target));
						}
					} catch (IOException e) {
						if(options.getBasicOptions().isDev()) {
							e.printStackTrace();
						}else {
							InfinitySnap.printf("&7There was an error saving to the souls.txt file");
						}
					}
				}
			} 
		}else {
			int size = souls.getNames().size();
			if(size > 0) {
				String list = souls.getNames().stream().collect(Collectors.joining(", "));
				sender.sendMessage(InfinitySnap.format("&7&6%d player%s in the soul stone&f: %s", size, size != 1 ? "s" : "", list));
			}else {
				sender.sendMessage(InfinitySnap.format("&cNo players have been sent to the soul stone yet."));
			}
		}
		return true;
	}	

}