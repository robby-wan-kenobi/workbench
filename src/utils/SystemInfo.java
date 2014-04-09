package utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarPermissionDeniedException;

public class SystemInfo {
	
	private static int skipWhitespace(BufferedInputStream bis) throws IOException{
		char inChar;
		int inInt;
		do{
			inInt = bis.read();
			inChar = (char)inInt;
		}while(Character.isWhitespace(inChar));
		return inInt;
	}
	
	private static int skipNonWhitespace(BufferedInputStream bis) throws IOException{
		char inChar;
		int inInt;
		do{
			inInt = bis.read();
			inChar = (char)inInt;
		}while(!Character.isWhitespace(inChar));
		return inInt;
	}

	private static int skipUntil(BufferedInputStream bis, char stopChar) throws IOException{
		int inInt;
		char inChar;
		do{
			inInt = bis.read();
			inChar = (char)inInt;
		}while(inChar != stopChar);
		return inInt;
	}
	
	// get "\Memory\Available bytes", "\process(*)\ID Process", "\process(*)\working set - private", "\process(*)\% processor time", "\process(_total)\% processor time"
	public static Map<Integer, Object[]> getTasksComplete(){
		List<String> taskNames = new ArrayList<String>();
		List<Integer> pids = new ArrayList<Integer>();
		List<Long> memUsages = new ArrayList<Long>();
		List<Long> cpuUsages = new ArrayList<Long>();
		Map<Integer, Object[]> taskMap = new LinkedHashMap<Integer, Object[]>();
		Process p;
		try {
			p = Runtime.getRuntime().exec("typeperf \"\\process(*)\\ID Process\" \"\\process(*)\\working set\" \"\\process(*)\\% processor time\" \"\\Memory\\Available bytes\" -sc 1");
			BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
			int inInt = bis.read();
			char inChar = (char)inInt;
			int lineNum = -1;
			while(inInt != -1){
				if(inChar == '\n')
					++lineNum;
				if(lineNum == 0){
					String taskName = "";
					while(!taskName.equals("_Total")){
						taskName = "";
						inInt = skipUntil(bis, ',');
						inChar = (char)inInt;
						// inChar should be ',', parse until the first '('
						inInt = skipUntil(bis, '(');
						inChar = (char)inInt;
						// inChar should be '('
						inInt = bis.read();
						inChar = (char)inInt;
						while(inChar != ')'){
							taskName += inChar;
							inInt = bis.read();
							inChar = (char)inInt;
						}
						taskNames.add(taskName);
					}
					inInt = skipUntil(bis, '\n');
					inChar = (char)inInt;
					continue;
				}
				if(lineNum == 1){
					String pid;
					String mem;
					String cpu;
					inInt = skipUntil(bis, ',');
					inChar = (char)inInt;
					
					while(pids.size() < taskNames.size()){
						inInt = skipUntil(bis, '"');
						inChar = (char)inInt;
						
						pid = "";
						
						// inChar should be '"', parse until the next '"'
						inInt = bis.read();
						inChar = (char)inInt;
						while(inChar != '"'){
							pid += inChar;
							inInt = bis.read();
							inChar = (char)inInt;
						}
						pids.add(Integer.parseInt(pid.contains(".") ? pid.substring(0, pid.indexOf('.')) : pid));
					}
					
					while(memUsages.size() < taskNames.size()){
						inInt = skipUntil(bis, '"');
						inChar = (char)inInt;
						
						mem = "";
						
						// inChar should be '"', parse until the next '"'
						inInt = bis.read();
						inChar = (char)inInt;
						while(inChar != '"'){
							mem += inChar;
							inInt = bis.read();
							inChar = (char)inInt;
						}
						memUsages.add(Long.parseLong(mem.contains(".") ? mem.substring(0, mem.indexOf('.')) : mem));
					}
					
					while(cpuUsages.size() < taskNames.size()){
						inInt = skipUntil(bis, '"');
						inChar = (char)inInt;
						
						cpu = "";
						
						// inChar should be '"', parse until the next '"'
						inInt = bis.read();
						inChar = (char)inInt;
						while(inChar != '"'){
							cpu += inChar;
							inInt = bis.read();
							inChar = (char)inInt;
						}
						cpuUsages.add(Long.parseLong(cpu.contains(".") ? cpu.substring(0, cpu.indexOf('.')) : cpu));
					}
					
					inInt = skipUntil(bis, '"');
					inChar = (char)inInt;
					
					mem = "";
					
					// inChar should be '"', parse until the next '"'
					inInt = bis.read();
					inChar = (char)inInt;
					while(inChar != '"'){
						mem += inChar;
						inInt = bis.read();
						inChar = (char)inInt;
					}
					
					memUsages.add(Long.parseLong(mem.contains(".") ? mem.substring(0, mem.indexOf('.')) : mem));
					
					inInt = skipUntil(bis, '\n');
					inChar = (char)inInt;
					break;
				}
				inInt = bis.read();
				inChar = (char)inInt;
			}
			bis.close();
			p.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<memUsages.size(); i++){
			if(i == memUsages.size()-1){
				Object[] totalMemory = {null, -2, memUsages.get(i), null};
				taskMap.put(new Integer(-2), totalMemory);
				break;
			}
			Integer curPID = pids.get(i);
			if(taskNames.get(i).equals("_Total"))
				curPID = new Integer(-1);
			Object[] data = {	taskNames.get(i), 
								curPID, 
								(long)(((double)memUsages.get(i) / (double)memUsages.get(memUsages.size()-1)) * 1024000.0),
								((double)cpuUsages.get(i) / (double)cpuUsages.get(cpuUsages.size()-1)) * 100.0};
			taskMap.put(pids.get(i), data);
		}
		return taskMap;
	}
	
	// Returns a map of PID -> {task, PID, memUsage}
	public static Map<Integer, Object[]> getTasks(){
		Map<Integer, Object[]> taskMap = new LinkedHashMap<Integer, Object[]>();
		List<String> pids = new ArrayList<String>();
		Sigar sigar = new Sigar();
			long[] processes = null;
			try{
				processes = sigar.getProcList();
			}catch(Exception e){}
			for(int i=0; i<processes.length; i++){
				try {
					String processName = sigar.getProcState(processes[i]).getName();
					long memUsage = sigar.getProcMem(processes[i]).getResident() / 1024;
//					ProcCpu procCpu = sigar.getProcCpu(processes[i]);
//					procCpu.getPercent();
//					procCpu = sigar.getProcCpu(processes[i]);
//					double cpuUsage = procCpu.getPercent();
//					System.out.println("pid: " + processes[i] + ", name: " + processName + ", memUsage: " + memUsage + ", cpu: " + cpuUsage*1.0e300*1.0e18);
					Object[] processData = {processName, (int)processes[i], memUsage};
					taskMap.put(new Integer((int)processes[i]), processData);
				} catch (Exception e1) {
//					System.out.println("Bad process: " + processes[i]);
				}
			}
		
		return taskMap;
	}
	
	// Returns a map of PID -> {serviceName, displayName, state, PID};
	public static Map<String, Object[]> getServices(){
		Map<String, Object[]> serviceMap = new LinkedHashMap<String, Object[]>();
		try {
			Process p = Runtime.getRuntime().exec("sc queryex state= all");
			BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
			int inInt = bis.read();
			String serviceName = "";
			String displayName = "";
			String state = "";
			String pid = "";
			int lineNum = -1;
			while(inInt != -1){
				char inChar = (char)inInt;
				if(lineNum == 0){
					// find service name
					boolean foundColon = false;
					while(inChar != '\n'){
						if(foundColon)
							serviceName += inChar;
						if(inChar == ':')
							foundColon = true;
						inInt = bis.read();
						inChar = (char)inInt;
					}
					inInt = bis.read();
					++lineNum;
				}
				else if(lineNum == 1){
					// find display name
					boolean foundColon = false;
					while(inChar != '\n'){
						if(foundColon)
							displayName += inChar;
						if(inChar == ':')
							foundColon = true;
						inInt = bis.read();
						inChar = (char)inInt;
					}
					inInt = bis.read();
					++lineNum;
				}
				else if(lineNum == 3){
					// find state
					boolean foundColon = false;
					while(inChar != '\n'){
						if(foundColon){
							state += inChar;
							if(state.length() == 2)
								foundColon = false;
						}
						if(inChar == ':')
							foundColon = true;
						inInt = bis.read();
						inChar = (char)inInt;
					}
					inInt = bis.read();
					++lineNum;
				}
				else if((state.trim().equals("1") && lineNum == 8) || (!state.trim().equals("1") && lineNum == 9)){
					// find pid
					boolean foundColon = false;
					while(inChar != '\n'){
						if(foundColon)
							pid += inChar;
						if(inChar == ':')
							foundColon = true;
						inInt = bis.read();
						inChar = (char)inInt;
					}
					inInt = bis.read();
					++lineNum;
				}
				else{
					if(inChar == '\n')
						++lineNum;
					if((state.trim().equals("1") && lineNum == 11) || lineNum == 12){
						// save and start over
						Object[] values = {serviceName.trim(), displayName.trim(), Integer.parseInt(state.trim()), Integer.parseInt(pid.trim())};
						serviceMap.put(serviceName, values);
						lineNum = 0;
						serviceName = "";
						displayName = "";
						state = "";
						pid = "";
					}
					inInt = bis.read();
				}
			}
			bis.close();
			p.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serviceMap;
	}
	
	public static String endTask(int pid, boolean killSubTasks){
		String result = "";
		String killAll = "";
		Process p;
		if(killSubTasks)
			killAll = " /T";
		try {
			p = Runtime.getRuntime().exec("taskkill /PID " + pid + "" + killAll);
			BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
			int inInt = bis.read();
			char inChar = (char)inInt;
			while(inInt != -1){
				result += inChar;
				inInt = bis.read();
				inChar = (char)inInt;
			}
			bis.close();
			p.destroy();
		} catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}
	
}
