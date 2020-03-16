package ScheduleCreator.models;

/**
 * This class stores various course information.
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/16/2020
 */

import java.util.ArrayList;

public class Course {

protected String abbreviation;
protected String courseNumber;
protected ArrayList<String> sections;

    public Course() {

    }

    public String getabbreviation() {
        return abbreviation;
    }

    public void setabbreviation(String _abbreviation) {
        this.abbreviation = _abbreviation;
    }

    //=================  GETTERS ===============

    public String getCourseNumber() {
        return courseNumber;
    }

    public ArrayList<String> getSections() {
        return sections;
    }

    //=================  SETTERS ===============

    public void setCourseNumber(String _courseNumber) {
        this.courseNumber = _courseNumber;
    }

    public void addSection(ArrayList<String> _sections) {
        this.sections = _sections;
    }

}
