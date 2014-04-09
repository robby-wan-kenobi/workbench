package model.notes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.FileIO;
import view.notes.NotepadContainer;

public class NotesModel {
	
	private List<String> noteList; 
	private List<Boolean> noteChangedList;
	private String lastTouched;
	private NotepadContainer nc;
	
	public NotesModel(NotepadContainer nc){
		noteList = new ArrayList<String>();
		noteChangedList = new ArrayList<Boolean>();
		lastTouched = ".";
		this.nc = nc;
	}
	
	public void addFile(String file){
		noteList.add(file);
		noteChangedList.add(false);
		String fileContents = "";
		if(file != null &&!file.equals("")){
			lastTouched = file;
			fileContents = FileIO.getFileContents(new File(file));
		}
		nc.addNote(getFileName(noteList.size()-1), fileContents, noteList.size()-1);
	}
	
	public void removeNote(int index){
		noteList.remove(index);
		noteChangedList.remove(index);
		nc.removeNote(index);
	}
	
	public void setFile(int index, String file){
		noteList.set(index, file);
		noteChangedList.set(index, false);
	}
	
	public String getFileName(int index){
		String fileName = noteList.get(index);
		if(fileName == null || fileName.equals(""))
			return "New Note";
		return new File(fileName).getName();
	}
	
	public int getFileCount(){
		return noteList.size();
	}
	
	public void setChanged(int index){
		if(noteChangedList.get(index) == false)
			nc.setNoteChanged(index);
		noteChangedList.set(index, true);
		String fileName = noteList.get(index);
		if(fileName != null && !fileName.equals(""))
			lastTouched = fileName;
	}
	
	public void saveNote(String text, int index){
		if(isNoteChanged(index)){
			FileIO.writeToFile(getFile(index), text);
			resetChanged(index);
		}
		String fileName = noteList.get(index);
		if(fileName != null && !fileName.equals(""))
			lastTouched = fileName;
	}
	
	public void saveNote(File file, String text, int index){
		try {
			file.createNewFile();
			FileIO.writeToFile(file.getCanonicalPath(), text);
			noteList.set(index, file.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		resetChanged(index);
		String fileName = noteList.get(index);
		if(fileName != null && !fileName.equals(""))
			lastTouched = fileName;
	}
	
	// Should this be here?
	public void newNote(int index){
		noteList.set(index, "");
	}
	
	public String getLastDirectory(){
		if(lastTouched != null && !lastTouched.equals("")){
			return new File(lastTouched).getPath();
		}
		return ".";
	}
	
	public void setLastTouched(String dir){
		lastTouched = dir;
	}
	
	public String getFile(int index){
		if(getFileCount() == 0 || index >= getFileCount())
			return "";
		return noteList.get(index);
	}

	public boolean isNoteChanged(int index){
		return noteChangedList.get(index);
	}

	public void resetChanged(int index){
		noteChangedList.set(index, false);
	}
	
	public void recordOpenFiles(){
		try{
			File fileData = new File("fileData.txt");
			if(fileData.exists())
				fileData.delete();
			FileWriter fw = new FileWriter(fileData);
			fw.write(lastTouched + "\n");
			for(int i=0; i<noteList.size(); i++){
				String fileName = noteList.get(i);
				if(fileName != null && !fileName.equals("")){
					fw.write(fileName);
					if(i < noteList.size() - 1){
						fw.write("\n");
					}
				}
			}
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
