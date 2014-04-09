package model.tracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataRetriever{
	
	private Document document;
	private String file;
	
	public DataRetriever(String fileName){
		file = fileName;
		document = loadDocument(fileName);
	}
	
	private Element getTaskFromDate(String key, Element dateEl){
		NodeList tasks = dateEl.getElementsByTagName("task");
		Element taskEl = null;
		for(int i=0; i<tasks.getLength(); i++){
			Element task = (Element)tasks.item(i);
			if(task.getAttribute("name").equals(key)){
				taskEl = task;
				break;
			}
		}
		return taskEl;
	}
	
	private Element getDateElement(String date){
		Element dateEl = null;
		NodeList nodes = document.getElementsByTagName("day");
		for(int i=0; i<nodes.getLength(); i++){
			Element day = (Element)nodes.item(i);
			if(day.getAttribute("date").equals(date)){
				dateEl = day;
				break;
			}
		}
		return dateEl;
	}
	
	public List<Duration> getDayTimesOf(String key, Calendar date){
		List<Duration> times = new ArrayList<Duration>();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);
		String dateString = year + "-" + month + "-" + day;
		
		Element dateEl = getDateElement(dateString);
		Element taskEl = getTaskFromDate(key, dateEl);
		NodeList durNodes = taskEl.getElementsByTagName("duration");
		
		for(int i=0; i<durNodes.getLength(); i++){
			Element curDurEl = (Element)durNodes.item(i);
			Element startEl = (Element)curDurEl.getElementsByTagName("start").item(0);
			String start = startEl.getFirstChild().getNodeValue();
			Element endEl = (Element)curDurEl.getElementsByTagName("end").item(0);
			String end = endEl.getFirstChild().getNodeValue();
			Duration curDur = new Duration(start, 
											end, 
											key, 
											Integer.parseInt(curDurEl.getAttribute("iteration")),
											Boolean.parseBoolean(curDurEl.getAttribute("logged")));
			times.add(curDur);
		}

		return times;
	}
	
	public Duration getMostRecentTime(String key){
		return null;
	}
	
	private Document loadDocument(String fileName){
		Document doc = null;
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = docBuilder.parse(new File(fileName));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e){
			File xmlFile = new File(fileName);
			if(!xmlFile.exists()){
				try {
					PrintWriter pw = new PrintWriter(xmlFile);
					String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
					pw.write(xmlString);
					pw.close();
					return loadDocument(fileName);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	// Returns map of task to duration list
	public Map<String, List<Duration>> getDayBreakdown(Calendar day){
		Map<String, List<Duration>> breakdown = new HashMap<String, List<Duration>>();
		Element dayEl = getDateElement(dateStringOf(day));
		if(dayEl == null)
			return breakdown;
		NodeList children = dayEl.getChildNodes();
		for(int i=0; i<children.getLength(); i++){
			Node child = children.item(i);
			if(child.getNodeType() == Element.ELEMENT_NODE && child.getNodeName().equals("task")){
				Element childEl = (Element)child;
				String tagName = childEl.getAttribute("name");
				List<Duration> tagDurations = getDurations(childEl);
				breakdown.put(tagName, tagDurations);
			}
		}
		return breakdown;
	}
	
	// Returns map of day(String) to duration list
	public Map<String, List<Duration>> getTaskBreakdown(String name){
		Map<String, List<Duration>> breakdown = new LinkedHashMap<String, List<Duration>>();
		NodeList tasks = document.getElementsByTagName("task");
		for(int i=tasks.getLength()-1; i>=0; i--){
			Element taskEl = (Element)tasks.item(i);
			if(!taskEl.getAttribute("name").equals(name))
				continue;
			String day = ((Element)taskEl.getParentNode()).getAttribute("date");
			List<Duration> tagDurations = getDurations(taskEl);
			breakdown.put(day, tagDurations);
		}
		return breakdown;
	}
	
	private List<Duration> getDurations(Element taskEl){
		NodeList durations = taskEl.getElementsByTagName("duration");
		
		List<Duration> durationList = new ArrayList<Duration>();
		for(int i=0; i<durations.getLength(); i++){
			Element durEl = (Element)durations.item(i);
			Element startEl = (Element)durEl.getElementsByTagName("start").item(0);
			String start = startEl.getFirstChild().getNodeValue();
			Element endEl = (Element)durEl.getElementsByTagName("end").item(0);
			if(endEl == null)
				continue;
			String end = endEl.getFirstChild().getNodeValue();
			Duration currentDuration = new Duration(start, 
													end, 
													taskEl.getAttribute("name"), 
													Integer.parseInt(durEl.getAttribute("iteration")),
													Boolean.parseBoolean(durEl.getAttribute("logged")));
			durationList.add(currentDuration);
		}
		return durationList;
	}
	
	private double getDurationTime(Element taskEl){
		List<Duration> durations = getDurations(taskEl);
		
		double taskDuration = 0.0;
		for(Duration dur: durations){
			double currentDuration = dur.getTime();
			taskDuration += currentDuration;
		}
		return taskDuration;
	}
	
	public double getDurationOfForDate(String key, Calendar date){
		String dateString = date.get(Calendar.YEAR) + "-" + 
									(date.get(Calendar.MONTH) + 1) + "-" +  
									date.get(Calendar.DAY_OF_MONTH);
		Element dateEl = getDateElement(dateString);
		Element taskEl = getTaskFromDate(key, dateEl);
		
		return getDurationTime(taskEl);
	}
	
	public double getTodaysDurationOf(String key){
		Calendar today = Calendar.getInstance();
		String dateString = today.get(Calendar.YEAR) + "-" + 
									(today.get(Calendar.MONTH) + 1) + "-" +  
									today.get(Calendar.DAY_OF_MONTH);
		Element dateEl = getDateElement(dateString);
		Element taskEl = getTaskFromDate(key, dateEl);
		
		return getDurationTime(taskEl);
	}
	
	public double getTotalDurationOf(String key){
		NodeList nodes = document.getElementsByTagName("task");
		double taskDuration = 0.0;
		
		for(int i=0; i<nodes.getLength(); i++){
			Element taskEl = (Element)nodes.item(i);
			if(!taskEl.getAttribute("name").equals(key))
				continue;
			double curDuration = getDurationTime(taskEl);
			taskDuration += curDuration;
		}
		return taskDuration;
	}

	public void addTask(String name){
		String dateString = currentDate();
		Element dateEl = getDateElement(dateString);
		if(dateEl != null){
			NodeList nodes = dateEl.getElementsByTagName("task");
			for(int i=0; i<nodes.getLength(); i++){
				if(((Element)nodes.item(i)).getAttribute("name").equals(name)){
					return;	// found the task in question for this day
				}
			}
		}
		
		Element docElement = (Element)document.getElementsByTagName("root").item(0);
		
		Element todaysNode = null;
		if(dateEl == null){	// make a tag for today
			todaysNode = (Element) docElement.appendChild(document.createElement("day"));
			todaysNode.setAttribute("date", dateString);
		}else{
			todaysNode = dateEl;
		}
		
		Element newEl = (Element) todaysNode.appendChild(document.createElement("task"));
		newEl.setAttribute("name", name);
		newEl.setAttribute("resolution", "New");
	}
	
	// !!! Assumes that addTask has been called for the given task
	public void startTask(String name){
		String dateString = currentDate();
		Element dateEl = getDateElement(dateString);
		/* 
		 * I don't believe this code is needed. If the date doesn't exist or 
		 * the task isn't on that date yet, it's added later.
		 * 
		 * if(dateNodes.getLength() > 0){
			NodeList nodes = ((Element)dateNodes.item(0)).getElementsByTagName("task");
			boolean foundTask = false;
			for(int i=0; i<nodes.getLength(); i++){
				Element task = (Element)nodes.item(i);
				if(task.getAttribute("name").equals(name)){
					foundTask = true;
					break;
				}
			}
			if(!foundTask)
				return;	// the task doesn't exist, so abort
		}*/
		
		Element docElement = (Element)document.getElementsByTagName("root").item(0);
		
		Element todaysNode = null;
		if(dateEl == null){	// make a tag for today
			todaysNode = (Element) docElement.appendChild(document.createElement("day"));
			todaysNode.setAttribute("date", dateString);
		}else{
			todaysNode = dateEl;
		}
		
		Element todaysElement = (Element)todaysNode;
		Element taskElement = getTaskFromDate(name, todaysElement);
		if(taskElement == null){
			taskElement = (Element)todaysElement.appendChild(document.createElement("task"));
			taskElement.setAttribute("name", name);
		}
		// Add a duration tag
		Element newDurationElement = document.createElement("duration");
		newDurationElement.setAttribute("iteration", ""+findCurrentIteration(name));
		newDurationElement.setAttribute("logged", "false");
		Element durationElement = (Element)taskElement.appendChild(newDurationElement);
		// Add a start tag to the duration tag
		Element startElement = (Element)durationElement.appendChild(document.createElement("start"));
		startElement.appendChild(document.createTextNode(currentTime()));
	}
	
	// !!! Assumes that addTask and addTaskStart have been called for the given task
	public void stopTask(String name){
		String dateString = currentDate();
		Element dateEl = getDateElement(dateString);
		if(dateEl != null){
			Element taskEl = getTaskFromDate(name, dateEl);
			if(taskEl == null){	// the task doesn't exit
				return;
			}
		}
		
		Element docElement = (Element)document.getElementsByTagName("root").item(0);
		
		Element todaysNode = null;
		if(dateEl == null){	// make a tag for today
			todaysNode = (Element) docElement.appendChild(document.createElement("day"));
			todaysNode.setAttribute("date", dateString);
		}else{
			todaysNode = dateEl;
		}
		
		Element todaysElement = (Element)todaysNode;
		Element taskElement = getTaskFromDate(name, todaysElement);
		
		// Get all the duration tags and check which doesn't have a start tag
		NodeList durations = taskElement.getElementsByTagName("duration");
		Element incompleteDuration = null;
		for(int i=0; i<durations.getLength(); i++){
			Element curDur = (Element)durations.item(i);
			if(curDur.getElementsByTagName("end").getLength() == 0){
				incompleteDuration = curDur;
				break;
			}
		}
		
		// Add an end tag to the duration tag
		Element endElement = (Element)incompleteDuration.appendChild(document.createElement("end"));
		endElement.appendChild(document.createTextNode(currentTime()));
	}
	
	private Element getDurationElement(String taskName, int duration){
		NodeList taskNodes = document.getElementsByTagName("task");
		String durString = Integer.toString(duration);
		Element durationEl = null;
		for(int i=0; i<taskNodes.getLength(); i++){
			Element taskEl = (Element)taskNodes.item(i);
			if(!taskEl.getAttribute("name").equals(taskName))
				continue;
			NodeList durationNodes = taskEl.getElementsByTagName("duration");
			for(int j=0; j<durationNodes.getLength(); j++){
				Element durEl = (Element)durationNodes.item(j);
				if(durEl.getAttribute("iteration").equals(durString)){
					durationEl = durEl;
					break;
				}
			}
			if(durationEl != null)
				break;
		}
		return durationEl;
	}
	
	public boolean isTaskAlive(String taskName){
		NodeList taskNodes = document.getElementsByTagName("task");
		for(int i=0; i<taskNodes.getLength(); i++){
			Element taskEl = (Element)taskNodes.item(i);
			if(!taskEl.getAttribute("name").equals(taskName))
				continue;
			NodeList durationNodes = ((Element)taskNodes.item(i)).getElementsByTagName("duration");
			for(int j=0; j<durationNodes.getLength(); j++){
				NodeList startNodes = ((Element)durationNodes.item(j)).getElementsByTagName("start");
				NodeList endNodes = ((Element)durationNodes.item(j)).getElementsByTagName("end");
				if(startNodes.getLength() > 0 && endNodes.getLength() == 0)
					return true;
			}
		}
		return false;
	}
	
	public void setTaskUpToDate(String taskName){
		NodeList tasks = document.getElementsByTagName("task");
		for(int i=0; i<tasks.getLength(); i++){
			Node node = tasks.item(i);
			if(node instanceof Element && ((Element)node).getAttribute("name").equals(taskName)){
				Element task = (Element)node;
				NodeList durationList = task.getElementsByTagName("duration");
				for(int j=0; j<durationList.getLength(); j++){
					Node durNode = durationList.item(j);
					if(durNode instanceof Element){
						Element durEl = (Element)durNode;
						durEl.setAttribute("logged", "true");
					}
				}
			}
		}
	}
	
	public boolean isTaskUpToDate(String taskName){
		NodeList tasks = document.getElementsByTagName("task");
		for(int i=0; i<tasks.getLength(); i++){
			Node node = tasks.item(i);
			if(node instanceof Element && ((Element)node).getAttribute("name").equals(taskName)){
				Element task = (Element)node;
				NodeList durationList = task.getElementsByTagName("duration");
				for(int j=0; j<durationList.getLength(); j++){
					Node durNode = durationList.item(j);
					if(durNode instanceof Element && ((Element)durNode).getAttribute("logged").equals("false")){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void setTaskDurationLogged(String taskName, int duration){
		Element durationEl = getDurationElement(taskName, duration);
		if(durationEl != null){
			durationEl.setAttribute("logged", "true");
		}
	}
	
	public boolean isTaskDurationLogged(String taskName, int duration){
		Element durationEl = getDurationElement(taskName, duration);
		return durationEl.getAttribute("logged").equals("true");
	}
	
	public void setTaskResolution(String taskName, String resolution){
		NodeList elements = document.getElementsByTagName("task");
		for(int i=0; i<elements.getLength(); i++){
			if(elements.item(i) instanceof Element){
				Element el = (Element)elements.item(i);
				if(el.getAttribute("name").equals(taskName)){
					el.setAttribute("resolution", resolution);
				}
			}
		}
	}
	
	public String getTaskResolution(String taskName){
		NodeList elements = document.getElementsByTagName("task");
		String resolution = null;
		for(int i=0; i<elements.getLength(); i++){
			if(elements.item(i) instanceof Element){
				Element el = (Element)elements.item(i);
				if(el.getAttribute("name").equals(taskName)){
					resolution = el.getAttribute("resolution");
					break;
				}
			}
		}
		return resolution;
	}
	
	public String[] getAllTasks(){
		Set<String> taskSet = new LinkedHashSet<String>();
		NodeList days = document.getChildNodes().item(0).getChildNodes();
		for(int i=days.getLength()-1; i>=0; i--){
			Node day = days.item(i);
			if(day.getNodeType() == Node.ELEMENT_NODE){
				NodeList tasks = ((Element)day).getChildNodes();
				for(int j=tasks.getLength()-1; j>=0; j--){
					Node task = tasks.item(j);
					if(task.getNodeType() == Node.ELEMENT_NODE){
						Element curTask = (Element)task;
						taskSet.add(curTask.getAttribute("name"));
					}
				}
			}
			
		}
		String[] taskArray = new String[0];
		return taskSet.toArray(taskArray);
	}
	
	private String currentDate(){
		return dateStringOf(Calendar.getInstance());
	}
	
	private String dateStringOf(Calendar date){
		String dateString = date.get(Calendar.YEAR) + "-" + 
									(date.get(Calendar.MONTH) + 1) + "-" +  
									date.get(Calendar.DAY_OF_MONTH);
		return dateString;
	}
	
	public void changeTaskName(String origName, String newName){
		NodeList nodes = document.getElementsByTagName("task");
		for(int i=0; i<nodes.getLength(); i++){
			Element taskEl = (Element)nodes.item(i);
			if(taskEl.getAttribute("name").equals(origName)){
				taskEl.setAttribute("name", newName);
			}
		}
	}
	
	public void changeCurrentIterationStart(String taskName, String newTime){
		NodeList nodes = document.getElementsByTagName("task");
		for(int i=0; i<nodes.getLength(); i++){
			Element taskEl = (Element)nodes.item(i);
			if(!taskEl.getAttribute("name").equals(taskName))
				continue;
			NodeList durations = taskEl.getElementsByTagName("duration");
			for(int j=0; j<durations.getLength(); j++){
				Element duration = (Element)durations.item(j);
				Element start = (Element)duration.getElementsByTagName("start").item(0);
				NodeList end = duration.getElementsByTagName("end");
				if(end.getLength() == 0){
					Node startTime = start.getChildNodes().item(0);
					start.replaceChild(document.createTextNode(newTime), startTime);
				}
			}
		}
	}
	
	public void changeIterationStart(int iteration, String taskName, String newTime){
		NodeList nodes = document.getElementsByTagName("task");
		for(int i=0; i<nodes.getLength(); i++){
			Element taskEl = (Element)nodes.item(i);
			if(!taskEl.getAttribute("name").equals(taskName))
				continue;
			NodeList durations = taskEl.getElementsByTagName("duration");
			for(int j=0; j<durations.getLength(); j++){
				Element duration = (Element)durations.item(j);
				if(Integer.parseInt(duration.getAttribute("iteration")) != iteration)
					continue;
				Element start = (Element)duration.getElementsByTagName("start").item(0);
				Node startTime = start.getChildNodes().item(0);
				start.replaceChild(document.createTextNode(newTime), startTime);
				return;
			}
		}
	}
	
	private int findCurrentIteration(String taskName){
		NodeList taskNodes = document.getElementsByTagName("task");
		int max = 0;
		for(int i=0; i<taskNodes.getLength(); i++){
			Element task = (Element)taskNodes.item(i);
			if(!task.getAttribute("name").equals(taskName))
				continue;
			NodeList taskDurations = task.getElementsByTagName("duration");
			for(int j=0; j<taskDurations.getLength(); j++){
				Element curDurationEl = (Element)taskDurations.item(j);
				String iteration = curDurationEl.getAttribute("iteration");
				int curDuration = Integer.parseInt(iteration);
				max = Math.max(max, curDuration);
			}
		}
		return max + 1;
	}
	
	private String currentTime(){
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
		String currentTime = sdf.format(today.getTime());
		return currentTime;
	}
	
	public void writeDocumentToFile(){
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource("indentation.xsl"));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(new DOMSource(document), new StreamResult(new PrintWriter(file)));
		} catch (TransformerConfigurationException e){
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e){
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
}