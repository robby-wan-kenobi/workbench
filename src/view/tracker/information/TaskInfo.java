package view.tracker.information;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

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
public class TaskInfo extends BaseInfo{
	
	private JComboBox<String> taskComboBox;
	
	private GeneralListener gl;
	
	private static String currentTask = null;

	public TaskInfo(Controller controller, GeneralListener gl) {
		super(controller,gl);
		this.gl = gl;
		setBackground(ViewDefinitions.panel2Background);
		if(currentTask == null)
			currentTask = controller.getCurrentTaskName();
		setContent(currentTask);
	}
	
	public TaskInfo(Controller controller, GeneralListener gl, String task) {
		super(controller, gl);
		this.gl = gl;
		currentTask = task;
		setBackground(ViewDefinitions.panel2Background);
		setContent(currentTask);
	}
	
	private void setContent(String task){
		
		JPanel taskPanel = new JPanel(new BorderLayout());
		taskPanel.setBackground(ViewDefinitions.panel2Background);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(577, 20));
		topPanel.setBackground(ViewDefinitions.panel2Background);
		
		JLabel dateLabel = new JLabel();
		dateLabel.setText("Task");
		dateLabel.setForeground(ViewDefinitions.button1Foreground);
		topPanel.add(dateLabel, BorderLayout.WEST);
		
		JPanel taskPicker = new JPanel();
		taskPicker.setLayout(new BoxLayout(taskPicker, BoxLayout.LINE_AXIS));
		taskPicker.setBackground(ViewDefinitions.panel2Background);
		
		String[] taskList = controller.getAllTasks();
		taskComboBox = new JComboBox<String>(taskList);
		taskComboBox.setBackground(ViewDefinitions.button1Background);
		taskComboBox.setForeground(ViewDefinitions.button1Foreground);
		taskComboBox.setSelectedItem(task);
		taskPicker.add(taskComboBox);
		
		JButton submitTask = new JButton();
		submitTask.setText("Go");
		submitTask.setBackground(ViewDefinitions.button1Background);
		submitTask.setForeground(ViewDefinitions.button1Foreground);
		submitTask.setPreferredSize(new Dimension(50, 20));
		submitTask.setActionCommand("changeTask");
		submitTask.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String task = (String) taskComboBox.getSelectedItem();
				gl.submit(arg0.getActionCommand(), task);
				controller.setSelectedTask(task);
			}
		});
		taskPicker.add(submitTask);
		
		topPanel.add(taskPicker, BorderLayout.EAST);
		
		taskPanel.add(topPanel, BorderLayout.NORTH);
		
		JTree tree;
		DefaultMutableTreeNode treeRoot = new CustomTreeNode(task, Type.TASK_INFO);
		Map<String, List<Duration>> taskInfo = controller.getTaskBreakdown(task);
		for(String date: taskInfo.keySet()){
			DefaultMutableTreeNode subNode = new CustomTreeNode(date, Type.TASK_INFO);
			List<Duration> value = taskInfo.get(date);
			for(Duration dur: value){
				DefaultMutableTreeNode durNode = new CustomTreeNode(dur, Type.TASK_INFO);
				subNode.add(durNode);
			}
			treeRoot.add(subNode);
		}
		DefaultTreeModel treeModel = new DefaultTreeModel(treeRoot);
		tree = new JTree(treeModel);
		CustomCellEditor editor = new CustomCellEditor(tree, controller, gl);
		tree.setCellEditor(editor);
		tree.setCellRenderer(new CustomCellRenderer(controller));
		tree.setEditable(true);
		tree.setBackground(Color.white);
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(577, 300));
		scrollPane.setBackground(Color.white);
		taskPanel.add(scrollPane, BorderLayout.CENTER);
		
		add(taskPanel);
	}
}
