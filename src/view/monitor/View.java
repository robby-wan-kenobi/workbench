package view.monitor;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import view.ViewDefinitions;

@SuppressWarnings("serial")
public class View extends JTabbedPane{
	
	public View(){
		super(JTabbedPane.TOP);

		UIManager.put("TabbedPane.selected", ViewDefinitions.panel1Background);
		UIManager.put("TabbedPane.contentAreaColor", ViewDefinitions.panel1Background);
		UIManager.put("TabbedPane.highlight", Color.white);
		UIManager.put("TabbedPane.lightHighlight", Color.gray);
		UIManager.put("TabbedPane.shadow", Color.darkGray);
		UIManager.put("TabbedPane.darkShadow", Color.black);
		
		setBackground(ViewDefinitions.panel1Background);
		
		setName("Monitor");
		
		Tasks tasks = new Tasks();
		Services services = new Services();
		
		add(tasks);
		add(services);
	}
	
	public boolean windowClose(){
		return true;
	}
	
}