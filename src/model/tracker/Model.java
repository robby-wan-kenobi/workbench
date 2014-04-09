package model.tracker;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TimerTask;

import view.tracker.GeneralListener;

public class Model extends Observable{
	
	private String currentTask;
	private String selectedTask;
	private boolean taskStarted;
	private Timer taskTimer;
	private Timer dayTimer;
	private java.util.Timer dayTimeKeeper = null;
	private java.util.Timer taskTimeKeeper = null;
	private DataRetriever data;
	
	private class TimeUpdate extends TimerTask{
		private Timer timer;
		private String type;
		public TimeUpdate(Timer timer, String type){
			this.timer = timer;
			this.type = type;
		}
		public void run(){
			// send an update for the time with time retrieved from model
			long time = timer.poll();
			long hours = time / 3600;
			time -= (hours*3600);
			long minutes = time / 60;
			long seconds = time % 60;
			String timeString = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
			String[] timeInfo = new String[2];
			timeInfo[0] = type;
			timeInfo[1] = timeString;
			setChanged();
			notifyObservers(timeInfo);
		}
	}
	
	public Model(GeneralListener gl){
		addObserver(gl);
		taskStarted = false;
		currentTask = null;
		selectedTask = null;
		taskTimer = null;
		dayTimer = new Timer();
		dayTimer.start();
		dayTimeKeeper = new java.util.Timer();
		dayTimeKeeper.schedule(new TimeUpdate(dayTimer, "day"), 0, 1000);
		data = new DataRetriever("data.xml");
	}
	
	public String getCurrentTask(){
		return currentTask;
	}
	
	public void setCurrentTask(String curTask){
		currentTask = curTask;
		setChanged();
		notifyObservers("taskChanged");
	}
	
	public String getSelectedTask(){
		return selectedTask;
	}
	
	public void setSelectedTask(String task){
		selectedTask = task;
		setChanged();
		notifyObservers("selectedTaskChanged");
	}
	
	public void changeTaskName(String origName, String newName){
		data.changeTaskName(origName, newName);
	}
	
	// Returns map of day(String) to duration list
	public Map<String, List<Duration>> getTaskBreakdown(String taskName){
		return data.getTaskBreakdown(taskName);
	}
	
	public String[] getAllTasks(){
		return data.getAllTasks();
	}
	
	// Returns map of task to duration list
	public Map<String, List<Duration>> getTodaysBreakdown(){
		return getDayBreakdown(Calendar.getInstance());
	}
	
	public Map<String, List<Duration>> getDayBreakdown(Calendar day){
		return data.getDayBreakdown(day);
	}
	
	public void addTask(String name){
		data.addTask(name);
	}
	
	public void startTask(String name){
		taskStarted = true;
		data.startTask(name);
		taskTimer = new Timer();
		taskTimer.start();
		taskTimeKeeper = new java.util.Timer();
		taskTimeKeeper.schedule(new TimeUpdate(taskTimer, "task"), 0, 1000);
	}
	
	public void stopCurrentTask(){
		taskStarted = false;
		stopTask(currentTask);
	}
	
	public void stopTask(String name){
		taskStarted = false;
		if(data.isTaskAlive(name))	// Task is not already stopped
			data.stopTask(name);
		if(taskTimer != null)
			taskTimer.stop();
		if(taskTimeKeeper != null)
			taskTimeKeeper.cancel();
		taskTimer = null;
		taskTimeKeeper = null;
	}
	
	public void stopStartTask(){
		if(!taskStarted){
			if(taskTimer == null) // User wants to start the timer
				startTask(currentTask);
		}
		else{
			stopTask(currentTask);
		}
	}
	
	public void changeStartTime(String time){
		if(validateTime(time)){
			data.changeCurrentIterationStart(currentTask, time);
		}else{
			setChanged();
			notifyObservers("invalidTime");
		}
	}
	
	public double getTotalTaskTime(String name){
		Map<String, List<Duration>> breakdown = getTaskBreakdown(name);
		double totalTime = 0.0;
		for(List<Duration> durList: breakdown.values()){
			for(Duration duration: durList){
				totalTime += duration.getTime();
			}
		}
		return totalTime;
		//return data.getTotalDurationOf(name);		// This might be about as fast
	}
	
	public double getUnloggedTaskTime(String name){
		Map<String, List<Duration>> breakdown = getTaskBreakdown(name);
		double totalTime = 0.0;
		for(List<Duration> durList: breakdown.values()){
			for(Duration duration: durList){
				if(!duration.isLogged())
					totalTime += duration.getTime();
			}
		}
		return totalTime;
	}
	
	public long getTodaysTime(){
		return dayTimer.poll();
	}
	
	public long getCurrentTaskTime(){
		return taskTimer.poll();
	}
	
	public boolean isTaskDurationLogged(String task, int duration){
		return data.isTaskDurationLogged(task, duration);
	}
	
	public void setTaskDurationLogged(String task, int duration) {
		data.setTaskDurationLogged(task, duration);
	}
	
	public boolean isTaskUpToDate(String task){
		return data.isTaskUpToDate(task);
	}
	
	public void setTaskUpToDate(String task){
		data.setTaskUpToDate(task);
	}
	
	public String getTaskResolution(String taskName){
		return data.getTaskResolution(taskName);
	}
	
	public void setTaskResolution(String taskName, String resolution){
		data.setTaskResolution(taskName, resolution);
	}
	
	public boolean isDayPaused(){
		return dayTimer.isPaused();
	}
	
	public void pauseDay(){
		if(dayTimer.isPaused())
			dayTimer.resume();
		else
			dayTimer.pause();
	}
	
	public void resetDay(){
		dayTimer.stop();
		dayTimer = new Timer();
		dayTimer.start();
		dayTimeKeeper = new java.util.Timer();
		dayTimeKeeper.schedule(new TimeUpdate(dayTimer, "day"), 0, 1000);
	}

	private boolean validateTime(String time){
		String[] pieces = time.split(":");
		try{
			int hours = Integer.parseInt(pieces[0]);
			int minutes = Integer.parseInt(pieces[1]);
			int seconds = Integer.parseInt(pieces[2]);
			if(hours < 0 || hours > 99)
				return false;
			if(minutes < 0 || minutes > 59)
				return false;
			if(seconds < 0 || seconds > 59)
				return false;
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	public boolean isTaskStarted(){
		return taskStarted;
	}

	public void saveAll(){
		data.writeDocumentToFile();
	}
	
}