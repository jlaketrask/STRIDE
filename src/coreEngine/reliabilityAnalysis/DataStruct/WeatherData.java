package coreEngine.reliabilityAnalysis.DataStruct;

import coreEngine.Helper.CEConst;
import coreEngine.Helper.CETime;
import coreEngine.Seed;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author tristan and lake
 */
public class WeatherData {

    /**
     *
     */
    public static final int MediumRain = 0;

    /**
     *
     */
    public static final int HeavyRain = 1;

    /**
     *
     */
    public static final int LightSnow = 2;

    /**
     *
     */
    public static final int LightMediumSnow = 3;

    /**
     *
     */
    public static final int MediumHeavySnow = 4;

    /**
     *
     */
    public static final int HeavySnow = 5;

    /**
     *
     */
    public static final int SevereCold = 6;

    /**
     *
     */
    public static final int LowVisibility = 7;

    /**
     *
     */
    public static final int VeryLowVisibility = 8;

    /**
     *
     */
    public static final int MinimumVisibility = 9;

    /**
     *
     */
    public static final int NormalWeather = 10;

    /**
     *
     */
    public static final int AF_TYPE_CAF = 0;

    /**
     *
     */
    public static final int AF_TYPE_DAF = 1;

    public static final int AF_TYPE_SAF = 2;

    private final float[][] weatherProbability;

    private final float[] averageDuration;

    //private final float[][] adjustmentFactors;
    private final float[] weatherCAFs;
    private final float[] weatherDAFs;
    private final float[] weatherSAFs;

    private final float[] defaultCAF;

    private int seedDefaultFFS;

    private final boolean[] monthActive;

    private String nearestMetroArea;

    /**
     *
     */
    public WeatherData() {

        weatherProbability = new float[12][11];
        averageDuration = new float[10];
        //adjustmentFactors = new float[3][11];
        weatherCAFs = new float[11];
        weatherDAFs = new float[11];
        weatherSAFs = new float[11];
        //defaultCAF = new float[] {92.76f, 85.87f, 95.71f, 91.34f, 88.96f, 77.57f, 91.55f, 90.33f, 88.33f, 89.51f, 100.0f};
        defaultCAF = new float[11];
        monthActive = new boolean[12];

        seedDefaultFFS = 70;

        initializeFields();

    }

    public void initializeBySeed(Seed seed) {
        int speedSum = 0;
        for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
            //System.out.println(seed.getValueInt(CEConst.IDS_MAIN_FREE_FLOW_SPEED, seg));
            speedSum += seed.getValueInt(CEConst.IDS_MAIN_FREE_FLOW_SPEED, seg);
        }
        float avgFFS = speedSum / seed.getValueInt(CEConst.IDS_NUM_SEGMENT);
        int rnddAvgFFS = Math.round(avgFFS);
        this.setSeedDefaultFFS(rnddAvgFFS);
        this.setProbability(seed.getWeatherProbability());
        this.setAdjustmentFactorsBySeed(seed);
        this.setAverageDurations(seed.getWeatherAverageDuration());
        this.setNearestMetroArea(seed.getWeatherLocation());
    }

    private void setAdjustmentFactorsBySeed(Seed seed) {
        // Extracting any existing CAFs
        if (seed.getWeatherCAF() != null) {
            this.setAdjustmentFactors(AF_TYPE_CAF, seed.getWeatherCAF());
        } else {
            setDefaultCAFs();
        }

        // Extracting any existing DAFs
        if (seed.getWeatherDAF() != null) {
            this.setAdjustmentFactors(AF_TYPE_DAF, seed.getWeatherDAF());
        } else {
            setDefaultDAFs();
        }

        // Extracting any existing SAFs
        if (seed.getWeatherSAF() != null) {
            this.setAdjustmentFactors(AF_TYPE_SAF, seed.getWeatherSAF());
        } else {
            setDefaultSAFs(seedDefaultFFS);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     *
     * @param month
     * @param weatherType
     * @return
     */
    public float getProbability(int month, int weatherType) {

        if (month >= 0 && month < 12) {
            if (weatherType >= 0 && weatherType < 11) {
                return weatherProbability[month][weatherType];
            }
        }
        return 0.0f;
    }

    /**
     *
     * @return
     */
    public float[][] getProbability() {
        return weatherProbability;
    }

    /**
     *
     * @param month
     * @param weatherType
     * @return
     */
    public float getProbabilityDecimal(int month, int weatherType) {
        if (month >= 0 && month < 12) {
            if (weatherType >= 0 && weatherType < 11) {
                return weatherProbability[month][weatherType] / 100.0f;
            }
        }
        return 0.0f;
    }

    /**
     *
     * @param weatherType
     * @return
     */
    public float getAverageDurationMinutes(int weatherType) {
        if (weatherType >= 0 && weatherType < 10) {
            return averageDuration[weatherType];
        }
        return 0.0f;
    }

    /**
     *
     * @return
     */
    public float[] getAverageDurationMinutes() {
        return averageDuration;
    }

    /**
     *
     * @param weatherType
     * @return
     */
    public float getAverageDurationHours(int weatherType) {
        if (weatherType >= 0 && weatherType < 10) {
            return averageDuration[weatherType] / 60.0f;
        }
        return 0.0f;
    }

    /**
     *
     * @param weatherType
     * @return
     */
    public float getAvgDurRoundedTo15MinIncrHour(int weatherType) {
        if (weatherType >= 0 && weatherType < 10) {
            return (15 * (Math.round(averageDuration[weatherType] / 15))) / 60.0f;
        }
        return 0.0f;
    }

    /**
     *
     * @param weatherType
     * @return
     */
    public int getAvgDurRoundedTo15MinIncrMinute(int weatherType) {
        if (weatherType >= 0 && weatherType < 10) {
            return (15 * (Math.round(averageDuration[weatherType] / 15)));
        }
        return 0;
    }

    /**
     *
     * @param weatherType
     * @return
     */
    public int getAvgDurRoundedTo15MinIncrNumIncr(int weatherType) {
        if (weatherType >= 0 && weatherType < 10) {
            return (Math.round(averageDuration[weatherType] / 15));
        }
        return 0;
    }

    /**
     * Returns the adjustment factor of the specified type for the input weather
     * type/severity.
     *
     * @param factorType Adjustment factor type identifier (AF_TYPE_CAF,
     * AF_TYPE_DAF, AF_TYPE_SAF).
     * @param weatherType Weather type/severity identifier.
     * @return Adjustment factor
     */
    public float getAdjustmentFactor(int factorType, int weatherType) {
        switch (factorType) {
            case AF_TYPE_CAF:
                return getWeatherCAF(weatherType);
            case AF_TYPE_DAF:
                return getWeatherDAF(weatherType);
            case AF_TYPE_SAF:
                return getWeatherSAF(weatherType);
            default:
                throw new InvalidAdjustmentFactorTypeException();
        }
    }

    /**
     * Returns the adjustment factor array of the specified type.
     *
     * @param factorType Adjustment factor type identifier (AF_TYPE_CAF,
     * AF_TYPE_DAF, AF_TYPE_SAF).
     * @return Adjustment factor array.
     */
    public float[] getAdjustmentFactorArray(int factorType) {
        switch (factorType) {
            case AF_TYPE_CAF:
                return getWeatherCAFArray();
            case AF_TYPE_DAF:
                return getWeatherDAFArray();
            case AF_TYPE_SAF:
                return getWeatherSAFArray();
            default:
                throw new InvalidAdjustmentFactorTypeException();
        }
    }

    /**
     * Returns the Capacity Adjustment Factor (CAF) for the specified weather
     * type/severity.
     *
     * @param weatherType Weather event type (also called severity)
     * @return Capacity adjustment factor of weather type.
     */
    public float getWeatherCAF(int weatherType) {
        if (weatherType < 11) {
            return weatherCAFs[weatherType];
        } else {
            throw new InvalidWeatherTypeException();
        }
    }

    /**
     * Returns the array of Capacity Adjustment Factors (CAFs).
     *
     * @return Array of capacity adjustment factors.
     */
    public float[] getWeatherCAFArray() {
        return weatherCAFs;
    }

    /**
     * Returns the Demand Adjustment Factor (DAF) for the specified weather
     * type/severity.
     *
     * @param weatherType Weather event type (also called severity)
     * @return Demand adjustment factor of weather type.
     */
    public float getWeatherDAF(int weatherType) {
        if (weatherType < 11) {
            return weatherDAFs[weatherType];
        } else {
            throw new InvalidWeatherTypeException();
        }
    }

    /**
     * Returns the array of Demand Adjustment Factors (DAFs).
     *
     * @return Array of demand adjustment factors.
     */
    public float[] getWeatherDAFArray() {
        return weatherDAFs;
    }

    /**
     * Returns the Speed Adjustment Factor (SAF) for the specified weather
     * type/severity.
     *
     * @param weatherType Weather event type (also called severity)
     * @return Speed adjustment factor of weather type.
     */
    public float getWeatherSAF(int weatherType) {
        if (weatherType < 11) {
            return weatherSAFs[weatherType];
        } else {
            throw new InvalidWeatherTypeException();
        }
    }

    /**
     * Returns the array of Speed Adjustment Factors (SAFs).
     *
     * @return Array of speed adjustment factors.
     */
    public float[] getWeatherSAFArray() {
        return weatherSAFs;
    }

    /**
     *
     * @param month
     * @return
     */
    public boolean getMonthActive(int month) {
        if (month >= 0 && month < 12) {
            return monthActive[month];
        }
        return false;
    }

    /**
     *
     * @return
     */
    public int getNumWeatherTypes() {
        return 10;
    }

    /**
     *
     * @param weatherType
     * @return
     */
    public static String getWeatherTypeAbbrev(int weatherType) {
        switch (weatherType) {
            case 0:
                return "MR";
            case 1:
                return "HR";
            case 2:
                return "LS";
            case 3:
                return "LMS";
            case 4:
                return "MHS";
            case 5:
                return "HS";
            case 6:
                return "SC";
            case 7:
                return "LV";
            case 8:
                return "VLV";
            case 9:
                return "MV";
            default:
                return "";
        }
    }

    /**
     *
     * @param weatherType
     * @return
     */
    public static String getWeatherTypeFull(int weatherType) {
        switch (weatherType) {
            case 0:
                return "Medium Rain";
            case 1:
                return "Heavy Rain";
            case 2:
                return "Light Snow";
            case 3:
                return "Light-Medium Snow";
            case 4:
                return "Medium-Heavy Snow";
            case 5:
                return "Heavy Snow";
            case 6:
                return "Severe Cold";
            case 7:
                return "Low Visability";
            case 8:
                return "Very Low Visability";
            case 9:
                return "Minimum Visability";
            default:
                return "";
        }
    }

    /**
     *
     * @return
     */
    public String getNearestMetroArea() {
        return this.nearestMetroArea;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Setters">
    /**
     *
     * @param seedDefaultFFS
     */
    public void setSeedDefaultFFS(int seedDefaultFFS) {
        this.seedDefaultFFS = seedDefaultFFS;
    }

    /**
     *
     * @param month
     * @param weatherType
     * @param value
     */
    public void setValue(int month, int weatherType, float value) {
        if (month >= 0 && month < 12) {
            if (weatherType >= 0 && weatherType < 11) {
                weatherProbability[month][weatherType] = value;
                updateNormalWeather(month);
            }
        }
    }

    /**
     *
     * @param seedWeatherProbabilities
     */
    public void setProbability(float[][] seedWeatherProbabilities) {

        // Assigning probabilities to weatherProbability array
        if (seedWeatherProbabilities != null) {
            for (int month = 0; month < 12; month++) {
                for (int weatherType = 0; weatherType < 11; weatherType++) {
                    weatherProbability[month][weatherType] = seedWeatherProbabilities[month][weatherType];
                }
            }
        }

    }

    /**
     * Sets the adjustment factor for the specified weather type.
     *
     * @param adjFactorType Adjustment factor type identifier (AF_TYPE_CAF,
     * AF_TYPE_DAF, AF_TYPE_SAF).
     * @param weatherType Weather type/severity identifier/
     * @param value New adjustment factor value.
     */
    public void setAdjustmentFactor(int adjFactorType, int weatherType, float value) {
        switch (adjFactorType) {
            case AF_TYPE_CAF:
                setWeatherCAF(weatherType, value);
                break;
            case AF_TYPE_DAF:
                setWeatherDAF(weatherType, value);
                break;
            case AF_TYPE_SAF:
                setWeatherSAF(weatherType, value);
                break;
            default:
                throw new InvalidAdjustmentFactorTypeException();
        }
    }

    /**
     * Sets the adjustment factor array for the specified adjustment factor
     * type.
     *
     * @param adjFactorType Adjustment factor type identifier (AF_TYPE_CAF,
     * AF_TYPE_DAF, AF_TYPE_SAF).
     * @param values New adjustment factor array.
     */
    public void setAdjustmentFactors(int adjFactorType, float[] values) {
        switch (adjFactorType) {
            case AF_TYPE_CAF:
                setWeatherCAFArray(values);
                break;
            case AF_TYPE_DAF:
                setWeatherDAFArray(values);
                break;
            case AF_TYPE_SAF:
                setWeatherSAFArray(values);
                break;
            default:
                throw new InvalidAdjustmentFactorTypeException();
        }
    }

    /**
     * Sets the Capacity Adjustment Factor (CAF) for the specified weather
     * type/severity.
     *
     * @param weatherType Weather event type (also called severity).
     * @param newCAF New capacity adjustment factor for the weather
     * type/severity.
     */
    public void setWeatherCAF(int weatherType, float newCAF) {
        if (weatherType < 11) {
            weatherCAFs[weatherType] = newCAF;
        } else {
            throw new InvalidWeatherTypeException();
        }
    }

    /**
     * Sets the array of Capacity Adjustment Factors (CAFs).
     *
     * @param newCAFArray
     */
    public void setWeatherCAFArray(float[] newCAFArray) {
        if (newCAFArray.length == weatherCAFs.length) {
            for (int wType = 0; wType < weatherCAFs.length; wType++) {
                weatherCAFs[wType] = newCAFArray[wType];
            }
        } else {
            throw new RuntimeException("Invalid length of new Capacity Adjustment Factor array.");
        }
    }

    /**
     * Sets the Demand Adjustment Factor (DAF) for the specified weather
     * type/severity.
     *
     * @param weatherType Weather event type (also called severity).
     * @param newDAF New demand adjustment factor for the weather type/severity.
     */
    public void setWeatherDAF(int weatherType, float newDAF) {
        if (weatherType < 11) {
            weatherDAFs[weatherType] = newDAF;
        } else {
            throw new InvalidWeatherTypeException();
        }
    }

    /**
     * Sets the array of Demand Adjustment Factors (DAFs).
     *
     * @param newDAFArray
     */
    public void setWeatherDAFArray(float[] newDAFArray) {
        if (newDAFArray.length == weatherDAFs.length) {
            for (int wType = 0; wType < weatherDAFs.length; wType++) {
                weatherDAFs[wType] = newDAFArray[wType];
            }
        } else {
            throw new RuntimeException("Invalid length of new Demand Adjustment Factor array.");
        }
    }

    /**
     * Sets the Speed Adjustment Factor (SAF) for the specified weather
     * type/severity.
     *
     * @param weatherType Weather event type (also called severity).
     * @param newSAF New speed adjustment factor for the weather type/severity.
     */
    public void setWeatherSAF(int weatherType, float newSAF) {
        if (weatherType < 11) {
            weatherSAFs[weatherType] = newSAF;
        } else {
            throw new InvalidWeatherTypeException();
        }
    }

    /**
     * Sets the array of Speed Adjustment Factors (CAFs).
     *
     * @param newSAFArray
     */
    public void setWeatherSAFArray(float[] newSAFArray) {
        if (newSAFArray.length == weatherSAFs.length) {
            for (int wType = 0; wType < weatherSAFs.length; wType++) {
                weatherSAFs[wType] = newSAFArray[wType];
            }
        } else {
            throw new RuntimeException("Invalid length of new Speed Adjustment Factor array.");
        }
    }

    /**
     *
     * @param seedDurations
     */
    public void setAverageDurations(float[] seedDurations) {

        if (seedDurations != null) {
            for (int weatherType = 0; weatherType < 10; weatherType++) {
                averageDuration[weatherType] = seedDurations[weatherType];
            }
        }
    }

    /**
     *
     * @param month
     * @param active
     */
    public void setMonthActive(int month, boolean active) {
        if (month >= 0 && month < 12) {
            monthActive[month] = active;
        }
    }

    /**
     *
     * @param location
     */
    public void setNearestMetroArea(String location) {
        if (location != null) {
            this.nearestMetroArea = location;
        }
    }

    // </editor-fold>
    private void initializeFields() {

        // Zero probabilities
        for (int month = 0; month < 12; ++month) {
            float sum = 0.0f;
            for (int weather = 0; weather < 10; ++weather) {
                weatherProbability[month][weather] = 0.0f;
                sum += weatherProbability[month][weather];
            }
            weatherProbability[month][10] = 100.0f - sum;
        }

        // Default durations
        for (int weather = 0; weather < 10; ++weather) {
            averageDuration[weather] = 15.0f;
        }

        // Default adjustment factors
        for (int weather = 0; weather < 11; ++weather) {
            weatherCAFs[weather] = defaultCAF[weather];
            weatherDAFs[weather] = 1.0f;
            weatherSAFs[weather] = 1.0f;
        }

        for (int month = 0; month < 12; ++month) {
            monthActive[month] = true;
        }
    }

    private void updateNormalWeather(int month) {
        float sum = 0.0f;
        for (int weather = 0; weather < 10; ++weather) {
            sum += weatherProbability[month][weather];
        }
        weatherProbability[month][10] = 100.0f - sum;
    }

    /**
     *
     * @param metroAreaName
     * @param startTime
     * @param endTime
     */
    public void extractFromWeatherDB(String metroAreaName, CETime startTime, CETime endTime) {
        //Put these in initialize fields?
        initializeFields();
        // Extract cityCode from w_names database
        // Reformatting metroAreaName to allow for search in current csv input
        this.nearestMetroArea = metroAreaName;

        String[] metroAreaNameTokens = metroAreaName.split(",");
        metroAreaName = "\"" + metroAreaNameTokens[0] + " " + metroAreaNameTokens[1] + "\"";
        String cityCode = "";
        BufferedReader br = null;
        String line = "";
        String cvsSplitter = ",";

        try {
            br = new BufferedReader(new InputStreamReader(WeatherData.class.getResourceAsStream("/coreEngine/reliabilityAnalysis/database/w_names.csv")));
//                    br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();  //Reads and skips header row
            while ((line = br.readLine()) != null) {
                // comma is separator
                String[] line_tokens = line.split(cvsSplitter);
                // Each line is CITY ID, AIRPORT CODE,CITY,STATE,NAME,code,# of bad weather, portion of good weather
                //System.out.println(line_tokens[5]);
                if ((line_tokens[4] + line_tokens[5]).equals(metroAreaName)) { //looks at NAME field
                    cityCode = line_tokens[1];  // Assigns Airport code as in FREEVAL
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        int startHour = startTime.hour;
        int startMin = startTime.minute;
        int endHour = endTime.hour;
        int endMin = endTime.minute;
        int numPeriods = endHour - startHour;
        int k11 = 4 * startHour + startMin / 15;
        int k12 = 4 * endHour + endMin / 15;  //TODO add check to make sure k12!=k11
        //System.out.format("Seed start hour: %d%n",startHour);
        //System.out.format("Seed start min: %d%n",startMin);
        //System.out.format("Seed end hour: %d%n", endHour);
        //System.out.format("Seed end min: %d%n",endMin);
        //System.out.format("Number of hours %d%n", numPeriods);
        //System.out.format("k11: %d%n",k11);
        //System.out.format("k12 %d%n",k12);

        br = null;
        line = "";
        cvsSplitter = ",";

        try {
//                    br = new BufferedReader(new FileReader(csvFile));
            br = new BufferedReader(new InputStreamReader(WeatherData.class.getResourceAsStream("/coreEngine/reliabilityAnalysis/database/weather_db.csv")));
            line = br.readLine();  //Reads and skips header row
            String currCityCode = "";
            while (!currCityCode.equals(cityCode)) {  //Searches for first appearance of cityCode in database
                br.mark(1000);
                line = br.readLine();
                currCityCode = (line.split(","))[0];
            }
            br.reset(); //moving back to previous line, necessary in first iteration of code
            for (int month = 0; month < 12; ++month) {
                for (int hour = 0; hour <= startHour; ++hour) {
                    line = br.readLine();
                }
                // comma is separator
                // Each line is airport,med rain, str rain, low snow, lm snow, mh snow, h snow, severe cold, low vis, very low vis, min vis, normal, month, hour, count
                //System.out.format("Month %d%n", month+1);
                String[] line_tokens = line.split(cvsSplitter);
                for (int weather = 0; weather < 10; ++weather) {
                    //Subtracts any time not included in the first hour
                    //System.out.format("init: %f%n", weatherProbability[month][weather]);
                    weatherProbability[month][weather] = weatherProbability[month][weather] - (startMin / 60.0f) * (100.0f * Float.parseFloat(line_tokens[weather + 1]));  //TODO period length?
                }
                for (int period = 0; period <= numPeriods; ++period) { //TODO rename numPeriods, bad name
                    //System.out.format("pf %d%n", period);
                    for (int weather = 0; weather < 10; ++weather) {
                        weatherProbability[month][weather] = weatherProbability[month][weather] + (100.0f * Float.parseFloat(line_tokens[weather + 1]));  //TODO period length?
                    }
                    if (period < numPeriods) {
                        line = br.readLine();
                        line_tokens = line.split(cvsSplitter);
                    }
                }
                for (int weather = 0; weather < 10; ++weather) {
                    //Subtracts any time not included in the final hour
                    weatherProbability[month][weather] = weatherProbability[month][weather] - ((60.0f - endMin) / 60.0f) * (100.0f * Float.parseFloat(line_tokens[weather + 1]));  //TODO period length?
                    //System.out.format("wp: %f%n", weatherProbability[month][weather]);
                }
                for (int skipHours = 0; skipHours < 24 - endHour - 1; ++skipHours) {
                    line = br.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Post-processing and generating normal weather probability
        for (int month = 0; month < 12; ++month) {
            float sum = 0.0f;
            for (int weather = 0; weather < 10; ++weather) {
                if (weatherProbability[month][weather] > 0.001) { //TODO 0.1000%
                    weatherProbability[month][weather] = 4 * weatherProbability[month][weather] / (k12 - k11);
                } else {
                    weatherProbability[month][weather] = 0;
                }
                sum += weatherProbability[month][weather];
            }
            weatherProbability[month][10] = 100.0f - sum;
        }

        // Setting weather durations
        br = null;
        line = "";
        cvsSplitter = ",";

        try {
//                    br = new BufferedReader(new FileReader(csvFile));
            br = new BufferedReader(new InputStreamReader(WeatherData.class.getResourceAsStream("/coreEngine/reliabilityAnalysis/database/w_dur.csv")));
            line = br.readLine();  //Reads and skips header row
            while ((line = br.readLine()) != null) {
                String[] line_tokens = line.split(cvsSplitter);
                if (line_tokens[0].equals(cityCode)) {
                    for (int weather = 0; weather < 10; ++weather) {
                        averageDuration[weather] = 60.0f * Float.parseFloat(line_tokens[weather + 1]);
                    }
                    break;
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Setting default Capacity Adjustment Factors
        setDefaultCAFs();

        // Setting default FFS Adjustment Factors
        setDefaultSAFs(seedDefaultFFS);

        // Setting default Demand Adjustment Factors
        setDefaultDAFs();

    } //end of extractFromWeatherDB

    private void setDefaultCAFs() {
        // Setting default Capacity Adjustment Factors
        boolean usePercentage = false;
        if (usePercentage) {
            weatherCAFs[0] = 92.76f;
            weatherCAFs[1] = 85.87f;
            weatherCAFs[2] = 95.71f;
            weatherCAFs[3] = 91.34f;
            weatherCAFs[4] = 88.96f;
            weatherCAFs[5] = 77.57f;
            weatherCAFs[6] = 91.55f;
            weatherCAFs[7] = 90.33f;
            weatherCAFs[8] = 88.33f;
            weatherCAFs[9] = 89.51f;
            weatherCAFs[10] = 100.00f;
        } else {
            weatherCAFs[0] = .9276f;
            weatherCAFs[1] = .8587f;
            weatherCAFs[2] = .9571f;
            weatherCAFs[3] = .9134f;
            weatherCAFs[4] = .8896f;
            weatherCAFs[5] = .7757f;
            weatherCAFs[6] = .9155f;
            weatherCAFs[7] = .9033f;
            weatherCAFs[8] = .8833f;
            weatherCAFs[9] = .8951f;
            weatherCAFs[10] = 1.0f;
        }
    }

    private void setDefaultSAFs(int defaultFFS) {
        // Setting the FFS adjustment factors

        switch (defaultFFS) {
            case 55:
                //setWeatherSAFArray(new float[]{96.0f, 94.0f, 94.0f, 92.0f, 90.0f, 88.0f, 95.0f, 96.0f, 95.0f, 95.0f, 100.0f};
                setWeatherSAFArray(new float[]{0.96f, 0.94f, 0.94f, 0.92f, 0.90f, 0.88f, 0.95f, 0.96f, 0.95f, 0.95f, 1.0f});
                break;
            case 60:
                //setWeatherSAFArray(new float[]{95.0f, 93.0f, 92.0f, 90.0f, 88.0f, 86.0f, 95.0f, 95.0f, 94.0f, 94.0f, 100.0f};
                setWeatherSAFArray(new float[]{0.95f, 0.93f, 0.92f, 0.90f, 0.88f, 0.86f, 0.95f, 0.95f, 0.94f, 0.94f, 1.0f});
                break;
            case 65:
                //setWeatherSAFArray(new float[]{94.0f, 93.0f, 89.0f, 88.0f, 86.0f, 85.0f, 94.0f, 94.0f, 93.0f, 93.0f, 100.0f};
                setWeatherSAFArray(new float[]{0.94f, 0.93f, 0.89f, 0.88f, 0.86f, 0.85f, 0.94f, 0.94f, 0.93f, 0.93f, 1.0f});
                break;
            case 70:
                //setWeatherSAFArray(new float[]{93.0f, 92.0f, 87.0f, 86.0f, 84.0f, 83.0f, 93.0f, 94.0f, 92.0f, 92.0f, 100.0f};
                setWeatherSAFArray(new float[]{0.93f, 0.92f, 0.87f, 0.86f, 0.84f, 0.83f, 0.93f, 0.94f, 0.92f, 0.92f, 1.0f});
                break;
            case 75:
                //setWeatherSAFArray(new float[]{93.0f, 91.0f, 84.0f, 83.0f, 82.0f, 81.0f, 92.0f, 93.0f, 91.0f, 91.0f, 100.0f};
                setWeatherSAFArray(new float[]{0.93f, 0.91f, 0.84f, 0.83f, 0.82f, 0.81f, 0.92f, 0.93f, 0.91f, 0.91f, 1.0f});
                break;
            default:
                //Interpolate
                if (defaultFFS < 55) {
                    setWeatherSAFArray(new float[]{0.96f, 0.94f, 0.94f, 0.92f, 0.90f, 0.88f, 0.95f, 0.96f, 0.95f, 0.95f, 1.0f});
                } else if (defaultFFS > 75) {
                    setWeatherSAFArray(new float[]{0.93f, 0.91f, 0.84f, 0.83f, 0.82f, 0.81f, 0.92f, 0.93f, 0.91f, 0.91f, 1.0f});
                } else {
                    //setWeatherSAFArray(new float[11]);
                    float x1 = 0;
                    float[] lsafs = new float[11];
                    float[] usafs = new float[11];
                    if (55 < defaultFFS && defaultFFS < 60) {
                        x1 = 55.0f;
                        lsafs = new float[]{0.96f, 0.94f, 0.94f, 0.92f, 0.90f, 0.88f, 0.95f, 0.96f, 0.95f, 0.95f, 1.0f};
                        usafs = new float[]{0.95f, 0.93f, 0.92f, 0.90f, 0.88f, 0.86f, 0.95f, 0.95f, 0.94f, 0.94f, 1.0f};
                    } else if (60 < defaultFFS && defaultFFS < 65) {
                        x1 = 60.0f;
                        lsafs = new float[]{0.95f, 0.93f, 0.92f, 0.90f, 0.88f, 0.86f, 0.95f, 0.95f, 0.94f, 0.94f, 1.0f};
                        usafs = new float[]{0.94f, 0.93f, 0.89f, 0.88f, 0.86f, 0.85f, 0.94f, 0.94f, 0.93f, 0.93f, 1.0f};
                    } else if (65 < defaultFFS && defaultFFS < 70) {
                        x1 = 65.0f;
                        lsafs = new float[]{0.94f, 0.93f, 0.89f, 0.88f, 0.86f, 0.85f, 0.94f, 0.94f, 0.93f, 0.93f, 1.0f};
                        usafs = new float[]{0.93f, 0.92f, 0.87f, 0.86f, 0.84f, 0.83f, 0.93f, 0.94f, 0.92f, 0.92f, 1.0f};
                    } else if (70 < defaultFFS && defaultFFS < 75) {
                        x1 = 70.0f;
                        lsafs = new float[]{0.93f, 0.92f, 0.87f, 0.86f, 0.84f, 0.83f, 0.93f, 0.94f, 0.92f, 0.92f, 1.0f};
                        usafs = new float[]{0.93f, 0.91f, 0.84f, 0.83f, 0.82f, 0.81f, 0.92f, 0.93f, 0.91f, 0.91f, 1.0f};
                    }
                    for (int weatherType = 0; weatherType < 11; weatherType++) {
                        float m = (usafs[weatherType] - lsafs[weatherType]) / 5.0f;
                        float b = lsafs[weatherType] - m * x1;
                        float interpValue = m * defaultFFS + b;
                        weatherSAFs[weatherType] = interpValue;
                    }
                }
                break;
        }
    }

    private void setDefaultDAFs() {
        // Setting default Demand Adjustment Factors
        for (int weather = 0; weather < 11; ++weather) {
            weatherDAFs[weather] = 1.00f;
        }
    }

    /**
     *
     * @return
     */
    public boolean checkNonNegative() {

        // Checking to make sure none of the probabilities are negative
        for (int i = 0; i < weatherProbability.length; i++) {
            for (int j = 0; j < weatherProbability[i].length; j++) {
                if (weatherProbability[i][j] < 0) {
                    return false;
                }
            }
        }

        // Checkint to make sure none of the average durations are negative
        for (int i = 0; i < averageDuration.length; i++) {
            if (averageDuration[i] < 0) {
                return false;
            }
        }

        // Checking to make sure none of the adjustment Factors are negative
        for (int j = 0; j < weatherCAFs.length; j++) {
            if (weatherCAFs[j] < 0 || weatherDAFs[j] < 0 || weatherSAFs[j] < 0) {
                return false;
            }
        }

        return true;
    }

    public class InvalidWeatherTypeException extends RuntimeException {

        public InvalidWeatherTypeException() {
            super("Invalid Weather Type Specified.");
        }

    }

    public class InvalidAdjustmentFactorTypeException extends RuntimeException {

        public InvalidAdjustmentFactorTypeException() {
            super("Invalid Adjustment Factor Type Identifier.");
        }
    }
}
