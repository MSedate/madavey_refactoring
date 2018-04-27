/**
 * ProjectImpl.java
 * Created on 11.02.2003, 23:06:22 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import main.java.memoranda.interfaces.IProject;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Default implementation of Project interface
 */
/*$Id: ProjectImpl.java,v 1.7 2004/11/22 10:02:37 alexeya Exp $*/
public class ProjectImpl extends BaseImpl implements IProject {
	/*TASK 2-2 SMELL BETWEEN CLASSES
	 * < There was a code smell in ProjectImpl and
	 * this class where they both had duplicate code.
	 * The method getDescription() and setDescription()
	 * were the exact same in both classes.Therefore I changed the 
	 * Element method to have the same name "_root" and then
	 * made this new parent class holding the two methods.  Now TaskImpl and 
	 * ProjectImpl both extend BaseImpl class. The get and set Description 
	 * methods were removed. >
	 */

    /**
     * Constructor for ProjectImpl.
     */
    public ProjectImpl(Element root) {        
        _root = root;
    }

    /**
     * @see main.java.memoranda.interfaces.IProject#getID()
     */
    public String getID() {
        return _root.getAttribute("id").getValue();
    }

    /**
     * @see main.java.memoranda.interfaces.IProject#getStartDate()
     */
    public CalendarDate getStartDate() {
        Attribute d = _root.getAttribute("startDate");
        if (d == null) return null;
        return new CalendarDate(d.getValue());        
    }

    /**
     * @see main.java.memoranda.interfaces.IProject#setStartDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setStartDate(CalendarDate date) {
        if (date != null)
            setAttr("startDate", date.toString());
    }

    /**
     * @see main.java.memoranda.interfaces.IProject#getEndDate()
     */
    public CalendarDate getEndDate() {
        Attribute d = _root.getAttribute("endDate");
        if (d == null) return null;
        return new CalendarDate(d.getValue());
    }

    /**
     * @see main.java.memoranda.interfaces.IProject#setEndDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setEndDate(CalendarDate date) {
        if (date != null)
            setAttr("endDate", date.toString());
        else if (_root.getAttribute("endDate") != null)
            setAttr("endDate", null);
    }

    /**
     * @see main.java.memoranda.interfaces.IProject#getStatus()
     */
    public int getStatus() {
        if (isFrozen())
            return IProject.FROZEN;
        CalendarDate today = CurrentDate.get();
        CalendarDate prStart = getStartDate();
        CalendarDate prEnd = getEndDate();
        if (prEnd == null) {
            if (today.before(prStart))
                return IProject.SCHEDULED;
            else
                return IProject.ACTIVE;                
        }    
        if (today.inPeriod(prStart, prEnd))
            return IProject.ACTIVE;
        else if (today.after(prEnd)) {
            //if (getProgress() == 100)
                return IProject.COMPLETED;
            /*else
                return Project.FAILED;*/
        }
        else
            return IProject.SCHEDULED;
    }

    private boolean isFrozen() {
        return _root.getAttribute("frozen") != null;
    }

   
    /*public int getProgress() {
        Vector v = getAllTasks();
        if (v.size() == 0) return 0;
        int p = 0;
        for (Enumeration en = v.elements(); en.hasMoreElements();) {
          Task t = (Task) en.nextElement();
          p += t.getProgress();
        }
        return (p*100)/(v.size()*100);
    }*/
  
    
    /**
     * @see main.java.memoranda.interfaces.IProject#freeze()
     */
    public void freeze() {
        _root.addAttribute(new Attribute("frozen", "yes"));
    }

    /**
     * @see main.java.memoranda.interfaces.IProject#unfreeze()
     */
    public void unfreeze() {
        if (this.isFrozen())
            _root.removeAttribute(new Attribute("frozen", "yes"));
    }
    
    /**
     * @see main.java.memoranda.interfaces.IProject#getTitle()
     */
    public String getTitle() {
        Attribute ta = _root.getAttribute("title");
        if (ta != null)
            return ta.getValue();
        return "";
    }
    /**
     * @see main.java.memoranda.interfaces.IProject#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        setAttr("title", title);
    }
    
    private void setAttr(String name, String value) {
        Attribute a = _root.getAttribute(name);
        if (a == null) {
            if (value != null)
             _root.addAttribute(new Attribute(name, value));
        }
        else if (value != null)        
            a.setValue(value);
        else 
            _root.removeAttribute(a);
    }

        
    /**
     * @see net.sf.memoranda.Project#getTaskList()
     */
    /*public TaskList getTaskList() {
        return CurrentStorage.get().openTaskList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getNoteList()
     */
    /*public NoteList getNoteList() {
        return CurrentStorage.get().openNoteList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getResourcesList()
     */
    /*public ResourcesList getResourcesList() {
        return CurrentStorage.get().openResourcesList(this);
    }*/
}
