package ScheduleCreator.models;

import ScheduleCreator.Translator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class models a semester, which is a collection of sections.
 *
 * @author Nick Econopouly, Jamison Valentine
 *
 * Last Updated: 3/28/2020
 */
public class Semester {

    protected final String name;
    protected final List<String> allCourses;
    protected List<Schedule> schedules;
    protected LinkedHashMap<Course, List<Section>> selectedSections;


    public Semester(String _name) {
        this.name = _name;
        this.allCourses = Translator.getCourses(this.name);
        this.schedules = new ArrayList();
        this.selectedSections = new LinkedHashMap();
        this.loadSelectedCoursesFromFile();
        this.generateSchedules();
    }

    public void addSelectedSection(Course _course, Section _section) {
        List<Section> list = new ArrayList();
        if (this.selectedSections.get(_course) == null) {
            list.add(_section);
            this.selectedSections.put(_course, list);
        } else {
            List<Section> sectionList = this.selectedSections.get(_course);
            if (!sectionList.contains(_section)) {
                sectionList.add(_section);
            }
        }
    }

    public LinkedHashMap<Course, List<Section>> getSelectedSections() {
        return this.selectedSections;
    }

    public Boolean addCourse(Course _course) {
        if (!this.selectedSections.keySet().contains(_course)) {
            this.selectedSections.put(_course, _course.getSections());
            Translator.saveCourse(_course.getFullText(), this.name);
            return true;
        }
        return false;
    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public void generateSchedules() {
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

            for (Section section : this.selectedSections.get(course)) {
                for (Schedule schedule : generateSchedules(remainingCourses)) {
                    if (schedule.addSection(section)) {
                        validSchedules.add(schedule);
                    }
                }
            }
        }

        return validSchedules;
    }

    private void loadSelectedCoursesFromFile() {

        List<String> list = Translator.getSelectedCourses(this.name);
        if (!list.isEmpty()) {
            for (String courseName : list) {
                Course course = new Course(courseName, this.name);
                this.selectedSections.put(new Course(courseName, this.name), course.getSections());
            }
        }
    }

    public void removeCourse(String _course) {

        for (Course course : this.selectedSections.keySet()) {
            if (_course.equalsIgnoreCase(course.getFullText())) {
                this.selectedSections.remove(course);
                break;
            }
        }

        Translator.removeCourse(_course, this.name);
    }

    @Override
    public boolean equals(Object _obj) {
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
        return Translator.getSelectedCourses(this.name);
    }
}
