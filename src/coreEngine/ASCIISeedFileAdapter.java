package coreEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 *
 * @author Shu Liu
 */
public class ASCIISeedFileAdapter {

    /**
     * The output formatter
     */
    private static final DecimalFormat formatter = new DecimalFormat("0.0#");

    public static final int TIME_DEPENDENT = 0;
    public static final int TIME_INDEPENDENT = 1;
    public static final int GENERAL_INFO = 2;

    public static final int INTEGER = 0;
    public static final int FLOAT = 1;
    public static final int BOOLEAN = 2;
    public static final int STRING = 3;
    public static final int OTHER = 4;

    private static final int ITEM_WIDTH = 6;

    private final HashMap<String, String> idToHeaderMap = new HashMap();

    // <editor-fold defaultstate="collapsed" desc="HEADER ID">
    // <editor-fold defaultstate="collapsed" desc="GLOBAL INPUT">
    private static final String ID_PROJECT_NAME = "001";
    private final static String ID_START = "002";
    private final static String ID_END = "003";
    private static final String ID_NUM_SEGMENTS = "004";
    private static final String ID_FFS_KNOWN = "005";
    private static final String ID_RAMP_METERING = "006";
    private static final String ID_ML_USED = "007";
    private final static String ID_ALPHA = "008";
    private final static String ID_JAM_DENSITY = "009";
    private final static String ID_SEED_DEMAND_DATE = "010";
    private final static String ID_GP_OCCU = "011";
    private final static String ID_ML_OCCU = "012";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GP HEADER">
    // Basic Segment Variable Column Text
    private final static String ID_SEGMENT_TYPE = "101";
    private final static String ID_SEGMENT_LENGTH = "102";
    private final static String ID_SEGMENT_WIDTH = "103";
    private final static String ID_LATERAL_CLEARANCE = "104";
    private final static String ID_TERRAIN = "105";
    private final static String ID_TRUCK_CAR_EQ = "106";
    private final static String ID_RV_CAR_EQ = "107";

    private final static String ID_NUM_LANES = "108";
    private final static String ID_FREE_FLOW_SPEED = "109";
    private final static String ID_DEMAND_VEH = "110";
    private final static String ID_TRUCK_PERCENTAGE = "111";
    private final static String ID_RV_PERCENTAGE = "112";
    private final static String ID_U_CAF = "113";
    private final static String ID_U_OAF = "114";
    private final static String ID_U_DAF = "115";
    private final static String ID_U_SAF = "116";

    // ONR Variable Column Text
    private final static String ID_ON_RAMP_SIDE = "117";
    private final static String ID_ACC_DEC_LANE_LENGTH = "118";

    private final static String ID_NUM_ON_RAMP_LANES = "119";
    private final static String ID_ON_RAMP_DEMAND_VEH = "120";
    private final static String ID_ON_RAMP_FREE_FLOW_SPEED = "121";
    private final static String ID_ON_RAMP_METERING_RATE = "122";

    // OFR Variable Column Text
    private final static String ID_OFF_RAMP_SIDE = "123";

    private final static String ID_NUM_OFF_RAMP_LANES = "124";
    private final static String ID_OFF_RAMP_DEMAND_VEH = "125";
    private final static String ID_OFF_RAMP_FREE_FLOW_SPEED = "126";

    // Weaving Segment Variable Column Text
    private final static String ID_LENGTH_OF_WEAVING = "127";
    private final static String ID_MIN_LANE_CHANGE_ONR_TO_FRWY = "128";
    private final static String ID_MIN_LANE_CHANGE_FRWY_TO_OFR = "129";
    private final static String ID_MIN_LANE_CHANGE_ONR_TO_OFR = "130";
    private final static String ID_NUM_LANES_WEAVING = "131";

    private final static String ID_RAMP_TO_RAMP_DEMAND_VEH = "132";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ML HEADER">
    // Basic Segment Variable Column Text
    private final static String ID_ML_SEGMENT_TYPE = "201";
    private final static String ID_ML_METHOD_TYPE = "202";
    private final static String ID_ML_SEPARATION_TYPE = "203";

    private final static String ID_ML_NUM_LANES = "204";
    private final static String ID_ML_FREE_FLOW_SPEED = "205";
    private final static String ID_ML_DEMAND_VEH = "206";
    private final static String ID_ML_TRUCK_PERCENTAGE = "207";
    private final static String ID_ML_RV_PERCENTAGE = "208";
    private final static String ID_ML_UCAF = "209";
    private final static String ID_ML_UOAF = "210";
    private final static String ID_ML_UDAF = "211";
    private final static String ID_ML_USAF = "212";
    private final static String ID_ML_ACC_DEC_LANE_LENGTH = "213";

    // ONR Variable Column Text
    private final static String ID_ML_ON_RAMP_SIDE = "214";

    private final static String ID_ML_NUM_ON_RAMP_LANES = "215";
    private final static String ID_ML_ON_RAMP_DEMAND_VEH = "216";
    private final static String ID_ML_ON_RAMP_FREE_FLOW_SPEED = "217";

    // OFR Variable Column Text
    private final static String ID_ML_OFF_RAMP_SIDE = "218";

    private final static String ID_ML_NUM_OFF_RAMP_LANES = "219";
    private final static String ID_ML_OFF_RAMP_DEMAND_VEH = "220";
    private final static String ID_ML_OFF_RAMP_FREE_FLOW_SPEED = "221";

    // Weaving Segment Variable Column Text
    private final static String ID_ML_LENGTH_SHORT = "222";
    private final static String ID_ML_MIN_LANE_CHANGE_ONR_TO_FRWY = "223";
    private final static String ID_ML_MIN_LANE_CHANGE_FRWY_TO_OFR = "224";
    private final static String ID_ML_MIN_LANE_CHANGE_ONR_TO_OFR = "225";
    private final static String ID_ML_NUM_LANES_WEAVING = "226";
    private final static String ID_ML_LC_MIN = "227";
    private final static String ID_ML_LC_MAX = "228";
    private final static String ID_ML_RAMP_TO_RAMP_DEMAND_VEH = "229";

    private final static String ID_ML_HAS_CROSS_WEAVE = "230";
    private final static String ID_ML_CROSS_WEAVE_LC_MIN = "231";
    private final static String ID_ML_CROSS_WEAVE_VOLUME = "232";
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="HEADER CONSTANT">
    // <editor-fold defaultstate="collapsed" desc="GLOBAL INPUT">
    private static final String STR_PROJECT_NAME = "Project Name";
    private final static String STR_START = "Study Period Start Time (HH:MM)";
    private final static String STR_END = "Study Period End Time (HH:MM)";
    private static final String STR_NUM_SEGMENTS = "# of Segments";
    private static final String STR_FFS_KNOWN = "Free Flow Speed Known?";
    private static final String STR_RAMP_METERING = "Ramp Metering Used?";
    private static final String STR_ML_USED = "Managed Lane Used?";
    private final static String STR_ALPHA = "Capacity Drop (%)";
    private final static String STR_JAM_DENSITY = "Jam Density (pc/mi/ln)";
    private final static String STR_SEED_DEMAND_DATE = "Seed Demand Date (YYYY-MM-DD)";
    private final static String STR_GP_OCCU = "GP Segment Vehicle Occupancy (p/veh)";
    private final static String STR_ML_OCCU = "ML Segment Vehicle Occupancy (p/veh)";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GP HEADER">
    // Basic Segment Variable Column Text
    private final static String STR_SEGMENT_TYPE = "General Purpose Segment Type (Basic = 0, ONR = 1, OFR = 2, Overlap = 3, Weaving = 4, Access = 8)";
    private final static String STR_SEGMENT_LENGTH = "Segment Length (ft)";
    private final static String STR_SEGMENT_WIDTH = "Lane Width (ft)";
    private final static String STR_LATERAL_CLEARANCE = "Lateral Clearance (ft)";
    private final static String STR_TERRAIN = "Terrain (Level = 1, Mountainous = 2, Rolling = 3, Varying = 4)";
    private final static String STR_TRUCK_CAR_EQ = "Truck-PC Equivalence (ET)";
    private final static String STR_RV_CAR_EQ = "RV-PC Equivalence (ER)";

    private final static String STR_NUM_LANES = "# of Lanes: Mainline";
    private final static String STR_FREE_FLOW_SPEED = "Free Flow Speed (mph)";
    private final static String STR_DEMAND_VEH = "Mainline Dem. (vph)";
    private final static String STR_TRUCK_PERCENTAGE = "Truck (%)";
    private final static String STR_RV_PERCENTAGE = "RV (%)";
    private final static String STR_U_CAF = "Seed Capacity Adj. Fac.";
    private final static String STR_U_OAF = "Seed Entering Dem. Adj. Fac.";
    private final static String STR_U_DAF = "Seed Exit Dem. Adj. Fac.";
    private final static String STR_U_SAF = "Seed Free Flow Speed Adj. Fac.";

    // ONR Variable Column Text
    private final static String STR_ON_RAMP_SIDE = "ONR Side (Right = 0, Left = 1)";
    private final static String STR_ACC_DEC_LANE_LENGTH = "Acc/Dec Lane Length (ft)";

    private final static String STR_NUM_ON_RAMP_LANES = "# Lanes: ONR";
    private final static String STR_ON_RAMP_DEMAND_VEH = "ONR/Entering Dem. (vph)";
    private final static String STR_ON_RAMP_FREE_FLOW_SPEED = "ONR Free Flow Speed (mph)";
    private final static String STR_ON_RAMP_METERING_RATE = "ONR Metering Rate (vph)";

    // OFR Variable Column Text
    private final static String STR_OFF_RAMP_SIDE = "OFR Side (Right = 0, Left = 1)";

    private final static String STR_NUM_OFF_RAMP_LANES = "# Lanes: OFR";
    private final static String STR_OFF_RAMP_DEMAND_VEH = "OFR/Exit Dem. (vph)";
    private final static String STR_OFF_RAMP_FREE_FLOW_SPEED = "OFR Free Flow Speed (mph)";

    // Weaving Segment Variable Column Text
    private final static String STR_LENGTH_OF_WEAVING = "Weave Segment Ls (ft)";
    private final static String STR_MIN_LANE_CHANGE_ONR_TO_FRWY = "Weave Segment LCRF";
    private final static String STR_MIN_LANE_CHANGE_FRWY_TO_OFR = "Weave Segment LCFR";
    private final static String STR_MIN_LANE_CHANGE_ONR_TO_OFR = "Weave Segment LCRR";
    private final static String STR_NUM_LANES_WEAVING = "Weave Segment NW";

    private final static String STR_RAMP_TO_RAMP_DEMAND_VEH = "Ramp to Ramp Dem. (vph)";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ML HEADER">
    // Basic Segment Variable Column Text
    private final static String STR_ML_SEGMENT_TYPE = "ML Segment Type (Basic = 0, ONR = 1, OFR = 2, Overlap = 3, Weaving = 4, Access = 8)";
    private final static String STR_ML_METHOD_TYPE = "ML Type of ML (HOV = 0, HOT = 1)";
    private final static String STR_ML_SEPARATION_TYPE = "ML Type of Separation (Marking = 0, Buffer = 1, Barrier = 2)";

    private final static String STR_ML_NUM_LANES = "ML # of Lanes: Mainline";
    private final static String STR_ML_FREE_FLOW_SPEED = "ML Free Flow Speed (mph)";
    private final static String STR_ML_DEMAND_VEH = "ML Mainline Dem. (vph)";
    private final static String STR_ML_TRUCK_PERCENTAGE = "ML Truck (%)";
    private final static String STR_ML_RV_PERCENTAGE = "ML RV (%)";
    private final static String STR_ML_UCAF = "ML Seed Capacity Adj. Fac.";
    private final static String STR_ML_UOAF = "ML Seed Entering Dem. Adj. Fac.";
    private final static String STR_ML_UDAF = "ML Seed Exit Dem. Adj. Fac.";
    private final static String STR_ML_USAF = "ML Seed Free Flow Speed Adj. Fac.";
    private final static String STR_ML_ACC_DEC_LANE_LENGTH = "ML Acc/Dec Lane Length (ft)";

    // ONR Variable Column Text
    private final static String STR_ML_ON_RAMP_SIDE = "ML ONR Side (Right = 0, Left = 1)";

    private final static String STR_ML_NUM_ON_RAMP_LANES = "ML # Lanes: ONR";
    private final static String STR_ML_ON_RAMP_DEMAND_VEH = "ML ONR/Entering Dem. (vph)";
    private final static String STR_ML_ON_RAMP_FREE_FLOW_SPEED = "ML ONR Free Flow Speed (mph)";

    // OFR Variable Column Text
    private final static String STR_ML_OFF_RAMP_SIDE = "ML OFR Side (Right = 0, Left = 1)";

    private final static String STR_ML_NUM_OFF_RAMP_LANES = "ML # Lanes: OFR";
    private final static String STR_ML_OFF_RAMP_DEMAND_VEH = "ML OFR/Exiting Dem. (vph)";
    private final static String STR_ML_OFF_RAMP_FREE_FLOW_SPEED = "ML OFR Free Flow Speed (mph)";

    // Weaving Segment Variable Column Text
    private final static String STR_ML_LENGTH_SHORT = "ML Length Short (ft)";
    private final static String STR_ML_MIN_LANE_CHANGE_ONR_TO_FRWY = "ML Weave Segment LCRF";
    private final static String STR_ML_MIN_LANE_CHANGE_FRWY_TO_OFR = "ML Weave Segment LCFR";
    private final static String STR_ML_MIN_LANE_CHANGE_ONR_TO_OFR = "ML Weave Segment LCRR";
    private final static String STR_ML_NUM_LANES_WEAVING = "ML Weave Segment NW";
    private final static String STR_ML_LC_MIN = "ML Min Lane Change";
    private final static String STR_ML_LC_MAX = "ML Max Lane Change";
    private final static String STR_ML_RAMP_TO_RAMP_DEMAND_VEH = "ML Ramp to Ramp Dem. (vph)";

    private final static String STR_ML_HAS_CROSS_WEAVE = "Analysis of Cross Weave Effect";
    private final static String STR_ML_CROSS_WEAVE_LC_MIN = "Cross Weave LC-Min";
    private final static String STR_ML_CROSS_WEAVE_VOLUME = "Cross Weave Volume";
    // </editor-fold>
    // </editor-fold>

    private final ArrayList<Item> itemListGP = new ArrayList();
    private final ArrayList<Item> itemListML = new ArrayList();

    public ASCIISeedFileAdapter() {
        buildIDMap();
        buildGPList();
        buildMLList();
    }

    private void buildIDMap() {
        // <editor-fold defaultstate="collapsed" desc="GLOBAL INPUT">
        idToHeaderMap.put(ID_PROJECT_NAME, STR_PROJECT_NAME);
        idToHeaderMap.put(ID_NUM_SEGMENTS, STR_NUM_SEGMENTS);
        idToHeaderMap.put(ID_START, STR_START);
        idToHeaderMap.put(ID_END, STR_END);
        idToHeaderMap.put(ID_FFS_KNOWN, STR_FFS_KNOWN);
        idToHeaderMap.put(ID_RAMP_METERING, STR_RAMP_METERING);
        idToHeaderMap.put(ID_ML_USED, STR_ML_USED);
        idToHeaderMap.put(ID_ALPHA, STR_ALPHA);
        idToHeaderMap.put(ID_JAM_DENSITY, STR_JAM_DENSITY);
        idToHeaderMap.put(ID_SEED_DEMAND_DATE, STR_SEED_DEMAND_DATE);
        idToHeaderMap.put(ID_GP_OCCU, STR_GP_OCCU);
        idToHeaderMap.put(ID_ML_OCCU, STR_ML_OCCU);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="GP HEADER">
        // Basic Segment Variable Column Text
        idToHeaderMap.put(ID_SEGMENT_TYPE, STR_SEGMENT_TYPE);
        idToHeaderMap.put(ID_SEGMENT_LENGTH, STR_SEGMENT_LENGTH);
        idToHeaderMap.put(ID_SEGMENT_WIDTH, STR_SEGMENT_WIDTH);
        idToHeaderMap.put(ID_LATERAL_CLEARANCE, STR_LATERAL_CLEARANCE);
        idToHeaderMap.put(ID_TERRAIN, STR_TERRAIN);
        idToHeaderMap.put(ID_TRUCK_CAR_EQ, STR_TRUCK_CAR_EQ);
        idToHeaderMap.put(ID_RV_CAR_EQ, STR_RV_CAR_EQ);

        idToHeaderMap.put(ID_NUM_LANES, STR_NUM_LANES);
        idToHeaderMap.put(ID_FREE_FLOW_SPEED, STR_FREE_FLOW_SPEED);
        idToHeaderMap.put(ID_DEMAND_VEH, STR_DEMAND_VEH);
        idToHeaderMap.put(ID_TRUCK_PERCENTAGE, STR_TRUCK_PERCENTAGE);
        idToHeaderMap.put(ID_RV_PERCENTAGE, STR_RV_PERCENTAGE);
        idToHeaderMap.put(ID_U_CAF, STR_U_CAF);
        idToHeaderMap.put(ID_U_OAF, STR_U_OAF);
        idToHeaderMap.put(ID_U_DAF, STR_U_DAF);
        idToHeaderMap.put(ID_U_SAF, STR_U_SAF);

        // ONR Variable Column Text
        idToHeaderMap.put(ID_ON_RAMP_SIDE, STR_ON_RAMP_SIDE);
        idToHeaderMap.put(ID_ACC_DEC_LANE_LENGTH, STR_ACC_DEC_LANE_LENGTH);

        idToHeaderMap.put(ID_NUM_ON_RAMP_LANES, STR_NUM_ON_RAMP_LANES);
        idToHeaderMap.put(ID_ON_RAMP_DEMAND_VEH, STR_ON_RAMP_DEMAND_VEH);
        idToHeaderMap.put(ID_ON_RAMP_FREE_FLOW_SPEED, STR_ON_RAMP_FREE_FLOW_SPEED);
        idToHeaderMap.put(ID_ON_RAMP_METERING_RATE, STR_ON_RAMP_METERING_RATE);

        // OFR Variable Column Text
        idToHeaderMap.put(ID_OFF_RAMP_SIDE, STR_OFF_RAMP_SIDE);

        idToHeaderMap.put(ID_NUM_OFF_RAMP_LANES, STR_NUM_OFF_RAMP_LANES);
        idToHeaderMap.put(ID_OFF_RAMP_DEMAND_VEH, STR_OFF_RAMP_DEMAND_VEH);
        idToHeaderMap.put(ID_OFF_RAMP_FREE_FLOW_SPEED, STR_OFF_RAMP_FREE_FLOW_SPEED);

        // Weaving Segment Variable Column Text
        idToHeaderMap.put(ID_LENGTH_OF_WEAVING, STR_LENGTH_OF_WEAVING);
        idToHeaderMap.put(ID_MIN_LANE_CHANGE_ONR_TO_FRWY, STR_MIN_LANE_CHANGE_ONR_TO_FRWY);
        idToHeaderMap.put(ID_MIN_LANE_CHANGE_FRWY_TO_OFR, STR_MIN_LANE_CHANGE_FRWY_TO_OFR);
        idToHeaderMap.put(ID_MIN_LANE_CHANGE_ONR_TO_OFR, STR_MIN_LANE_CHANGE_ONR_TO_OFR);
        idToHeaderMap.put(ID_NUM_LANES_WEAVING, STR_NUM_LANES_WEAVING);

        idToHeaderMap.put(ID_RAMP_TO_RAMP_DEMAND_VEH, STR_RAMP_TO_RAMP_DEMAND_VEH);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="ML HEADER">
        // Basic Segment Variable Column Text
        idToHeaderMap.put(ID_ML_SEGMENT_TYPE, STR_ML_SEGMENT_TYPE);
        idToHeaderMap.put(ID_ML_METHOD_TYPE, STR_ML_METHOD_TYPE);
        idToHeaderMap.put(ID_ML_SEPARATION_TYPE, STR_ML_SEPARATION_TYPE);

        idToHeaderMap.put(ID_ML_NUM_LANES, STR_ML_NUM_LANES);
        idToHeaderMap.put(ID_ML_FREE_FLOW_SPEED, STR_ML_FREE_FLOW_SPEED);
        idToHeaderMap.put(ID_ML_DEMAND_VEH, STR_ML_DEMAND_VEH);
        idToHeaderMap.put(ID_ML_TRUCK_PERCENTAGE, STR_ML_TRUCK_PERCENTAGE);
        idToHeaderMap.put(ID_ML_RV_PERCENTAGE, STR_ML_RV_PERCENTAGE);
        idToHeaderMap.put(ID_ML_UCAF, STR_ML_UCAF);
        idToHeaderMap.put(ID_ML_UOAF, STR_ML_UOAF);
        idToHeaderMap.put(ID_ML_UDAF, STR_ML_UDAF);
        idToHeaderMap.put(ID_ML_USAF, STR_ML_USAF);
        idToHeaderMap.put(ID_ML_ACC_DEC_LANE_LENGTH, STR_ML_ACC_DEC_LANE_LENGTH);

        // ONR Variable Column Text
        idToHeaderMap.put(ID_ML_ON_RAMP_SIDE, STR_ML_ON_RAMP_SIDE);

        idToHeaderMap.put(ID_ML_NUM_ON_RAMP_LANES, STR_ML_NUM_ON_RAMP_LANES);
        idToHeaderMap.put(ID_ML_ON_RAMP_DEMAND_VEH, STR_ML_ON_RAMP_DEMAND_VEH);
        idToHeaderMap.put(ID_ML_ON_RAMP_FREE_FLOW_SPEED, STR_ML_ON_RAMP_FREE_FLOW_SPEED);

        // OFR Variable Column Text
        idToHeaderMap.put(ID_ML_OFF_RAMP_SIDE, STR_ML_OFF_RAMP_SIDE);

        idToHeaderMap.put(ID_ML_NUM_OFF_RAMP_LANES, STR_ML_NUM_OFF_RAMP_LANES);
        idToHeaderMap.put(ID_ML_OFF_RAMP_DEMAND_VEH, STR_ML_OFF_RAMP_DEMAND_VEH);
        idToHeaderMap.put(ID_ML_OFF_RAMP_FREE_FLOW_SPEED, STR_ML_OFF_RAMP_FREE_FLOW_SPEED);

        // Weaving Segment Variable Column Text
        idToHeaderMap.put(ID_ML_LENGTH_SHORT, STR_ML_LENGTH_SHORT);
        idToHeaderMap.put(ID_ML_MIN_LANE_CHANGE_ONR_TO_FRWY, STR_ML_MIN_LANE_CHANGE_ONR_TO_FRWY);
        idToHeaderMap.put(ID_ML_MIN_LANE_CHANGE_FRWY_TO_OFR, STR_ML_MIN_LANE_CHANGE_FRWY_TO_OFR);
        idToHeaderMap.put(ID_ML_MIN_LANE_CHANGE_ONR_TO_OFR, STR_ML_MIN_LANE_CHANGE_ONR_TO_OFR);
        idToHeaderMap.put(ID_ML_NUM_LANES_WEAVING, STR_ML_NUM_LANES_WEAVING);
        idToHeaderMap.put(ID_ML_LC_MIN, STR_ML_LC_MIN);
        idToHeaderMap.put(ID_ML_LC_MAX, STR_ML_LC_MAX);
        idToHeaderMap.put(ID_ML_RAMP_TO_RAMP_DEMAND_VEH, STR_ML_RAMP_TO_RAMP_DEMAND_VEH);

        idToHeaderMap.put(ID_ML_HAS_CROSS_WEAVE, STR_ML_HAS_CROSS_WEAVE);
        idToHeaderMap.put(ID_ML_CROSS_WEAVE_LC_MIN, STR_ML_CROSS_WEAVE_LC_MIN);
        idToHeaderMap.put(ID_ML_CROSS_WEAVE_VOLUME, STR_ML_CROSS_WEAVE_VOLUME);
        // </editor-fold>
    }

    private void buildGPList() {
        //Global Input
        itemListGP.add(new Item(ID_PROJECT_NAME, CEConst.IDS_PROJECT_NAME, GENERAL_INFO, STRING));
        itemListGP.add(new Item(ID_START, CEConst.IDS_START_TIME, GENERAL_INFO, OTHER));
        itemListGP.add(new Item(ID_END, CEConst.IDS_END_TIME, GENERAL_INFO, OTHER));
        itemListGP.add(new Item(ID_NUM_SEGMENTS, CEConst.IDS_NUM_SEGMENT, GENERAL_INFO, INTEGER));
        itemListGP.add(new Item(ID_FFS_KNOWN, CEConst.IDS_FFS_KNOWN, GENERAL_INFO, BOOLEAN));
        itemListGP.add(new Item(ID_RAMP_METERING, CEConst.IDS_RM_USED, GENERAL_INFO, BOOLEAN));
        itemListGP.add(new Item(ID_ML_USED, CEConst.IDS_MANAGED_LANE_USED, GENERAL_INFO, BOOLEAN));
        itemListGP.add(new Item(ID_ALPHA, CEConst.IDS_CAPACITY_ALPHA, GENERAL_INFO, INTEGER));
        itemListGP.add(new Item(ID_JAM_DENSITY, CEConst.IDS_JAM_DENSITY, GENERAL_INFO, FLOAT));
        itemListGP.add(new Item(ID_SEED_DEMAND_DATE, CEConst.IDS_SEED_DEMAND_DATE, GENERAL_INFO, OTHER));
        itemListGP.add(new Item(ID_GP_OCCU, CEConst.IDS_OCCU_GP, GENERAL_INFO, FLOAT));
        itemListGP.add(new Item(ID_ML_OCCU, CEConst.IDS_OCCU_ML, GENERAL_INFO, FLOAT));

        //GP Segment Input
        itemListGP.add(new Item(ID_SEGMENT_TYPE, CEConst.IDS_SEGMENT_TYPE, TIME_INDEPENDENT, OTHER));
        itemListGP.add(new Item(ID_SEGMENT_LENGTH, CEConst.IDS_SEGMENT_LENGTH_FT, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_SEGMENT_WIDTH, CEConst.IDS_LANE_WIDTH, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_LATERAL_CLEARANCE, CEConst.IDS_LATERAL_CLEARANCE, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_TERRAIN, CEConst.IDS_TERRAIN, TIME_INDEPENDENT, OTHER));
        itemListGP.add(new Item(ID_TRUCK_CAR_EQ, CEConst.IDS_TRUCK_CAR_ET, TIME_INDEPENDENT, FLOAT));
        itemListGP.add(new Item(ID_RV_CAR_EQ, CEConst.IDS_RV_CAR_ER, TIME_INDEPENDENT, FLOAT));
        itemListGP.add(new Item(ID_NUM_LANES, CEConst.IDS_MAIN_NUM_LANES_IN, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_FREE_FLOW_SPEED, CEConst.IDS_MAIN_FREE_FLOW_SPEED, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_DEMAND_VEH, CEConst.IDS_MAIN_DEMAND_VEH, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_TRUCK_PERCENTAGE, CEConst.IDS_TRUCK_PERCENTAGE, TIME_DEPENDENT, FLOAT));
        itemListGP.add(new Item(ID_RV_PERCENTAGE, CEConst.IDS_RV_PERCENTAGE, TIME_DEPENDENT, FLOAT));

        itemListGP.add(new Item(ID_U_CAF, CEConst.IDS_U_CAF_GP, TIME_DEPENDENT, FLOAT));
        itemListGP.add(new Item(ID_U_OAF, CEConst.IDS_U_OAF_GP, TIME_DEPENDENT, FLOAT));
        itemListGP.add(new Item(ID_U_DAF, CEConst.IDS_U_DAF_GP, TIME_DEPENDENT, FLOAT));
        itemListGP.add(new Item(ID_U_SAF, CEConst.IDS_U_SAF_GP, TIME_DEPENDENT, FLOAT));

        itemListGP.add(new Item(ID_ACC_DEC_LANE_LENGTH, CEConst.IDS_ACC_DEC_LANE_LENGTH, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_ON_RAMP_SIDE, CEConst.IDS_ON_RAMP_SIDE, TIME_INDEPENDENT, OTHER));
        itemListGP.add(new Item(ID_NUM_ON_RAMP_LANES, CEConst.IDS_NUM_ON_RAMP_LANES, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_ON_RAMP_DEMAND_VEH, CEConst.IDS_ON_RAMP_FREE_FLOW_SPEED, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_ON_RAMP_FREE_FLOW_SPEED, CEConst.IDS_ON_RAMP_DEMAND_VEH, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_ON_RAMP_METERING_RATE, CEConst.IDS_ON_RAMP_METERING_RATE, TIME_DEPENDENT, INTEGER));

        itemListGP.add(new Item(ID_OFF_RAMP_SIDE, CEConst.IDS_OFF_RAMP_SIDE, TIME_INDEPENDENT, OTHER));
        itemListGP.add(new Item(ID_NUM_OFF_RAMP_LANES, CEConst.IDS_NUM_OFF_RAMP_LANES, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_OFF_RAMP_DEMAND_VEH, CEConst.IDS_OFF_RAMP_FREE_FLOW_SPEED, TIME_DEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_OFF_RAMP_FREE_FLOW_SPEED, CEConst.IDS_OFF_RAMP_DEMAND_VEH, TIME_DEPENDENT, INTEGER));

        itemListGP.add(new Item(ID_LENGTH_OF_WEAVING, CEConst.IDS_LENGTH_OF_WEAVING, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_MIN_LANE_CHANGE_ONR_TO_FRWY, CEConst.IDS_MIN_LANE_CHANGE_ONR_TO_FRWY, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_MIN_LANE_CHANGE_FRWY_TO_OFR, CEConst.IDS_MIN_LANE_CHANGE_FRWY_TO_OFR, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_MIN_LANE_CHANGE_ONR_TO_OFR, CEConst.IDS_MIN_LANE_CHANGE_ONR_TO_OFR, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_NUM_LANES_WEAVING, CEConst.IDS_NUM_LANES_WEAVING, TIME_INDEPENDENT, INTEGER));
        itemListGP.add(new Item(ID_RAMP_TO_RAMP_DEMAND_VEH, CEConst.IDS_RAMP_TO_RAMP_DEMAND_VEH, TIME_DEPENDENT, INTEGER));
    }

    private void buildMLList() {
        //ML Segment Input
        itemListML.add(new Item(ID_ML_SEGMENT_TYPE, CEConst.IDS_ML_SEGMENT_TYPE, TIME_INDEPENDENT, OTHER));
        itemListML.add(new Item(ID_ML_METHOD_TYPE, CEConst.IDS_ML_METHOD_TYPE, TIME_INDEPENDENT, OTHER));
        itemListML.add(new Item(ID_ML_SEPARATION_TYPE, CEConst.IDS_ML_SEPARATION_TYPE, TIME_INDEPENDENT, OTHER));
        itemListML.add(new Item(ID_ML_NUM_LANES, CEConst.IDS_ML_NUM_LANES, TIME_DEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_FREE_FLOW_SPEED, CEConst.IDS_ML_FREE_FLOW_SPEED, TIME_DEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_DEMAND_VEH, CEConst.IDS_ML_DEMAND_VEH, TIME_DEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_TRUCK_PERCENTAGE, CEConst.IDS_ML_TRUCK_PERCENTAGE, TIME_DEPENDENT, FLOAT));
        itemListML.add(new Item(ID_ML_RV_PERCENTAGE, CEConst.IDS_ML_RV_PERCENTAGE, TIME_DEPENDENT, FLOAT));
        itemListML.add(new Item(ID_ML_UCAF, CEConst.IDS_ML_UCAF, TIME_DEPENDENT, FLOAT));
        itemListML.add(new Item(ID_ML_UOAF, CEConst.IDS_ML_UOAF, TIME_DEPENDENT, FLOAT));
        itemListML.add(new Item(ID_ML_UDAF, CEConst.IDS_ML_UDAF, TIME_DEPENDENT, FLOAT));
        itemListML.add(new Item(ID_ML_USAF, CEConst.IDS_ML_USAF, TIME_DEPENDENT, FLOAT));

        itemListML.add(new Item(ID_ML_ACC_DEC_LANE_LENGTH, CEConst.IDS_ML_ACC_DEC_LANE_LENGTH, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_ON_RAMP_SIDE, CEConst.IDS_ML_ON_RAMP_SIDE, TIME_INDEPENDENT, OTHER));
        itemListML.add(new Item(ID_ML_NUM_ON_RAMP_LANES, CEConst.IDS_ML_NUM_ON_RAMP_LANES, TIME_DEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_ON_RAMP_FREE_FLOW_SPEED, CEConst.IDS_ML_ON_RAMP_FREE_FLOW_SPEED, TIME_DEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_ON_RAMP_DEMAND_VEH, CEConst.IDS_ML_ON_RAMP_DEMAND_VEH, TIME_DEPENDENT, INTEGER));

        itemListML.add(new Item(ID_ML_OFF_RAMP_SIDE, CEConst.IDS_ML_OFF_RAMP_SIDE, TIME_INDEPENDENT, OTHER));
        itemListML.add(new Item(ID_ML_NUM_OFF_RAMP_LANES, CEConst.IDS_ML_NUM_OFF_RAMP_LANES, TIME_DEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_OFF_RAMP_FREE_FLOW_SPEED, CEConst.IDS_ML_OFF_RAMP_FREE_FLOW_SPEED, TIME_DEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_OFF_RAMP_DEMAND_VEH, CEConst.IDS_ML_OFF_RAMP_DEMAND_VEH, TIME_DEPENDENT, INTEGER));

        itemListML.add(new Item(ID_ML_LENGTH_SHORT, CEConst.IDS_ML_LENGTH_SHORT, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_MIN_LANE_CHANGE_ONR_TO_FRWY, CEConst.IDS_ML_MIN_LANE_CHANGE_ONR_TO_FRWY, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_MIN_LANE_CHANGE_FRWY_TO_OFR, CEConst.IDS_ML_MIN_LANE_CHANGE_FRWY_TO_OFR, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_MIN_LANE_CHANGE_ONR_TO_OFR, CEConst.IDS_ML_MIN_LANE_CHANGE_ONR_TO_OFR, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_NUM_LANES_WEAVING, CEConst.IDS_ML_NUM_LANES_WEAVING, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_RAMP_TO_RAMP_DEMAND_VEH, CEConst.IDS_ML_RAMP_TO_RAMP_DEMAND_VEH, TIME_DEPENDENT, INTEGER));

        itemListML.add(new Item(ID_ML_LC_MIN, CEConst.IDS_ML_MIN_LANE_CHANGE_ML, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_LC_MAX, CEConst.IDS_ML_MAX_LANE_CHANGE_ML, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_HAS_CROSS_WEAVE, CEConst.IDS_ML_HAS_CROSS_WEAVE, TIME_INDEPENDENT, BOOLEAN));
        itemListML.add(new Item(ID_ML_CROSS_WEAVE_LC_MIN, CEConst.IDS_ML_CROSS_WEAVE_LC_MIN, TIME_INDEPENDENT, INTEGER));
        itemListML.add(new Item(ID_ML_CROSS_WEAVE_VOLUME, CEConst.IDS_ML_CROSS_WEAVE_VOLUME, TIME_DEPENDENT, INTEGER));
    }

    // <editor-fold defaultstate="collapsed" desc="Import Functions">
    /**
     * Import a Seed instance from an ASCII file (selected by user using file
     * chooser)
     *
     * @return a Seed instance
     */
    public Seed importFromASCII() {
        final JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return importFromASCII(fc.getSelectedFile().getAbsolutePath());
        }

        return null;
    }

    /**
     * Import a Seed instance from an ASCII file
     *
     * @param fileName path of the ASCII file
     * @return a Seed instance
     */
    public Seed importFromASCII(String fileName) {

        Seed seed = new Seed();
        seed.setValue(CEConst.IDS_SEED_FILE_NAME, fileName);

        try {
            FileInputStream f = new FileInputStream(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(f));

            /* Read the first separator and header lines */
            String header = in.readLine();
            while (header != null) {
                //skip invalid line until find a header or end of file
                while (header != null && !header.startsWith("<")) {
                    header = in.readLine();
                }

                if (header != null) {
                    String headerID = header.substring(1, header.indexOf(">"));
                    Item item = findItem(headerID);
                    if (item == null) {
                        System.out.println("Can not find header ID " + headerID);
                    } else {
                        String value;
                        switch (item.coreEngineID) {
                            case CEConst.IDS_START_TIME:
                            case CEConst.IDS_END_TIME:
                                value = in.readLine();
                                seed.setValue(item.coreEngineID,
                                        new CETime(Integer.parseInt(value.substring(0, value.indexOf(":"))),
                                                Integer.parseInt(value.substring(value.indexOf(":") + 1, value.length()))));
                                break;
                            case CEConst.IDS_SEED_DEMAND_DATE:
                                value = in.readLine();
                                seed.setSeedFileDate(new CEDate(value));
                                break;
                            default:
                                switch (item.property) {
                                    case GENERAL_INFO:
                                        readGeneralInfo(seed, item, in);
                                        break;
                                    case TIME_DEPENDENT:
                                        readTimeDependent(seed, item, in);
                                        break;
                                    case TIME_INDEPENDENT:
                                        readTimeIndependent(seed, item, in);
                                        break;
                                    default:
                                        System.out.println("Error in import");
                                }
                        }
                    }
                }

                header = in.readLine();
            }

            in.close();
            f.close();
        } catch (Exception e) {
            System.out.println("Error in importFromASCII");
            e.printStackTrace();
            return null;
        }

        return seed;
    }

    private Item findItem(String headerID) {
        for (Item item : itemListGP) {
            if (item.headerID.equals(headerID)) {
                return item;
            }
        }
        for (Item item : itemListML) {
            if (item.headerID.equals(headerID)) {
                return item;
            }
        }
        return null;
    }

    private void readGeneralInfo(Seed seed, Item item, BufferedReader in) throws IOException {
        String value = in.readLine();
        seed.setValue(item.coreEngineID, value);
    }

    private void readTimeIndependent(Seed seed, Item item, BufferedReader in) throws IOException {
        in.readLine(); //skip segment index line
        Scanner line = new Scanner(in.readLine());
        for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
            String value = line.next();
            if (!value.equals(CEConst.IDS_NA)) {
                seed.setValue(item.coreEngineID, value, seg);
            }
        }
        line.close();
    }

    private void readTimeDependent(Seed seed, Item item, BufferedReader in) throws IOException {
        in.readLine(); //skip segment index line
        for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
            Scanner line = new Scanner(in.readLine());
            line.next(); //skip period token
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                String value = line.next();
                if (!value.equals(CEConst.IDS_NA)) {
                    seed.setValue(item.coreEngineID, value, seg, period);
                }
            }
            line.close();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Export Functions">
    public String exportToASCII(Seed seed) {
        final JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            exportToASCII(seed, fc.getSelectedFile().getAbsolutePath());
            return fc.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    public void exportToASCII(Seed seed, String newFileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(newFileName));

            for (Item item : itemListGP) {
                try {
                    writeItem(seed, item, out);
                } catch (Exception e) {
                    System.out.println("Error in exportToASCII - GP");
                }
            }

            if (seed.isManagedLaneUsed()) {
                for (Item item : itemListML) {
                    try {
                        writeItem(seed, item, out);
                    } catch (Exception e) {
                        System.out.println("Error in exportToASCII- ML");
                    }
                }
            }

            out.close();
        } catch (Exception e) {
            System.out.println("Error in exportToASCII");
        }
    }

    private void writeItem(Seed seed, Item item, BufferedWriter out) throws IOException {
        switch (item.property) {
            case GENERAL_INFO:
                writeGeneralInfo(seed, item, out);
                break;
            case TIME_DEPENDENT:
                writeTimeDependent(seed, item, out);
                break;
            case TIME_INDEPENDENT:
                writeTimeIndependent(seed, item, out);
                break;
            default:
                System.out.println("Error in writeItem");
        }
    }

    private void writeGeneralInfo(Seed seed, Item item, BufferedWriter out) throws IOException {
        writeHeader(item.headerID, out);
        switch (item.dataType) {
            case FLOAT:
                out.write(formatter.format(seed.getValueFloat(item.coreEngineID)));
                break;
            default:
                out.write(seed.getValueString(item.coreEngineID));
                break;
        }

        out.newLine();
    }

    private void writeTimeDependent(Seed seed, Item item, BufferedWriter out) throws IOException {
        writeHeader(item.headerID, out);
        writeSegmentIndex(seed, out);

        switch (item.dataType) {
            case FLOAT:
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    out.write(alignString("t=" + (period + 1)));
                    for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                        out.write(alignString(formatter.format(seed.getValueFloat(item.coreEngineID, seg, period))));
                    }
                    out.newLine();
                }
                break;
            default:
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    out.write(alignString("t=" + (period + 1)));
                    for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                        out.write(alignString(seed.getValueString(item.coreEngineID, seg, period)));
                    }
                    out.newLine();
                }
                break;
        }
    }

    private void writeTimeIndependent(Seed seed, Item item, BufferedWriter out) throws IOException {
        writeHeader(item.headerID, out);
        writeSegmentIndex(seed, out);

        switch (item.dataType) {
            case FLOAT:
                out.write(alignString(""));
                for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                    out.write(alignString(formatter.format(seed.getValueFloat(item.coreEngineID, seg))));
                }
                out.newLine();
                break;
            default:
                out.write(alignString(""));
                for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                    out.write(alignString(seed.getValueString(item.coreEngineID, seg)));
                }
                out.newLine();
                break;
        }
    }

    private void writeHeader(String headerID, BufferedWriter out) throws IOException {
        out.write("<" + headerID + "> " + idToHeaderMap.get(headerID));
        out.newLine();
    }

    private void writeSegmentIndex(Seed seed, BufferedWriter out) throws IOException {
        out.write(alignString("Seg."));
        for (int index = 1; index <= seed.getValueInt(CEConst.IDS_NUM_SEGMENT); index++) {
            out.write(alignString("#" + index));
        }
        out.newLine();
    }

    private String alignString(String str) {
        if (str.length() < ITEM_WIDTH) {
            String result = "";
            for (int i = 0; i < ITEM_WIDTH - str.length(); i++) {
                result += " ";
            }
            return result + str;
        } else {
            return " " + str;
        }
    }
    // </editor-fold>

    private class Item {

        final String headerID;
        final String coreEngineID;
        final int property;
        final int dataType;

        public Item(String headerID, String coreEngineID, int property, int dataType) {
            this.headerID = headerID;
            this.coreEngineID = coreEngineID;
            this.property = property;
            this.dataType = dataType;
        }
    }

    /* Static main method for testing purposes only */
    /**
     * Unit test for this class
     *
     * @param args command line arguments (not in use)
     */
    public static void main(String[] args) {
        System.out.println("Starting Test");
        ASCIISeedFileAdapter seedFile = new ASCIISeedFileAdapter();
        Seed i40 = seedFile.importFromASCII("/Users/Shu/i40newtest.txt");
        //seedFile.exportToFile(Paths.get("/Users/Shu/out.txt"), i40);
        //System.out.println("Finished");
    }
}
