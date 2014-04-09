package view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

@SuppressWarnings("serial")
public class View extends JTabbedPane{
	
	private model.tracker.Model trackerModel;
	private view.tracker.View trackerView;
	private view.notes.View notesView;
	
	public View(){
		super(JTabbedPane.TOP);

		UIManager.put("TabbedPane.selected", Color.lightGray);
		UIManager.put("TabbedPane.contentAreaColor", Color.lightGray);
		UIManager.put("TabbedPane.highlight", Color.white);
		UIManager.put("TabbedPane.lightHighlight", Color.gray);
		UIManager.put("TabbedPane.shadow", Color.darkGray);
		UIManager.put("TabbedPane.darkShadow", Color.black);
		
		view.tracker.GeneralListener tgl = new view.tracker.GeneralListener();
		trackerModel = new model.tracker.Model(tgl);
		controller.tracker.Controller trackerController = new controller.tracker.Controller(trackerModel);
		
		setBackground(view.ViewDefinitions.panel1Background);
		
		setUI(new BasicTabbedPaneUI() {
		   @Override
		   protected void installDefaults() {
		       super.installDefaults();
		   }
		});
		
		setBorder(BorderFactory.createEmptyBorder());
		setFocusable(true);
		
		trackerView = new view.tracker.View();
		notesView = new view.notes.View();
		
		add(trackerView);
		add(notesView);
		add(new view.music.View());
		add(new view.monitor.View());
		
	}
	
	public boolean windowClose(){
		trackerView.windowClose();
		return notesView.windowClose();
	}
}