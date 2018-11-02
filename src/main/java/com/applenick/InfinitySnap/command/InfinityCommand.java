package com.applenick.InfinitySnap.command;

import java.util.stream.Collectors;

import com.applenick.InfinitySnap.InfinitySnap;
import com.applenick.InfinitySnap.snap.SnapType;

public interface InfinityCommand {
	
	default String format(String format, Object... args) {
		return InfinitySnap.format(format, args);
	}

	
	default String formatUnknownSnapType(String type) {
		return format("&b%s &cis not a valid snap type!\n&7Please use one of the following: &6%s", type, SnapType.getTabOptions().stream().collect(Collectors.joining("&7,&6 ")));
	}
}
