package com.applenick.InfinitySnap.command.tab;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.command.SnapCommand;
import com.applenick.InfinitySnap.snap.SnapType;
import com.google.common.collect.Lists;

public class SnapTabCompleter implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = Lists.newArrayList();

		if(sender.hasPermission(SnapCommand.PERMISSION)) {
			if(args.length == 1) {
				results = SnapType.getTabOptions();
			} else if(args.length > 1) {
				results = InfinitySnap.get().getOnlinePlayers().stream().map(Player::getName).filter(n -> !isIncluded(n, args)).collect(Collectors.toList());
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
