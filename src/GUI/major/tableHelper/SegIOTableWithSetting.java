package GUI.major.tableHelper;

import GUI.major.MainWindow;
import coreEngine.CEConst;
import coreEngine.Seed;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * This class is contains tables for segment input and output
 *
 * @author Shu Liu
 */
public class SegIOTableWithSetting implements FREEVAL_TableWithSetting {

    // <editor-fold defaultstate="collapsed" desc="CORE FUNCTIONS">
    /**
     * Constructor Create 4 table models and 2 table
     */
    public SegIOTableWithSetting() {
        _resetCellSettings();

        firstColumnModel = new SegIOTableModel(true, this);
        //firstColumnModelOutput = new SegIOTableModel(true, false, this);
        firstColumnTable = new FREEVAL_JTable(firstColumnModel);

        restColumnModel = new SegIOTableModel(false, this);
        //restColumnModelOutput = new SegIOTableModel(false, false, this);
        restColumnTable = new FREEVAL_JTable(restColumnModel);

        restColumnTable.setColumnSelectionAllowed(true);
        restColumnTable.setRowSelectionAllowed(false);

        textFieldForCellEditor.setHorizontalAlignment(JTextField.CENTER);
        textFieldForCellEditor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textFieldForCellEditor.selectAll();
            }
        });
        textFieldForCellEditor.setBorder(null);
        textFieldForCellEditor.setFont(MainWindow.getTableFont());
    }

    /**
     * Getter for first column table
     *
     * @return first column table
     */
    @Override
    public FREEVAL_JTable getFirstColumnTable() {
        return firstColumnTable;
    }

    /**
     * Getter for rest column table
     *
     * @return rest column table
     */
    @Override
    public FREEVAL_JTable getRestColumnTable() {
        return restColumnTable;
    }

    /**
     * Show input
     */
    public void showInput() {
        isInput = true;
        //firstColumnTable.setModel(firstColumnModel);
        //restColumnTable.setModel(restColumnModel);
        update();
    }

    /**
     * Show output
     */
    public void showOutput() {
        isInput = false;
        //firstColumnTable.setModel(firstColumnModelOutput);
        //restColumnTable.setModel(restColumnModelOutput);
        update();
    }

    /**
     * Getter for cell editor
     *
     * @param isFirstColumn whether it is first column model
     * @param isInput whether this model is used to show input
     * @param row row index
     * @param col column index
     * @return cell editor
     */
    public TableCellEditor getCellEditor(boolean isFirstColumn, boolean isInput, int row, int col) {
        TableCellSetting setting = findCellSetting(row, isInput);

        if (isFirstColumn) {
            return defaultCellEditor;
        } else {
            int segTypeGP = seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, col);
            int segTypeML = 0;
            if (seed.isManagedLaneUsed()) {
                segTypeML = seed.getValueInt(CEConst.IDS_ML_SEGMENT_TYPE, col);
            }
            switch (setting.identifier) {
                case CEConst.IDS_SEGMENT_TYPE:
                case CEConst.IDS_ML_SEGMENT_TYPE:
                    if (seed.isManagedLaneUsed()) {
                        tableSegTypeEditor.switchToGPAndML();
                    } else {
                        tableSegTypeEditor.switchToGPOnly();
                    }
                    return tableSegTypeEditor;
                case CEConst.IDS_ML_SEPARATION_TYPE:
                    return tableSeparationTypeEditor;
                case CEConst.IDS_ML_METHOD_TYPE:
                    return tableMLMethodTypeEditor;
                case CEConst.IDS_ON_RAMP_SIDE:
                    if (segTypeGP == CEConst.SEG_TYPE_ONR || segTypeGP == CEConst.SEG_TYPE_W) {
                        return tableRampSideEditor;
                    } else {
                        return defaultCellEditor;
                    }
                case CEConst.IDS_ML_ON_RAMP_SIDE:
                    if (segTypeML == CEConst.SEG_TYPE_ONR || segTypeML == CEConst.SEG_TYPE_W) {
                        return tableRampSideEditor;
                    } else {
                        return defaultCellEditor;
                    }
                case CEConst.IDS_OFF_RAMP_SIDE:
                    if (segTypeGP == CEConst.SEG_TYPE_OFR || segTypeGP == CEConst.SEG_TYPE_W) {
                        return tableRampSideEditor;
                    } else {
                        return defaultCellEditor;
                    }
                case CEConst.IDS_ML_OFF_RAMP_SIDE:
                    if (segTypeML == CEConst.SEG_TYPE_OFR || segTypeML == CEConst.SEG_TYPE_W) {
                        return tableRampSideEditor;
                    } else {
                        return defaultCellEditor;
                    }
                case CEConst.IDS_TERRAIN:
                    return tableTerrainEditor;
                case CEConst.IDS_ML_HAS_CROSS_WEAVE:
                    return tableCheckBoxEditor;
                default:
                    return defaultCellEditor;
            }
        }
    }

    /**
     * Getter for cell render
     *
     * @param isFirstColumn whether it is first column model
     * @param row row index
     * @param col column index
     * @return cell render
     */
    public TableCellRenderer getCellRenderer(boolean isFirstColumn, int row, int col) {
        TableCellSetting setting = findCellSetting(row, isInput);

        if (isFirstColumn) {
            return tableFirstColumnRenderer;
        } else {
            int segTypeGP = seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, col);
            int segTypeML = 0;
            if (seed.isManagedLaneUsed()) {
                segTypeML = seed.getValueInt(CEConst.IDS_ML_SEGMENT_TYPE, col);
            }
            switch (setting.identifier) {
                case CEConst.IDS_SEGMENT_TYPE:
                case CEConst.IDS_TYPE_USED:
                case CEConst.IDS_ML_SEGMENT_TYPE:
                case CEConst.IDS_ML_TYPE_USED:
                    return tableSegTypeRenderer;
                case CEConst.IDS_ML_SEPARATION_TYPE:
                    return tableSeparationTypeRenderer;
                case CEConst.IDS_ML_METHOD_TYPE:
                    return tableMLMethodTypeRenderer;
                case CEConst.IDS_ON_RAMP_SIDE:
                    if (segTypeGP == CEConst.SEG_TYPE_ONR || segTypeGP == CEConst.SEG_TYPE_W) {
                        return tableRampSideRenderer;
                    } else {
                        return tableNumAndStringRenderer;
                    }
                case CEConst.IDS_ML_ON_RAMP_SIDE:
                    if (segTypeML == CEConst.SEG_TYPE_ONR || segTypeML == CEConst.SEG_TYPE_W) {
                        return tableRampSideRenderer;
                    } else {
                        return tableNumAndStringRenderer;
                    }
                case CEConst.IDS_OFF_RAMP_SIDE:
                    if (segTypeGP == CEConst.SEG_TYPE_OFR || segTypeGP == CEConst.SEG_TYPE_W) {
                        return tableRampSideRenderer;
                    } else {
                        return tableNumAndStringRenderer;
                    }
                case CEConst.IDS_ML_OFF_RAMP_SIDE:
                    if (segTypeML == CEConst.SEG_TYPE_OFR || segTypeML == CEConst.SEG_TYPE_W) {
                        return tableRampSideRenderer;
                    } else {
                        return tableNumAndStringRenderer;
                    }
                case CEConst.IDS_TERRAIN:
                    return tableTerrainRenderer;
                case CEConst.IDS_ML_HAS_CROSS_WEAVE:
                    return tableCheckBoxRenderer;
                default:
                    return tableNumAndStringRenderer;
            }
        }
    }

    /**
     * Getter for row count
     *
     * @param isInput whether this model is used to show input
     * @return row count
     */
    public int getRowCount(boolean isInput) {
        int count = 0;
        if (isInput) {
            for (TableCellSetting setting : settings) {
                if (setting.showInInput && ((setting.showInGP && showGP) || (setting.showInML && showML))) {
                    count++;
                }
            }
        } else {
            for (TableCellSetting setting : settings) {
                if (setting.showInOutput && ((setting.showInGP && showGP) || (setting.showInML && showML))) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Getter for column count
     *
     * @param isFirstColumn whether it is first column model
     * @return column count
     */
    public int getColumnCount(boolean isFirstColumn) {
        return isFirstColumn ? (seed == null ? 0 : 1) : (seed == null ? 0 : seed.getValueInt(CEConst.IDS_NUM_SEGMENT));
    }

    /**
     * Getter for value at a cell
     *
     * @param isFirstColumn whether it is first column model
     * @param isInput whether this model is used to show input
     * @param row row index
     * @param col column index
     * @return value at a cell
     */
    public Object getValueAt(boolean isFirstColumn, boolean isInput, int row, int col) {
        TableCellSetting setting = findCellSetting(row, isInput);
        if (isFirstColumn) {
            return setting.header;
        } else {
            return seed.getValueString(setting.identifier, col, period, scen, atdm);
        }
    }

    /**
     * Getter for whether a cell is editable
     *
     * @param isFirstColumn whether it is first column model
     * @param isInput whether this model is used to show input
     * @param row row index
     * @param col column index
     * @return whether a cell is editable
     */
    public boolean isCellEditable(boolean isFirstColumn, boolean isInput, int row, int col) {
        return !isFirstColumn && findCellSetting(row, isInput).editable && !getValueAt(isFirstColumn, isInput, row, col).equals(CEConst.IDS_NA);
    }

    /**
     * Setter for value at a cell
     *
     * @param isFirstColumn whether it is first column model
     * @param isInput whether this model is used to show input
     * @param value new value
     * @param row row index
     * @param col column index
     */
    public void setValueAt(boolean isFirstColumn, boolean isInput, Object value, int row, int col) {
        TableCellSetting setting = findCellSetting(row, isInput);
        if (!isFirstColumn && setting.editable) {
            seed.setValue(setting.identifier, value, col, period, scen, atdm);
            mainWindow.seedDataChanged();
        }
    }

    /**
     * Getter for column header
     *
     * @param isFirstColumn whether it is first column model
     * @param isInput whether this model is used to show input
     * @param col column index
     * @return column header
     */
    public String getColumnName(boolean isFirstColumn, boolean isInput, int col) {
        if (isFirstColumn) {
            return "Segment";
        } else {
            return "Seg. " + Integer.toString(col + 1);
        }
    }

    /**
     * Show data for a particular seed, scenario and period
     *
     * @param seed seed to be displayed
     * @param scen index of scenario to be displayed
     * @param atdm
     * @param period index of period to be displayed
     */
    public void selectSeedScenPeriod(Seed seed, int scen, int atdm, int period) {
        this.seed = seed;
        this.scen = scen;
        this.atdm = atdm;
        this.period = period;
        update();
    }

    public void showGPOnly() {
        showGP = true;
        showML = false;
        update();
    }

    public void showMLOnly() {
        showGP = false;
        showML = true;
        update();
    }

    public void showGPML() {
        showGP = true;
        showML = true;
        update();
    }

    /**
     * Update table content
     */
    public void update() {
        autoConfigRowDisplay();
        firstColumnModel.fireTableStructureChanged();
        //firstColumnModelOutput.fireTableStructureChanged();
        restColumnModel.fireTableStructureChanged();
        //restColumnModelOutput.fireTableStructureChanged();
    }

    /**
     * Setter for MainWindow
     *
     * @param mainWindow MainWindow instance
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Getter for cell settings currently in use
     *
     * @return cell settings currently in use
     */
    public ArrayList<TableCellSetting> getCellSettings() {
        return settings;
    }

    /**
     * Setter for table font
     *
     * @param newTableFont new table font
     */
    public void setTableFont(Font newTableFont) {
        textFieldForCellEditor.setFont(newTableFont);
    }

    /**
     * Setter for new cell settings to be used
     *
     * @param settings new cell settings to be used
     */
    public void setCellSettings(ArrayList<TableCellSetting> settings) {
        if (settings != null) {
            this.settings = settings;
            update();
        }
    }

    /**
     * Auto configure row display to show or hide some rows
     */
    private void autoConfigRowDisplay() {
        if (seed != null) {
            for (TableCellSetting setting : settings) {
                switch (setting.identifier) {
                    case CEConst.IDS_LANE_WIDTH:
                    case CEConst.IDS_LATERAL_CLEARANCE:
                        //auto hide rows depending on whether free flow speed is known
                        setting.showInInput = !seed.isFreeFlowSpeedKnown();
                        setting.showInOutput = setting.showInOutput && !seed.isFreeFlowSpeedKnown();
                        break;
                    case CEConst.IDS_MAIN_FREE_FLOW_SPEED:
                        //auto hide rows depending on whether free flow speed is known
                        setting.showInInput = seed.isFreeFlowSpeedKnown();
                        setting.showInOutput = setting.showInOutput && seed.isFreeFlowSpeedKnown();
                        break;
                    case CEConst.IDS_ON_RAMP_METERING_RATE:
                        //auto hide rows depending on whether ramp metering is used
                        setting.showInInput = seed.isRampMeteringUsed();
                        setting.showInOutput = setting.showInOutput && seed.isRampMeteringUsed();
                        break;
                    case CEConst.IDS_RL_CAF_GP:
                    case CEConst.IDS_RL_OAF_GP:
                    case CEConst.IDS_RL_DAF_GP:
                    case CEConst.IDS_RL_SAF_GP:
                    case CEConst.IDS_RL_LAFI_GP:
                    case CEConst.IDS_RL_LAFWZ_GP:
                    case CEConst.IDS_ML_RLSCAF:
                    case CEConst.IDS_ML_RLSOAF:
                    case CEConst.IDS_ML_RLSDAF:
                    case CEConst.IDS_ML_RLSSAF:
                    case CEConst.IDS_ML_RLSLAF:
                        //auto hide rows depending on whether it is default scenario or generated RL or ATDM scenario
                        setting.showInInput = scen != 0;
                        setting.showInOutput = setting.showInOutput && scen != 0;
                        break;
                    case CEConst.IDS_ATDM_CAF:
                    case CEConst.IDS_ATDM_OAF:
                    case CEConst.IDS_ATDM_DAF:
                    case CEConst.IDS_ATDM_SAF:
                    case CEConst.IDS_ATDM_LAF:
                    case CEConst.IDS_ATDM_RM:
                        //auto hide rows depending on whether it is ATDM or not
                        setting.showInInput = atdm >= 0;
                        setting.showInOutput = setting.showInOutput && atdm >= 0;
                        break;

                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CELL SETTINGS">
    /**
     * Reset all cell settings to default
     */
    public void resetCellSettings() {
        _resetCellSettings();
    }

    private void _resetCellSettings() {
        //reset header strings
        // <editor-fold defaultstate="collapsed" desc="default GP header strings">
        // Basic Segment Variable Column Text
        STR_GP_HEADER = "General Purpose Segment Data";
        STR_SEGMENT_TYPE = "General Purpose Segment Type";
        STR_SEGMENT_LENGTH = "Segment Length (ft)";
        STR_SEGMENT_WIDTH = "Lane Width (ft)";
        STR_LATERAL_CLEARANCE = "Lateral Clearance (ft)";
        STR_TERRAIN = "Terrain";
        STR_TRUCK_CAR_EQ = "Truck-PC Equivalence (ET)";
        STR_RV_CAR_EQ = "RV-PC Equivalence (ER)";

        STR_NUM_LANES = "# of Lanes: Mainline";
        STR_FREE_FLOW_SPEED = "Free Flow Speed (mph)";
        STR_DEMAND_VEH = "Mainline Dem. (vph)";
        STR_TRUCK_PERCENTAGE = "Truck (%)";
        STR_RV_PERCENTAGE = "RV (%)";
        STR_U_CAF = "Seed Capacity Adj. Fac.";
        STR_U_OAF = "Seed Entering Dem. Adj. Fac.";
        STR_U_DAF = "Seed Exit Dem. Adj. Fac.";
        STR_U_SAF = "Seed Free Flow Speed Adj. Fac.";
        STR_RL_CAF = "RL Capacity Adj. Fac.";
        STR_RL_OAF = "RL Entering Dem. Adj. Fac.";
        STR_RL_DAF = "RL Exit Dem. Adj. Fac.";
        STR_RL_SAF = "RL Free Flow Speed Adj. Fac.";
        STR_RL_LAFI = "RL # Lanes Adj. Fac. I.";
        STR_RL_LAFWZ = "RL # Lanes Adj. Fac. WZ.";
        STR_ATDM_CAF = "ATDM Capacity Adj. Fac.";
        STR_ATDM_OAF = "ATDM Entering Dem. Adj. Fac.";
        STR_ATDM_DAF = "ATDM Exit Dem. Adj. Fac.";
        STR_ATDM_SAF = "ATDM Free Flow Speed Adj. Fac.";
        STR_ATDM_LAF = "ATDM # Lanes Adj. Fac.";
        STR_ATDM_RM = "ATDM Ramp Metering (vph)";

        // ONR Variable Column Text
        STR_ON_RAMP_SIDE = "ONR Side";
        STR_ACC_DEC_LANE_LENGTH = "Acc/Dec Lane Length (ft)";

        STR_NUM_ON_RAMP_LANES = "# Lanes: ONR";
        STR_ON_RAMP_DEMAND_VEH = "ONR/Entering Dem. (vph)";
        STR_ON_RAMP_FREE_FLOW_SPEED = "ONR Free Flow Speed (mph)";
        STR_ON_RAMP_METERING_RATE = "ONR Metering Rate (vph)";

        // OFR Variable Column Text
        STR_OFF_RAMP_SIDE = "OFR Side";

        STR_NUM_OFF_RAMP_LANES = "# Lanes: OFR";
        STR_OFF_RAMP_DEMAND_VEH = "OFR/Exit Dem. (vph)";
        STR_OFF_RAMP_FREE_FLOW_SPEED = "OFR Free Flow Speed (mph)";

        // Weaving Segment Variable Column Text
        STR_LENGTH_OF_WEAVING = "Weave Segment Ls (ft)";
        STR_MIN_LANE_CHANGE_ONR_TO_FRWY = "Weave Segment LCRF";
        STR_MIN_LANE_CHANGE_FRWY_TO_OFR = "Weave Segment LCFR";
        STR_MIN_LANE_CHANGE_ONR_TO_OFR = "Weave Segment LCRR";
        STR_NUM_LANES_WEAVING = "Weave Segment NW";

        STR_RAMP_TO_RAMP_DEMAND_VEH = "Ramp to Ramp Dem. (vph)";

        // Basic Segment Output Column Text
        STR_TYPE_USED = "Processed Segment Type";
        STR_SPEED = "Speed (mph)";
        STR_TOTAL_DENSITY_VEH = "Total Density (veh/mi/ln)";
        STR_TOTAL_DENSITY_PC = "Total Density (pc/mi/ln)";
        STR_INFLUENCED_DENSITY_PC = "Influence Area Density (pc/mi/ln)";
        STR_CAPACITY = "Adjusted Capacity (vph)";
        STR_ADJUSTED_DEMAND = "Adjusted Mainline Dem. (vph)";
        STR_DC = "D/C";
        STR_VOLUME_SERVED = "Mainline Volume Served (vph)";
        STR_VC = "V/C";
        STR_DENSITY_BASED_LOS = "Density Based LOS";
        STR_DEMAND_BASED_LOS = "Dem. Based LOS";
        STR_QUEUE_LENGTH = "Mainline Queue Length (ft)";
        STR_QUEUE_PERCENTAGE = "Mainline Queue Length (%)";
        STR_ON_QUEUE_VEH = "ONR Queue (veh)";

        STR_ACTUAL_TIME = "Actual Travel Time (min)";
        STR_FFS_TIME = "FFS Travel Time (min)";
        STR_MAINLINE_DELAY = "Mainline Delay (min)";
        STR_SYSTEM_DELAY = "System Delay (min)";
        STR_VMTD = "VMTD (veh-miles / interval)";
        STR_VMTV = "VMTV (veh-miles / interval)";
        STR_PMTD = "PMTD (p-miles / interval)";
        STR_PMTV = "PMTV (p-miles / interval)";
        STR_VHT = "VHT (travel / interval (hrs))";
        STR_VHD = "VHD (delay / interval (hrs))";
        STR_SPACE_MEAN_SPEED = "Space Mean Speed (mph)";
        STR_TRAVEL_TIME_INDEX = "Travel Time Index";

        // Special Output Column Text
        STR_ON_RAMP_CAPACITY = "ONR Capacity (vph)";
        STR_ADJUSTED_ON_RAMP_DEMAND = "Adjusted ONR Dem. (vph)";
        STR_ON_RAMP_VOLUME_SERVED = "ONR Volume Served (vph)";
        STR_OFF_RAMP_CAPACITY = "OFR Capacity (vph)";
        STR_ADJUSTED_OFF_RAMP_DEMAND = "Adjusted OFR Dem. (vph)";
        STR_OFF_RAMP_VOLUME_SERVED = "OFR Volume Served (vph)";
        STR_ON_RAMP_DELAY = "ONR Delay (min)";
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="default ML header strings">
        // Basic Segment Variable Column Text
        STR_ML_HEADER = "Managed Lanes Segment Data";
        STR_ML_SEGMENT_TYPE = "ML Segment Type";
        STR_ML_METHOD_TYPE = "ML Type of ML (HOV, HOT)";
        STR_ML_SEPARATION_TYPE = "ML Type of Separation";
        //STR_ML_SEGMENT_LENGTH = "ML Segment Length (ft)";

        STR_ML_NUM_LANES = "ML # of Lanes: Mainline";
        STR_ML_FREE_FLOW_SPEED = "ML Free Flow Speed (mph)";
        STR_ML_DEMAND_VEH = "ML Mainline Dem. (vph)";
        STR_ML_TRUCK_PERCENTAGE = "ML Truck (%)";
        STR_ML_RV_PERCENTAGE = "ML RV (%)";
        STR_ML_UCAF = "ML Seed Capacity Adj. Fac.";
        STR_ML_UOAF = "ML Seed Entering Dem. Adj. Fac.";
        STR_ML_UDAF = "ML Seed Exit Dem. Adj. Fac.";
        STR_ML_USAF = "ML Seed Free Flow Speed Adj. Fac.";
        STR_ML_SCAF = "ML RL Capacity Adj. Fac.";
        STR_ML_SOAF = "ML RL Entering Dem. Adj. Fac.";
        STR_ML_SDAF = "ML RL Exit Dem. Adj. Fac.";
        STR_ML_SSAF = "ML RL Free Flow Speed Adj. Fac.";
        STR_ML_SLAF = "ML RL # Lanes Adj. Fac. I.";
        STR_ML_ACC_DEC_LANE_LENGTH = "ML Acc/Dec Lane Length (ft)";

        // ONR Variable Column Text
        STR_ML_ON_RAMP_SIDE = "ML ONR Side";

        STR_ML_NUM_ON_RAMP_LANES = "ML # Lanes: ONR";
        STR_ML_ON_RAMP_DEMAND_VEH = "ML ONR/Entering Dem. (vph)";
        STR_ML_ON_RAMP_FREE_FLOW_SPEED = "ML ONR Free Flow Speed (mph)";

        // OFR Variable Column Text
        STR_ML_OFF_RAMP_SIDE = "ML OFR Side";

        STR_ML_NUM_OFF_RAMP_LANES = "ML # Lanes: OFR";
        STR_ML_OFF_RAMP_DEMAND_VEH = "ML OFR/Exiting Dem. (vph)";
        STR_ML_OFF_RAMP_FREE_FLOW_SPEED = "ML OFR Free Flow Speed (mph)";

        // Weaving Segment Variable Column Text
        STR_ML_LENGTH_SHORT = "ML Length Short (ft)";
        STR_ML_MIN_LANE_CHANGE_ONR_TO_FRWY = "ML Weave Segment LCRF";
        STR_ML_MIN_LANE_CHANGE_FRWY_TO_OFR = "ML Weave Segment LCFR";
        STR_ML_MIN_LANE_CHANGE_ONR_TO_OFR = "ML Weave Segment LCRR";
        STR_ML_NUM_LANES_WEAVING = "ML Weave Segment NW";
        STR_ML_LC_MIN = "ML Min Lane Change";
        STR_ML_LC_MAX = "ML Max Lane Change";
        STR_ML_RAMP_TO_RAMP_DEMAND_VEH = "ML Ramp to Ramp Dem. (vph)";

        STR_ML_HAS_CROSS_WEAVE = "Analysis of Cross Weave Effect";
        STR_ML_CROSS_WEAVE_LC_MIN = "Cross Weave LC-Min";
        STR_ML_CROSS_WEAVE_VOLUME = "Cross Weave Volume";

        // Basic Segment Output Column Text
        STR_ML_TYPE_USED = "ML Processed Segment Type";
        STR_ML_SPEED = "ML Speed (mph)";
        STR_ML_TOTAL_DENSITY_VEH = "ML Total Density (veh/mi/ln)";
        STR_ML_TOTAL_DENSITY_PC = "ML Total Density (pc/mi/ln)";
        STR_ML_INFLUENCED_DENSITY_PC = "ML Influence Area Density (pc/mi/ln)";
        STR_ML_CAPACITY = "ML Adjusted Capacity (vph)";
        STR_ML_ADJUSTED_DEMAND = "ML Adjusted Mainline Dem. (vph)";
        STR_ML_DC = "ML D/C";
        STR_ML_VOLUME_SERVED = "ML Mainline Volume Served (vph)";
        STR_ML_VC = "ML V/C";
        STR_ML_DENSITY_BASED_LOS = "ML Density Based LOS";
        STR_ML_DEMAND_BASED_LOS = "ML Dem. Based LOS";
        //STR_ML_QUEUE_LENGTH = "ML Mainline Queue Length (ft)";
        //STR_ML_QUEUE_PERCENTAGE = "ML Mainline Queue Length (%)";
        //STR_ML_ON_QUEUE_VEH = "ML ONR Queue (veh)";

        STR_ML_ACTUAL_TIME = "ML Actual Travel Time (min)";
        STR_ML_FFS_TIME = "ML FFS Travel Time (min)";
        STR_ML_MAINLINE_DELAY = "ML Mainline Delay (min)";
        STR_ML_SYSTEM_DELAY = "ML System Delay (min)";
        STR_ML_VMTD = "ML VMTD (veh-miles / interval)";
        STR_ML_VMTV = "ML VMTV (veh-miles / interval)";
        STR_ML_PMTD = "ML PMTD (p-miles / interval)";
        STR_ML_PMTV = "ML PMTV (p-miles / interval)";
        STR_ML_VHT = "ML VHT (travel / interval (hrs))";
        STR_ML_VHD = "ML VHD (delay / interval (hrs))";
        STR_ML_SPACE_MEAN_SPEED = "ML Space Mean Speed (mph)";
        STR_ML_TRAVEL_TIME_INDEX = "ML Travel Time Index";

        // Special Output Column Text
        STR_ML_ON_RAMP_CAPACITY = "ML ONR Capacity (vph)";
        STR_ML_ADJUSTED_ON_RAMP_DEMAND = "ML Adjusted ONR Dem. (vph)";
        STR_ML_ON_RAMP_VOLUME_SERVED = "ML ONR Volume Served (vph)";
        STR_ML_OFF_RAMP_CAPACITY = "ML OFR Capacity (vph)";
        STR_ML_ADJUSTED_OFF_RAMP_DEMAND = "ML Adjusted OFR Dem. (vph)";
        STR_ML_OFF_RAMP_VOLUME_SERVED = "ML OFR Volume Served (vph)";
        //STR_ML_ON_RAMP_DELAY = "ML ONR Delay (min)";
        // </editor-fold>

        //reset order and visibility
        // <editor-fold defaultstate="collapsed" desc="create default order and visibility">
        settings = new ArrayList<>();
        // <editor-fold defaultstate="collapsed" desc="GP Default">
        //basic fixed input data
        settings.add(new TableCellSetting(STR_GP_HEADER, CEConst.IDS_DASH, true, true, null, true, false, false));
        settings.add(new TableCellSetting(STR_SEGMENT_TYPE, CEConst.IDS_SEGMENT_TYPE, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_SEGMENT_LENGTH, CEConst.IDS_SEGMENT_LENGTH_FT, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_SEGMENT_WIDTH, CEConst.IDS_LANE_WIDTH, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_LATERAL_CLEARANCE, CEConst.IDS_LATERAL_CLEARANCE, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_TERRAIN, CEConst.IDS_TERRAIN, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_TRUCK_CAR_EQ, CEConst.IDS_TRUCK_CAR_ET, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_RV_CAR_EQ, CEConst.IDS_RV_CAR_ER, true, false, COLOR_GP_FIX_INPUT, true, false, true));

        //basic time depended input data
        settings.add(new TableCellSetting(STR_NUM_LANES, CEConst.IDS_MAIN_NUM_LANES_IN, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_FREE_FLOW_SPEED, CEConst.IDS_MAIN_FREE_FLOW_SPEED, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_DEMAND_VEH, CEConst.IDS_MAIN_DEMAND_VEH, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_TRUCK_PERCENTAGE, CEConst.IDS_TRUCK_PERCENTAGE, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_RV_PERCENTAGE, CEConst.IDS_RV_PERCENTAGE, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_U_CAF, CEConst.IDS_U_CAF_GP, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_U_OAF, CEConst.IDS_U_OAF_GP, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_U_DAF, CEConst.IDS_U_DAF_GP, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_U_SAF, CEConst.IDS_U_SAF_GP, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_RL_OAF, CEConst.IDS_RL_OAF_GP, true, false, COLOR_GP_SCENARIO_1, true, false, true));
        settings.add(new TableCellSetting(STR_RL_DAF, CEConst.IDS_RL_DAF_GP, true, false, COLOR_GP_SCENARIO_1, true, false, true));
        settings.add(new TableCellSetting(STR_RL_CAF, CEConst.IDS_RL_CAF_GP, true, false, COLOR_GP_SCENARIO_2, true, false, true));
        settings.add(new TableCellSetting(STR_RL_SAF, CEConst.IDS_RL_SAF_GP, true, false, COLOR_GP_SCENARIO_2, true, false, true));
        settings.add(new TableCellSetting(STR_RL_LAFI, CEConst.IDS_RL_LAFI_GP, true, false, COLOR_GP_SCENARIO_2, true, false, true));
        settings.add(new TableCellSetting(STR_RL_LAFWZ, CEConst.IDS_RL_LAFWZ_GP, true, false, COLOR_GP_SCENARIO_2, true, false, true));
        settings.add(new TableCellSetting(STR_ATDM_OAF, CEConst.IDS_ATDM_OAF, true, false, COLOR_GP_SCENARIO_1, true, false, true));
        settings.add(new TableCellSetting(STR_ATDM_DAF, CEConst.IDS_ATDM_DAF, true, false, COLOR_GP_SCENARIO_1, true, false, true));
        settings.add(new TableCellSetting(STR_ATDM_CAF, CEConst.IDS_ATDM_CAF, true, false, COLOR_GP_SCENARIO_2, true, false, true));
        settings.add(new TableCellSetting(STR_ATDM_SAF, CEConst.IDS_ATDM_SAF, true, false, COLOR_GP_SCENARIO_2, true, false, true));
        settings.add(new TableCellSetting(STR_ATDM_LAF, CEConst.IDS_ATDM_LAF, true, false, COLOR_GP_SCENARIO_2, true, false, true));
        settings.add(new TableCellSetting(STR_ATDM_RM, CEConst.IDS_ATDM_RM, true, false, COLOR_GP_SCENARIO_2, true, false, true));

        //special input data
        settings.add(new TableCellSetting(STR_ACC_DEC_LANE_LENGTH, CEConst.IDS_ACC_DEC_LANE_LENGTH, true, false, COLOR_GP_FIX_INPUT, true, false, true));

        settings.add(new TableCellSetting(STR_ON_RAMP_SIDE, CEConst.IDS_ON_RAMP_SIDE, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_NUM_ON_RAMP_LANES, CEConst.IDS_NUM_ON_RAMP_LANES, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_ON_RAMP_FREE_FLOW_SPEED, CEConst.IDS_ON_RAMP_FREE_FLOW_SPEED, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_ON_RAMP_DEMAND_VEH, CEConst.IDS_ON_RAMP_DEMAND_VEH, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_ON_RAMP_METERING_RATE, CEConst.IDS_ON_RAMP_METERING_RATE, true, false, COLOR_GP_TIME_INPUT, true, false, true));

        settings.add(new TableCellSetting(STR_OFF_RAMP_SIDE, CEConst.IDS_OFF_RAMP_SIDE, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_NUM_OFF_RAMP_LANES, CEConst.IDS_NUM_OFF_RAMP_LANES, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_OFF_RAMP_FREE_FLOW_SPEED, CEConst.IDS_OFF_RAMP_FREE_FLOW_SPEED, true, false, COLOR_GP_TIME_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_OFF_RAMP_DEMAND_VEH, CEConst.IDS_OFF_RAMP_DEMAND_VEH, true, false, COLOR_GP_TIME_INPUT, true, false, true));

        settings.add(new TableCellSetting(STR_LENGTH_OF_WEAVING, CEConst.IDS_LENGTH_OF_WEAVING, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_MIN_LANE_CHANGE_ONR_TO_FRWY, CEConst.IDS_MIN_LANE_CHANGE_ONR_TO_FRWY, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_MIN_LANE_CHANGE_FRWY_TO_OFR, CEConst.IDS_MIN_LANE_CHANGE_FRWY_TO_OFR, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_MIN_LANE_CHANGE_ONR_TO_OFR, CEConst.IDS_MIN_LANE_CHANGE_ONR_TO_OFR, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_NUM_LANES_WEAVING, CEConst.IDS_NUM_LANES_WEAVING, true, false, COLOR_GP_FIX_INPUT, true, false, true));
        settings.add(new TableCellSetting(STR_RAMP_TO_RAMP_DEMAND_VEH, CEConst.IDS_RAMP_TO_RAMP_DEMAND_VEH, true, false, COLOR_GP_TIME_INPUT, true, false, true));

        //basic and special output data
        settings.add(new TableCellSetting(STR_TYPE_USED, CEConst.IDS_TYPE_USED, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_SPEED, CEConst.IDS_SPEED, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_TOTAL_DENSITY_VEH, CEConst.IDS_TOTAL_DENSITY_VEH, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_TOTAL_DENSITY_PC, CEConst.IDS_TOTAL_DENSITY_PC, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_INFLUENCED_DENSITY_PC, CEConst.IDS_INFLUENCED_DENSITY_PC, false, true, COLOR_GP_OUTPUT, true, false, false));

        settings.add(new TableCellSetting(STR_CAPACITY, CEConst.IDS_MAIN_CAPACITY, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_ADJUSTED_DEMAND, CEConst.IDS_ADJUSTED_MAIN_DEMAND, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_DC, CEConst.IDS_DC, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_VOLUME_SERVED, CEConst.IDS_MAIN_VOLUME_SERVED, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_VC, CEConst.IDS_VC, false, true, COLOR_GP_OUTPUT, true, false, false));

        settings.add(new TableCellSetting(STR_ADJUSTED_ON_RAMP_DEMAND, CEConst.IDS_ADJUSTED_ON_RAMP_DEMAND, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_ON_RAMP_CAPACITY, CEConst.IDS_ON_RAMP_CAPACITY, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_ON_RAMP_VOLUME_SERVED, CEConst.IDS_ON_RAMP_VOLUME_SERVED, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_ADJUSTED_OFF_RAMP_DEMAND, CEConst.IDS_ADJUSTED_OFF_RAMP_DEMAND, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_OFF_RAMP_CAPACITY, CEConst.IDS_OFF_RAMP_CAPACITY, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_OFF_RAMP_VOLUME_SERVED, CEConst.IDS_OFF_RAMP_VOLUME_SERVED, false, true, COLOR_GP_OUTPUT, true, false, false));

        settings.add(new TableCellSetting(STR_DENSITY_BASED_LOS, CEConst.IDS_DENSITY_BASED_LOS, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_DEMAND_BASED_LOS, CEConst.IDS_DEMAND_BASED_LOS, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_QUEUE_LENGTH, CEConst.IDS_QUEUE_LENGTH, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_QUEUE_PERCENTAGE, CEConst.IDS_QUEUE_PERCENTAGE, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_ON_QUEUE_VEH, CEConst.IDS_ON_QUEUE_VEH, false, true, COLOR_GP_OUTPUT, true, false, false));

        settings.add(new TableCellSetting(STR_ACTUAL_TIME, CEConst.IDS_ACTUAL_TIME, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_FFS_TIME, CEConst.IDS_FFS_TIME, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_MAINLINE_DELAY, CEConst.IDS_MAINLINE_DELAY, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_ON_RAMP_DELAY, CEConst.IDS_ON_RAMP_DELAY, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_SYSTEM_DELAY, CEConst.IDS_SYSTEM_DELAY, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_VMTD, CEConst.IDS_VMTD, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_VMTV, CEConst.IDS_VMTV, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_PMTD, CEConst.IDS_PMTD, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_PMTV, CEConst.IDS_PMTV, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_VHT, CEConst.IDS_VHT, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_VHD, CEConst.IDS_VHD, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_SPACE_MEAN_SPEED, CEConst.IDS_SPACE_MEAN_SPEED, false, true, COLOR_GP_OUTPUT, true, false, false));
        settings.add(new TableCellSetting(STR_TRAVEL_TIME_INDEX, CEConst.IDS_TRAVEL_TIME_INDEX, false, true, COLOR_GP_OUTPUT, true, false, false));
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="ML Default">
        //ML input
        settings.add(new TableCellSetting(STR_ML_HEADER, CEConst.IDS_DASH, true, true, null, false, true, false));
        settings.add(new TableCellSetting(STR_ML_SEGMENT_TYPE, CEConst.IDS_ML_SEGMENT_TYPE, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        //settings.add(new TableCellSetting(STR_ML_METHOD_TYPE, CEConst.IDS_ML_METHOD_TYPE, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_SEPARATION_TYPE, CEConst.IDS_ML_SEPARATION_TYPE, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        //settings.add(new CellSetting(STR_ML_SEGMENT_LENGTH, CEConst.IDS_ML_SEGMENT_LENGTH_FT, true, false, COLOR_ML_FIX_INPUT, false, true, true));

        settings.add(new TableCellSetting(STR_ML_NUM_LANES, CEConst.IDS_ML_NUM_LANES, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_FREE_FLOW_SPEED, CEConst.IDS_ML_FREE_FLOW_SPEED, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_DEMAND_VEH, CEConst.IDS_ML_DEMAND_VEH, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_TRUCK_PERCENTAGE, CEConst.IDS_ML_TRUCK_PERCENTAGE, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_RV_PERCENTAGE, CEConst.IDS_ML_RV_PERCENTAGE, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_UCAF, CEConst.IDS_ML_UCAF, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_UOAF, CEConst.IDS_ML_UOAF, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_UDAF, CEConst.IDS_ML_UDAF, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_USAF, CEConst.IDS_ML_USAF, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_SOAF, CEConst.IDS_ML_RLSOAF, true, false, COLOR_ML_SCENARIO_1, false, true, true));
        settings.add(new TableCellSetting(STR_ML_SDAF, CEConst.IDS_ML_RLSDAF, true, false, COLOR_ML_SCENARIO_1, false, true, true));
        settings.add(new TableCellSetting(STR_ML_SCAF, CEConst.IDS_ML_RLSCAF, true, false, COLOR_ML_SCENARIO_2, false, true, true));
        settings.add(new TableCellSetting(STR_ML_SSAF, CEConst.IDS_ML_RLSSAF, true, false, COLOR_ML_SCENARIO_2, false, true, true));
        settings.add(new TableCellSetting(STR_ML_SLAF, CEConst.IDS_ML_RLSLAF, true, false, COLOR_ML_SCENARIO_2, false, true, true));

        settings.add(new TableCellSetting(STR_ML_ACC_DEC_LANE_LENGTH, CEConst.IDS_ML_ACC_DEC_LANE_LENGTH, true, false, COLOR_ML_FIX_INPUT, false, true, true));

        settings.add(new TableCellSetting(STR_ML_ON_RAMP_SIDE, CEConst.IDS_ML_ON_RAMP_SIDE, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_NUM_ON_RAMP_LANES, CEConst.IDS_ML_NUM_ON_RAMP_LANES, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_ON_RAMP_FREE_FLOW_SPEED, CEConst.IDS_ML_ON_RAMP_FREE_FLOW_SPEED, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_ON_RAMP_DEMAND_VEH, CEConst.IDS_ML_ON_RAMP_DEMAND_VEH, true, false, COLOR_ML_TIME_INPUT, false, true, true));

        settings.add(new TableCellSetting(STR_ML_OFF_RAMP_SIDE, CEConst.IDS_ML_OFF_RAMP_SIDE, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_NUM_OFF_RAMP_LANES, CEConst.IDS_ML_NUM_OFF_RAMP_LANES, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_OFF_RAMP_FREE_FLOW_SPEED, CEConst.IDS_ML_OFF_RAMP_FREE_FLOW_SPEED, true, false, COLOR_ML_TIME_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_OFF_RAMP_DEMAND_VEH, CEConst.IDS_ML_OFF_RAMP_DEMAND_VEH, true, false, COLOR_ML_TIME_INPUT, false, true, true));

        settings.add(new TableCellSetting(STR_ML_LENGTH_SHORT, CEConst.IDS_ML_LENGTH_SHORT, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_MIN_LANE_CHANGE_ONR_TO_FRWY, CEConst.IDS_ML_MIN_LANE_CHANGE_ONR_TO_FRWY, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_MIN_LANE_CHANGE_FRWY_TO_OFR, CEConst.IDS_ML_MIN_LANE_CHANGE_FRWY_TO_OFR, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_MIN_LANE_CHANGE_ONR_TO_OFR, CEConst.IDS_ML_MIN_LANE_CHANGE_ONR_TO_OFR, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_NUM_LANES_WEAVING, CEConst.IDS_ML_NUM_LANES_WEAVING, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_RAMP_TO_RAMP_DEMAND_VEH, CEConst.IDS_ML_RAMP_TO_RAMP_DEMAND_VEH, true, false, COLOR_ML_TIME_INPUT, false, true, true));

        settings.add(new TableCellSetting(STR_ML_LC_MIN, CEConst.IDS_ML_MIN_LANE_CHANGE_ML, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_LC_MAX, CEConst.IDS_ML_MAX_LANE_CHANGE_ML, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_HAS_CROSS_WEAVE, CEConst.IDS_ML_HAS_CROSS_WEAVE, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_CROSS_WEAVE_LC_MIN, CEConst.IDS_ML_CROSS_WEAVE_LC_MIN, true, false, COLOR_ML_FIX_INPUT, false, true, true));
        settings.add(new TableCellSetting(STR_ML_CROSS_WEAVE_VOLUME, CEConst.IDS_ML_CROSS_WEAVE_VOLUME, true, false, COLOR_ML_TIME_INPUT, false, true, true));

        //ML output
        settings.add(new TableCellSetting(STR_ML_TYPE_USED, CEConst.IDS_ML_TYPE_USED, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_SPEED, CEConst.IDS_ML_SPEED, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_TOTAL_DENSITY_VEH, CEConst.IDS_ML_TOTAL_DENSITY_VEH, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_TOTAL_DENSITY_PC, CEConst.IDS_ML_TOTAL_DENSITY_PC, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_INFLUENCED_DENSITY_PC, CEConst.IDS_ML_INFLUENCED_DENSITY_PC, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_CAPACITY, CEConst.IDS_ML_MAIN_CAPACITY, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_ADJUSTED_DEMAND, CEConst.IDS_ML_ADJUSTED_MAIN_DEMAND, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_DC, CEConst.IDS_ML_DC, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_VOLUME_SERVED, CEConst.IDS_ML_MAIN_VOLUME_SERVED, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_VC, CEConst.IDS_ML_VC, false, true, COLOR_ML_OUTPUT, false, true, false));

        settings.add(new TableCellSetting(STR_ML_ON_RAMP_CAPACITY, CEConst.IDS_ML_ON_RAMP_CAPACITY, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_ADJUSTED_ON_RAMP_DEMAND, CEConst.IDS_ML_ADJUSTED_ON_RAMP_DEMAND, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_ON_RAMP_VOLUME_SERVED, CEConst.IDS_ML_ON_RAMP_VOLUME_SERVED, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_OFF_RAMP_CAPACITY, CEConst.IDS_ML_OFF_RAMP_CAPACITY, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_ADJUSTED_OFF_RAMP_DEMAND, CEConst.IDS_ML_ADJUSTED_OFF_RAMP_DEMAND, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_OFF_RAMP_VOLUME_SERVED, CEConst.IDS_ML_OFF_RAMP_VOLUME_SERVED, false, true, COLOR_ML_OUTPUT, false, true, false));

        settings.add(new TableCellSetting(STR_ML_DENSITY_BASED_LOS, CEConst.IDS_ML_DENSITY_BASED_LOS, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_DEMAND_BASED_LOS, CEConst.IDS_ML_DEMAND_BASED_LOS, false, true, COLOR_ML_OUTPUT, false, true, false));

        settings.add(new TableCellSetting(STR_ML_ACTUAL_TIME, CEConst.IDS_ML_ACTUAL_TIME, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_FFS_TIME, CEConst.IDS_ML_FFS_TIME, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_MAINLINE_DELAY, CEConst.IDS_ML_MAINLINE_DELAY, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_SYSTEM_DELAY, CEConst.IDS_ML_SYSTEM_DELAY, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_VMTD, CEConst.IDS_ML_VMTD, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_VMTV, CEConst.IDS_ML_VMTV, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_PMTD, CEConst.IDS_ML_PMTD, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_PMTV, CEConst.IDS_ML_PMTV, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_VHT, CEConst.IDS_ML_VHT, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_VHD, CEConst.IDS_ML_VHD, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_SPACE_MEAN_SPEED, CEConst.IDS_ML_SPACE_MEAN_SPEED, false, true, COLOR_ML_OUTPUT, false, true, false));
        settings.add(new TableCellSetting(STR_ML_TRAVEL_TIME_INDEX, CEConst.IDS_ML_TRAVEL_TIME_INDEX, false, true, COLOR_ML_OUTPUT, false, true, false));
        // </editor-fold>
        // </editor-fold>
    }

    // <editor-fold defaultstate="collapsed" desc="GP header strings">
    // Basic Segment Variable Column Text
    transient private String STR_GP_HEADER;
    transient private String STR_SEGMENT_TYPE;
    transient private String STR_SEGMENT_LENGTH;
    transient private String STR_SEGMENT_WIDTH;
    transient private String STR_LATERAL_CLEARANCE;
    transient private String STR_TERRAIN;
    transient private String STR_TRUCK_CAR_EQ;
    transient private String STR_RV_CAR_EQ;

    transient private String STR_NUM_LANES;
    transient private String STR_FREE_FLOW_SPEED;
    transient private String STR_DEMAND_VEH;
    transient private String STR_TRUCK_PERCENTAGE;
    transient private String STR_RV_PERCENTAGE;
    transient private String STR_U_CAF;
    transient private String STR_U_OAF;
    transient private String STR_U_DAF;
    transient private String STR_U_SAF;
    transient private String STR_RL_CAF;
    transient private String STR_RL_OAF;
    transient private String STR_RL_DAF;
    transient private String STR_RL_SAF;
    transient private String STR_RL_LAFI;
    transient private String STR_RL_LAFWZ;
    transient private String STR_ATDM_CAF;
    transient private String STR_ATDM_OAF;
    transient private String STR_ATDM_DAF;
    transient private String STR_ATDM_SAF;
    transient private String STR_ATDM_LAF;
    transient private String STR_ATDM_RM;

    transient private String STR_ACC_DEC_LANE_LENGTH;

    // ONR Variable Column Text
    transient private String STR_ON_RAMP_SIDE;

    transient private String STR_NUM_ON_RAMP_LANES;
    transient private String STR_ON_RAMP_DEMAND_VEH;
    transient private String STR_ON_RAMP_FREE_FLOW_SPEED;
    //transient private String STR_ON_RAMP_TRUCK_PERCENTAGE;
    //transient private String STR_ON_RAMP_RV_PERCENTAGE;
    transient private String STR_ON_RAMP_METERING_RATE;

    // OFR Variable Column Text
    transient private String STR_OFF_RAMP_SIDE;

    transient private String STR_NUM_OFF_RAMP_LANES;
    transient private String STR_OFF_RAMP_DEMAND_VEH;
    transient private String STR_OFF_RAMP_FREE_FLOW_SPEED;
    //transient private String STR_OFF_RAMP_TRUCK_PERCENTAGE;
    //transient private String STR_OFF_RAMP_RV_PERCENTAGE;

    // Weaving Segment Variable Column Text
    transient private String STR_LENGTH_OF_WEAVING;
    transient private String STR_MIN_LANE_CHANGE_ONR_TO_FRWY;
    transient private String STR_MIN_LANE_CHANGE_FRWY_TO_OFR;
    transient private String STR_MIN_LANE_CHANGE_ONR_TO_OFR;
    transient private String STR_NUM_LANES_WEAVING;

    transient private String STR_RAMP_TO_RAMP_DEMAND_VEH;

    // Basic Segment Output Column Text
    transient private String STR_TYPE_USED;
    transient private String STR_SPEED;
    transient private String STR_TOTAL_DENSITY_VEH;
    transient private String STR_TOTAL_DENSITY_PC;
    transient private String STR_INFLUENCED_DENSITY_PC;
    transient private String STR_CAPACITY;
    transient private String STR_ADJUSTED_DEMAND;
    transient private String STR_DC;
    transient private String STR_VOLUME_SERVED;
    transient private String STR_VC;
    transient private String STR_DENSITY_BASED_LOS;
    transient private String STR_DEMAND_BASED_LOS;
    transient private String STR_QUEUE_LENGTH;
    transient private String STR_QUEUE_PERCENTAGE;
    transient private String STR_ON_QUEUE_VEH;

    transient private String STR_ACTUAL_TIME;
    transient private String STR_FFS_TIME;
    transient private String STR_MAINLINE_DELAY;
    transient private String STR_SYSTEM_DELAY;
    transient private String STR_VMTD;
    transient private String STR_VMTV;
    transient private String STR_PMTD;
    transient private String STR_PMTV;
    transient private String STR_VHT;
    transient private String STR_VHD;
    transient private String STR_SPACE_MEAN_SPEED;
    transient private String STR_TRAVEL_TIME_INDEX;

    // Special Output Column Text
    transient private String STR_ON_RAMP_CAPACITY;
    transient private String STR_ADJUSTED_ON_RAMP_DEMAND;
    transient private String STR_ON_RAMP_VOLUME_SERVED;
    transient private String STR_OFF_RAMP_CAPACITY;
    transient private String STR_ADJUSTED_OFF_RAMP_DEMAND;
    transient private String STR_OFF_RAMP_VOLUME_SERVED;

    transient private String STR_ON_RAMP_DELAY;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ML header strings">
    // Basic Segment Variable Column Text
    transient private String STR_ML_HEADER;
    transient private String STR_ML_SEGMENT_TYPE;
    transient private String STR_ML_METHOD_TYPE;
    transient private String STR_ML_SEPARATION_TYPE;
    //transient private String STR_ML_SEGMENT_LENGTH;

    transient private String STR_ML_NUM_LANES;
    transient private String STR_ML_FREE_FLOW_SPEED;
    transient private String STR_ML_DEMAND_VEH;
    transient private String STR_ML_TRUCK_PERCENTAGE;
    transient private String STR_ML_RV_PERCENTAGE;
    transient private String STR_ML_UCAF;
    transient private String STR_ML_UOAF;
    transient private String STR_ML_UDAF;
    transient private String STR_ML_USAF;
    transient private String STR_ML_SCAF;
    transient private String STR_ML_SOAF;
    transient private String STR_ML_SDAF;
    transient private String STR_ML_SSAF;
    transient private String STR_ML_SLAF;

    transient private String STR_ML_ACC_DEC_LANE_LENGTH;

    // ONR Variable Column Text
    transient private String STR_ML_ON_RAMP_SIDE;

    transient private String STR_ML_NUM_ON_RAMP_LANES;
    transient private String STR_ML_ON_RAMP_DEMAND_VEH;
    transient private String STR_ML_ON_RAMP_FREE_FLOW_SPEED;

    // OFR Variable Column Text
    transient private String STR_ML_OFF_RAMP_SIDE;

    transient private String STR_ML_NUM_OFF_RAMP_LANES;
    transient private String STR_ML_OFF_RAMP_DEMAND_VEH;
    transient private String STR_ML_OFF_RAMP_FREE_FLOW_SPEED;

    // Weaving Segment Variable Column Text
    transient private String STR_ML_LENGTH_SHORT;
    transient private String STR_ML_MIN_LANE_CHANGE_ONR_TO_FRWY;
    transient private String STR_ML_MIN_LANE_CHANGE_FRWY_TO_OFR;
    transient private String STR_ML_MIN_LANE_CHANGE_ONR_TO_OFR;
    transient private String STR_ML_NUM_LANES_WEAVING;
    transient private String STR_ML_LC_MIN;
    transient private String STR_ML_LC_MAX;

    transient private String STR_ML_RAMP_TO_RAMP_DEMAND_VEH;

    transient private String STR_ML_HAS_CROSS_WEAVE;
    transient private String STR_ML_CROSS_WEAVE_LC_MIN;
    transient private String STR_ML_CROSS_WEAVE_VOLUME;

    // Basic Segment Output Column Text
    transient private String STR_ML_TYPE_USED;
    transient private String STR_ML_SPEED;
    transient private String STR_ML_TOTAL_DENSITY_VEH;
    transient private String STR_ML_TOTAL_DENSITY_PC;
    transient private String STR_ML_INFLUENCED_DENSITY_PC;
    transient private String STR_ML_CAPACITY;
    transient private String STR_ML_ADJUSTED_DEMAND;
    transient private String STR_ML_DC;
    transient private String STR_ML_VOLUME_SERVED;
    transient private String STR_ML_VC;
    transient private String STR_ML_DENSITY_BASED_LOS;
    transient private String STR_ML_DEMAND_BASED_LOS;

    transient private String STR_ML_ACTUAL_TIME;
    transient private String STR_ML_FFS_TIME;
    transient private String STR_ML_MAINLINE_DELAY;
    transient private String STR_ML_SYSTEM_DELAY;
    transient private String STR_ML_VMTD;
    transient private String STR_ML_VMTV;
    transient private String STR_ML_PMTD;
    transient private String STR_ML_PMTV;
    transient private String STR_ML_VHT;
    transient private String STR_ML_VHD;
    transient private String STR_ML_SPACE_MEAN_SPEED;
    transient private String STR_ML_TRAVEL_TIME_INDEX;

    // Special Output Column Text
    transient private String STR_ML_ON_RAMP_CAPACITY;
    transient private String STR_ML_ADJUSTED_ON_RAMP_DEMAND;
    transient private String STR_ML_ON_RAMP_VOLUME_SERVED;
    transient private String STR_ML_OFF_RAMP_CAPACITY;
    transient private String STR_ML_ADJUSTED_OFF_RAMP_DEMAND;
    transient private String STR_ML_OFF_RAMP_VOLUME_SERVED;
    // </editor-fold>

    private ArrayList<TableCellSetting> settings;

    private static final Color COLOR_GP_FIX_INPUT = new Color(153, 255, 153);
    private static final Color COLOR_GP_TIME_INPUT = new Color(255, 255, 150); //new Color(255, 255, 0)
    private static final Color COLOR_GP_SCENARIO_1 = new Color(255, 200, 200); //Color.pink; //new Color(255, 175, 175)
    private static final Color COLOR_GP_SCENARIO_2 = new Color(255, 230, 230); //Color.orange; //new Color(255, 200, 0)
    private static final Color COLOR_GP_OUTPUT = Color.cyan; //new Color(0, 255, 255)

    private static final Color COLOR_ML_FIX_INPUT = new Color(0, 200, 100);
    private static final Color COLOR_ML_TIME_INPUT = new Color(255, 200, 0);
    private static final Color COLOR_ML_SCENARIO_1 = new Color(255, 120, 120);
    private static final Color COLOR_ML_SCENARIO_2 = new Color(255, 160, 160);
    private static final Color COLOR_ML_OUTPUT = new Color(0, 200, 255);

    private TableCellSetting findCellSetting(int row, boolean showInput) {
        //search for corrent item
        if (showInput) {
            int count = -1;
            for (TableCellSetting setting : settings) {
                if (setting.showInInput && ((setting.showInGP && showGP) || (setting.showInML && showML))) {
                    count++;
                    if (count == row) {
                        return setting;
                    }
                }
            }
        } else {
            int count = -1;
            for (TableCellSetting setting : settings) {
                if (setting.showInOutput && ((setting.showInGP && showGP) || (setting.showInML && showML))) {
                    count++;
                    if (count == row) {
                        return setting;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Getter for color at a row
     *
     * @param row row index
     * @return color at a row
     */
    public Color getColorAt(int row) {
        return findCellSetting(row, isInput).bgColor;
    }

    /**
     * Getter for cell setting at a row
     *
     * @param row row index
     * @return cell setting at a row
     */
    public TableCellSetting getCellSettingAt(int row) {
        return findCellSetting(row, isInput);
    }

    // <editor-fold defaultstate="collapsed" desc="CELL RENDERER AND EDITOR">
    private final TableFirstColumnRenderer tableFirstColumnRenderer = new TableFirstColumnRenderer(this);
    private final TableNumAndStringRenderer tableNumAndStringRenderer = new TableNumAndStringRenderer(this);
    private final TableSegTypeRenderer tableSegTypeRenderer = new TableSegTypeRenderer();
    private final TableRampSideRenderer tableRampSideRenderer = new TableRampSideRenderer();
    private final TableTerrainRenderer tableTerrainRenderer = new TableTerrainRenderer();
    private final TableCheckBoxRenderer tableCheckBoxRenderer = new TableCheckBoxRenderer();
    private final TableSeparationTypeRenderer tableSeparationTypeRenderer = new TableSeparationTypeRenderer();
    private final TableMLMethodTypeRenderer tableMLMethodTypeRenderer = new TableMLMethodTypeRenderer();

    private final JTextField textFieldForCellEditor = new JTextField();
    private final DefaultCellEditor defaultCellEditor = new DefaultCellEditor(textFieldForCellEditor);
    private final TableSegTypeEditor tableSegTypeEditor = new TableSegTypeEditor();
    private final TableTerrainEditor tableTerrainEditor = new TableTerrainEditor();
    private final TableRampSideEditor tableRampSideEditor = new TableRampSideEditor();
    private final TableCheckBoxEditor tableCheckBoxEditor = new TableCheckBoxEditor();
    private final TableSeparationTypeEditor tableSeparationTypeEditor = new TableSeparationTypeEditor();
    private final TableMLMethodTypeEditor tableMLMethodTypeEditor = new TableMLMethodTypeEditor();

    private class TableFirstColumnRenderer extends DefaultTableCellRenderer {

        SegIOTableWithSetting wrapper;

        public TableFirstColumnRenderer(SegIOTableWithSetting wrapper) {
            super();
            this.wrapper = wrapper;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {

            this.setForeground(null);
            this.setBackground(null);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            this.setHorizontalAlignment(JLabel.RIGHT);
            this.setBackground(wrapper.getColorAt(row));
            setText(value == null ? "null" : value.toString());

            return this;
        }
    }

    private class TableNumAndStringRenderer extends DefaultTableCellRenderer {

        SegIOTableWithSetting wrapper;

        public TableNumAndStringRenderer(SegIOTableWithSetting wrapper) {
            super();
            this.wrapper = wrapper;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {

            setForeground(null);
            setBackground(null);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);

            setHorizontalAlignment(JLabel.CENTER);
            TableCellSetting setting = wrapper.getCellSettingAt(row);
            if (setting == null) {
                setText(value.toString());
            } else {
                try {

                    tryInt(value.toString());
                    //only change color if it is an integer
                    switch (setting.identifier) {
                        case CEConst.IDS_SEGMENT_LENGTH_FT:
                        case CEConst.IDS_MAIN_DEMAND_VEH:
                        case CEConst.IDS_ON_RAMP_DEMAND_VEH:
                        case CEConst.IDS_OFF_RAMP_DEMAND_VEH:
                        case CEConst.IDS_RAMP_TO_RAMP_DEMAND_VEH:
                        case CEConst.IDS_LENGTH_OF_WEAVING:
                        case CEConst.IDS_MIN_LANE_CHANGE_ONR_TO_FRWY:
                        case CEConst.IDS_MIN_LANE_CHANGE_FRWY_TO_OFR:
                        case CEConst.IDS_MIN_LANE_CHANGE_ONR_TO_OFR:
                        case CEConst.IDS_NUM_LANES_WEAVING:

                        case CEConst.IDS_ML_SEGMENT_LENGTH_FT:
                        case CEConst.IDS_ML_DEMAND_VEH:
                        case CEConst.IDS_ML_ON_RAMP_DEMAND_VEH:
                        case CEConst.IDS_ML_OFF_RAMP_DEMAND_VEH:
                        case CEConst.IDS_ML_RAMP_TO_RAMP_DEMAND_VEH:
                        case CEConst.IDS_ML_LENGTH_SHORT:
                        case CEConst.IDS_ML_MIN_LANE_CHANGE_ONR_TO_FRWY:
                        case CEConst.IDS_ML_MIN_LANE_CHANGE_FRWY_TO_OFR:
                        case CEConst.IDS_ML_MIN_LANE_CHANGE_ONR_TO_OFR:
                        case CEConst.IDS_ML_NUM_LANES_WEAVING:
                        case CEConst.IDS_ML_CROSS_WEAVE_LC_MIN:
                        case CEConst.IDS_ML_CROSS_WEAVE_VOLUME:
                        case CEConst.IDS_ML_MIN_LANE_CHANGE_ML:
                        case CEConst.IDS_ML_MAX_LANE_CHANGE_ML:
                            this.setBackground(setting.bgColor);
                            break;
                    }
                } catch (IllegalArgumentException e1) {
                    try {
                        switch (setting.identifier) {
                            case CEConst.IDS_ADJUSTED_MAIN_DEMAND:
                            case CEConst.IDS_MAIN_CAPACITY:
                            case CEConst.IDS_MAIN_VOLUME_SERVED:
                            case CEConst.IDS_VMTD:
                            case CEConst.IDS_VMTV:
                            case CEConst.IDS_PMTD:
                            case CEConst.IDS_PMTV:
                            case CEConst.IDS_ON_RAMP_CAPACITY:
                            case CEConst.IDS_ADJUSTED_ON_RAMP_DEMAND:
                            case CEConst.IDS_ON_RAMP_VOLUME_SERVED:
                            case CEConst.IDS_OFF_RAMP_CAPACITY:
                            case CEConst.IDS_ADJUSTED_OFF_RAMP_DEMAND:
                            case CEConst.IDS_OFF_RAMP_VOLUME_SERVED:
                            case CEConst.IDS_ML_ADJUSTED_MAIN_DEMAND:
                            case CEConst.IDS_ML_MAIN_CAPACITY:
                            case CEConst.IDS_ML_MAIN_VOLUME_SERVED:
                            case CEConst.IDS_ML_VMTD:
                            case CEConst.IDS_ML_VMTV:
                            case CEConst.IDS_ML_PMTD:
                            case CEConst.IDS_ML_PMTV:
                            case CEConst.IDS_ML_ON_RAMP_CAPACITY:
                            case CEConst.IDS_ML_ADJUSTED_ON_RAMP_DEMAND:
                            case CEConst.IDS_ML_ON_RAMP_VOLUME_SERVED:
                            case CEConst.IDS_ML_OFF_RAMP_CAPACITY:
                            case CEConst.IDS_ML_ADJUSTED_OFF_RAMP_DEMAND:
                            case CEConst.IDS_ML_OFF_RAMP_VOLUME_SERVED:
                                tryFloat_0f(value.toString());
                                break;
                            case CEConst.IDS_SPEED:
                            case CEConst.IDS_TOTAL_DENSITY_VEH:
                            case CEConst.IDS_TOTAL_DENSITY_PC:
                            case CEConst.IDS_INFLUENCED_DENSITY_PC:
                            case CEConst.IDS_SPACE_MEAN_SPEED:
                            case CEConst.IDS_ML_SPEED:
                            case CEConst.IDS_ML_TOTAL_DENSITY_VEH:
                            case CEConst.IDS_ML_TOTAL_DENSITY_PC:
                            case CEConst.IDS_ML_INFLUENCED_DENSITY_PC:
                            case CEConst.IDS_ML_SPACE_MEAN_SPEED:
                                tryFloat_1f(value.toString());
                                break;
                            case CEConst.IDS_QUEUE_LENGTH:
                            case CEConst.IDS_ON_QUEUE_VEH:
                                tryFloat_0f_pos(value.toString());
                                break;
                            case CEConst.IDS_QUEUE_PERCENTAGE:
                                tryPercentage(value.toString());
                                break;
                            default:
                                tryFloat_2f(value.toString());
                        }
                    } catch (IllegalArgumentException e2) {
                        if (value.toString().equals(CEConst.IDS_NA)) {
                            this.setForeground(Color.darkGray);
                            this.setBackground(Color.darkGray);
                        }
                        setText(value.toString());
                    }
                }
            }

            return this;
        }

        private void tryPercentage(String value) {
            DecimalFormat formatter = new DecimalFormat("#,##0.0");
            if (Float.parseFloat(value) > CEConst.ZERO) {
                setText(formatter.format(Float.parseFloat(value) * 100) + "%");
            } else {
                setText("");
            }
        }

        private void tryInt(String value) {
            DecimalFormat formatter = new DecimalFormat("#,##0");
            setText(formatter.format(Integer.parseInt(value)));
        }

        private void tryFloat_2f(String value) {
            DecimalFormat formatter = new DecimalFormat("#,##0.00");
            setText(formatter.format(Float.parseFloat(value)));
        }

        private void tryFloat_1f(String value) {
            DecimalFormat formatter = new DecimalFormat("#,##0.0");
            setText(formatter.format(Float.parseFloat(value)));
        }

        private void tryFloat_0f(String value) {
            DecimalFormat formatter = new DecimalFormat("#,##0");
            setText(formatter.format(Float.parseFloat(value)));
        }

        private void tryFloat_0f_pos(String value) {
            DecimalFormat formatter = new DecimalFormat("#,##0");
            if (Float.parseFloat(value) > CEConst.ZERO) {
                setText(formatter.format(Float.parseFloat(value)));
            } else {
                setText("");
            }
        }
    }

    private class TableRampSideEditor extends AbstractCellEditor implements TableCellEditor {

        private final JComboBox rampSideCombox
                = new JComboBox(new String[]{CEConst.STR_RAMP_SIDE_LEFT, CEConst.STR_RAMP_SIDE_RIGHT});

        @Override
        public Object getCellEditorValue() {
            switch (rampSideCombox.getSelectedIndex()) {
                case 1:
                    return CEConst.RAMP_SIDE_RIGHT;
                default:
                    return CEConst.RAMP_SIDE_LEFT;
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            switch (Integer.parseInt(value.toString())) {
                case CEConst.RAMP_SIDE_RIGHT:
                    rampSideCombox.setSelectedIndex(1);
                    break;
                default:
                    rampSideCombox.setSelectedIndex(0);
                    break;
            }

            return rampSideCombox;
        }
    }

    private class TableRampSideRenderer extends DefaultTableCellRenderer {

        public TableRampSideRenderer() {
            super();
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public void setValue(Object value) {
            try {
                switch (Integer.parseInt(value.toString())) {
                    case CEConst.RAMP_SIDE_LEFT:
                        setText(CEConst.STR_RAMP_SIDE_LEFT);
                        break;
                    case CEConst.RAMP_SIDE_RIGHT:
                        setText(CEConst.STR_RAMP_SIDE_RIGHT);
                        break;
                    default:
                        setText("RS Error: " + value.toString());
                }
            } catch (NumberFormatException e) {
                setText("RS Error: " + value.toString());
            }
        }
    }

    private class TableSegTypeEditor extends AbstractCellEditor implements TableCellEditor {

        private final JComboBox SEG_TYPE_GP_DEFAULT = new JComboBox(new String[]{CEConst.STR_SEG_TYPE_B, CEConst.STR_SEG_TYPE_ONR,
            CEConst.STR_SEG_TYPE_OFR, CEConst.STR_SEG_TYPE_W, CEConst.STR_SEG_TYPE_R});
        private final JComboBox SEG_TYPE_GP_ML_ACS = new JComboBox(new String[]{CEConst.STR_SEG_TYPE_B, CEConst.STR_SEG_TYPE_ONR,
            CEConst.STR_SEG_TYPE_OFR, CEConst.STR_SEG_TYPE_W, CEConst.STR_SEG_TYPE_R, CEConst.STR_SEG_TYPE_ACS});

        private JComboBox segTypeCombox = SEG_TYPE_GP_DEFAULT;

        @Override
        public Object getCellEditorValue() {
            switch (segTypeCombox.getSelectedIndex()) {
                case 1:
                    return CEConst.SEG_TYPE_ONR;
                case 2:
                    return CEConst.SEG_TYPE_OFR;
                case 3:
                    return CEConst.SEG_TYPE_W;
                case 4:
                    return CEConst.SEG_TYPE_R;
                case 5:
                    return CEConst.SEG_TYPE_ACS;
                default:
                    return CEConst.SEG_TYPE_B;
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            switch (Integer.parseInt(value.toString())) {
                case CEConst.SEG_TYPE_ONR:
                    segTypeCombox.setSelectedIndex(1);
                    break;
                case CEConst.SEG_TYPE_OFR:
                    segTypeCombox.setSelectedIndex(2);
                    break;
                case CEConst.SEG_TYPE_W:
                    segTypeCombox.setSelectedIndex(3);
                    break;
                case CEConst.SEG_TYPE_R:
                    segTypeCombox.setSelectedIndex(4);
                    break;
                case CEConst.SEG_TYPE_ACS:
                    segTypeCombox.setSelectedIndex(5);
                    break;
                default:
                    segTypeCombox.setSelectedIndex(0);
                    break;
            }

            return segTypeCombox;
        }

        public void switchToGPOnly() {
            segTypeCombox = SEG_TYPE_GP_DEFAULT;
        }

        public void switchToGPAndML() {
            segTypeCombox = SEG_TYPE_GP_ML_ACS;
        }
    }

    private class TableSegTypeRenderer extends DefaultTableCellRenderer {

        public TableSegTypeRenderer() {
            super();
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public void setValue(Object value) {
            try {
                switch (Integer.parseInt(value.toString())) {
                    case CEConst.SEG_TYPE_B:
                        setText(CEConst.STR_SEG_TYPE_B);
                        break;
                    case CEConst.SEG_TYPE_ONR:
                        setText(CEConst.STR_SEG_TYPE_ONR);
                        break;
                    case CEConst.SEG_TYPE_ONR_B:
                        setText(CEConst.STR_SEG_TYPE_ONR_B);
                        break;
                    case CEConst.SEG_TYPE_OFR:
                        setText(CEConst.STR_SEG_TYPE_OFR);
                        break;
                    case CEConst.SEG_TYPE_OFR_B:
                        setText(CEConst.STR_SEG_TYPE_OFR_B);
                        break;
                    case CEConst.SEG_TYPE_R:
                        setText(CEConst.STR_SEG_TYPE_R);
                        break;
                    case CEConst.SEG_TYPE_W:
                        setText(CEConst.STR_SEG_TYPE_W);
                        break;
                    case CEConst.SEG_TYPE_W_B:
                        setText(CEConst.STR_SEG_TYPE_W_B);
                        break;
                    case CEConst.SEG_TYPE_ACS:
                        setText(CEConst.STR_SEG_TYPE_ACS);
                        break;
                    default:
                        setText("ST Error: " + value.toString());
                }
            } catch (NumberFormatException e) {
                setText("ST Error: " + value.toString());
            }
        }
    }

    private class TableTerrainEditor extends AbstractCellEditor implements TableCellEditor {

        private final JComboBox terrainCombox = new JComboBox(new String[]{CEConst.STR_TERRAIN_LEVEL, CEConst.STR_TERRAIN_MOUNTAINOUS,
            CEConst.STR_TERRAIN_ROLLING, CEConst.STR_TERRAIN_VARYING_OR_OTHER});

        @Override
        public Object getCellEditorValue() {
            switch (terrainCombox.getSelectedIndex()) {
                case 1:
                    return CEConst.TERRAIN_MOUNTAINOUS;
                case 2:
                    return CEConst.TERRAIN_ROLLING;
                case 3:
                    return CEConst.TERRAIN_VARYING_OR_OTHER;
                default:
                    return CEConst.TERRAIN_LEVEL;
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            switch (Integer.parseInt(value.toString())) {
                case CEConst.TERRAIN_MOUNTAINOUS:
                    terrainCombox.setSelectedIndex(1);
                    break;
                case CEConst.TERRAIN_ROLLING:
                    terrainCombox.setSelectedIndex(2);
                    break;
                case CEConst.TERRAIN_VARYING_OR_OTHER:
                    terrainCombox.setSelectedIndex(3);
                    break;
                default:
                    terrainCombox.setSelectedIndex(0);
                    break;
            }

            return terrainCombox;
        }
    }

    private class TableTerrainRenderer extends DefaultTableCellRenderer {

        public TableTerrainRenderer() {
            super();
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public void setValue(Object value) {
            try {
                switch (Integer.parseInt(value.toString())) {
                    case CEConst.TERRAIN_LEVEL:
                        setText(CEConst.STR_TERRAIN_LEVEL);
                        break;
                    case CEConst.TERRAIN_MOUNTAINOUS:
                        setText(CEConst.STR_TERRAIN_MOUNTAINOUS);
                        break;
                    case CEConst.TERRAIN_ROLLING:
                        setText(CEConst.STR_TERRAIN_ROLLING);
                        break;
                    case CEConst.TERRAIN_VARYING_OR_OTHER:
                        setText(CEConst.STR_TERRAIN_VARYING_OR_OTHER);
                        break;
                    default:
                        setText("TE Error: " + value.toString());
                }
            } catch (NumberFormatException e) {
                setText("TE Error: " + value.toString());
            }
        }
    }

    public class TableCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        TableCheckBoxRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelected(value != null && Boolean.parseBoolean(value.toString()));
            return this;
        }
    }

    private class TableCheckBoxEditor extends AbstractCellEditor implements TableCellEditor {

        private final JCheckBox checkBox = new JCheckBox();

        TableCheckBoxEditor() {
            checkBox.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Object getCellEditorValue() {
            return checkBox.isSelected();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            checkBox.setSelected(value != null && Boolean.parseBoolean(value.toString()));
            return checkBox;
        }
    }

    private class TableSeparationTypeEditor extends AbstractCellEditor implements TableCellEditor {

        private final JComboBox separationTypeCombox = new JComboBox(new String[]{CEConst.STR_ML_SEPARATION_MARKING, CEConst.STR_ML_SEPARATION_BUFFER,
            CEConst.STR_ML_SEPARATION_BARRIER});

        @Override
        public Object getCellEditorValue() {
            switch (separationTypeCombox.getSelectedIndex()) {
                case 1:
                    return CEConst.ML_SEPARATION_BUFFER;
                case 2:
                    return CEConst.ML_SEPARATION_BARRIER;
                default:
                    return CEConst.ML_SEPARATION_MARKING;
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            switch (Integer.parseInt(value.toString())) {
                case CEConst.ML_SEPARATION_BUFFER:
                    separationTypeCombox.setSelectedIndex(1);
                    break;
                case CEConst.ML_SEPARATION_BARRIER:
                    separationTypeCombox.setSelectedIndex(2);
                    break;
                default:
                    separationTypeCombox.setSelectedIndex(0);
                    break;
            }

            return separationTypeCombox;
        }
    }

    private class TableSeparationTypeRenderer extends DefaultTableCellRenderer {

        public TableSeparationTypeRenderer() {
            super();
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public void setValue(Object value) {
            try {
                switch (Integer.parseInt(value.toString())) {
                    case CEConst.ML_SEPARATION_MARKING:
                        setText(CEConst.STR_ML_SEPARATION_MARKING);
                        break;
                    case CEConst.ML_SEPARATION_BUFFER:
                        setText(CEConst.STR_ML_SEPARATION_BUFFER);
                        break;
                    case CEConst.ML_SEPARATION_BARRIER:
                        setText(CEConst.STR_ML_SEPARATION_BARRIER);
                        break;
                    default:
                        setText("ST Error: " + value.toString());
                }
            } catch (NumberFormatException e) {
                setText("ST Error: " + value.toString());
            }
        }
    }

    private class TableMLMethodTypeEditor extends AbstractCellEditor implements TableCellEditor {

        private final JComboBox MLMethodCombox = new JComboBox(new String[]{CEConst.STR_ML_METHOD_HOV, CEConst.STR_ML_METHOD_HOT});

        @Override
        public Object getCellEditorValue() {
            switch (MLMethodCombox.getSelectedIndex()) {
                case 1:
                    return CEConst.ML_METHOD_HOT;
                default:
                    return CEConst.ML_METHOD_HOV;
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            switch (Integer.parseInt(value.toString())) {
                case CEConst.ML_METHOD_HOT:
                    MLMethodCombox.setSelectedIndex(1);
                    break;
                default:
                    MLMethodCombox.setSelectedIndex(0);
                    break;
            }

            return MLMethodCombox;
        }
    }

    private class TableMLMethodTypeRenderer extends DefaultTableCellRenderer {

        public TableMLMethodTypeRenderer() {
            super();
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public void setValue(Object value) {
            try {
                switch (Integer.parseInt(value.toString())) {
                    case CEConst.ML_METHOD_HOV:
                        setText(CEConst.STR_ML_METHOD_HOV);
                        break;
                    case CEConst.ML_METHOD_HOT:
                        setText(CEConst.STR_ML_METHOD_HOT);
                        break;
                    default:
                        setText("ST Error: " + value.toString());
                }
            } catch (NumberFormatException e) {
                setText("ST Error: " + value.toString());
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="TABLE AND TABLE MODELS">
    private final FREEVAL_JTable firstColumnTable;
    private final FREEVAL_TableModel firstColumnModel;
    //private final FREEVAL_TableModel firstColumnModelOutput;
    private final FREEVAL_JTable restColumnTable;
    private final FREEVAL_TableModel restColumnModel;
    //private final FREEVAL_TableModel restColumnModelOutput;

    private class SegIOTableModel extends FREEVAL_TableModel {

        /**
         * Constructor
         *
         * @param isFirstColumn whether it is first column model
         * @param isInput whether this model is used for input
         * @param tableWithSetting the SegIOTableWithSetting object that
         * contains this model
         */
        public SegIOTableModel(boolean isFirstColumn,//, boolean isInput,
                SegIOTableWithSetting tableWithSetting) {
            this.isFirstColumn = isFirstColumn;
            //this.isInput = isInput;
            this.tableWithSetting = tableWithSetting;
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            return tableWithSetting.getCellEditor(isFirstColumn, isInput, row, column);
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            return tableWithSetting.getCellRenderer(isFirstColumn, row, column);
        }

        @Override
        public int getRowCount() {
            return tableWithSetting.getRowCount(isInput);
        }

        @Override
        public int getColumnCount() {
            return tableWithSetting.getColumnCount(isFirstColumn);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return tableWithSetting.getValueAt(isFirstColumn, isInput, rowIndex, columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return tableWithSetting.isCellEditable(isFirstColumn, isInput, row, column);
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            tableWithSetting.setValueAt(isFirstColumn, isInput, value, row, column);
        }

        @Override
        public String getColumnName(int column) {
            return tableWithSetting.getColumnName(isFirstColumn, isInput, column);
        }

        private final boolean isFirstColumn;
        //private final boolean isInput;
        private final SegIOTableWithSetting tableWithSetting;
    }
    // </editor-fold>

    private Seed seed;
    private int period = 0;
    private int scen = 0;
    private int atdm = -1;
    private boolean isInput = true;
    private boolean showGP = true;
    private boolean showML = false;

    private MainWindow mainWindow;
}
