package com.applenick.InfinitySnap.snap.effects;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.snap.Snap;

public class IDontFeelSoGood extends SnapEffect {

	public static final Sound SOUND = Sound.BLOCK_PORTAL_TRIGGER;//TEST SOUNDS
	
	public static final Particle EFFECT = Particle.SMOKE_NORMAL;

	public static final PotionEffectValue[] POTIONS = {new PotionEffectValue(PotionEffectType.CONFUSION, 5),
													   new PotionEffectValue(PotionEffectType.WITHER, 0)};

	private Random random;
	
	public IDontFeelSoGood(InfinitySnap plugin, Snap snap, Player target, int seconds) {
		super(plugin, snap, target, seconds);
		this.random = new Random();
	}

	@Override
	public void prepare() {
		addPotionEffects(getTarget(), POTIONS);
		getTarget().playSound(getTarget().getLocation(), SOUND, 1f, 0.1f);
	}


	@Override
	public void effect() {
		Location loc = getTarget().getLocation();

		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();

		for(int i = 0; i < 10; i++) {
			//loc.getWorld().spawn.spigot().playEffect(loc, EFFECT, 0, 0, random.nextFloat(), random.nextFloat(), random.nextFloat(), 0, 20, 20);
			loc.getWorld().spawnParticle(EFFECT, x, y, z, 20, random.nextDouble(), random.nextDouble(), random.nextDouble(), 0);
		}
	}


	@Override
	public void finish() {
		removePotions(getTarget(), POTIONS);
		callFinishEvent();
	}



}
