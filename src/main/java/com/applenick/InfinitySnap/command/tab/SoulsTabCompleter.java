package com.applenick.InfinitySnap.command.tab;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.command.SoulsCommand;
import com.applenick.InfinitySnap.files.SoulStone;
import com.google.common.collect.Lists;

public class SoulsTabCompleter implements TabCompleter {

	private SoulStone souls;

	public SoulsTabCompleter(SoulStone stone) {
		this.souls = stone;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = Lists.newArrayList();

		if(sender.hasPermission(SoulsCommand.PERMISSION)) {
			if(args.length == 1) {
				results = Arrays.asList(SoulsCommand.SUB_COMMANDS);
			} else if (args.length > 1) {
				String sub = args[0];
				switch(sub) {
				case SoulsCommand.ADD_SUB_COMMAND:
					results = InfinitySnap.get().getOnlinePlayers().stream().map(Player::getName).filter(n -> !isIncluded(n, args)).collect(Collectors.toList());
					break;
				case SoulsCommand.REMOVE_SUB_COMMAND:
					results = souls.getNames().stream().filter(n -> !isIncluded(n, args)).collect(Collectors.toList());
					break;
				default: break;
				}
			}
		}
		return results;
	}

	public boolean isIncluded(String name, String[] args) {
		for(int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}


}
