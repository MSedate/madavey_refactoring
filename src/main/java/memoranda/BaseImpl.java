package main.java.memoranda;

import nu.xom.Element;
/*TASK 2-2 SMELL BETWEEN CLASSES
 * < There was a code smell in ProjectImpl and
 * TaskImpl where they both had duplicate code.
 * The method getDescription() and setDescription()
 * were the exact same in both classes.Therefore I changed the 
 * Element method to have the same name "_root" and then
 * made this new parent class holding the two methods.  Now TaskImpl and 
 * ProjectImpl both extend this class. >
 */
public class BaseImpl {
	
    protected Element _root = null;
    
    public String getDescription() {
    	Element thisElement = _root.getFirstChildElement("description");
    	if (thisElement == null) {
    		return null;
    	}
    	else {
       		return thisElement.getValue();
    	}
    }

    public void setDescription(String s) {
    	Element desc = _root.getFirstChildElement("description");
    	if (desc == null) {
        	desc = new Element("description");
            desc.appendChild(s);
            _root.appendChild(desc);    	
    	}
    	else {
            desc.removeChildren();
            desc.appendChild(s);    	
    	}
    }
}
