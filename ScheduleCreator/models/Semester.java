package ScheduleCreator.models;

import ScheduleCreator.Translator;
import java.util.ArrayList;
import java.util.List;

/**
 * This class models a semester, which is a collection of sections.
 *
 * Can be a literal real-world semester, or another collection of sections (like courses the user saves for later).
 *
 * @author Nick Econopouly, Jamison Valentine
 *
<<<<<<< HEAD
 * Last Updated: 3/17/2020
=======
 * Last Updated: 3/16/2020
>>>>>>> master
 */

public class Semester {

    protected final String name;
    protected ArrayList<Course> selectedCourses;
    protected ArrayList<Section> selectedSections;
    protected Schedule schedule;

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

    }

    //WORK IN PROGRESS
    public void addSelectedSection(Section _section) {
        this.selectedSections.add(_section);
    }

    public void generateCourseList() {
         // generate the courseList by iterating through the sections.
         // this should only be performed on an actual semester (fall 2020, etc.)
         // and only after all sections have been imported
    }

    public Boolean addCourse(String _course) {
        Boolean contains = false;

        for (Course course: this.selectedCourses) {

            if (course.getFullText().equalsIgnoreCase(_course)) {

                contains = true;
                break;
            }
        }

        if (!contains) {

            this.selectedCourses.add(new Course(_course, this.name));

            Translator.saveCourse(_course, this.name);
            printCourseNames();
            return true;
        } else {
            return false;
        }

    }

// ============================== Getters ============================

    public String getName() {
        return this.name;
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
                this.selectedCourses.remove(courseToRemove);
                break;
            }
        }

        Translator.removeCourse(_course, this.name);
        printCourseNames();

    }

    public List<Course> getSelectedCourses() {
        return this.selectedCourses;
    }

    public void printCourseNames() {
        System.out.println(this.name);
        StringBuilder content = new StringBuilder();

        for (Course course: this.selectedCourses) {
            System.out.println(course.getFullText());
        }
        System.out.println("\n\n");

    }
}