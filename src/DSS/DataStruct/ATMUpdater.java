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

    /**
     * Constructor for the ATM Updater
     *
     * @param seed Seed for to which the updater is attached
     * @param atmScenario ATDMScenario datastruct to hold applied ATM
     * adjustments
     * @param periodATM Contains period by period specification of ATM
     * strategies
     */
    public ATMUpdater(Seed seed, ATDMScenario atmScenario, PeriodATM[] periodATM) {
        this.seed = seed;
        this.atm = atmScenario;  //x = segment, y = period
        this.periodATM = periodATM;
    }

    public void update(int currPeriod) {
        PeriodATM currATM = periodATM[currPeriod];
        PeriodATM nextATM = periodATM[currPeriod + 1];

        for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {

            // Ramp Metering Check
            if (currATM.getRMUsed(seg)) {
                // Assigning Ramp Metering
                atm.RM().set(currATM.getRMRate(seg), seg, currPeriod + 1);
                atm.setRampMetering(true);
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
                    float seedCAF = seed.getValueFloat(CEConst.IDS_U_CAF_GP, seg, currPeriod + 1);
                    float rlCAF = seed.getRLCAF(1, seg, currPeriod + 1, CEConst.SEG_TYPE_GP);
                    float avgCAF = (seedCAF + rlCAF) / 2.0f;
                    float newCAF = ((numLanesSegment * avgCAF * atm.CAF().get(seg, currPeriod + 1)) + currATM.getHSRCapacity(seg)) / (numLanesSegment + 1);
                    atm.CAF().set((newCAF / avgCAF), seg, currPeriod + 1);
                }

                // Update next PeriodATM Instance
                if (currATM.getHSRDuration(seg) > 1) {
                    nextATM.setHSRUsed(Boolean.TRUE, seg);
                    nextATM.setHSRCapacity(currATM.getHSRCapacity(seg), seg);
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
