package coreEngine;

/**
 *
 * @author Shu Liu
 */
public class FacilitySummary {

    public final static String HEADER_totalLength = "Length (mi)";
    public final static String HEADER_actualTravelTime = "Average Travel Time (min)";
    public final static String HEADER_VMTD = "VMTD (veh-miles / interval)";
    public final static String HEADER_VMTV = "VMTV (veh-miles / interval)";
    public final static String HEADER_PMTD = "PMTD (p-miles / interval)";
    public final static String HEADER_PMTV = "PMTV (p-miles / interval)";
    public final static String HEADER_VHT = "VHT (travel / interval (hrs))";
    public final static String HEADER_VHD = "VHD (delay / interval (hrs))";
    public final static String HEADER_spaceMeanSpeed = "Space Mean Speed (mph)";
    public final static String HEADER_reportDensity = "Reported Density (pc/mi/ln)";
    public final static String HEADER_maxDC = "Max D/C";
    public final static String HEADER_maxVC = "Max V/C";

    public final float totalLength;
    public final float actualTravelTime;
    public final float VMTD;
    public final float VMTV;
    public final float PMTD;
    public final float PMTV;
    public final float VHT;
    public final float VHD;
    public final float spaceMeanSpeed;
    public final float reportDensity;
    public final float maxDC;
    public final float maxVC;

    public FacilitySummary(Seed seed, int scen) {
        this(seed, scen, -1);
    }

    public FacilitySummary(Seed seed, int scen, int atdm) {
        totalLength = seed.getValueFloat(CEConst.IDS_CB_TOTAL_LENGTH_MI);
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
