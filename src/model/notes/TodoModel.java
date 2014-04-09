package model.notes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import view.notes.Todo;

public class TodoModel {
	
	private Todo todo;
	private List<Object[]> tasks;
	
	public TodoModel(Todo todo){
		this.todo = todo;
		tasks = new ArrayList<Object[]>();
	}
	
	public void addTask(String taskName, int taskPriority){
		addTask(taskName, taskPriority, false);
	}
	
	private void addTask(String taskName, int taskPriority, boolean taskCompletion){
		Object[] taskInfo = {taskName, taskPriority, taskCompletion};
		tasks.add(taskInfo);
		todo.addTask(taskName, taskPriority, taskCompletion);
	}
	
	public void updateTask(int row, int col, Object value){
		Object[] taskInfo = tasks.get(row);
		taskInfo[col] = value;
		tasks.set(row, taskInfo);
	}
	
	public void removeTasks(int[] rows){
		for(int i=rows.length-1; i>=0; i--){
			tasks.remove(rows[i]);
		}
		todo.deleteTask(rows);
	}
	
	public void loadTasks(){
		Scanner scan;
		try{
			File file = new File("taskData.txt");
			if(!file.exists())
				return;
			scan = new Scanner(file);
			while(scan.hasNext()){
				String line = scan.nextLine();
				String[] taskInfo = line.split("\0");
				addTask(taskInfo[0], Integer.parseInt(taskInfo[1]), Boolean.parseBoolean(taskInfo[2]));
			}
		}catch(FileNotFoundException e){
			return;
		}
	}
	
	public void saveTasks(){
		try{
			File fileData = new File("taskData.txt");
			if(fileData.exists())
				fileData.delete();
			FileWriter fw = new FileWriter(fileData);
			for(int i=0; i<tasks.size(); i++){
				Object[] taskInfo = tasks.get(i);
				fw.write((String)taskInfo[0]);
				fw.write("\0");
				fw.write(((Integer)taskInfo[1]).toString());
				fw.write("\0");
				fw.write(((Boolean)taskInfo[2]).toString());
				if(i < tasks.size() - 1){
					fw.write("\n");
				}
			}
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
