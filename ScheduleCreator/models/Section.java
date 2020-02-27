package ScheduleCreator.models;

import java.util.HashMap;
/**
 * This class models information for course sections.
 *
 * @author Jamison Valentine, Ilyass Sfar, Nick Econopouly, Nathan Tolodzieki
 *
 * Last Updated: 2/17/2020
 */

public class Section {
    protected String className; // e.g. Software Engineering
    protected String abbreviation; // e.g. "CSC"
    protected int courseNumber; // e.g. 340
    protected int sectionNumber; // e.g. 02
    protected String location; // e.g. Petty Building 007
    protected String instructor; // e.g. Richard I Quigley
    protected HashMap daysAndTimes; // TODO
    protected String CRN; // e.g. 13198

    public Section() {
    }

//=================  GETTERS ===============

    public String getClassName() {
        return className;
    }
    public String getAbbreviation() {
        return abbreviation;
    }
    
    public int getCourseNumber() {
        return courseNumber;
    }
    
    public HashMap getDaysAndTimes() {
        return daysAndTimes;
    }

    public String getCRN() {
        return CRN;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getLocation() {
        return location;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

//=================  SETTERS ===============
    public void setClassName (String _className) {
        this.className = _className;
    }

    public void setAbbreviation(String _abbreviation) {
        this.abbreviation = _abbreviation;
    }
    public void setCourseNumber(int _courseNumber) {
        this.courseNumber = _courseNumber;
    }
    
    public void setSectionNumber(int _sectionNumber) {
        this.sectionNumber = _sectionNumber;
    }

    public void setLocation(String _location) {
        this.location = _location;
    }

    public void setInstructor(String _instructor) {
        this.instructor = _instructor;
    }

    public void setDaysAndTimes(HashMap _daysAndTimes) {
        this.daysAndTimes = _daysAndTimes;
    }

    public void setCRN(String _CRN) {
        this.CRN = _CRN;
    }
}
