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

    protected String location;
    protected String instructor;
    protected HashMap daysAndTimes;
    protected String CRN;
    protected int sectionNumber;

    public Section() {

    }

//=================  GETTERS ===============

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