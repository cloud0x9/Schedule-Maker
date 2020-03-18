package ScheduleCreator.models;

import java.util.Scanner;

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
    protected int startTime;
    protected int endTime;
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
        if (!this.isOnline) setTimes(_daysAndTimes);
    }
    
    public void setTimes(String _daysAndTimes) {
        Scanner input = new Scanner(_daysAndTimes);
        input.next();
        int start = Integer.parseInt(input.next().replace(":", ""));
        if (input.next().equals("pm")) {
            start += 1200;
            if (start >= 2400) start -= 2400;
        }
        input.next();
        int end = Integer.parseInt(input.next().replace(":", ""));
        if (input.next().equals("pm")) {
            end += 1200;
            if (end >= 2400) end -= 2400;
        }
        this.startTime = start;
        this.endTime = end;
        System.out.println("Start time: " + this.startTime);
        System.out.println("End time: " + this.endTime);
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