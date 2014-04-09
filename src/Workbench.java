import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import view.View;

public class Workbench{
	
	private static final int xCoord = 100;
	private static final int yCoord = 100;
	private static final int xSize = 610;
	private static final int ySize = 540;
	
	private static void createLogger(){
		final View view = new View();
		
		final JFrame mainFrame = new JFrame("Workbench");
		
		mainFrame.getContentPane().setBackground(Color.white);
		mainFrame.setBounds(xCoord, yCoord, xSize, ySize);
		
		try {
			mainFrame.setIconImage(ImageIO.read(new File("icon.jpg")));
		} catch (IOException e) {}
		
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent evt){
					if(view.windowClose()){
						mainFrame.dispose();
						System.exit(0);
					}
				}
			}
		);
		mainFrame.getContentPane().add(view);
		mainFrame.setVisible(true);
		mainFrame.setFocusable(true);
		
		mainFrame.setResizable(false);
	}
	
	public static void main(String[] args){
		createLogger();
	}
}