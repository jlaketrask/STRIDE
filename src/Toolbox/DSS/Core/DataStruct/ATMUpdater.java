/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Toolbox.DSS.Core.DataStruct;

import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import coreEngine.atdm.DataStruct.ATDMScenario;
import coreEngine.reliabilityAnalysis.DataStruct.IncidentEvent;
import coreEngine.reliabilityAnalysis.DataStruct.ScenarioInfo;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

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
     * @param atmScenario ATDMScenario data structure to hold applied ATM
     * adjustments
     * @param periodATM Contains period by period specification of ATM
     * strategies
     * @param userParams
     */
    public ATMUpdater(Seed seed, ATDMScenario atmScenario, PeriodATM[] periodATM, UserLevelParameterSet userParams) {
        this.seed = seed;
        this.atm = atmScenario;  //x = segment, y = period
        this.periodATM = periodATM;
        this.userParams = userParams;
    }

    public void update(int currPeriod) {
        PeriodATM currATM = periodATM[currPeriod];
        PeriodATM nextATM = periodATM[currPeriod + 1];

        // Applying Facility Strategies
// GP to ML Demand Diversion.
        if (currATM.getGP2MLDiversionUsed()) {
            // Applying GP to ML Demand Diversion
            atm.OAF().multiply(userParams.atm.getGP2MLDiversionFactor(), 0, currPeriod + 1);
            atm.DAF().multiply(userParams.atm.getGP2MLDiversionFactor(), 0, currPeriod + 1);

            // Updating next PeriodATM Instance
            if (currATM.getGP2MLDiversionDuration() > 1) {
                nextATM.setGP2MLDiversionUsed(Boolean.TRUE);
                nextATM.setGP2MLDiversionDuration(currATM.getGP2MLDiversionDuration() - 1);
            }
        }

        // Checking for any incidents active in the next period
        boolean activeIncident = false;
        boolean trafficDiversionApplicable = false;
        ArrayList<IncidentEvent> activeIncidents = new ArrayList<>();
        ScenarioInfo currScenarioInfo = seed.getRLScenarioInfo().get(1);
        ArrayList<IncidentEvent> incidents = currScenarioInfo.getGPIncidentEventList();
        // Checking if any incidents occur in time period
        for (IncidentEvent inc : incidents) {
            if (inc.checkActiveInPeriod(currPeriod + 1)) {
                activeIncident = true;
                activeIncidents.add(inc);
            }
        }
        Collections.sort(activeIncidents, IncidentEvent.Comparators.SEGMENT);
        int finalActiveIncidentLocation = (activeIncidents.size() > 0) ? activeIncidents.get(activeIncidents.size() - 1).getSegment() : 0;

        // Incident management
        if (activeIncident) {
            if (currATM.getIncidentManagementUsed()) {
                int incidentRemainingActive = -1;
                for (IncidentEvent inc : activeIncidents) {
                    // Check if the remaining incident duration <= to the incident duration reduction
                    if (inc.getEndPeriod() - currPeriod > 0 && inc.getEndPeriod() - currPeriod <= userParams.atm.incidentDurationReduction[inc.severity]) {
                        // If it is, apply Incident reduction
                        atm.CAF().multiply(1.0f / inc.getEventCAF(currPeriod + 1, inc.getSegment()),
                                inc.getSegment(),
                                currPeriod + 1
                        );
                        atm.OAF().multiply(1.0f / inc.getEventOAF(currPeriod + 1, inc.getSegment()),
                                inc.getSegment(),
                                currPeriod + 1
                        );
                        atm.DAF().multiply(1.0f / inc.getEventDAF(currPeriod + 1, inc.getSegment()),
                                inc.getSegment(),
                                currPeriod + 1
                        );
                        atm.SAF().multiply(1.0f / inc.getEventSAF(currPeriod + 1, inc.getSegment()),
                                inc.getSegment(),
                                currPeriod + 1
                        );
                        atm.LAF().add(-1 * inc.getEventLAF(currPeriod + 1, inc.getSegment()),
                                inc.getSegment(),
                                currPeriod + 1
                        );
                    } else {
                        // Signal that traffic (OFR/ONR) diversion is to be used
                        if (inc.getSegment() > incidentRemainingActive) {
                            incidentRemainingActive = inc.getSegment();
                        }
                        trafficDiversionApplicable = true;
                    }
                }

                if (incidentRemainingActive < finalActiveIncidentLocation) {
                    finalActiveIncidentLocation = incidentRemainingActive;
                }

            } else {
                trafficDiversionApplicable = true;
            }
        }

        // Updating next PeriodATM Instance
        if (currATM.getIncidentManagementUsed()) {
            if (currATM.getIncidentManagementDuration() > 1) {
                nextATM.setIncidentManagementUsed(Boolean.TRUE);
                nextATM.setIncidentManagementDuration(currATM.getIncidentManagementDuration() - 1);
            }
        }

        // Applying Segment Strategies
        for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {

            // Ramp Metering Check
            if (currATM.getRMUsed(seg)) {
                // Assigning Ramp Metering
                if (currATM.getRMType(seg) == PeriodATM.ID_RM_TYPE_USER) {
                    // User Specified
                    atm.RM().getRampMeteringFixRate().set(currATM.getRMRate(seg), seg, currPeriod + 1);
                    atm.RM().getRampMeteringType().set(CEConst.IDS_RAMP_METERING_TYPE_FIX, seg, currPeriod + 1);
                } else {
                    // Use adaptive
                    atm.RM().getRampMeteringType().set(CEConst.IDS_RAMP_METERING_TYPE_LINEAR, seg, currPeriod + 1);
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

            // Assigning OFR Traffic Diversion to all downstream segments up to incident
            if (currATM.getOFRDiversionUsed(seg) && trafficDiversionApplicable) {
                for (int segIdx = seg; segIdx <= finalActiveIncidentLocation; segIdx++) {
                    if (seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, segIdx) == CEConst.SEG_TYPE_OFR
                            || seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, segIdx) == CEConst.SEG_TYPE_W) {
                        //Off-ramp segment
                        atm.DAF().multiply((1.0f + userParams.atm.OFRdiversion[seg]), segIdx, currPeriod + 1);
                    }
                }
            }

            // Assigning ONR diversion if ONR is upstream of or at most downstream active incident
            if (currATM.getONRDiversionUsed(seg)) {
                atm.OAF().multiply((1.0f - userParams.atm.ONRdiversion[seg]), seg, currPeriod + 1);
            }

            // Updating next PeriodATM Instance
            // Downstream OFR diversion
            if (currATM.getOFRDiversionDuration(seg) > 1) {
                nextATM.setOFRDiversionUsed(Boolean.TRUE, seg);
                nextATM.setOFRDiversionDuration(currATM.getOFRDiversionDuration(seg) - 1, seg);
            }
            // ONR diversion
            if (currATM.getONRDiversionDuration(seg) > 1) {
                nextATM.setONRDiversionUsed(Boolean.TRUE, seg);
                nextATM.setONRDiversionDuration(currATM.getONRDiversionDuration(seg) - 1, seg);
            }

        } //  End loop over all segments

    }

    public boolean validate(int currPeriod) {
        PeriodATM currATM = periodATM[currPeriod];
        boolean atmValid = true;

        // Checking non-continuous shoulder running
        int numStarts = 0;
        if (currATM.getHSRUsed(0)) {
            numStarts++;
        }
        for (int seg = 1; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
            if (currATM.getHSRUsed(seg) && !currATM.getHSRUsed(seg - 1)) {
                numStarts++;
            }
        }
        if (numStarts >= 2) {
            atmValid = false;
        }

        if (!atmValid) {
            int status = JOptionPane.showConfirmDialog(null, "<HTML><Center>Warning: Hard Shoulder Running is not applied over a continuous stretch of the facility<br>"
                    + "Proceed anyways?", "Warning", JOptionPane.WARNING_MESSAGE);
            atmValid = (status == JOptionPane.OK_OPTION);
        }

        return atmValid;
    }

    public PeriodATM[] getAllPeriodATM() {
        return periodATM;
    }
}
