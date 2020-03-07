package ScheduleCreator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wrycode
 */
public class Tests {

    public static void main(String[] args) {
        try {
            /*
            try {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            }       //ParseData.getAllData("raw/spring2020");
            } catch (Exception ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
            //should return TBA for both day and time since ADS 600 is a online class
            //DBAdapter.getTime("ADS 600", "Spring2020");
            //DBAdapter.getDay("ADS 600", "Spring2020");

            //should return real info for CSC 250 - 01
            DBAdapter.getTime("CSC 250 - 01", "Spring2020");
            DBAdapter.getDay("CSC 250 - 01", "Spring2020");
            DBAdapter.getBuilding("CSC 250 - 01", "Spring2020");
            DBAdapter.getCRN("CSC 250 - 01", "Spring2020");
            DBAdapter.getInstructor("CSC 250 - 01", "Spring2020");
            } catch (Exception ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
            }
            //*/
            
            DBAdapter.regenDB();
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
