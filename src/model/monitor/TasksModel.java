package model.monitor;

import java.util.Map;

import utils.SystemInfo;
import view.monitor.Tasks;

public class TasksModel {

	private Tasks tasks;
	
	public TasksModel(Tasks tasks){
		this.tasks = tasks;
	}
	
	public Map<Integer, Object[]> getTasks(){
		return SystemInfo.getTasks();
	}

	public Map<Integer, Object[]> getTasksComplete(){
		return SystemInfo.getTasksComplete();
	}
	
	public void updateTasks(){
		tasks.setTasks();
	}
	
	public String endTask(int pid){
		return SystemInfo.endTask(pid, false);
	}

	public String endTaskTree(int pid){
		return SystemInfo.endTask(pid, true);
	}
	
}
