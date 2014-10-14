/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.eventHelper;

import DSS.DataStruct.ScenarioEvent;
import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author jltrask
 */
public class WorkZoneDialog extends javax.swing.JDialog {

    private Seed seed;

    private boolean status;

    /**
     * Creates new form weatherEventDialog
     * 
     * @param parent
     * @param modal 
     */
    public WorkZoneDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
        resetPanel();
    }

    private void resetPanel() {
        startPeriodCB.setModel(periodCBModelCreator(0));
        endPeriodCB.setModel(periodCBModelCreator(1));

        startSegmentCB.setModel(segmentCBModelCreator());
        endSegmentCB.setModel(segmentCBModelCreator());
    }

    public ScenarioEvent getWorkZoneEvent() {
        ScenarioEvent wzEvent = new ScenarioEvent(ScenarioEvent.WORK_ZONE_EVENT);
        wzEvent.startSegment = startSegmentCB.getSelectedIndex();
        wzEvent.endSegment = Integer.parseInt((String) endSegmentCB.getSelectedItem()) - 1;
        wzEvent.startPeriod = startPeriodCB.getSelectedIndex();
        wzEvent.endPeriod = Integer.parseInt(((String) endPeriodCB.getSelectedItem()).split(" ")[0]) - 1;
        wzEvent.caf = Float.parseFloat(cafTextField.getText());
        wzEvent.daf = Float.parseFloat(dafTextField.getText());
        wzEvent.saf = Float.parseFloat(safTextField.getText());
        switch (severityCB.getSelectedIndex()) {
            case 0:
            case 1:
                wzEvent.laf = 0;
                break;
            default:
                wzEvent.laf = -1 * (severityCB.getSelectedIndex() - 1);
                break;
        }
        //System.out.println(incEvent.laf);
        return wzEvent;

    }

    /**
     * Creates the model for the period selection combo box. The type designates
     * whether it is for start period (0) selection or end period selection (1).
     * End period selection indicates that the periods go through the end
     * period, and the clock time displayed is the time at the end of the period
     * (as opposed to clock time at the beginning of the period for start
     * times).
     *
     * @param type
     * @return
     */
    private DefaultComboBoxModel periodCBModelCreator(int type) {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_PERIOD)];
        //tempArr[0] = "<Select Period>";
        int currHour = seed.getStartTime().hour;
        int currMin = seed.getStartTime().minute;
        if (type == 1) {
            currMin += 15;
        }
        for (int perIdx = 1; perIdx <= tempArr.length - 1; perIdx++) {
            if (currMin == 60) {
                currMin = 0;
                currHour++;
            }
            if (currMin == 0) {
                tempArr[perIdx - 1] = String.valueOf(perIdx) + "  (" + currHour + ":00)";
            } else {
                tempArr[perIdx - 1] = String.valueOf(perIdx) + "  (" + currHour + ":" + currMin + ")";
            }
            currMin += 15;
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(tempArr);
        return model;
    }

    /**
     * Creates the model for the segment selection combo box.
     *
     * @return
     */
    private DefaultComboBoxModel segmentCBModelCreator() {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_SEGMENT)];

        for (int seg = 1; seg <= tempArr.length; seg++) {
            tempArr[seg - 1] = String.valueOf(seg);
        }

        return new DefaultComboBoxModel(tempArr);
    }

    /**
     * Creates the model for the segment selection combo box.
     *
     * @return
     */
    private DefaultComboBoxModel segmentCBModelCreator(int startSegment) {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_SEGMENT) - startSegment];

        int currSeg = 0;
        for (int seg = (startSegment + 1); seg <= tempArr.length; seg++) {
            tempArr[currSeg] = String.valueOf(seg);
            currSeg++;
        }

        return new DefaultComboBoxModel(tempArr);
    }

    /**
     * Creates the model for the period selection combo box. The type designates
     * whether it is for start period (0) selection or end period selection (1).
     * End period selection indicates that the periods go through the end
     * period, and the clock time displayed is the time at the end of the period
     * (as opposed to clock time at the beginning of the period for start
     * times).
     *
     * @param type
     * @param startPeriod
     * @return
     */
    private DefaultComboBoxModel periodCBModelCreator(int type, int startPeriod) {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_PERIOD) - (startPeriod - 1)];
        //tempArr[0] = "<Select Period>";
        int currHour = seed.getStartTime().hour;
        int currMin = seed.getStartTime().minute;
        if (type == 1) {
            currMin += 15;
        }
        int currIdx = 0;
        for (int perIdx = 1; perIdx <= tempArr.length - 1; perIdx++) {
            if (currMin == 60) {
                currMin = 0;
                currHour++;
            }
            if (perIdx >= startPeriod) {
                if (currMin == 0) {
                    tempArr[currIdx] = String.valueOf(perIdx) + "  (" + currHour + ":00)";
                } else {
                    tempArr[currIdx] = String.valueOf(perIdx) + "  (" + currHour + ":" + currMin + ")";
                }
                currIdx++;
            }
            currMin += 15;

        }

        return new DefaultComboBoxModel(tempArr);
    }

    public boolean getReturnStatus() {
        return status;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cafTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        safTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        dafTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        severityCB = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        startPeriodCB = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        endPeriodCB = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        startSegmentCB = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        endSegmentCB = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create Work Zone");

        jPanel1.setLayout(new java.awt.GridLayout(3, 2));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Enter CAF:");
        jPanel1.add(jLabel3);

        cafTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cafTextField.setText("1.0");
        jPanel1.add(cafTextField);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Enter SAF:");
        jPanel1.add(jLabel5);

        safTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        safTextField.setText("1.0");
        jPanel1.add(safTextField);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Enter DAF:");
        jPanel1.add(jLabel4);

        dafTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dafTextField.setText("1.0");
        jPanel1.add(dafTextField);

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridLayout(5, 2));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Work Zone Type:");
        jPanel2.add(jLabel1);

        severityCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Select Type>", "Shoulder Closure", "1 Lane Closure", "2 Lane Closure", "3 Lane Closure", "4 Lane Closure" }));
        jPanel2.add(severityCB);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Starting in Period:");
        jPanel2.add(jLabel6);

        startPeriodCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Select Period>", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", " ", " " }));
        startPeriodCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startPeriodCBItemStateChanged(evt);
            }
        });
        jPanel2.add(startPeriodCB);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Ending in Period:");
        jPanel2.add(jLabel2);

        endPeriodCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Select Period>" }));
        jPanel2.add(endPeriodCB);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Starting in Segment");
        jPanel2.add(jLabel7);

        startSegmentCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Select Segment>" }));
        startSegmentCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startSegmentCBItemStateChanged(evt);
            }
        });
        jPanel2.add(startSegmentCB);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Ending in Segment:");
        jPanel2.add(jLabel8);

        endSegmentCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Select Segment>" }));
        jPanel2.add(endSegmentCB);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        status = false;
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (severityCB.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a work zone type.", "Error: No Incident Severity Selected", JOptionPane.ERROR_MESSAGE);
        } else {
            status = true;
            this.setVisible(false);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void startPeriodCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startPeriodCBItemStateChanged
        int endCBidx = Integer.parseInt(((String) endPeriodCB.getSelectedItem()).split(" ")[0]);
        int startCBidx = startPeriodCB.getSelectedIndex();
        endPeriodCB.setModel(periodCBModelCreator(1, startPeriodCB.getSelectedIndex() + 1));
        if (endCBidx >= startCBidx + 1) {
            endPeriodCB.setSelectedIndex(endCBidx - (startCBidx + 1));
        } else {
            endPeriodCB.setSelectedIndex(0);
        }

    }//GEN-LAST:event_startPeriodCBItemStateChanged

    private void startSegmentCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startSegmentCBItemStateChanged
        int endCBidx = Integer.parseInt(((String) endSegmentCB.getSelectedItem()));
        int startCBidx = startSegmentCB.getSelectedIndex();
        endSegmentCB.setModel(segmentCBModelCreator(startCBidx));
        if (endCBidx >= (startCBidx + 1)) {
            //System.out.println((endCBidx - (startCBidx + 1)));
            endSegmentCB.setSelectedIndex(endCBidx - (startCBidx + 1));
        } else {
            endSegmentCB.setSelectedIndex(0);
        }
    }//GEN-LAST:event_startSegmentCBItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cafTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dafTextField;
    private javax.swing.JComboBox endPeriodCB;
    private javax.swing.JComboBox endSegmentCB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField safTextField;
    private javax.swing.JComboBox severityCB;
    private javax.swing.JComboBox startPeriodCB;
    private javax.swing.JComboBox startSegmentCB;
    // End of variables declaration//GEN-END:variables
}
