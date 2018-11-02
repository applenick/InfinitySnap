package com.applenick.InfinitySnap.files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.SnapOptions;
import com.applenick.InfinitySnap.events.BanSnapEvent;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class SoulStone implements Listener {

	public static final String SOUL_FILE_NAME = "souls.txt";

	private List<String> snapped;

	private File souls;

	private InfinitySnap plugin;
	private SnapOptions  options;

	public SoulStone(InfinitySnap plugin, SnapOptions options) throws IOException {
		this.plugin  = plugin;
		this.options = options;
		this.snapped = Lists.newArrayList();

		souls = new File(plugin.getDataFolder() + File.separator + SOUL_FILE_NAME);

		if(!souls.exists()) {
			souls.createNewFile();
			InfinitySnap.printf("No soul.txt file found, so a new one has been created for you.");
		}else {
			snapped = Files.readLines(souls, Charset.forName("UTF-8"));
			InfinitySnap.printf("&6%d &7souls were found.", snapped.size());
		}
	}    

	public int clear() throws IOException {
		int size = snapped.size();
		snapped.clear();

		if(options.getBasicOptions().isInstantSaveEnabled()) {
			saveFile();
		}    

		return size;
	}

	public boolean add(String name) throws IOException {
		if(!contains(name)) {
			snapped.add(name);

			if(options.getBasicOptions().isInstantSaveEnabled()) {
				saveFile();
			}
			return true;
		}else {
			return false;
		}
	}

	public boolean remove(String name) throws IOException {
		if(contains(name)) {
			snapped.remove(name);

			if(options.getBasicOptions().isInstantSaveEnabled()) {
				saveFile();
			}

			return true;
		}else {
			return false;
		}

	}

	public boolean contains(String name) {
		for(String s : snapped) {
			if(name.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	public List<String> getNames() {
		return snapped;
	}

	public void saveFile() throws IOException {
		if(souls.exists()) {
			Files.write(snapped.stream().collect(Collectors.joining("\n")), souls, Charset.forName("UTF-8"));
		}else {
			InfinitySnap.printf("&cUnable to save the souls.txt file!");
		}
	}

	@EventHandler public void onJoin(AsyncPlayerPreLoginEvent event) {
		if(contains(event.getName())){
			event.setLoginResult(Result.KICK_BANNED);
			event.setKickMessage(plugin.getOptions().getBanOptions().getSnapBanMessage());
		}
	}

	@EventHandler public void onBanSnap(BanSnapEvent event) {
		List<String> newPlayerNames = event.getPlayerNames();
		snapped.addAll(newPlayerNames);
		try {
			saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InfinitySnap.printf("%d &7players have been added to the &6souls.txt&7 file.", newPlayerNames.size());
	}

}
