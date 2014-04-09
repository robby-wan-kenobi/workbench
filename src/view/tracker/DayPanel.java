package view.tracker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import view.ViewDefinitions;

import controller.tracker.Controller;

@SuppressWarnings("serial")
public class DayPanel extends JPanel implements Observer{
	
	private Controller controller;
	private GeneralListener gl;
	
	private JTextArea timeOutput;
	private JButton pauseDay;
	private JButton resetDay;
	
	public DayPanel(Controller controller, GeneralListener gl){
		this.controller = controller;
		this.gl = gl;
		setPreferredSize(new Dimension(600, 40));
		setBackground(ViewDefinitions.panel1Background);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		addComponents();
	}
	
	private void addComponents(){
		JLabel label = new JLabel();
		label.setText("Today");
		label.setBackground(ViewDefinitions.panel1Background);
		label.setFont(new Font("Arial", Font.ITALIC, 18));
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		add(label, BorderLayout.WEST);
		
		timeOutput = new JTextArea();
		timeOutput.setBackground(ViewDefinitions.panel1Background);
		timeOutput.setEditable(false);
		timeOutput.setText("00:00:00 (0.00)");
		timeOutput.setFont(new Font("Arial", Font.ITALIC, 18));
		timeOutput.setBorder(BorderFactory.createEmptyBorder(5, 100, 0, 0));
		add(timeOutput, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBackground(ViewDefinitions.panel1Background);
		
		JLabel empty = new JLabel();
		empty.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		buttonPanel.add(empty);
		
		pauseDay = new JButton();
		pauseDay.setBackground(ViewDefinitions.button1Background);
		pauseDay.setForeground(ViewDefinitions.button1Foreground);
		pauseDay.setText("Pause");
		pauseDay.addActionListener(gl);
		pauseDay.setActionCommand("pauseDay");
		pauseDay.setPreferredSize(new Dimension(70, 25));
		pauseDay.setMaximumSize(new Dimension(70, 25));
		pauseDay.setMargin(new Insets(0, 0, 0, 0));
		buttonPanel.add(pauseDay);
		
		empty = new JLabel();
		empty.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		buttonPanel.add(empty);
		
		resetDay = new JButton();
		resetDay.setBackground(ViewDefinitions.button1Background);
		resetDay.setForeground(ViewDefinitions.button1Foreground);
		resetDay.setText("Reset");
		resetDay.addActionListener(gl);
		resetDay.setActionCommand("resetDay");
		resetDay.setPreferredSize(new Dimension(70, 25));
		resetDay.setMaximumSize(new Dimension(70, 25));
		buttonPanel.add(resetDay);
		
		empty = new JLabel();
		empty.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		buttonPanel.add(empty);
		
		JButton saveDay = new JButton();
		saveDay.setBackground(ViewDefinitions.button1Background);
		saveDay.setForeground(ViewDefinitions.button1Foreground);
		saveDay.setText("Save");
		saveDay.addActionListener(gl);
		saveDay.setActionCommand("saveDay");
		saveDay.setPreferredSize(new Dimension(70, 25));
		saveDay.setMaximumSize(new Dimension(70, 25));
		buttonPanel.add(saveDay);
		
		add(buttonPanel, BorderLayout.EAST);
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
	
	private void setTimes(String curTimeString){
		long time = (long) (controller.getTodaysTime());
		long hours = time / 3600;
		time -= (hours*3600);
		long minutes = time / 60;
		long seconds = time % 60;
		String timeString = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		double timeDouble = dateStringToDouble(timeString);
		DecimalFormat df = new DecimalFormat("#0.00");
		timeOutput.setText("");
		timeOutput.setText(timeString + " (" + df.format(timeDouble) + ")");
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof String){
			if(arg1.equals("pauseDay")){
				controller.pauseDay();
				if(controller.isDayPaused())
					pauseDay.setText("Resume");
				else
					pauseDay.setText("Pause");
			}
			else if(arg1.equals("resetDay")){
				controller.resetDay();
				pauseDay.setText("Pause");
			}
		}
		else if(arg1 instanceof String[]){
			setTimes(((String[])arg1)[1]);
		}
	}
	
}