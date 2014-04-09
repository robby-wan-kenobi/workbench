package view.music;

import java.awt.BorderLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class View extends JPanel{
	
//	private JPanel topPanel;
//	private JPanel infoPanel;
//	private JPanel dayPanel;
	
	public View(/*Controller controller, TrackerGeneralListener gl*/){
		super(new BorderLayout());
		
		//gl.addObserver(controller);
		
		setName("Music");
		
		setFocusable(true);
		
		/*topPanel = new StatusBar(controller, gl);
		infoPanel = new Information(controller, gl);
		dayPanel = new DayPanel(controller, gl);
		
		addKeyListener(gl);
		gl.addObserver((Observer)topPanel);
		gl.addObserver((Observer)infoPanel);
		gl.addObserver((Observer)dayPanel);
		
		add(topPanel, BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);
		add(dayPanel, BorderLayout.SOUTH);*/
	}
}