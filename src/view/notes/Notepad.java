package view.notes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import controller.notes.NotesController;

import utils.FileIO;
import view.ViewDefinitions;

@SuppressWarnings("serial")
public class Notepad extends JPanel implements Observer{
	private JTextArea textArea;
	private JLabel fileLabel;
	
	private NotesController controller;
	private int index;
	
	public Notepad(int i, NotesController c){
		super(new BorderLayout());
		
		this.controller = c;
		
		setBackground(ViewDefinitions.panel1Background);
		
		index = i;
		
		setName("New Note");
		
		setFocusable(true);
		
		JPanel topPanel = getButtonPanel();
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Courier", Font.PLAIN, 12));
		textArea.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e){
				if(!e.isControlDown()){
					controller.setChanged(index);
					resetTab();
				}
			}
			@Override
			public void keyReleased(KeyEvent e){
			}
			@Override
			public void keyPressed(KeyEvent e){
				if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
					controller.saveNote(index, textArea.getText());
					fileLabel.setText(controller.getFileName(index));
					setTabTitle(controller.getFileName(index));
					resetTab();
				}
			}
		});
		JScrollPane mainPane = new JScrollPane(textArea);
		mainPane.setPreferredSize(new Dimension(400,385));
		
		add(topPanel, BorderLayout.NORTH);
		add(mainPane, BorderLayout.CENTER);
		
		if(controller.getFileCount() > index && controller.getFile(index) != null && !controller.getFile(index).equals("")){
			File selectedFile = new File(controller.getFile(index));
			try{
				controller.setFile(index, selectedFile.getCanonicalPath());
			}catch(IOException e){e.printStackTrace();}
			fileLabel.setText(selectedFile.getName());
			textArea.setText(FileIO.getFileContents(selectedFile));
		}
	}
	
	public JPanel getButtonPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.setBackground(ViewDefinitions.panel1Background);
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		labelPanel.setBackground(ViewDefinitions.panel1Background);
		
		JLabel spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		labelPanel.add(spacer);
		
		fileLabel = new JLabel();
		fileLabel.setText("New Note");
		labelPanel.add(fileLabel);
		
		panel.add(labelPanel, BorderLayout.WEST);
		
		JLabel empty = new JLabel();
		empty.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
		panel.add(empty, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBackground(ViewDefinitions.panel1Background);
		
		JButton newButton = new JButton();
		newButton.setText("New");
		newButton.setBackground(ViewDefinitions.button1Background);
		newButton.setForeground(ViewDefinitions.button1Foreground);
		newButton.setPreferredSize(new Dimension(70, 20));
		newButton.setActionCommand("newNote");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!checkClose())
					return;
				fileLabel.setText("New Note");
				setTabTitle("New Note");
				textArea.setText("");
				controller.newNote(index);
				resetTab();
			}
		});
		buttonPanel.add(newButton);
		
		spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		buttonPanel.add(spacer);
		
		JButton loadButton = new JButton();
		loadButton.setText("Load");
		loadButton.setBackground(ViewDefinitions.button1Background);
		loadButton.setForeground(ViewDefinitions.button1Foreground);
		loadButton.setPreferredSize(new Dimension(70, 20));
		loadButton.setActionCommand("loadNote");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!checkClose())
					return;
				JFileChooser fileChooser = new JFileChooser(controller.getLastDirectory());
				int fileReturn = fileChooser.showOpenDialog(null);
				if(fileReturn == JFileChooser.APPROVE_OPTION){
					File selectedFile = fileChooser.getSelectedFile();
					try{
						controller.setFile(index, selectedFile.getCanonicalPath());
					}catch(IOException e){e.printStackTrace();}
					fileLabel.setText(selectedFile.getName());
					setTabTitle(selectedFile.getName());
					textArea.setText(FileIO.getFileContents(selectedFile));
					textArea.setCaretPosition(0);
					textArea.requestFocus();
					resetTab();
				}
			}
		});
		buttonPanel.add(loadButton);
		
		spacer = new JLabel();
		spacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		buttonPanel.add(spacer);
		
		JButton saveButton = new JButton();
		saveButton.setText("Save");
		saveButton.setBackground(ViewDefinitions.button1Background);
		saveButton.setForeground(ViewDefinitions.button1Foreground);
		saveButton.setPreferredSize(new Dimension(70, 20));
		saveButton.setActionCommand("saveNote");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.saveNote(index, textArea.getText());
				fileLabel.setText(controller.getFileName(index));
				setTabTitle(controller.getFileName(index));
				resetTab();
			}
		});
		buttonPanel.add(saveButton);
		
		panel.add(buttonPanel, BorderLayout.EAST);
		
		return panel;
	}
	
	public void resetTab(){
		JTabbedPane parent = ((JTabbedPane)getParent());
		parent.setComponentAt(index, this);
	}
	
	public void setTabContent(String text){
		textArea.setText(text);
		textArea.setCaretPosition(0);
		textArea.requestFocus();
	}
	
	public void setTabTitle(String title){
		JTabbedPane parent = ((JTabbedPane)getParent());
		parent.setTitleAt(index, title);
	}
	
	private boolean checkClose(){
		if(controller.isNoteChanged(index)){
			int option = JOptionPane.showConfirmDialog(null, "Save " + controller.getFileName(index) + " first?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(option == JOptionPane.CANCEL_OPTION)
				return false;
			else if(option == JOptionPane.YES_OPTION)
				controller.saveNote(index, textArea.getText());
			else if(option == JOptionPane.NO_OPTION)
				controller.resetChanged(index);
		}
		return true;
	}
	
	public boolean closeTab(){
		return checkClose();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		int removedIndex = (Integer)arg1;
		if(removedIndex < index)
			index--;
	}
	
}
