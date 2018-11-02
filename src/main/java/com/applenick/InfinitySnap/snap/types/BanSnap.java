package com.applenick.InfinitySnap.snap.types;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.events.BanSnapEvent;
import com.applenick.InfinitySnap.snap.Snap;
import com.applenick.InfinitySnap.snap.effects.IDontFeelSoGood;
import com.google.common.collect.Lists;

public class BanSnap extends Snap {

	List<String> snapped;

	public BanSnap(InfinitySnap plugin, CommandSender sender, List<Player> population) {
		super(plugin, sender, population);
		snapped = Lists.newArrayList();
	}

	public void banPlayer(Player target) {
		target.kickPlayer(options.getBanOptions().getSnapBanMessage());
		snapped.add(target.getName());
	}

	@Override public void action(Player target) {
		banPlayer(target);
	}

	//Call the BanSnapEvent to save banned players
	@Override public void finish() {
		plugin.getServer().getPluginManager().callEvent(new BanSnapEvent(snapped));
	}

	@Override
	public void playEffect(Player target) {
		new IDontFeelSoGood(plugin, this, target, 8);
	}


}
