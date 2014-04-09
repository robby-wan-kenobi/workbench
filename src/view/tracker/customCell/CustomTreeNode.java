package view.tracker.customCell;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class CustomTreeNode extends DefaultMutableTreeNode{
	
	private Type type;
	
	public CustomTreeNode(Object obj){
		super(obj);
	}
	
	public CustomTreeNode(Object obj, Type type){
		super(obj);
		this.type = type;
	}
	
	public Type getType(){
		return type;
	}
	
	public void setType(Type type){
		this.type = type;
	}
	
}