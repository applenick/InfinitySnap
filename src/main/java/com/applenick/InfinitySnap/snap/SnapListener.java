package com.applenick.InfinitySnap.snap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.applenick.InfinitySnap.SnapOptions;
import com.applenick.InfinitySnap.events.SnapEffectFinishEvent;

public class SnapListener implements Listener {
	
	private SnapManager manager;
	private SnapOptions options;
	
	public SnapListener(SnapManager manager, SnapOptions options) {
		this.manager = manager;
		this.options = options;
	}
		
	@EventHandler public void onSnapEffectFinish(SnapEffectFinishEvent event) {
		event.getSnap().snapTarget(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGH) public void onQuit(PlayerQuitEvent event) {
		manager.getActiveSnap().ifPresent(snap -> {
			if(snap.isSelected(event.getPlayer())) {
				event.setQuitMessage(null);
			}
		});
	}

	@EventHandler(priority = EventPriority.HIGH) public void onKick(PlayerKickEvent event) {
		manager.getActiveSnap().ifPresent(snap -> {
			Player target = event.getPlayer();			
			if(snap.isSelected(target)) {
				event.setLeaveMessage(null);
			}
		});
	}
	
}
