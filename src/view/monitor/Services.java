package view.monitor;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import view.ViewDefinitions;
import controller.monitor.ServicesController;

@SuppressWarnings("serial")
public class Services extends JPanel{
	private ServicesController controller;
	private DefaultTableModel tm;
	private JTable servicesList;
	
	public Services(){
		super(new BorderLayout());
		
		controller = new ServicesController(this);
		
		setBackground(ViewDefinitions.panel1Background);
		
		setName("Services");
		
		setFocusable(true);
		
		UIManager.put("TableHeader.background", ViewDefinitions.panel1Background);
		UIManager.put("TableHeader.font", new Font(null, Font.BOLD, 12));
		//UIManager.put("Table.font", new Font(null, Font.PLAIN, 14));
		UIManager.put("Viewport.background", ViewDefinitions.panel1Background);
	}
}