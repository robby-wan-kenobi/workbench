package controller.monitor;

import java.util.Map;
import java.util.TimerTask;

import view.monitor.Tasks;
import model.monitor.TasksModel;

public class TasksController {
	
	private TasksModel model;
	
	private class TaskUpdate extends TimerTask{
		public TaskUpdate(){
		}
		public void run(){
			updateTasks();
		}
	}
	
	public TasksController(Tasks tasks){
		this.model = new TasksModel(tasks);
	}
	
	public void setInterval(int seconds){
		java.util.Timer taskTimer = new java.util.Timer();
		taskTimer.schedule(new TaskUpdate(), 0, seconds * 1000);
	}
	
	public Map<Integer, Object[]> getTasks(){
		return model.getTasks();
	}
	
	public Map<Integer, Object[]> getTasksComplete(){
		return model.getTasksComplete();
	}
	
	public void updateTasks(){
		model.updateTasks();
	}
	
	public String endTask(int pid){
		return model.endTask(pid);
	}

	public String endTaskTree(int pid){
		return model.endTaskTree(pid);
	}
	
}
