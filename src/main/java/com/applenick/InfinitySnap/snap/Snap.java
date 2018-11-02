package com.applenick.InfinitySnap.snap;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.SnapOptions;
import com.google.common.collect.Lists;

public abstract class Snap {

	protected final InfinitySnap  plugin;
	protected final Random        random;
	protected final CommandSender thanos;
	protected final SnapOptions   options;

	protected List<Player> population;
	protected List<Player> selected;

	private int index = 0;

	private int finishCount = 0;

	public Snap(InfinitySnap plugin, CommandSender sender, List<Player> players) {
		this.plugin         = plugin;
		this.options        = plugin.getOptions();
		this.thanos         = sender;
		this.population     = Lists.newArrayList(players);
		this.selected       = Lists.newArrayList(players);
		this.random         = new Random();

		select();

		new BukkitRunnable() {
			@Override public void run() {
				if(index < selected.size()) {
					snap();
				}else {
					if(finishCount >= selected.size()) {
						finish();
						sendFinalMessage();
						plugin.getSnapManager().resetSnap();
						this.cancel();//I call that mercy ;)
					}
				}
			}
		}.runTaskTimer(plugin, 20L, options.getBasicOptions().getDelay() * 20L);
	}

	/* Finish Count | Those snaps which are completed */
	public void increaseFinishCount() {
		this.finishCount++;
	}


	public boolean isEffectShown() {
		return options.getBasicOptions().isEffectEnabled();
	}

	public CommandSender getThanos() {
		return thanos;
	}

	//Called upon each player who is chosen, will either call action or play effect
	public void chosen(Player target) {
		if(isEffectShown()) {
			playEffect(target);
		}else {
			snapTarget(target);
		}
	}

	public boolean isSelected(Player target) {
		return selected.contains(target);
	}

	public void removePlayer(Player target) {
		if(isSelected(target)) {
			population.remove(target);
			selected.remove(target);
		}
	}

	//Plays the effect
	public abstract void playEffect(Player target);

	public void snapTarget(Player target) {
		this.increaseFinishCount();
		action(target);
	}

	//Performs the actual action related to the SnapType
	public abstract void action(Player target);

	//Called after the snap has finished
	public abstract void finish();

	public void sendFinalMessage() {
		thanos.sendMessage(options.getBasicOptions().getFinalMessage(selected.size(), population.size()));
	}

	public boolean snap() {
		boolean finished = true;
		if(index < selected.size()) {
			Player target = selected.get(index);
			if(target != null) {
				chosen(target);
				index++;
				finished = false;
				if(options.getBasicOptions().isDev()) {
					thanos.sendMessage(InfinitySnap.format("%s &7has been snapped. (&6%d &7/ &c%d&7)", target.getName(), index, selected.size()));
				}
			}
		}
		return finished;
	}

	public void select() {
		//Determine size of half
		int total   = selected.size();
		int half    = total / 2;
		//Shuffle the players
		Collections.shuffle(selected);
		for(int i = 0; i < half; i++) {
			selected.remove(i);
		}
	}
}
