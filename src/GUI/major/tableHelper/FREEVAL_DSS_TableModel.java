/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.tableHelper;

import DSS.DataStruct.PeriodATM;
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
    private String[] rowNames = {"Capacity Adj. Factor",
        "Speed Adj. Factor",
        "Ramp Metering Used",
        "Ramp Metering Rate",};

    private final Seed seed;

    private PeriodATM[] periodATM;

    private int currPeriod;

    private final CheckBoxRenderer checkBoxRenderer;
    private final DefaultTableCellRenderer centerRenderer;
    private final DefaultTableCellRenderer blackOutRenderer;

    public FREEVAL_DSS_TableModel(Seed seed, PeriodATM[] periodATM, JTable parentTable) {
        this.seed = seed;
        this.periodATM = periodATM;
        currPeriod = 0;

        checkBoxRenderer = new CheckBoxRenderer();
        checkBoxRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        blackOutRenderer = new DefaultTableCellRenderer();
        blackOutRenderer.setForeground(Color.DARK_GRAY);
        blackOutRenderer.setBackground(Color.DARK_GRAY);
    }

    @Override
    public int getColumnCount() {
        return seed.getValueInt(CEConst.IDS_NUM_SEGMENT) + 1;
    }

    @Override
    public int getRowCount() {
        return rowNames.length;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "Segment";
        } else {
            return "Seg. " + col;
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return rowNames[row];
        } else {
            switch (row) {
                case 0:
                    return periodATM[currPeriod].getCAF(col - 1);
                case 1:
                    return periodATM[currPeriod].getSAF(col - 1);
                case 2:
                    return periodATM[currPeriod].getRMUsed(col - 1);
                case 3:
                    return periodATM[currPeriod].getRMRate(col - 1);
                default:
                    throw new RuntimeException("Invalid Row Index");
            }
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        try {
            switch (row) {
                case 0:
                    periodATM[currPeriod].setCAF(Float.parseFloat((String) value), col - 1);
                    break;
                case 1:
                    periodATM[currPeriod].setSAF(Float.parseFloat((String) value), col - 1);
                    break;
                case 2:
                    periodATM[currPeriod].setRMUsed((boolean) value, col - 1);
                    fireTableDataChanged();
                    break;
                case 3:
                    periodATM[currPeriod].setRMRate(Integer.parseInt((String) value), col - 1);
                    break;
                default:
                    throw new RuntimeException("Invalid Row Index");
            }
        } catch (NumberFormatException e) {
            MainWindowUser.printLog("Invalid Value Entered");
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return false;
        } else {
            if (row == 2) {
                return (seed.getValueInt(CEConst.IDS_SEGMENT_TYPE, col - 1) == CEConst.SEG_TYPE_ONR);
            } else if (row == 3) {
                return (periodATM[currPeriod].getRMUsed(col - 1));
            } else {
                return true;
            }
        }
    }

    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
        setupTable();
    }

    public void setupTable() {

    }

    // <editor-fold defaultstate="collapsed" desc="Renderers">
    public TableCellRenderer getRenderer(int row, int col) {
        if (row == 2 && col != 0) {
            if (isCellEditable(row, col)) {
                return checkBoxRenderer;
            } else {
                return blackOutRenderer;
            }
        } else if (row == 3 && col != 0 && !isCellEditable(row, col)) {
            return blackOutRenderer;
        } else {
            return centerRenderer;
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
}
