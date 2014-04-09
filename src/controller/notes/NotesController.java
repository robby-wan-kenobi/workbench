package controller.notes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import view.notes.NotepadContainer;

import model.notes.NotesModel;

public class NotesController{

	private NotesModel model;
	
	public NotesController(NotepadContainer nc){
		this.model = new NotesModel(nc);
	}
	
	public void addFile(String file){
		model.addFile(file);
	}
	
	public String getFile(int index){
		return model.getFile(index);
	}
	
	public void setFile(int index, String fileName){
//		model.setCurrentFile(fileName);
		model.setFile(index, fileName);
	}
	
	public void removeNote(int index){
		model.removeNote(index);
	}
	
	public String getFileName(int index){
		return model.getFileName(index);
	}
	
	public int getFileCount(){
		return model.getFileCount();
	}
	
	public boolean isNoteChanged(int index){
		return model.isNoteChanged(index);
	}
	
	public String getLastDirectory(){
		return model.getLastDirectory();
	}
	
	public void setChanged(int index){
		model.setChanged(index);
	}
	
	public void resetChanged(int index){
		model.resetChanged(index);
	}
	
	public void newNote(int index){
		model.newNote(index);
	}
	
	public void saveNote(int index, String text){
		if(model.getFile(index) == null || model.getFile(index).equals("")){
			JFileChooser fileChooser = new JFileChooser(getLastDirectory());
			int choice = fileChooser.showSaveDialog(null);
			if(choice == JFileChooser.APPROVE_OPTION){
				File file = fileChooser.getSelectedFile();
				model.saveNote(file, text, index);
			}
			else if(choice == JFileChooser.CANCEL_OPTION){
				return;
			}
		}
		else{
			model.saveNote(text, index);
		}
	}
	
	public void recordOpenFiles(){
		model.recordOpenFiles();
	}
	
	public void loadFiles(){
		Scanner scan;
		try {
			File fileData = new File("fileData.txt");
			if(!fileData.exists())
				return;
			scan = new Scanner(fileData);
			if(scan.hasNext())
				model.setLastTouched(scan.nextLine());
			while(scan.hasNext()){
				String fileName = scan.nextLine();
				addFile(fileName);
			}
		} catch (FileNotFoundException e) {
			return;
		}
	}
}
