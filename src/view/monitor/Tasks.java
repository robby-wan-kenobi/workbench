package view.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import view.ViewDefinitions;
import controller.monitor.TasksController;

@SuppressWarnings("serial")
public class Tasks extends JPanel{
	private TasksController controller;
	private DefaultTableModel tm;
	private JTable tasksList;
	private JLabel totalTasks;
	private JLabel cpuUsage;
	private JLabel memUsage;
	
	private Tasks thisClass;
	
	private boolean cpuSelected;
	
	public Tasks(){
		super(new BorderLayout());
		
		thisClass = this;
		
		controller = new TasksController(this);
		
		setBackground(ViewDefinitions.panel1Background);
		
		setName("Tasks");
		
		setFocusable(true);
		
		cpuSelected = false;
		
		UIManager.put("TableHeader.background", ViewDefinitions.panel1Background);
		UIManager.put("TableHeader.font", new Font(null, Font.BOLD, 12));
		//UIManager.put("Table.font", new Font(null, Font.PLAIN, 14));
		UIManager.put("Viewport.background", ViewDefinitions.panel1Background);
		
		JPanel infoPane = new JPanel();
//		infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.LINE_AXIS));
		infoPane.setLayout(new BorderLayout());
		infoPane.setBackground(ViewDefinitions.panel1Background);
		infoPane.setPreferredSize(new Dimension(400, 35));
		infoPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
//		totalTasks = new JLabel();
//		totalTasks.setBackground(ViewDefinitions.panel1Background);
//		infoPane.add(totalTasks);
//		
//		cpuUsage = new JLabel();
//		cpuUsage.setBackground(ViewDefinitions.panel1Background);
//		infoPane.add(cpuUsage);
//		
//		memUsage = new JLabel();
//		memUsage.setBackground(ViewDefinitions.panel1Background);
//		infoPane.add(memUsage);
		
		JCheckBox includeCPU = new JCheckBox("Include CPU");
		includeCPU.setBackground(ViewDefinitions.panel1Background);
		includeCPU.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox checkBox = (JCheckBox)(arg0.getSource());
				if(checkBox.isSelected())
					cpuSelected = true;
				else
					cpuSelected = false;
			}
		});
		infoPane.add(includeCPU, BorderLayout.CENTER);
		
		JButton refresh = new JButton();
		refresh.setBackground(ViewDefinitions.button2Background);
		refresh.setForeground(ViewDefinitions.button2Foreground);
		refresh.setText("Refresh");
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.updateTasks();
			}
		});
		infoPane.add(refresh, BorderLayout.EAST);
		
		tm = new DefaultTableModel(){
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int c){
				return getValueAt(0, c).getClass();
			}
		};
		tm.addColumn("Task");
		tm.addColumn("PID");
		tm.addColumn("Memory Usage (K)");
		tm.addColumn("CPU Usage (%)");
		
		tasksList = new JTable(tm);
		tasksList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tasksList.setAutoCreateRowSorter(true);
		tasksList.setSelectionBackground(ViewDefinitions.button2Background);
		tasksList.setSelectionForeground(ViewDefinitions.button2Foreground);
		tasksList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getButton() == MouseEvent.BUTTON3){
					JTable sourceTable = (JTable)arg0.getSource();
					int row = sourceTable.rowAtPoint(arg0.getPoint());
					final Integer pid = (Integer)sourceTable.getValueAt(row, 1);
					sourceTable.setRowSelectionInterval(row, row);
					if(pid == 0 || pid == 4){
						JOptionPane.showMessageDialog(thisClass, "You don't want to mess with that task.", "WARNING", JOptionPane.WARNING_MESSAGE);
						System.out.println("BOOM!");
					}
					else{
						JMenuItem stopTask = new JMenuItem("End task");
						stopTask.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								String result = controller.endTask(pid);
								JOptionPane.showMessageDialog(thisClass, result, "INFO", JOptionPane.INFORMATION_MESSAGE);
								controller.updateTasks();
							}
						});
						JMenuItem stopTaskTree = new JMenuItem("End task tree");
						stopTaskTree.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								String result = controller.endTaskTree(pid);
								JOptionPane.showMessageDialog(thisClass, result, "INFO", JOptionPane.INFORMATION_MESSAGE);
								controller.updateTasks();
							}
						});
						JPopupMenu rightClickMenu = new JPopupMenu();
						rightClickMenu.add(stopTask);
						rightClickMenu.add(stopTaskTree);
						rightClickMenu.show(sourceTable, arg0.getX()+10, arg0.getY());
					}
				}
			}
		});
		
		TableColumn taskColumn = tasksList.getColumnModel().getColumn(0);
		taskColumn.setPreferredWidth(220);
		TableColumn pidColumn = tasksList.getColumnModel().getColumn(1);
		pidColumn.setPreferredWidth(40);
		TableColumn memColumn = tasksList.getColumnModel().getColumn(2);
		memColumn.setPreferredWidth(70);
		TableColumn cpuColumn = tasksList.getColumnModel().getColumn(3);
		cpuColumn.setPreferredWidth(70);
		
		JScrollPane mainPane = new JScrollPane(tasksList);
		mainPane.setPreferredSize(new Dimension(400,415));
		mainPane.setBackground(Color.white);
		
		add(infoPane, BorderLayout.SOUTH);
		add(mainPane, BorderLayout.NORTH);
		
		//controller.setInterval(2);
		setTasks();
	}
	
	public void setTasks(){
		Map<Integer, Object[]> tasks = cpuSelected ? controller.getTasksComplete() : controller.getTasks();
		Object[] totalData = {"---","---",0L,"---"};
		double totalCPU = 0.0;
		long totalMem = 1L;
		for(int i=tm.getRowCount()-1; i>=0; i--)
			tm.removeRow(i);
		tasksList.removeAll();
		for(Integer pid: tasks.keySet()){
			Object[] origData = tasks.get(pid);
			Object[] data = origData;
			if(origData.length == 3){
				data = new Object[4];
				data[0] = origData[0];
				data[1] = origData[1];
				data[2] = origData[2];
				data[3] = "---";
			}
			if(data[3] instanceof Double && !data[0].equals("Idle") && !data[0].equals("System Idle Process"))
				totalCPU += (Double)data[3];
			if((Integer)(data[1]) == -1)
				totalData = data;
			else if((Long)(data[2]) == -2)
				totalMem = (Long)data[2];
			else if((Integer)(data[1]) >= 0)
				tm.addRow(data);
		}
//		totalTasks.setText("Tasks: " + tm.getRowCount() + " | ");
//		DecimalFormat df = new DecimalFormat("#0.00");
//		cpuUsage.setText("CPU Usage: " + df.format(totalCPU - 100) + "% | ");
//		memUsage.setText("Memory Usage: " + df.format((Long)totalData[2] / totalMem) + "% | ");
	}
	
}
