package coreEngine;

import GUI.major.MainWindow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represent a single general purpose (GP) or managed lane (ML)
 * segment. Only used in coreEngine package (no public methods). This class
 * includes 1. Input and output data for GP segment. 2. Both under saturated and
 * over saturated GP segment calculations.
 *
 * @author Shu Liu
 */
class GPMLSegment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 67867856456L;

    // <editor-fold defaultstate="collapsed" desc="INPUT DATA">
    /**
     *
     */
    private Seed seed;

    //input data for GP segment
    // <editor-fold defaultstate="collapsed" desc="FIXED OVER TIME INPUT DATA">
    //input data fixed over time
    /**
     * Segment type (Basic, On Ramp, Off Ramp, Weaving, or Overlapping)
     */
    int inType = CEConst.SEG_TYPE_B;
    /**
     * Number of periods
     */
    int inNumPeriod = 1;
    /**
     * Adjacent upstream segment
     */
    GPMLSegment inUpSeg;
    /**
     * Adjacent downstream segment
     */
    GPMLSegment inDownSeg;
    /**
     * Parallel GP/ML segment
     */
    GPMLSegment inParallelSeg;
    /**
     * Segment type (General Purpose or Managed Lane)
     */
    int inGPMLType = CEConst.SEG_TYPE_GP;
    /**
     * Capacity drop percentage for two capacity
     */
    float inCapacityDropPercentage = 0;
    /**
     * Segment index, for debug only, not used for calculation
     */
    int inIndex = 0;
    /**
     * Segment length in feet
     */
    int inSegLength_ft = 2640;
    /**
     * Acceleration or deceleration lane length in feet, used for on ramp or off
     * ramp segment only
     */
    int inAccDecLength_ft = 500;
    /**
     * Short length in feet, used for weave segment only
     */
    int inShort_ft = 1500;
    /**
     * Lane width in feet
     */
    int inLaneWidth_ft = 12;
    /**
     * Lateral clearance in feet
     */
    int inLateralClearance_ft = 4;
    /**
     * Terrain type
     */
    int inTerrain = CEConst.TERRAIN_LEVEL;
    /**
     * Truck passenger car equivalent ET
     */
    float inET = 1.5f;
    /**
     * RV passenger car equivalent ER
     */
    float inER = 1.2f;
    /**
     * Peak hour factor, currently not in use
     */
    float inPeak = 1.0f;
    /**
     * Driver population factor, currently not in use
     */
    float inDriver = 1.0f;
    /**
     * On ramp side
     */
    int inOnSide = CEConst.RAMP_SIDE_RIGHT;
    /**
     * Off ramp side
     */
    int inOffSide = CEConst.RAMP_SIDE_RIGHT;
    /**
     * Number of lanes weave, can only be 2 or 3
     */
    int inNWL = 2;
    /**
     * Minimum number of lane change from ramp to freeway
     */
    int inLCRF = 1;
    /**
     * Minimum number of lane change from freeway to ramp
     */
    int inLCFR = 1;
    /**
     * Minimum number of lane change from ramp to ramp
     */
    int inLCRR = 0;
    /**
     * Number of steps per hour = 60 * 4
     */
    static final int T = 240;
    /**
     * Number of steps per analysis period
     */
    static final int NUM_STEPS = 60;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CHANGE OVER TIME INPUT DATA">
    //input data changing over time
    /**
     * Mainline number of lanes
     */
    ArrayList<Integer> inMainlineNumLanes;
    /**
     * On ramp number of lanes
     */
    ArrayList<Integer> inOnNumLanes;
    /**
     * Off ramp number of lanes
     */
    ArrayList<Integer> inOffNumLanes;
    /**
     * Mainline demand in vph, only required for the first segment
     */
    ArrayList<Integer> inMainlineDemand_veh;
    /**
     * On ramp demand in vph
     */
    ArrayList<Integer> inOnDemand_veh;
    /**
     * Off ramp demand in vph
     */
    ArrayList<Integer> inOffDemand_veh;
    /**
     * Ramp to ramp demand in vph
     */
    ArrayList<Integer> inRRDemand_veh;
    /**
     * Mainline free flow speed, mph
     */
    ArrayList<Integer> inMainlineFFS;
    /**
     * Mainline truck percentage
     */
    ArrayList<Float> inMainlineTruck;
    /**
     * Mainline RV percentage
     */
    ArrayList<Float> inMainlineRV;
    /**
     * On ramp free flow speed, mph
     */
    ArrayList<Integer> inOnFFS;
    /**
     * Off ramp free flow speed, mph
     */
    ArrayList<Integer> inOffFFS;
    /**
     * User capacity adjustment factor
     */
    ArrayList<Float> inUCAF;
    /**
     * User origin demand adjustment factor
     */
    ArrayList<Float> inUOAF;
    /**
     * User destination demand adjustment factor
     */
    ArrayList<Float> inUDAF;
    /**
     * User free flow speed adjustment factor
     */
    ArrayList<Float> inUSAF;
    /**
     * Ramp metering rate vph
     */
    ArrayList<Integer> inRM_veh;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MANAGED LANE ADDITIONAL INPUT DATA">
    /**
     *
     */
    int inMLMethod = CEConst.ML_METHOD_HOV;

    /**
     *
     */
    int inMLSeparation = CEConst.ML_SEPARATION_MARKING;

    /**
     *
     */
    int inMLMinLC = 1;

    /**
     *
     */
    int inMLMaxLC = 1;

    /**
     *
     */
    boolean inMLHasCrossWeave = false;

    /**
     *
     */
    int inMLCrossWeaveLCMin = 1;

    /**
     *
     */
    ArrayList<Integer> inMLCrossWeaveVolume;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ADJUSTED INPUT">
    /**
     * TRD: total ramp density
     */
    transient float totalRampDensity;

    /**
     * Heavy vehicle adjustment factor
     */
    transient float[] inMainlineFHV;
    /**
     * Cross weaving capacity adjustment factor
     */
    transient float[] inCrossCAF;
    /**
     * Mainline number of lanes
     */
    transient int[] scenMainlineNumLanes;
    /**
     * Process segment type
     */
    transient int[] scenType;
    /**
     * Mainline demand in vph, only required for the first segment
     */
    transient float[] scenMainlineDemand_veh;
    /**
     * On ramp demand in vph
     */
    transient float[] scenOnDemand_veh;
    /**
     * Off ramp demand in vph
     */
    transient float[] scenOffDemand_veh;
    /**
     * Ramp to ramp demand in vph
     */
    transient float[] scenRRDemand_veh;
    /**
     * Ramp Metering Rate
     */
    transient float[] scenRM_veh;
    /**
     * Weave volume pc/h
     */
    transient float[] scenVW;
    /**
     * Non-weave volume pc/h
     */
    transient float[] scenVNW;
    /**
     * Mainline free flow speed, mph
     */
    transient float[] scenMainlineFFS;
    /**
     * On ramp free flow speed, mph
     */
    transient float[] scenOnFFS;
    /**
     * Off ramp free flow speed, mph
     */
    transient float[] scenOffFFS;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OUTPUT DATA">
    /**
     * Mainline capacity in vph
     */
    transient float[] scenMainlineCapacity_veh;
    /**
     * On ramp capacity in vph
     */
    transient float[] scenOnCapacity_veh;
    /**
     * Off ramp capacity in vph
     */
    transient float[] scenOffCapacity_veh;
    /**
     * Volume / capacity ratio
     */
    transient float[] scenVC;
    /**
     * Max DC for this segment over all periods in one scenario
     */
    transient float scenMaxDC;
    /**
     * Max VC for this segment over all periods in one scenario
     */
    transient float scenMaxVC;
    /**
     * Overall speed for each segment, mph
     */
    transient float[] scenSpeed;
    /**
     * Overall density for each segment, in veh/mi/ln
     */
    transient float[] scenAllDensity_veh;
    /**
     * Overall density for each segment, in pc/mi/ln
     */
    //transient float[] scenAllDensity_pc;
    /**
     * Influence area density for ONR or OFR segment, in pc/mi/ln
     */
    transient float[] scenIADensity_pc;
    /**
     * Density based LOS
     */
    //transient String[] scenDensityLOS;
    /**
     * Demand based LOS
     */
    //transient String[] scenDemandLOS;
    /**
     * Mainline volume served in vph
     */
    transient float[] scenMainlineVolume_veh;
    /**
     * On ramp volume served in vph
     */
    transient float[] scenOnVolume_veh;
    /**
     * Off ramp volume served in vph
     */
    transient float[] scenOffVolume_veh;
    /**
     * Capacity drop, two capacity
     */
    //transient float[] scenCapaDrop;
    /**
     * Whether is front clearing queue
     */
    transient boolean[] scenIsFrontClearingQueues;

    //Extended Output Data
    /**
     * Actual travel time
     */
    //transient float[] scenActualTime;
    /**
     * Free flow speed travel time
     */
    //transient float[] scenFFSTime;
    /**
     * Mainline delay
     */
    //transient float[] scenMainlineDelay;
    /**
     * On ramp delay
     */
    transient float[] scenOnDelay;
    /**
     * System delay
     */
    transient float[] scenSysDelay;
    /**
     * VMTD veh-miles / interval
     */
    transient float[] scenVMTD;
    /**
     * VMTV veh-miles / interval
     */
    transient float[] scenVMTV;
    /**
     * VHT travel / interval (hrs)
     */
    transient float[] scenVHT;
    /**
     * VHD delay / interval (hrs)
     */
    transient float[] scenVHD;
    /**
     * Travel time index
     */
    //transient float[] scenTTI;
    /**
     * Deny entry queue length
     */
    transient float[] scenDenyQ;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS AND MEMORY LOCATE">
    /**
     * Constructor of GPSegment
     *
     * @param seed seed that contains this segment
     * @param inNumPeriod number of periods
     */
    GPMLSegment(Seed seed, int inNumPeriod) {
        this.seed = seed;
        this.inNumPeriod = inNumPeriod;
        initInput();
    }

    /**
     * Constructor of GPSegment
     *
     * @param seed seed that contains this segment
     */
    GPMLSegment(Seed seed) {
        this(seed, 1);
    }

    /**
     * Create memory space for input data
     */
    private void initInput() {
        inMainlineNumLanes = CEHelper.int_1D(inNumPeriod, 3); //mainline number of lanes
        inOnNumLanes = CEHelper.int_1D(inNumPeriod, 1); //on ramp number of lanes
        inOffNumLanes = CEHelper.int_1D(inNumPeriod, 1); //off ramp number of lanes

        inMainlineDemand_veh = CEHelper.int_1D(inNumPeriod, 1); //mainline demand in vph, only required for the first segment
        inOnDemand_veh = CEHelper.int_1D(inNumPeriod, 1); //on ramp demand in vph
        inOffDemand_veh = CEHelper.int_1D(inNumPeriod, 1); //off ramp demand in vph
        inRRDemand_veh = CEHelper.int_1D(inNumPeriod, 1); //ramp to ramp demand in vph

        inMainlineFFS = CEHelper.int_1D(inNumPeriod, 70); //mainline free flow speed, mph
        inMainlineTruck = CEHelper.float_1D(inNumPeriod, 5); //mainline truck percentage
        inMainlineRV = CEHelper.float_1D(inNumPeriod, 0); //mainline RV percentage

        inOnFFS = CEHelper.int_1D(inNumPeriod, 45); //on ramp free flow speed, mph
        inOffFFS = CEHelper.int_1D(inNumPeriod, 45); //off ramp free flow speed, mph

        inUCAF = CEHelper.float_1D(inNumPeriod, 1); //user capacity adjustment factor
        inUOAF = CEHelper.float_1D(inNumPeriod, 1); //user origin demand adjustment factor
        inUDAF = CEHelper.float_1D(inNumPeriod, 1); //user densination demand adjustment factor
        inUSAF = CEHelper.float_1D(inNumPeriod, 1); //user free flow speed adjustment factor

        inRM_veh = CEHelper.int_1D(inNumPeriod, 2100); //ramp metering rate vph

        inMLCrossWeaveVolume = CEHelper.int_1D(inNumPeriod, 1);
    }

    /**
     * Create memory space for scenarios (preprocess, output, and over
     * saturated)
     */
    final void initMemory() {
        //create memory space for preprocess and output
        totalRampDensity = 0;
        inMainlineFHV = CEHelper.float_1D_normal(inNumPeriod, 1); //heavy vehicle adjustment factor
        inCrossCAF = CEHelper.float_1D_normal(inNumPeriod, 1); //heavy vehicle adjustment factor

        scenMainlineNumLanes = CEHelper.int_1D_normal(inNumPeriod, 0); //mainline number of lanes
        scenType = CEHelper.int_1D_normal(inNumPeriod, CEConst.SEG_TYPE_B); //process segment type

        scenMainlineDemand_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //mainline demand in vph, only required for the first segment
        scenOnDemand_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //on ramp demand in vph
        scenOffDemand_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //off ramp demand in vph
        scenRRDemand_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //ramp to ramp demand in vph
        scenRM_veh = CEHelper.float_1D_normal(inNumPeriod, 2100); //ramp metering rate

        scenVW = CEHelper.float_1D_normal(inNumPeriod, 0); //weave volume pc/h
        scenVNW = CEHelper.float_1D_normal(inNumPeriod, 0); //non-weave volume pc/h

        scenMainlineFFS = CEHelper.float_1D_normal(inNumPeriod, 0); //mainline free flow speed, mph
        scenOnFFS = CEHelper.float_1D_normal(inNumPeriod, 0); //on ramp free flow speed, mph
        scenOffFFS = CEHelper.float_1D_normal(inNumPeriod, 0); //off ramp free flow speed, mph

        scenMainlineCapacity_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //mainline capacity in vph
        scenOnCapacity_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //on ramp capacity in vph
        scenOffCapacity_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //off ramp capacity in vph

        scenVC = CEHelper.float_1D_normal(inNumPeriod, 0); //volume / capacity ratio
        scenMaxDC = 0; //max DC
        scenMaxVC = 0; //max VC

        scenSpeed = CEHelper.float_1D_normal(inNumPeriod, 0); //overall speed for each segment, mph
        scenAllDensity_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //overall density for each segment, in veh/mi/ln
        scenIADensity_pc = CEHelper.float_1D_normal(inNumPeriod, 0); //influence area density for ONR & OFR segment, in pc/mi/ln

        scenMainlineVolume_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //volume served in vph
        scenOnVolume_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //on ramp volume served in vph
        scenOffVolume_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //off ramp volume served in vph

        scenIsFrontClearingQueues = CEHelper.bool_1D_normal(inNumPeriod, false);

        //Extended Output Data
        scenOnDelay = CEHelper.float_1D_normal(inNumPeriod, 0); //on ramp delay
        scenSysDelay = CEHelper.float_1D_normal(inNumPeriod, 0); //system delay
        scenVMTD = CEHelper.float_1D_normal(inNumPeriod, 0); //VMTD
        scenVMTV = CEHelper.float_1D_normal(inNumPeriod, 0); //VMTV
        scenVHT = CEHelper.float_1D_normal(inNumPeriod, 0); //VHT
        scenVHD = CEHelper.float_1D_normal(inNumPeriod, 0); //VHD

        if (inUpSeg == null) {
            scenDenyQ = CEHelper.float_1D_normal(inNumPeriod, 0); //deny entry queue length
        }

        //create memory space for over sat
        ED = 0; //expected demand, vph
        KB = 0; //background density

        inOverMode = 0; //track how long (in periods) does a scenario in over sat calculation
        denyEntry = 0;

        WTT = 0; //wave travel time (in time step)
        capacityDropFactor = 1; //capacity drop factor (for two capacity calculation)

        ONRF = CEHelper.float_1D_normal(NUM_STEPS, 0); //on ramp flow, veh
        ONRQ = CEHelper.float_1D_normal(NUM_STEPS, 0); //on ramp queue

        ONRO = CEHelper.float_1D_normal(NUM_STEPS, 0); //on ramp output

        OFRF = CEHelper.float_1D_normal(NUM_STEPS, 0); //off ramp flow, veh

        UV = CEHelper.float_1D_normal(NUM_STEPS, 0); //unserved vehicles
        NV = CEHelper.float_1D_normal(NUM_STEPS, 0); //number of vehicles
        KQ = CEHelper.float_1D_normal(NUM_STEPS, 0); //density

        DEF = CEHelper.float_1D_normal(NUM_STEPS, 0); //deficit (for off ramp calculation)

        MI = CEHelper.float_1D_normal(NUM_STEPS, 0); //mainline input
        MO1 = CEHelper.float_1D_normal(NUM_STEPS, Float.MAX_VALUE); //mainline output 1
        MO2 = CEHelper.float_1D_normal(NUM_STEPS, Float.MAX_VALUE); //mainline output 2
        MO3 = CEHelper.float_1D_normal(NUM_STEPS, Float.MAX_VALUE); //mainline output 3
        MF = CEHelper.float_1D_normal(NUM_STEPS, 0); //mainline flow
        SF = CEHelper.float_1D_normal(NUM_STEPS, 0); //segment flow

        Q = CEHelper.float_1D_normal(inNumPeriod, 0); //mainline queue
        ONRQL = CEHelper.float_1D_normal(inNumPeriod, 0); //on ramp queue length
        ONRQ_End_veh = CEHelper.float_1D_normal(inNumPeriod, 0); //on ramp queue?????
        testOnRampDelay = CEHelper.float_1D_normal(inNumPeriod, 0); //new method for on ramp delay calculation
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PREPROCESS">
    /**
     * Estimate free flow speed if free flow speed is unknown
     *
     */
    void estimateFFS() {
        float f_LW;
        float f_LC;

        //Exhibit 11-8
        if (inLaneWidth_ft >= 12) {
            f_LW = 0;
        } else {
            if (inLaneWidth_ft >= 11) {
                f_LW = 1.9f;
            } else {
                f_LW = 6.6f;
            }
        }

        //Equation 11 - 1
        for (int period = 0; period < inNumPeriod; period++) {
            //Exhibit 11-9
            switch (inMainlineNumLanes.get(period)) {
                case 2:
                    f_LC = (float) Math.max(3.6 - inLateralClearance_ft * 0.6, 0);
                    break;
                case 3:
                    f_LC = (float) Math.max(2.4 - inLateralClearance_ft * 0.4, 0);
                    break;
                case 4:
                    f_LC = (float) Math.max(1.2 - inLateralClearance_ft * 0.2, 0);
                    break;
                default:
                    f_LC = (float) Math.max(0.6 - inLateralClearance_ft * 0.1, 0);
            }

            //TODO round for now
            inMainlineFFS.set(period, (int) Math.round(75.4 - f_LW - f_LC - 3.22 * Math.pow(totalRampDensity, 0.84)));
        }
    }

    /**
     * Create adjusted input data for one scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm atdm index
     */
    void scenPreprocess(int scen, int atdm) {
        resetMemory();
        calCrossCAF();
        calRM(scen, atdm);
        calLane(scen, atdm);
        calHeavyVehAdj();
        calTotalRampDensity();
        calDemand(scen, atdm);
        calFFS(scen, atdm);
        calType();
        calCapacity(scen, atdm);
        calDC();
        checkFrontClearQueue();
        resetOversaturated(true);
    }

    /**
     *
     */
    private void resetMemory() {
        scenMaxDC = 0;
        scenMaxVC = 0;
    }

    /**
     *
     * @param scen
     * @param atdm
     */
    private void calRM(int scen, int atdm) {
        if (inGPMLType == CEConst.SEG_TYPE_GP) {
            if (atdm < 0) {
                for (int period = 0; period < inNumPeriod; period++) {
                    scenRM_veh[period] = inRM_veh.get(period);
                }
            } else {
                for (int period = 0; period < inNumPeriod; period++) {
                    //ATDM RM override seed RM
                    scenRM_veh[period] = seed.getATDMRM(scen, atdm, inIndex, period);
                }
            }
        } else {
            for (int period = 0; period < inNumPeriod; period++) {
                scenRM_veh[period] = Float.MAX_VALUE;
            }
        }
    }

    /**
     *
     */
    private void checkFrontClearQueue() {
        scenIsFrontClearingQueues[0] = false;
        for (int period = 1; period < inNumPeriod; period++) {
            scenIsFrontClearingQueues[period] = scenMainlineNumLanes[period] > scenMainlineNumLanes[period - 1]
                    && scenMainlineCapacity_veh[period] > scenMainlineDemand_veh[period];
        }
    }

    /**
     * Calculate heavy vehicle adjustment factors
     *
     */
    private void calHeavyVehAdj() {
        //calculate for fHV
        for (int period = 0; period < inNumPeriod; period++) {
            inMainlineFHV[period] = (float) (1.0 / (1.0
                    + inMainlineTruck.get(period) * (inET - 1.0) / 100.0
                    + inMainlineRV.get(period) * (inER - 1.0) / 100.0));
        }
    }

    /**
     * Calculate total ramp density
     *
     */
    private void calTotalRampDensity() {
        //only first segment does calculation
        if (inUpSeg != null) {
            totalRampDensity = inUpSeg.totalRampDensity;
        } else {
            int totalRamps = 0;
            float totalLengthInFt = 0;

            GPMLSegment tempSeg = this;
            while (tempSeg != null) {
                if (tempSeg.inType == CEConst.SEG_TYPE_ONR || tempSeg.inType == CEConst.SEG_TYPE_OFR) {
                    totalRamps++;
                }
                if (tempSeg.inType == CEConst.SEG_TYPE_W) {
                    totalRamps += 2;
                }
                totalLengthInFt += tempSeg.inSegLength_ft;

                tempSeg = tempSeg.inDownSeg;
            }

            //ramp/mi
            totalRampDensity = totalRamps / (totalLengthInFt / 5280f);
        }
    }

    /**
     * Calculate adjusted demand based on adjustment factors for one scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     */
    private void calDemand(int scen, int atdm) {
        float value;
        for (int period = 0; period < inNumPeriod; period++) {

            if (inGPMLType == CEConst.SEG_TYPE_GP) {
                calDemand_Mainline(scen, atdm, period);
                if (seed.isManagedLaneUsed()) {
                    inParallelSeg.calDemand_Mainline(scen, atdm, period);
                }
            }

            if (inType == CEConst.SEG_TYPE_W) {
                value = inRRDemand_veh.get(period)
                        * Math.min(inUOAF.get(period) * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType),
                                inUDAF.get(period) * seed.getRLAndATDMDAF(scen, atdm, inIndex, period, inGPMLType));
                scenRRDemand_veh[period] = value;
                calWeaving(period);
            }

            if (inType == CEConst.SEG_TYPE_ACS) {
                //TODO: need discussion
                //float minODAF = Math.min(inUOAF.get(period) * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType),
                //        inUDAF.get(period) * seed.getRLAndATDMDAF(scen, atdm, inIndex, period, inGPMLType));

                scenRRDemand_veh[period] = 1;

                //recalculate ONR/OFR demand for access segments, TODO need discussion
//                if (inGPMLType == CEConst.SEG_TYPE_GP) {
//                    scenRRDemand_veh[period]
//                            = inParallelSeg.scenMainlineDemand_veh[period] / inParallelSeg.scenMainlineNumLanes[period];
//
//                    scenOnDemand_veh[period]
//                            = scenRRDemand_veh[period] + inOnDemand_veh.get(period) * minODAF;
//
//                    scenOffDemand_veh[period]
//                            = scenRRDemand_veh[period] + inOffDemand_veh.get(period) * minODAF;
//
//                    scenMainlineDemand_veh[period] += scenRRDemand_veh[period];
//                } else {
//                    scenRRDemand_veh[period]
//                            = (inParallelSeg.scenMainlineDemand_veh[period] - scenRRDemand_veh[period])
//                            / inParallelSeg.scenMainlineNumLanes[period];
//
//                    scenOnDemand_veh[period]
//                            = scenRRDemand_veh[period] + inOnDemand_veh.get(period) * minODAF;
//
//                    scenOffDemand_veh[period]
//                            = scenRRDemand_veh[period] + inOffDemand_veh.get(period) * minODAF;
//
//                    scenMainlineDemand_veh[period] += scenRRDemand_veh[period];
//                }
                if (scenOnDemand_veh[period] <= 0) {
                    MainWindow.printLog("Warning: Negtive ONR Demand " + scenOnDemand_veh[period] + " in segment " + (inIndex + 1) + " period " + (period + 1));
                }
                if (scenOffDemand_veh[period] <= 0) {
                    MainWindow.printLog("Warning: Negtive OFR Demand " + scenOffDemand_veh[period] + " in segment " + (inIndex + 1) + " period " + (period + 1));
                }
                if (scenRRDemand_veh[period] <= 0) {
                    MainWindow.printLog("Warning: Negtive RR Demand " + scenRRDemand_veh[period] + " in segment " + (inIndex + 1) + " period " + (period + 1));
                }

                calWeaving(period);
            }
        }
    }

    /**
     *
     * @param scen
     * @param atdm
     * @param period
     */
    private void calDemand_Mainline(int scen, int atdm, int period) {

        if (inUpSeg == null) {
            //assume first segment is always basic segment
            //adjust mainline demand
            float value = inMainlineDemand_veh.get(period)
                    * inUOAF.get(period)
                    * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType);
            scenMainlineDemand_veh[period] = value;
        } else {
            //adjust on ramp demand
            float value = inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                    ? inOnDemand_veh.get(period) * inUOAF.get(period) * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType) : 0;
            scenOnDemand_veh[period] = value;

            //adjust mainline demand
            scenMainlineDemand_veh[period] = funcDemandMainline(period);

            //adjust off ramp demand
            value = inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                    ? inOffDemand_veh.get(period) * inUDAF.get(period) * seed.getRLAndATDMDAF(scen, atdm, inIndex, period, inGPMLType) : 0;
            scenOffDemand_veh[period] = value;
        }
    }

    private float funcDemandMainline(int period) {
        return inUpSeg.scenMainlineDemand_veh[period] - inUpSeg.scenOffDemand_veh[period]
                + scenOnDemand_veh[period];
    }

    /**
     * Calculate weaving volume for one scenario one period
     *
     * @param period analysis period index (0 is the first period)
     */
    private void calWeaving(int period) {
        //calculate weave volume, non weave volume, and min rate of lane change
        float vFF_pc; //freeway to freeway demand pc/h
        float vFR_pc; //freeway to ramp demand pc/h
        float vRF_pc; //ramp to freeway demand pc/h
        float vRR_pc; //ramp to ramp demand pc/h

        vRR_pc = CEHelper.veh_to_pc(scenRRDemand_veh[period], inMainlineFHV[period]);//scenRRDemand_pc[period];
        vRF_pc = CEHelper.veh_to_pc(scenOnDemand_veh[period], inMainlineFHV[period])/*scenOnDemand_pc[period]*/ - vRR_pc;
        vFR_pc = CEHelper.veh_to_pc(scenOffDemand_veh[period], inMainlineFHV[period])/*scenOffDemand_pc[period]*/ - vRR_pc;
        vFF_pc = CEHelper.veh_to_pc(scenMainlineDemand_veh[period], inMainlineFHV[period])/*scenMainlineDemand_pc[period]*/
                - vRF_pc //CEHelper.veh_to_pc(scenOnDemand_veh[period], inMainlineFHV[period])/*scenOnDemand_pc[period]*/
                - vFR_pc;

        if (inOnSide == inOffSide) {
            scenVW[period] = vFR_pc + vRF_pc;
            scenVNW[period] = vFF_pc + vRR_pc;
        } else {
            scenVW[period] = vRR_pc;
            scenVNW[period] = vFF_pc + vFR_pc + vRF_pc;
        }
    }

    /**
     * Calculate adjusted free flow speed based on adjustment factors for one
     * scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     */
    private void calFFS(int scen, int atdm) {
        for (int period = 0; period < inNumPeriod; period++) {
            scenMainlineFFS[period]
                    = inMainlineFFS.get(period) * inUSAF.get(period) * seed.getRLAndATDMSAF(scen, atdm, inIndex, period, inGPMLType);
            scenOnFFS[period]
                    = inOnFFS.get(period) * inUSAF.get(period) * seed.getRLAndATDMSAF(scen, atdm, inIndex, period, inGPMLType);
            scenOffFFS[period]
                    = inOffFFS.get(period) * inUSAF.get(period) * seed.getRLAndATDMSAF(scen, atdm, inIndex, period, inGPMLType);
        }
    }

    /**
     * Calculate adjusted number of lanes based on adjustment factors for one
     * scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     */
    private void calLane(int scen, int atdm) {
        if (inGPMLType == CEConst.SEG_TYPE_GP) {
            //calculate number of lanes for GP segments (min one lane)
            for (int period = 0; period < inNumPeriod; period++) {
                scenMainlineNumLanes[period]
                        = Math.max(1, inMainlineNumLanes.get(period)
                                + seed.getRLAndATDMLAF(scen, atdm, inIndex, period, inGPMLType));
            }
            //calculate number of lanes for ML segments (min one lane)
            if (seed.isManagedLaneUsed()) {
                for (int period = 0; period < inNumPeriod; period++) {
                    inParallelSeg.scenMainlineNumLanes[period]
                            = Math.max(1, inParallelSeg.inMainlineNumLanes.get(period)
                                    + seed.getRLAndATDMLAF(scen, atdm, inIndex, period, inParallelSeg.inGPMLType));
                }
            }
        }
    }

    /**
     * Calculate processing segment type for one scenario
     *
     */
    private void calType() {
        for (int period = 0; period < inNumPeriod; period++) {
            try {
                switch (inType) {
                    case CEConst.SEG_TYPE_B:
                    case CEConst.SEG_TYPE_R:
                    case CEConst.SEG_TYPE_ACS:
                        scenType[period] = inType;
                        break;
                    case CEConst.SEG_TYPE_ONR:
                        scenType[period]
                                = inUpSeg.scenMainlineNumLanes[period] < scenMainlineNumLanes[period]
                                        ? CEConst.SEG_TYPE_ONR_B : inType;
                        break;
                    case CEConst.SEG_TYPE_OFR:
                        scenType[period]
                                = inDownSeg.scenMainlineNumLanes[period] > scenMainlineNumLanes[period]
                                        ? CEConst.SEG_TYPE_OFR_B : inType;
                        break;
                    case CEConst.SEG_TYPE_W:
                        scenType[period]
                                = inShort_ft > funcMaxShort(period)
                                        ? CEConst.SEG_TYPE_W_B : inType;
                        break;
                    default:
                        System.out.println("Warning: calType - Invalid Type");
                }
            } catch (Exception e) {
                System.out.println("calType: " + e.toString());
                scenType[period] = inType;
            }
        }
    }

    /**
     * Calculate maximum short length for weaving segment in ft
     *
     * @param period analysis period index (0 is the first period)
     * @return maximum short length for weaving segment in ft
     */
    private float funcMaxShort(int period) {
        return (float) (5728 * Math.pow(1
                + scenVW[period] / (scenVW[period] + scenVNW[period])/*scenVR[period]*/,
                1.6) - 1566 * inNWL);
    }

    /**
     * Calculate adjusted mainline and ramp capacities
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     */
    private void calCapacity(int scen, int atdm) {
        float result;
        for (int period = 0; period < inNumPeriod; period++) {
            switch (scenType[period]) {
                case CEConst.SEG_TYPE_B:
                case CEConst.SEG_TYPE_R:
                    result = funcBasicMainlineCapacity(scen, atdm, period);
                    scenMainlineCapacity_veh[period] = result;
                    break;
                case CEConst.SEG_TYPE_ONR:
                case CEConst.SEG_TYPE_ONR_B:
                    result = funcBasicMainlineCapacity(scen, atdm, period);
                    scenMainlineCapacity_veh[period] = result;
                    result = funcOnRampCapacity(period);
                    scenOnCapacity_veh[period] = result;
                    break;
                case CEConst.SEG_TYPE_OFR:
                case CEConst.SEG_TYPE_OFR_B:
                    result = funcBasicMainlineCapacity(scen, atdm, period);
                    scenMainlineCapacity_veh[period] = result;
                    result = funcOffRampCapacity(period);
                    scenOffCapacity_veh[period] = result;
                    break;
                case CEConst.SEG_TYPE_W:
                case CEConst.SEG_TYPE_ACS:
                    result = funcWeaveMainlineCapacity(scen, atdm, period);
                    scenMainlineCapacity_veh[period] = result;
                    result = funcOnRampCapacity(period);
                    scenOnCapacity_veh[period] = result;
                    result = funcOffRampCapacity(period);
                    scenOffCapacity_veh[period] = result;
                    break;
                case CEConst.SEG_TYPE_W_B:
                    result = funcBasicMainlineCapacity(scen, atdm, period);
                    scenMainlineCapacity_veh[period] = result;
                    result = funcOnRampCapacity(period);
                    scenOnCapacity_veh[period] = result;
                    result = funcOffRampCapacity(period);
                    scenOffCapacity_veh[period] = result;
                    break;
            }
        }
    }

    private void calCrossCAF() {
        //calculate for CrossCAF
        for (int period = 0; period < inNumPeriod; period++) {
            if (seed.isManagedLaneUsed() && inGPMLType == CEConst.SEG_TYPE_GP && inParallelSeg.inMLHasCrossWeave) {
                //Equation 13-24
                inCrossCAF[period] = 1 - (float) Math.max(-0.0897 + 0.0252 * Math.log(inParallelSeg.inMLCrossWeaveVolume.get(period))
                        - 0.00001453 * inSegLength_ft + 0.002967 * inParallelSeg.inMLCrossWeaveLCMin, 0);
            } else {
                inCrossCAF[period] = 1;
            }
        }
    }

    /**
     * Calculate adjusted mainline capacity (vph) for basic segment
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return adjusted mainline capacity (vph) for basic segment
     */
    private float funcBasicMainlineCapacity(int scen, int atdm, int period) {
        float CAF = inUCAF.get(period) * inCrossCAF[period] * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType);
        float result = 0;
        //TODO need discussion about what capacity for ML segments
        if (inGPMLType == CEConst.SEG_TYPE_GP) {
            // || (inType != CEConst.SEG_TYPE_B && inType != CEConst.SEG_TYPE_ONR_B && inType != CEConst.SEG_TYPE_OFR_B && inType != CEConst.SEG_TYPE_W_B)) {
            //GP basic mainline capacity
            result = CEHelper.pc_to_veh(funcIdealCapacity(scenMainlineFFS[period]) * scenMainlineNumLanes[period],
                    inMainlineFHV[period]);
        } else {
            //ML basic mainline capacity
            result = funcMLBasicMainlineCapacity(period);
        }
        return result * CAF; //vph
    }

    private float funcMLBasicMainlineCapacity(int period) {
        final float C_75;
        final float lamda_C = 10;
        switch (inMLSeparation) {
            case CEConst.ML_SEPARATION_BUFFER:
                if (scenMainlineNumLanes[period] == 1) {
                    C_75 = 1700;
                } else {
                    C_75 = 1850;
                }
                break;
            case CEConst.ML_SEPARATION_BARRIER:
                if (scenMainlineNumLanes[period] == 1) {
                    C_75 = 1750;
                } else {
                    C_75 = 2100;
                }
                break;
            default:
                C_75 = 1800;
        }
        float FFS_adj = scenMainlineFFS[period];
        return (C_75 - lamda_C * (75 - FFS_adj)) * scenMainlineNumLanes[period];
    }

    /**
     * Calculate adjusted on ramp capacity (vph)
     *
     * @param period analysis period index (0 is the first period)
     * @return adjusted on ramp capacity (vph)
     */
    private float funcOnRampCapacity(int period) {
        float result = CEHelper.pc_to_veh(funcRampCapacity(inOnNumLanes.get(period), scenOnFFS[period]),
                inMainlineFHV[period]);
        return result; //vph
    }

    /**
     * Calculate adjusted off ramp capacity (vph)
     *
     * @param period analysis period index (0 is the first period)
     * @return adjusted off ramp capacity (vph)
     */
    private float funcOffRampCapacity(int period) {
        float result = CEHelper.pc_to_veh(funcRampCapacity(inOffNumLanes.get(period), scenOffFFS[period]),
                inMainlineFHV[period]);
        return result; //vph
    }

    /**
     * Calculate adjusted mainline capacity (vph) for weaving segment
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return adjusted mainline capacity (vph) for weaving segment
     */
    private float funcWeaveMainlineCapacity(int scen, int atdm, int period) {
        //HCM Page 12-17
        float cIWL, cIW, cW;
        float cIFL = inGPMLType == CEConst.SEG_TYPE_GP
                ? funcIdealCapacity(scenMainlineFFS[period])
                : funcMLBasicMainlineCapacity(period) / scenMainlineNumLanes[period];
        float VR = scenVW[period] / (scenVW[period] + scenVNW[period]);
        //Weaving
        //Equation 12-5
        cIWL = (float) (cIFL - 438.2 * Math.pow(1 + VR/*scenVR[period]*/, 1.6)
                + 0.0765 * inShort_ft + 119.8 * inNWL);
        //Equation 12-7
        cIW = (inNWL <= 2 ? 2400 : 3500) / VR/*scenVR[period]*/ / inNWL;
        //Equation 12-6, 12-8
        cW = CEHelper.pc_to_veh(Math.min(cIWL, cIW) * scenMainlineNumLanes[period], inMainlineFHV[period]);
        return cW * inUCAF.get(period) * inCrossCAF[period] * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType); //vph
    }

    /**
     * Calculate ideal mainline capacity (pc/h/ln) based on mainline free flow
     * speed
     *
     * @param freeFlowSpeed mainline free flow speed (mph)
     * @return ideal mainline capacity (pc/h/ln)
     */
    private float funcIdealCapacity(float freeFlowSpeed) {
        //pc/h/ln
        return 2200 + 10 * (Math.min(70, freeFlowSpeed) - 50); //New Generic Equation for GP segments
        //return 2400 - Math.max(0, (70 - freeFlowSpeed)) * 10; //HCM2010 Equation
    }

    /**
     * Calculate ramp capacity (pc/h) based on ramp free flow speed and number
     * of ramp lanes
     *
     * @param numOfRampLanes number of ramp lanes
     * @param rampFreeFlowSpeed ramp free flow speed
     * @return ramp capacity (pc/h)
     */
    private float funcRampCapacity(int numOfRampLanes, float rampFreeFlowSpeed) {
        float result;
        //TODO need to check, different from HCM
        if (numOfRampLanes == 1) {
            if (rampFreeFlowSpeed > 50) {
                result = 2200;
            } else {
                if (rampFreeFlowSpeed > 40) {
                    result = 2100;
                } else {
                    if (rampFreeFlowSpeed > 30) {
                        result = 2000;
                    } else {
                        if (rampFreeFlowSpeed >= 20) {
                            result = 1900;
                        } else {
                            result = 1800;
                        }
                    }
                }
            }
        } else {
            if (rampFreeFlowSpeed > 50) {
                result = 4400;
            } else {
                if (rampFreeFlowSpeed > 40) {
                    result = 4200;
                } else {
                    if (rampFreeFlowSpeed > 30) {
                        result = 4000;
                    } else {
                        if (rampFreeFlowSpeed >= 20) {
                            result = 3800;
                        } else {
                            result = 3600;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Calculate demand over capacity ratio
     *
     */
    private void calDC() {
        float max = 0;
        float DC;
        for (int period = 0; period < inNumPeriod; period++) {
            DC = scenMainlineDemand_veh[period] / scenMainlineCapacity_veh[period];
            //scenDC[period] = DC;
            max = Math.max(max, DC);
        }
        scenMaxDC = max;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UNDERSAT FUNCTIONS - SPEED & DENSITY">
    /**
     * Run under saturated analysis for one scenario one period
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     */
    void runUndersaturated(int scen, int atdm, int period) {
        calSpeedAndDensity(scen, atdm, period, CEConst.STATUS_UNDER);
        //limits for speed
        scenSpeed[period] = Math.min(scenSpeed[period], scenMainlineFFS[period]);
        scenSpeed[period] = Math.min(scenSpeed[period], funcMaxSpeed(period));

        scenMainlineVolume_veh[period] = scenMainlineDemand_veh[period];
        scenOnVolume_veh[period]
                = inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                        ? scenOnDemand_veh[period] : 0;
        scenOffVolume_veh[period]
                = inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                        ? scenOffDemand_veh[period] : 0;

        scenVC[period] = scenMainlineVolume_veh[period] / scenMainlineCapacity_veh[period];
        if (scenVC[period] > scenMaxVC) {
            scenMaxVC = scenVC[period];
        }

        if (inUpSeg != null && inUpSeg.inType == CEConst.SEG_TYPE_R) {
            checkOverlap(period);
        }

        if (inUpSeg == null) {
            scenDenyQ[period] = 0f;
        }

        if (inOverMode != 0) {
            resetOversaturated(false);
        }
    }

    /**
     * Calculate speed and density using under saturated method
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @param status whether under saturated, over saturated, or background
     * density calculation
     */
    private void calSpeedAndDensity(int scen, int atdm, int period, int status) {
        switch (scenType[period]) {
            case CEConst.SEG_TYPE_B:
            case CEConst.SEG_TYPE_ONR_B:
            case CEConst.SEG_TYPE_OFR_B:
            case CEConst.SEG_TYPE_W_B:
                scenSpeed[period] = funcBasicSpeed(status, scen, atdm, period);
                scenAllDensity_veh[period] = funcBasicDensity(status, scen, atdm, period);
                break;
            case CEConst.SEG_TYPE_ONR:
                scenSpeed[period] = funcOnSpeed(status, period);
                scenIADensity_pc[period] = funcOnIADensity(status, period);
                scenAllDensity_veh[period] = funcOnAllDensity(status, period);
                break;
            case CEConst.SEG_TYPE_OFR:
                scenSpeed[period] = funcOffSpeed(status, period);
                scenIADensity_pc[period] = funcOffIADensity(status, period);
                scenAllDensity_veh[period] = funcOffAllDensity(status, period);
                break;
            case CEConst.SEG_TYPE_W:
            case CEConst.SEG_TYPE_ACS:
                scenSpeed[period] = funcWeaveSpeed(status, scen, atdm, period);
                scenAllDensity_veh[period] = funcWeaveDensity(status, scen, atdm, period);
                break;
            case CEConst.SEG_TYPE_R:
                scenSpeed[period] = funcOverlapSpeed(status, scen, atdm, period);
                scenAllDensity_veh[period] = funcOverlapDensity(status, scen, atdm, period);
                break;
        }
    }

    /**
     * Calculate maximum speed based on upstream speed
     *
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcMaxSpeed(int period) {
        //maximum achievable segment speed Equation 25-2 :  HCM Page 25-13
        if (inUpSeg == null) {
            return Float.POSITIVE_INFINITY;
        } else {
            return (float) (scenMainlineFFS[period]
                    - (scenMainlineFFS[period] - inUpSeg.scenSpeed[period])
                    * Math.exp(-0.00162 * (inUpSeg.inSegLength_ft + inSegLength_ft) / 2.0));
        }
    }

    /**
     * Calculate speed for basic segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcBasicSpeed(int status, int scen, int atdm, int period) {
        float vp; //demand flow rate
        //calculate vp
        switch (status) {
            case CEConst.STATUS_UNDER:
                //demandFlowRate (vp) Equation 11-2: HCM Page 11-13
                vp
                        = CEHelper.veh_to_pc(scenMainlineDemand_veh[period], inMainlineFHV[period]) / scenMainlineNumLanes[period];
                break;
            case CEConst.STATUS_BG:
                vp = CEHelper.veh_to_pc(ED, inMainlineFHV[period])
                        / scenMainlineNumLanes[period];
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                vp = CEHelper.veh_to_pc(scenMainlineVolume_veh[period], inMainlineFHV[period])
                        / scenMainlineNumLanes[period];
                break;
            default:
                vp = 0;
        }

        //calculat speed
        if (inGPMLType == CEConst.SEG_TYPE_GP) {
            //new 25-1 equation for GP segment
            //TODO use original FFS for now, may need to use adjusted FFS
            float CAF = inUCAF.get(period) * inCrossCAF[period] * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType);
            float BP_adj = (1000 + 40 * (75 - scenMainlineFFS[period])) * CAF * CAF;

            if (vp <= BP_adj) {
                return scenMainlineFFS[period];
            } else {
                float C_adj = CAF * (2200 + 10 * (Math.min(70, inMainlineFFS.get(period)) - 50));
                return scenMainlineFFS[period]
                        - (scenMainlineFFS[period] - C_adj / 45f)
                        / (C_adj - BP_adj) / (C_adj - BP_adj)
                        * (vp - BP_adj) * (vp - BP_adj);
            }
        } else {
            float CAF = inUCAF.get(period) * inCrossCAF[period] * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType);
            final float BP_75, lamda_BP, C_75, C_55_2, lamda_C2, C_1, K_nf_C, K_f_C;
            final float lamda_C = 10;
            switch (inMLSeparation) {
                case CEConst.ML_SEPARATION_BUFFER:
                    if (scenMainlineNumLanes[period] == 1) {
                        BP_75 = 600;
                        lamda_BP = 0;
                        C_75 = 1700;
                        C_55_2 = 1.4f;
                        lamda_C2 = 0;
                        C_1 = 0.0033f;
                        K_nf_C = 30;
                        K_f_C = 42;
                    } else {
                        BP_75 = 500;
                        lamda_BP = 10;
                        C_75 = 1850;
                        C_55_2 = 1.5f;
                        lamda_C2 = 0.02f;
                        C_1 = 0;
                        K_nf_C = 45;
                        K_f_C = 45;
                    }
                    break;
                case CEConst.ML_SEPARATION_BARRIER:
                    if (scenMainlineNumLanes[period] == 1) {
                        BP_75 = 800;
                        lamda_BP = 0;
                        C_75 = 1750;
                        C_55_2 = 1.4f;
                        lamda_C2 = 0;
                        C_1 = 0.004f;
                        K_nf_C = 35;
                        K_f_C = 35;
                    } else {
                        BP_75 = 700;
                        lamda_BP = 20;
                        C_75 = 2100;
                        C_55_2 = 1.3f;
                        lamda_C2 = 0.02f;
                        C_1 = 0;
                        K_nf_C = 45;
                        K_f_C = 45;
                    }
                    break;
                default:
                    BP_75 = 500;
                    lamda_BP = 0;
                    C_75 = 1800;
                    C_55_2 = 2.5f;
                    lamda_C2 = 0;
                    C_1 = 0;
                    K_nf_C = 30;
                    K_f_C = 45;
            }
            float FFS_adj = scenMainlineFFS[period];
            float BP_adj = (BP_75 + lamda_BP * (75 - FFS_adj)) * CAF * CAF;
            float C_adj = CAF * (C_75 - lamda_C * (75 - FFS_adj));
            float C_2 = C_55_2 + lamda_C2 * (FFS_adj - 55);
            float S_1 = FFS_adj - C_1 * Math.min(vp, BP_adj);
            if (vp <= BP_adj) {
                return S_1;
            } else {
                float S_2 = (float) ((S_1 - C_adj / K_nf_C) / Math.pow(C_adj - BP_adj, C_2) * Math.pow(vp - BP_adj, C_2));
                float S_3 = (float) ((C_adj / K_nf_C - C_adj / K_f_C) / Math.pow(C_adj - BP_adj, 2) * Math.pow(vp - BP_adj, 2));
                return S_1 - S_2 - (CEHelper.veh_to_pc(inParallelSeg.scenAllDensity_veh[period], inParallelSeg.inMainlineFHV[period]) <= 35 ? 0 : 1) * S_3;
            }
        }
    }

    /**
     * Calculate density for basic segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcBasicDensity(int status, int scen, int atdm, int period) {
        switch (status) {
            case CEConst.STATUS_UNDER:
                //return veh/mi/ln
                //need to calculate speed first
                return scenMainlineDemand_veh[period]
                        / scenMainlineNumLanes[period] / scenSpeed[period];
            case CEConst.STATUS_BG:
                //return veh/mi
                return ED / funcBasicSpeed(status, scen, atdm, period);
            case CEConst.STATUS_OVER_TO_UNDER:
                //return veh/mi/ln
                //need to calculate speed first
                return scenMainlineVolume_veh[period]
                        / scenMainlineNumLanes[period] / scenSpeed[period];
            default:
                return 0;
        }
    }

    /**
     * Calculate speed for on ramp segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcOnSpeed(int status, int period) {
        //TODO need to check condition
        //if (!(inUCAF.get(period) * inCrossCAF[period] * scenarios.get(scen).sCAF[period] != 1 && status == CEConst.STATUS_BG)) {
        //Exhibit 13-11 : HCM Page 13-20
        float v_12 = funcOnFlowRateInLanes1and2(status, period);
        float v_F = funcOnOff_vF(status, period);
        int N_O = Math.max(scenMainlineNumLanes[period] - 2, 0);
        float v_R12 = v_12 + funcOn_vR(status, period);
        float M_S = (float) (0.321 + 0.0039 * Math.exp(v_R12 / 1000.0) - 2e-6 * inAccDecLength_ft * scenOnFFS[period]);
        //TODO negative on ramp segment speed!!!
        float S_R = (float) Math.max(1, scenMainlineFFS[period] - (scenMainlineFFS[period] - 42) * M_S);
        if (N_O == 0) {
            return S_R;
        } else {
            float v_OA = (v_F - v_12) / N_O;
            float S_O;
            if (v_OA < 500) {
                S_O = scenMainlineFFS[period];
            } else {
                if (v_OA > 2300) {
                    S_O = (float) (scenMainlineFFS[period] - 6.53 - 0.006 * (v_OA - 2300));
                } else {
                    S_O = (float) (scenMainlineFFS[period] - 0.0036 * (v_OA - 500));
                }
            }
            float S = (v_R12 + v_OA * N_O) / ((v_R12 / S_R) + (v_OA * N_O / S_O));
            return S;
        }
        //} else {
        //    return funcBasicSpeed(status, period);
        //}
    }

    /**
     * Calculate on ramp flow rate in lane 1 and 2 using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return flow rate
     */
    private float funcOnFlowRateInLanes1and2(int status, int period) {
        float P_FM = funcOnRemainFactor(status, period);
        float v_F = funcOnOff_vF(status, period);
        float result;

        //Equation 13-2 : HCM Page 13-12
        result = v_F * P_FM;
        //HCM Page 13-16, check reasonableness of the lane distribution prediction
        //TODO check 5 or more lanes?
        switch (scenMainlineNumLanes[period]) {
            case 3: //6-Lane, 3 lanes each direction
                //Equation 13-14 : HCM Page 13-16
                float v_3 = v_F - result;
                //Equation 13-15 : HCM Page 13-16
                if (v_3 > 2700) {
                    result = v_F - 2700;
                }
                //Equation 13-16 : HCM Page 13-16
                if (v_3 > 1.5 * result / 2) {
                    result = v_F / 1.75f;
                }
                break;
            case 4: //8-Lane, 4 lanes each direction
                //Equation 13-14 : HCM Page 13-16
                float v_av34 = (v_F - result) / 2;
                //Equation 13-15 : HCM Page 13-16
                if (v_av34 > 2700) {
                    result = v_F - 5400;
                }
                //Equation 13-16 : HCM Page 13-16
                if (v_av34 > 1.5 * result / 2) {
                    result = v_F / 2.5f;
                }
                break;
        }
        //if number of lanes is 1, it will be multiplied by 2
        return result * (scenMainlineNumLanes[period] > 1 ? 1 : 2);
    }

    /**
     * Calculate on ramp remain factor using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return remain factor
     */
    private float funcOnRemainFactor(int status, int period) {
        //TODO incomplete
        float v_F = funcOnOff_vF(status, period);
        float v_R = funcOn_vR(status, period);
        float result;

        if (scenMainlineNumLanes[period] <= 4) {
            //Exhibit 13-6 : HCM Page 13-13 and Exhibit 13-16 : HCM Page 13-25
            switch (scenMainlineNumLanes[period]) {
                //TODO ignore 10 lane case
                case 3: //6-Lane, 3 lanes each direction
                    //TODO need a function to calculate P_FM, use Equation 13-3 now
                    result = (float) (0.5775 + 0.000028 * inAccDecLength_ft);
                    if (inOnSide == CEConst.RAMP_SIDE_LEFT) {
                        result *= 1.12;
                    }
                    break;
                case 4: //8-Lane, 4 lanes each direction
                    float S_FR = scenOnFFS[period];
                    if (v_F / S_FR <= 72) {
                        result = (float) (0.2178 - 0.0000125 * v_R + 0.0115 * inAccDecLength_ft / S_FR);
                    } else {
                        result = (float) (0.2178 - 0.0000125 * v_R);
                    }
                    if (inOnSide == CEConst.RAMP_SIDE_LEFT) {
                        result *= 1.20;
                    }
                    break;
                default:
                    result = 1.0f;
            }
        } else {
            //TODO temporary, need to change
            result = (float) (0.2178 * 4 / scenMainlineNumLanes[period]);
        }

        return result;
    }

    /**
     * Calculate v_F for on ramp or off ramp segment using under saturated
     * method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return v_F
     */
    private float funcOnOff_vF(int status, int period) {
        float result;
        switch (status) {
            //assume left segment exists and is basic or R
            case CEConst.STATUS_UNDER:
                result = CEHelper.veh_to_pc(inUpSeg.scenMainlineDemand_veh[period], inUpSeg.inMainlineFHV[period])/*inUpSeg.scenMainlineDemand_pc[period]*/;
                break;
            case CEConst.STATUS_BG:
                //TODO use left ED?
                result = CEHelper.veh_to_pc(inUpSeg.ED, inUpSeg.inMainlineFHV[period]);
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                //TODO use left SF?
                result = CEHelper.veh_to_pc(inUpSeg.scenMainlineVolume_veh[period], inUpSeg.inMainlineFHV[period]);
                break;
            default:
                result = 0;
        }
        return result;
    }

    /**
     * Calculate v_R for on ramp segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return v_R
     */
    private float funcOn_vR(int status, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
            case CEConst.STATUS_BG:
                result = CEHelper.veh_to_pc(scenOnDemand_veh[period], inMainlineFHV[period])/*scenOnDemand_pc[period]*/;
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                result = CEHelper.veh_to_pc(CEHelper.average(ONRF) * T, inMainlineFHV[period]);
                break;
            default:
                result = 0;
        }
        return result;
    }

    /**
     * Calculate overall density for on ramp segment using under saturated
     * method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return overall density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcOnAllDensity(int status, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
                if (scenMainlineNumLanes[period] > 2) {
                    //on ramp demand included
                    result = scenMainlineDemand_veh[period] / scenSpeed[period] / scenMainlineNumLanes[period];
                } else {
                    //Equation 13-21 : HCM Page 13-19
                    result = (float) (5.475 + 0.00734 * funcOn_vR(status, period)
                            + 0.0078 * funcOnFlowRateInLanes1and2(status, period) - 0.00627 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[period]);
                }
                //return veh/mi/ln
                return result;

            case CEConst.STATUS_BG:
                if (scenMainlineNumLanes[period] > 2) {
                    float speed = funcOnSpeed(status, period);
                    //TODO use V_fe??
                    result = (inUpSeg.ED + scenOnDemand_veh[period]) / speed;
                    //result = ED / speed;
                } else {
                    result = (float) (5.475 + 0.00734 * funcOn_vR(status, period)
                            + 0.0078 * funcOnFlowRateInLanes1and2(status, period) - 0.00627 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[period]) * scenMainlineNumLanes[period];
                }
                //return veh/mi
                return result;

            case CEConst.STATUS_OVER_TO_UNDER:
                if (scenMainlineNumLanes[period] > 2) {
                    result = scenMainlineVolume_veh[period] / scenSpeed[period] / scenMainlineNumLanes[period];
                } else {
                    result = (float) (5.475 + 0.00734 * funcOn_vR(status, period)
                            + 0.0078 * funcOnFlowRateInLanes1and2(status, period) - 0.00627 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[period]);
                }
                //return veh/mi/ln
                return result;

            default:
                return 0;
        }
    }

    /**
     * Calculate influenced area density for on ramp segment using under
     * saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return influenced area density (pc/mi/ln)
     */
    private float funcOnIADensity(int status, int period) {
        float result;
        //Equation 13-21 : HCM Page 13-19
        result = (float) (5.475 + 0.00734 * funcOn_vR(status, period)
                + 0.0078 * funcOnFlowRateInLanes1and2(status, period) - 0.00627 * inAccDecLength_ft);
        //return pc/mi/ln
        return result;
    }

    /**
     * Calculate speed for off ramp segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcOffSpeed(int status, int period) {
        //TODO need to check condition
        //if (!(inUCAF.get(period) * inCrossCAF[period] * scenarios.get(scen).sCAF[period] != 1 && status == CEConst.STATUS_BG)) {
        //Exhibit 13-11 : HCM Page 13-20
        float v_F = funcOnOff_vF(status, period);
        float P_FD = funcOffRemainFactor(status, period);
        float v_R = funcOff_vR(status, period);
        int N_O = Math.max(scenMainlineNumLanes[period] - 2, 0);
        float D_S = (float) (0.883 + 0.00009 * v_R - 0.013 * scenOffFFS[period]);
        float S_R = scenMainlineFFS[period] - (scenMainlineFFS[period] - 42) * D_S;
        if (N_O == 0) {
            return S_R;
        } else {
            float v_12 = funcOffFlowRateInLanes1and2(status, period);
            float v_OA = (v_F - v_12) / N_O;
            float S_O;
            if (v_OA < 1000) {
                S_O = (float) (1.097 * scenMainlineFFS[period]);
            } else {
                //TODO new equation??
                S_O = (float) (1.097 * scenMainlineFFS[period] - 0.0039 * ((v_F - v_R - (v_F - v_R) * P_FD) / N_O - 1000)); //VBA
                //S_O = (float) (1.097 * scenMainlineFFS[period] + 0.00009 * v_R - 0.013 * scenOffFFS[period]); //HCM
            }
            //TODO new equation??
            float S = v_F / ((v_R + (v_F - v_R) * P_FD) / S_R + (v_F - v_R - (v_F - v_R) * P_FD) / S_O); // VBA
            //float S = (v_12 + v_OA * N_O) / ((v_12 / S_R) + (v_OA * N_O / S_O)); //HCM
            return S;
        }
    }

    /**
     * Calculate overall density for off ramp segment using under saturated
     * method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return overall density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcOffAllDensity(int status, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
                if (scenMainlineNumLanes[period] > 2) {
                    result = scenMainlineDemand_veh[period] / scenSpeed[period] / scenMainlineNumLanes[period];
                } else {
                    //Equation 13-21 : HCM Page 13-19
                    result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, period) - 0.009 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[period]);
                }
                //return veh/mi/ln
                return result;

            case CEConst.STATUS_BG:
                if (scenMainlineNumLanes[period] > 2) {
                    float speed = funcOffSpeed(CEConst.STATUS_BG, period);
                    result = ED / speed;
                } else {
                    result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, period) - 0.009 * inAccDecLength_ft)
                            * scenMainlineNumLanes[period];
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[period]) * scenMainlineNumLanes[period];
                }
                //check left overlapping segment
                if (inUpSeg != null && inUpSeg.inType == CEConst.SEG_TYPE_R) {
                    inUpSeg.KB = Math.max(result, inUpSeg.KB);
                }
                //return veh/mi
                return result;

            case CEConst.STATUS_OVER_TO_UNDER:
                if (scenMainlineNumLanes[period] > 2) {
                    result = scenMainlineVolume_veh[period] / scenSpeed[period] / scenMainlineNumLanes[period];
                } else {
                    //Equation 13-21 : HCM Page 13-19
                    result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, period) - 0.009 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[period]);
                }
                //return veh/mi/ln
                return result;

            default:
                return 0;
        }
    }

    /**
     * Calculate influenced area density for off ramp segment using under
     * saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return influenced area density (pc/mi/ln)
     */
    private float funcOffIADensity(int status, int period) {
        float result;
        //Equation 13-21 : HCM Page 13-19
        result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, period) - 0.009 * inAccDecLength_ft);
        //return pc/mi/ln
        return result;
    }

    /**
     * Calculate off ramp flow rate in lane 1 and 2 using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return flow rate
     */
    private float funcOffFlowRateInLanes1and2(int status, int period) {
        float v_F = funcOnOff_vF(status, period);
        float P_FD = funcOffRemainFactor(status, period);
        float v_R = funcOff_vR(status, period);

        //Equation 13-8 : HCM Page 13-14
        float v_12 = v_R + (v_F - v_R) * P_FD;

        //check reasonableness of the lane distribution prediction
        //HCM Page 13-16
        switch (scenMainlineNumLanes[period]) {
            case 3: //6-Lane, 3 lanes each direction
                //Equation 13-14 : HCM Page 13-16
                float v_3 = v_F - v_12;
                //Equation 13-15 : HCM Page 13-16
                if (v_3 > 2700) {
                    v_12 = v_F - 2700;
                }
                //Equation 13-16 : HCM Page 13-16
                if (v_3 > 1.5 * v_12 / 2) {
                    v_12 = v_F / 1.75f;
                }
                break;
            case 4: //8-Lane, 4 lanes each direction
                //Equation 13-14 : HCM Page 13-16
                float v_av34 = (v_F - v_12) / 2;
                //Equation 13-15 : HCM Page 13-16
                if (v_av34 > 2700) {
                    v_12 = v_F - 5400;
                }
                //Equation 13-16 : HCM Page 13-16
                if (v_av34 > 1.5 * v_12 / 2) {
                    v_12 = v_F / 2.5f;
                }
                break;
        }
        return v_12 * (scenMainlineNumLanes[period] > 1 ? 1 : 2); //Only used for off ramp segment density, speed only depends on V_R
    }

    /**
     * Calculate off ramp remain factor using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return remain factor
     */
    private float funcOffRemainFactor(int status, int period) {
        //TODO incomplete
        float v_F = funcOnOff_vF(status, period);
        float v_R = funcOff_vR(status, period);
        float result;
        if (scenMainlineNumLanes[period] <= 4) {
            //Exhibit 13-7 : HCM Page 13-14 and Exhibit 13-16 : HCM Page 13-25
            switch (scenMainlineNumLanes[period]) {
                //TODO ignore 10 lane case
                case 3: //6-Lane, 3 lanes each direction
                    //TODO need a function to calculate P_FD, use Equation 13-9 now
                    result = (float) (0.760 - 0.000025 * v_F - 0.000046 * v_R);
                    if (inOffSide == CEConst.RAMP_SIDE_LEFT) {
                        result *= 1.05;
                    }
                    break;
                case 4: //8-Lane, 4 lanes each direction
                    result = 0.436f;
                    if (inOffSide == CEConst.RAMP_SIDE_LEFT) {
                        result *= 1.10;
                    }
                    break;
                default:
                    result = 1.0f;
            }
        } else {
            //TODO temporary, need to change
            result = (float) (0.436 * 4 / scenMainlineNumLanes[period]);
        }
        return result;
    }

    /**
     * Calculate v_R for off ramp segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param period analysis period index (0 is the first period)
     * @return v_R
     */
    private float funcOff_vR(int status, int period) {
        float result;
        switch (status) {
            //assume left segment exists and is basic or R
            case CEConst.STATUS_UNDER:
            case CEConst.STATUS_BG:
                //TODO use left ED?
                result = CEHelper.veh_to_pc(scenOffDemand_veh[period], inMainlineFHV[period])/*scenOffDemand_pc[period]*/;
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                //TODO use left SF?
                result = CEHelper.veh_to_pc(CEHelper.average(OFRF) * T, inMainlineFHV[period]);
                break;
            default:
                result = 0;
        }
        return result;
    }

    /**
     * Calculate speed for weaving segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcWeaveSpeed(int status, int scen, int atdm, int period) {
        if (!(inUCAF.get(period) * inCrossCAF[period] * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType) != 1 && status == CEConst.STATUS_BG)) {
            float minRateOfLaneChange;
            float vRR_pc = CEHelper.veh_to_pc(scenRRDemand_veh[period], inMainlineFHV[period]);//scenRRDemand_pc[period]; //ramp to ramp demand pc/h
            float vRF_pc = CEHelper.veh_to_pc(scenOnDemand_veh[period], inMainlineFHV[period])/*scenOnDemand_pc[period]*/ - vRR_pc; //ramp to freeway demand pc/h
            float vFR_pc = CEHelper.veh_to_pc(scenOffDemand_veh[period], inMainlineFHV[period])/*scenOffDemand_pc[period]*/ - vRR_pc; //freeway to ramp demand pc/h

            if (inOnSide == inOffSide) {
                //one-sided weaving HCM: Page 12-11
                minRateOfLaneChange = (inLCRF * vRF_pc)
                        + (inLCFR * vFR_pc);
            } else {
                //two-sided weaving HCM: Page 12-12
                minRateOfLaneChange = inLCRR * vRR_pc;
            }

            //HCM Page 12-19 to 12-21
            float LC_W, I_NW, LC_NW, LC_NW1, LC_NW2, LC_ALL;
            //Equation 12 - 10
            LC_W = (float) (minRateOfLaneChange + 0.39 * Math.pow(inShort_ft - 300, 0.5)
                    * Math.pow(scenMainlineNumLanes[period], 2) * Math.pow(1 + totalRampDensity / 2.0, 0.8)); //assume ID = TRD/2
            //Equation 12 - 11
            I_NW = inShort_ft * (totalRampDensity / 2.0f) * scenVNW[period] / 10000; //assume ID = TRD/2

            if (I_NW <= 1300) {
                //Equation 12 - 12
                LC_NW = (float) (0.206 * scenVNW[period] + 0.542 * inShort_ft - 192.6 * scenMainlineNumLanes[period]);
            } else {
                if (I_NW >= 1950) {
                    //Equation 12 - 13
                    LC_NW = (float) (2135 + 0.223 * (scenVNW[period] - 2000));
                } else {
                    LC_NW1 = (float) (0.206 * scenVNW[period] + 0.542 * inShort_ft - 192.6 * scenMainlineNumLanes[period]);
                    LC_NW2 = (float) (2135 + 0.223 * (scenVNW[period] - 2000));
                    if (LC_NW1 >= LC_NW2) {
                        LC_NW = LC_NW2;
                    } else {
                        //Equation 12 - 14
                        LC_NW = LC_NW1 + (LC_NW2 - LC_NW1) * (I_NW - 1300) / 650;
                    }
                }
            }
            //Equation 12 - 16
            LC_ALL = LC_W + LC_NW;

            //HCM Page 12-21 to 12-22
            float W, S_W, S_NW, S;
            //Equation 12-18
            W = (float) (0.226 * Math.pow(LC_ALL / inShort_ft, 0.789));
            S_W = 15 + (scenMainlineFFS[period] - 15) / (1 + W);
            //Equation 12-19
            S_NW = (float) (scenMainlineFFS[period] - 0.0072 * minRateOfLaneChange
                    - 0.0048 * (scenVW[period] + scenVNW[period]) / scenMainlineNumLanes[period]);
            //Equation 12-20
            S = (scenVW[period] + scenVNW[period]) / (scenVW[period] / S_W + scenVNW[period] / S_NW);
            return S;
        } else {
            return funcBasicSpeed(status, scen, atdm, period);
        }
    }

    /**
     * Calculate density for weaving segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcWeaveDensity(int status, int scen, int atdm, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
                result = scenMainlineDemand_veh[period] / scenMainlineNumLanes[period] / scenSpeed[period];
                //return veh/mi/ln
                return result;

            case CEConst.STATUS_BG:
                //TODO not use ED?
                result = scenMainlineDemand_veh[period] / funcWeaveSpeed(status, scen, atdm, period);
                //return veh/mi
                return result;

            case CEConst.STATUS_OVER_TO_UNDER:
                result = scenMainlineVolume_veh[period] / scenMainlineNumLanes[period] / scenSpeed[period];
                //return veh/mi/ln
                return result;

            default:
                return 0;
        }
    }

    /**
     * Calculate speed for overlapping segment
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcOverlapSpeed(int status, int scen, int atdm, int period) {
        //TODO need to check which way
        switch (status) {
            case CEConst.STATUS_UNDER:
                return inUpSeg == null ? 0 : inUpSeg.scenSpeed[period];
            default:
                return funcBasicSpeed(status, scen, atdm, period);
        }
    }

    /**
     * Calculate density for overlapping segment
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @return density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcOverlapDensity(int status, int scen, int atdm, int period) {
        //TODO need to check which way
        switch (status) {
            case CEConst.STATUS_UNDER:
                return inUpSeg == null ? 0 : inUpSeg.scenAllDensity_veh[period];
            default:
                return funcBasicDensity(status, scen, atdm, period);
        }
    }

    /**
     * Check adjacent upstream overlapping segment speed and density
     *
     * @param period analysis period index (0 is the first period)
     */
    void checkOverlap(int period) {
        if (inUpSeg != null && inUpSeg.inType == CEConst.SEG_TYPE_R) {
            inUpSeg.scenSpeed[period]
                    = Math.min(inUpSeg.scenSpeed[period], scenSpeed[period]);
            inUpSeg.scenAllDensity_veh[period]
                    = Math.max(inUpSeg.scenAllDensity_veh[period], scenAllDensity_veh[period]);
//            inUpSeg.scenAllDensity_pc[period]
//                    = Math.max(inUpSeg.scenAllDensity_pc[period], scenAllDensity_pc[period]);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OVERSAT DATA">
    /**
     * Track how long (in periods) does a scenario in over sat calculation
     */
    transient int inOverMode;
    /**
     * Expected demand, vph
     */
    transient float ED;
    /**
     * Background density veh/mi
     */
    transient float KB;
    /**
     * Wave travel time (in time step)
     */
    transient int WTT;
    /**
     * Capacity drop factor (for two capacity calculation)
     */
    transient float capacityDropFactor;
    /**
     * On ramp flow, veh
     */
    transient float[] ONRF;
    /**
     * On ramp output, veh
     */
    transient float[] ONRO;
    /**
     * Off ramp flow, veh
     */
    transient float[] OFRF;
    /**
     * On ramp queue, veh
     */
    transient float[] ONRQ;
    /**
     * New method for on ramp delay calculation, veh
     */
    transient float[] testOnRampDelay;
    /**
     * Mainline queue length in ft (at end of each period)
     */
    transient float[] Q;
    /**
     * On ramp queue length in ft (at end of each period)
     */
    transient float[] ONRQL;
    /**
     * On ramp queue (at end of each period), veh
     */
    transient float[] ONRQ_End_veh;
    /**
     * Unserved vehicles, veh
     */
    transient float[] UV;
    /**
     * Number of vehicles, veh
     */
    transient float[] NV;
    /**
     * Queue density, veh/mi
     */
    transient float[] KQ;
    /**
     * Deficit (for off ramp calculation), veh
     */
    transient float[] DEF;
    /**
     * Mainline input, veh
     */
    transient float[] MI;
    /**
     * Mainline output 1 (based capacity and previous MO2 and MO3), veh
     */
    transient float[] MO1;
    /**
     * Mainline output 2 (based on queue density), veh
     */
    transient float[] MO2;
    /**
     * Mainline output 3 (based on front clear queue and wave travel speed), veh
     */
    transient float[] MO3;
    /**
     * Mainline flow, veh
     */
    transient float[] MF;
    /**
     * Segment flow, veh
     */
    transient float[] SF;
    /**
     * Facility-wide jam density, veh/mi/ln
     */
    float KJ;
    /**
     * Ideal density at capacity, veh/mi/ln
     */
    float KC = 45;
    /**
     * Number of vehicles deny entry
     */
    transient float denyEntry;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OVERSAT FUNCTIONS">
    /**
     * Run analysis for over saturated cases
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     */
    void runOversaturated(int scen, int atdm, int period, int step) {
        try {
            //ASSUME FIRST AND LAST SEGMENT MUST BE BASIC
            //initialization----once per analysis period-------step 1 & 2-------------------------------
            if (step == 0) {
                //Equation 25-3 HCM Page 25-21
                ED = funcED(period);
                KB = funcKB(scen, atdm, period);

                if (inDownSeg != null) {
                    //Equation 25-10&11 HCM Page 25-24
                    inDownSeg.WTT = inDownSeg.funcWTT(period);
                }
                testOnRampDelay[period] = 0f;
            }

            //adjust capacity (two capacity)
            if (inDownSeg == null) {
                if (UV[(step + NUM_STEPS - 1) % NUM_STEPS] > CEConst.ZERO) {
                    capacityDropFactor = 1 - inCapacityDropPercentage;
                } else {
                    capacityDropFactor = 1f;
                }
            } else {
                if (UV[(step + NUM_STEPS - 1) % NUM_STEPS] > CEConst.ZERO && inDownSeg.UV[(step + NUM_STEPS - 1) % NUM_STEPS] <= CEConst.ZERO) {
                    inDownSeg.capacityDropFactor = 1 - inCapacityDropPercentage;
                } else {
                    inDownSeg.capacityDropFactor = 1f;
                }
            }

            //for every time step---------------step 5 to 25----------(order is different from HCM)--------
            //off-ramp calculation--------------------------------------------------------------------
            if (inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                DEF[step] = funcDEF(period, step);
                OFRF[step] = funcOFRF(period, step);
            }

            //mainline flow calculation-------------------------------------------------------------------
            //Equation 25-5 HCM Page 25-22
            MI[step] = funcMI(period, step);
            if (inDownSeg != null && (inDownSeg.inType == CEConst.SEG_TYPE_ONR || inDownSeg.inType == CEConst.SEG_TYPE_W || inDownSeg.inType == CEConst.SEG_TYPE_ACS)) {
                //on-ramp calculation
                inDownSeg.ONRF[step] = inDownSeg.funcONRF(period, step);
            }

            //Equation 25-6 HCM Page 25-22
            MO1[step] = funcMO1(period, step);
            //Equation 25-8 HCM Page 25-23
            MO2[step] = funcMO2(period, step);
            //Equation 25-12 HCM Page 25-24
            MO3[step] = funcMO3(period, step);
            //Equation 25-13 HCM Page 25-25
            MF[step] = funcMF(period, step);

            //segment flow calculation----------------------------------------------------------------
            SF[step] = funcSF(step);
            NV[step] = funcNV(period, step);
            UV[step] = funcUV(step);

            //TODO test new on ramp delay method
            if (inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                testOnRampDelay[period] += ONRQ[step]; //Each step is 0.25 min //ONRQ[step] / ONRF[step];
            }

            //summary period result, once per analysis period
            if (step == NUM_STEPS - 1) {
                //segment and ramp performance measures---------------------------------------------------
                //Equation 25-30 HCM Page 25-29
                //calculate mainline queue length, ft
                //TODO new method required for KQ calculation /*(KQ[NUM_STEPS - 1] + KQ[NUM_STEPS - 2]) / 2*/
                Q[period] = Math.max(UV[NUM_STEPS - 1] / (KQ[NUM_STEPS - 1] - KB) * 5280, 0);

                checkMainlineQueueLength(period);

                //calculate deny entry queue length, ft /*(KQ[NUM_STEPS - 1] + KQ[NUM_STEPS - 2]) / 2*/
                if (inUpSeg == null) {
                    scenDenyQ[period] = Math.max(denyEntry
                            / (KQ[NUM_STEPS - 1] - KB) * 5280 - inSegLength_ft, 0);
                }

                //mainline volume served, CEHelper.average segment flow vph
                scenMainlineVolume_veh[period] = CEHelper.average(SF) * T;

                //volume/capacity ratio
                scenVC[period] = scenMainlineVolume_veh[period] / scenMainlineCapacity_veh[period];
                if (scenVC[period] > scenMaxVC) {
                    scenMaxVC = scenVC[period];
                }

                switch (inType) {
                    case CEConst.SEG_TYPE_ONR:
                        scenOnVolume_veh[period] = CEHelper.average(ONRF) * T; //CEHelper.average on ramp flow vph
                        //Equation 25-31 HCM Page 25-29
                        ONRQ_End_veh[period] = ONRQ[NUM_STEPS - 1];
                        ONRQL[period] = funcONRQL(period);
                        //TODO temporary
                        if (testOnRampDelay[period] > CEConst.ZERO) {
                            testOnRampDelay[period]
                                    = testOnRampDelay[period] / 4;// / CEHelper.sum(ONRQ); // weighted CEHelper.average
                        } else {
                            testOnRampDelay[period] = 0f;
                        }
                        break;
                    case CEConst.SEG_TYPE_OFR:
                        scenOffVolume_veh[period] = CEHelper.average(OFRF) * T; //CEHelper.average off ramp flow vph
                        break;
                    case CEConst.SEG_TYPE_W:
                    case CEConst.SEG_TYPE_ACS:
                        scenOnVolume_veh[period] = CEHelper.average(ONRF) * T; //CEHelper.average on ramp flow vph
                        scenOffVolume_veh[period] = CEHelper.average(OFRF) * T; //CEHelper.average off ramp flow vph
                        //Equation 25-31 HCM Page 25-29
                        ONRQ_End_veh[period] = ONRQ[NUM_STEPS - 1];
                        ONRQL[period] = funcONRQL(period);
                        //TODO temporary
                        if (testOnRampDelay[period] > CEConst.ZERO) {
                            testOnRampDelay[period]
                                    = testOnRampDelay[period] / 4 / CEHelper.sum(ONRQ); // weighted CEHelper.average
                        } else {
                            testOnRampDelay[period] = 0f;
                        }
                        break;
                    default:

                }

                //TODO check whether the condition is correct
                if (inType != CEConst.SEG_TYPE_R) {
                    if (CEHelper.sum(UV) > CEConst.ZERO) {
                        //calculate speed and density use over saturated method
                        float NV_average = CEHelper.average(NV);
                        //TODO need to divide by num of lanes? modified Equation 25-26 to 25-29, HCM Page 25-28
                        scenSpeed[period] = scenMainlineVolume_veh[period] / NV_average * inSegLength_ft / 5280f;
                        scenAllDensity_veh[period] = NV_average / (inSegLength_ft / 5280f) / scenMainlineNumLanes[period];
                        scenIADensity_pc[period]
                                = CEHelper.veh_to_pc(scenAllDensity_veh[period], inMainlineFHV[period]);//scenAllDensity_pc[period];
                    } else {
                        //calculate speed and density use under saturated method with SF_average_per_hour
                        calSpeedAndDensity(scen, atdm, period, CEConst.STATUS_OVER_TO_UNDER);
                    }
                } else {
                    scenSpeed[period] = inUpSeg == null ? 0 : inUpSeg.scenSpeed[period];
                    scenAllDensity_veh[period] = inUpSeg == null ? 0 : inUpSeg.scenAllDensity_veh[period];
                }

                //limits for speed
                scenSpeed[period] = Math.min(scenSpeed[period], scenMainlineFFS[period]);
                scenSpeed[period] = Math.min(scenSpeed[period], funcMaxSpeed(period));
                scenSpeed[period] = (float) Math.max(scenSpeed[period], 1);
                checkOverlap(period);

                inOverMode++;
            }
        } catch (Exception e) {
            System.out.println("runOversaturated " + e.toString());
            e.printStackTrace();
        }
    }

    //helper funtions for oversatuarated----------------------------------------------------
    /**
     * Reset over saturated data for a particular scenario
     *
     * @param isFirstTime whether it is first time enter over saturated method
     */
    private void resetOversaturated(boolean isFirstTime) {

        inOverMode = 0; //track how long (in periods) does a scenario in over sat calculation
        denyEntry = 0;

        capacityDropFactor = 1f; //capacity drop factor (for two capacity calculation)

        Arrays.fill(ONRF, 0); //on ramp flow, veh

        Arrays.fill(ONRO, 0); //on ramp output
        Arrays.fill(ONRQ, 0); //on ramp queue?????

        Arrays.fill(OFRF, 0); //off ramp flow, veh

        Arrays.fill(UV, 0); //unserved vehicles
        Arrays.fill(NV, 0); //number of vehicles
        Arrays.fill(KQ, 0); //density

        Arrays.fill(DEF, 0); //deficit (for off ramp calculation)

        Arrays.fill(MI, 0); //mainline input
        Arrays.fill(MO1, Float.MAX_VALUE); //mainline output 1
        Arrays.fill(MO2, Float.MAX_VALUE); //mainline output 2
        Arrays.fill(MO3, Float.MAX_VALUE); //mainline output 3
        Arrays.fill(MF, 0); //mainline flow
        Arrays.fill(SF, 0); //segment flow

        if (isFirstTime) {
            Arrays.fill(ONRQ_End_veh, 0); //on ramp queue
            Arrays.fill(Q, 0); //mainline queue
            Arrays.fill(ONRQL, 0); //on ramp queue length
            Arrays.fill(testOnRampDelay, 0); //new method for on ramp delay calculation
        }
    }

    //helper functions called once per analysis period, require 2 parameters-----------------------------
    /**
     * Calculate ED for a particular period in a particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @return ED
     */
    private float funcED(int period) {
        float result;
        //Equation 25-3 HCM Page 25-21 vph?
        if (inUpSeg == null) {
            //assume first segment is a basic segment
            result = Math.min(scenMainlineCapacity_veh[period] * capacityDropFactor, scenMainlineDemand_veh[period]);
        } else {
            float temp = inUpSeg.ED;
            if (inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                temp += scenOnDemand_veh[period];
            }
            if (inUpSeg.inType == CEConst.SEG_TYPE_OFR || inUpSeg.inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                temp -= inUpSeg.scenOffDemand_veh[period];
            }
            result = Math.min(scenMainlineCapacity_veh[period] * capacityDropFactor, temp);
        }
        result = Math.max(result, 1);
        return result;
    }

    /**
     * Calculate KB for a particular period in a particular scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first analysis period)
     * @return KB
     */
    private float funcKB(int scen, int atdm, int period) {
        float density;
        switch (scenType[period]) {
            case CEConst.SEG_TYPE_B:
            case CEConst.SEG_TYPE_ONR_B:
            case CEConst.SEG_TYPE_OFR_B:
            case CEConst.SEG_TYPE_W_B:
                density = funcBasicDensity(CEConst.STATUS_BG, scen, atdm, period);
                break;
            case CEConst.SEG_TYPE_ONR:
                density = funcOnAllDensity(CEConst.STATUS_BG, period);
                break;
            case CEConst.SEG_TYPE_OFR:
                density = funcOffAllDensity(CEConst.STATUS_BG, period);
                break;
            case CEConst.SEG_TYPE_W:
            case CEConst.SEG_TYPE_ACS:
                density = funcWeaveDensity(CEConst.STATUS_BG, scen, atdm, period);
                break;
            default: //Const.SEG_TYPE_R :
                //assume first segment will never be R
                density = inUpSeg.KB;
        }
        density = (float) Math.min(
                CEHelper.pc_to_veh(KC, inMainlineFHV[period]) * scenMainlineNumLanes[period],
                density);
        return density;
    }

    /**
     * Calculate WTT for a particular period in a particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @return WTT
     */
    private int funcWTT(int period) {
        //Equation 25-10&11 HCM Page 25-24
        float WS = scenMainlineCapacity_veh[period] * capacityDropFactor / (scenMainlineNumLanes[period] * (KJ - KC));
        return (int) (T * inSegLength_ft / 5280f / WS);
    }

    /**
     * Check whether mainline queue length is less or equal to segment length.
     * If not, add extra to upstream segments
     *
     * @param period analysis period index (0 is the first analysis period)
     */
    private void checkMainlineQueueLength(int period) {
        if (Q[period] > inSegLength_ft) {
            if (inUpSeg != null) {
                inUpSeg.Q[period] += Q[period] - inSegLength_ft;
                Q[period] = inSegLength_ft;
                inUpSeg.checkMainlineQueueLength(period);
            } else {
                scenDenyQ[period] += Q[period] - inSegLength_ft;
                Q[period] = inSegLength_ft;
            }
        }
    }

    //helper functions called every time step, require 3 parameters----------------------------------------
    //mainline helper functions----------------------------------------------------------------------
    /**
     * Calculate MI for a particular step in a particular period in a particular
     * scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MI
     */
    private float funcMI(int period, int step) {
        float result;
        //Equation 25-5 HCM Page 25-22
        if (inUpSeg == null) {
            //assume first segment is a basic segment
            if (step == 0) {
                result = funcDummyMF(period, step) + UV[NUM_STEPS - 1];
            } else {
                result = funcDummyMF(period, step) + UV[step - 1];
            }
        } else {
            if (step == 0) {
                result = inUpSeg.MF[step] + ONRF[step]
                        - OFRF[step] + UV[NUM_STEPS - 1];
            } else {
                result = inUpSeg.MF[step] + ONRF[step]
                        - OFRF[step] + UV[step - 1];
            }
        }
        return result;
    }

    /**
     * Calculate Dummy MF (for first segment only) for a particular step in a
     * particular period in a particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return Dummy MF
     */
    private float funcDummyMF(int period, int step) {
        float expect = scenMainlineDemand_veh[period] / T;

        //limit by capacity (MO1)
        float result = Math.min(scenMainlineDemand_veh[period] / T + denyEntry,
                scenMainlineCapacity_veh[period] * capacityDropFactor / T);

        //additional KQ calculation for first segment
        if (inUpSeg == null) {
            KQ[step] = funcKQ(period, step);
        }

        float result2 = Float.MAX_VALUE;
        //limit by MO2
        if (step == 0) {
            if (UV[NUM_STEPS - 1] > CEConst.ZERO) {
                if (inOverMode > 1) {
                    result2 = KQ[step] * inSegLength_ft / 5280f + SF[NUM_STEPS - 1]
                            - NV[NUM_STEPS - 1];
                }
            }
        } else {
            if (UV[step - 1] > CEConst.ZERO) {
                result2 = KQ[step] * inSegLength_ft / 5280f + SF[step - 1]
                        - NV[step - 1];
            }
        }

        result2 = Math.max(0, result2);

        result = Math.min(result, result2);

        denyEntry += expect - result;
        denyEntry = Math.max(0, denyEntry);

        return result;
    }

    /**
     * Calculate MO1 for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MO1
     */
    private float funcMO1(int period, int step) {
        if (inDownSeg != null) {
            //Equation 25-6 HCM Page 25-22
            float result = inDownSeg.scenMainlineCapacity_veh[period] * inDownSeg.capacityDropFactor / T
                    - inDownSeg.ONRF[step];

            if (step == 0) {
                if (inOverMode > 1) {
                    //if (MO2[NUM_STEPS - 1] > CEConst.ZERO) {
                    result = Math.min(result, MO2[NUM_STEPS - 1]);
                    //}
                    //if (MO3[NUM_STEPS - 1] > CEConst.ZERO) {
                    result = Math.min(result, MO3[NUM_STEPS - 1]);
                    //}
                }
            } else {
                //TODO copy from VBA, over estimate if ignore
                //if (MO2[step - 1] > CEConst.ZERO) {
                result = Math.min(result, MO2[step - 1]);
                //}
                //if (MO3[step - 1] > CEConst.ZERO) {
                result = Math.min(result, MO3[step - 1]);
                //}
            } //2, 3
            return Math.max(result, 0);
        } else {
            return Float.MAX_VALUE;
        }
    }

    /**
     * Calculate KQ for a particular step in a particular period in a particular
     * scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return KQ
     */
    private float funcKQ(int period, int step) {
        float result;
        float testSF = SF[(step + NUM_STEPS - 1) % NUM_STEPS];
        //Equation 25-7 HCM Page 25-23
        if (step == 0) {
            if (inOverMode > 1) {
                result = KJ - (KJ - KC) * testSF / (scenMainlineCapacity_veh[period] * capacityDropFactor / T);
            } else {
                result = KC;
            }
        } else {
            result = KJ - (KJ - KC) * testSF / (scenMainlineCapacity_veh[period] * capacityDropFactor / T);
        }

        //return density for all lanes
        return result * scenMainlineNumLanes[period];
    }

    /**
     * Calculate MO2 for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MO2
     */
    private float funcMO2(int period, int step) {
        //TODO according to VBA code, only need when previous right UV > 0
        if (inDownSeg != null) {
            float result;
            inDownSeg.KQ[step] = inDownSeg.funcKQ(period, step); //queue density
            //Equation 25-8 HCM Page 25-23
            if (step == 0) {
                if (inDownSeg.UV[NUM_STEPS - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }

                if (inOverMode > 1) {
                    result = inDownSeg.KQ[step] * inDownSeg.inSegLength_ft / 5280f + inDownSeg.SF[NUM_STEPS - 1]
                            - inDownSeg.ONRF[step] - inDownSeg.NV[NUM_STEPS - 1];
                } else {
                    return Float.MAX_VALUE;
                }
            } else {
                if (inDownSeg.UV[step - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }

                result = inDownSeg.KQ[step] * inDownSeg.inSegLength_ft / 5280f + inDownSeg.SF[step - 1]
                        - inDownSeg.ONRF[step] - inDownSeg.NV[step - 1];
            }

            return Math.max(result, 0);
        } else {
            return Float.MAX_VALUE;
        }
    }

    /**
     * Calculate MO3 for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MO3
     */
    private float funcMO3(int period, int step) {
        if (inDownSeg != null && isFrontClearQueue(period, step)) {
            if (step == 0) {
                if (inDownSeg.UV[NUM_STEPS - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }
            } else {
                if (inDownSeg.UV[step - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }
            }

            int _WTT = inDownSeg.WTT;
            //Equation 25-12 HCM Page 25-24
            float result;
            if (step < _WTT) {
                if (inOverMode > _WTT && _WTT < NUM_STEPS && period > 0) {
                    //assume WTT < numOfSteps
                    result = inDownSeg.scenMainlineCapacity_veh[period - 1] * inDownSeg.capacityDropFactor / T; //4
                    if (inDownSeg != null) {
                        result = Math.min(inDownSeg.MO1[step + NUM_STEPS - _WTT], result); //1
                        result = Math.min(inDownSeg.MO2[step + NUM_STEPS - _WTT] + inDownSeg.OFRF[step + NUM_STEPS - _WTT], result); //2
                        result = Math.min(inDownSeg.MO3[step + NUM_STEPS - _WTT] + inDownSeg.OFRF[step + NUM_STEPS - _WTT], result); //3
                        if (inDownSeg.inDownSeg != null) {
                            result = Math.min(inDownSeg.inDownSeg.scenMainlineCapacity_veh[period - 1] * inDownSeg.inDownSeg.capacityDropFactor / T
                                    + inDownSeg.OFRF[step + NUM_STEPS - _WTT], result); //5
                        }
                    }
                } else {
                    return Float.MAX_VALUE;
                }
            } else {
                result = inDownSeg.scenMainlineCapacity_veh[period] * inDownSeg.capacityDropFactor / T; //4
                if (inDownSeg != null) {
                    result = Math.min(inDownSeg.MO1[step - _WTT], result); //1
                    result = Math.min(inDownSeg.MO2[step - _WTT] + inDownSeg.OFRF[step - _WTT], result); //2
                    result = Math.min(inDownSeg.MO3[step - _WTT] + inDownSeg.OFRF[step - _WTT], result); //3
                    if (inDownSeg.inDownSeg != null) {
                        result = Math.min(inDownSeg.inDownSeg.scenMainlineCapacity_veh[period] * inDownSeg.inDownSeg.capacityDropFactor / T
                                + inDownSeg.OFRF[step - _WTT], result); //5
                    }
                }
            }
            result -= inDownSeg.ONRF[step];
            return result;
        } else {
            return Float.MAX_VALUE;
        }
    }

    /**
     * Check whether it is front clear queue for a particular step in a
     * particular period in a particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return whether it is front clear queue
     */
    private boolean isFrontClearQueue(int period, int step) {
        if (inDownSeg == null) {
            return false;
        } else {
            GPMLSegment segment = inDownSeg;
            while (!segment.scenIsFrontClearingQueues[period]
                    && segment.UV[(step + NUM_STEPS - 1) % NUM_STEPS] > 0
                    && segment.inDownSeg != null) {
                segment = segment.inDownSeg;
            }
            return segment.scenIsFrontClearingQueues[period];
        }
    }

    /**
     * Calculate MF for a particular step in a particular period in a particular
     * scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MF
     */
    private float funcMF(int period, int step) {
        //Equation 25-13 HCM Page 25-25
        float result = MI[step];

        //if (MO1[step] > CEConst.ZERO) {
        result = Math.min(MO1[step], result);
        //}
        //if (MO2[step] > CEConst.ZERO) {
        result = Math.min(MO2[step], result);
        //}

        //if (MO3[step] > CEConst.ZERO) {
        result = Math.min(MO3[step], result);
        //}

        result = Math.min(scenMainlineCapacity_veh[period] * capacityDropFactor / T, result);

        if (inDownSeg != null) {
            result = Math.min(inDownSeg.scenMainlineCapacity_veh[period] * inDownSeg.capacityDropFactor / T, result);
        }

        return result;
    }

    //on ramp helper functions----------------------------------------------------------------------
    /**
     * Calculate ONRI for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return ONRI
     */
    private float funcONRI(int period, int step) {
        float ONRD = 0;
        if (inType == CEConst.SEG_TYPE_ONR) {
            ONRD = scenOnDemand_veh[period] / T;
        }
        if (inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
            ONRD = scenOnDemand_veh[period] / T;
        }
        //Equation 25-14 HCM Page 25-25
        if (step == 0) {
            if (inOverMode > 1) {
                return ONRD + ONRQ[NUM_STEPS - 1];
            } else {
                return ONRD;
            }
        } else {
            return ONRD + ONRQ[step - 1];
        }
    }

    /**
     * Calculate ONRO for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return ONRO
     */
    private float funcONRO(int period, int step) {
        //Equation 25-15 HCM Page 25-26
        float result = Math.min(scenRM_veh[period], scenOnCapacity_veh[period]) / T;//1 and 2

        float onr2 = scenMainlineCapacity_veh[period] * capacityDropFactor / T; //3
        //assume left segment exists since ONRF is only called by left segment
        if (step == 0) {
            if (inOverMode > 1) {
                float _ONRF = ONRF[NUM_STEPS - 1]
                        * scenOnDemand_veh[period]
                        / scenOnDemand_veh[period - 1];

                //TODO only need when MO2 > 0 and MO3 > 0 ??
                if (/*MO2[NUM_STEPS - 1] > CEConst.ZERO && */MO2[NUM_STEPS - 1] < Float.MAX_VALUE) {
                    onr2 = Math.min(MF[NUM_STEPS - 1] + _ONRF, onr2);
                }
                if (/*MO3[NUM_STEPS - 1] > CEConst.ZERO &&*/inUpSeg.MO3[NUM_STEPS - 1] < Float.MAX_VALUE) {
                    onr2 = Math.min(inUpSeg.MO3[NUM_STEPS - 1] + _ONRF, onr2);
                }
            }
        } else {
            //TODO unclear, need to check
            if (/*MO2[step - 1] > CEConst.ZERO && */MO2[step - 1] < Float.MAX_VALUE) {
                onr2 = Math.min(MF[step - 1] + ONRF[step - 1], onr2);
            }
            if (/*MO3[step - 1] > CEConst.ZERO && */inUpSeg.MO3[step - 1] < Float.MAX_VALUE) {
                onr2 = Math.min(inUpSeg.MO3[step - 1] + ONRF[step - 1], onr2);
            }
        }
        onr2 = Math.max(onr2 - inUpSeg.MI[step], onr2 / 2.0f / scenMainlineNumLanes[period]);

        result = Math.min(result, onr2);

        result = Math.max(result, 0);
        return result;
    }

    /**
     * Calculate ONRF for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return ONRF
     */
    private float funcONRF(int period, int step) {
        if (inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
            float ONRI = funcONRI(period, step);
            //float ONRO = funcONRO( period, step);
            ONRO[step] = funcONRO(period, step);
            if (ONRO[step] >= ONRI) {
                ONRQ[step] = 0f;
                //Equation 25-16 HCM Page 25-26
                return ONRI;
            } else {
                //update ONRQ
                //Equation 25-18 HCM Page 25-26
                ONRQ[step] = ONRI - ONRO[step];
                //Equation 25-17 HCM Page 25-26
                return ONRO[step];
            }
        } else {
            return 0;
        }
    }

    //off ramp helper functions---------------------------------------------------------------------
    /**
     * Calculate DEF for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return DEF
     */
    private float funcDEF(int period, int step) {
        //Equation 25-19 HCM Page 25-27
        if (inUpSeg == null) {
            //assume first segment basic, and will never have deficit
            return 0;
        } else {
            //TODO different from HCM, need to check
            float result;
            if (step == 0) {
                if (inOverMode > 1) {
                    //since DEF is calculated first, MF and ONRF are from previous analysis period
                    result = scenMainlineDemand_veh[period - 1] / T * NUM_STEPS
                            - CEHelper.sum(inUpSeg.MF) - CEHelper.sum(ONRF);
                } else {
                    result = 0;
                }
            } else {
                float actualFlow = inUpSeg.MF[step - 1] + ONRF[step - 1];
                result = DEF[step - 1] - actualFlow;
            }
            //unnecessary, only check whether > 0
            result = Math.max(result, 0);
            return result;
        }
    }

    /**
     * Calculate OFRF for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return OFRF
     */
    private float funcOFRF(int period, int step) {
        if (inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
            //inUpSeg.MF and ONRF were calculated before OFRF
            if (period == 0) {
                float OFRD_p = scenOffDemand_veh[period]; //no convert since it is ratio
                return (inUpSeg.MF[step] + ONRF[step]) * OFRD_p / scenMainlineDemand_veh[period];
            } else {
                //no convert since it is ratio
                float OFRD_p = scenOffDemand_veh[period];
                float OFRD_p_1 = scenOffDemand_veh[period - 1];

                if (DEF[step] > CEConst.ZERO) {
                    if (inUpSeg.MF[step] + ONRF[step] <= DEF[step]) {
                        //Equation 25-20 HCM Page 25-27
                        return (inUpSeg.MF[step] + ONRF[step]) * OFRD_p_1 / scenMainlineDemand_veh[period - 1];
                    } else {
                        //Equation 25-21 HCM Page 25-27
                        return DEF[step] * OFRD_p_1 / scenMainlineDemand_veh[period - 1]
                                + (inUpSeg.MF[step] + ONRF[step]
                                - DEF[step]) * OFRD_p / scenMainlineDemand_veh[period];
                    }
                } else {
                    //no deficit
                    //Equation 25-22 HCM Page 25-27
                    return (inUpSeg.MF[step] + ONRF[step]) * OFRD_p / scenMainlineDemand_veh[period];
                }
            }
        } else {
            return 0;
        }
    }

    //segment helper functions----------------------------------------
    /**
     * Calculate SF for a particular step in a particular period in a particular
     * scenario
     *
     * @param step time step index (0 is the first step)
     * @return SF
     */
    private float funcSF(int step) {
        //Equation 25-23 HCM Page 25-28
        return MF[step] + OFRF[step];
    }

    /**
     * Calculate NV for a particular step in a particular period in a particular
     * scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return NV
     */
    private float funcNV(int period, int step) {
        //Equation 25-24 HCM Page 25-28
        float result;
        if (step == 0) {
            //Equation 25-4 HCM Page 25-2
            if (inOverMode > 1) {
                //NV 0
                result = KB * inSegLength_ft / 5280f + UV[NUM_STEPS - 1];
            } else {
                result = KB * inSegLength_ft / 5280f;
            }
        } else {
            result = NV[step - 1];
        }

        if (inUpSeg != null) {
            result += inUpSeg.MF[step];
        } else {
            result += funcDummyMF(period, step);
        }
        result += ONRF[step] - MF[step] - OFRF[step];

        return result;
    }

    /**
     * Calculate UV for a particular step in a particular period in a particular
     * scenario
     *
     * @param step time step index (0 is the first step)
     * @return UV
     */
    private float funcUV(int step) {
        //Equation 25-25 HCM Page 25-28
        float value = Math.max(NV[step] - KB * inSegLength_ft / 5280f, 0);
        return value;
    }

    /**
     * Calculate ONRQL for a particular step in a particular period in a
     * particular scenario
     *
     * @param period analysis period index (0 is the first analysis period)
     * @return ONRQL
     */
    private float funcONRQL(int period) {
        float RM = scenRM_veh[period] / T;
        float ONRC = scenOnCapacity_veh[period] / T;
        float result = ONRQ[NUM_STEPS - 1]
                / (KJ - Math.min(RM, ONRO[NUM_STEPS - 1]) * (KJ - KC) / ONRC)
                * 5280;

        return CEHelper.veh_to_pc(result, inMainlineFHV[period]);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EXTEND RESULTS">
    /**
     * Extend output results based on output speed and density
     *
     */
    void calExtendedResults() {
        for (int period = 0; period < inNumPeriod; period++) {
            float scenActualTime
                    = inSegLength_ft / 5280f / scenSpeed[period] * 60;
            float scenFFSTime
                    = inSegLength_ft / 5280f / (inMainlineFFS.get(period) * inUSAF.get(period)) * 60;
            float scenMainlineDelay = scenActualTime - scenFFSTime;

            //TODO use new test method for on ramp delay
            scenOnDelay[period]
                    = inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                            ? testOnRampDelay[period]
                            : 0;
            if (inType != CEConst.SEG_TYPE_ACS) {
                //calculate system delay for normal segments
                scenSysDelay[period] = scenMainlineDelay + scenOnDelay[period];
            } else {
                //calculate system delay for access segments
                if (inGPMLType == CEConst.SEG_TYPE_GP) {
                    scenSysDelay[period] = scenMainlineDelay;
                } else {
                    scenSysDelay[period] = scenMainlineDelay + inParallelSeg.scenOnDelay[period];
                    inParallelSeg.scenSysDelay[period] += scenOnDelay[period];
                }
            }
            //TODO VMTD, VMTV only consider mainline???
            scenVMTD[period]
                    = scenMainlineDemand_veh[period] * inSegLength_ft / 5280f / 4f;
            scenVMTV[period]
                    = scenMainlineVolume_veh[period] * inSegLength_ft / 5280f / 4f;
            scenVHT[period]
                    = scenActualTime * scenMainlineVolume_veh[period] / 240;
            scenVHD[period]
                    = scenMainlineDelay * scenMainlineVolume_veh[period] / 240
                    + scenOnDelay[period] * scenOnVolume_veh[period] / 240;
//            scenTTI[period]
//                    = scenActualTime[period] / scenFFSTime[period];
        }
    }

    /**
     * Calculate density based level of service
     *
     * @param period analysis period index (0 is the first period)
     * @return density based level of service
     */
    String funcDensityLOS(int period) {
        if (scenType[period] == CEConst.SEG_TYPE_B || scenType[period] == CEConst.SEG_TYPE_ONR_B
                || scenType[period] == CEConst.SEG_TYPE_OFR_B || scenType[period] == CEConst.SEG_TYPE_W_B
                || scenType[period] == CEConst.SEG_TYPE_R) {
            float density_pc = CEHelper.veh_to_pc(scenAllDensity_veh[period], inMainlineFHV[period]);//scenAllDensity_pc[period];
            //Exhibit 11-5: HCM Page 11-7
            if (density_pc <= 11.5) {
                return "A";
            } else {
                if (density_pc <= 18.5) {
                    return "B";
                } else {
                    if (density_pc <= 26.5) {
                        return "C";
                    } else {
                        if (density_pc <= 35.5) {
                            return "D";
                        } else {
                            if (density_pc <= 45.5) {
                                return "E";
                            } else {
                                return "F";
                            }
                        }
                    }
                }
            }
        } else {
            float density_pc
                    = (scenType[period] == CEConst.SEG_TYPE_W
                            ? CEHelper.veh_to_pc(scenAllDensity_veh[period], inMainlineFHV[period])
                            : scenIADensity_pc[period]);
            if (density_pc <= 10.5) {
                return "A";
            } else {
                if (density_pc <= 20.5) {
                    return "B";
                } else {
                    if (density_pc <= 28.5) {
                        return "C";
                    } else {
                        if (density_pc <= 35.5) {
                            return "D";
                        } else {
                            if (density_pc <= 43.5) {
                                return "E";
                            } else {
                                return "F";
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Calculate demand based level of service
     *
     * @param period analysis period index (0 is the first period)
     * @return demand based level of service
     */
    String funcDemandLOS(int period) {
        return scenMainlineDemand_veh[period] / scenMainlineCapacity_veh[period]/*scenDC[period]*/ > 1 ? "F" : "";
    }

    /**
     * Getter for scenario DC
     *
     * @param period analysis period index (0 is the first period)
     * @return DC
     */
    float getScenDC(int period) {
        return scenMainlineDemand_veh[period] / scenMainlineCapacity_veh[period];
    }

    /**
     * Getter for scenario ActualTime
     *
     * @param period analysis period index (0 is the first period)
     * @return ActualTime
     */
    float getScenActualTime(int period) {
        return inSegLength_ft / 5280f / scenSpeed[period] * 60;
    }

    /**
     * Getter for scenario FFSTime
     *
     * @param period analysis period index (0 is the first period)
     * @return FFSTime
     */
    float getScenFFSTime(int period) {
        return inSegLength_ft / 5280f / (inMainlineFFS.get(period) * inUSAF.get(period)) * 60;
    }

    /**
     * Getter for scenario MainlineDelay
     *
     * @param period analysis period index (0 is the first period)
     * @return MainlineDelay
     */
    float getScenMainlineDelay(int period) {
        return getScenActualTime(period) - getScenFFSTime(period);
    }

    /**
     * Getter for scenario TTI
     *
     * @param period analysis period index (0 is the first period)
     * @return TTI
     */
    float getScenTTI(int period) {
        return getScenActualTime(period) / getScenFFSTime(period);
    }

    /**
     * Getter for scenario AllDensity_pc
     *
     * @param period analysis period index (0 is the first period)
     * @return AllDensity_pc
     */
    float getScenAllDensity_pc(int period) {
        return CEHelper.veh_to_pc(scenAllDensity_veh[period], inMainlineFHV[period]);
    }
    // </editor-fold>
}
