/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliabilityAnalysis.DataStruct;

import coreEngine.CEConst;
import coreEngine.Seed;

/**
 *
 * @author Lake and tristan
 */
public class DemandData {

    private final float[][] demands = new float[12][7];
    private final boolean[] activeDays = new boolean[7];
    private final boolean[] activeMonths = new boolean[12];

    private int year;
    private int startMonth;
    private int startDay;
    private int endMonth;
    private int endDay;

    private Seed seed;

    /**
     *
     */
    public DemandData() {
        //useAllDays();
        useWeekDays();
        useAllMonths();
    }

    /**
     *
     * @param year
     * @param startMonth
     * @param startDay
     * @param endMonth
     * @param endDay
     */
    public DemandData(int year, int startMonth, int startDay, int endMonth, int endDay) {
        //useAllDays();
        useWeekDays();
                //useAllMonths();

        this.year = year;
        this.startMonth = startMonth; // Indexing starts at 1
        this.startDay = startDay; // Indexing starts at 1
        this.endMonth = endMonth; // Indexing starts at 1
        this.endDay = endDay; // Indexing starts at 1

        // Setting active months
        for (int i = 0; i < 12; ++i) {
            if (i < startMonth - 1 || i > endMonth - 1) {
                activeMonths[i] = false;
            } else {
                activeMonths[i] = true;
            }
        }
    }

    /**
     *
     * @param seed
     */
    public DemandData(Seed seed) {
		//useAllDays();
        //useWeekDays();
        //useAllMonths();

        this.seed = seed;
        this.year = seed.getSeedFileDate().year;
        this.startMonth = seed.getRRPStartDate().month;
        this.startDay = seed.getRRPStartDate().day;
        this.endMonth = seed.getRRPEndDate().month;
        this.endDay = seed.getRRPEndDate().day;

        // Setting active days
        for (int i = 0; i < 7; i++) {
            if (seed.getWeekdayUsed()[i]) {
                activeDays[i] = true;
            } else {
                activeDays[i] = false;
            }
        }

        // Setting active months
        for (int i = 0; i < 12; ++i) {
            if (i < startMonth - 1 || i > endMonth - 1) {
                activeMonths[i] = false;
            } else {
                activeMonths[i] = true;
            }
        }

        // Setting demand table values
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 7; j++) {
                demands[i][j] = seed.getSpecifiedDemand()[i][j];
            }
        }

                // Comment out for not i40 data
        //useI40Defaults();
    }

    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     *
     * @return
     */
    public int getYear() {
        return this.year;
    }

    /**
     *
     * @return
     */
    public int getStartMonth() {
        return this.startMonth;
    }

    /**
     *
     * @return
     */
    public int getStartDay() {
        return this.startDay;
    }

    /**
     *
     * @return
     */
    public int getEndMonth() {
        return this.endMonth;
    }

    /**
     *
     * @return
     */
    public int getEndDay() {
        return this.endDay;
    }

    /**
     *
     * @return
     */
    public boolean[] getActiveDays() {
        return activeDays;
    }
    
    /**
     * Returns the number of active days
     * @return int numDaysActive
     */
    public int getActiveDaysCount() {
        int numDaysActive = 0;
        for (int day = 0; day < activeDays.length; day++) {
            if (activeDays[day]) {
                numDaysActive++;
            }
        }
        return numDaysActive;
    }

    /**
     *
     * @return
     */
    public boolean[] getActiveMonths() {
        return activeMonths;
    }

    /**
     *
     * @param day
     * @return
     */
    public boolean getDayActive(int day) {
        if (day >= 0 && day < 7) {
            return activeDays[day];
        }
        return false;
    }

    /**
     *
     * @return
     */
    public float getMaxValue() {
        float currMax = -99999.0f;
        for (int month = 0; month < 12; ++month) {
            for (int day = 0; day < 7; ++day) {
                if (activeMonths[month] && activeDays[day] && demands[month][day] > currMax) {
                    currMax = demands[month][day];
                }
            }
        }
        return currMax;
    }

    /**
     *
     * @return
     */
    public float getMinValue() {
        float currMin = 99999.0f;
        for (int month = 0; month < 12; ++month) {
            for (int day = 0; day < 7; ++day) {
                if (activeMonths[month] && activeDays[day] && demands[month][day] < currMin) {
                    currMin = demands[month][day];
                }
            }
        }
        return currMin;
    }

    /**
     *
     * @param month
     * @return
     */
    public boolean getMonthActive(int month) {
        if (month >= 0 && month < 12) {
            return activeMonths[month];
        }
        return false;
    }

    /**
     *
     * @param month
     * @param day
     * @return
     */
    public float getValue(int month, int day) {
        if (month >= 0 && month < 12
                && day >= 0 && day < 7) {
            return demands[month][day];
        }
        return 0.0f;

    }

    /**
     *
     * @return
     */
    public float[][] getSpecifiedDemand() {
        return demands;
    }

    /**
     *
     * @return
     */
    public float getSeedTotalVMT() {
        return seed.getValueFloat(CEConst.IDS_SP_VMTV, 0, 0, 0, -1);
    }

    // </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setters">
    /**
     *
     * @param month
     */
    public void setStartMonth(int month) {
        if (month >= 1 && month <= 12) {
            startMonth = month;
        }
    }

    /**
     *
     * @param day
     */
    public void setStartDay(int day) {
        if (day >= 1 && day <= 31) {
            startDay = day;
        }
    }

    /**
     *
     * @param month
     */
    public void setEndMonth(int month) {
        if (month >= 1 && month <= 12) {
            endMonth = month;
        }
    }

    /**
     *
     * @param day
     */
    public void setEndDay(int day) {
        if (day >= 1 && day <= 31) {
            endDay = day;
        }
    }

    /**
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     *
     * @param day
     * @param active
     */
    public void setDayActive(int day, boolean active) {
        if (day >= 0 && day < 7) {
            activeDays[day] = active;
        }
    }

    /**
     *
     * @param month
     * @param active
     */
    public void setMonthActive(int month, boolean active) {
        if (month >= 0 && month < 12) {
            activeMonths[month] = active;
        }
    }

    /**
     *
     * @param month
     * @param day
     * @param val
     */
    public void setValue(int month, int day, float val) {
        if (month >= 0 && month < 12
                && day >= 0 && day < 7
                && val >= 0.0f) {
            demands[month][day] = val;
        }
    }

    // </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Fill Defaults">
    /**
     *
     */
    public void useUrbanDefaults() {
        for (int month = 0; month < 12; ++month) {
            for (int day = 0; day < 7; ++day) {
                demands[month][day] = urbanDefaultValues[month][day];
            }
        }
    }

    public void useI40Defaults() {
        for (int month = 0; month < 12; month++) {
            for (int day = 0; day < 7; day++) {
                demands[month][day] = i40DefaultValues[month][day];
            }
        }
    }

    /**
     *
     */
    public void useRuralDefaults() {
        for (int month = 0; month < 12; ++month) {
            for (int day = 0; day < 7; ++day) {
                demands[month][day] = ruralDefaultValues[month][day];
            }
        }
    }

    /**
     *
     */
    public void useFacilitySpecificDefaults() {
        for (int month = 0; month < 12; ++month) {
            for (int day = 0; day < 7; ++day) {
                demands[month][day] = 1.0f;
            }
        }
    }

    /**
     *
     */
    public void useSeedValues() {
        for (int month = 0; month < 12; ++month) {
            for (int day = 0; day < 7; ++day) {
                demands[month][day] = seed.getSpecifiedDemand()[month][day];
            }
        }
    }

    /**
     *
     */
    public final void useAllDays() {
        for (int day = 0; day < 7; ++day) {
            activeDays[day] = true;
        }
    }

    /**
     *
     */
    public final void useWeekDays() {
        for (int day = 0; day < 5; ++day) {
            activeDays[day] = true;
        }
    }

    /**
     *
     */
    public final void useAllMonths() {
        for (int month = 0; month < 12; ++month) {
            activeMonths[month] = true;
        }
    }

    private final float urbanDefaultValues[][] = new float[][]{
        {0.822158f, 0.822158f, 0.838936f, 0.864104f, 0.964777f, 0.830547f, 0.729875f},
        {0.848710f, 0.848710f, 0.866031f, 0.892012f, 0.995936f, 0.857371f, 0.753447f},
        {0.920502f, 0.920502f, 0.939288f, 0.967466f, 1.080181f, 0.929895f, 0.817180f},
        {0.975575f, 0.975575f, 0.995484f, 1.025349f, 1.144807f, 0.985529f, 0.866071f},
        {0.973608f, 0.973608f, 0.993477f, 1.023281f, 1.142499f, 0.983542f, 0.864325f},
        {1.021796f, 1.021796f, 1.042649f, 1.073929f, 1.199047f, 1.032223f, 0.907105f},
        {1.132925f, 1.132925f, 1.156046f, 1.190728f, 1.329453f, 1.144486f, 1.005760f},
        {1.032614f, 1.032614f, 1.053688f, 1.085299f, 1.211741f, 1.043151f, 0.916708f},
        {1.063101f, 1.063101f, 1.084797f, 1.117341f, 1.247516f, 1.073949f, 0.943773f},
        {0.995243f, 0.995243f, 1.015554f, 1.046021f, 1.167888f, 1.005399f, 0.883532f},
        {0.995243f, 0.995243f, 1.015554f, 1.046021f, 1.167888f, 1.005399f, 0.883532f},
        {0.978525f, 0.978525f, 0.998495f, 1.028450f, 1.148269f, 0.988510f, 0.868690f}
    };

    private final float ruralDefaultValues[][] = new float[][]{
        {0.710902f, 0.680969f, 0.695936f, 0.733352f, 0.868049f, 0.785734f, 0.755801f},
        {0.787988f, 0.754810f, 0.771399f, 0.812872f, 0.962175f, 0.870934f, 0.837756f},
        {0.881252f, 0.844147f, 0.862700f, 0.909081f, 1.076055f, 0.974016f, 0.936910f},
        {0.945966f, 0.906136f, 0.926051f, 0.975839f, 1.155075f, 1.045542f, 1.005711f},
        {1.034472f, 0.990915f, 1.012694f, 1.067140f, 1.263145f, 1.143364f, 1.099807f},
        {1.051602f, 1.007324f, 1.029463f, 1.084811f, 1.284062f, 1.162297f, 1.118019f},
        {1.182934f, 1.133126f, 1.158030f, 1.220289f, 1.444424f, 1.307453f, 1.257645f},
        {1.082056f, 1.036496f, 1.059276f, 1.116226f, 1.321247f, 1.195957f, 1.150396f},
        {1.034472f, 0.990915f, 1.012694f, 1.067140f, 1.263145f, 1.143364f, 1.099807f},
        {0.947870f, 0.907959f, 0.927914f, 0.977802f, 1.157399f, 1.047645f, 1.007735f},
        {0.926933f, 0.887904f, 0.907418f, 0.956204f, 1.131834f, 1.024505f, 0.985476f},
        {0.829862f, 0.794920f, 0.812391f, 0.856068f, 1.013305f, 0.917216f, 0.882274f}
    };

    private final float i40DefaultValues[][] = new float[][]{
        {0.996623f, 1.027775f, 1.040394f, 1.052601f, 1.081612f, 0.0f, 0.0f},
        {0.939253f, 1.010728f, 1.039214f, 1.092029f, 1.140072f, 0.0f, 0.0f},
        {1.043305f, 1.069335f, 1.063524f, 1.110921f, 1.171121f, 0.0f, 0.0f},
        {1.073578f, 1.087455f, 1.098238f, 1.161974f, 1.215002f, 0.0f, 0.0f},
        {1.076331f, 1.106182f, 1.113955f, 1.157717f, 1.210434f, 0.0f, 0.0f},
        {1.078043f, 1.085853f, 1.067470f, 1.138720f, 1.180327f, 0.0f, 0.0f},
        {1.082580f, 1.070993f, 1.102512f, 1.147279f, 1.184981f, 0.0f, 0.0f},
        {1.046045f, 1.052146f, 1.060371f, 1.093243f, 1.164901f, 0.0f, 0.0f},
        {1.016023f, 1.024051f, 1.023625f, 1.074782f, 1.152946f, 0.0f, 0.0f},
        {1.048981f, 1.045723f, 1.066986f, 1.107044f, 1.160954f, 0.0f, 0.0f},
        {0.974044f, 0.999947f, 1.041211f, 1.081541f, 1.070354f, 0.0f, 0.0f},
        {0.974785f, 0.956475f, 0.987019f, 0.916107f, 1.007695f, 0.0f, 0.0f},};

    // </editor-fold>
}
