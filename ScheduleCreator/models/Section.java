package ScheduleCreator.models;

/**
 * This class models information for course sections.
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/17/2020
 */

public class Section {

    protected final String location;
    protected final String instructor;
    protected final String daysAndTimes;
    protected final String CRN;
    protected final String sectionNumber;
    protected final Boolean isOnline;

    public Section(String _sectionNumber, String _daysAndTimes, String _location, String _instructor, String _CRN, Boolean _isOnline) {
        this.location = _location;
        this.instructor = _instructor;
        this.daysAndTimes = _daysAndTimes;
        this.CRN = _CRN;
        this.sectionNumber = _sectionNumber;
        this.isOnline = _isOnline;
    }

//=================  GETTERS ===============

    public String getDaysAndTimes() {
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

    public String getSectionNumber() {
        return sectionNumber;
    }

    @Override
    public String toString() {
        String string = "";

        if (!this.isOnline) {
            string = this.sectionNumber + " | " + this.daysAndTimes + " | " + this.location + " | "+ this.instructor + " | " + this.CRN;
        }
        else {
            string = this.sectionNumber + " | Online | " + this.instructor + " " + this.CRN;
        }
        return string;
    }

}