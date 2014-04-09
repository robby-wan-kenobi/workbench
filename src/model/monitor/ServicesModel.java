package model.monitor;

import java.util.Map;

import utils.SystemInfo;
import view.monitor.Services;

public class ServicesModel {

	public ServicesModel(Services services){
		
	}
	
	public Map<String, Object[]> getServices(){
		return SystemInfo.getServices();
	}
	
}
