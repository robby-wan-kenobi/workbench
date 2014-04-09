package view.tracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;

import model.tracker.Model;

public class GeneralListener extends Observable implements KeyListener, ActionListener, Observer, FocusListener{
	
	public GeneralListener(){
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String action = arg0.getActionCommand();
		setChanged();
		notifyObservers(action);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getSource() instanceof JTextArea){
			if((int)e.getKeyChar() == 10){
				String[] params = new String[2];
				JTextArea source = (JTextArea)e.getSource();
				source.setText(source.getText().trim());
				params[0] = source.getName();
				params[1] = source.getText();
				setChanged();
				notifyObservers(params);
			}
			else if(e.getSource() instanceof JTextArea 
					&& ((JTextArea)e.getSource()).getName() != null 
					&& ((JTextArea)e.getSource()).getName().equals("currentTask")
					&& e.getKeyCode() == KeyEvent.VK_ENTER){
				JTextArea currentTask = ((JTextArea)e.getSource());
				String taskName = currentTask.getText();
				taskName += '\b';
				currentTask.setText(taskName);
				String[] taskInfo = new String[2];
				taskInfo[0] = ((JTextArea)e.getSource()).getName();
				taskInfo[1] = taskName;
				setChanged();
				notifyObservers(taskInfo);
			}
		}
		System.out.print(e.getKeyChar());
		String event = "";
		switch (e.getKeyChar()){
			case 'a':
				event = "a";
				break;
			case 's':
				event = "s";
				break;
			case 'd':
				event = "d";
				break;
			default:
				event = "f";
		}
		setChanged();
		notifyObservers(event);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof Model){
			setChanged();
			notifyObservers(arg1);
		}
		if(arg1 instanceof String){
			if(arg1.equals("invalidTime")){
				setChanged();
				notifyObservers(arg1);
			}
			else if(arg1.equals("taskChanged")){
				setChanged();
				notifyObservers(arg1);
			}
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		if(arg0.getSource() instanceof JTextArea 
				&& ((JTextArea)arg0.getSource()).getName() != null 
				&& ((JTextArea)arg0.getSource()).getName().equals("currentTask")){
			String taskName = ((JTextArea)arg0.getSource()).getText();
			String[] taskInfo = new String[2];
			taskInfo[0] = ((JTextArea)arg0.getSource()).getName();
			taskInfo[1] = taskName;
			setChanged();
			notifyObservers(taskInfo);
		}
	}

	public void submit(String action, Object selection) {
		if(selection instanceof String){
			String[] actions = new String[2];
			actions[0] = action;
			actions[1] = (String) selection;
			setChanged();
			notifyObservers(actions);
		}
		else if(selection instanceof String[] && action.equals("changeDate")){
			String[] date = (String[])selection;
			String[] actions = new String[date.length + 1];
			actions[0] = action;
			for(int i=1; i<=date.length; i++){
				actions[i] = date[i-1];
			}
			setChanged();
			notifyObservers(actions);
		}
	}

	public void submitAction(String action){
		setChanged();
		notifyObservers(action);
	}
}