package view.tracker;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import view.ViewDefinitions;

import controller.tracker.Controller;

@SuppressWarnings("serial")
public class StatusBar extends BasePanel implements Observer{
	
	private final int xSize = 600;
	private final int ySize = 100;
	
	private final String TIME_CARD = "timeCard";
	private final String NEW_TASK_CARD = "newTaskCard";
	
	private JPanel newTaskPanel = null;
	private JPanel timePanel = null;
	private JPanel taskPanel = null;
	
	private JTextField newTaskName = null;
	private JTextArea taskName;
	private JTextArea taskTime;
	private JTextArea startTime;
	private JTextArea endTime;
	private JTextArea totalTime;
	private JButton stopStartButton;
	private JComboBox<String> existingTaskName;
	
	private GeneralListener gl;
	
	public StatusBar(Controller controller, GeneralListener gl){
		super(controller, gl);
		this.gl = gl;
		
		taskTime = null;
		
		setBackground(ViewDefinitions.panel1Background);
		setPreferredSize(new Dimension(xSize, ySize));
		
		setLayout(new BorderLayout());
		
		addCurrentTask();
		addButtonPanel();
	}
	
	private JPanel addCurrentTaskBot(){
		JPanel curTaskBot = new JPanel(new BorderLayout());
		curTaskBot.setPreferredSize(new Dimension(381, 70));
		curTaskBot.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
		curTaskBot.setBackground(ViewDefinitions.panel1Background);
		
		JPanel curTaskBotLeft = new JPanel(new GridBagLayout());
		curTaskBotLeft.setPreferredSize(new Dimension(225, 70));
		curTaskBotLeft.setBackground(ViewDefinitions.panel1Background);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		JLabel startTimeLabel = new JLabel();
		startTimeLabel.setText("Start");
		startTimeLabel.setBackground(ViewDefinitions.panel1Background);
		startTimeLabel.setPreferredSize(new Dimension(75, 20));
		startTimeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		curTaskBotLeft.add(startTimeLabel, constraints);
		
		constraints = new GridBagConstraints();
		
		startTime = new JTextArea();
		startTime.setBackground(ViewDefinitions.panel1Background);
		startTime.setText("--:--:--");
		startTime.addFocusListener(gl);
		startTime.addKeyListener(gl);
		startTime.setName("startTime");
		startTime.setEditable(false);
		startTime.setFont(new Font("Arial", Font.ITALIC, 12));
		startTime.setPreferredSize(new Dimension(120, 20));
		startTime.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		startTime.addKeyListener(gl);
		constraints.gridwidth = 1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		curTaskBotLeft.add(startTime, constraints);
		
		constraints = new GridBagConstraints();
		
		JLabel endTimeLabel = new JLabel();
		endTimeLabel.setBackground(ViewDefinitions.panel1Background);
		endTimeLabel.setText("End");
		endTimeLabel.setPreferredSize(new Dimension(75, 20));
		endTimeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		curTaskBotLeft.add(endTimeLabel, constraints);
		
		constraints = new GridBagConstraints();
		
		endTime = new JTextArea();
		endTime.setBackground(ViewDefinitions.panel1Background);
		endTime.setText("--:--:--");
		endTime.setEditable(false);
		endTime.setFont(new Font("Arial", Font.ITALIC, 12));
		endTime.setPreferredSize(new Dimension(120, 20));
		endTime.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		endTime.addKeyListener(gl);
		constraints.gridwidth = 1;
		constraints.gridx = 1;
		constraints.gridy = 1;
		curTaskBotLeft.add(endTime, constraints);
		
		constraints = new GridBagConstraints();
		
		JLabel totalTimeLabel = new JLabel();
		totalTimeLabel.setBackground(ViewDefinitions.panel1Background);
		totalTimeLabel.setText("Total");
		totalTimeLabel.setPreferredSize(new Dimension(75, 20));
		totalTimeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		curTaskBotLeft.add(totalTimeLabel, constraints);
		
		constraints = new GridBagConstraints();
		
		totalTime = new JTextArea();
		totalTime.setBackground(ViewDefinitions.panel1Background);
		totalTime.setText("00:00:00");
		totalTime.setEditable(false);
		totalTime.setFont(new Font("Arial", Font.ITALIC, 12));
		totalTime.setPreferredSize(new Dimension(135, 20));
		totalTime.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		constraints.gridwidth = 1;
		constraints.gridx = 1;
		constraints.gridy = 2;
		curTaskBotLeft.add(totalTime, constraints);
		
		JPanel curTaskBotRight = new JPanel();
		curTaskBotRight.setLayout(new BoxLayout(curTaskBotRight, BoxLayout.Y_AXIS));
		curTaskBotRight.setPreferredSize(new Dimension(156, 50));
		curTaskBotRight.setBackground(ViewDefinitions.panel1Background);
		curTaskBotRight.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		stopStartButton = new JButton();
		stopStartButton.setBackground(ViewDefinitions.button1Background);
		stopStartButton.setForeground(ViewDefinitions.button1Foreground);
		stopStartButton.setText("Start Task");
		stopStartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		stopStartButton.addActionListener(gl);
		stopStartButton.setActionCommand("stopStartTask");
		curTaskBotRight.add(stopStartButton);
		
		curTaskBotRight.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JButton newTaskButton = new JButton();
		newTaskButton.setBackground(ViewDefinitions.button1Background);
		newTaskButton.setForeground(ViewDefinitions.button1Foreground);
		newTaskButton.setText("New Task");
		newTaskButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newTaskButton.addActionListener(gl);
		newTaskButton.setActionCommand("newTask");
		curTaskBotRight.add(newTaskButton);
		
		curTaskBotRight.add(Box.createRigidArea(new Dimension(0, 8)));
		
		curTaskBot.add(curTaskBotLeft, BorderLayout.WEST);
		curTaskBot.add(curTaskBotRight, BorderLayout.EAST);
		
		return curTaskBot;
	}
	
	private JPanel addCurrentTaskTop(){
		JPanel curTaskTop = new JPanel(new BorderLayout());
		curTaskTop.setPreferredSize(new Dimension(381, 35));
		curTaskTop.setBackground(ViewDefinitions.panel1Background);
		curTaskTop.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		taskName = new JTextArea("Current Task");
		taskName.setBackground(ViewDefinitions.panel1Background);
		taskName.setEditable(true);
		taskName.addFocusListener(gl);
		taskName.addKeyListener(gl);
		taskName.setName("currentTask");
		taskName.setFont(new Font("Arial", Font.ITALIC, 18));
		taskName.setPreferredSize(new Dimension(225, 35));
		curTaskTop.add(taskName, BorderLayout.WEST);
		
		taskTime = new JTextArea("00:00:00 (0.00)");
		taskTime.setBackground(ViewDefinitions.panel1Background);
		taskTime.setEditable(false);
		taskTime.setFont(new Font("Arial", Font.ITALIC, 18));
		taskTime.setPreferredSize(new Dimension(125, 35));
		curTaskTop.add(taskTime, BorderLayout.EAST);
		
		return curTaskTop;
	}
	
	private void addCurrentTask(){
		taskPanel = new JPanel(new CardLayout());
		taskPanel.setPreferredSize(new Dimension(381, 100));
		
		timePanel = getTimePanel();
		newTaskPanel = getNewTaskPanel();
		
		taskPanel.add(newTaskPanel, NEW_TASK_CARD);
		taskPanel.add(timePanel, TIME_CARD);
		
		add(taskPanel, BorderLayout.WEST);
	}
	
	private void showBadTaskInput(String message){
		JOptionPane.showMessageDialog(this, message, "Invalid Task", JOptionPane.WARNING_MESSAGE);
	}
	
	private JPanel getNewTaskPanel(){
		JPanel newTaskPanel = new JPanel(new GridBagLayout());
		newTaskPanel.setPreferredSize(new Dimension(381, 100));
		newTaskPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		newTaskPanel.setBackground(ViewDefinitions.panel1Background);

		GridBagConstraints constraints = new GridBagConstraints();
		
		JLabel title = new JLabel();
		title.setBackground(ViewDefinitions.panel1Background);
		title.setText("Start a task");
		title.setFont(new Font("Arial", Font.ITALIC, 18));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		newTaskPanel.add(title, constraints);
		
		constraints = new GridBagConstraints();
		
		newTaskName = new JTextField(24);
		newTaskName.setEditable(true);
		newTaskName.setSize(20, 10);
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		newTaskPanel.add(newTaskName, constraints);
		
		constraints = new GridBagConstraints();
		
		JButton createTask = new JButton();
		createTask.setText("Create");
		createTask.setBackground(ViewDefinitions.button1Background);
		createTask.setForeground(ViewDefinitions.button1Foreground);
		createTask.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newTask = newTaskName.getText().trim();
				if(newTask.equals(""))
					showBadTaskInput("Please enter a name for the task.");
				else
					gl.submit(arg0.getActionCommand(), newTask);
			}
		});
		createTask.setActionCommand("createTask");
		constraints.gridx = 1;
		constraints.gridy = 1;
		newTaskPanel.add(createTask, constraints);
		
		constraints = new GridBagConstraints();
		
		String[] taskList = controller.getAllTasks();
		existingTaskName = new JComboBox<String>(taskList);
		existingTaskName.setPreferredSize(new Dimension(160, 20));
		existingTaskName.setBackground(ViewDefinitions.button1Background);
		existingTaskName.setForeground(ViewDefinitions.button1Foreground);
		existingTaskName.setEditable(false);
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		newTaskPanel.add(existingTaskName, constraints);
		
		constraints = new GridBagConstraints();
		
		JButton loadTask = new JButton();
		loadTask.setText("Load");
		loadTask.setBackground(ViewDefinitions.button1Background);
		loadTask.setForeground(ViewDefinitions.button1Foreground);
		loadTask.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String existingTask = (String)existingTaskName.getSelectedItem();
				if(existingTask == null || existingTask.trim().equals(""))
					showBadTaskInput("Please select a valid task or create a new one.");
				else
					gl.submit(arg0.getActionCommand(), existingTask);
			}
		});
		loadTask.setActionCommand("loadTask");
		constraints.weighty = 1.0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		newTaskPanel.add(loadTask, constraints);
		
		return newTaskPanel;
	}
	
	private JPanel getTimePanel(){
		JPanel curTimePanel = new JPanel(new BorderLayout());
		curTimePanel.setPreferredSize(new Dimension(381, 100));
		
		JPanel curTaskTop = addCurrentTaskTop();
		curTimePanel.add(curTaskTop, BorderLayout.NORTH);
		
		JPanel curTaskBot = addCurrentTaskBot();
		curTimePanel.add(curTaskBot, BorderLayout.SOUTH);
		
		return curTimePanel;
	}
	
	private void addButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(210, 100));
		buttonPanel.setBackground(ViewDefinitions.panel1Background);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 4)));
		
		JButton taskViewButton = new JButton();
		taskViewButton.setText("Edit Task");
		taskViewButton.setBackground(ViewDefinitions.button1Background);
		taskViewButton.setForeground(ViewDefinitions.button1Foreground);
		taskViewButton.addActionListener(gl);
		taskViewButton.setActionCommand("taskInfo");
		taskViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(taskViewButton);
		
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JButton dayViewButton = new JButton();
		dayViewButton.setText("Day Info");
		dayViewButton.setBackground(ViewDefinitions.button1Background);
		dayViewButton.setForeground(ViewDefinitions.button1Foreground);
		dayViewButton.addActionListener(gl);
		dayViewButton.setActionCommand("dayInfo");
		dayViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(dayViewButton);
		
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JButton allTaskViewButton = new JButton();
		allTaskViewButton.setText("All Task Info");
		allTaskViewButton.setBackground(ViewDefinitions.button1Background);
		allTaskViewButton.setForeground(ViewDefinitions.button1Foreground);
		allTaskViewButton.addActionListener(gl);
		allTaskViewButton.setActionCommand("allTaskInfo");
		allTaskViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(allTaskViewButton);
		
		add(buttonPanel, BorderLayout.EAST);
	}
	
	private String currentTime(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		return sdf.format(calendar.getTime());
	}
	
	private void clearStartEnd(){
		startTime.setText("--:--:--");
		endTime.setText("--:--:--");
	}
	
	private double dateStringToDouble(String date){
		String[] pieces = date.split(":");
		double hours = Double.parseDouble(pieces[0]);
		double minutes = Double.parseDouble(pieces[1]);
		double seconds = Double.parseDouble(pieces[2]);
		double time = hours;
		time += (minutes/60.0);
		time += (seconds/3600.0);
		return time;
	}
	
	// 01:59:41 = 1.0 + 0.9833333 + 0.01138888 = 1.994722
	
	private void setTimes(String curTimeString){
		DecimalFormat df = new DecimalFormat("#0.00");
		// TODO: can put rounding here
		double curTimeDouble = dateStringToDouble(curTimeString);
		taskTime.setText(curTimeString + " (" + df.format(curTimeDouble) + ")");
		long currentTime = 0;
		try {
			currentTime = (new SimpleDateFormat("hh:mm:ss").parse(curTimeString).getTime() - 25200000) / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		double timeDouble = controller.getTaskTime(controller.getCurrentTaskName()) + currentTime;
		long time = (long)timeDouble;
		long hours = time / 3600;
		time -= (hours*3600);
		long minutes = time / 60;
		long seconds = time % 60;
		String timeString = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		double timeDecimal = timeDouble / 3600.0;
		// TODO: double-check the decimal value
		totalTime.setText(timeString + " (" + df.format(timeDecimal) + ")");
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof String){
			if(arg1.equals("stopStartTask") || arg1.equals("pauseDay") || arg1.equals("resetDay")){
				if(controller.isTaskStarted()){
					stopStartButton.setText("Start Task");
					endTime.setText(currentTime());
				}else if(!controller.isTaskStarted() && arg1.equals("stopStartTask")){
					stopStartButton.setText("Stop Task");
					startTime.setText(currentTime());
					endTime.setText("--:--:--");
				}
			}
			else if(arg1.equals("invalidTime")){
				JOptionPane.showMessageDialog(this, 
												"Invalid time format. Use hh:mm:ss.", 
												"Invalid Time Format", 
												JOptionPane.WARNING_MESSAGE);
			}
			else if(arg1.equals("taskChanged")){
				CardLayout cl = (CardLayout)taskPanel.getLayout();
				cl.show(taskPanel, TIME_CARD);
				taskName.setText(controller.getCurrentTaskName());
				clearStartEnd();
				setTimes("00:00:00");
			}
			else if(arg1.equals("newTask")){
				boolean stopTask = true;
				if(controller.isTaskStarted()){
					int confirm = JOptionPane.showConfirmDialog(this, 
												"Stop current task first?", 
												"Stop Task", 
												JOptionPane.WARNING_MESSAGE);
					stopTask = (confirm == JOptionPane.YES_OPTION);
				}
				if(stopTask){
					controller.stopTask(controller.getCurrentTaskName());
					// Add all the tasks again
					String[] taskList = controller.getAllTasks();
					existingTaskName.removeAllItems();
					for(int i=0; i<taskList.length; i++)
						existingTaskName.addItem(taskList[i]);
					
					CardLayout cl = (CardLayout)taskPanel.getLayout();
					cl.show(taskPanel, NEW_TASK_CARD);
					newTaskName.setText("");
				}
			}
		}
		if(arg1 instanceof String[]){
			String type = ((String[])arg1)[0];
			if(type.equals("task")){
				String curTimeString = ((String[])arg1)[1];
				setTimes(curTimeString);
			}
			if(type.equals("currentTask")){
				requestFocusInWindow();
			}
		}
		
	}
}