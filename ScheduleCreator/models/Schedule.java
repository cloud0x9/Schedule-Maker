package ScheduleCreator.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class keeps track of added course sections and produces CRNs.
 *
 * @author Jamison Valentine
 *
 * Last Updated: 3/16/2020
 */
public class Schedule {

    protected List<Section> addedSections;
    protected String totalCredits;

    //checkConflicts
    //addSection
    //removeSection
    //getCRNS
    //saveSchedule
    //resetSchedule
    //generateRandomSchedules
    public Schedule() {
        addedSections = new ArrayList();
    }

    public Boolean addSection(Section _newSection) {
        if (this.addedSections.size() > 0 && !_newSection.isOnline) {

            for (Section existingSection : addedSections) {

                Boolean sameDay = false;
                for (char day : _newSection.getDays().toCharArray()) {
                    if (existingSection.getDays().contains("" + day)) sameDay = true;
                }
                System.out.println("Checking " + _newSection.getCourseID() + " - " + _newSection.toString());
                System.out.println("Checking " + existingSection.getCourseID() + " - " + existingSection.toString());

                if (sameDay && existingSection.isOnline) {
                    if (existingSection.endTime >= _newSection.startTime && existingSection.startTime < _newSection.startTime) {
                        System.out.println("Schedule Conflict");
                        return false;
                    }
                    if (existingSection.startTime <= _newSection.endTime && existingSection.endTime > _newSection.endTime) {
                        System.out.println("Schedule Conflict");
                        return false;
                    }
                }
            }
        }

        System.out.print("Section " + _newSection.courseID + " - " + _newSection.sectionNumber + " added successfully");
        this.addedSections.add(_newSection);
        return true;
    }

    public List<Section> getAddedSections() {
        return this.addedSections;
    }

    @Override
        public String toString() {
        StringBuilder string = new StringBuilder();
        for (Section section : addedSections) {
            string.append("(" + section.getCourseID() + " - " + section.getSectionNumber() + ")\n");
        }
        return string.toString();
    }
}
