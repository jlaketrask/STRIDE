/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreEngine.reliabilityAnalysis.DataStruct;

import coreEngine.Helper.CEDate;
import coreEngine.Seed;
import java.io.Serializable;

/**
 *
 * @author jltrask
 */
public class WorkZoneData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8234793115000L;

    /**
     *
     */
    private static Seed seed;

    // Dates
    /**
     *
     */
    private CEDate startDate;

    /**
     *
     */
    private CEDate endDate;

    //Periods
    /**
     *
     */
    private int startPeriod;

    /**
     *
     */
    private int endPeriod;

    //Segments
    /**
     *
     */
    private int startSegment;

    /**
     *
     */
    private int endSegment;

    //Severity
    /**
     *
     */
    private int severity;

    /**
     *
     * @param dates
     * @param segments
     * @param periods
     * @param severity
     */
    public WorkZoneData(CEDate[] dates, int[] segments, int[] periods, int severity) {
        this.startDate = dates[0];
        this.endDate = dates[1];
        this.startSegment = segments[0];
        this.endSegment = segments[1];
        this.startPeriod = periods[0];
        this.endPeriod = periods[1];
        this.severity = severity;

    }

    /**
     *
     * @param dates
     * @param segments
     * @param periods
     * @param severity
     */
    public WorkZoneData(CEDate[] dates, Integer[] segments, Integer[] periods, int severity) {
        this.startDate = dates[0];
        this.endDate = dates[1];
        this.startSegment = segments[0];
        this.endSegment = segments[1];
        this.startPeriod = periods[0];
        this.endPeriod = periods[1];
        this.severity = severity;

    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param startSegment
     * @param endSegment
     * @param startPeriod
     * @param endPeriod
     * @param severity
     */
    public WorkZoneData(CEDate startDate, CEDate endDate, int startSegment, int endSegment, int startPeriod, int endPeriod, int severity) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startSegment = startSegment;
        this.endSegment = endSegment;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.severity = severity;

    }

    @Override
    public String toString() {
        String severityString = "";
        switch (severity) {
            case 0:
                severityString = "Shoulder Closure";
                break;
            case 1:
                severityString = "1 Lane Closure";
                break;
            case 2:
                severityString = "2 Lane Closure";
                break;
            case 3:
                severityString = "3+ Lane Closure";
                break;
        }
        return severityString + ": " + startDate.toWorkZoneString() + " - " + endDate.toWorkZoneString()
                + "  (Seg. " + startSegment + " - " + endSegment + ")"
                + "  (Per. " + startPeriod + " - " + endPeriod + ")";
    }

    /**
     *
     * @param wz
     * @return
     */
    public boolean hasOverlap(WorkZoneData wz) {
        boolean hasOverlap = false;
        boolean hasDateOverlap = false;
        boolean hasDateAndSegmentOverlap = false;
        if (this.startDate.isBeforeOrSameAs(wz.startDate) && this.endDate.isAfterOrSameAs(wz.startDate)) {
            hasDateOverlap = true;
        } else if (this.startDate.isAfter(wz.startDate) && this.startDate.isBeforeOrSameAs(wz.endDate)) {
            hasDateOverlap = true;
        }

        // If dates overlap, check if segments overlap
        if (hasDateOverlap) {
            if (this.startSegment <= wz.startSegment && this.endSegment >= wz.startSegment) {
                hasDateAndSegmentOverlap = true;
            } else if (this.startSegment > wz.startSegment && this.startSegment <= wz.endSegment) {
                hasDateAndSegmentOverlap = true;
            }
        }

        // If necessary, check period overlap
        if (hasDateAndSegmentOverlap) {
            if (this.startPeriod <= wz.startPeriod && this.endPeriod >= wz.startPeriod) {
                hasOverlap = true;
            } else if (this.startPeriod > wz.startPeriod && this.startPeriod <= wz.endPeriod) {
                hasOverlap = true;
            }
        }

        return hasOverlap;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     *
     * @return
     */
    public CEDate getStartDate() {
        return this.startDate;
    }

    /**
     *
     * @return
     */
    public CEDate getEndDate() {
        return this.endDate;
    }

    /**
     *
     * @return
     */
    public CEDate[] getDates() {
        return new CEDate[]{startDate, endDate};
    }

    /**
     *
     * @return
     */
    public int getStartSegment() {
        return this.startSegment;
    }

    /**
     *
     * @return
     */
    public int getEndSegment() {
        return this.endSegment;
    }

    /**
     *
     * @return
     */
    public int[] getSegments() {
        return new int[]{startSegment, endSegment};
    }

    /**
     *
     * @return
     */
    public Integer[] getSegmentsForSeed() {
        return new Integer[]{startSegment, endSegment};
    }

    /**
     *
     * @return
     */
    public int getStartPeriod() {
        return this.startPeriod;
    }

    /**
     *
     * @return
     */
    public int getEndPeriod() {
        return this.endPeriod;
    }

    /**
     *
     * @return
     */
    public int[] getPeriods() {
        return new int[]{startPeriod, endPeriod};
    }

    /**
     *
     * @return
     */
    public Integer[] getPeriodsForSeed() {
        return new Integer[]{startPeriod, endPeriod};
    }

    /**
     * Returns the daily duration of the work zone in number of periods
     *
     * @return int numPeriods
     */
    public int getDuration() {
        return (this.endPeriod - this.startPeriod) + 1;
    }

    /**
     *
     * @return
     */
    public int getSeverity() {
        return this.severity;
    }

    /**
     *
     * @return
     */
    public String getSeverityString() {
        switch (this.severity) {
            case 0:
                return "Shoulder Closure";
            default:
                return this.severity + " Lane Closure";
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Setters">
    /**
     *
     * @param date
     */
    public void setStartDate(CEDate date) {
        this.startDate = date;
    }

    /**
     *
     * @param date
     */
    public void setEndDate(CEDate date) {
        this.endDate = date;
    }

    /**
     *
     * @param startDate
     * @param endDate
     */
    public void setDates(CEDate startDate, CEDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     *
     * @param dates
     */
    public void setDates(CEDate[] dates) {
        this.startDate = dates[0];
        this.endDate = dates[1];
    }

    /**
     *
     * @param segment
     */
    public void setStartSegment(int segment) {
        this.startSegment = segment;
    }

    /**
     *
     * @param segment
     */
    public void setEndSegment(int segment) {
        this.endSegment = segment;
    }

    /**
     *
     * @param startSegment
     * @param endSegment
     */
    public void setSegments(int startSegment, int endSegment) {
        this.startSegment = startSegment;
        this.endSegment = endSegment;
    }

    /**
     *
     * @param segments
     */
    public void setSegments(int[] segments) {
        this.startSegment = segments[0];
        this.endSegment = segments[1];
    }

    /**
     *
     * @param period
     */
    public void setStartPeriod(int period) {
        this.startPeriod = period;
    }

    /**
     *
     * @param period
     */
    public void setEndPeriod(int period) {
        this.endPeriod = period;
    }

    /**
     *
     * @param startPeriod
     * @param endPeriod
     */
    public void setPeriods(int startPeriod, int endPeriod) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    /**
     *
     * @param periods
     */
    public void setPeriods(int[] periods) {
        this.startPeriod = periods[0];
        this.endPeriod = periods[1];
    }

    /**
     *
     * @param severity
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Static Getters">
    /**
     *
     * @param adjFacType 0 - FFSAF, 1 - CAF, 2 - DAF, 3 - LAF
     * @param incType 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 -
     * 3 lane closure, 4 - 4 lane closure
     * @param numLanes
     * @return
     */
    public static float getAdjFactor(int adjFacType, int incType, int numLanes) {
        switch (adjFacType) {
            case 0: // FFSAF
                return getFFSAF(incType, numLanes);
            case 1: // CAF
                return getCAF(incType, numLanes);
            case 2: // DAF
                return getDAF(incType, numLanes);
            case 3: // LAF - cast int to float
                return (float) getLAF(incType, numLanes);
            default:
                return -1.0f;
        }
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @return
     */
    public static float getFFSAF(int incType, int numLanes) {
        return workZoneFFSAFs[numLanes][incType];
    }

    /**
     * Get method to return the float[][] of workZone FFSAFs
     *
     * @return float[][] workZoneFFSAFs
     */
    public static float[][] getFFSAF() {
        return workZoneFFSAFs;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @return
     */
    public static float getCAF(int incType, int numLanes) {
        return workZoneCAFs[numLanes][incType];
    }

    /**
     * Get method to return the float[][] of workZone CAFs
     *
     * @return float[][] workZoneCAFs
     */
    public static float[][] getCAF() {
        return workZoneCAFs;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @return
     */
    public static float getDAF(int incType, int numLanes) {
        return workZoneDAFs[numLanes][incType];
    }

    /**
     * Get method to return the float[][] of workZone DAFs
     *
     * @return float[][] workZoneDAFs
     */
    public static float[][] getDAF() {
        return workZoneDAFs;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @return
     */
    public static int getLAF(int incType, int numLanes) {
        return workZoneLAFs[numLanes][incType];
    }

    /**
     * Get method to get the int[][] array of workZone LAFs.
     *
     * @return int[][] workZone LAFs
     */
    public static int[][] getLAF() {
        return workZoneLAFs;
    }

    /**
     *
     * @param incType
     * @return
     */
    public static String getTypeFull(int incType) {
        switch (incType) {
            case 0:
                return "Shoulder closure";
            case 1:
                return "One lane closure";
            case 2:
                return "Two lane closure";
            case 3:
                return "Three lane closure";
            default:
                return "Four or more lane closure";
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Static Setters">
    /**
     *
     * @param newSeed
     */
    public static void setSeed(Seed newSeed) {
        seed = newSeed;
        //0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 - 3 lane closure, 4 - 4 lane closure
        if (seed.getWorkZoneSAFs() != null) {
            setFFSAF(seed.getWorkZoneSAFs());
        }
        if (seed.getWorkZoneCAFs() != null) {
            setCAF(seed.getWorkZoneCAFs());
        }
        if (seed.getWorkZoneDAFs() != null) {
            setDAF(seed.getWorkZoneDAFs());
        }
        if (seed.getWorkZoneLAFs() != null) {
            setLAF(seed.getWorkZoneLAFs());
        }
    }

    /**
     *
     * @param adjFacType 0 - FFSAF, 1 - CAF, 2 - DAF, 3 - LAF
     * @param incType 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 -
     * 3 lane closure, 4 - 4 lane closure
     * @param numLanes
     * @param newVal
     */
    public static void setAdjFactor(int adjFacType, int incType, int numLanes, float newVal) {
        switch (adjFacType) {
            case 0: // FFSAF
                setFFSAF(incType, numLanes, newVal);
                break;
            case 1: // CAF
                setCAF(incType, numLanes, newVal);
                break;
            case 2: // DAF
                setDAF(incType, numLanes, newVal);
                break;
            case 3: // LAF - cast int to float
                setLAF(incType, numLanes, (int) newVal);
                break;
            default:
                System.err.println("Invalid adjustment factor type specified. Value not set.");
                break;

        }
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @param newVal
     */
    public static void setFFSAF(int incType, int numLanes, float newVal) {
        workZoneFFSAFs[numLanes][incType] = newVal;
    }

    /**
     * Assigns a full array to the static workZoneFFSAFs array;
     *
     * @param newVals
     */
    public static void setFFSAF(float[][] newVals) {
        for (int incType = 0; incType < workZoneFFSAFs[0].length; incType++) {
            for (int numLanes = 0; numLanes < workZoneFFSAFs.length; numLanes++) {
                setFFSAF(incType, numLanes, newVals[numLanes][incType]);
            }
        }
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @param newVal
     */
    public static void setCAF(int incType, int numLanes, float newVal) {
        workZoneCAFs[numLanes][incType] = newVal;
    }

    /**
     * Assigns a full array to the static workZoneFFSAFs array;
     *
     * @param newVals
     */
    public static void setCAF(float[][] newVals) {
        for (int incType = 0; incType < workZoneCAFs[0].length; incType++) {
            for (int numLanes = 0; numLanes < workZoneCAFs.length; numLanes++) {
                setCAF(incType, numLanes, newVals[numLanes][incType]);
            }
        }
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @param newVal
     */
    public static void setDAF(int incType, int numLanes, float newVal) {
        workZoneDAFs[numLanes][incType] = newVal;
    }

    /**
     * Assigns a full array to the static workZoneFFSAFs array;
     *
     * @param newVals
     */
    public static void setDAF(float[][] newVals) {
        for (int incType = 0; incType < workZoneDAFs[0].length; incType++) {
            for (int numLanes = 0; numLanes < workZoneDAFs.length; numLanes++) {
                setDAF(incType, numLanes, newVals[numLanes][incType]);
            }
        }
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @param newVal
     */
    public static void setLAF(int incType, int numLanes, int newVal) {
        workZoneLAFs[numLanes][incType] = newVal;
    }

    /**
     * Assigns a full array to the static workZoneFFSAFs array;
     *
     * @param newVals
     */
    public static void setLAF(int[][] newVals) {
        for (int incType = 0; incType < workZoneLAFs[0].length; incType++) {
            for (int numLanes = 0; numLanes < workZoneLAFs.length; numLanes++) {
                setLAF(incType, numLanes, newVals[numLanes][incType]);
            }
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Fill Presets">
    /**
     *
     */
    private static void useDefaultAdjFactors() {
        useDefaultFFSAFs();
        useDefaultCAFs();
        useDefaultDAFs();
        useDefaultLAFs();
    }

    /**
     * Method to set default values for a particular adjustment factor type. 0 -
     * SAF (FFSAF), 1 - CAF, 2 - DAF, 3 - LAF
     *
     * @param adjFactor Integer of adjustment factor to fill with default
     * values.
     */
    private static void useDefaultAdjFactors(int adjFactor) {
        switch (adjFactor) {
            case 0:
                useDefaultFFSAFs();
                break;
            case 1:
                useDefaultCAFs();
                break;
            case 2:
                useDefaultDAFs();
                break;
            case 3:
                useDefaultLAFs();
                break;
            default:
                System.err.println("Invalid Adjustment Factor type specified.");
                break;
        }
    }

    /**
     *
     */
    private static void useDefaultFFSAFs() {
        float[][] tempFFSAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };
        //System.out.println(workZoneFFSAFs[0].length);
        //System.out.println(workZoneFFSAFs.length);
        for (int incType = 0; incType < workZoneFFSAFs.length; incType++) {
            for (int lane = 0; lane < workZoneFFSAFs[0].length; lane++) {
                //System.out.println("Lane: "+lane+"   incType:" +incType);
                workZoneFFSAFs[lane][incType] = tempFFSAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private static void useDefaultCAFs() {
        float[][] tempCAFs = {{0.81f, 0.70f, 0.70f, 0.70f, 0.70f},
        {0.83f, 0.74f, 0.51f, 0.51f, 0.51f},
        {0.85f, 0.77f, 0.50f, 0.52f, 0.52f},
        {0.87f, 0.81f, 0.67f, 0.50f, 0.50f},
        {0.89f, 0.85f, 0.75f, 0.52f, 0.52f},
        {0.91f, 0.88f, 0.80f, 0.63f, 0.63f},
        {0.93f, 0.89f, 0.84f, 0.66f, 0.66f}
        };

        for (int incType = 0; incType < workZoneCAFs.length; incType++) {
            for (int lane = 0; lane < workZoneCAFs[0].length; lane++) {
                workZoneCAFs[lane][incType] = tempCAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private static void useDefaultDAFs() {
        float[][] tempDAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };

        for (int incType = 0; incType < workZoneDAFs.length; incType++) {
            for (int lane = 0; lane < workZoneDAFs[0].length; lane++) {
                workZoneDAFs[lane][incType] = tempDAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private static void useDefaultLAFs() {
        int[][] tempLAFs = {{0, -1, -1, -1, -1},
        {0, -1, -2, -2, -2},
        {0, -1, -2, -3, -3},
        {0, -1, -2, -3, -4},
        {0, -1, -2, -3, -4},
        {0, -1, -2, -3, -4},
        {0, -1, -2, -3, -4}
        };
//
//        int[][] tempLAFs =     {{0,-1,-2,-2,-2},
//                                {0,-1,-2,-3,-3},
//                                {0,-1,-2,-3,-4},
//                                {0,-1,-2,-3,-4},
//                                {0,-1,-2,-3,-4},
//                                {0,-1,-2,-3,-4},
//                                {0,-1,-2,-3,-4}
//                               };

//        int[][] tempLAFs =     {{0,0,0,0,0},
//                                {0,0,0,0,0},
//                                {0,0,0,0,0},
//                                {0,0,0,0,0},
//                                {0,0,0,0,0},
//                                {0,0,0,0,0},
//                                {0,0,0,0,0}
//                               };
        for (int incType = 0; incType < workZoneLAFs.length; incType++) {
            for (int lane = 0; lane < workZoneLAFs[0].length; lane++) {
                workZoneLAFs[lane][incType] = tempLAFs[lane][incType];
            }
        }
    }

    // </editor-fold>
    // 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 - 3 lane closure, 4 - 4 lane closure
    /**
     *
     */
    private static final float[][] workZoneFFSAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
    }; // 5 is workZone type, 7 is number of lanes;

    /**
     *
     */
    private static final float[][] workZoneCAFs = {{0.81f, 0.70f, 0.70f, 0.70f, 0.70f},
    {0.83f, 0.74f, 0.51f, 0.51f, 0.51f},
    {0.85f, 0.77f, 0.50f, 0.52f, 0.52f},
    {0.87f, 0.81f, 0.67f, 0.50f, 0.50f},
    {0.89f, 0.85f, 0.75f, 0.52f, 0.52f},
    {0.91f, 0.88f, 0.80f, 0.63f, 0.63f},
    {0.93f, 0.89f, 0.84f, 0.66f, 0.66f}
    }; // 5 is workZone type, 7 is number of lanes;

    /**
     *
     */
    private static final float[][] workZoneDAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
    {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
    }; // 5 is workZone type, 7 is number of lanes;

    /**
     *
     */
    private static final int[][] workZoneLAFs = {{0, -1, -1, -1, -1},
    {0, -1, -2, -2, -2},
    {0, -1, -2, -3, -3},
    {0, -1, -2, -3, -4},
    {0, -1, -2, -3, -4},
    {0, -1, -2, -3, -4},
    {0, -1, -2, -3, -4}
    }; // 5 is workZone type, 7 is number of lanes;

}
