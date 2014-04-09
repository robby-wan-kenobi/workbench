package view.tracker.information;

import java.awt.Color;

import view.ViewDefinitions;
import view.tracker.GeneralListener;

import controller.tracker.Controller;

@SuppressWarnings("serial")
public class BlankInfo extends BaseInfo{

	public BlankInfo(Controller controller, GeneralListener gl) {
		super(controller, gl);
		setBackground(ViewDefinitions.panel2Background);
	}
	
}