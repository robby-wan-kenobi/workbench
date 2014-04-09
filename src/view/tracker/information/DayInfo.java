package view.tracker.information;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import view.ViewDefinitions;
import view.tracker.GeneralListener;
import view.tracker.customCell.CustomCellEditor;
import view.tracker.customCell.CustomCellRenderer;
import view.tracker.customCell.CustomTreeNode;
import view.tracker.customCell.Type;

import model.tracker.Duration;

import controller.tracker.Controller;

@SuppressWarnings("serial")
public class DayInfo extends BaseInfo{
	
	private JComboBox<String> monthDropDown;
	private JComboBox<Integer> dayDropDown;
	private JComboBox<Integer> yearDropDown;
	private GeneralListener gl;
	
	private static Calendar currentDay = null;
	
	public DayInfo(Controller controller, GeneralListener gl) {
		super(controller, gl);
		this.gl = gl;
		setBackground(ViewDefinitions.panel2Background);
		if(currentDay == null)
			currentDay = Calendar.getInstance();
		String label = getMonthName(currentDay.get(Calendar.MONTH)) + " " +
						currentDay.get(Calendar.DAY_OF_MONTH) + ", " +
						currentDay.get(Calendar.YEAR);
		setContent(label, currentDay);
	}
	
	public DayInfo(Controller controller, GeneralListener gl, String[] date) {
		super(controller, gl);
		this.gl = gl;
		setBackground(ViewDefinitions.panel2Background);
		Calendar day = Calendar.getInstance();
		String label = date[0] + " " + date[1] + ", " + date[2];
		day.set(Calendar.MONTH, getMonthNum(date[0]));
		day.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
		day.set(Calendar.YEAR, Integer.parseInt(date[2]));
		currentDay = day;
		setContent(label, currentDay);
	}
	
	private String getMonthName(int month){
		switch(month){
			case 0:
				return "January";
			case 1:
				return "February";
			case 2:
				return "March";
			case 3:
				return "April";
			case 4:
				return "May";
			case 5:
				return "June";
			case 6:
				return "July";
			case 7:
				return "August";
			case 8:
				return "September";
			case 9:
				return "October";
			case 10:
				return "November";
			case 11:
				return "December";
		}
		return "January";
	}
	
	private int getMonthNum(String month){
		if(month.equals("January"))
			return 0;
		else if(month.equals("February"))
			return 1;
		else if(month.equals("March"))
			return 2;
		else if(month.equals("April"))
			return 3;
		else if(month.equals("May"))
			return 4;
		else if(month.equals("June"))
			return 5;
		else if(month.equals("July"))
			return 6;
		else if(month.equals("August"))
			return 7;
		else if(month.equals("September"))
			return 8;
		else if(month.equals("October"))
			return 9;
		else if(month.equals("November"))
			return 10;
		else if(month.equals("December"))
			return 11;
		return 0;
	}
	
	private void setContent(String label, Calendar day){
		
		JPanel datePanel = new JPanel(new BorderLayout());
		datePanel.setBackground(ViewDefinitions.panel2Background);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(385, 20));
		topPanel.setBackground(ViewDefinitions.panel2Background);
		
		JLabel dateLabel = new JLabel();
		dateLabel.setText("Date");
		dateLabel.setForeground(ViewDefinitions.button1Foreground);
		topPanel.add(dateLabel, BorderLayout.WEST);
		
		JPanel datePicker = new JPanel();
		datePicker.setLayout(new BoxLayout(datePicker, BoxLayout.LINE_AXIS));
		datePicker.setBackground(ViewDefinitions.panel2Background);
		
		JButton submitToday = new JButton();
		submitToday.setText("Today");
		submitToday.setBackground(ViewDefinitions.button1Background);
		submitToday.setForeground(ViewDefinitions.button1Foreground);
		submitToday.setPreferredSize(new Dimension(70, 20));
		submitToday.setActionCommand("changeDate");
		submitToday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] date = new String[3];
				date[0] = getMonthName(Calendar.getInstance().get(Calendar.MONTH));
				date[1] = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				date[2] = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
				gl.submit(arg0.getActionCommand(), date);
			}
		});
		datePicker.add(submitToday);
		
		String[] months = {	"January", "February", "March", "April", "May", "June",
							"July", "August", "September", "October", "November", "December"};
		monthDropDown = new JComboBox<String>(months);
		monthDropDown.setBackground(ViewDefinitions.button1Background);
		monthDropDown.setForeground(ViewDefinitions.button1Foreground);
		monthDropDown.setSelectedItem(getMonthName(day.get(Calendar.MONTH)));
		datePicker.add(monthDropDown);
		
		Integer[] days = {	1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
							11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
							21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
							31};
		dayDropDown = new JComboBox<Integer>(days);
		dayDropDown.setBackground(ViewDefinitions.button1Background);
		dayDropDown.setForeground(ViewDefinitions.button1Foreground);
		dayDropDown.setSelectedItem(day.get(Calendar.DAY_OF_MONTH));
		datePicker.add(dayDropDown);
		
		Integer[] years = {	2010, 2011, 2012, 2013, 2014, 2015,
							2016, 2017, 2018, 2019, 2020};
		yearDropDown = new JComboBox<Integer>(years);
		yearDropDown.setBackground(ViewDefinitions.button1Background);
		yearDropDown.setForeground(ViewDefinitions.button1Foreground);
		yearDropDown.setSelectedItem(day.get(Calendar.YEAR));
		datePicker.add(yearDropDown);
		
		JButton submitDate = new JButton();
		submitDate.setText("Go");
		submitDate.setBackground(ViewDefinitions.button1Background);
		submitDate.setForeground(ViewDefinitions.button1Foreground);
		submitDate.setPreferredSize(new Dimension(50, 20));
		submitDate.setActionCommand("changeDate");
		submitDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] date = new String[3];
				date[0] = (String) monthDropDown.getSelectedItem();
				date[1] = Integer.toString((Integer) dayDropDown.getSelectedItem());
				date[2] = Integer.toString((Integer) yearDropDown.getSelectedItem());
				gl.submit(arg0.getActionCommand(), date);
			}
		});
		datePicker.add(submitDate);
		
		topPanel.add(datePicker, BorderLayout.EAST);
		
		datePanel.add(topPanel, BorderLayout.NORTH);
		
		JTree tree;
		
		DefaultMutableTreeNode treeRoot = new CustomTreeNode(label, Type.DAY_INFO);
		Map<String, List<Duration>> taskInfo = controller.getDayBreakdown(day);
		for(String date: taskInfo.keySet()){
			DefaultMutableTreeNode subNode = new CustomTreeNode(date, Type.DAY_INFO);
			List<Duration> value = taskInfo.get(date);
			for(Duration dur: value){
				DefaultMutableTreeNode durNode = new CustomTreeNode(dur, Type.DAY_INFO);
				subNode.add(durNode);
			}
			treeRoot.add(subNode);
		}
		DefaultTreeModel treeModel = new DefaultTreeModel(treeRoot);
		tree = new JTree(treeModel);
		CustomCellEditor editor = new CustomCellEditor(tree, controller, gl, true);
		tree.setCellEditor(editor);
		tree.setCellRenderer(new CustomCellRenderer(controller, true));
		tree.setEditable(true);
		tree.setBackground(Color.white);
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(577, 300));
		scrollPane.setBackground(Color.white);
		datePanel.add(scrollPane, BorderLayout.CENTER);
		
		add(datePanel);
	}
}
