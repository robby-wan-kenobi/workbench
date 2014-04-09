package controller.tracker;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import model.tracker.Duration;
import model.tracker.Model;

public class Controller implements Observer{
	
	private Model model;
	
	public Controller(Model model){
		this.model = model;
	}
	
	public void addTask(String name){
		model.addTask(name);
	}
	
	public void startTask(String name){
		model.startTask(name);
	}
	
	public void stopTask(String name){
		model.stopTask(name);
	}
	
	public void stopCurrentTask(){
		model.stopTask(getCurrentTaskName());
	}
	
	public String getCurrentTaskName(){
		return model.getCurrentTask();
	}
	
	public double getTaskTime(String name){
		return model.getTotalTaskTime(name);
	}
	
	public double getUnloggedTaskTime(String task){
		return model.getUnloggedTaskTime(task);
	}
	
	public double getTodaysTime(){
		return model.getTodaysTime();
	}
	
	public String[] getAllTasks(){
		return model.getAllTasks();
	}
	
	public void setTaskDurationLogged(String task, int duration) {
		model.setTaskDurationLogged(task, duration);
	}
	
	public boolean isTaskDurationLogged(String task, int duration){
		return model.isTaskDurationLogged(task, duration);
	}
	
	public void setTaskUpToDate(String task){
		model.setTaskUpToDate(task);
	}
	
	public boolean isTaskUpToDate(String task){
		return model.isTaskUpToDate(task);
	}
	
	public String getTaskResolution(String taskName){
		return model.getTaskResolution(taskName);
	}
	
	public void setTaskResolution(String taskName, String resolution){
		model.setTaskResolution(taskName, resolution);
	}
	
	public boolean isTaskStarted(){
		return model.isTaskStarted();
	}
	
	public boolean isDayPaused(){
		return model.isDayPaused();
	}
	
	public void pauseDay(){
		model.pauseDay();
	}
	
	public void resetDay(){
		model.resetDay();
	}
	
	public String getSelectedTask(){
		return model.getSelectedTask();
	}
	
	public void setSelectedTask(String task){
		model.setSelectedTask(task);
	}
	
	public Map<String, List<Duration>> getTaskBreakdown(String taskName){
		return model.getTaskBreakdown(taskName);
	}
	
	public Map<String, Map<String, List<Duration>>> getAllTaskBreakdowns(){
		String[] tasks = getAllTasks();
		Map<String, Map<String, List<Duration>>> taskBreakdowns = new LinkedHashMap<String, Map<String, List<Duration>>>();
		for(int i=0; i<tasks.length; i++)
			taskBreakdowns.put(tasks[i], getTaskBreakdown(tasks[i]));
		return taskBreakdowns;
	}
	
	public Map<String, List<Duration>> getTodaysBreakdown(){
		return model.getTodaysBreakdown();
	}
	
	public Map<String, List<Duration>> getDayBreakdown(Calendar day){
		return model.getDayBreakdown(day);
	}
	
	public void saveAll(){
		model.saveAll();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof String){
			if(arg1.equals("stopStartTask")){
				model.stopStartTask();
			}
			else if(arg1.equals("pauseDay") || arg1.equals("resetDay")){
				saveAll();
				if(model.isTaskStarted()){
					model.stopCurrentTask();
				}
			}
			else if(arg1.equals("saveDay")){
				saveAll();
			}
		}
		else if(arg1 instanceof String[]){
			String[] args = (String[])arg1;
			if(args[0].equals("startTime")){
				model.changeStartTime(args[1]);
			}
			else if(args[0].equals("createTask")){
				model.addTask(args[1]);
				model.setCurrentTask(args[1]);
			}
			else if(args[0].equals("loadTask")){
				model.setCurrentTask(args[1]);
			}
			else if(args[0].equals("currentTask")){
				if(!model.getCurrentTask().equals(args[1]))
					model.changeTaskName(model.getCurrentTask(), args[1]);
			}
		}
	}
	
}