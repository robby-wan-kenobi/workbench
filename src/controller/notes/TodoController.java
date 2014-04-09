package controller.notes;

import model.notes.TodoModel;
import view.notes.Todo;

public class TodoController {
	
	private TodoModel model;
	
	public TodoController(Todo todo){
		this.model = new TodoModel(todo);
	}
	
	public void addTask(String taskName, int taskPriority){
		model.addTask(taskName, taskPriority);
	}
	
	public void updateTask(int row, int col, Object value){
		model.updateTask(row, col, value);
	}
	
	public void removeTasks(int[] rows){
		model.removeTasks(rows);
	}
	
	public void loadTasks(){
		model.loadTasks();
	}
	
	public void saveTasks(){
		model.saveTasks();
	}
}
