/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreEngine.reliabilityAnalysis.DataStruct;

import coreEngine.Seed;

/**
 *
 * @author Lake and tristan
 */
public class IncidentData {

    private final float[][] incidentProbabilities;

    private final float[] incidentFreqMonth;  // Total number of incidents in a month

    private final float[] incidentDistribution;

    private final float[][] incidentDurationInfo;

    private final float[][] incidentFFSAFs;

    private final float[][] incidentCAFs;

    private final float[][] incidentDAFs;

    private final int[][] incidentLAFs;

    private float crashRateRatio;

    private Seed seed;

    private final int modelType;

    /**
     *
     */
    public final static int TYPE_GP = 0;

    /**
     *
     */
    public final static int TYPE_ML = 1;

    /**
     *
     * @param type
     */
    public IncidentData(int type) {
        if (type == TYPE_GP || type == TYPE_ML) {
            this.modelType = type;
        } else {
            throw new RuntimeException("Invalid IncidentData type.");
        }

        incidentProbabilities = new float[12][6];
        incidentFreqMonth = new float[12];

        incidentDistribution = new float[5];
        incidentDurationInfo = new float[5][4];

        // 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 - 3 lane closure, 4 - 4 lane closure
        incidentFFSAFs = new float[5][7]; // 5 is incident type, 8 is number of lanes
        incidentCAFs = new float[5][7]; // 5 is incident type, 8 is number of lanes
        incidentDAFs = new float[5][7]; // 5 is incident type, 8 is number of lanes
        incidentLAFs = new int[5][7]; // 5 is incident type, 8 is number of lanes

        crashRateRatio = 4.9f;

        useDefaultFrequencies();
        if (type == TYPE_GP) {
            useNationalDefaultDistribution();
            useDefaultAdjFactors();
        } else {
            useMLDefaultDistribution();
            useMLDefaultAdjFactors();
        }

    }

    /**
     *
     * @param seed
     * @param type
     */
    public IncidentData(Seed seed, int type) {

        if (type == TYPE_GP || type == TYPE_ML) {
            this.modelType = type;
        } else {
            throw new RuntimeException("Invalid IncidentData type.");
        }

        this.seed = seed;
        incidentProbabilities = new float[12][6];

        // Reading in data from seed (if data exists)
        incidentFreqMonth = new float[12];
        if (type == TYPE_GP) {
            setIncidentFrequency(seed.getGPIncidentFrequency());

            if (seed.getGPIncidentDistribution() == null) {
                incidentDistribution = new float[5];
                useNationalDefaultDistribution();
            } else {
                incidentDistribution = seed.getGPIncidentDistribution();
            }
            if (seed.getGPIncidentDuration() == null) {
                incidentDurationInfo = new float[5][4];
                useDefaultDuration();
            } else {
                incidentDurationInfo = seed.getGPIncidentDuration();
            }

            // 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 - 3 lane closure, 4 - 4 lane closure
            if (seed.getGPIncidentSAF() == null) {
                incidentFFSAFs = new float[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(0);
            } else {
                incidentFFSAFs = seed.getGPIncidentSAF();
            }
            if (seed.getGPIncidentCAF() == null) {
                incidentCAFs = new float[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(1);
            } else {
                incidentCAFs = seed.getGPIncidentCAF();
            }
            if (seed.getGPIncidentDAF() == null) {
                incidentDAFs = new float[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(2);
            } else {
                incidentDAFs = seed.getGPIncidentDAF();
            }
            if (seed.getGPIncidentLAF() == null) {
                incidentLAFs = new int[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(3);
            } else {
                incidentLAFs = seed.getGPIncidentLAF();
            }

            if (seed.getGPIncidentCrashRatio() == 0.0f) {
                crashRateRatio = 4.9f; // National default
            } else {
                crashRateRatio = seed.getGPIncidentCrashRatio();
            }
        } else {
            setIncidentFrequency(seed.getMLIncidentFrequency());

            if (seed.getMLIncidentDistribution() == null) {
                incidentDistribution = new float[5];
                useMLDefaultDistribution();
            } else {
                incidentDistribution = seed.getMLIncidentDistribution();
            }
            if (seed.getMLIncidentDuration() == null) {
                incidentDurationInfo = new float[5][4];
                useDefaultDuration();
            } else {
                incidentDurationInfo = seed.getMLIncidentDuration();
            }

            // 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 - 3 lane closure, 4 - 4 lane closure
            if (seed.getMLIncidentSAF() == null) {
                incidentFFSAFs = new float[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(0);
            } else {
                incidentFFSAFs = seed.getMLIncidentSAF();
            }
            if (seed.getMLIncidentCAF() == null) {
                incidentCAFs = new float[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(1);
            } else {
                incidentCAFs = seed.getMLIncidentCAF();
            }
            if (seed.getMLIncidentDAF() == null) {
                incidentDAFs = new float[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(2);
            } else {
                incidentDAFs = seed.getMLIncidentDAF();
            }
            if (seed.getMLIncidentLAF() == null) {
                incidentLAFs = new int[5][7]; // 5 is incident type, 7 is number of lanes (2 lanes to 8 lanes)
                useDefaultAdjFactors(3);
            } else {
                incidentLAFs = seed.getMLIncidentLAF();
            }

            if (seed.getMLIncidentCrashRatio() == 0.0f) {
                crashRateRatio = 4.9f; // National default
            } else {
                crashRateRatio = seed.getMLIncidentCrashRatio();
            }
        }

        if (type == TYPE_GP) {
            //useNationalDefaultDistribution();
            //useDefaultAdjFactors();
        } else {
            //useMLDefaultDistribution();
            //useMLDefaultAdjFactors();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Get method for return the crash rate ratio for incident scenario
     * generation
     *
     * @return float crashRateRatio
     */
    public float getCrashRateRatio() {
        return crashRateRatio;
    }

    /**
     * Getter for the incident probabilities (deprecated).
     *
     * @param incidentType
     * @param month
     * @return
     */
    public float getIncidentProbability(int incidentType, int month) {
        return incidentProbabilities[month][incidentType];
    }

    /**
     * Getter for the expected frequency of incidents in a given month.
     *
     * @param month 0 - January, 1 - February, etc.
     * @return
     */
    public float getIncidentFrequencyMonth(int month) {
        return incidentFreqMonth[month];
    }

    /**
     *
     * @return
     */
    public float[] getIncidentFrequencyArr() {
        return incidentFreqMonth;
    }

    /**
     *
     * @param incidentType
     * @return
     */
    public float getIncidentDistribution(int incidentType) {
        return incidentDistribution[incidentType];
    }

    /**
     *
     * @param incidentType
     * @return
     */
    public float getIncidentDistributionDecimal(int incidentType) {
        return incidentDistribution[incidentType] / 100.0f;
    }

    /**
     *
     * @return
     */
    public float[] getIncidentDistribution() {
        return incidentDistribution;
    }

    /**
     *
     * @param incidentType
     * @return
     */
    public float getIncidentDurMin(int incidentType) {
        return incidentDurationInfo[incidentType][2];
    }

    /**
     *
     * @param incidentType
     * @return
     */
    public float getIncidentDurMax(int incidentType) {
        return incidentDurationInfo[incidentType][3];
    }

    /**
     *
     * @return
     */
    public float getIncidentDistributionSum() {
        float sum = 0.0f;
        for (int i = 0; i < incidentDistribution.length; i++) {
            sum += incidentDistribution[i];
        }

        return sum;
    }

    /**
     *
     * @param incidentType
     * @return
     */
    public float getIncidentDuration(int incidentType) {
        return incidentDurationInfo[incidentType][0];
    }

    /**
     *
     * @return
     */
    public float[][] getIncidentDurationInfo() {
        return incidentDurationInfo;
    }

    /**
     *
     * @param incidentType
     * @return
     */
    public float getIncidentDurationStdDev(int incidentType) {
        return incidentDurationInfo[incidentType][1];
    }

    /**
     *
     * @param adjFacType 0 - FFSAF, 1 - CAF, 2 - DAF, 3 - LAF
     * @param incType 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 -
     * 3 lane closure, 4 - 4 lane closure
     * @param numLanes
     * @return
     */
    public float getIncidentAdjFactor(int adjFacType, int incType, int numLanes) {
        switch (adjFacType) {
            case 0: // FFSAF
                return getIncidentFFSAF(incType, numLanes);
            case 1: // CAF
                return getIncidentCAF(incType, numLanes);
            case 2: // DAF
                return getIncidentDAF(incType, numLanes);
            case 3: // LAF - cast int to float
                return (float) getIncidentLAF(incType, numLanes);
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
    public float getIncidentFFSAF(int incType, int numLanes) {
        return incidentFFSAFs[incType][numLanes];
    }

    /**
     * Get method to return the float[][] of incident FFSAFs
     *
     * @return float[][] incidentFFSAFs
     */
    public float[][] getIncidentFFSAF() {
        return incidentFFSAFs;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @return
     */
    public float getIncidentCAF(int incType, int numLanes) {
        return incidentCAFs[incType][numLanes];
    }

    /**
     * Get method to return the float[][] of incident CAFs
     *
     * @return float[][] incidentCAFs
     */
    public float[][] getIncidentCAF() {
        return incidentCAFs;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @return
     */
    public float getIncidentDAF(int incType, int numLanes) {
        return incidentDAFs[incType][numLanes];
    }

    /**
     * Get method to return the float[][] of incident DAFs
     *
     * @return float[][] incidentDAFs
     */
    public float[][] getIncidentDAF() {
        return incidentDAFs;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @return
     */
    public int getIncidentLAF(int incType, int numLanes) {
        return incidentLAFs[incType][numLanes];
    }

    /**
     * Get method to get the int[][] array of incident LAFs.
     *
     * @return int[][] incident LAFs
     */
    public int[][] getIncidentLAF() {
        return incidentLAFs;
    }

    /**
     *
     * @param incType
     * @return
     */
    public static String getIncidentTypeFull(int incType) {
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

    /**
     *
     */
    public void useFrequenciesFromSeed() {
        if (this.seed != null) {
            float[] tempFreqArr;
            if (modelType == TYPE_GP) {
                tempFreqArr = seed.getGPIncidentFrequency();
            } else {
                tempFreqArr = seed.getMLIncidentFrequency();
            }
            if (tempFreqArr != null) {
                for (int month = 0; month < tempFreqArr.length; month++) {
                    this.incidentFreqMonth[month] = tempFreqArr[month];
                    //System.out.println(incidentFreqMonth[month]);
                }
            } else {
                System.out.println("Seed frequency array is null");
            }
        } else {
            System.out.println("Seed is null");
        }
    }

    /**
     *
     * @return
     */
    public int getModelType() {
        return modelType;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Setters">

    /**
     *
     * @param newVal
     */
        public void setCrashRateRatio(float newVal) {
        this.crashRateRatio = newVal;
    }

    /**
     *
     * @param incidentType
     * @param demandPattern
     * @param newValue
     */
    public void setIncidentProbability(int incidentType, int demandPattern, float newValue) {
        incidentProbabilities[demandPattern][incidentType] = newValue;
    }

    /**
     *
     * @param month
     * @param newValue
     */
    public void setIncidentFrequencyMonth(int month, float newValue) {
        incidentFreqMonth[month] = newValue;
    }

    /**
     *
     * @param incidentFrequency
     */
    public void setIncidentFrequency(float[] incidentFrequency) {
        if (incidentFrequency != null) {
            for (int month = 0; month < incidentFrequency.length; month++) {
                this.incidentFreqMonth[month] = incidentFrequency[month];
            }
        }
    }

    /**
     *
     * @param incidentType
     * @param newValue
     */
    public void setIncidentDistribution(int incidentType, float newValue) {
        incidentDistribution[incidentType] = newValue;
    }

    /**
     *
     * @param incidentType
     * @param newValue
     */
    public void setIncidentDurMin(int incidentType, float newValue) {
        incidentDurationInfo[incidentType][2] = newValue;
    }

    /**
     *
     * @param incidentType
     * @param newValue
     */
    public void setIncidentDurMax(int incidentType, float newValue) {
        incidentDurationInfo[incidentType][3] = newValue;
    }

    /**
     *
     * @param incidentType
     * @param newValue
     */
    public void setIncidentDuration(int incidentType, float newValue) {
        incidentDurationInfo[incidentType][0] = newValue;
    }

    /**
     *
     * @param incidentType
     * @param newValue
     */
    public void setIncidentDurationStdDev(int incidentType, float newValue) {
        incidentDurationInfo[incidentType][1] = newValue;
    }

    /**
     *
     * @param adjFacType 0 - FFSAF, 1 - CAF, 2 - DAF, 3 - LAF
     * @param incType 0 - Shoulder, 1 - 1 lane closure, 2 - 2 lane closure, 3 -
     * 3 lane closure, 4 - 4 lane closure
     * @param numLanes
     * @param newVal
     */
    public void setIncidentAdjFactor(int adjFacType, int incType, int numLanes, float newVal) {
        switch (adjFacType) {
            case 0: // FFSAF
                setIncidentFFSAF(incType, numLanes, newVal);
                break;
            case 1: // CAF
                setIncidentCAF(incType, numLanes, newVal);
                break;
            case 2: // DAF
                setIncidentDAF(incType, numLanes, newVal);
                break;
            case 3: // LAF - cast int to float
                setIncidentLAF(incType, numLanes, (int) newVal);
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
    public void setIncidentFFSAF(int incType, int numLanes, float newVal) {
        incidentFFSAFs[incType][numLanes] = newVal;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @param newVal
     */
    public void setIncidentCAF(int incType, int numLanes, float newVal) {
        incidentCAFs[incType][numLanes] = newVal;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @param newVal
     */
    public void setIncidentDAF(int incType, int numLanes, float newVal) {
        incidentDAFs[incType][numLanes] = newVal;
    }

    /**
     *
     * @param incType
     * @param numLanes
     * @param newVal
     */
    public void setIncidentLAF(int incType, int numLanes, int newVal) {
        incidentLAFs[incType][numLanes] = newVal;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Fill presets">
    /**
     *
     */
    public void useDefaultFrequencies() {
        incidentFreqMonth[0] = 0.0f;
        incidentFreqMonth[1] = 0.0f;
        incidentFreqMonth[2] = 0.0f;
        incidentFreqMonth[3] = 0.0f;
        incidentFreqMonth[4] = 0.0f;
        incidentFreqMonth[5] = 0.0f;
        incidentFreqMonth[6] = 0.0f;
        incidentFreqMonth[7] = 0.0f;
        incidentFreqMonth[8] = 0.0f;
        incidentFreqMonth[9] = 0.0f;
        incidentFreqMonth[10] = 0.0f;
        incidentFreqMonth[11] = 0.0f;
    }

    /**
     *
     */
    public void useOldI40DefaultFrequencies() {
        incidentFreqMonth[0] = 3.267957f;
        incidentFreqMonth[1] = 3.38f;
        incidentFreqMonth[2] = 3.67f;
        incidentFreqMonth[3] = 3.89f;
        incidentFreqMonth[4] = 3.88f;
        incidentFreqMonth[5] = 4.07f;
        incidentFreqMonth[6] = 4.51f;
        incidentFreqMonth[7] = 4.11f;
        incidentFreqMonth[8] = 4.24f;
        incidentFreqMonth[9] = 3.97f;
        incidentFreqMonth[10] = 3.97f;
        incidentFreqMonth[11] = 3.90f;
    }

    /**
     *
     */
    public void useDefaultDistribution() {

        // Setting default distribution
        incidentDistribution[0] = 75.0f;
        incidentDistribution[1] = 20.0f;
        incidentDistribution[2] = 5.0f;
        incidentDistribution[3] = 0.0f;
        incidentDistribution[4] = 0.0f;

        // Setting default durations and duration standard deviations
        //useDefaultDuration();
    }

    /**
     *
     */
    public void useNationalDefaultDistribution() {

        // Setting default distribution
        incidentDistribution[0] = 75.4f;
        incidentDistribution[1] = 19.60f;
        incidentDistribution[2] = 3.10f;
        incidentDistribution[3] = 1.90f;
        incidentDistribution[4] = 0.0f;

        // Setting default durations and duration standard deviations
        //useDefaultDuration();
    }

    /**
     *
     */
    public void useMLDefaultDistribution() {

        // Setting default distribution
        incidentDistribution[0] = 75.0f;
        incidentDistribution[1] = 25.0f;
        incidentDistribution[2] = 0.0f;
        incidentDistribution[3] = 0.0f;
        incidentDistribution[4] = 0.0f;

        // Setting default durations and duration standard deviations
        //useDefaultDuration();
    }

    /**
     *
     */
    public void useDefaultDuration() {
        incidentDurationInfo[0][0] = 34.0f;
        incidentDurationInfo[1][0] = 34.6f;
        incidentDurationInfo[2][0] = 53.6f;
        incidentDurationInfo[3][0] = 67.9f;
        incidentDurationInfo[4][0] = 67.9f;

        incidentDurationInfo[0][1] = 15.1f;
        incidentDurationInfo[1][1] = 13.8f;
        incidentDurationInfo[2][1] = 13.9f;
        incidentDurationInfo[3][1] = 21.9f;
        incidentDurationInfo[4][1] = 21.9f;

        incidentDurationInfo[0][2] = 8.7f;
        incidentDurationInfo[1][2] = 16.0f;
        incidentDurationInfo[2][2] = 30.5f;
        incidentDurationInfo[3][2] = 36.0f;
        incidentDurationInfo[4][2] = 36.0f;

        incidentDurationInfo[0][3] = 58.0f;
        incidentDurationInfo[1][3] = 58.2f;
        incidentDurationInfo[2][3] = 66.9f;
        incidentDurationInfo[3][3] = 93.3f;
        incidentDurationInfo[4][3] = 93.3f;

    }

    private void useDefaultAdjFactors() {
        useDefaultFFSAFs();
        useDefaultCAFs();
        useDefaultDAFs();
        useDefaultLAFs();
    }

    private void useMLDefaultAdjFactors() {
        useMLDefaultFFSAFs();
        useMLDefaultCAFs();
        useMLDefaultDAFs();
        useMLDefaultLAFs();
    }

    /**
     * Method to set default values for a particular adjustment factor type. 0 -
     * SAF (FFSAF), 1 - CAF, 2 - DAF, 3 - LAF
     *
     * @param adjFactor Integer of adjustment factor to fill with default
     * values.
     */
    private void useDefaultAdjFactors(int adjFactor) {
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

    private void useDefaultFFSAFs() {
        float[][] tempFFSAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };
        //System.out.println(incidentFFSAFs[0].length);
        //System.out.println(incidentFFSAFs.length);
        for (int incType = 0; incType < incidentFFSAFs.length; incType++) {
            for (int lane = 0; lane < incidentFFSAFs[0].length; lane++) {
                //System.out.println("Lane: "+lane+"   incType:" +incType);
                incidentFFSAFs[incType][lane] = tempFFSAFs[lane][incType];
            }
        }
    }

    private void useDefaultCAFs() {
        float[][] tempCAFs = {{0.81f, 0.70f, 0.70f, 0.70f, 0.70f},
        {0.83f, 0.74f, 0.51f, 0.51f, 0.51f},
        {0.85f, 0.77f, 0.50f, 0.52f, 0.52f},
        {0.87f, 0.81f, 0.67f, 0.50f, 0.50f},
        {0.89f, 0.85f, 0.75f, 0.52f, 0.52f},
        {0.91f, 0.88f, 0.80f, 0.63f, 0.63f},
        {0.93f, 0.89f, 0.84f, 0.66f, 0.66f}
        };

        for (int incType = 0; incType < incidentCAFs.length; incType++) {
            for (int lane = 0; lane < incidentCAFs[0].length; lane++) {
                incidentCAFs[incType][lane] = tempCAFs[lane][incType];
            }
        }
    }

    private void useDefaultDAFs() {
        float[][] tempDAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };

        for (int incType = 0; incType < incidentDAFs.length; incType++) {
            for (int lane = 0; lane < incidentDAFs[0].length; lane++) {
                incidentDAFs[incType][lane] = tempDAFs[lane][incType];
            }
        }
    }

    private void useDefaultLAFs() {
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
        for (int incType = 0; incType < incidentLAFs.length; incType++) {
            for (int lane = 0; lane < incidentLAFs[0].length; lane++) {
                incidentLAFs[incType][lane] = tempLAFs[lane][incType];
            }
        }
    }

    private void useMLDefaultFFSAFs() {
        float[][] tempFFSAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };
        //System.out.println(incidentFFSAFs[0].length);
        //System.out.println(incidentFFSAFs.length);
        for (int incType = 0; incType < incidentFFSAFs.length; incType++) {
            for (int lane = 0; lane < incidentFFSAFs[0].length; lane++) {
                //System.out.println("Lane: "+lane+"   incType:" +incType);
                incidentFFSAFs[incType][lane] = tempFFSAFs[lane][incType];
            }
        }
    }

    private void useMLDefaultCAFs() {
        float[][] tempCAFs = {{0.81f, 0.70f, 0.70f, 0.70f, 0.70f},
        {0.83f, 0.74f, 0.51f, 0.51f, 0.51f},
        {0.85f, 0.77f, 0.50f, 0.52f, 0.52f},
        {0.87f, 0.81f, 0.67f, 0.50f, 0.50f},
        {0.89f, 0.85f, 0.75f, 0.52f, 0.52f},
        {0.91f, 0.88f, 0.80f, 0.63f, 0.63f},
        {0.93f, 0.89f, 0.84f, 0.66f, 0.66f}
        };

        for (int incType = 0; incType < incidentCAFs.length; incType++) {
            for (int lane = 0; lane < incidentCAFs[0].length; lane++) {
                incidentCAFs[incType][lane] = tempCAFs[lane][incType];
            }
        }
    }

    private void useMLDefaultDAFs() {
        float[][] tempDAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };

        for (int incType = 0; incType < incidentDAFs.length; incType++) {
            for (int lane = 0; lane < incidentDAFs[0].length; lane++) {
                incidentDAFs[incType][lane] = tempDAFs[lane][incType];
            }
        }
    }

    private void useMLDefaultLAFs() {
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
        for (int incType = 0; incType < incidentLAFs.length; incType++) {
            for (int lane = 0; lane < incidentLAFs[0].length; lane++) {
                incidentLAFs[incType][lane] = tempLAFs[lane][incType];
            }
        }
    }

    // </editor-fold>
}
