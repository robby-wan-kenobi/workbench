package view.tracker;

import javax.swing.JPanel;

import controller.tracker.Controller;

@SuppressWarnings("serial")
public class BasePanel extends JPanel{
	
	protected Controller controller;
	protected GeneralListener gl;
	
	public BasePanel(Controller controller, GeneralListener gl){
		this.controller = controller;
		this.gl = gl;
	}
}