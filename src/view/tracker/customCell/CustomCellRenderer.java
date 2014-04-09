package view.tracker.customCell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import view.ViewDefinitions;

import controller.tracker.Controller;

import model.tracker.Duration;

public class CustomCellRenderer implements TreeCellRenderer{
	
	private JPanel nodePanel = null;
	private Controller controller;
	private boolean date;
	
	public CustomCellRenderer(Controller controller){
		this.controller = controller;
		date = false;
	}
	
	public CustomCellRenderer(Controller controller, boolean date){
		this.controller = controller;
		this.date = date;
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		nodePanel = new JPanel();
		nodePanel.setBackground(Color.white);
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
		int depth = root.getDepth();
		String rootValue = (String)root.getUserObject();
		
		Object valueObj = node.getUserObject();
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		Type nodeType = ((CustomTreeNode)node).getType();
		
		if(valueObj instanceof Duration){	/* Leaf node */
			addDuration(valueObj, depth == 2);
		}
		else if(nodeType == Type.TASK_INFO && node.isRoot()){	/* Task View and root */
			addRoot(valueObj);
		}
		else if(nodeType == Type.DAY_INFO && node.isRoot()){
			addDayRoot(valueObj);
		}
		else if(nodeType == Type.ALL_TASK_INFO && node.isRoot()){	/* All Task View and root */
			JLabel testLabel = new JLabel();
			testLabel.setText((String)valueObj);
			nodePanel.add(testLabel);
		}
		else if(nodeType == Type.ALL_TASK_INFO && parent != null && parent.isRoot()){		/* All Task View and a task */
			addTask(valueObj);
		}
		else if(date && parent != null && parent.isRoot()){	/* Date View and a task */
			addTask(valueObj);
		}
		else{	/* Sub headings (dates) */
			JLabel testLabel = new JLabel();
			testLabel.setText((String)valueObj);
			nodePanel.add(testLabel);
		}
		
		return nodePanel;
	}
	
	private JPanel durTimePanel(Duration dur, boolean editable){
		JPanel timePanel = new JPanel(new GridBagLayout());
		timePanel.setBackground(Color.white);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		JLabel startLabel = new JLabel();
		startLabel.setBackground(Color.white);
		startLabel.setText("Start: ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		timePanel.add(startLabel, constraints);
		
		constraints = new GridBagConstraints();
		
		JTextArea startTime = new JTextArea();
		startTime.setEditable(editable);
		startTime.setBackground(Color.white);
		startTime.setText(dur.getStart());
		constraints.gridx = 1;
		constraints.gridy = 0;
		timePanel.add(startTime, constraints);
		
		constraints = new GridBagConstraints();
		
		JLabel endLabel = new JLabel();
		endLabel.setBackground(Color.white);
		endLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		endLabel.setText("End: ");
		constraints.gridx = 2;
		constraints.gridy = 0;
		timePanel.add(endLabel, constraints);
		
		constraints = new GridBagConstraints();
		
		JTextArea endTime = new JTextArea();
		endTime.setEditable(editable);
		endTime.setBackground(Color.white);
		endTime.setText(dur.getEnd());
		constraints.gridx = 3;
		constraints.gridy = 0;
		timePanel.add(endTime, constraints);
		
		return timePanel;
	}
	
	
	private void addDuration(Object obj, boolean editable){
		Duration dur = (Duration)obj;
		
		nodePanel.setLayout(new BorderLayout());
		nodePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		
		JPanel timePanel = durTimePanel(dur, editable);
		timePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
		nodePanel.add(timePanel, BorderLayout.WEST);
		
		if(!date && editable){
			JButton log = new JButton();
			log.setText("Logged");
			log.setBackground(ViewDefinitions.button2Background);
			log.setForeground(ViewDefinitions.button2Foreground);
			log.setPreferredSize(new Dimension(80, 20));
			if(!controller.isTaskDurationLogged(dur.getTask(), dur.getIteration()))
				nodePanel.add(log, BorderLayout.EAST);
		}
//		else if(!date){
//			if(!controller.isTaskDurationLogged(dur.getTask(), dur.getIteration())){
//				JLabel logged = new JLabel();
//				logged.setText("Not logged");
//				nodePanel.add(logged, BorderLayout.EAST);
//			}
//		}
		else if(!controller.isTaskDurationLogged(dur.getTask(), dur.getIteration())){
			JLabel logged = new JLabel();
			logged.setText("Not logged");
			nodePanel.add(logged, BorderLayout.EAST);
		}
	}
	
	private void addRoot(Object obj){
		String rootValue = "No task selected";
		JLabel rootLabel = new JLabel();
		String timeString = "";
		if(obj != null){
			rootValue = (String)obj;
			double time = controller.getUnloggedTaskTime(rootValue) / 3600.0;
			if(time > 0.0){
				DecimalFormat df = new DecimalFormat("#0.00");
				timeString = " (" + df.format(time) + ")";
			}
		}
		rootLabel.setText(rootValue + timeString);
		rootLabel.setPreferredSize(new Dimension(217, 20));
		nodePanel.add(rootLabel, BorderLayout.WEST);
		
		if(!date && obj != null && controller.getAllTasks().length > 0){
			String[] resTypes = {"New", "Resolved", "Reassigned"};
			JComboBox<String> resolution = new JComboBox<String>(resTypes);
			resolution.setBackground(ViewDefinitions.button2Background);
			resolution.setForeground(ViewDefinitions.button2Foreground);
			resolution.setSelectedItem(controller.getTaskResolution(rootValue));
			resolution.setPreferredSize(new Dimension(100, 20));
			nodePanel.add(resolution, BorderLayout.EAST);
			
			if(!controller.isTaskUpToDate(rootValue)){
				JButton allLogged = new JButton();
				allLogged.setText("All Logged");
				allLogged.setBackground(ViewDefinitions.button2Background);
				allLogged.setForeground(ViewDefinitions.button2Foreground);
				allLogged.setPreferredSize(new Dimension(100, 20));
				nodePanel.add(allLogged, BorderLayout.EAST);
			}
			else{
				JLabel upToDate = new JLabel();
				upToDate.setText("Up to date");
				upToDate.setPreferredSize(new Dimension(100, 20));
				upToDate.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
				nodePanel.add(upToDate, BorderLayout.EAST);
			}
		}
	}
	
	private void addDayRoot(Object obj){
		String rootValue = "Today";
		JLabel rootLabel = new JLabel();
		if(obj != null)
			rootValue = (String)obj;
		rootLabel.setText(rootValue);
		rootLabel.setPreferredSize(new Dimension(150, 20));
		nodePanel.add(rootLabel, BorderLayout.WEST);
	}
	
	private void addTask(Object obj){
		String rootValue = "";
		String timeString = "";
		if(obj != null){
			rootValue = (String)obj;
			double time = controller.getUnloggedTaskTime(rootValue) / 3600.0;
			if(time > 0.0){
				DecimalFormat df = new DecimalFormat("#0.00");
				timeString = " (" + df.format(time) + ")";
			}
		}
		JLabel rootLabel = new JLabel();
		rootLabel.setText(rootValue + timeString);
		rootLabel.setPreferredSize(new Dimension(300, 20));
		nodePanel.add(rootLabel, BorderLayout.WEST);
		
		JLabel resolution = new JLabel();
		resolution.setText(controller.getTaskResolution(rootValue));
		nodePanel.add(resolution, BorderLayout.EAST);
	}
	
	public JPanel getPanel(){
		return nodePanel;
	}
	
}