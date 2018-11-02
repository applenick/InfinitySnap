package com.applenick.InfinitySnap.snap.types;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.snap.Snap;
import com.applenick.InfinitySnap.snap.effects.IDontFeelSoGood;

public class MessageSnap extends Snap {
	
    public MessageSnap(InfinitySnap plugin, CommandSender sender, List<Player> population) {
        super(plugin, sender, population);
    }

    public void broadcastMessage(Player player) {
        plugin.getServer().broadcastMessage(options.getBroadcastOptions().getSnapMessage(player.getDisplayName()));
    }

    @Override public void action(Player target) {
    	broadcastMessage(target);
    }

    //Nothing after snap
    @Override public void finish() {}

	@Override
	public void playEffect(Player target) {
		new IDontFeelSoGood(plugin, this, target, 8);
	}
}
