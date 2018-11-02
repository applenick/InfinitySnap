package com.applenick.InfinitySnap;

import java.util.List;
import java.util.Random;

import org.bukkit.configuration.file.FileConfiguration;

import com.applenick.InfinitySnap.command.exceptions.UnknownSnapTypeException;
import com.applenick.InfinitySnap.snap.SnapType;

public class SnapOptions {

    private final FileConfiguration  config;
    private final BasicSnapOptions   basics;
    private final SnapMessageOptions snapMsg;
    private final SnapKickOptions    snapKick;
    private final SnapBanOptions     snapBan;
    

    public SnapOptions(FileConfiguration config) {
        this.config   = config;
        this.basics   = new BasicSnapOptions(this);
        this.snapMsg  = new SnapMessageOptions(this);
        this.snapKick = new SnapKickOptions(this);
        this.snapBan  = new SnapBanOptions(this);
    }
    
    public FileConfiguration getConfig() {
    	return config;
    }

    public BasicSnapOptions getBasicOptions() {
        return basics;
    }
    
    public SnapMessageOptions getBroadcastOptions() {
    	return snapMsg;
    }

    public SnapKickOptions getKickOptions() {
    	return snapKick;
    }
    
    public SnapBanOptions getBanOptions() {
    	return snapBan;
    }    
    
    public static class SnapBanOptions extends SubSnapOptions {
    	//Paths
    	public static final String PATH = "snap-ban";
    	public static final String SNAP_BAN_PATH = PATH + ".ban-message";
    	
    	//Defaults
    	public static final String DEFAULT_BAN_MESSAGE = "&cYou have been snapped!";
    	
    	//Values
        private String snapBanMessage;

        public SnapBanOptions(SnapOptions options) {
        	super(options);
        }
    	
        public String getSnapBanMessage() {
            return InfinitySnap.format(snapBanMessage);
        }

		@Override public void initValues() {
	        this.snapBanMessage = options.getConfig().getString(SNAP_BAN_PATH, DEFAULT_BAN_MESSAGE);
		}
    }
    
    
    public static class SnapKickOptions extends SubSnapOptions {
    	//Paths
    	public static final String PATH = "snap-kick";
    	public static final String SNAP_KICK_PATH = PATH + ".kick-message";
    	
    	//Defaults
    	public static final String DEFAULT_KICK_MESSAGE = "&cYou have been snapped!";
    	
    	//Values
        private String snapKickMessage;

        public SnapKickOptions(SnapOptions options) {
        	super(options);
        }
    	
        public String getSnapKickMessage() {
            return InfinitySnap.format(snapKickMessage);
        }

		@Override public void initValues() {
	        this.snapKickMessage = options.getConfig().getString(SNAP_KICK_PATH, DEFAULT_KICK_MESSAGE);
		}
    }
    
    public static class SnapMessageOptions extends SubSnapOptions {
    	//Paths
    	public static final String PATH = "snap-message";
    	public static final String SNAP_MSG_PATH = PATH + ".broadcast-message";
    	
    	//Defaults
    	public static final String DEFAULT_MESSAGE = "%player% has been snapped!";
    	
    	//Values
        private String snapMessage;

        public SnapMessageOptions(SnapOptions options) {
        	super(options);
        }
    	
        public String getSnapMessage(String player) {
            return InfinitySnap.format(snapMessage.replace("%player%", player));
        }

		@Override public void initValues() {
	        this.snapMessage = options.getConfig().getString(SNAP_MSG_PATH, DEFAULT_MESSAGE);
		}
    }


    public static class BasicSnapOptions extends SubSnapOptions {
        //Paths
        public static final String PATH              = "snap";
        public static final String DELAY_PATH        = PATH + ".delay";
        public static final String SNAP_TYPE_PATH    = PATH + ".default-type";
        public static final String SNAP_QUOTES_PATH  = PATH + ".snap-quotes";
        public static final String SNAP_EFFECT_PATH  = PATH + ".effect-enabled";
        public static final String INSTANT_SAVE_PATH = PATH + ".instant-save";
        public static final String FINAL_MSG_PATH    = PATH + ".final-message";
        public static final String HIDE_LEAVE_PATH   = PATH + ".hide-leave-message";
        public static final String ALLOW_IGNORE_PATH = PATH + ".allow-ignore-perms";

        //Defaults
        public static final int DEFAULT_DELAY = 1;
        public static final String THANOS_MSG   = "&tPerfectly Balanced &7- (&b%snapped% &7/ &3%total%)";
        public static final String THANOS_QUOTE = "Fun isn’t something one considers when balancing the server."
        		                                + " But this... does put a smile on my face.";

        //Values
        private int          delay;
        private SnapType     snapType;
        private List<String> snapQuotes;
        private boolean      effectEnabled;
        private boolean      instantSave;
        private String       finalMSG;
        private boolean      hideLeave;
        private boolean      allowIgnorePerms;
        
        private boolean dev;
        
        //Extras
        private Random random;

        
        public BasicSnapOptions(SnapOptions options) {
        	super(options);
        }
        
        public boolean isDev() {
        	return dev;
        }

        public int getDelay() {
            return Math.max(0, delay);
        }
        
        public SnapType getSnapType() {
        	return snapType;
        }
        
        public List<String> getSnapQuotes() {
        	return snapQuotes;
        }
        
        public String getRandomQuote() {
        	return InfinitySnap.format(snapQuotes.get(random.nextInt(snapQuotes.size())));
        }
        
        public boolean isEffectEnabled() {
        	return effectEnabled;
        }
        
        public boolean isInstantSaveEnabled() {
        	return instantSave;
        }
        
        public boolean isLeaveMessageHidden() {
        	return hideLeave;
        }
        
        public boolean isIgnorePermsAllowed() {
        	return allowIgnorePerms;
        }
        
        public String getFinalMessage(int snapped, int total) {
        	return InfinitySnap.format(finalMSG.replaceAll("%snapped%", "" + snapped)
        			                   .replaceAll("%total%", "" + total));
        }
        
        @Override public void initValues() {
            this.delay         = options.getConfig().getInt(DELAY_PATH, DEFAULT_DELAY);
            try {
				this.snapType      = SnapType.parseConfig(options.getConfig().getString(SNAP_TYPE_PATH, ""));
			} catch (UnknownSnapTypeException e) {
				this.snapType = SnapType.BROADCAST;
			}
            this.snapQuotes       = options.getConfig().getStringList(SNAP_QUOTES_PATH);
            this.effectEnabled    = options.getConfig().getBoolean(SNAP_EFFECT_PATH, false);
            this.instantSave      = options.getConfig().getBoolean(INSTANT_SAVE_PATH, true);
            this.finalMSG         = options.getConfig().getString(FINAL_MSG_PATH, THANOS_MSG);
            this.hideLeave        = options.getConfig().getBoolean(HIDE_LEAVE_PATH, true);
            this.allowIgnorePerms = options.getConfig().getBoolean(ALLOW_IGNORE_PATH, true);
            this.dev              = options.getConfig().getBoolean(PATH + ".debug", false);
            this.random           = new Random();
            
            if(snapQuotes.isEmpty()) {
            	snapQuotes.add(InfinitySnap.getRandomInfinityStone().toString() + THANOS_QUOTE);
            }
        }
    }
    
    public abstract static class SubSnapOptions {
        protected SnapOptions options;
        public SubSnapOptions(SnapOptions options) {
            this.options = options;
            this.initValues();
        }
        public abstract void initValues();
    }

}
