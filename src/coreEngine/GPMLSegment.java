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
     * Number of threads
     */
    final static int MAX_NUM_THREAD = 4;
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
    int inMLCrossWeaveLCMin = 0;

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
    transient float[] totalRampDensity;

    /**
     * Heavy vehicle adjustment factor
     */
    transient float[][] inMainlineFHV;
    /**
     * Mainline number of lanes
     */
    transient int[][] scenMainlineNumLanes;
    /**
     * Process segment type
     */
    transient int[][] scenType;
    /**
     * Mainline demand in vph, only required for the first segment
     */
    transient float[][] scenMainlineDemand_veh;
    /**
     * On ramp demand in vph
     */
    transient float[][] scenOnDemand_veh;
    /**
     * Off ramp demand in vph
     */
    transient float[][] scenOffDemand_veh;
    /**
     * Ramp to ramp demand in vph
     */
    transient float[][] scenRRDemand_veh;
    /**
     * Ramp Metering Rate
     */
    transient float[][] scenRM_veh;
    /**
     * Weave volume pc/h
     */
    transient float[][] scenVW;
    /**
     * Non-weave volume pc/h
     */
    transient float[][] scenVNW;
    /**
     * Mainline free flow speed, mph
     */
    transient float[][] scenMainlineFFS;
    /**
     * On ramp free flow speed, mph
     */
    transient float[][] scenOnFFS;
    /**
     * Off ramp free flow speed, mph
     */
    transient float[][] scenOffFFS;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OUTPUT DATA">
    /**
     * Mainline capacity in vph
     */
    transient float[][] scenMainlineCapacity_veh;
    /**
     * On ramp capacity in vph
     */
    transient float[][] scenOnCapacity_veh;
    /**
     * Off ramp capacity in vph
     */
    transient float[][] scenOffCapacity_veh;
    /**
     * Demand / capacity ratio
     */
    //transient float[][] scenDC;
    /**
     * Volume / capacity ratio
     */
    transient float[][] scenVC;
    /**
     * Max DC for this segment over all periods in one scenario
     */
    transient float[] scenMaxDC;
    /**
     * Max VC for this segment over all periods in one scenario
     */
    transient float[] scenMaxVC;
    /**
     * Overall speed for each segment, mph
     */
    transient float[][] scenSpeed;
    /**
     * Overall density for each segment, in veh/mi/ln
     */
    transient float[][] scenAllDensity_veh;
    /**
     * Overall density for each segment, in pc/mi/ln
     */
    //transient float[][] scenAllDensity_pc;
    /**
     * Influence area density for ONR or OFR segment, in pc/mi/ln
     */
    transient float[][] scenIADensity_pc;
    /**
     * Density based LOS
     */
    //transient String[][] scenDensityLOS;
    /**
     * Demand based LOS
     */
    //transient String[][] scenDemandLOS;
    /**
     * Mainline volume served in vph
     */
    transient float[][] scenMainlineVolume_veh;
    /**
     * On ramp volume served in vph
     */
    transient float[][] scenOnVolume_veh;
    /**
     * Off ramp volume served in vph
     */
    transient float[][] scenOffVolume_veh;
    /**
     * Capacity drop, two capacity
     */
    //transient float[] scenCapaDrop;
    /**
     * Whether is front clearing queue
     */
    transient boolean[][] scenIsFrontClearingQueues;

    //Extended Output Data
    /**
     * Actual travel time
     */
    //transient float[][] scenActualTime;
    /**
     * Free flow speed travel time
     */
    //transient float[][] scenFFSTime;
    /**
     * Mainline delay
     */
    //transient float[][] scenMainlineDelay;
    /**
     * On ramp delay
     */
    transient float[][] scenOnDelay;
    /**
     * System delay
     */
    transient float[][] scenSysDelay;
    /**
     * VMTD veh-miles / interval
     */
    transient float[][] scenVMTD;
    /**
     * VMTV veh-miles / interval
     */
    transient float[][] scenVMTV;
    /**
     * VHT travel / interval (hrs)
     */
    transient float[][] scenVHT;
    /**
     * VHD delay / interval (hrs)
     */
    transient float[][] scenVHD;
    /**
     * Travel time index
     */
    //transient float[][] scenTTI;
    /**
     * Deny entry queue length
     */
    transient float[][] scenDenyQ;
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

        inMLCrossWeaveVolume = CEHelper.int_1D(inNumPeriod, 0);
    }

    /**
     * Create memory space for scenarios (preprocess, output, and over
     * saturated)
     */
    final void initThreadMemory() {
        //create memory space for preprocess and output
        totalRampDensity = CEHelper.float_1D_normal(MAX_NUM_THREAD, 0);
        inMainlineFHV = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 1); //heavy vehicle adjustment factor

        scenMainlineNumLanes = CEHelper.int_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //mainline number of lanes
        scenType = CEHelper.int_2D_normal(MAX_NUM_THREAD, inNumPeriod, CEConst.SEG_TYPE_B); //process segment type

        scenMainlineDemand_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //mainline demand in vph, only required for the first segment
        scenOnDemand_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //on ramp demand in vph
        scenOffDemand_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //off ramp demand in vph
        scenRRDemand_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //ramp to ramp demand in vph
        scenRM_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 2100); //ramp metering rate

        scenVW = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //weave volume pc/h
        scenVNW = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //non-weave volume pc/h

        scenMainlineFFS = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //mainline free flow speed, mph
        scenOnFFS = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //on ramp free flow speed, mph
        scenOffFFS = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //off ramp free flow speed, mph

        scenMainlineCapacity_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //mainline capacity in vph
        scenOnCapacity_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //on ramp capacity in vph
        scenOffCapacity_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //off ramp capacity in vph

        scenVC = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //volume / capacity ratio
        scenMaxDC = CEHelper.float_1D_normal(MAX_NUM_THREAD, 0); //max DC
        scenMaxVC = CEHelper.float_1D_normal(MAX_NUM_THREAD, 0); //max VC

        scenSpeed = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //overall speed for each segment, mph
        scenAllDensity_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //overall density for each segment, in veh/mi/ln
        scenIADensity_pc = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //influence area density for ONR & OFR segment, in pc/mi/ln

        scenMainlineVolume_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //volume served in vph
        scenOnVolume_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //on ramp volume served in vph
        scenOffVolume_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //off ramp volume served in vph

        scenIsFrontClearingQueues = CEHelper.bool_2D_normal(MAX_NUM_THREAD, inNumPeriod, false);

        //Extended Output Data
        scenOnDelay = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //on ramp delay
        scenSysDelay = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //system delay
        scenVMTD = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //VMTD
        scenVMTV = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //VMTV
        scenVHT = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //VHT
        scenVHD = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //VHD

        if (inUpSeg == null) {
            scenDenyQ = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //deny entry queue length
        }

        //create memory space for over sat
        ED = CEHelper.float_1D_normal(MAX_NUM_THREAD, 0); //expected demand, vph
        KB = CEHelper.float_1D_normal(MAX_NUM_THREAD, 0); //background density

        inOverMode = CEHelper.int_1D_normal(MAX_NUM_THREAD, 0); //track how long (in periods) does a scenario in over sat calculation
        denyEntry = CEHelper.float_1D_normal(MAX_NUM_THREAD, 0);

        WTT = CEHelper.int_1D_normal(MAX_NUM_THREAD, 0); //wave travel time (in time step)
        capacityDropFactor = CEHelper.float_1D_normal(MAX_NUM_THREAD, 1); //capacity drop factor (for two capacity calculation)

        ONRF = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //on ramp flow, veh
        ONRQ = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //on ramp queue

        ONRO = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //on ramp output

        OFRF = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //off ramp flow, veh

        UV = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //unserved vehicles
        NV = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //number of vehicles
        KQ = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //density

        DEF = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //deficit (for off ramp calculation)

        MI = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //mainline input
        MO1 = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, Float.MAX_VALUE); //mainline output 1
        MO2 = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, Float.MAX_VALUE); //mainline output 2
        MO3 = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, Float.MAX_VALUE); //mainline output 3
        MF = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //mainline flow
        SF = CEHelper.float_2D_normal(MAX_NUM_THREAD, NUM_STEPS, 0); //segment flow

        Q = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //mainline queue
        ONRQL = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //on ramp queue length
        ONRQ_End_veh = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //on ramp queue?????
        testOnRampDelay = CEHelper.float_2D_normal(MAX_NUM_THREAD, inNumPeriod, 0); //new method for on ramp delay calculation
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PREPROCESS">
    /**
     * Estimate free flow speed if free flow speed is unknown
     *
     * @param thread
     */
    void estimateFFS(int thread) {
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
            inMainlineFFS.set(period, (int) Math.round(75.4 - f_LW - f_LC - 3.22 * Math.pow(totalRampDensity[thread], 0.84)));
        }
    }

    /**
     * Create adjusted input data for one scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm atdm index
     * @param thread thread index to which this scenario will be assigned to
     */
    void scenPreprocess(int scen, int atdm, int thread) {
        resetThreadMemory(thread);
        calRM(scen, atdm, thread);
        calLane(scen, atdm, thread);
        calHeavyVehAdj(thread);
        calTotalRampDensity(thread);
        calDemand(scen, atdm, thread);
        calFFS(scen, atdm, thread);
        calType(thread);
        calCapacity(scen, atdm, thread);
        calDC(thread);
        checkFrontClearQueue(thread);
        resetOversaturated(thread, true);
    }

    private void resetThreadMemory(int thread) {
        scenMaxDC[thread] = 0;
        scenMaxVC[thread] = 0;
    }

    private void calRM(int scen, int atdm, int thread) {
        if (inType == CEConst.SEG_TYPE_GP) {
            if (atdm < 0) {
                for (int period = 1; period < inNumPeriod; period++) {
                    scenRM_veh[thread][period] = inRM_veh.get(period);
                }
            } else {
                for (int period = 1; period < inNumPeriod; period++) {
                    scenRM_veh[thread][period] = seed.getATDMRM(scen, atdm, inIndex, period);
                }
            }
        } else {
            for (int period = 1; period < inNumPeriod; period++) {
                scenRM_veh[thread][period] = Float.MAX_VALUE;
            }
        }
    }

    /**
     *
     * @param thread
     */
    private void checkFrontClearQueue(int thread) {
        scenIsFrontClearingQueues[thread][0] = false;
        for (int period = 1; period < inNumPeriod; period++) {
            scenIsFrontClearingQueues[thread][period] = scenMainlineNumLanes[thread][period] > scenMainlineNumLanes[thread][period - 1]
                    && scenMainlineCapacity_veh[thread][period] > scenMainlineDemand_veh[thread][period];
        }
    }

    /**
     * Calculate heavy vehicle adjustment factors
     *
     * @param thread
     */
    private void calHeavyVehAdj(int thread) {
        //calculate for fHV
        for (int period = 0; period < inNumPeriod; period++) {
            inMainlineFHV[thread][period] = (float) (1.0 / (1.0
                    + inMainlineTruck.get(period) * (inET - 1.0) / 100.0
                    + inMainlineRV.get(period) * (inER - 1.0) / 100.0));
        }
    }

    /**
     * Calculate total ramp density
     *
     * @param thread
     */
    private void calTotalRampDensity(int thread) {
        //only first segment does calculation
        if (inUpSeg != null) {
            totalRampDensity[thread] = inUpSeg.totalRampDensity[thread];
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
            totalRampDensity[thread] = totalRamps / (totalLengthInFt / 5280f);
        }
    }

    /**
     * Calculate adjusted demand based on adjustment factors for one scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param thread
     */
    private void calDemand(int scen, int atdm, int thread) {
        float value;
        for (int period = 0; period < inNumPeriod; period++) {

            if (inGPMLType == CEConst.SEG_TYPE_GP) {
                calDemand_Mainline(scen, atdm, thread, period);
                if (seed.isManagedLaneUsed()) {
                    inParallelSeg.calDemand_Mainline(scen, atdm, thread, period);
                }
            }

            if (inType == CEConst.SEG_TYPE_W) {
                value = inRRDemand_veh.get(period)
                        * Math.min(inUOAF.get(period) * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType),
                                inUDAF.get(period) * seed.getRLAndATDMDAF(scen, atdm, inIndex, period, inGPMLType));
                scenRRDemand_veh[thread][period] = value;
                calWeaving(thread, period);
            }

            if (inType == CEConst.SEG_TYPE_ACS) {
                //TODO: need discussion
                float minODAF = Math.min(inUOAF.get(period) * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType),
                        inUDAF.get(period) * seed.getRLAndATDMDAF(scen, atdm, inIndex, period, inGPMLType));

                //recalculate ONR/OFR demand for access segments
                scenOnDemand_veh[thread][period]
                        = inParallelSeg.scenMainlineDemand_veh[thread][period] / inParallelSeg.scenMainlineNumLanes[thread][period];

                scenRRDemand_veh[thread][period]
                        = scenOnDemand_veh[thread][period] - inOnDemand_veh.get(period) * minODAF;

                scenOffDemand_veh[thread][period]
                        = scenRRDemand_veh[thread][period] + inOffDemand_veh.get(period) * minODAF;

                if (scenOnDemand_veh[thread][period] <= 0) {
                    MainWindow.printLog("Warning: Negtive ONR Demand " + scenOnDemand_veh[thread][period] + " in segment " + (inIndex + 1) + " period " + (period + 1));
                }
                if (scenOffDemand_veh[thread][period] <= 0) {
                    MainWindow.printLog("Warning: Negtive OFR Demand " + scenOffDemand_veh[thread][period] + " in segment " + (inIndex + 1) + " period " + (period + 1));
                }
                if (scenRRDemand_veh[thread][period] <= 0) {
                    MainWindow.printLog("Warning: Negtive RR Demand " + scenRRDemand_veh[thread][period] + " in segment " + (inIndex + 1) + " period " + (period + 1));
                }

                calWeaving(thread, period);
            }
        }
    }

    private void calDemand_Mainline(int scen, int atdm, int thread, int period) {

        if (inUpSeg == null) {
            //assume first segment is always basic segment
            //adjust mainline demand
            float value = inMainlineDemand_veh.get(period)
                    * inUOAF.get(period)
                    * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType);
            scenMainlineDemand_veh[thread][period] = value;
        } else {
            //adjust on ramp demand
            float value = inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                    ? inOnDemand_veh.get(period) * inUOAF.get(period) * seed.getRLAndATDMOAF(scen, atdm, inIndex, period, inGPMLType) : 0;
            scenOnDemand_veh[thread][period] = value;

            //adjust mainline demand
            value = inUpSeg.scenMainlineDemand_veh[thread][period] - inUpSeg.scenOffDemand_veh[thread][period]
                    + scenOnDemand_veh[thread][period];
            scenMainlineDemand_veh[thread][period] = value;

            //adjust off ramp demand
            value = inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                    ? inOffDemand_veh.get(period) * inUDAF.get(period) * seed.getRLAndATDMDAF(scen, atdm, inIndex, period, inGPMLType) : 0;
            scenOffDemand_veh[thread][period] = value;
        }
    }

    /**
     * Calculate weaving volume for one scenario one period
     *
     * @param thread thread index
     * @param period analysis period index (0 is the first period)
     */
    private void calWeaving(int thread, int period) {
        //calculate weave volume, non weave volume, and min rate of lane change
        float vFF_pc; //freeway to freeway demand pc/h
        float vFR_pc; //freeway to ramp demand pc/h
        float vRF_pc; //ramp to freeway demand pc/h
        float vRR_pc; //ramp to ramp demand pc/h

        vRR_pc = CEHelper.veh_to_pc(scenRRDemand_veh[thread][period], inMainlineFHV[thread][period]);//scenRRDemand_pc[thread][period];
        vRF_pc = CEHelper.veh_to_pc(scenOnDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenOnDemand_pc[thread][period]*/ - vRR_pc;
        vFR_pc = CEHelper.veh_to_pc(scenOffDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenOffDemand_pc[thread][period]*/ - vRR_pc;
        vFF_pc = CEHelper.veh_to_pc(scenMainlineDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenMainlineDemand_pc[thread][period]*/
                - CEHelper.veh_to_pc(scenOnDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenOnDemand_pc[thread][period]*/
                - vFR_pc;

        if (inOnSide == inOffSide) {
            scenVW[thread][period] = vFR_pc + vRF_pc;
            scenVNW[thread][period] = vFF_pc + vRR_pc;
        } else {
            scenVW[thread][period] = vRR_pc;
            scenVNW[thread][period] = vFF_pc + vFR_pc + vRF_pc;
        }
    }

    /**
     * Calculate adjusted free flow speed based on adjustment factors for one
     * scenario
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param thread
     */
    private void calFFS(int scen, int atdm, int thread) {
        for (int period = 0; period < inNumPeriod; period++) {
            scenMainlineFFS[thread][period]
                    = inMainlineFFS.get(period) * inUSAF.get(period) * seed.getRLAndATDMSAF(scen, atdm, inIndex, period, inGPMLType);
            scenOnFFS[thread][period]
                    = inOnFFS.get(period) * inUSAF.get(period) * seed.getRLAndATDMSAF(scen, atdm, inIndex, period, inGPMLType);
            scenOffFFS[thread][period]
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
     * @param thread
     */
    private void calLane(int scen, int atdm, int thread) {
        if (inGPMLType == CEConst.SEG_TYPE_GP) {
            //calculate number of lanes for GP segments (min one lane)
            for (int period = 0; period < inNumPeriod; period++) {
                scenMainlineNumLanes[thread][period]
                        = Math.max(1, inMainlineNumLanes.get(period)
                                + seed.getRLAndATDMLAF(scen, atdm, inIndex, period, inGPMLType));
            }
            //calculate number of lanes for ML segments (min one lane)
            if (seed.isManagedLaneUsed()) {
                for (int period = 0; period < inNumPeriod; period++) {
                    inParallelSeg.scenMainlineNumLanes[thread][period]
                            = Math.max(1, inParallelSeg.inMainlineNumLanes.get(period)
                                    + seed.getRLAndATDMLAF(scen, atdm, inIndex, period, inParallelSeg.inGPMLType));
                }
            }
        }
    }

    /**
     * Calculate processing segment type for one scenario
     *
     * @param thread
     */
    private void calType(int thread) {
        for (int period = 0; period < inNumPeriod; period++) {
            try {
                switch (inType) {
                    case CEConst.SEG_TYPE_B:
                    case CEConst.SEG_TYPE_R:
                    case CEConst.SEG_TYPE_ACS:
                        scenType[thread][period] = inType;
                        break;
                    case CEConst.SEG_TYPE_ONR:
                        scenType[thread][period]
                                = inUpSeg.scenMainlineNumLanes[thread][period] < scenMainlineNumLanes[thread][period]
                                ? CEConst.SEG_TYPE_ONR_B : inType;
                        break;
                    case CEConst.SEG_TYPE_OFR:
                        scenType[thread][period]
                                = inDownSeg.scenMainlineNumLanes[thread][period] > scenMainlineNumLanes[thread][period]
                                ? CEConst.SEG_TYPE_OFR_B : inType;
                        break;
                    case CEConst.SEG_TYPE_W:
                        scenType[thread][period]
                                = inShort_ft > funcMaxShort(thread, period)
                                ? CEConst.SEG_TYPE_W_B : inType;
                        break;
                    default:
                        System.out.println("Warning: calType - Invalid Type");
                }
            } catch (Exception e) {
                System.out.println("calType: " + e.toString());
                scenType[thread][period] = inType;
            }
        }
    }

    /**
     * Calculate maximum short length for weaving segment in ft
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return maximum short length for weaving segment in ft
     */
    private float funcMaxShort(int thread, int period) {
        return (float) (5728 * Math.pow(1
                + scenVW[thread][period] / (scenVW[thread][period] + scenVNW[thread][period])/*scenVR[thread][period]*/,
                1.6) - 1566 * inNWL);
    }

    /**
     * Calculate adjusted mainline and ramp capacities
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param thread
     */
    private void calCapacity(int scen, int atdm, int thread) {
        float result;
        for (int period = 0; period < inNumPeriod; period++) {
            switch (scenType[thread][period]) {
                case CEConst.SEG_TYPE_B:
                case CEConst.SEG_TYPE_R:
                    result = funcBasicMainlineCapacity(scen, atdm, thread, period);
                    scenMainlineCapacity_veh[thread][period] = result;
                    break;
                case CEConst.SEG_TYPE_ONR:
                case CEConst.SEG_TYPE_ONR_B:
                    result = funcBasicMainlineCapacity(scen, atdm, thread, period);
                    scenMainlineCapacity_veh[thread][period] = result;
                    result = funcOnRampCapacity(thread, period);
                    scenOnCapacity_veh[thread][period] = result;
                    break;
                case CEConst.SEG_TYPE_OFR:
                case CEConst.SEG_TYPE_OFR_B:
                    result = funcBasicMainlineCapacity(scen, atdm, thread, period);
                    scenMainlineCapacity_veh[thread][period] = result;
                    result = funcOffRampCapacity(thread, period);
                    scenOffCapacity_veh[thread][period] = result;
                    break;
                case CEConst.SEG_TYPE_W:
                case CEConst.SEG_TYPE_ACS:
                    result = funcWeaveMainlineCapacity(scen, atdm, thread, period);
                    scenMainlineCapacity_veh[thread][period] = result;
                    result = funcOnRampCapacity(thread, period);
                    scenOnCapacity_veh[thread][period] = result;
                    result = funcOffRampCapacity(thread, period);
                    scenOffCapacity_veh[thread][period] = result;
                    break;
                case CEConst.SEG_TYPE_W_B:
                    result = funcBasicMainlineCapacity(scen, atdm, thread, period);
                    scenMainlineCapacity_veh[thread][period] = result;
                    result = funcOnRampCapacity(thread, period);
                    scenOnCapacity_veh[thread][period] = result;
                    result = funcOffRampCapacity(thread, period);
                    scenOffCapacity_veh[thread][period] = result;
                    break;
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
     * @param thread
     * @return adjusted mainline capacity (vph) for basic segment
     */
    private float funcBasicMainlineCapacity(int scen, int atdm, int thread, int period) {
        float result = CEHelper.pc_to_veh(funcIdealCapacity(scenMainlineFFS[thread][period], thread, period)
                * scenMainlineNumLanes[thread][period],
                inMainlineFHV[thread][period]);
        return result * inUCAF.get(period) * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType); //vph
    }

    /**
     * Calculate adjusted on ramp capacity (vph)
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return adjusted on ramp capacity (vph)
     */
    private float funcOnRampCapacity(int thread, int period) {
        float result = CEHelper.pc_to_veh(funcRampCapacity(inOnNumLanes.get(period), scenOnFFS[thread][period]),
                inMainlineFHV[thread][period]);
        return result; //vph
    }

    /**
     * Calculate adjusted off ramp capacity (vph)
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return adjusted off ramp capacity (vph)
     */
    private float funcOffRampCapacity(int thread, int period) {
        float result = CEHelper.pc_to_veh(funcRampCapacity(inOffNumLanes.get(period), scenOffFFS[thread][period]),
                inMainlineFHV[thread][period]);
        return result; //vph
    }

    /**
     * Calculate adjusted mainline capacity (vph) for weaving segment
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param period analysis period index (0 is the first period)
     * @param thread
     * @return adjusted mainline capacity (vph) for weaving segment
     */
    private float funcWeaveMainlineCapacity(int scen, int atdm, int thread, int period) {
        //HCM Page 12-17
        float cIWL, cIW, cW;
        float cIFL = funcIdealCapacity(scenMainlineFFS[thread][period], thread, period);
        float VR = scenVW[thread][period] / (scenVW[thread][period] + scenVNW[thread][period]);
        //Weaving
        //Equation 12-5
        cIWL = (float) (cIFL - 438.2 * Math.pow(1 + VR/*scenVR[thread][period]*/, 1.6)
                + 0.0765 * inShort_ft + 119.8 * inNWL);
        //Equation 12-7
        cIW = (inNWL == 2 ? 2400 : 3500) / VR/*scenVR[thread][period]*/ / inNWL;
        //Equation 12-6, 12-8
        cW = CEHelper.pc_to_veh(Math.min(cIWL, cIW) * scenMainlineNumLanes[thread][period], inMainlineFHV[thread][period]);
        return cW * inUCAF.get(period) * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType); //vph
    }

    /**
     * Calculate ideal mainline capacity (pc/h/ln) based on mainline free flow
     * speed
     *
     * @param freeFlowSpeed mainline free flow speed (mph)
     * @param thread
     * @param period
     * @return ideal mainline capacity (pc/h/ln)
     */
    private float funcIdealCapacity(float freeFlowSpeed, int thread, int period) {
        //pc/h/ln
        return 2200 + 10 * (Math.min(70, freeFlowSpeed) - 50); //New Generic Equation
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
     * @param thread
     */
    private void calDC(int thread) {
        float max = 0;
        float DC;
        for (int period = 0; period < inNumPeriod; period++) {
            DC = scenMainlineDemand_veh[thread][period] / scenMainlineCapacity_veh[thread][period];
            //scenDC[thread][period] = DC;
            max = Math.max(max, DC);
        }
        scenMaxDC[thread] = max;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UNDERSAT FUNCTIONS - SPEED & DENSITY">
    /**
     * Run under saturated analysis for one scenario one period
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param thread
     * @param period analysis period index (0 is the first period)
     */
    void runUndersaturated(int scen, int atdm, int thread, int period) {
        calSpeedAndDensity(scen, atdm, thread, period, CEConst.STATUS_UNDER);
        //limits for speed
        scenSpeed[thread][period] = Math.min(scenSpeed[thread][period], scenMainlineFFS[thread][period]);
        scenSpeed[thread][period] = Math.min(scenSpeed[thread][period], funcMaxSpeed(thread, period));

        scenMainlineVolume_veh[thread][period] = scenMainlineDemand_veh[thread][period];
        scenOnVolume_veh[thread][period]
                = inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                ? scenOnDemand_veh[thread][period] : 0;
        scenOffVolume_veh[thread][period]
                = inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                ? scenOffDemand_veh[thread][period] : 0;

        scenVC[thread][period] = scenMainlineVolume_veh[thread][period] / scenMainlineCapacity_veh[thread][period];
        if (scenVC[thread][period] > scenMaxVC[thread]) {
            scenMaxVC[thread] = scenVC[thread][period];
        }

        if (inUpSeg != null && inUpSeg.inType == CEConst.SEG_TYPE_R) {
            checkOverlap(thread, period);
        }

        if (inUpSeg == null) {
            scenDenyQ[thread][period] = 0f;
        }

        if (inOverMode[thread] != 0) {
            resetOversaturated(thread, false);
        }
    }

    /**
     * Calculate speed and density using under saturated method
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @param status whether under saturated, over saturated, or background
     * density calculation
     */
    private void calSpeedAndDensity(int scen, int atdm, int thread, int period, int status) {
        switch (scenType[thread][period]) {
            case CEConst.SEG_TYPE_B:
            case CEConst.SEG_TYPE_ONR_B:
            case CEConst.SEG_TYPE_OFR_B:
            case CEConst.SEG_TYPE_W_B:
                scenSpeed[thread][period] = funcBasicSpeed(status, scen, atdm, thread, period);
                scenAllDensity_veh[thread][period] = funcBasicDensity(status, scen, atdm, thread, period);
                break;
            case CEConst.SEG_TYPE_ONR:
                scenSpeed[thread][period] = funcOnSpeed(status, thread, period);
                scenIADensity_pc[thread][period] = funcOnIADensity(status, thread, period);
                scenAllDensity_veh[thread][period] = funcOnAllDensity(status, thread, period);
                break;
            case CEConst.SEG_TYPE_OFR:
                scenSpeed[thread][period] = funcOffSpeed(status, thread, period);
                scenIADensity_pc[thread][period] = funcOffIADensity(status, thread, period);
                scenAllDensity_veh[thread][period] = funcOffAllDensity(status, thread, period);
                break;
            case CEConst.SEG_TYPE_W:
            case CEConst.SEG_TYPE_ACS:
                scenSpeed[thread][period] = funcWeaveSpeed(status, scen, atdm, thread, period);
                scenAllDensity_veh[thread][period] = funcWeaveDensity(status, scen, atdm, thread, period);
                break;
            case CEConst.SEG_TYPE_R:
                scenSpeed[thread][period] = funcOverlapSpeed(status, scen, atdm, thread, period);
                scenAllDensity_veh[thread][period] = funcOverlapDensity(status, scen, atdm, thread, period);
                break;
        }
    }

    /**
     * Calculate maximum speed based on upstream speed
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcMaxSpeed(int thread, int period) {
        //maximum achievable segment speed Equation 25-2 :  HCM Page 25-13
        if (inUpSeg == null) {
            return Float.POSITIVE_INFINITY;
        } else {
            return (float) (scenMainlineFFS[thread][period]
                    - (scenMainlineFFS[thread][period] - inUpSeg.scenSpeed[thread][period])
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
     * @param thread
     * @return speed (mph)
     */
    private float funcBasicSpeed(int status, int scen, int atdm, int thread, int period) {
        float vp; //demand flow rate
        //calculate vp
        switch (status) {
            case CEConst.STATUS_UNDER:
                //demandFlowRate (vp) Equation 11-2: HCM Page 11-13
                vp
                        = CEHelper.veh_to_pc(scenMainlineDemand_veh[thread][period], inMainlineFHV[thread][period]) / scenMainlineNumLanes[thread][period];
                break;
            case CEConst.STATUS_BG:
                vp = CEHelper.veh_to_pc(ED[thread], inMainlineFHV[thread][period])
                        / scenMainlineNumLanes[thread][period];
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                vp = CEHelper.veh_to_pc(scenMainlineVolume_veh[thread][period], inMainlineFHV[thread][period])
                        / scenMainlineNumLanes[thread][period];
                break;
            default:
                vp = 0;
        }

        //calculat speed
        if (inGPMLType == CEConst.SEG_TYPE_GP) {
            //new 25-1 equation for GP segment
            //TODO use original FFS for now, may need to use adjusted FFS
            float CAF = inUCAF.get(period) * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType);
            float BP_adj = (1000 + 40 * (75 - scenMainlineFFS[thread][period])) * CAF * CAF;

            if (vp <= BP_adj) {
                return scenMainlineFFS[thread][period];
            } else {
                float C_adj = CAF * (2200 + 10 * (Math.min(70, inMainlineFFS.get(period)) - 50));
                return scenMainlineFFS[thread][period]
                        - (scenMainlineFFS[thread][period] - C_adj / 45f)
                        / (C_adj - BP_adj) / (C_adj - BP_adj)
                        * (vp - BP_adj) * (vp - BP_adj);
            }
        } else {
            float CAF = inUCAF.get(period) * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType);
            final float BP_75, lamda_BP, C_75, C_55_2, lamda_C2, C_1, K_nf_C, K_f_C;
            final float lamda_C = 10;
            switch (inMLSeparation) {
                case CEConst.ML_SEPARATION_BUFFER:
                    if (scenMainlineNumLanes[thread][period] == 1) {
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
                    if (scenMainlineNumLanes[thread][period] == 1) {
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
            float FFS_adj = scenMainlineFFS[thread][period];
            float BP_adj = (BP_75 + lamda_BP * (75 - FFS_adj)) * CAF * CAF;
            float C_adj = CAF * (C_75 - lamda_C * (75 - FFS_adj));
            float C_2 = C_55_2 + lamda_C2 * (FFS_adj - 55);
            float S_1 = FFS_adj - C_1 * Math.min(vp, BP_adj);
            if (vp <= BP_adj) {
                return S_1;
            } else {
                float S_2 = (float) ((S_1 - C_adj / K_nf_C) / Math.pow(C_adj - BP_adj, C_2) * Math.pow(vp - BP_adj, C_2));
                float S_3 = (float) ((C_adj / K_nf_C - C_adj / K_f_C) / Math.pow(C_adj - BP_adj, 2) * Math.pow(vp - BP_adj, 2));
                return S_1 - S_2 - (CEHelper.veh_to_pc(inParallelSeg.scenAllDensity_veh[thread][period], inParallelSeg.inMainlineFHV[thread][period]) <= 35 ? 0 : 1) * S_3;
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcBasicDensity(int status, int scen, int atdm, int thread, int period) {
        switch (status) {
            case CEConst.STATUS_UNDER:
                //return veh/mi/ln
                //need to calculate speed first
                return scenMainlineDemand_veh[thread][period]
                        / scenMainlineNumLanes[thread][period] / scenSpeed[thread][period];
            case CEConst.STATUS_BG:
                //return veh/mi
                return ED[thread] / funcBasicSpeed(status, scen, atdm, thread, period);
            case CEConst.STATUS_OVER_TO_UNDER:
                //return veh/mi/ln
                //need to calculate speed first
                return scenMainlineVolume_veh[thread][period]
                        / scenMainlineNumLanes[thread][period] / scenSpeed[thread][period];
            default:
                return 0;
        }
    }

    /**
     * Calculate speed for on ramp segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcOnSpeed(int status, int thread, int period) {
        //TODO need to check condition
        //if (!(inUCAF.get(period) * scenarios.get(scen).sCAF[period] != 1 && status == CEConst.STATUS_BG)) {
        //Exhibit 13-11 : HCM Page 13-20
        float v_12 = funcOnFlowRateInLanes1and2(status, thread, period);
        float v_F = funcOnOff_vF(status, thread, period);
        int N_O = Math.max(scenMainlineNumLanes[thread][period] - 2, 0);
        float v_R12 = v_12 + funcOn_vR(status, thread, period);
        float M_S = (float) (0.321 + 0.0039 * Math.exp(v_R12 / 1000.0) - 2e-6 * inAccDecLength_ft * scenOnFFS[thread][period]);
        //TODO negative on ramp segment speed!!!
        float S_R = (float) Math.max(1, scenMainlineFFS[thread][period] - (scenMainlineFFS[thread][period] - 42) * M_S);
        if (N_O == 0) {
            return S_R;
        } else {
            float v_OA = (v_F - v_12) / N_O;
            float S_O;
            if (v_OA < 500) {
                S_O = scenMainlineFFS[thread][period];
            } else {
                if (v_OA > 2300) {
                    S_O = (float) (scenMainlineFFS[thread][period] - 6.53 - 0.006 * (v_OA - 2300));
                } else {
                    S_O = (float) (scenMainlineFFS[thread][period] - 0.0036 * (v_OA - 500));
                }
            }
            float S = (v_R12 + v_OA * N_O) / ((v_R12 / S_R) + (v_OA * N_O / S_O));
            return S;
        }
        //} else {
        //    return funcBasicSpeed(status, thread, period);
        //}
    }

    /**
     * Calculate on ramp flow rate in lane 1 and 2 using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return flow rate
     */
    private float funcOnFlowRateInLanes1and2(int status, int thread, int period) {
        float P_FM = funcOnRemainFactor(status, thread, period);
        float v_F = funcOnOff_vF(status, thread, period);
        float result;

        //Equation 13-2 : HCM Page 13-12
        result = v_F * P_FM;
        //HCM Page 13-16, check reasonableness of the lane distribution prediction
        //TODO check 5 or more lanes?
        switch (scenMainlineNumLanes[thread][period]) {
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
        return result;
    }

    /**
     * Calculate on ramp remain factor using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return remain factor
     */
    private float funcOnRemainFactor(int status, int thread, int period) {
        //TODO incomplete
        float v_F = funcOnOff_vF(status, thread, period);
        float v_R = funcOn_vR(status, thread, period);
        float result;

        if (scenMainlineNumLanes[thread][period] <= 4) {
            //Exhibit 13-6 : HCM Page 13-13 and Exhibit 13-16 : HCM Page 13-25
            switch (scenMainlineNumLanes[thread][period]) {
                //TODO ignore 10 lane case
                case 3: //6-Lane, 3 lanes each direction
                    //TODO need a function to calculate P_FM, use Equation 13-3 now
                    result = (float) (0.5775 + 0.000028 * inAccDecLength_ft);
                    if (inOnSide == CEConst.RAMP_SIDE_LEFT) {
                        result *= 1.12;
                    }
                    break;
                case 4: //8-Lane, 4 lanes each direction
                    float S_FR = scenOnFFS[thread][period];
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
            result = (float) (0.2178 * 4 / scenMainlineNumLanes[thread][period]);
        }

        return result;
    }

    /**
     * Calculate v_F for on ramp or off ramp segment using under saturated
     * method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return v_F
     */
    private float funcOnOff_vF(int status, int thread, int period) {
        float result;
        switch (status) {
            //assume left segment exists and is basic or R
            case CEConst.STATUS_UNDER:
                result = CEHelper.veh_to_pc(inUpSeg.scenMainlineDemand_veh[thread][period], inUpSeg.inMainlineFHV[thread][period])/*inUpSeg.scenMainlineDemand_pc[thread][period]*/;
                break;
            case CEConst.STATUS_BG:
                //TODO use left ED?
                result = CEHelper.veh_to_pc(inUpSeg.ED[thread], inUpSeg.inMainlineFHV[thread][period]);
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                //TODO use left SF?
                result = CEHelper.veh_to_pc(inUpSeg.scenMainlineVolume_veh[thread][period], inUpSeg.inMainlineFHV[thread][period]);
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return v_R
     */
    private float funcOn_vR(int status, int thread, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
            case CEConst.STATUS_BG:
                result = CEHelper.veh_to_pc(scenOnDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenOnDemand_pc[thread][period]*/;
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                result = CEHelper.veh_to_pc(CEHelper.average(ONRF[thread]) * T, inMainlineFHV[thread][period]);
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return overall density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcOnAllDensity(int status, int thread, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
                if (scenMainlineNumLanes[thread][period] > 2) {
                    //on ramp demand included
                    result = scenMainlineDemand_veh[thread][period] / scenSpeed[thread][period] / scenMainlineNumLanes[thread][period];
                } else {
                    //Equation 13-21 : HCM Page 13-19
                    result = (float) (5.475 + 0.00734 * funcOn_vR(status, thread, period)
                            + 0.0078 * funcOnFlowRateInLanes1and2(status, thread, period) - 0.00627 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[thread][period]);
                }
                //return veh/mi/ln
                return result;

            case CEConst.STATUS_BG:
                if (scenMainlineNumLanes[thread][period] > 2) {
                    float speed = funcOnSpeed(status, thread, period);
                    //TODO use V_fe??
                    result = (inUpSeg.ED[thread] + scenOnDemand_veh[thread][period]) / speed;
                    //result = ED[thread] / speed;
                } else {
                    result = (float) (5.475 + 0.00734 * funcOn_vR(status, thread, period)
                            + 0.0078 * funcOnFlowRateInLanes1and2(status, thread, period) - 0.00627 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[thread][period]) * scenMainlineNumLanes[thread][period];
                }
                //return veh/mi
                return result;

            case CEConst.STATUS_OVER_TO_UNDER:
                if (scenMainlineNumLanes[thread][period] > 2) {
                    result = scenMainlineVolume_veh[thread][period] / scenSpeed[thread][period] / scenMainlineNumLanes[thread][period];
                } else {
                    result = (float) (5.475 + 0.00734 * funcOn_vR(status, thread, period)
                            + 0.0078 * funcOnFlowRateInLanes1and2(status, thread, period) - 0.00627 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[thread][period]);
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return influenced area density (pc/mi/ln)
     */
    private float funcOnIADensity(int status, int thread, int period) {
        float result;
        //Equation 13-21 : HCM Page 13-19
        result = (float) (5.475 + 0.00734 * funcOn_vR(status, thread, period)
                + 0.0078 * funcOnFlowRateInLanes1and2(status, thread, period) - 0.00627 * inAccDecLength_ft);
        //return pc/mi/ln
        return result;
    }

    /**
     * Calculate speed for off ramp segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcOffSpeed(int status, int thread, int period) {
        //TODO need to check condition
        //if (!(inUCAF.get(period) * scenarios.get(scen).sCAF[period] != 1 && status == CEConst.STATUS_BG)) {
        //Exhibit 13-11 : HCM Page 13-20
        float v_12 = funcOffFlowRateInLanes1and2(status, thread, period);
        float v_F = funcOnOff_vF(status, thread, period);
        float P_FD = funcOffRemainFactor(status, thread, period);
        float v_R = funcOff_vR(status, thread, period);
        int N_O = Math.max(scenMainlineNumLanes[thread][period] - 2, 0);
        float D_S = (float) (0.883 + 0.00009 * v_R - 0.013 * scenOffFFS[thread][period]);
        float S_R = scenMainlineFFS[thread][period] - (scenMainlineFFS[thread][period] - 42) * D_S;
        if (N_O == 0) {
            return S_R;
        } else {
            float v_OA = (v_F - v_12) / N_O;
            float S_O;
            if (v_OA < 1000) {
                S_O = (float) (1.097 * scenMainlineFFS[thread][period]);
            } else {
                //TODO new equation??
                S_O = (float) (1.097 * scenMainlineFFS[thread][period] - 0.0039 * ((v_F - v_R - (v_F - v_R) * P_FD) / N_O - 1000)); //VBA
                //S_O = (float) (1.097 * scenMainlineFFS[thread][period] + 0.00009 * v_R - 0.013 * scenOffFFS[thread][period]); //HCM
            }
            //TODO new equation??
            float S = v_F / ((v_R + (v_F - v_R) * P_FD) / S_R + (v_F - v_R - (v_F - v_R) * P_FD) / S_O); // VBA
            //float S = (v_12 + v_OA * N_O) / ((v_12 / S_R) + (v_OA * N_O / S_O)); //HCM
            return S;
        }
        //} else {
        //    return funcBasicSpeed(status, thread, period);
        //}
    }

    /**
     * Calculate overall density for off ramp segment using under saturated
     * method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return overall density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcOffAllDensity(int status, int thread, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
                if (scenMainlineNumLanes[thread][period] > 2) {
                    result = scenMainlineDemand_veh[thread][period] / scenSpeed[thread][period] / scenMainlineNumLanes[thread][period];
                } else {
                    //Equation 13-21 : HCM Page 13-19
                    result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, thread, period) - 0.009 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[thread][period]);
                }
                //return veh/mi/ln
                return result;

            case CEConst.STATUS_BG:
                if (scenMainlineNumLanes[thread][period] > 2) {
                    float speed = funcOffSpeed(CEConst.STATUS_BG, thread, period);
                    result = ED[thread] / speed;
                } else {
                    result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, thread, period) - 0.009 * inAccDecLength_ft)
                            * scenMainlineNumLanes[thread][period];
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[thread][period]) * scenMainlineNumLanes[thread][period];
                }
                //check left overlapping segment
                if (inUpSeg != null && inUpSeg.inType == CEConst.SEG_TYPE_R) {
                    inUpSeg.KB[thread] = Math.max(result, inUpSeg.KB[thread]);
                }
                //return veh/mi
                return result;

            case CEConst.STATUS_OVER_TO_UNDER:
                if (scenMainlineNumLanes[thread][period] > 2) {
                    result = scenMainlineVolume_veh[thread][period] / scenSpeed[thread][period] / scenMainlineNumLanes[thread][period];
                } else {
                    //Equation 13-21 : HCM Page 13-19
                    result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, thread, period) - 0.009 * inAccDecLength_ft);
                    result = CEHelper.pc_to_veh(result, inMainlineFHV[thread][period]);
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return influenced area density (pc/mi/ln)
     */
    private float funcOffIADensity(int status, int thread, int period) {
        float result;
        //Equation 13-21 : HCM Page 13-19
        result = (float) (4.252 + 0.0086 * funcOffFlowRateInLanes1and2(status, thread, period) - 0.009 * inAccDecLength_ft);
        //return pc/mi/ln
        return result;
    }

    /**
     * Calculate off ramp flow rate in lane 1 and 2 using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return flow rate
     */
    private float funcOffFlowRateInLanes1and2(int status, int thread, int period) {
        float v_F = funcOnOff_vF(status, thread, period);
        float P_FD = funcOffRemainFactor(status, thread, period);
        float v_R = funcOff_vR(status, thread, period);

        //Equation 13-8 : HCM Page 13-14
        float v_12 = v_R + (v_F - v_R) * P_FD;

        //check reasonableness of the lane distribution prediction
        //HCM Page 13-16
        switch (scenMainlineNumLanes[thread][period]) {
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
        return v_12;
    }

    /**
     * Calculate off ramp remain factor using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return remain factor
     */
    private float funcOffRemainFactor(int status, int thread, int period) {
        //TODO incomplete
        float v_F = funcOnOff_vF(status, thread, period);
        float v_R = funcOff_vR(status, thread, period);
        float result;
        if (scenMainlineNumLanes[thread][period] <= 4) {
            //Exhibit 13-7 : HCM Page 13-14 and Exhibit 13-16 : HCM Page 13-25
            switch (scenMainlineNumLanes[thread][period]) {
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
            result = (float) (0.436 * 4 / scenMainlineNumLanes[thread][period]);
        }
        return result;
    }

    /**
     * Calculate v_R for off ramp segment using under saturated method
     *
     * @param status whether under saturated, over saturated, or background
     * density calculation
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return v_R
     */
    private float funcOff_vR(int status, int thread, int period) {
        float result;
        switch (status) {
            //assume left segment exists and is basic or R
            case CEConst.STATUS_UNDER:
            case CEConst.STATUS_BG:
                //TODO use left ED?
                result = CEHelper.veh_to_pc(scenOffDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenOffDemand_pc[thread][period]*/;
                break;
            case CEConst.STATUS_OVER_TO_UNDER:
                //TODO use left SF?
                result = CEHelper.veh_to_pc(CEHelper.average(OFRF[thread]) * T, inMainlineFHV[thread][period]);
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcWeaveSpeed(int status, int scen, int atdm, int thread, int period) {
        if (!(inUCAF.get(period) * seed.getRLAndATDMCAF(scen, atdm, inIndex, period, inGPMLType) != 1 && status == CEConst.STATUS_BG)) {
            float minRateOfLaneChange;
            float vRR_pc = CEHelper.veh_to_pc(scenRRDemand_veh[thread][period], inMainlineFHV[thread][period]);//scenRRDemand_pc[thread][period]; //ramp to ramp demand pc/h
            float vRF_pc = CEHelper.veh_to_pc(scenOnDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenOnDemand_pc[thread][period]*/ - vRR_pc; //ramp to freeway demand pc/h
            float vFR_pc = CEHelper.veh_to_pc(scenOffDemand_veh[thread][period], inMainlineFHV[thread][period])/*scenOffDemand_pc[thread][period]*/ - vRR_pc; //freeway to ramp demand pc/h

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
                    * Math.pow(scenMainlineNumLanes[thread][period], 2) * Math.pow(1 + totalRampDensity[thread] / 2.0, 0.8)); //assume ID = TRD/2
            //Equation 12 - 11
            I_NW = inShort_ft * (totalRampDensity[thread] / 2.0f) * scenVNW[thread][period] / 10000; //assume ID = TRD/2

            if (I_NW <= 1300) {
                //Equation 12 - 12
                LC_NW = (float) (0.206 * scenVNW[thread][period] + 0.542 * inShort_ft - 192.6 * scenMainlineNumLanes[thread][period]);
            } else {
                if (I_NW >= 1950) {
                    //Equation 12 - 13
                    LC_NW = (float) (2135 + 0.223 * (scenVNW[thread][period] - 2000));
                } else {
                    LC_NW1 = (float) (0.206 * scenVNW[thread][period] + 0.542 * inShort_ft - 192.6 * scenMainlineNumLanes[thread][period]);
                    LC_NW2 = (float) (2135 + 0.223 * (scenVNW[thread][period] - 2000));
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
            S_W = 15 + (scenMainlineFFS[thread][period] - 15) / (1 + W);
            //Equation 12-19
            S_NW = (float) (scenMainlineFFS[thread][period] - 0.0072 * minRateOfLaneChange
                    - 0.0048 * (scenVW[thread][period] + scenVNW[thread][period]) / scenMainlineNumLanes[thread][period]);
            //Equation 12-20
            S = (scenVW[thread][period] + scenVNW[thread][period]) / (scenVW[thread][period] / S_W + scenVNW[thread][period] / S_NW);
            return S;
        } else {
            return funcBasicSpeed(status, scen, atdm, thread, period);
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
     * @param thread
     * @return density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcWeaveDensity(int status, int scen, int atdm, int thread, int period) {
        float result;
        switch (status) {
            case CEConst.STATUS_UNDER:
                result = scenMainlineDemand_veh[thread][period] / scenMainlineNumLanes[thread][period] / scenSpeed[thread][period];
                //return veh/mi/ln
                return result;

            case CEConst.STATUS_BG:
                //TODO not use ED?
                result = scenMainlineDemand_veh[thread][period] / funcWeaveSpeed(status, scen, atdm, thread, period);
                //return veh/mi
                return result;

            case CEConst.STATUS_OVER_TO_UNDER:
                result = scenMainlineVolume_veh[thread][period] / scenMainlineNumLanes[thread][period] / scenSpeed[thread][period];
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return speed (mph)
     */
    private float funcOverlapSpeed(int status, int scen, int atdm, int thread, int period) {
        //TODO need to check which way
        switch (status) {
            case CEConst.STATUS_UNDER:
                return inUpSeg == null ? 0 : inUpSeg.scenSpeed[thread][period];
            default:
                return funcBasicSpeed(status, scen, atdm, thread, period);
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
     * @param thread
     * @return density (unit depends on status, veh/mi/ln or veh/mi)
     */
    private float funcOverlapDensity(int status, int scen, int atdm, int thread, int period) {
        //TODO need to check which way
        switch (status) {
            case CEConst.STATUS_UNDER:
                return inUpSeg == null ? 0 : inUpSeg.scenAllDensity_veh[thread][period];
            default:
                return funcBasicDensity(status, scen, atdm, thread, period);
        }
    }

    /**
     * Check adjacent upstream overlapping segment speed and density
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     */
    void checkOverlap(int thread, int period) {
        if (inUpSeg != null && inUpSeg.inType == CEConst.SEG_TYPE_R) {
            inUpSeg.scenSpeed[thread][period]
                    = Math.min(inUpSeg.scenSpeed[thread][period], scenSpeed[thread][period]);
            inUpSeg.scenAllDensity_veh[thread][period]
                    = Math.max(inUpSeg.scenAllDensity_veh[thread][period], scenAllDensity_veh[thread][period]);
//            inUpSeg.scenAllDensity_pc[thread][period]
//                    = Math.max(inUpSeg.scenAllDensity_pc[thread][period], scenAllDensity_pc[thread][period]);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OVERSAT DATA">
    /**
     * Track how long (in periods) does a scenario in over sat calculation
     */
    transient int[] inOverMode;
    /**
     * Expected demand, vph
     */
    transient float[] ED;
    /**
     * Background density veh/mi
     */
    transient float[] KB;
    /**
     * Wave travel time (in time step)
     */
    transient int[] WTT;
    /**
     * Capacity drop factor (for two capacity calculation)
     */
    transient float[] capacityDropFactor;
    /**
     * On ramp flow, veh
     */
    transient float[][] ONRF;
    /**
     * On ramp output, veh
     */
    transient float[][] ONRO;
    /**
     * Off ramp flow, veh
     */
    transient float[][] OFRF;
    /**
     * On ramp queue, veh
     */
    transient float[][] ONRQ;
    /**
     * New method for on ramp delay calculation, veh
     */
    transient float[][] testOnRampDelay;
    /**
     * Mainline queue length in ft (at end of each period)
     */
    transient float[][] Q;
    /**
     * On ramp queue length in ft (at end of each period)
     */
    transient float[][] ONRQL;
    /**
     * On ramp queue (at end of each period), veh
     */
    transient float[][] ONRQ_End_veh;
    /**
     * Unserved vehicles, veh
     */
    transient float[][] UV;
    /**
     * Number of vehicles, veh
     */
    transient float[][] NV;
    /**
     * Queue density, veh/mi
     */
    transient float[][] KQ;
    /**
     * Deficit (for off ramp calculation), veh
     */
    transient float[][] DEF;
    /**
     * Mainline input, veh
     */
    transient float[][] MI;
    /**
     * Mainline output 1 (based capacity and previous MO2 and MO3), veh
     */
    transient float[][] MO1;
    /**
     * Mainline output 2 (based on queue density), veh
     */
    transient float[][] MO2;
    /**
     * Mainline output 3 (based on front clear queue and wave travel speed), veh
     */
    transient float[][] MO3;
    /**
     * Mainline flow, veh
     */
    transient float[][] MF;
    /**
     * Segment flow, veh
     */
    transient float[][] SF;
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
    transient float[] denyEntry;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OVERSAT FUNCTIONS">
    /**
     * Run analysis for over saturated cases
     *
     * @param scen scenario index (0 is the default scenario, 1 is the first
     * generated scenario)
     * @param atdm
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     */
    void runOversaturated(int scen, int atdm, int thread, int period, int step) {
        try {
            //ASSUME FIRST AND LAST SEGMENT MUST BE BASIC
            //initialization----once per analysis period-------step 1 & 2-------------------------------
            if (step == 0) {
                //Equation 25-3 HCM Page 25-21
                ED[thread] = funcED(thread, period);
                KB[thread] = funcKB(scen, atdm, thread, period);

                if (inDownSeg != null) {
                    //Equation 25-10&11 HCM Page 25-24
                    inDownSeg.WTT[thread] = inDownSeg.funcWTT(thread, period);
                }
                testOnRampDelay[thread][period] = 0f;
            }

            //adjust capacity (two capacity)
            if (inDownSeg == null) {
                if (UV[thread][(step + NUM_STEPS - 1) % NUM_STEPS] > CEConst.ZERO) {
                    capacityDropFactor[thread] = 1 - inCapacityDropPercentage;
                } else {
                    capacityDropFactor[thread] = 1f;
                }
            } else {
                if (UV[thread][(step + NUM_STEPS - 1) % NUM_STEPS] > CEConst.ZERO && inDownSeg.UV[thread][(step + NUM_STEPS - 1) % NUM_STEPS] <= CEConst.ZERO) {
                    inDownSeg.capacityDropFactor[thread] = 1 - inCapacityDropPercentage;
                } else {
                    inDownSeg.capacityDropFactor[thread] = 1f;
                }
            }

            //for every time step---------------step 5 to 25----------(order is different from HCM)--------
            //off-ramp calculation--------------------------------------------------------------------
            if (inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                DEF[thread][step] = funcDEF(thread, period, step);
                OFRF[thread][step] = funcOFRF(thread, period, step);
            }

            //mainline flow calculation-------------------------------------------------------------------
            //Equation 25-5 HCM Page 25-22
            MI[thread][step] = funcMI(thread, period, step);
            if (inDownSeg != null && (inDownSeg.inType == CEConst.SEG_TYPE_ONR || inDownSeg.inType == CEConst.SEG_TYPE_W || inDownSeg.inType == CEConst.SEG_TYPE_ACS)) {
                //on-ramp calculation
                inDownSeg.ONRF[thread][step] = inDownSeg.funcONRF(thread, period, step);
            }

            //Equation 25-6 HCM Page 25-22
            MO1[thread][step] = funcMO1(thread, period, step);
            //Equation 25-8 HCM Page 25-23
            MO2[thread][step] = funcMO2(thread, period, step);
            //Equation 25-12 HCM Page 25-24
            MO3[thread][step] = funcMO3(thread, period, step);
            //Equation 25-13 HCM Page 25-25
            MF[thread][step] = funcMF(thread, period, step);

            //segment flow calculation----------------------------------------------------------------
            SF[thread][step] = funcSF(thread, step);
            NV[thread][step] = funcNV(thread, period, step);
            UV[thread][step] = funcUV(thread, step);

            //TODO test new on ramp delay method
            if (inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                testOnRampDelay[thread][period] += ONRQ[thread][step]; //Each step is 0.25 min //ONRQ[thread][step] / ONRF[thread][step];
            }

            //summary period result, once per analysis period
            if (step == NUM_STEPS - 1) {
                //segment and ramp performance measures---------------------------------------------------
                //Equation 25-30 HCM Page 25-29
                //calculate mainline queue length, ft
                //TODO new method required for KQ calculation /*(KQ[thread][NUM_STEPS - 1] + KQ[thread][NUM_STEPS - 2]) / 2*/
                Q[thread][period] = Math.max(UV[thread][NUM_STEPS - 1] / (KQ[thread][NUM_STEPS - 1] - KB[thread]) * 5280, 0);

                checkMainlineQueueLength(thread, period);

                //calculate deny entry queue length, ft /*(KQ[thread][NUM_STEPS - 1] + KQ[thread][NUM_STEPS - 2]) / 2*/
                if (inUpSeg == null) {
                    scenDenyQ[thread][period] = Math.max(denyEntry[thread]
                            / (KQ[thread][NUM_STEPS - 1] - KB[thread]) * 5280 - inSegLength_ft, 0);
                }

                //mainline volume served, CEHelper.average segment flow vph
                scenMainlineVolume_veh[thread][period] = CEHelper.average(SF[thread]) * T;

                //volume/capacity ratio
                scenVC[thread][period] = scenMainlineVolume_veh[thread][period] / scenMainlineCapacity_veh[thread][period];
                if (scenVC[thread][period] > scenMaxVC[thread]) {
                    scenMaxVC[thread] = scenVC[thread][period];
                }

                switch (inType) {
                    case CEConst.SEG_TYPE_ONR:
                        scenOnVolume_veh[thread][period] = CEHelper.average(ONRF[thread]) * T; //CEHelper.average on ramp flow vph
                        //Equation 25-31 HCM Page 25-29
                        ONRQ_End_veh[thread][period] = ONRQ[thread][NUM_STEPS - 1];
                        ONRQL[thread][period] = funcONRQL(thread, period);
                        //TODO temporary
                        if (testOnRampDelay[thread][period] > CEConst.ZERO) {
                            testOnRampDelay[thread][period]
                                    = testOnRampDelay[thread][period] / 4;// / CEHelper.sum(ONRQ[thread]); // weighted CEHelper.average
                        } else {
                            testOnRampDelay[thread][period] = 0f;
                        }
                        break;
                    case CEConst.SEG_TYPE_OFR:
                        scenOffVolume_veh[thread][period] = CEHelper.average(OFRF[thread]) * T; //CEHelper.average off ramp flow vph
                        break;
                    case CEConst.SEG_TYPE_W:
                    case CEConst.SEG_TYPE_ACS:
                        scenOnVolume_veh[thread][period] = CEHelper.average(ONRF[thread]) * T; //CEHelper.average on ramp flow vph
                        scenOffVolume_veh[thread][period] = CEHelper.average(OFRF[thread]) * T; //CEHelper.average off ramp flow vph
                        //Equation 25-31 HCM Page 25-29
                        ONRQ_End_veh[thread][period] = ONRQ[thread][NUM_STEPS - 1];
                        ONRQL[thread][period] = funcONRQL(thread, period);
                        //TODO temporary
                        if (testOnRampDelay[thread][period] > CEConst.ZERO) {
                            testOnRampDelay[thread][period]
                                    = testOnRampDelay[thread][period] / 4 / CEHelper.sum(ONRQ[thread]); // weighted CEHelper.average
                        } else {
                            testOnRampDelay[thread][period] = 0f;
                        }
                        break;
                    default:

                }

                //TODO check whether the condition is correct
                if (inType != CEConst.SEG_TYPE_R) {
                    if (CEHelper.sum(UV[thread]) > CEConst.ZERO) {
                        //calculate speed and density use over saturated method
                        float NV_average = CEHelper.average(NV[thread]);
                        //TODO need to divide by num of lanes? modified Equation 25-26 to 25-29, HCM Page 25-28
                        scenSpeed[thread][period] = scenMainlineVolume_veh[thread][period] / NV_average * inSegLength_ft / 5280f;
                        scenAllDensity_veh[thread][period] = NV_average / (inSegLength_ft / 5280f) / scenMainlineNumLanes[thread][period];
//                        scenAllDensity_pc[thread][period] = CEHelper.veh_to_pc(scenAllDensity_veh[thread][period], inMainlineFHV[thread][period]);
                        scenIADensity_pc[thread][period]
                                = CEHelper.veh_to_pc(scenAllDensity_veh[thread][period], inMainlineFHV[thread][period]);//scenAllDensity_pc[thread][period];
                    } else {
                        //calculate speed and density use under saturated method with SF_average_per_hour
                        calSpeedAndDensity(scen, atdm, thread, period, CEConst.STATUS_OVER_TO_UNDER);
                    }
                } else {
                    scenSpeed[thread][period] = inUpSeg == null ? 0 : inUpSeg.scenSpeed[thread][period];
                    scenAllDensity_veh[thread][period] = inUpSeg == null ? 0 : inUpSeg.scenAllDensity_veh[thread][period];
                    //scenAllDensity_pc[thread][period]
                    //        = CEHelper.veh_to_pc(scenAllDensity_veh[thread][period], inMainlineFHV[thread][period]);
                }

                //limits for speed
                scenSpeed[thread][period] = Math.min(scenSpeed[thread][period], scenMainlineFFS[thread][period]);
                scenSpeed[thread][period] = Math.min(scenSpeed[thread][period], funcMaxSpeed(thread, period));
                scenSpeed[thread][period] = (float) Math.max(scenSpeed[thread][period], 1);
                checkOverlap(thread, period);
            }

            inOverMode[thread]++;
        } catch (Exception e) {
            System.out.println("runOversaturated " + e.toString());
            e.printStackTrace();
        }
    }

    //helper funtions for oversatuarated----------------------------------------------------
    /**
     * Reset over saturated data for a particular scenario
     *
     * @param thread
     * @param isFirstTime whether it is first time enter over saturated method
     */
    private void resetOversaturated(int thread, boolean isFirstTime) {

        inOverMode[thread] = 0; //track how long (in periods) does a scenario in over sat calculation
        denyEntry[thread] = 0;

        capacityDropFactor[thread] = 1f; //capacity drop factor (for two capacity calculation)

        Arrays.fill(ONRF[thread], 0); //on ramp flow, veh

        Arrays.fill(ONRO[thread], 0); //on ramp output
        Arrays.fill(ONRQ[thread], 0); //on ramp queue?????

        Arrays.fill(OFRF[thread], 0); //off ramp flow, veh

        Arrays.fill(UV[thread], 0); //unserved vehicles
        Arrays.fill(NV[thread], 0); //number of vehicles
        Arrays.fill(KQ[thread], 0); //density

        Arrays.fill(DEF[thread], 0); //deficit (for off ramp calculation)

        Arrays.fill(MI[thread], 0); //mainline input
        Arrays.fill(MO1[thread], Float.MAX_VALUE); //mainline output 1
        Arrays.fill(MO2[thread], Float.MAX_VALUE); //mainline output 2
        Arrays.fill(MO3[thread], Float.MAX_VALUE); //mainline output 3
        Arrays.fill(MF[thread], 0); //mainline flow
        Arrays.fill(SF[thread], 0); //segment flow

        if (isFirstTime) {
            Arrays.fill(ONRQ_End_veh[thread], 0); //on ramp queue
            Arrays.fill(Q[thread], 0); //mainline queue
            Arrays.fill(ONRQL[thread], 0); //on ramp queue length
            Arrays.fill(testOnRampDelay[thread], 0); //new method for on ramp delay calculation
        }
    }

    //helper functions called once per analysis period, require 2 parameters-----------------------------
    /**
     * Calculate ED for a particular period in a particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @return ED
     */
    private float funcED(int thread, int period) {
        float result;
        //Equation 25-3 HCM Page 25-21 vph?
        if (inUpSeg == null) {
            //assume first segment is a basic segment
            result = Math.min(scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread], scenMainlineDemand_veh[thread][period]);
        } else {
            float temp = inUpSeg.ED[thread];
            if (inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                temp += scenOnDemand_veh[thread][period];
            }
            if (inUpSeg.inType == CEConst.SEG_TYPE_OFR || inUpSeg.inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
                temp -= inUpSeg.scenOffDemand_veh[thread][period];
            }
            result = Math.min(scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread], temp);
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
     * @param thread
     * @return KB
     */
    private float funcKB(int scen, int atdm, int thread, int period) {
        float density;
        switch (scenType[thread][period]) {
            case CEConst.SEG_TYPE_B:
            case CEConst.SEG_TYPE_ONR_B:
            case CEConst.SEG_TYPE_OFR_B:
            case CEConst.SEG_TYPE_W_B:
                density = funcBasicDensity(CEConst.STATUS_BG, scen, atdm, thread, period);
                break;
            case CEConst.SEG_TYPE_ONR:
                density = funcOnAllDensity(CEConst.STATUS_BG, thread, period);
                break;
            case CEConst.SEG_TYPE_OFR:
                density = funcOffAllDensity(CEConst.STATUS_BG, thread, period);
                break;
            case CEConst.SEG_TYPE_W:
            case CEConst.SEG_TYPE_ACS:
                density = funcWeaveDensity(CEConst.STATUS_BG, scen, atdm, thread, period);
                break;
            default: //Const.SEG_TYPE_R :
                //assume first segment will never be R
                density = inUpSeg.KB[thread];
        }
        density = (float) Math.min(
                CEHelper.pc_to_veh(KC, inMainlineFHV[thread][period]) * scenMainlineNumLanes[thread][period],
                density);
        return density;
    }

    /**
     * Calculate WTT for a particular period in a particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @return WTT
     */
    private int funcWTT(int thread, int period) {
        //Equation 25-10&11 HCM Page 25-24
        float WS = scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread] / (scenMainlineNumLanes[thread][period] * (KJ - KC));
        return (int) (T * inSegLength_ft / 5280f / WS);
    }

    /**
     * Check whether mainline queue length is less or equal to segment length.
     * If not, add extra to upstream segments
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     */
    private void checkMainlineQueueLength(int thread, int period) {
        if (Q[thread][period] > inSegLength_ft) {
            if (inUpSeg != null) {
                inUpSeg.Q[thread][period] += Q[thread][period] - inSegLength_ft;
                Q[thread][period] = inSegLength_ft;
                inUpSeg.checkMainlineQueueLength(thread, period);
            } else {
                scenDenyQ[thread][period] += Q[thread][period] - inSegLength_ft;
                Q[thread][period] = inSegLength_ft;
            }
        }
    }

    //helper functions called every time step, require 3 parameters----------------------------------------
    //mainline helper functions----------------------------------------------------------------------
    /**
     * Calculate MI for a particular step in a particular period in a particular
     * scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MI
     */
    private float funcMI(int thread, int period, int step) {
        float result;
        //Equation 25-5 HCM Page 25-22
        if (inUpSeg == null) {
            //assume first segment is a basic segment
            if (step == 0) {
                result = funcDummyMF(thread, period, step) + UV[thread][NUM_STEPS - 1];
            } else {
                result = funcDummyMF(thread, period, step) + UV[thread][step - 1];
            }
        } else {
            if (step == 0) {
                result = inUpSeg.MF[thread][step] + ONRF[thread][step]
                        - OFRF[thread][step] + UV[thread][NUM_STEPS - 1];
            } else {
                result = inUpSeg.MF[thread][step] + ONRF[thread][step]
                        - OFRF[thread][step] + UV[thread][step - 1];
            }
        }
        return result;
    }

    /**
     * Calculate Dummy MF (for first segment only) for a particular step in a
     * particular period in a particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return Dummy MF
     */
    private float funcDummyMF(int thread, int period, int step) {
        float expect = scenMainlineDemand_veh[thread][period] / T;

        //limit by capacity (MO1)
        float result = Math.min(scenMainlineDemand_veh[thread][period] / T + denyEntry[thread],
                scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread] / T);

        //additional KQ calculation for first segment
        if (inUpSeg == null) {
            KQ[thread][step] = funcKQ(thread, period, step);
        }

        float result2 = Float.MAX_VALUE;
        //limit by MO2
        if (step == 0) {
            if (UV[thread][NUM_STEPS - 1] > CEConst.ZERO) {
                if (inOverMode[thread] > 1) {
                    result2 = KQ[thread][step] * inSegLength_ft / 5280f + SF[thread][NUM_STEPS - 1]
                            - NV[thread][NUM_STEPS - 1];
                }
            }
        } else {
            if (UV[thread][step - 1] > CEConst.ZERO) {
                result2 = KQ[thread][step] * inSegLength_ft / 5280f + SF[thread][step - 1]
                        - NV[thread][step - 1];
            }
        }

        result2 = Math.max(0, result2);

        result = Math.min(result, result2);

        denyEntry[thread] += expect - result;
        denyEntry[thread] = Math.max(0, denyEntry[thread]);

        return result;
    }

    /**
     * Calculate MO1 for a particular step in a particular period in a
     * particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MO1
     */
    private float funcMO1(int thread, int period, int step) {
        if (inDownSeg != null) {
            //Equation 25-6 HCM Page 25-22
            float result = inDownSeg.scenMainlineCapacity_veh[thread][period] * inDownSeg.capacityDropFactor[thread] / T
                    - inDownSeg.ONRF[thread][step];

            if (step == 0) {
                if (inOverMode[thread] > 1) {
                    //if (MO2[thread][NUM_STEPS - 1] > CEConst.ZERO) {
                    result = Math.min(result, MO2[thread][NUM_STEPS - 1]);
                    //}
                    //if (MO3[thread][NUM_STEPS - 1] > CEConst.ZERO) {
                    result = Math.min(result, MO3[thread][NUM_STEPS - 1]);
                    //}
                }
            } else {
                //TODO copy from VBA, over estimate if ignore
                //if (MO2[thread][step - 1] > CEConst.ZERO) {
                result = Math.min(result, MO2[thread][step - 1]);
                //}
                //if (MO3[thread][step - 1] > CEConst.ZERO) {
                result = Math.min(result, MO3[thread][step - 1]);
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
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return KQ
     */
    private float funcKQ(int thread, int period, int step) {
        float result;
        float testSF = SF[thread][(step + NUM_STEPS - 1) % NUM_STEPS];
        //Equation 25-7 HCM Page 25-23
        if (step == 0) {
            if (inOverMode[thread] > 1) {
                result = KJ - (KJ - KC) * testSF / (scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread] / T);
            } else {
                result = KC;
            }
        } else {
            result = KJ - (KJ - KC) * testSF / (scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread] / T);
        }

        //return density for all lanes
        return result * scenMainlineNumLanes[thread][period];
    }

    /**
     * Calculate MO2 for a particular step in a particular period in a
     * particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MO2
     */
    private float funcMO2(int thread, int period, int step) {
        //TODO according to VBA code, only need when previous right UV > 0
        if (inDownSeg != null) {
            float result;
            inDownSeg.KQ[thread][step] = inDownSeg.funcKQ(thread, period, step); //queue density
            //Equation 25-8 HCM Page 25-23
            if (step == 0) {
                if (inDownSeg.UV[thread][NUM_STEPS - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }

                if (inOverMode[thread] > 1) {
                    result = inDownSeg.KQ[thread][step] * inDownSeg.inSegLength_ft / 5280f + inDownSeg.SF[thread][NUM_STEPS - 1]
                            - inDownSeg.ONRF[thread][step] - inDownSeg.NV[thread][NUM_STEPS - 1];
                } else {
                    return Float.MAX_VALUE;
                }
            } else {
                if (inDownSeg.UV[thread][step - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }

                result = inDownSeg.KQ[thread][step] * inDownSeg.inSegLength_ft / 5280f + inDownSeg.SF[thread][step - 1]
                        - inDownSeg.ONRF[thread][step] - inDownSeg.NV[thread][step - 1];
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
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MO3
     */
    private float funcMO3(int thread, int period, int step) {
        if (inDownSeg != null && isFrontClearQueue(thread, period, step)) {
            if (step == 0) {
                if (inDownSeg.UV[thread][NUM_STEPS - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }
            } else {
                if (inDownSeg.UV[thread][step - 1] < CEConst.ZERO) {
                    return Float.MAX_VALUE;
                }
            }

            int _WTT = inDownSeg.WTT[thread];
            //Equation 25-12 HCM Page 25-24
            float result;
            if (step < _WTT) {
                if (inOverMode[thread] > _WTT && _WTT < NUM_STEPS && period > 0) {
                    //assume WTT < numOfSteps
                    result = inDownSeg.scenMainlineCapacity_veh[thread][period - 1] * inDownSeg.capacityDropFactor[thread] / T; //4
                    if (inDownSeg != null) {
                        result = Math.min(inDownSeg.MO1[thread][step + NUM_STEPS - _WTT], result); //1
                        result = Math.min(inDownSeg.MO2[thread][step + NUM_STEPS - _WTT] + inDownSeg.OFRF[thread][step + NUM_STEPS - _WTT], result); //2
                        result = Math.min(inDownSeg.MO3[thread][step + NUM_STEPS - _WTT] + inDownSeg.OFRF[thread][step + NUM_STEPS - _WTT], result); //3
                        if (inDownSeg.inDownSeg != null) {
                            result = Math.min(inDownSeg.inDownSeg.scenMainlineCapacity_veh[thread][period - 1] * inDownSeg.inDownSeg.capacityDropFactor[thread] / T
                                    + inDownSeg.OFRF[thread][step + NUM_STEPS - _WTT], result); //5
                        }
                    }
                } else {
                    return Float.MAX_VALUE;
                }
            } else {
                result = inDownSeg.scenMainlineCapacity_veh[thread][period] * inDownSeg.capacityDropFactor[thread] / T; //4
                if (inDownSeg != null) {
                    result = Math.min(inDownSeg.MO1[thread][step - _WTT], result); //1
                    result = Math.min(inDownSeg.MO2[thread][step - _WTT] + inDownSeg.OFRF[thread][step - _WTT], result); //2
                    result = Math.min(inDownSeg.MO3[thread][step - _WTT] + inDownSeg.OFRF[thread][step - _WTT], result); //3
                    if (inDownSeg.inDownSeg != null) {
                        result = Math.min(inDownSeg.inDownSeg.scenMainlineCapacity_veh[thread][period] * inDownSeg.inDownSeg.capacityDropFactor[thread] / T
                                + inDownSeg.OFRF[thread][step - _WTT], result); //5
                    }
                }
            }
            result -= inDownSeg.ONRF[thread][step];
            return result;
        } else {
            return Float.MAX_VALUE;
        }
    }

    /**
     * Check whether it is front clear queue for a particular step in a
     * particular period in a particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return whether it is front clear queue
     */
    private boolean isFrontClearQueue(int thread, int period, int step) {
        if (inDownSeg == null) {
            return false;
        } else {
            GPMLSegment segment = inDownSeg;
            while (!segment.scenIsFrontClearingQueues[thread][period]
                    && segment.UV[thread][(step + NUM_STEPS - 1) % NUM_STEPS] > 0
                    && segment.inDownSeg != null) {
                segment = segment.inDownSeg;
            }
            return segment.scenIsFrontClearingQueues[thread][period];
        }
    }

    /**
     * Calculate MF for a particular step in a particular period in a particular
     * scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return MF
     */
    private float funcMF(int thread, int period, int step) {
        //Equation 25-13 HCM Page 25-25
        float result = MI[thread][step];

        //if (MO1[thread][step] > CEConst.ZERO) {
        result = Math.min(MO1[thread][step], result);
        //}
        //if (MO2[thread][step] > CEConst.ZERO) {
        result = Math.min(MO2[thread][step], result);
        //}

        //if (MO3[thread][step] > CEConst.ZERO) {
        result = Math.min(MO3[thread][step], result);
        //}

        result = Math.min(scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread] / T, result);

        if (inDownSeg != null) {
            result = Math.min(inDownSeg.scenMainlineCapacity_veh[thread][period] * inDownSeg.capacityDropFactor[thread] / T, result);
        }

        return result;
    }

    //on ramp helper functions----------------------------------------------------------------------
    /**
     * Calculate ONRI for a particular step in a particular period in a
     * particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return ONRI
     */
    private float funcONRI(int thread, int period, int step) {
        float ONRD = 0;
        if (inType == CEConst.SEG_TYPE_ONR) {
            ONRD = scenOnDemand_veh[thread][period] / T;
        }
        if (inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
            ONRD = scenOnDemand_veh[thread][period] / T;
        }
        //Equation 25-14 HCM Page 25-25
        if (step == 0) {
            if (inOverMode[thread] > 1) {
                return ONRD + ONRQ[thread][NUM_STEPS - 1];
            } else {
                return ONRD;
            }
        } else {
            return ONRD + ONRQ[thread][step - 1];
        }
    }

    /**
     * Calculate ONRO for a particular step in a particular period in a
     * particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return ONRO
     */
    private float funcONRO(int thread, int period, int step) {
        //Equation 25-15 HCM Page 25-26
        float result = Math.min(scenRM_veh[thread][period], scenOnCapacity_veh[thread][period] / T); //1 and 2

        float onr2 = scenMainlineCapacity_veh[thread][period] * capacityDropFactor[thread] / T; //3
        //assume left segment exists since ONRF is only called by left segment
        if (step == 0) {
            if (inOverMode[thread] > 1) {
                float _ONRF = ONRF[thread][NUM_STEPS - 1]
                        * scenOnDemand_veh[thread][period]
                        / scenOnDemand_veh[thread][period - 1];

                //TODO only need when MO2 > 0 and MO3 > 0 ??
                if (/*MO2[thread][NUM_STEPS - 1] > CEConst.ZERO && */MO2[thread][NUM_STEPS - 1] < Float.MAX_VALUE) {
                    onr2 = Math.min(MF[thread][NUM_STEPS - 1] + _ONRF, onr2);
                }
                if (/*MO3[thread][NUM_STEPS - 1] > CEConst.ZERO &&*/inUpSeg.MO3[thread][NUM_STEPS - 1] < Float.MAX_VALUE) {
                    onr2 = Math.min(inUpSeg.MO3[thread][NUM_STEPS - 1] + _ONRF, onr2);
                }
            }
        } else {
            //TODO unclear, need to check
            if (/*MO2[thread][step - 1] > CEConst.ZERO && */MO2[thread][step - 1] < Float.MAX_VALUE) {
                onr2 = Math.min(MF[thread][step - 1] + ONRF[thread][step - 1], onr2);
            }
            if (/*MO3[thread][step - 1] > CEConst.ZERO && */inUpSeg.MO3[thread][step - 1] < Float.MAX_VALUE) {
                onr2 = Math.min(inUpSeg.MO3[thread][step - 1] + ONRF[thread][step - 1], onr2);
            }
        }
        onr2 = Math.max(onr2 - inUpSeg.MI[thread][step], onr2 / 2.0f / scenMainlineNumLanes[thread][period]);

        result = Math.min(result, onr2);

        result = Math.max(result, 0);
        return result;
    }

    /**
     * Calculate ONRF for a particular step in a particular period in a
     * particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return ONRF
     */
    private float funcONRF(int thread, int period, int step) {
        if (inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
            float ONRI = funcONRI(thread, period, step);
            //float ONRO = funcONRO(thread, period, step);
            ONRO[thread][step] = funcONRO(thread, period, step);
            if (ONRO[thread][step] >= ONRI) {
                ONRQ[thread][step] = 0f;
                //Equation 25-16 HCM Page 25-26
                return ONRI;
            } else {
                //update ONRQ
                //Equation 25-18 HCM Page 25-26
                ONRQ[thread][step] = ONRI - ONRO[thread][step];
                //Equation 25-17 HCM Page 25-26
                return ONRO[thread][step];
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
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return DEF
     */
    private float funcDEF(int thread, int period, int step) {
        //Equation 25-19 HCM Page 25-27
        if (inUpSeg == null) {
            //assume first segment basic, and will never have deficit
            return 0;
        } else {
            //TODO different from HCM, need to check
            float result;
            if (step == 0) {
                if (inOverMode[thread] > 1) {
                    //since DEF is calculated first, MF and ONRF are from previous analysis period
                    result = scenMainlineDemand_veh[thread][period - 1] / T * NUM_STEPS
                            - CEHelper.sum(inUpSeg.MF[thread]) - CEHelper.sum(ONRF[thread]);
                } else {
                    result = 0;
                }
            } else {
                float actualFlow = inUpSeg.MF[thread][step - 1] + ONRF[thread][step - 1];
                result = DEF[thread][step - 1] - actualFlow;
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
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return OFRF
     */
    private float funcOFRF(int thread, int period, int step) {
        if (inType == CEConst.SEG_TYPE_OFR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS) {
            //inUpSeg.MF and ONRF were calculated before OFRF
            if (period == 0) {
                float OFRD_p = scenOffDemand_veh[thread][period]; //no convert since it is ratio
                return (inUpSeg.MF[thread][step] + ONRF[thread][step]) * OFRD_p / scenMainlineDemand_veh[thread][period];
            } else {
                //no convert since it is ratio
                float OFRD_p = scenOffDemand_veh[thread][period];
                float OFRD_p_1 = scenOffDemand_veh[thread][period - 1];

                if (DEF[thread][step] > CEConst.ZERO) {
                    if (inUpSeg.MF[thread][step] + ONRF[thread][step] <= DEF[thread][step]) {
                        //Equation 25-20 HCM Page 25-27
                        return (inUpSeg.MF[thread][step] + ONRF[thread][step]) * OFRD_p_1 / scenMainlineDemand_veh[thread][period - 1];
                    } else {
                        //Equation 25-21 HCM Page 25-27
                        return DEF[thread][step] * OFRD_p_1 / scenMainlineDemand_veh[thread][period - 1]
                                + (inUpSeg.MF[thread][step] + ONRF[thread][step]
                                - DEF[thread][step]) * OFRD_p / scenMainlineDemand_veh[thread][period];
                    }
                } else {
                    //no deficit
                    //Equation 25-22 HCM Page 25-27
                    return (inUpSeg.MF[thread][step] + ONRF[thread][step]) * OFRD_p / scenMainlineDemand_veh[thread][period];
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
     * @param thread
     * @param step time step index (0 is the first step)
     * @return SF
     */
    private float funcSF(int thread, int step) {
        //Equation 25-23 HCM Page 25-28
        return MF[thread][step] + OFRF[thread][step];
    }

    /**
     * Calculate NV for a particular step in a particular period in a particular
     * scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @param step time step index (0 is the first step)
     * @return NV
     */
    private float funcNV(int thread, int period, int step) {
        //Equation 25-24 HCM Page 25-28
        float result;
        if (step == 0) {
            //Equation 25-4 HCM Page 25-2
            if (inOverMode[thread] > 1) {
                //NV 0
                result = KB[thread] * inSegLength_ft / 5280f + UV[thread][NUM_STEPS - 1];
            } else {
                result = KB[thread] * inSegLength_ft / 5280f;
            }
        } else {
            result = NV[thread][step - 1];
        }

        if (inUpSeg != null) {
            result += inUpSeg.MF[thread][step];
        } else {
            result += funcDummyMF(thread, period, step);
        }
        result += ONRF[thread][step] - MF[thread][step] - OFRF[thread][step];

        return result;
    }

    /**
     * Calculate UV for a particular step in a particular period in a particular
     * scenario
     *
     * @param thread
     * @param step time step index (0 is the first step)
     * @return UV
     */
    private float funcUV(int thread, int step) {
        //Equation 25-25 HCM Page 25-28
        float value = Math.max(NV[thread][step] - KB[thread] * inSegLength_ft / 5280f, 0);
        return value;
    }

    /**
     * Calculate ONRQL for a particular step in a particular period in a
     * particular scenario
     *
     * @param thread
     * @param period analysis period index (0 is the first analysis period)
     * @return ONRQL
     */
    private float funcONRQL(int thread, int period) {
        float RM = scenRM_veh[thread][period] / T;
        float ONRC = scenOnCapacity_veh[thread][period] / T;
        float result = ONRQ[thread][NUM_STEPS - 1]
                / (KJ - Math.min(RM, ONRO[thread][NUM_STEPS - 1]) * (KJ - KC) / ONRC)
                * 5280;

        return CEHelper.veh_to_pc(result, inMainlineFHV[thread][period]);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EXTEND RESULTS">
    /**
     * Extend output results based on output speed and density
     *
     * @param thread
     */
    void calExtendedResults(int thread) {
        for (int period = 0; period < inNumPeriod; period++) {
//            scenDensityLOS[thread][period] = funcDensityLOS(thread, period)(thread, period);
//            scenDemandLOS[thread][period] = funcDemandLOS(thread, period);
//            scenActualTime[thread][period]
//                    = inSegLength_ft / 5280f / scenSpeed[thread][period] * 60;
//            scenFFSTime[thread][period]
//                    = inSegLength_ft / 5280f / (inMainlineFFS.get(period) * inUSAF.get(period)) * 60;
//            scenMainlineDelay[thread][period]
//                    = scenActualTime[thread][period] - scenFFSTime[thread][period];

            float scenActualTime
                    = inSegLength_ft / 5280f / scenSpeed[thread][period] * 60;
            float scenFFSTime
                    = inSegLength_ft / 5280f / (inMainlineFFS.get(period) * inUSAF.get(period)) * 60;
            float scenMainlineDelay
                    = scenActualTime/*[thread][period]*/ - scenFFSTime/*[thread][period]*/;

            //TODO use new test method for on ramp delay
            scenOnDelay[thread][period]
                    = inType == CEConst.SEG_TYPE_ONR || inType == CEConst.SEG_TYPE_W || inType == CEConst.SEG_TYPE_ACS
                    ? testOnRampDelay[thread][period] : 0;
            scenSysDelay[thread][period]
                    = scenMainlineDelay/*[thread][period]*/ + scenOnDelay[thread][period];
            //TODO VMTD, VMTV only consider mainline???
            scenVMTD[thread][period]
                    = scenMainlineDemand_veh[thread][period] * inSegLength_ft / 5280f / 4f;
            scenVMTV[thread][period]
                    = scenMainlineVolume_veh[thread][period] * inSegLength_ft / 5280f / 4f;
            scenVHT[thread][period]
                    = scenActualTime/*[thread][period]*/ * scenMainlineVolume_veh[thread][period] / 240;
            scenVHD[thread][period]
                    = scenMainlineDelay/*[thread][period]*/ * scenMainlineVolume_veh[thread][period] / 240
                    + scenOnDelay[thread][period] * scenOnVolume_veh[thread][period] / 240;
//            scenTTI[thread][period]
//                    = scenActualTime[thread][period] / scenFFSTime[thread][period];
        }
    }

    /**
     * Calculate density based level of service
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return density based level of service
     */
    String funcDensityLOS(int thread, int period) {
        if (scenType[thread][period] == CEConst.SEG_TYPE_B || scenType[thread][period] == CEConst.SEG_TYPE_ONR_B
                || scenType[thread][period] == CEConst.SEG_TYPE_OFR_B || scenType[thread][period] == CEConst.SEG_TYPE_W_B
                || scenType[thread][period] == CEConst.SEG_TYPE_R) {
            float density_pc = CEHelper.veh_to_pc(scenAllDensity_veh[thread][period], inMainlineFHV[thread][period]);//scenAllDensity_pc[thread][period];
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
                    = (scenType[thread][period] == CEConst.SEG_TYPE_W
                    ? CEHelper.veh_to_pc(scenAllDensity_veh[thread][period], inMainlineFHV[thread][period])
                    : scenIADensity_pc[thread][period]);
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
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return demand based level of service
     */
    String funcDemandLOS(int thread, int period) {
        return scenMainlineDemand_veh[thread][period] / scenMainlineCapacity_veh[thread][period]/*scenDC[thread][period]*/ > 1 ? "F" : "";
    }

    /**
     * Getter for scenario DC
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return DC
     */
    float getScenDC(int thread, int period) {
        return scenMainlineDemand_veh[thread][period] / scenMainlineCapacity_veh[thread][period];
    }

    /**
     * Getter for scenario ActualTime
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return ActualTime
     */
    float getScenActualTime(int thread, int period) {
        return inSegLength_ft / 5280f / scenSpeed[thread][period] * 60;
    }

    /**
     * Getter for scenario FFSTime
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return FFSTime
     */
    float getScenFFSTime(int thread, int period) {
        return inSegLength_ft / 5280f / (inMainlineFFS.get(period) * inUSAF.get(period)) * 60;
    }

    /**
     * Getter for scenario MainlineDelay
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return MainlineDelay
     */
    float getScenMainlineDelay(int thread, int period) {
        return getScenActualTime(thread, period) - getScenFFSTime(thread, period);
    }

    /**
     * Getter for scenario TTI
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return TTI
     */
    float getScenTTI(int thread, int period) {
        return getScenActualTime(thread, period) / getScenFFSTime(thread, period);
    }

    /**
     * Getter for scenario AllDensity_pc
     *
     * @param thread
     * @param period analysis period index (0 is the first period)
     * @return AllDensity_pc
     */
    float getScenAllDensity_pc(int thread, int period) {
        return CEHelper.veh_to_pc(scenAllDensity_veh[thread][period], inMainlineFHV[thread][period]);
    }
    // </editor-fold>
}
