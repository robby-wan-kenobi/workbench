package view.tracker.information;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;

import controller.tracker.Controller;
import view.tracker.BasePanel;
import view.tracker.GeneralListener;

@SuppressWarnings("serial")
public class BaseInfo extends BasePanel{
	
	protected final int xSize = 600;
	protected final int ySize = 360;
	
	public BaseInfo(Controller controller, GeneralListener gl) {
		super(controller, gl);
		setPreferredSize(new Dimension(xSize, ySize));
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
}