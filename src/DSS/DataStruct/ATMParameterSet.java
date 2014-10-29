/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DSS.DataStruct;

import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import java.util.Arrays;

/**
 *
 * @author jltrask
 */
public class ATMParameterSet {

    private Seed seed;
    
    public float hsrCapacity[];
    public Boolean[] diversionAtSeg;
    public float[] OFRdiversion;
    public float[] ONRdiversion;
    public int[] incidentDurationReduction;
    
    public boolean GP2MLDiversionEnabled;
    public int GP2MLDiversion;

    public final static String ID_HSR_TYPE_PERCENT_OF_MAINLINE_LANE = "ID_HSR_TYPE_PERCENT_OF_MAINLINE_LANE";
    public final static String ID_HSR_TYPE_VPH = "ID_HSR_TYPE_VPH";

    private final static float[] HSR_Capacities = new float[]{0.82f, 0.88f, 0.89f, 1.0f};

    public ATMParameterSet(Seed seed) {
        this.seed = seed;
        
        hsrCapacity = new float[5];
        incidentDurationReduction = new int[5];
        if  (seed != null) {
            diversionAtSeg = new Boolean[seed.getValueInt(CEConst.IDS_NUM_SEGMENT)];
            OFRdiversion = new float[seed.getValueInt(CEConst.IDS_NUM_SEGMENT)];
            ONRdiversion = new float[seed.getValueInt(CEConst.IDS_NUM_SEGMENT)];
        } else {
            diversionAtSeg = new Boolean[1];
            OFRdiversion = new float[1];
            ONRdiversion = new float[1];
        }
        useDefaults();
    }

    public Object getHSRCapacity(String type) {
        switch (type) {
            default:
            case (ID_HSR_TYPE_VPH):
                return hsrCapacity;
        }
    }

    private void useDefaults() {
        hsrCapacity[0] = HSR_Capacities[0];
        hsrCapacity[1] = HSR_Capacities[1];
        hsrCapacity[2] = HSR_Capacities[2];
        hsrCapacity[3] = HSR_Capacities[3];
        hsrCapacity[4] = HSR_Capacities[3];
        
        Arrays.fill(incidentDurationReduction, 0);

        Arrays.fill(diversionAtSeg, Boolean.FALSE);
        Arrays.fill(ONRdiversion, 0.0f);
        Arrays.fill(OFRdiversion, 0.0f);
        
        GP2MLDiversionEnabled = false;
        GP2MLDiversion = 0;
    }
    
    public float getGP2MLDiversionFactor() {
        System.out.println(GP2MLDiversion);
        System.out.println(seed.getValueFloat(CEConst.IDS_MAIN_DEMAND_VEH));
        System.out.println(1.0f - GP2MLDiversion/((float) seed.getValueFloat(CEConst.IDS_MAIN_DEMAND_VEH)));
        return 1.0f - GP2MLDiversion/((float) seed.getValueFloat(CEConst.IDS_MAIN_DEMAND_VEH));
    }
}
