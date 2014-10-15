/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DSS.DataStruct;

/**
 *
 * @author jltrask
 */
public class UserLevelParameterSet {
    public ATMParameterSet atm;
    
    //<editor-fold defaultstate="collapsed" desc="Scenario Event Paremters">
    //<editor-fold defaultstate="collapsed" desc="Incident Parameters">
    // GP
    public float[][] IncidentCAFs_GP;
    public float[][] IncidentDAFs_GP;
    public float[][] IncidentSAFs_GP;
    
    // ML
    public float[][] IncidentCAFs_ML;
    public float[][] IncidentDAFs_ML;
    public float[][] IncidentSAFs_ML;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Work Zone Parameters">
    // GP
    public float[][] WorkZoneCAFs_GP;
    public float[][] WorkZoneDAFs_GP;
    public float[][] WorkZoneSAFs_GP;
    
    // ML
    public float[][] WorkZoneCAFs_ML;
    public float[][] WorkZoneDAFs_ML;
    public float[][] WorkZoneSAFs_ML;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Weather Parameters">
    // GP
    public float[][] WeatherCAFs_GP;
    public float[][] WeatherDAFs_GP;
    public float[][] WeatherSAFs_GP;
    
    // ML
    public float[][] WeatherCAFs_ML;
    public float[][] WeatherDAFs_ML;
    public float[][] WeatherSAFs_ML;
    //</editor-fold>
    //</editor-fold>
    
    public final static String ID_HSR_TYPE_PERCENT_OF_MAINLINE_LANE = "ID_HSR_TYPE_PERCENT_OF_MAINLINE_LANE";
    public final static String ID_HSR_TYPE_VPH = "ID_HSR_TYPE_VPH";
    
    public UserLevelParameterSet() {
        
        
    }
    
    public Object getHSRCapacity(String type) {
        switch (type) {
            default:
            case (ID_HSR_TYPE_VPH):
                return atm.hsrCapacity;
        }
    }
    
}
