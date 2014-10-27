/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DSS.DataStruct;

import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import coreEngine.atdm.DataStruct.ATDMScenario;

/**
 *
 * @author jltrask
 */
public class ATMUpdater {

    private final Seed seed;
    private final ATDMScenario atm;
    private final PeriodATM[] periodATM;
    private final UserLevelParameterSet userParams;

    /**
     * Constructor for the ATM Updater
     *
     * @param seed Seed for to which the updater is attached
     * @param atmScenario ATDMScenario datastruct to hold applied ATM
     * adjustments
     * @param periodATM Contains period by period specification of ATM
     * strategies
     * @param userParams
     */
    public ATMUpdater(Seed seed, ATDMScenario atmScenario, PeriodATM[] periodATM, UserLevelParameterSet userParams) {
        this.seed = seed;
        this.atm = atmScenario;  //x = segment, y = period
        this.periodATM = periodATM;
        if (userParams != null) {
            this.userParams = userParams;
        } else {
            this.userParams = new UserLevelParameterSet();
            //this.userParams.useDefaults();
        }
    }

    public void update(int currPeriod) {
        PeriodATM currATM = periodATM[currPeriod];
        PeriodATM nextATM = periodATM[currPeriod + 1];

        for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {

            // Ramp Metering Check
            if (currATM.getRMUsed(seg)) {
                // Assigning Ramp Metering
                if (currATM.getRMType(seg) == PeriodATM.ID_RM_TYPE_USER) {
                    // User Specified
                    atm.RM().set(currATM.getRMRate(seg), seg, currPeriod + 1);
                    atm.setRampMetering(true);
                } else {
                    // Use adaptive
                    System.out.println("Using adaptive.");
                    seed.setValue(CEConst.IDS_RAMP_METERING_TYPE, CEConst.IDS_RAMP_METERING_TYPE_LINEAR, seg, currPeriod + 1);
                }

                if (currATM.getRMDuration(seg) > 1) {
                    // Updating next PeriodATM instance
                    nextATM.setRMType(currATM.getRMType(seg), seg);
                    nextATM.setRMRate(currATM.getRMRate(seg), seg);
                    nextATM.setRMDuration(currATM.getRMDuration(seg) - 1, seg);
                } else {
                    nextATM.setRMDuration(0, seg);
                }
            }

            // Hard Shoulder Running Check
            if (currATM.getHSRUsed(seg)) {
                // Assigning Hard Shoulder Running
                int numLanesSegment = seed.getValueInt(CEConst.IDS_MAIN_NUM_LANES_IN, seg);
                if (currATM.getHSRUsed(seg)) {
                    atm.LAF().add(1, seg, currPeriod + 1); // Adding lane

                    // Calculating new segment CAF using shoulder CAF 
                    float seedCAF = seed.getValueFloat(CEConst.IDS_GP_USER_CAF, seg, currPeriod + 1);
                    float rlCAF = seed.getRLCAF(1, seg, currPeriod + 1, CEConst.SEG_TYPE_GP);
                    //float avgCAF = (seedCAF + rlCAF) / 2.0f;
                    float combinedCAF = seedCAF * rlCAF;
                    float newCAF = ((numLanesSegment * combinedCAF * atm.CAF().get(seg, currPeriod + 1)) + userParams.atm.hsrCapacity[Math.min(seed.getValueInt(CEConst.IDS_MAIN_NUM_LANES_IN, seg, currPeriod) - 1, 4)]) / (numLanesSegment + 1);
                    atm.CAF().set((newCAF / combinedCAF), seg, currPeriod + 1);
                }

                // Update next PeriodATM Instance
                if (currATM.getHSRDuration(seg) > 1) {
                    nextATM.setHSRUsed(Boolean.TRUE, seg);
                    //nextATM.setHSRCapacity(, seg);
                    nextATM.setHSRDuration(currATM.getHSRDuration(seg) - 1, seg);
                } else {
                    nextATM.setHSRDuration(0, seg);
                }
            }
        }

    }

    public PeriodATM[] getAllPeriodATM() {
        return periodATM;
    }
}
