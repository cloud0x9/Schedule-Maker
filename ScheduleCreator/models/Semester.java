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
 * Last Updated: 3/31/2020
 */

public  class Semester {

    protected final String name;
    protected final List<String> allCourses;
    protected List<Schedule> schedules;
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
        this.allCourses = Translator.getCourses(this.name);
        this.schedules = new ArrayList();
        this.selectedSections = new HashMap();
        loadSelectedCoursesFromFile();
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

            this.selectedSections.put(new Course(_course, this.name), new ArrayList<Section>());

            Translator.saveCourse(_course, this.name);
            return true;

        } else {
            return false;
        }

    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public void generateSchedules() {
        List<Schedule> list = generateSchedules(new ArrayList<Course>(this.selectedSections.keySet()));
        this.schedules = list;
        this.numberOfSchedules = this.schedules.size();
    }

    private List<Schedule> generateSchedules(List<Course> selectedCourses) {

        List<Schedule> validSchedules = new ArrayList();

        //if there are no remaining, return an empty list
        if (selectedCourses.isEmpty()) return validSchedules;

        //Select first course in the remaining course.
        Course course = selectedCourses.get(0);

        /**
         * Base case for recursion
         *
         * If there is only one course in the remaining list, construct a new schedule for each section.
         **/
        if (selectedCourses.size() == 1) {
            for (Section section : course.getSections()) {
                Schedule newSchedule = new Schedule();
                newSchedule.addSection(section);
                validSchedules.add(newSchedule);
            }
        }

        //If there is more than one course in the list
        else {

            //Remove the current course from the remaining list
            List<Course> remainingCourses = new ArrayList(selectedCourses);
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
        if (!list.isEmpty()) {
            for (String courseName: list) {
                this.selectedSections.put(new Course(courseName, this.name), new ArrayList<Section>());
            }
        }

    }

    public void removeCourse(String _course) throws Exception {
        Course courseToRemove;

        for (Course course: this.selectedSections.keySet()) {

            if (_course.equalsIgnoreCase(course.getFullText())) {

                courseToRemove = course;
                this.selectedSections.remove(course);
                break;
            }
        }

        Translator.removeCourse(_course, this.name);
    }

    public List<Course> getSelectedCourses() {
        return new ArrayList<Course>(this.selectedSections.keySet());
    }

}