package com.applenick.InfinitySnap.events;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanSnapEvent extends Event {

    private List<String> players;

    public BanSnapEvent(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayerNames() {
        return players;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
