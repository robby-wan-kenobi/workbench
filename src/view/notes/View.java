package view.notes;

import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import view.ViewDefinitions;

@SuppressWarnings("serial")
public class View extends JTabbedPane{
	
	private NotepadContainer notes;
	private Todo todo;
	
	public View(){
		super(JTabbedPane.TOP);

		UIManager.put("TabbedPane.selected", ViewDefinitions.panel1Background);
		UIManager.put("TabbedPane.contentAreaColor", ViewDefinitions.panel1Background);
		UIManager.put("TabbedPane.highlight", Color.white);
		UIManager.put("TabbedPane.lightHighlight", Color.gray);
		UIManager.put("TabbedPane.shadow", Color.darkGray);
		UIManager.put("TabbedPane.darkShadow", Color.black);
		
		setBackground(ViewDefinitions.panel1Background);
		
		//gl.addObserver(controller);
		
		setName("Notes");
		
		notes = new NotepadContainer();
		todo = new Todo();
		
		add(notes.getPane());
		add(todo);
	}
	
	public boolean windowClose(){
		todo.windowClose();
		return notes.windowClose();
	}
	
}