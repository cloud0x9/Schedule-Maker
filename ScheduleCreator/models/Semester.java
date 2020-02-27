package ScheduleCreator.models;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * This class models a semester, which is a collection of sections.
 * 
 * Can be a literal real-world semester, or another collection of sections (like courses the user saves for later).
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 2/27/2020
 */

public class Semester {

    protected ArrayList <ScheduleCreator.models.Section> sections;
    protected TreeMap<String,Semester> courseList;
    
    public Semester() {
    }
    
    public void addSection(ScheduleCreator.models.Section _section) {
        this.sections.add(_section);
    }
    
    public void generateCourseList() {
         // generate the courseList by iterating through the sections.
         // this should only be performed on an actual semester (fall 2020, etc.)
         // and only after all sections have been imported
    }
    
// ============================== Getters ============================

    public ArrayList<ScheduleCreator.models.Section> getSections() {
    return this.sections;
}
    public TreeMap<String, Semester> getCourseList() {
        return this.courseList;
    }
    
    
//    ========================= Setters ==============================
  
    public void setSections(ArrayList<ScheduleCreator.models.Section> _sections) {
        this.sections = _sections;
    }
    
    // No setter for courses
    
}
