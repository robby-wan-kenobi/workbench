package view.notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;

import controller.notes.TodoController;

import view.ViewDefinitions;

@SuppressWarnings("serial")
public class Todo extends JPanel{
	private JTextField taskName;
	private JComboBox<Integer> taskPriority;
	
	private TodoController controller;
	private DefaultTableModel tm;
	private JTable todoList;
	
	public Todo(){
		super(new BorderLayout());
		
		controller = new TodoController(this);
		
		setBackground(ViewDefinitions.panel1Background);
		
		setName("Todo");
		
		setFocusable(true);
		
		UIManager.put("TableHeader.background", ViewDefinitions.panel1Background);
		UIManager.put("TableHeader.font", new Font(null, Font.BOLD, 12));
		//UIManager.put("Table.font", new Font(null, Font.PLAIN, 14));
		UIManager.put("Viewport.background", ViewDefinitions.panel1Background);
		
		JPanel topPane = new JPanel();
		topPane.setLayout(new BoxLayout(topPane, BoxLayout.LINE_AXIS));
		topPane.setBackground(ViewDefinitions.panel1Background);
		topPane.setPreferredSize(new Dimension(400, 35));
		topPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
		topPane.add(spacer);
		
		taskName = new JTextField(30);
		taskName.setEditable(true);
		taskName.setSize(20, 10);
		taskName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					controller.addTask(taskName.getText(), (Integer)taskPriority.getSelectedItem());
				}
			}
		});
		topPane.add(taskName);
		
		spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
		topPane.add(spacer);
		topPane.add(spacer);
		
		Integer[] priorities = {1, 2, 3, 4, 5};
		taskPriority = new JComboBox<Integer>(priorities);
		taskPriority.setBackground(ViewDefinitions.button2Background);
		taskPriority.setForeground(ViewDefinitions.button2Foreground);
		taskPriority.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					controller.addTask(taskName.getText(), (Integer)taskPriority.getSelectedItem());
				}
			}
		});
		topPane.add(taskPriority);
		
		spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
		topPane.add(spacer);
		topPane.add(spacer);
		
		JButton addTask = new JButton();
		addTask.setBackground(ViewDefinitions.button2Background);
		addTask.setForeground(ViewDefinitions.button2Foreground);
		addTask.setText("Add Task");
		addTask.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.addTask(taskName.getText(), (Integer)taskPriority.getSelectedItem());
			}
		});
		topPane.add(addTask);
		
		spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
		topPane.add(spacer);
		
		JButton removeTasks = new JButton();
		removeTasks.setBackground(ViewDefinitions.button2Background);
		removeTasks.setForeground(ViewDefinitions.button2Foreground);
		removeTasks.setText("Remove Selected Tasks");
		removeTasks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.removeTasks(todoList.getSelectedRows());
			}
		});
		topPane.add(removeTasks);
		
		spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
		topPane.add(spacer);
		
		tm = new DefaultTableModel(){
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int c){
				return getValueAt(0, c).getClass();
			}
		};
		tm.addColumn("Task");
		tm.addColumn("Priority");
		tm.addColumn("Complete");
		
		todoList = new JTable(tm);
		todoList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		todoList.setAutoCreateRowSorter(true);
		todoList.setSelectionBackground(ViewDefinitions.button2Background);
		todoList.setSelectionForeground(ViewDefinitions.button2Foreground);
		todoList.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				if(arg0.getColumn() >= 0){
					int row = arg0.getFirstRow();
					int col = arg0.getColumn();
					Object value = ((DefaultTableModel)arg0.getSource()).getValueAt(row, col);
					controller.updateTask(row, col, value);
				}
			}
		});
		
		TableColumn taskColumn = todoList.getColumnModel().getColumn(0);
		taskColumn.setPreferredWidth(360);
		TableColumn priorityColumn = todoList.getColumnModel().getColumn(1);
		priorityColumn.setPreferredWidth(20);
		Integer[] columnPriorities = {1, 2, 3, 4, 5};
		priorityColumn.setCellEditor(new DefaultCellEditor(new JComboBox<Integer>(columnPriorities)));
		TableColumn completeColumn = todoList.getColumnModel().getColumn(2);
		completeColumn.setPreferredWidth(20);
		
		JScrollPane mainPane = new JScrollPane(todoList);
		mainPane.setPreferredSize(new Dimension(400,415));
		mainPane.setBackground(Color.white);
		
		add(topPane, BorderLayout.SOUTH);
		add(mainPane, BorderLayout.NORTH);
		
		controller.loadTasks();
	}
	
	public void addTask(String task, int priority){
		addTask(task, priority, false);
	}
	
	public void addTask(String task, int priority, boolean completion){
		Object[] taskInfo = {task, priority, new Boolean(completion)};
		tm.addRow(taskInfo);
		todoList.setModel(tm);
		taskName.setText("");
		taskPriority.setSelectedIndex(0);
	}
	
	public void deleteTask(int[] rows){
		if(rows.length == 0)
			return;
		for(int i=rows.length-1; i>=0; i--){
			tm.removeRow(rows[i]);
		}
	}
	
	public void windowClose(){
		controller.saveTasks();
	}
}
