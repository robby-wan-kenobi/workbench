package controller.monitor;

import java.util.Map;

import utils.SystemInfo;
import view.monitor.Services;
import model.monitor.ServicesModel;

public class ServicesController{
	
	private ServicesModel model;
	
	public ServicesController(Services services){
		this.model = new ServicesModel(services);
	}
	
	public Map<String, Object[]> getServices(){
		return model.getServices();
	}
	
}
