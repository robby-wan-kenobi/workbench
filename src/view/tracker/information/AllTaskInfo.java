package view.tracker.information;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

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
public class AllTaskInfo extends BaseInfo{

	public AllTaskInfo(Controller controller, GeneralListener gl) {
		super(controller, gl);
		setBackground(ViewDefinitions.panel2Background);
		setContent();
	}
	
	private void setContent(){
		JTree tree;
		DefaultMutableTreeNode treeRoot = new CustomTreeNode("Tasks", Type.ALL_TASK_INFO);
		Map<String, Map<String, List<Duration>>> allTaskInfo = controller.getAllTaskBreakdowns();
		for(String task: allTaskInfo.keySet()){
			Map<String, List<Duration>> taskInfo = allTaskInfo.get(task);
			DefaultMutableTreeNode taskNode = new CustomTreeNode(task, Type.ALL_TASK_INFO);
			for(String date: taskInfo.keySet()){
				DefaultMutableTreeNode subNode = new CustomTreeNode(date, Type.ALL_TASK_INFO);
				List<Duration> value = taskInfo.get(date);
				for(Duration dur: value){
					DefaultMutableTreeNode durNode = new CustomTreeNode(dur, Type.ALL_TASK_INFO);
					subNode.add(durNode);
				}
				taskNode.add(subNode);
			}
			treeRoot.add(taskNode);
		}
		DefaultTreeModel treeModel = new DefaultTreeModel(treeRoot);
		tree = new JTree(treeModel);
		CustomCellEditor editor = new CustomCellEditor(tree, controller, gl);
		tree.setCellEditor(editor);
		tree.setCellRenderer(new CustomCellRenderer(controller));
		tree.setEditable(true);
		tree.setBackground(Color.white);
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(577, 320));
		scrollPane.setBackground(Color.white);
		add(scrollPane);
	}
}
