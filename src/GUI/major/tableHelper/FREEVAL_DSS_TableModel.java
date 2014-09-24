/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.tableHelper;

import DSS.DataStruct.PeriodATM;
import GUI.major.MainWindow;
import GUI.major.MainWindowUser;
import coreEngine.CEConst;
import coreEngine.Seed;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author jltrask
 */
public class FREEVAL_DSS_TableModel extends AbstractTableModel {

    //private String[] columnNames;
    private final String[] rowNames = {"Ramp Metering Used",
                                       "Ramp Metering Rate",
                                       "Hard Shoulder Running Used",
                                       "Hard Shoulder Capacity"};

    private Seed seed;

    private PeriodATM[] periodATM;

    private int currPeriod;

    private final CheckBoxRenderer checkBoxRenderer;
    private final DefaultTableCellRenderer centerRenderer;
    private final DefaultTableCellRenderer blackOutRenderer;
    private final DefaultTableCellRenderer rightRenderer;
    private final JTable parentTable;
    
    private final int tableType;
    
    public static final int TYPE_ROW_NAMES = 0;
    public static final int TYPE_ATM_INPUT = 1;

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
                return "Seg. " + (col+1);
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
                    case 0:
                        return periodATM[currPeriod].getRMUsed(col);
                    case 1:
                        return periodATM[currPeriod].getRMRate(col);
                    case 2:
                        return periodATM[currPeriod].getHSRUsed(col);
                    case 3:
                        return periodATM[currPeriod].getHSRCapacity(col);
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
                        case 0:
                            periodATM[currPeriod].setRMUsed((boolean) value, col);
                            fireTableDataChanged();
                            break;
                        case 1:
                            periodATM[currPeriod].setRMRate(Integer.parseInt((String) value), col);
                            break;
                        case 2:
                            periodATM[currPeriod].setHSRUsed((boolean) value, col);
                            fireTableDataChanged();
                            break;
                        case 3:
                            periodATM[currPeriod].setHSRCapacity(Integer.parseInt((String) value), col);
                            break;
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
        switch (tableType){
            default:
            case TYPE_ROW_NAMES:
                return false;
            case TYPE_ATM_INPUT:
                switch(row) {
                    case 0:
                        return (seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_ONR);
                    case 1:
                        return (periodATM[currPeriod].getRMUsed(col));
                    case 2:
                        return true;
                    case 3:
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

    // <editor-fold defaultstate="collapsed" desc="Renderers">
    public TableCellRenderer getRenderer(int row, int col) {
        switch (tableType) {
            default:
            case TYPE_ROW_NAMES:
                return rightRenderer;
            case TYPE_ATM_INPUT:
                switch (row) {
                    case 0:
                    case 2:
                        if (isCellEditable(row, col)) {
                            return checkBoxRenderer;
                        } else {
                            return blackOutRenderer;
                        }
                    case 1:
                    case 3:
                        if (isCellEditable(row-1, col)) {
                            return centerRenderer;
                        } else {
                            return blackOutRenderer;
                        }
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
    // </editor-fold>
    
    public int getTableType() {
        return this.tableType;
    }
    
    public void setPeriod(int newPeriod) {
        this.currPeriod = newPeriod;
    }
    
    public void setSeed(Seed seed) {
        this.seed = seed;
        //TODO Allow Period ATM to be pulled from the seed.
        if (true) {
            periodATM = new PeriodATM[seed.getValueInt(CEConst.IDS_NUM_PERIOD)];
            for (int per = 0; per < periodATM.length; per++) {
                periodATM[per] = new PeriodATM(seed, per);
            }
        }
        fireTableStructureChanged();
    }
}
