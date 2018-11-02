package com.applenick.InfinitySnap.snap;

import java.util.Optional;

public class SnapManager {
	
	private Optional<Snap> activeSnap;
	private boolean isFinished;
	
	public SnapManager() {
		this.isFinished = true;
		this.activeSnap = Optional.empty();
	}
	
	public void setActiveSnap(Snap snap) {
		this.isFinished = false;
		this.activeSnap = Optional.of(snap);
	}
	
	public void setFinished(boolean finished) {
		this.isFinished = finished;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public Optional<Snap> getActiveSnap(){
		return activeSnap;
	}

	public void resetSnap() {
		activeSnap = Optional.empty();
		isFinished = true;
	}
}
