/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DSS.DataStruct;

import coreEngine.Seed;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author jltrask
 */
public class UserLevelParameterSet implements Serializable {
    
    private static final long serialVersionUID = 613281561348L;
    public ATMParameterSet atm;

    //<editor-fold defaultstate="collapsed" desc="Scenario Event Paremters">
    //<editor-fold defaultstate="collapsed" desc="Incident Parameters">
    // GP
    public float[][] IncidentCAFs_GP;
    public float[][] IncidentDAFs_GP;
    public float[][] IncidentSAFs_GP;

    // ML
    public float[][] IncidentCAFs_ML;
    public float[][] IncidentDAFs_ML;
    public float[][] IncidentSAFs_ML;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Work Zone Parameters">
    // GP
    public float[][] WorkZoneCAFs_GP;
    public float[][] WorkZoneDAFs_GP;
    public float[][] WorkZoneSAFs_GP;

    // ML
    //public float[][] WorkZoneCAFs_ML;
    //public float[][] WorkZoneDAFs_ML;
    //public float[][] WorkZoneSAFs_ML;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Weather Parameters">
    // GP
    public float[] WeatherCAFs_GP;
    public float[] WeatherDAFs_GP;
    public float[] WeatherSAFs_GP;

    // ML
    //public float[] WeatherCAFs_ML;
    //public float[] WeatherDAFs_ML;
    //public float[] WeatherSAFs_ML;
    //</editor-fold>
    //</editor-fold>
    public final static String ID_HSR_TYPE_PERCENT_OF_MAINLINE_LANE = "ID_HSR_TYPE_PERCENT_OF_MAINLINE_LANE";
    public final static String ID_HSR_TYPE_VPH = "ID_HSR_TYPE_VPH";

    private String DSSProjectName = "New Project";
    private String projectFileName;
    
    public UserLevelParameterSet(Seed seed) {
        //initArrays();
        if (seed != null) {
            setArraysBySeed(seed);
        } else {
            initArrays();
            useDefaults();
        }
        atm = new ATMParameterSet(seed);
        //atm.useDefaults();

    }

    public Object getHSRCapacity(String type) {
        switch (type) {
            default:
            case (ID_HSR_TYPE_VPH):
                return atm.hsrCapacity;
        }
    }

    public void setSeed(Seed seed) {
        setArraysBySeed(seed);
        updateATMParams(seed);
    }

    public String getDSSProjectName() {
        return DSSProjectName;
    }

    public void setDSSProjectName(String DSSProjectName) {
        this.DSSProjectName = DSSProjectName;
    }

    public String getProjectFileName() {
        return projectFileName;
    }

    public void setProjectFileName(String projectFileName) {
        this.projectFileName = projectFileName;
    }
    
    private void updateATMParams(Seed seed) {
        atm = new ATMParameterSet(seed);
    }

    private void initArrays() {

        IncidentCAFs_GP = new float[5][7];
        IncidentDAFs_GP = new float[5][7];
        IncidentSAFs_GP = new float[5][7];

        IncidentCAFs_ML = new float[5][7];
        IncidentDAFs_ML = new float[5][7];
        IncidentSAFs_ML = new float[5][7];

        WorkZoneCAFs_GP = new float[5][7];
        WorkZoneDAFs_GP = new float[5][7];
        WorkZoneSAFs_GP = new float[5][7];

        //WorkZoneCAFs_ML = new float[2][2];
        //WorkZoneDAFs_ML = new float[2][2];
        //WorkZoneSAFs_ML = new float[2][2];
        WeatherCAFs_GP = new float[11];
        WeatherDAFs_GP = new float[11];
        WeatherSAFs_GP = new float[11];

        //WeatherCAFs_ML = new float[10];
        //WeatherDAFs_ML = new float[10];
        //WeatherSAFs_ML = new float[10];
    }

    private void setArraysBySeed(Seed seed) {
        if (seed.getGPIncidentCAF() != null) {
            IncidentCAFs_GP = seed.getGPIncidentCAF();
        } else {
            IncidentCAFs_GP = new float[5][7];
            useDefaultIncidentCAFs_GP();
        }

        if (seed.getGPIncidentDAF() != null) {
            IncidentDAFs_GP = seed.getGPIncidentDAF();
        } else {
            IncidentDAFs_GP = new float[5][7];
            useDefaultIncidentDAFs_GP();
        }

        if (seed.getGPIncidentSAF() != null) {
            IncidentSAFs_GP = seed.getGPIncidentSAF();
        } else {
            IncidentSAFs_GP = new float[5][7];
            useDefaultIncidentSAFs_GP();
        }

        if (seed.getMLIncidentCAF() != null) {
            IncidentCAFs_ML = seed.getMLIncidentCAF();
        } else {
            IncidentCAFs_ML = new float[5][7];
            useDefaultIncidentCAFs_ML();
        }

        if (seed.getMLIncidentDAF() != null) {
            IncidentDAFs_ML = seed.getMLIncidentDAF();
        } else {
            IncidentDAFs_ML = new float[5][7];
            useDefaultIncidentDAFs_ML();
        }

        if (seed.getMLIncidentSAF() != null) {
            IncidentSAFs_ML = seed.getMLIncidentSAF();
        } else {
            IncidentSAFs_ML = new float[5][7];
            useDefaultIncidentSAFs_ML();
        }

        // Work Zones
        if (seed.getWorkZoneCAFs() != null) {
            WorkZoneCAFs_GP = seed.getWorkZoneCAFs();
        } else {
            WorkZoneCAFs_GP = new float[5][7];
            useDefaultWorkZoneCAFs_GP();
        }

        if (seed.getWorkZoneDAFs() != null) {
            WorkZoneDAFs_GP = seed.getWorkZoneDAFs();
        } else {
            WorkZoneDAFs_GP = new float[5][7];
            useDefaultWorkZoneDAFs_GP();
        }

        if (seed.getWorkZoneSAFs() != null) {
            WorkZoneSAFs_GP = seed.getWorkZoneSAFs();
        } else {
            WorkZoneSAFs_GP = new float[5][7];
            useDefaultWorkZoneSAFs_GP();
        }

        // Weather
        if (seed.getWeatherAdjustmentFactors() != null) {
            WeatherCAFs_GP = seed.getWeatherAdjustmentFactors()[0];
            WeatherDAFs_GP = seed.getWeatherAdjustmentFactors()[2];
            WeatherSAFs_GP = seed.getWeatherAdjustmentFactors()[1];
        } else {
            WeatherCAFs_GP = new float[11];
            useDefaultWeatherCAFs();
            WeatherDAFs_GP = new float[11];
            useDefaultWeatherDAFs();
            WeatherSAFs_GP = new float[11];
            useDefaultWeatherSAFs(70);
        }

    }

    private void useDefaults() {

        useDefaultIncidentCAFs_GP();
        useDefaultIncidentDAFs_GP();
        useDefaultIncidentSAFs_GP();

        useDefaultIncidentCAFs_ML();
        useDefaultIncidentDAFs_ML();
        useDefaultIncidentSAFs_ML();

        useDefaultWorkZoneCAFs_GP();
        useDefaultWorkZoneDAFs_GP();
        useDefaultWorkZoneSAFs_GP();

        //Arrays.fill(WorkZoneCAFs_ML,1.0f);
        //Arrays.fill(WorkZoneDAFs_ML,1.0f);
        //Arrays.fill(WorkZoneSAFs_ML,1.0f);
        useDefaultWeatherCAFs();
        useDefaultWeatherDAFs();
        useDefaultWeatherSAFs(70);

        //Arrays.fill(WeatherCAFs_ML,1.0f);
        //Arrays.fill(WeatherDAFs_ML,1.0f);
        //Arrays.fill(WeatherSAFs_ML,1.0f);
    }

    /**
     *
     */
    private void useDefaultIncidentSAFs_GP() {
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
        for (int incType = 0; incType < IncidentSAFs_GP.length; incType++) {
            for (int lane = 0; lane < IncidentSAFs_GP[0].length; lane++) {
                //System.out.println("Lane: "+lane+"   incType:" +incType);
                IncidentSAFs_GP[incType][lane] = tempFFSAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultIncidentCAFs_GP() {
        float[][] tempCAFs = {{0.81f, 0.70f, 0.70f, 0.70f, 0.70f},
        {0.83f, 0.74f, 0.51f, 0.51f, 0.51f},
        {0.85f, 0.77f, 0.50f, 0.52f, 0.52f},
        {0.87f, 0.81f, 0.67f, 0.50f, 0.50f},
        {0.89f, 0.85f, 0.75f, 0.52f, 0.52f},
        {0.91f, 0.88f, 0.80f, 0.63f, 0.63f},
        {0.93f, 0.89f, 0.84f, 0.66f, 0.66f}
        };

        for (int incType = 0; incType < IncidentCAFs_GP.length; incType++) {
            for (int lane = 0; lane < IncidentCAFs_GP[0].length; lane++) {
                IncidentCAFs_GP[incType][lane] = tempCAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultIncidentDAFs_GP() {
        float[][] tempDAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };

        for (int incType = 0; incType < IncidentDAFs_GP.length; incType++) {
            for (int lane = 0; lane < IncidentDAFs_GP[0].length; lane++) {
                IncidentDAFs_GP[incType][lane] = tempDAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultIncidentSAFs_ML() {
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
        for (int incType = 0; incType < IncidentSAFs_ML.length; incType++) {
            for (int lane = 0; lane < IncidentSAFs_ML[0].length; lane++) {
                //System.out.println("Lane: "+lane+"   incType:" +incType);
                IncidentSAFs_ML[incType][lane] = tempFFSAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultIncidentCAFs_ML() {
        float[][] tempCAFs = {{0.81f, 0.70f, 0.70f, 0.70f, 0.70f},
        {0.83f, 0.74f, 0.51f, 0.51f, 0.51f},
        {0.85f, 0.77f, 0.50f, 0.52f, 0.52f},
        {0.87f, 0.81f, 0.67f, 0.50f, 0.50f},
        {0.89f, 0.85f, 0.75f, 0.52f, 0.52f},
        {0.91f, 0.88f, 0.80f, 0.63f, 0.63f},
        {0.93f, 0.89f, 0.84f, 0.66f, 0.66f}
        };

        for (int incType = 0; incType < IncidentCAFs_ML.length; incType++) {
            for (int lane = 0; lane < IncidentCAFs_ML[0].length; lane++) {
                IncidentCAFs_ML[incType][lane] = tempCAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultIncidentDAFs_ML() {
        float[][] tempDAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };

        for (int incType = 0; incType < IncidentDAFs_ML.length; incType++) {
            for (int lane = 0; lane < IncidentDAFs_ML[0].length; lane++) {
                IncidentDAFs_ML[incType][lane] = tempDAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultWeatherCAFs() {
        // Setting default Capacity Adjustment Factors
        WeatherCAFs_GP[0] = .9276f;
        WeatherCAFs_GP[1] = .8587f;
        WeatherCAFs_GP[2] = .9571f;
        WeatherCAFs_GP[3] = .9134f;
        WeatherCAFs_GP[4] = .8896f;
        WeatherCAFs_GP[5] = .7757f;
        WeatherCAFs_GP[6] = .9155f;
        WeatherCAFs_GP[7] = .9033f;
        WeatherCAFs_GP[8] = .8833f;
        WeatherCAFs_GP[9] = .8951f;
        WeatherCAFs_GP[10] = 1.0f;
    }

    /**
     *
     * @param defaultFFS
     */
    private void useDefaultWeatherSAFs(int defaultFFS) {
        // Setting the FFS adjustment factors

        switch (defaultFFS) {
            case 55:
                WeatherSAFs_GP = new float[]{0.96f, 0.94f, 0.94f, 0.92f, 0.90f, 0.88f, 0.95f, 0.96f, 0.95f, 0.95f, 1.0f};
                break;
            case 60:
                WeatherSAFs_GP = new float[]{0.95f, 0.93f, 0.92f, 0.90f, 0.88f, 0.86f, 0.95f, 0.95f, 0.94f, 0.94f, 1.0f};
                break;
            case 65:
                WeatherSAFs_GP = new float[]{0.94f, 0.93f, 0.89f, 0.88f, 0.86f, 0.85f, 0.94f, 0.94f, 0.93f, 0.93f, 1.0f};
                break;
            case 70:
                WeatherSAFs_GP = new float[]{0.93f, 0.92f, 0.87f, 0.86f, 0.84f, 0.83f, 0.93f, 0.94f, 0.92f, 0.92f, 1.0f};
                break;
            case 75:
                WeatherSAFs_GP = new float[]{0.93f, 0.91f, 0.84f, 0.83f, 0.82f, 0.81f, 0.92f, 0.93f, 0.91f, 0.91f, 1.0f};
                break;
            default:
                //Interpolate
                if (defaultFFS < 55) {
                    WeatherSAFs_GP = new float[]{0.96f, 0.94f, 0.94f, 0.92f, 0.90f, 0.88f, 0.95f, 0.96f, 0.95f, 0.95f, 1.0f};
                } else if (defaultFFS > 75) {
                    WeatherSAFs_GP = new float[]{0.93f, 0.91f, 0.84f, 0.83f, 0.82f, 0.81f, 0.92f, 0.93f, 0.91f, 0.91f, 1.0f};
                } else {
                    WeatherSAFs_GP = new float[11];
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
                        WeatherSAFs_GP[weatherType] = interpValue;
                    }
                }
                break;
        }
    }

    /**
     *
     */
    private void useDefaultWeatherDAFs() {
        // Setting default Demand Adjustment Factors
        Arrays.fill(WeatherDAFs_GP, 1.0f);
    }

    /**
     *
     */
    private void useDefaultWorkZoneSAFs_GP() {
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
        for (int incType = 0; incType < WorkZoneSAFs_GP.length; incType++) {
            for (int lane = 0; lane < WorkZoneSAFs_GP[0].length; lane++) {
                //System.out.println("Lane: "+lane+"   incType:" +incType);
                WorkZoneSAFs_GP[incType][lane] = tempFFSAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultWorkZoneCAFs_GP() {
        float[][] tempCAFs = {{0.81f, 0.70f, 0.70f, 0.70f, 0.70f},
        {0.83f, 0.74f, 0.51f, 0.51f, 0.51f},
        {0.85f, 0.77f, 0.50f, 0.52f, 0.52f},
        {0.87f, 0.81f, 0.67f, 0.50f, 0.50f},
        {0.89f, 0.85f, 0.75f, 0.52f, 0.52f},
        {0.91f, 0.88f, 0.80f, 0.63f, 0.63f},
        {0.93f, 0.89f, 0.84f, 0.66f, 0.66f}
        };

        for (int incType = 0; incType < WorkZoneCAFs_GP.length; incType++) {
            for (int lane = 0; lane < WorkZoneCAFs_GP[0].length; lane++) {
                WorkZoneCAFs_GP[incType][lane] = tempCAFs[lane][incType];
            }
        }
    }

    /**
     *
     */
    private void useDefaultWorkZoneDAFs_GP() {
        float[][] tempDAFs = {{1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
        };

        for (int incType = 0; incType < WorkZoneDAFs_GP.length; incType++) {
            for (int lane = 0; lane < WorkZoneDAFs_GP[0].length; lane++) {
                WorkZoneDAFs_GP[incType][lane] = tempDAFs[lane][incType];
            }
        }
    }

}
