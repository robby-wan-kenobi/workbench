package view.tracker.customCell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import view.tracker.GeneralListener;

import model.tracker.Duration;

import controller.tracker.Controller;

public class CustomCellEditor extends AbstractCellEditor implements TreeCellEditor{
	
	private CustomCellRenderer renderer = null;
	private JTree tree;
	private Controller controller;
	private Object value;
	private boolean date;
	private GeneralListener gl;
	
	public CustomCellEditor(JTree tree, Controller controller, GeneralListener gl){
		this.tree = tree;
		this.tree.requestFocus();
		this.controller = controller;
		renderer = new CustomCellRenderer(controller);
		date = false;
		this.gl = gl;
	}
	
	public CustomCellEditor(JTree tree, Controller controller, GeneralListener gl, boolean date){
		this.tree = tree;
		this.tree.requestFocus();
		this.controller = controller;
		renderer = new CustomCellRenderer(controller, date);
		this.date = date;
		this.gl = gl;
	}
	
	public CustomCellRenderer getRenderer(){
		return renderer;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		
		this.value = value;
		
		JPanel editor = (JPanel)renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
		
		Component[] components = editor.getComponents();
		if(components.length == 1 && components[0] instanceof JPanel){
			components = ((JPanel)components[0]).getComponents();
		}
		for(int i=0; i<components.length; i++){
			if(components[i] instanceof JTextArea)
				configureJTextArea(components[i], ((CustomTreeNode)value).getType());
			else if(components[i] instanceof JButton)
				configureJButton(components[i]);
			else if(components[i] instanceof JComboBox)
				configureJComboBox(components[i]);
		}
		
		return editor;
	}
	
	@Override
	public Object getCellEditorValue() {
		return renderer.getPanel();
	}
	
	@Override
	public boolean isCellEditable(EventObject event){
		boolean returnValue = false;
		if(event instanceof MouseEvent){
			TreePath path = tree.getPathForLocation(((MouseEvent)event).getX(), ((MouseEvent)event).getY());
			if(path != null){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
				returnValue = (node != null && (node.isLeaf() || node.isRoot()));
			}
		}
		return returnValue;
	}
	
	private void configureJComboBox(Component jComboBox){
		final JComboBox<String> comboBox = (JComboBox<String>)jComboBox;
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				JComboBox<String> box = (JComboBox<String>)arg0.getSource();
				String selection = (String)box.getSelectedItem();
				controller.setTaskResolution(controller.getSelectedTask(), selection);
			}
		});
	}
	
	private void configureJButton(Component jButton){
		final JButton button = (JButton)jButton;
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
				JButton curButton = (JButton)arg0.getSource();
				if(node.getUserObject() instanceof Duration){
					Duration dur = (Duration)node.getUserObject();
					curButton.setEnabled(false);
					curButton.setVisible(false);
					controller.setTaskDurationLogged(dur.getTask(), dur.getIteration());
				}
				else{
					curButton.setEnabled(false);
					curButton.setVisible(false);
					JPanel parentPanel = (JPanel) curButton.getParent();
					controller.setTaskUpToDate((String)node.getUserObject());
					JLabel upToDate = new JLabel();
					upToDate.setText("Up to date");
					upToDate.setPreferredSize(new Dimension(100, 20));
					upToDate.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
					parentPanel.add(upToDate, BorderLayout.EAST);
				}
				gl.submitAction("taskInfo");
			}
		});
	}
	
	private void configureJTextArea(Component jTextArea, Type type){
		JTextArea textComp = (JTextArea)jTextArea;
		if(type == Type.DAY_INFO){
			textComp.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent arg0) {
				}
				@Override
				public void focusGained(FocusEvent arg0) {
				}
			});
		}
		else{
			textComp.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent arg0) {
				}
				@Override
				public void focusGained(FocusEvent arg0) {
				}
			});
		}
	}
	
}