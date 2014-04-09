package view.notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.notes.NotesController;

import view.ViewDefinitions;

public class NotepadContainer extends Observable{
	
	private boolean addBool = true;
	
	private JTabbedPane pane;
	
	private NotesController controller;
	
	public NotepadContainer(){
		pane = new JTabbedPane(JTabbedPane.TOP);

		UIManager.put("TabbedPane.selected", ViewDefinitions.panel1Background);
		UIManager.put("TabbedPane.contentAreaColor", ViewDefinitions.panel1Background);
		UIManager.put("TabbedPane.highlight", Color.white);
		UIManager.put("TabbedPane.lightHighlight", Color.gray);
		UIManager.put("TabbedPane.shadow", Color.darkGray);
		UIManager.put("TabbedPane.darkShadow", Color.black);
		
		pane.setBackground(ViewDefinitions.panel1Background);
		
		pane.setMaximumSize(new Dimension(1, 1));
		
		
		controller = new NotesController(this);
		
		pane.setName("Notes");
		
		JPanel addTabPanel = new JPanel(new BorderLayout());
		JLabel sbLabel = new JLabel(new ImageIcon("Strong_Bad.jpg"));
		addTabPanel.add(sbLabel, BorderLayout.CENTER);
		pane.add(addTabPanel);
		controller.loadFiles();
		if(controller.getFileCount() == 0)
			controller.addFile("");
		pane.setSelectedIndex(0);
		pane.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0){
				JTabbedPane source = ((JTabbedPane)arg0.getSource());
				int selectedIndex = source.getSelectedIndex();
				if(selectedIndex < 7 && selectedIndex == pane.getTabCount()-1 && addBool){
					controller.addFile("");
				}
			}
		});
		pane.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0){
			}
			@Override
			public void keyReleased(KeyEvent arg0){
			}
			@Override
			public void keyPressed(KeyEvent arg0){
				if(arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_T && pane.getTabCount() < 8 && addBool){
					controller.addFile("");
				}
				else if(arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_W){
					
				}
			}
		});
		pane.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2 || e.getButton() == MouseEvent.BUTTON2){
					JTabbedPane source = ((JTabbedPane)e.getSource());
					int selectedIndex = source.getSelectedIndex();
					if(selectedIndex == controller.getFileCount())	// Don't delete the last one
						return;
					boolean close = ((Notepad)pane.getComponentAt(selectedIndex)).closeTab();
					if(!close)
						return;
					if(source.getTabCount() > 2)
						addBool = false;
					
					controller.removeNote(selectedIndex);
					addBool = true;
					if(controller.getFileCount() == 0){
						controller.addFile("");
					}
				}
			}
		});
	}
	
	public JTabbedPane getPane(){
		return pane;
	}
	
	public void addNote(String fileName, String text, int index){
		addBool = false;
		Notepad note = new Notepad(index, controller);
		note.setTabContent(text);
		addObserver(note);
		pane.add(note, index);
		pane.setTitleAt(index, fileName);
		pane.setSelectedIndex(index);
		addBool = true;
	}
	
	public void updateNote(String fileName, String text, int index){
		Notepad note = (Notepad)pane.getComponentAt(index);
		note.setTabTitle(fileName);
		if(text != null)
			note.setTabContent(text);
	}
	
	public void removeNote(int index){
		addBool = false;
		pane.remove(index);
		setChanged();
		notifyObservers(index);
		if(index >= pane.getTabCount()-2){
			index--;
			if(index < 0) index = 0;
		}
		pane.setSelectedIndex(index);
		addBool = true;
	}
	
	public boolean windowClose(){
		for(int i=0; i<pane.getTabCount()-1; i++){
			Notepad note = (Notepad)pane.getComponentAt(i);
			
			if(!note.closeTab())
				return false;
		}
		controller.recordOpenFiles();
		return true;
	}

	public void setNoteChanged(int index){
		String title = pane.getTitleAt(index);
		pane.setTitleAt(index, "*" + title);
	}
}