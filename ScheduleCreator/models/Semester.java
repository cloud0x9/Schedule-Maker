package ScheduleCreator.models;

import java.util.ArrayList;
import java.util.TreeMap;
import ScheduleCreator.models.Section;

/**
 * This class models a semester, which is a collection of sections.
 *
 * Can be a literal real-world semester, or another collection of sections (like courses the user saves for later).
 *
 * @author Nick Econopouly, Jamison Valentine
 *
 * Last Updated: 3/16/2020
 */

public class Semester {

    protected final String name;
    protected ArrayList<Course> selectedCourses;
    protected ArrayList<Section> selectedSections;
    protected TreeMap<String,Semester> courseList;
    protected Schedule schedule;

    public Semester(String _name) {
        this.name = _name;
    }

    public void addSelectedSection(Section _section) {
        this.selectedSections.add(_section);
    }

    public void generateCourseList() {
         // generate the courseList by iterating through the sections.
         // this should only be performed on an actual semester (fall 2020, etc.)
         // and only after all sections have been imported
    }

// ============================== Getters ============================

    public ArrayList<Section> getAvailableSections(Course _course) {
        ArrayList<Section> list = new ArrayList();
        return list;
    }
    public ArrayList<Section> getSelectedSections() {
    return this.selectedSections;

}
    public TreeMap<String, Semester> getCourseList() {
        return this.courseList;
    }


}