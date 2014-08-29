package GUI.major.tableHelper;

import GUI.major.MainWindow;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Modified JTable
 *
 * @author Shu Liu
 */
public class FREEVAL_JTable extends JTable {

    private FREEVAL_TableModel tableModel;

    /**
     * Constructor
     *
     * @param tableModel table model used in this table
     */
    public FREEVAL_JTable(FREEVAL_TableModel tableModel) {
        super();
        this.tableModel = tableModel;
        this.setModel(tableModel);
        this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        this.setFont(MainWindow.getTableFont());
        setRowHeight(19);
        resetModel();
    }
    
    private void resetModel() {
        getTableHeader().setReorderingAllowed(false);
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

    /**
     * Highlight a row
     *
     * @param row row to be highlighted
     */
    public void setHighlightRow(int row) {
        try {
            setRowSelectionInterval(row, row);
        } catch (Exception e) {
            //skip
        }
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        return tableModel.getCellEditor(row, column);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return tableModel.getCellRenderer(row, column);
    }

    // <editor-fold defaultstate="collapsed" desc="setter and getters">
    /**
     * Getter for table model
     * 
     * @return table model
     */
    public FREEVAL_TableModel getTableModel() {
        return tableModel;
    }

    /**
     * Setter for table model
     * 
     * @param tableModel table model
     */
    public void setTableModel(FREEVAL_TableModel tableModel) {
        this.tableModel = tableModel;
    }
    // </editor-fold>
}
