package com.applenick.InfinitySnap.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.applenick.InfinitySnap.snap.Snap;

public class SnapEffectFinishEvent extends Event {
	
	private Player player;
	private Snap   snap;
	
	public SnapEffectFinishEvent(Player player, Snap snap) {
		this.player = player;
		this.snap   = snap;
	}
	
	public Snap getSnap() {
		return snap;
	}
	
    public Player getPlayer() {
		return player;
	}

	private static final HandlerList handlers = new HandlerList();

    @Override public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
