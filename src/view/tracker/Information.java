package view.tracker;

import java.awt.CardLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import controller.tracker.Controller;

import view.tracker.information.*;

@SuppressWarnings("serial")
public class Information extends BasePanel implements Observer{
	
	private final String BLANK_CARD = "Blank";
	private final String TASK_CARD = "Task Information";
	private final String DAY_CARD = "Day Information";
	private final String ALL_TASK_CARD = "All Task Information";
	
	private JPanel blankCard = null;
	private JPanel taskCard = null;
	private JPanel dayCard = null;
	private JPanel allTaskCard = null;
	
	private GeneralListener gl;
	
	public Information(Controller controller, GeneralListener gl){
		super(controller, gl);
		setLayout(new CardLayout());
		
		this.gl = gl;
		
		blankCard = new BlankInfo(controller, gl);
		taskCard = new TaskInfo(controller, gl);
		dayCard = new DayInfo(controller, gl);
		allTaskCard = new AllTaskInfo(controller, gl);
		
		add(blankCard, BLANK_CARD);
		add(taskCard, TASK_CARD);
		add(dayCard, DAY_CARD);
		add(allTaskCard, ALL_TASK_CARD);
		
		setFocusable(true);
		addKeyListener(gl);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof String[]){
			String[] args = (String[])arg1;
			if(args[0].equals("changeDate")){
				String[] date = new String[3];
				date[0] = args[1];
				date[1] = args[2];
				date[2] = args[3];
				dayCard = new DayInfo(controller, gl, date);
				remove(dayCard);
				add(dayCard, DAY_CARD);
				CardLayout cl = (CardLayout)getLayout();
				cl.show(this, DAY_CARD);
			}
			else if(args[0].equals("changeTask")){
				taskCard = new TaskInfo(controller, gl, args[1]);
				remove(taskCard);
				add(taskCard, TASK_CARD);
				CardLayout cl = (CardLayout)getLayout();
				cl.show(this, TASK_CARD);
			}
			return;
		}
		String change = (String)arg1;
		if(change.equals("taskInfo")){
			taskCard = new TaskInfo(controller, gl);
			remove(taskCard);
			add(taskCard, TASK_CARD);
			CardLayout cl = (CardLayout)getLayout();
			cl.show(this, TASK_CARD);
		}
		else if(change.equals("dayInfo")){
			dayCard = new DayInfo(controller, gl);
			remove(dayCard);
			add(dayCard, DAY_CARD);
			CardLayout cl = (CardLayout)getLayout();
			cl.show(this, DAY_CARD);
		}
		else if(change.equals("allTaskInfo")){
			allTaskCard = new AllTaskInfo(controller, gl);
			remove(allTaskCard);
			add(allTaskCard, ALL_TASK_CARD);
			CardLayout cl = (CardLayout)getLayout();
			cl.show(this, ALL_TASK_CARD);
			allTaskCard.requestFocus();
		}
		else if(change.equals("f")){
			CardLayout cl = (CardLayout)getLayout();
			cl.show(this, BLANK_CARD);
		}
			
	}
	
}