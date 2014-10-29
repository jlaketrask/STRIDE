/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DSS.DataStruct;

import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author jltrask
 */
public class PeriodATM {

    private final Seed seed;

    //Instance Specific variables
    private final int period;

    // Adjustment factors
    private final float[] caf;
    private final float[] saf;

    // Other ATM
    private final int[] rmDuration;
    private final int[] rampMetering;
    private final Boolean[] rampMeteringUsed;
    private final int[] rampMeteringType;
    private final int[] hsrDuration;
    //private final float[] hsrCapacity;
    private final Boolean[] hsrUsed;

    private final Boolean[] diversionUsed;
    private final int[] diversionDuration;
    
    private Boolean GP2MLDiversionUsed;
    private int GP2MLDiversionDuration;
    
    private Boolean incidentManagementUsed;
    private int incidentManagementDuration;

    private final ATMParameterSet ATMParams;
    // <editor-fold defaultstate="collapsed" desc="Indentifier Constants">
    public static final String ID_AFTYPE_CAF = "ID_AFTYPE_CAF";
    public static final String ID_AFTYPE_SAF = "ID_AFTYPE_SAF";
    public static final String ID_RAMP_METERING_DURATION = "ID_RAMP_METERING_DURATION";
    public static final String ID_RAMP_METERING_USED = "ID_RAMP_METERING_USED";
    public static final String ID_RAMP_METERING_TYPE = "ID_RAMP_METERING_TYPE";
    public static final int ID_RM_TYPE_NONE = 0;
    public static final int ID_RM_TYPE_USER = 1;
    public static final int ID_RM_TYPE_LINEAR = 2;
    public static final int ID_RM_TYPE_FUZZY = 3;
    public static final String ID_RAMP_METERING_RATE = "ID_RAMP_METERING";
    public static final String ID_HSR_DURATION = "ID_HSR_DURATION";
    public static final String ID_HSR_USED = "ID_HSR_USED";
    public static final String ID_HSR_CAPACITY = "ID_HSR_CAPACITY";
    public static final String ID_DIVERSION_USED = "ID_DIVERSION_USED";
    public static final String ID_DIVERSION_DURATION = "ID_DIVERSION_DURATION";

// </editor-fold>
    public PeriodATM(Seed seed, int period, ATMParameterSet ATMParams) {
        this.seed = seed;
        this.ATMParams = ATMParams;

        // Settting period
        this.period = period;
        int numSeg = seed.getValueInt(CEConst.IDS_NUM_SEGMENT);

        // Initializing adjustment factor arrays
        caf = new float[numSeg];
        Arrays.fill(caf, 1.0f);
        saf = new float[numSeg];
        Arrays.fill(saf, 1.0f);

        //Initializing other arrays
        rmDuration = new int[numSeg];
        Arrays.fill(rmDuration, 0);
        rampMeteringUsed = new Boolean[numSeg];
        Arrays.fill(rampMeteringUsed, false);
        rampMeteringType = new int[numSeg];
        Arrays.fill(rampMeteringType, 0);
        rampMetering = new int[numSeg];
        Arrays.fill(rampMetering, 2100);

        hsrDuration = new int[numSeg];
        Arrays.fill(hsrDuration, 0);
        hsrUsed = new Boolean[numSeg];
        Arrays.fill(hsrUsed, false);

        diversionUsed = new Boolean[numSeg];
        Arrays.fill(diversionUsed, false);
        diversionDuration = new int[numSeg];
        Arrays.fill(diversionDuration, 0);
        
        GP2MLDiversionUsed = false;
        GP2MLDiversionDuration = 0;
        
        incidentManagementUsed = false;
        incidentManagementDuration = 0;

    }

//<editor-fold defaultstate="collapsed" desc="Universal Getter">
    public float getValueFloat(String identifier, int seg) {
        switch (identifier) {
            case ID_AFTYPE_CAF:
                return caf[seg];
            case ID_AFTYPE_SAF:
                return saf[seg];
            //case ID_HSR_CAPACITY:
            //    return hsrCapacity[seg];
            default:
                throw new RuntimeException("Invalid Identifier");
        }
    }

    public int getValueInt(String identifier, int seg) {
        switch (identifier) {
            case ID_RAMP_METERING_TYPE:
                return rampMeteringType[seg];
            case ID_RAMP_METERING_RATE:
                return rampMetering[seg];
            case ID_RAMP_METERING_DURATION:
                return rmDuration[seg];
            case ID_HSR_DURATION:
                return hsrDuration[seg];
            case ID_DIVERSION_DURATION:
                return diversionDuration[seg];
            default:
                throw new RuntimeException("Invalid Identifier");
        }
    }

    public boolean getValueBool(String identifier, int seg) {
        switch (identifier) {
            case ID_RAMP_METERING_USED:
                return rampMeteringUsed[seg];
            case ID_HSR_USED:
                return hsrUsed[seg];
            case ID_DIVERSION_USED:
                return diversionUsed[seg];
            default:
                throw new RuntimeException("Invalid Identifier");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Universal Setter">
    public void setValueFloat(String identifier, float value, int seg) {
        switch (identifier) {
            case ID_AFTYPE_CAF:
                caf[seg] = value;
                break;
            case ID_AFTYPE_SAF:
                saf[seg] = value;
                break;
            //case ID_HSR_CAPACITY:
            //    hsrCapacity[seg] = value;
            //    break;
            default:
                throw new RuntimeException("Invalid Identifier");
        }
    }

    public void setValueInt(String identifier, int value, int seg) {
        switch (identifier) {
            case ID_RAMP_METERING_TYPE:
                rampMeteringType[seg] = value;
                break;
            case ID_RAMP_METERING_RATE:
                rampMetering[seg] = value;
                break;
            case ID_RAMP_METERING_DURATION:
                rmDuration[seg] = value;
                break;
            case ID_HSR_DURATION:
                hsrDuration[seg] = value;
                break;
            case ID_DIVERSION_DURATION:
                diversionDuration[seg] = value;
                break;
            default:
                throw new RuntimeException("Invalid Identifier");
        }
    }

    public void setValueBool(String identifier, boolean value, int seg) {
        switch (identifier) {
            case ID_RAMP_METERING_USED:
                rampMeteringUsed[seg] = value;
                break;
            case ID_HSR_USED:
                hsrUsed[seg] = value;
                break;
            case ID_DIVERSION_USED:
                diversionUsed[seg] = value;
                break;
            default:
                throw new RuntimeException("Invalid Identifier");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Setters for Fields">
    public void setCAF(float value, int seg) {
        caf[seg] = value;
    }

    public void setSAF(float value, int seg) {
        saf[seg] = value;
    }

    public void setRMDuration(int value, int seg) {
        this.rmDuration[seg] = value;
    }

    private void setRMUsed(Boolean value, int seg) {
        rampMeteringUsed[seg] = value;
    }

    public void setRMType(int value, int seg) {
        rampMeteringType[seg] = value;
        if (value == ID_RM_TYPE_USER || value == ID_RM_TYPE_LINEAR || value == ID_RM_TYPE_FUZZY) {
            setRMUsed(true, seg);
        } else {
            setRMUsed(false, seg);
        }
    }

    public void setRMRate(int value, int seg) {
        rampMetering[seg] = value;
    }

    public void setHSRDuration(int value, int seg) {
        this.hsrDuration[seg] = value;
    }

    public void setHSRUsed(Boolean value, int seg) {
        hsrUsed[seg] = value;
    }

    public void setDiversionDuration(int value, int seg) {
        diversionDuration[seg] = value;
    }

    public void setDiversionUsed(Boolean value, int seg) {
        diversionUsed[seg] = value;
    }
    
    public void setGP2MLDiversionUsed(Boolean value) {
        GP2MLDiversionUsed = value;
    }

    public void setGP2MLDiversionDuration(int GP2MLDiversionDuration) {
        this.GP2MLDiversionDuration = GP2MLDiversionDuration;
    }
    
    public void setIncidentManagementUsed(Boolean incidentManagementUsed) {
        this.incidentManagementUsed = incidentManagementUsed;
    }

    public void setIncidentManagementDuration(int incidentManagementDuration) {
        this.incidentManagementDuration = incidentManagementDuration;
    }
    
    //public void setHSRCapacity(float value, int seg) {
    //    hsrCapacity[seg] = value;
    //}
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Getters for Fields">
    public int getPeriod() {
        return period;
    }

    public float getCAF(int seg) {
        return caf[seg];
    }

    public float getSAF(int seg) {
        return saf[seg];
    }

    public int getRMDuration(int seg) {
        return rmDuration[seg];
    }

    public Boolean getRMUsed(int seg) {
        return rampMeteringUsed[seg];
    }

    public int getRMType(int seg) {
        return rampMeteringType[seg];
    }

    public int getRMRate(int seg) {
        return rampMetering[seg];
    }

    public int getHSRDuration(int seg) {
        return hsrDuration[seg];
    }

    public Boolean getHSRUsed(int seg) {
        return hsrUsed[seg];
    }

    public int getDiversionDuration(int seg) {
        return diversionDuration[seg];
    }

    public Boolean getDiversionUsed(int seg) {
        return diversionUsed[seg];
    }
    
    public Boolean getGP2MLDiversionUsed() {
        return GP2MLDiversionUsed;
    }
    
    public int getGP2MLDiversionDuration() {
        return GP2MLDiversionDuration;
    }
    
    public Boolean getIncidentManagementUsed() {
        return incidentManagementUsed;
    }
    
    public int getIncidentManagementDuration() {
        return incidentManagementDuration;
    }

    //public float getHSRCapacity(int seg) {
    //    return hsrCapacity[seg];
    //}
//</editor-fold>
    public boolean diversionAvailableAtSegment(int seg) {
        return ATMParams.diversionAtSeg[seg];
    }
    
    public boolean checkEmpty() {
        boolean isEmpty = true;
        
        for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
            if (rampMeteringUsed[seg] || hsrUsed[seg] || diversionUsed[seg]) {
                isEmpty = false;
                break;
            }
        }
        
        if (GP2MLDiversionUsed) {
            isEmpty = false;
        }
        
        if (incidentManagementUsed) {
            isEmpty = false;
        }
        
        if (!isEmpty) {
            int status = JOptionPane.showConfirmDialog(null, "<HTML><Center>Warning: Currently selected strategies will be ignored.<br>"
                    + "Proceed anyways?","Warning", JOptionPane.WARNING_MESSAGE);
            isEmpty = (status == JOptionPane.OK_OPTION);
        }
        return isEmpty;
    }

    private int arrayMax(int[] arr) {
        int max = -9999;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
}
