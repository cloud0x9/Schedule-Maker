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
            Translator.getAllData("raw/spring2020");
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }   
    } 
}