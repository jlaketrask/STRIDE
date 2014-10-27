package coreEngine.Helper;

import coreEngine.Seed;

/**
 * This class is used to store output of a seed single run.
 *
 * @author Shu Liu
 */
public class FacilitySummary {

    /**
     * Display Header Constant
     */
    public final static String HEADER_totalLength = "Length (mi)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_actualTravelTime = "Average Travel Time (min)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_VMTD = "VMTD (veh-miles / interval)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_VMTV = "VMTV (veh-miles / interval)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_PMTD = "PMTD (p-miles / interval)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_PMTV = "PMTV (p-miles / interval)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_VHT = "VHT (travel / interval (hrs))";

    /**
     * Display Header Constant
     */
    public final static String HEADER_VHD = "VHD (delay / interval (hrs))";

    /**
     * Display Header Constant
     */
    public final static String HEADER_spaceMeanSpeed = "Space Mean Speed (mph)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_reportDensity = "Reported Density (pc/mi/ln)";

    /**
     * Display Header Constant
     */
    public final static String HEADER_maxDC = "Max D/C";

    /**
     * Display Header Constant
     */
    public final static String HEADER_maxVC = "Max V/C";

    /**
     * Summary Data
     */
    public final float totalLength;

    /**
     * Summary Data
     */
    public final float actualTravelTime;

    /**
     * Summary Data
     */
    public final float VMTD;

    /**
     * Summary Data
     */
    public final float VMTV;

    /**
     * Summary Data
     */
    public final float PMTD;

    /**
     * Summary Data
     */
    public final float PMTV;

    /**
     * Summary Data
     */
    public final float VHT;

    /**
     * Summary Data
     */
    public final float VHD;

    /**
     * Summary Data
     */
    public final float spaceMeanSpeed;

    /**
     * Summary Data
     */
    public final float reportDensity;

    /**
     * Summary Data
     */
    public final float maxDC;

    /**
     * Summary Data
     */
    public final float maxVC;

    /**
     * Constructor
     *
     * @param seed seed
     * @param scen scenario index
     */
    public FacilitySummary(Seed seed, int scen) {
        this(seed, scen, -1);
    }

    /**
     * Constructor
     *
     * @param seed seed
     * @param scen scenario index
     * @param atdm ATDM set index
     */
    public FacilitySummary(Seed seed, int scen, int atdm) {
        totalLength = seed.getValueFloat(CEConst.IDS_TOTAL_LENGTH_MI);
        actualTravelTime = seed.getValueFloat(CEConst.IDS_CB_SP_ACTUAL_TIME, 0, 0, scen, atdm);
        VMTD = seed.getValueFloat(CEConst.IDS_CB_SP_VMTD, 0, 0, scen, atdm);
        VMTV = seed.getValueFloat(CEConst.IDS_CB_SP_VMTV, 0, 0, scen, atdm);
        PMTD = seed.getValueFloat(CEConst.IDS_CB_SP_PMTD, 0, 0, scen, atdm);
        PMTV = seed.getValueFloat(CEConst.IDS_CB_SP_PMTV, 0, 0, scen, atdm);
        VHT = seed.getValueFloat(CEConst.IDS_CB_SP_VHT, 0, 0, scen, atdm);
        VHD = seed.getValueFloat(CEConst.IDS_CB_SP_VHD, 0, 0, scen, atdm);
        spaceMeanSpeed = seed.getValueFloat(CEConst.IDS_CB_SP_SPACE_MEAN_SPEED, 0, 0, scen, atdm);
        reportDensity = seed.getValueFloat(CEConst.IDS_CB_SP_REPORT_DENSITY_PC, 0, 0, scen, atdm);
        maxDC = seed.getValueFloat(CEConst.IDS_CB_SP_MAX_DC, 0, 0, scen, atdm);
        maxVC = seed.getValueFloat(CEConst.IDS_CB_SP_MAX_VC, 0, 0, scen, atdm);
    }
}
