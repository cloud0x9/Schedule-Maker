package ScheduleCreator.models;

import ScheduleCreator.Translator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class models a semester, which is a collection of sections.
 *
 * Can be a literal real-world semester, or another collection of sections (like courses the user saves for later).
 *
 * @author Nick Econopouly, Jamison Valentine
 *
 * Last Updated: 3/17/2020
 */

public  class Semester {

    protected final String name;
    protected final List<String> allCourses;
    protected List<Course> selectedCourses;
    protected List<Schedule> validSchedules;
    protected HashMap<Course,List<Section>> selectedSections;
    protected int numberOfSchedules;
    /**
     * Methods to implement
     * clearCalendar();
     * clearSelectedSection();
     */


    /**
     *
     * @param _name
     */
    public Semester(String _name) {
        this.name = _name;
        loadSelectedCoursesFromFile();
        this.allCourses = Translator.getCourses(this.name);
        this.validSchedules = new ArrayList();
        this.selectedSections = new HashMap();
    }

    //WORK IN PROGRESS
    public void addSelectedSection(Course _course, Section _section) {
        List<Section> list = new ArrayList();
        if (this.selectedSections.get(_course) == null) {
            list.add(_section);
            this.selectedSections.put(_course, list);
        }
        else {
            List<Section> sectionList = this.selectedSections.get(_course);
            if (!sectionList.contains(_section)) {
                sectionList.add(_section) ;
            }
        }

    }

    public HashMap<Course,List<Section>> getSelectedSections() {
        return this.selectedSections;
    }


    public void generateCourseList() {
         // generate the courseList by iterating through the sections.
         // this should only be performed on an actual semester (fall 2020, etc.)
         // and only after all sections have been imported
    }

    public Boolean addCourse(String _course) {
        Boolean contains = false;

        for (Course course: this.selectedSections.keySet()) {

            if (course.getFullText().equalsIgnoreCase(_course)) {

                contains = true;
                break;
            }
        }

        if (!contains) {

            this.selectedCourses.add(new Course(_course, this.name));

            Translator.saveCourse(_course, this.name);
            return true;

        } else {
            return false;
        }

    }

    public List<Schedule> getSchedules() {
        return this.validSchedules;
    }

    public void generateSchedules() {
        List<Schedule> list = generateSchedules(new ArrayList<Course>(this.selectedCourses));
        this.validSchedules = list;
        this.numberOfSchedules = this.validSchedules.size();
    }

    private List<Schedule> generateSchedules(List<Course> remainingCourses) {
        List<Schedule> validSchedules = new ArrayList();

        //if there are no remaining, return an empty list
        if (remainingCourses.isEmpty()) return validSchedules;

        //Select first course in the remaining course.
        Course course = remainingCourses.get(0);

        //If there is only one course in the remaing list, construct a new schedule for each section.
        if (remainingCourses.size() == 1) {
            for (Section section : course.getSections()) {
                Schedule newSchedule = new Schedule();
                newSchedule.addSection(section);
                validSchedules.add(newSchedule);
            }
        }

        //If there is more than one course in the list
        else {

            //Remove the current course from the remaining list
            remainingCourses.remove(course);

            for (Section section : course.getSections()) {
                for (Schedule schedule : generateSchedules(remainingCourses)) {
                    if (schedule.addSection(section)) validSchedules.add(schedule);
                }
            }

        }

        return validSchedules;
    }

// ============================== Getters ============================

    public String getName() {
        return this.name;
    }

    public int getNumberOfSchedules() {
        return this.numberOfSchedules;
    }
    
    public List<String> getAllCourses() {
        return this.allCourses;
    }

    //WORK IN PROGRESS
    public List<Section> getAvailableSections(Course _course) {
        ArrayList<Section> list = new ArrayList();
        return list;
    }

    public void loadSelectedCoursesFromFile() {

        List<String> list = Translator.getSelectedCourses(this.name);
        this.selectedCourses = new ArrayList();
        if (!list.isEmpty()) {
            for (String courseName: list) {
                this.selectedCourses.add(new Course(courseName, this.name));
            }
        }

    }

    public void removeCourse(String _course) throws Exception {
        Course courseToRemove;

        for (Course course: this.selectedCourses) {

            if (_course.equalsIgnoreCase(course.getFullText())) {

                courseToRemove = course;
                this.selectedSections.remove(courseToRemove);
                break;
            }
        }

        Translator.removeCourse(_course, this.name);
    }

    public List<Course> getSelectedCourses() {
        return this.selectedCourses;
    }

}