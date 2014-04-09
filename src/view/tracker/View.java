package view.tracker;

import java.awt.BorderLayout;
import java.util.Observer;

import javax.swing.JPanel;

import model.tracker.Model;

import controller.tracker.Controller;

@SuppressWarnings("serial")
public class View extends JPanel{
	
	private JPanel topPanel;
	private JPanel infoPanel;
	private JPanel dayPanel;
	
	private Controller controller;
	
	public View(){
		super(new BorderLayout());
		
		GeneralListener gl = new GeneralListener();
		Model model = new Model(gl);
		controller = new Controller(model);
		
		gl.addObserver(controller);
		
		setName("Tracker");
		
		topPanel = new StatusBar(controller, gl);
		infoPanel = new Information(controller, gl);
		dayPanel = new DayPanel(controller, gl);
		
		setFocusable(true);
		addKeyListener(gl);
		gl.addObserver((Observer)topPanel);
		gl.addObserver((Observer)infoPanel);
		gl.addObserver((Observer)dayPanel);
		
		add(topPanel, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);
		add(dayPanel, BorderLayout.SOUTH);
	}
	
	public void windowClose(){
		controller.stopCurrentTask();
		controller.saveAll();
	}
	
}