package com.applenick.InfinitySnap.snap.types;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.snap.Snap;
import com.applenick.InfinitySnap.snap.effects.IDontFeelSoGood;

public class KickSnap extends Snap {

    public KickSnap(InfinitySnap plugin, CommandSender sender, List<Player> population) {
        super(plugin, sender, population);
    }

    @Override public void action(Player target) {
        target.kickPlayer(options.getKickOptions().getSnapKickMessage());
    }

    //Nothing after the snap
    @Override public void finish() {}

	@Override
	public void playEffect(Player target) {
		new IDontFeelSoGood(plugin, this, target, 8);
	}

}
