package ScheduleCreator.models;

import ScheduleCreator.Adapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class models a semester, which is a collection of sections.
 *
 * @author Nick Econopouly, Jamison Valentine
 *
 * Last Updated: 4/21/2020
 */
public class Semester {

    protected final String name;
    protected final List<String> allCourses;
    protected List<Schedule> schedules;
    protected LinkedHashMap<Course, List<Section>> selectedSections;
    protected Adapter adapter = new Adapter();

    public Semester(String _name) {
        this.name = _name;
        this.allCourses = this.adapter.getCourses(this.name);
        this.schedules = new ArrayList();
        this.selectedSections = new LinkedHashMap();
        this.loadSelectedCoursesFromFile();
        this.generateSchedules();
    }

    public void addSelectedSection(Course _course, Section _section) {
        List<Section> list = new ArrayList();

        //If the selected Course is not the the Map, add it.
        if (this.selectedSections.get(_course) == null) {
            list.add(_section);
            this.selectedSections.put(_course, list);
        } else {

            //Checks to see if this section is already selected.
            List<Section> sectionList = this.selectedSections.get(_course);
            if (!sectionList.contains(_section)) {
                sectionList.add(_section);
            }
        }
    }

    public Boolean addCourse(Course _course) {
        if (!this.selectedSections.keySet().contains(_course)) {
            this.selectedSections.put(_course, _course.getSections());
            this.adapter.saveCourse(_course.getFullText(), this.name);
            return true;
        }
        return false;
    }

    public final void generateSchedules() {
        List<Schedule> list = generateSchedules(new ArrayList(this.selectedSections.keySet()));
        this.schedules = list;
    }

    private List<Schedule> generateSchedules(List<Course> selectedCourses) {

        List<Schedule> validSchedules = new ArrayList();

        //if there are no remaining, return an empty list
        if (selectedCourses.isEmpty()) {
            return validSchedules;
        }

        //Select first course in the remaining list of coursec.
        Course course = selectedCourses.get(0);

        /**
         * Base case for recursion
         *
         * If there is only one course in the remaining list, construct a new
         * schedule for each section.
         *
         */
        if (selectedCourses.size() == 1) {
            for (Section section : this.selectedSections.get(course)) {
                Schedule newSchedule = new Schedule();
                newSchedule.addSection(section);
                validSchedules.add(newSchedule);
            }
        } //If there is more than one course in the list
        else {

            //Remove the current course from the remaining list
            List<Course> remainingCourses = new ArrayList(selectedCourses);
            remainingCourses.remove(course);

            this.selectedSections.get(course).forEach((section) -> {
                generateSchedules(remainingCourses).stream().filter((schedule) -> (schedule.addSection(section))).forEachOrdered((schedule) -> {
                    validSchedules.add(schedule);
                });
            });
        }
        return validSchedules;
    }

    private void loadSelectedCoursesFromFile() {

        List<String> list = this.adapter.getSelectedCourses(this.name);
        if (!list.isEmpty()) {
            list.forEach((courseName) -> {
                Course course = new Course(courseName, this.name);
                this.selectedSections.put(new Course(courseName, this.name), course.getSections());
            });
        }
    }

    /**
     * Removes the course from the Semester.
     *
     * @param _course
     */
    public void removeCourse(String _course) {

        for (Course course : this.selectedSections.keySet()) {
            if (_course.equalsIgnoreCase(course.getFullText())) {
                this.selectedSections.remove(course);
                break;
            }
        }
        this.adapter.removeCourse(_course, this.name);
    }

    @Override
    public boolean equals(Object _obj) {
        if (!(_obj instanceof Semester)) {
            return false;
        }
        Semester otherSemester = (Semester) _obj;
        return this.name.equalsIgnoreCase(otherSemester.getName());
    }

// ============================== Getters ============================
    public String getName() {
        return this.name;
    }

    public int getNumberOfSchedules() {
        return this.schedules.size();
    }

    public List<String> getAllCourses() {
        return this.allCourses;
    }

    public List<Course> getSelectedCourses() {
        return new ArrayList(this.selectedSections.keySet());
    }

    public List<String> getSelectedCourseStrings() {
        return this.adapter.getSelectedCourses(this.name);
    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public LinkedHashMap<Course, List<Section>> getSelectedSections() {
        return this.selectedSections;
    }
}
