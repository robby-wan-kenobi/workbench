package model.tracker;

import java.util.ArrayList;
import java.util.List;

public class Timer{
	
	private long startTime;
	private long stopTime;
	private List<Pause> pauseTimes;
	private Pause currentPause;
	
	private class Pause{
		public long start;
		public long end;
		
		public long getDuration(){
			return end - start;
		}
	}
	
	public Timer(){
		startTime = 0;
		stopTime = 0;
		pauseTimes = new ArrayList<Pause>();
		currentPause = null;
	}
	
	public void start(){
		startTime = 0;
		stopTime = 0;
		startTime = System.currentTimeMillis();// - 3582499L;
	}
	
	public void stop(){
		stopTime = System.currentTimeMillis();
	}
	
	public boolean isPaused(){
		return currentPause != null;
	}
	
	public void pause(){
		currentPause = new Pause();
		currentPause.start = System.currentTimeMillis();
		currentPause.end = 0;
	}
	
	public void resume(){
		currentPause.end = System.currentTimeMillis();
		pauseTimes.add(currentPause);
		currentPause = null;
	}
	
	// Gets the amount this timer's being going for
	public long poll(){
		long baseDuration = 0;
		if(startTime == 0)
			return 0;
		else if(stopTime > 0){
			baseDuration = stopTime - startTime;
			baseDuration -= getTotalPauseTime();
		}
		else if(currentPause != null){
			baseDuration = currentPause.start - startTime;
			baseDuration -= getTotalPauseTime();
		}
		else{
			baseDuration = System.currentTimeMillis() - startTime;
			baseDuration -= getTotalPauseTime();
		}
		return baseDuration / 1000;
	}
	
	private long getTotalPauseTime(){
		long duration = 0;
		for(int i=0; i<pauseTimes.size(); i++){
			duration += pauseTimes.get(i).getDuration();
		}
		return duration;
	}
	
	public void subtract(long duration){
		startTime += duration;
	}
}