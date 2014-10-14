/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.tableHelper;

import DSS.DataStruct.PeriodATM;
import GUI.major.MainWindow;
import GUI.major.MainWindowUser;
import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author jltrask
 */
public class FREEVAL_DSS_TableModel extends AbstractTableModel {

    //private String[] columnNames;
    private final String[] rowNames = {"Toggle Ramp Metering Used",
        "Remaining Implementation Periods",
        "Ramp Metering Rate",
        "Toggle Hard Shoulder Running Used",
        "Remaining Implementation Periods",};
        //"Hard Shoulder Capacity"};

    private MainWindowUser mainWindow;
    private Seed seed;

    private PeriodATM[] periodATM;

    private int currPeriod;

    private final CheckBoxRenderer checkBoxRenderer;
    private final DefaultTableCellRenderer centerRenderer;
    private final DefaultTableCellRenderer blackOutRenderer;
    private final DefaultTableCellRenderer rightRenderer;
    //private final ComboBoxRenderer rmComboBoxRenderer;
    
    private final TableSelectionCellEditor defaultCellEditor;
    private final DefaultCellEditor checkBoxEditor;
    private final DefaultCellEditor rmComboBoxEditor;
    
    private final JTable parentTable;

    private final int tableType;

    public static final int TYPE_ROW_NAMES = 0;
    public static final int TYPE_ATM_INPUT = 1;
    
    private static final int ROW_RM_TYPE = 0;
    private static final int ROW_RM_IMPLEMENTATION_PERIODS = 1;
    private static final int ROW_RM_RATE = 2;
    private static final int ROW_HSR_TOGGLE = 3;
    private static final int ROW_HSR_IMPLEMENTATION_PERIODS = 4;
    //private static final int ROW_HSR_CAPACITY = 5;

    public FREEVAL_DSS_TableModel(int tableType, JTable parentTable) {
        //this.seed = seed;
        //this.periodATM = periodATM;
        this.parentTable = parentTable;
        this.tableType = tableType;
        currPeriod = 0;

        checkBoxRenderer = new CheckBoxRenderer();
        checkBoxRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        blackOutRenderer = new DefaultTableCellRenderer();
        blackOutRenderer.setForeground(Color.DARK_GRAY);
        blackOutRenderer.setBackground(Color.DARK_GRAY);
        rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Creating Default cell editor
        defaultCellEditor = new TableSelectionCellEditor(true);
        
        // Creating CheckBox cell editor
        JCheckBox editorCB = new JCheckBox();
        editorCB.setHorizontalAlignment(JLabel.CENTER);
        editorCB.setBackground(Color.WHITE);
        editorCB.setForeground(Color.WHITE);
        checkBoxEditor = new DefaultCellEditor(editorCB);
        
        //Creating Ramp Metering ComboBoxEditor
        //rmComboBoxRenderer = new ComboBoxRenderer();
        //rmComboBoxRenderer.addItem("None");
        //rmComboBoxRenderer.addItem("User Specified");
        //rmComboBoxRenderer.addItem("Adaptive");
        // Creating Ramp Metering JComboBox Editor
        WideComboBox editorComboBox = new WideComboBox();
        editorComboBox.addItem("None");
        editorComboBox.addItem("User Specified");
        editorComboBox.addItem("Adaptive");
        rmComboBoxEditor = new DefaultCellEditor(editorComboBox);
    }

    //<editor-fold defaultstate="collapsed" desc="Overrides">
    @Override
    public int getColumnCount() {
        switch (tableType) {
            default:
            case TYPE_ROW_NAMES:
                return 1;
            case TYPE_ATM_INPUT:
                if (seed == null) {
                    return 0;
                } else {
                    return seed.getValueInt(CEConst.IDS_NUM_SEGMENT);
                }
        }
    }

    @Override
    public int getRowCount() {
        return rowNames.length;
    }

    @Override
    public String getColumnName(int col) {
        switch (tableType) {
            default:
            case TYPE_ROW_NAMES:
                return "Segment ATM";
            case TYPE_ATM_INPUT:
                return "Seg. " + (col + 1);
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (tableType) {
            default:
            case TYPE_ROW_NAMES:
                return rowNames[row];
            case TYPE_ATM_INPUT:
                switch (row) {
                    case ROW_RM_TYPE:
                        if (periodATM[currPeriod].getRMType(col) == PeriodATM.ID_RM_TYPE_USER) {
                                return "User Specified";
                            } else if (periodATM[currPeriod].getRMType(col) == PeriodATM.ID_RM_TYPE_ADAPTIVE) {
                                return "Adaptive";
                            } else {
                                return "None";
                            }
                    case ROW_RM_IMPLEMENTATION_PERIODS:
                        return periodATM[currPeriod].getRMDuration(col);
                    case ROW_RM_RATE:
                        return periodATM[currPeriod].getRMRate(col);
                    case ROW_HSR_TOGGLE:
                        return periodATM[currPeriod].getHSRUsed(col);
                    case ROW_HSR_IMPLEMENTATION_PERIODS:
                        return periodATM[currPeriod].getHSRDuration(col);
                    //case 5:
                    //    return periodATM[currPeriod].getHSRCapacity(col);
                    default:
                        throw new RuntimeException("Invalid Row Index");
                }
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        switch (tableType) {
            case TYPE_ATM_INPUT:
                try {
                    switch (row) {
                        case ROW_RM_IMPLEMENTATION_PERIODS:
                            periodATM[currPeriod].setRMDuration(Integer.parseInt((String) value), col);
                            break;
                        case ROW_RM_TYPE:
                            if (((String) value).equalsIgnoreCase("User Specified")) {
                                periodATM[currPeriod].setRMType(PeriodATM.ID_RM_TYPE_USER, col);
                                //parentTable.setColumnSelectionInterval(col, col);
                                //parentTable.setRowSelectionInterval(ROW_RM_IMPLEMENTATION_PERIODS,ROW_RM_IMPLEMENTATION_PERIODS);
                                parentTable.changeSelection(ROW_RM_IMPLEMENTATION_PERIODS, col, false, false);
                                //this.fireTableDataChanged();
                                //parentTable.editCellAt(ROW_RM_IMPLEMENTATION_PERIODS, col);
                            } else if (((String) value).equalsIgnoreCase("Adaptive")) {
                                periodATM[currPeriod].setRMType(PeriodATM.ID_RM_TYPE_ADAPTIVE, col);
                                //parentTable.setColumnSelectionInterval(col, col);
                                //parentTable.setRowSelectionInterval(ROW_RM_IMPLEMENTATION_PERIODS,ROW_RM_IMPLEMENTATION_PERIODS);
                                parentTable.changeSelection(ROW_RM_IMPLEMENTATION_PERIODS, col, false, false);
                            } else {
                                periodATM[currPeriod].setRMType(PeriodATM.ID_RM_TYPE_NONE, col);
                            }
                            break;
                        case ROW_RM_RATE:
                            periodATM[currPeriod].setRMRate(Integer.parseInt((String) value), col);
                            break;
                        case ROW_HSR_IMPLEMENTATION_PERIODS:
                            periodATM[currPeriod].setHSRDuration(Integer.parseInt((String) value), col);
                            break;
                        case ROW_HSR_TOGGLE:
                            periodATM[currPeriod].setHSRUsed((boolean) value, col);
                            break;
                        //case 5:
                        //    periodATM[currPeriod].setHSRCapacity(Integer.parseInt((String) value), col);
                        //    break;
                        default:
                            throw new RuntimeException("Invalid Row Index");
                    }
                } catch (NumberFormatException e) {
                    MainWindowUser.printLog("Invalid Value Entered");
                }
            default:
            // Do nothing, cells not editable
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        switch (tableType) {
            default:
            case TYPE_ROW_NAMES:
                return false;
            case TYPE_ATM_INPUT:
                switch (row) {
                    case ROW_RM_TYPE:
                        return (seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_ONR || seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_W);
                    case ROW_HSR_TOGGLE:
                        return true;
                    case ROW_RM_IMPLEMENTATION_PERIODS:
                    case ROW_RM_RATE:
                        return (periodATM[currPeriod].getRMUsed(col));
                    case ROW_HSR_IMPLEMENTATION_PERIODS:
                        return (periodATM[currPeriod].getHSRUsed(col));
                    default:
                        return false;
                }
        }
    }

    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
        //setupTable();
    }

    @Override
    public void fireTableStructureChanged() {
        super.fireTableStructureChanged();
        setupTable();
    }
//</editor-fold>

    public void setupTable() {
        parentTable.setFont(MainWindow.getTableFont());
        parentTable.setRowHeight(MainWindow.getTableFont().getSize() + 2);
    }

    // <editor-fold defaultstate="collapsed" desc="Cell Renderers">
    public TableCellRenderer getRenderer(int row, int col) {
        switch (tableType) {
            default:
            case TYPE_ROW_NAMES:
                return rightRenderer;
            case TYPE_ATM_INPUT:
                switch (row) {
                    case ROW_RM_IMPLEMENTATION_PERIODS:
                    case ROW_RM_RATE:
                        if (isCellEditable(ROW_RM_TYPE, col)) {
                            return centerRenderer;
                        } else {
                            return blackOutRenderer;
                        }
                    case ROW_RM_TYPE:
                        if (isCellEditable(row, col)) {
                            return centerRenderer;
                        } else {
                            return blackOutRenderer;
                        }
                    case ROW_HSR_TOGGLE:
                        if (isCellEditable(row, col)) {
                            return checkBoxRenderer;
                        } else {
                            return blackOutRenderer;
                        }
                    case ROW_HSR_IMPLEMENTATION_PERIODS:
                    default:
                        return centerRenderer;
                }
        }
    }

    private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            try {
                this.setSelected((boolean) value);
            } catch (Exception e) {
                this.setSelected(false);
            }
            return this;
        }
    }
    
    private class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            try {
                this.setSelectedIndex((int) value);
            } catch (Exception e) {
                this.setSelectedIndex(0);
            }
            return this;
        }
    }
    
    public class WideComboBox extends JComboBox{ 

        public WideComboBox() { 
        } 

        public WideComboBox(final Object items[]){ 
            super(items); 
        } 

        public WideComboBox(Vector items) { 
            super(items); 
        } 

            public WideComboBox(ComboBoxModel aModel) { 
            super(aModel); 
        } 

        private boolean layingOut = false; 

        public void doLayout(){ 
            try{ 
                layingOut = true; 
                    super.doLayout(); 
            }finally{ 
                layingOut = false; 
            } 
        } 

        public Dimension getSize(){ 
            Dimension dim = super.getSize(); 
            if(!layingOut) 
                dim.width = Math.max(dim.width, getPreferredSize().width); 
            return dim; 
        } 
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Cell Editors">
    public TableCellEditor getEditor(int row,int col) {
        switch (this.tableType) {
            default:
            case FREEVAL_DSS_TableModel.TYPE_ROW_NAMES:
                return defaultCellEditor;
            case FREEVAL_DSS_TableModel.TYPE_ATM_INPUT:
                if (row == ROW_RM_TYPE) {
                    return rmComboBoxEditor;
                } else if (row == ROW_HSR_TOGGLE) {
                    return checkBoxEditor;
                } else {
                    return defaultCellEditor;
                }
        }
    }
    //</editor-fold>

    public int getTableType() {
        return this.tableType;
    }

    public void setPeriod(int newPeriod) {
        this.currPeriod = newPeriod;
    }

    public void setMainWindow(MainWindowUser mainWindow) {
        this.mainWindow = mainWindow;
        this.seed = this.mainWindow.getActiveSeed();
        this.periodATM = mainWindow.getATMUpdater().getAllPeriodATM();
        fireTableStructureChanged();
    }
}
