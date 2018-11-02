package com.applenick.InfinitySnap.snap.effects;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.events.SnapEffectFinishEvent;
import com.applenick.InfinitySnap.snap.Snap;

public abstract class SnapEffect {

	private InfinitySnap plugin;
    private Player       target;
	private int          totalSeconds;
	private int          time;
	
	private final Snap snap;
    
    public SnapEffect(InfinitySnap plugin, Snap snap, Player target, int seconds) {
    	this.plugin       = plugin;
    	this.snap         = snap;
        this.target       = target;
        this.totalSeconds = seconds;
        this.time         = seconds;
        
        
        prepare();
        
        new BukkitRunnable() {
			@Override
			public void run() {
				if(time > 0) {
					effect();
				}else {
					finish();
					this.cancel();
				}
				time--;
			}
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    public Snap getSnap() {
    	return snap;
    }
    
    public Player getTarget() {
        return target;
    }
    
    public int getSeconds() {
    	return totalSeconds;
    }
    
    public abstract void prepare();
    public abstract void effect();
    public abstract void finish();
    
    
    public void callFinishEvent() {
    	plugin.getServer().getPluginManager().callEvent(new SnapEffectFinishEvent(getTarget(), getSnap()));
    }
    
    public void addPotionEffects(Player target, PotionEffectValue... values) {
    	for(PotionEffectValue value : values) {
    		addPotionEffect(target, value.getType(), value.getPower());
    	}
    }
    
    public void addPotionEffect(Player target, PotionEffectType potion, int power) {
    	target.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE, power, true, false));
    }
    
    public void removePotions(Player target, PotionEffectValue... types) {
    	for(PotionEffectValue type : types) {
    		target.removePotionEffect(type.getType());
    	}
    }
    
    public void playEffect(Effect effect, Location location) {
    	location.getWorld().playEffect(location, effect, 0);
    }
    
    public static class PotionEffectValue {
    	private PotionEffectType type;
    	private int              power;
    	
    	public PotionEffectValue(PotionEffectType type, int power) {
    		this.type  = type;
    		this.power = power;
    	}

		public PotionEffectType getType() {
			return type;
		}

		public int getPower() {
			return power;
		}
    	
    	
    }
}
