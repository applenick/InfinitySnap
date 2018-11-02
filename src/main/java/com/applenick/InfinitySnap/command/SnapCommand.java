package com.applenick.InfinitySnap.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.SnapOptions;
import com.applenick.InfinitySnap.command.exceptions.UnknownSnapTypeException;
import com.applenick.InfinitySnap.snap.Snap;
import com.applenick.InfinitySnap.snap.SnapManager;
import com.applenick.InfinitySnap.snap.SnapType;
import com.applenick.InfinitySnap.snap.types.BanSnap;
import com.applenick.InfinitySnap.snap.types.KickSnap;
import com.applenick.InfinitySnap.snap.types.MessageSnap;
import com.google.common.collect.Lists;

public class SnapCommand implements InfinityCommand, CommandExecutor {

	public static final String COMMAND_NAME = "snap";
	public static final String PERMISSION   = "snap.command";
	
	private final InfinitySnap plugin;
	private final SnapManager  manager;
	private final SnapOptions  options;

	public SnapCommand(InfinitySnap plugin, SnapManager manager, SnapOptions options) {
		this.plugin  = plugin;
		this.manager = manager;
		this.options = options;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission(PERMISSION)) {
			sender.sendMessage(format("&7You do not have permission for this command."));
			return true;
		}

		if(!manager.getActiveSnap().isPresent() && manager.isFinished()) {
			Snap snap = null;

			if(args.length <= 0) {
				//Default Snap
				snap = createSnap(options.getBasicOptions().getSnapType(), plugin, sender);
			}else {
				if(args.length == 1) {
					//Specific Snap
					try {
						snap = createSnap(SnapType.parseConfig(args[0]), plugin, sender);
					} catch (UnknownSnapTypeException e) {
						if(options.getBasicOptions().isDev()) {
							e.printStackTrace();
						}
						sender.sendMessage(formatUnknownSnapType(e.getSnapType()));
					}
				}else {
					//Specific Snap & Group
					List<Player> targets = getTargets(args, 1);
					if(targets.size() < 1) {
						sender.sendMessage(format("&cNone of the provided players could be found."));
					}else {
						try {
							snap = createSnap(SnapType.parseConfig(args[0]), plugin, sender, targets);
						} catch (UnknownSnapTypeException e) {
							if(options.getBasicOptions().isDev()) {
								e.printStackTrace();
							}
							sender.sendMessage(formatUnknownSnapType(e.getSnapType()));
						}  
					}
				}
			}

			if(snap != null) {
				sender.sendMessage(options.getBasicOptions().getRandomQuote());
				manager.setActiveSnap(snap);
			}

		} else {
			sender.sendMessage(format("&7A snap is currently running. Please wait for it to finish!"));
		}
		return true;
	}

	public List<Player> getTargets(String[] args, int startIndex){
		List<Player> players = Lists.newArrayList();
		for(int i = startIndex; i < args.length; i++) {
			plugin.getPlayer(args[i]).ifPresent(target -> {
				if(!players.contains(target)) {
					players.add(target);
				}
			});
		}    	
		return players;
	}

	public Snap createSnap(SnapType type, InfinitySnap plugin, CommandSender sender) {
		return createSnap(type, plugin, sender, plugin.getOnlinePlayers());
	}

	public Snap createSnap(SnapType type, InfinitySnap plugin, CommandSender sender, List<Player> population) {

		if(population.size() < 2) {
			sender.sendMessage(format("&cAt least two players are required for this command."));
			return null;
		}

		switch(type) {
		case BAN:
			return new BanSnap(plugin, sender, population);
		case KICK:
			return new KickSnap(plugin, sender, population);
		case BROADCAST:
			return new MessageSnap(plugin, sender, population);
		default:
			return null;
		}
	}
}
