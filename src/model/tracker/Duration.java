package model.tracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Duration{
	
	private String startDate;
	private String endDate;
	private String task;
	private int iteration;
	private boolean logged;
	
	public Duration(String start, String end){
		startDate = start;
		endDate = end;
		logged = false;
	}
	
	public Duration(String start, String end, String taskName, int iter, boolean logged){
		startDate = start;
		endDate = end;
		task = taskName;
		iteration = iter;
		this.logged = logged;
	}
	
	public String getTask(){
		return task;
	}
	
	public int getIteration(){
		return iteration;
	}
	
	public String getStart(){
		return startDate;
	}
	
	public String getEnd(){
		return endDate;
	}
	
	public boolean isLogged(){
		return logged;
	}
	
	// Returns difference in seconds
	public double getTime(){
		SimpleDateFormat startObj = new SimpleDateFormat("h:mm:ss a");
		SimpleDateFormat endObj = new SimpleDateFormat("h:mm:ss a");
		
		long startTime = 0L;
		long endTime = 0L;
		try {
			startTime = startObj.parse(startDate).getTime();
			endTime = endObj.parse(endDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return (double)(endTime - startTime) / 1000;
	}
	
	public String toString(){
		return "Start: " + startDate + "\nEnd: " + endDate;
	}
}