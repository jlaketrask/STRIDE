/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.tableHelper;

import java.awt.Color;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author jltrask
 */
public class FREEVAL_DSS_JTable extends JTable {

    private final TableSelectionCellEditor defaultCellEditor;
    private final DefaultCellEditor checkBoxEditor;

    public FREEVAL_DSS_JTable() {
        super();
        defaultCellEditor = new TableSelectionCellEditor(true);
        JCheckBox editorCB = new JCheckBox();
        editorCB.setHorizontalAlignment(JLabel.CENTER);
        editorCB.setBackground(Color.WHITE);
        editorCB.setForeground(Color.WHITE);
        checkBoxEditor = new DefaultCellEditor(editorCB);

        this.rowSelectionAllowed = false;
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }
    
    /**
     * Highlight a column
     *
     * @param col column to be highlighted
     */
    public void setHighlightCol(int col) {
        try {
            setColumnSelectionInterval(col, col);
        } catch (Exception e) {
            //skip
        }
    }

    @Override
    public TableCellEditor getCellEditor(int row, int col) {        
        switch (((FREEVAL_DSS_TableModel) this.getModel()).getTableType()) {
            default:
            case FREEVAL_DSS_TableModel.TYPE_ROW_NAMES:
                return defaultCellEditor;
            case FREEVAL_DSS_TableModel.TYPE_ATM_INPUT:
                if (row == 1 || row == 4) {
                    return checkBoxEditor;
                } else {
                    return defaultCellEditor;
                }
        }
        //return tableModel.getCellEditor(row, column);
        //return defaultCellEditor;
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int col) {
        //if (row == 2 && col > 0) {
        //    return checkBoxRenderer;
        //} else {
        //    return centerRenderer;
        //}
        return ((FREEVAL_DSS_TableModel) this.getModel()).getRenderer(row, col);
        //return tableModel.getCellRenderer(row, column);
    }
}
