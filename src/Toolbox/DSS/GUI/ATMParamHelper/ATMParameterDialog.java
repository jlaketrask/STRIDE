/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Toolbox.DSS.GUI.ATMParamHelper;

import Toolbox.DSS.Core.DataStruct.ATMParameterSet;
import Toolbox.DSS.GUI.major.MainWindow;
import coreEngine.Helper.CEConst;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author jltrask
 */
public class ATMParameterDialog extends javax.swing.JDialog {

    private int laneMaxCapacity;

    private ATMParameterSet atmParams;

    private boolean returnStatus = false;

    private MainWindow mainWindow;

    /**
     * Creates new form ATMParameterDialog
     *
     * @param parent
     * @param modal
     */
    public ATMParameterDialog(MainWindow parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.mainWindow = parent;
        atmParams = mainWindow.getUserLevelParameters().atm;

        //<editor-fold defaultstate="collapsed" desc="Spinner Listeners">
        hsr1LPct.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr1LPctStateChanged(evt);
            }
        });

        hsr1LVPH.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr1LVPHStateChanged(evt);
            }
        });
        hsr2LPct.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr2LPctStateChanged(evt);
            }
        });

        hsr2LVPH.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr2LVPHStateChanged(evt);
            }
        });
        hsr3LPct.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr3LPctStateChanged(evt);
            }
        });

        hsr3LVPH.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr3LVPHStateChanged(evt);
            }
        });
        hsr4LPct.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr4LPctStateChanged(evt);
            }
        });

        hsr4LVPH.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr4LVPHStateChanged(evt);
            }
        });
        hsr5LPct.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr5LPctStateChanged(evt);
            }
        });

        hsr5LVPH.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hsr5LVPHStateChanged(evt);
            }
        });
//</editor-fold>

        laneMaxCapacity = 2400;

        hsr1LPct.setModel(new SpinnerNumberModel(0, 0, laneMaxCapacity, 1));
        hsr2LPct.setModel(new SpinnerNumberModel(0, 0, laneMaxCapacity, 1));
        hsr3LPct.setModel(new SpinnerNumberModel(0, 0, laneMaxCapacity, 1));
        hsr4LPct.setModel(new SpinnerNumberModel(0, 0, laneMaxCapacity, 1));
        hsr5LPct.setModel(new SpinnerNumberModel(0, 0, laneMaxCapacity, 1));

        hsr1LPct.setModel(new SpinnerNumberModel(50, 0, 100, 1));
        hsr2LPct.setModel(new SpinnerNumberModel(50, 0, 100, 1));
        hsr3LPct.setModel(new SpinnerNumberModel(50, 0, 100, 1));
        hsr4LPct.setModel(new SpinnerNumberModel(50, 0, 100, 1));
        hsr5LPct.setModel(new SpinnerNumberModel(50, 0, 100, 1));

        shoulderCB.setModel(createIncidentReductionModel(6));
        oneLaneCB.setModel(createIncidentReductionModel(6));
        twoLaneCB.setModel(createIncidentReductionModel(6));
        threeLaneCB.setModel(createIncidentReductionModel(6));
        fourLaneCB.setModel(createIncidentReductionModel(6));

        updateHSRSpinnersVPH();
        updateComboBoxModels();
        updateGP2MLDiversion();
    }

    public void setATMParameters(ATMParameterSet atmParams) {
        setHardShoulderCAFs(atmParams.hsrCapacity);
        this.atmParams = atmParams;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    private void setHardShoulderCAFs(float[] hsrCAFs) {
        hsr1LPct.setValue(Math.round(hsrCAFs[0] * 100.0f));
        hsr2LPct.setValue(Math.round(hsrCAFs[1] * 100.0f));
        hsr3LPct.setValue(Math.round(hsrCAFs[2] * 100.0f));
        hsr4LPct.setValue(Math.round(hsrCAFs[3] * 100.0f));
        hsr5LPct.setValue(Math.round(hsrCAFs[4] * 100.0f));
    }

    public float[] getHardShoulderCAFs() {
        float[] hsrCAFs = new float[5];
        hsrCAFs[0] = ((int) hsr1LPct.getValue()) / 100.0f;
        hsrCAFs[1] = ((int) hsr2LPct.getValue()) / 100.0f;
        hsrCAFs[2] = ((int) hsr3LPct.getValue()) / 100.0f;
        hsrCAFs[3] = ((int) hsr4LPct.getValue()) / 100.0f;
        hsrCAFs[4] = ((int) hsr5LPct.getValue()) / 100.0f;

        return hsrCAFs;
    }

    public int[] getIncidentReductions() {
        int[] tempArr = new int[5];
        tempArr[0] = shoulderCB.getSelectedIndex();
        tempArr[1] = oneLaneCB.getSelectedIndex();
        tempArr[2] = twoLaneCB.getSelectedIndex();
        tempArr[3] = threeLaneCB.getSelectedIndex();
        tempArr[4] = fourLaneCB.getSelectedIndex();
        return tempArr;
    }

    public boolean getReturnStatus() {
        return returnStatus;
    }

    private void doClose() {
        if (returnStatus == true) {
            this.atmParams.hsrCapacity = this.getHardShoulderCAFs();
            this.atmParams.incidentDurationReduction = this.getIncidentReductions();
            this.atmParams.GP2MLDiversionEnabled = gp2MLDiversionCB.isSelected();
            if (this.atmParams.GP2MLDiversionEnabled) {
                this.atmParams.GP2MLDiversion = (int) gp2MLDiversionSpinner.getValue();
            }
        } else {

        }
        this.setVisible(false);
    }

    private void updateHSRSpinnersVPH() {
        updateHSRSpinnersVPH(1);
        updateHSRSpinnersVPH(2);
        updateHSRSpinnersVPH(3);
        updateHSRSpinnersVPH(4);
        updateHSRSpinnersVPH(5);
    }

    private void updateHSRSpinnersVPH(int numLanes) {
        int val;
        switch (numLanes) {
            case 1:
                val = Math.round(((int) hsr1LPct.getValue()) / 100.0f * laneMaxCapacity);
                hsr1LVPH.setValue(val);
                break;
            case 2:
                val = Math.round(((int) hsr2LPct.getValue()) / 100.0f * laneMaxCapacity);
                hsr2LVPH.setValue(val);
                break;
            case 3:
                val = Math.round(((int) hsr3LPct.getValue()) / 100.0f * laneMaxCapacity);
                hsr3LVPH.setValue(val);
                break;
            case 4:
                val = Math.round(((int) hsr4LPct.getValue()) / 100.0f * laneMaxCapacity);
                hsr4LVPH.setValue(val);
                break;
            case 5:
                val = Math.round(((int) hsr5LPct.getValue()) / 100.0f * laneMaxCapacity);
                hsr5LVPH.setValue(val);
                break;
        }
    }

    private void updateHSRSpinnersPct() {
        updateHSRSpinnersPct(1);
        updateHSRSpinnersPct(2);
        updateHSRSpinnersPct(3);
        updateHSRSpinnersPct(4);
        updateHSRSpinnersPct(5);
    }

    private void updateHSRSpinnersPct(int numLanes) {
        int val;
        switch (numLanes) {
            case 1:
                val = Math.round(100.0f * ((int) hsr1LVPH.getValue()) / laneMaxCapacity);
                hsr1LPct.setValue(val);
                break;
            case 2:
                val = Math.round(100.0f * ((int) hsr2LVPH.getValue()) / laneMaxCapacity);
                hsr2LPct.setValue(val);
                break;
            case 3:
                val = Math.round(100.0f * ((int) hsr3LVPH.getValue()) / laneMaxCapacity);
                hsr3LPct.setValue(val);
                break;
            case 4:
                val = Math.round(100.0f * ((int) hsr4LVPH.getValue()) / laneMaxCapacity);
                hsr4LPct.setValue(val);
                break;
            case 5:
                val = Math.round(100.0f * ((int) hsr5LVPH.getValue()) / laneMaxCapacity);
                hsr5LPct.setValue(val);
                break;
        }
    }

    private void hsr1LPctStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersVPH(1);
    }

    private void hsr1LVPHStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersPct(1);
    }

    private void hsr2LPctStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersVPH(2);
    }

    private void hsr2LVPHStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersPct(2);
    }

    private void hsr3LPctStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersVPH(3);
    }

    private void hsr3LVPHStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersPct(3);
    }

    private void hsr4LPctStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersVPH(4);
    }

    private void hsr4LVPHStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersPct(1);
    }

    private void hsr5LPctStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersVPH(5);
    }

    private void hsr5LVPHStateChanged(javax.swing.event.ChangeEvent evt) {
        updateHSRSpinnersPct(5);
    }

    private DefaultComboBoxModel createIncidentReductionModel(int maxNumPeriodsReduced) {
        String[] itemArr = new String[maxNumPeriodsReduced + 1];
        for (int i = 0; i <= maxNumPeriodsReduced; i++) {
            itemArr[i] = i * 15 + " min";
        }
        return new DefaultComboBoxModel(itemArr);
    }

    private void updateComboBoxModels() {
        shoulderCB.setSelectedIndex(atmParams.incidentDurationReduction[0]);
        oneLaneCB.setSelectedIndex(atmParams.incidentDurationReduction[1]);
        twoLaneCB.setSelectedIndex(atmParams.incidentDurationReduction[2]);
        threeLaneCB.setSelectedIndex(atmParams.incidentDurationReduction[3]);
        fourLaneCB.setSelectedIndex(atmParams.incidentDurationReduction[4]);
    }

    private void updateGP2MLDiversion() {
        if (!mainWindow.getActiveSeed().isManagedLaneUsed()) {
            atmParams.GP2MLDiversionEnabled = false;
            gp2MLDiversionCB.setEnabled(false);
            gp2MLDiversionSpinner.setEnabled(false);
        }
        SpinnerNumberModel mlDiversionSpinnerModel = (SpinnerNumberModel) gp2MLDiversionSpinner.getModel();
        mlDiversionSpinnerModel.setMinimum(0);
        mlDiversionSpinnerModel.setMaximum(mainWindow.getActiveSeed().getValueInt(CEConst.IDS_MAIN_DEMAND_VEH));
        mlDiversionSpinnerModel.setValue(atmParams.GP2MLDiversion);
        gp2MLDiversionCB.setSelected(atmParams.GP2MLDiversionEnabled);
        updateGP2MLDiversionPCTLabel();
        gp2MLDiversionCBActionPerformed(null);
        if (!mainWindow.getActiveSeed().isManagedLaneUsed()) {
            atmParams.GP2MLDiversionEnabled = false;
        }
    }

    private void updateGP2MLDiversionPCTLabel() {
        float pct = ((int) gp2MLDiversionSpinner.getValue() / ((float) mainWindow.getActiveSeed().getValueInt(CEConst.IDS_MAIN_DEMAND_VEH))) * 100.0f;
        gp2MLDiversionPCTLabel.setText("(" + String.format("%.1f", pct) + "%)");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hsrPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        hsr1LPct = new javax.swing.JSpinner();
        hsr2LPct = new javax.swing.JSpinner();
        hsr3LPct = new javax.swing.JSpinner();
        hsr4LPct = new javax.swing.JSpinner();
        hsr5LPct = new javax.swing.JSpinner();
        hsr1LVPH = new javax.swing.JSpinner();
        hsr2LVPH = new javax.swing.JSpinner();
        hsr3LVPH = new javax.swing.JSpinner();
        hsr4LVPH = new javax.swing.JSpinner();
        hsr5LVPH = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        shoulderCB = new javax.swing.JComboBox();
        oneLaneCB = new javax.swing.JComboBox();
        twoLaneCB = new javax.swing.JComboBox();
        threeLaneCB = new javax.swing.JComboBox();
        fourLaneCB = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        gp2MLDiversionCB = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        gp2MLDiversionLabel = new javax.swing.JLabel();
        gp2MLDiversionSpinner = new javax.swing.JSpinner();
        gp2MLDiversionPCTLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("User Level ATM Parameter Defaults");

        hsrPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hard Shoulder Running", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jPanel1.setLayout(new java.awt.GridLayout(3, 7, 2, 0));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("One Lane");
        jPanel1.add(jLabel7);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Two Lane");
        jPanel1.add(jLabel8);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("3 Lane");
        jPanel1.add(jLabel9);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("4 Lane");
        jPanel1.add(jLabel10);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("5+ Lane");
        jPanel1.add(jLabel11);
        jPanel1.add(hsr1LPct);
        jPanel1.add(hsr2LPct);
        jPanel1.add(hsr3LPct);
        jPanel1.add(hsr4LPct);
        jPanel1.add(hsr5LPct);
        jPanel1.add(hsr1LVPH);
        jPanel1.add(hsr2LVPH);
        jPanel1.add(hsr3LVPH);
        jPanel1.add(hsr4LVPH);
        jPanel1.add(hsr5LVPH);

        jPanel2.setLayout(new java.awt.GridLayout(3, 1));

        jLabel1.setText("Shoulder Capacity:");
        jPanel2.add(jLabel1);

        jLabel2.setText("% of 1 Mainline Lane:");
        jPanel2.add(jLabel2);

        jLabel4.setText("Veh. Per Hour:");
        jPanel2.add(jLabel4);

        javax.swing.GroupLayout hsrPanelLayout = new javax.swing.GroupLayout(hsrPanel);
        hsrPanel.setLayout(hsrPanelLayout);
        hsrPanelLayout.setHorizontalGroup(
            hsrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hsrPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        hsrPanelLayout.setVerticalGroup(
            hsrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hsrPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hsrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText("OK");
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

        jButton1.setText("Configure Variable Message Sign Locations for Traffic Diversion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Incident Management", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jLabel3.setText("Incident Severity");
        jPanel4.add(jLabel3);

        jLabel5.setText("Duration Reduction");
        jPanel4.add(jLabel5);

        jPanel5.setLayout(new java.awt.GridLayout(2, 5));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Shoulder");
        jPanel5.add(jLabel6);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("One Lane");
        jPanel5.add(jLabel13);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Two Lane");
        jPanel5.add(jLabel15);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("3 Lane");
        jPanel5.add(jLabel12);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("4+ Lane");
        jPanel5.add(jLabel14);

        shoulderCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(shoulderCB);

        oneLaneCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(oneLaneCB);

        twoLaneCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(twoLaneCB);

        threeLaneCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(threeLaneCB);

        fourLaneCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(fourLaneCB);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        gp2MLDiversionCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gp2MLDiversionCBActionPerformed(evt);
            }
        });

        jLabel16.setText("GP to ML Traffic Diversion: ");

        gp2MLDiversionLabel.setText("Amount of Traffic Diverted:");

        gp2MLDiversionSpinner.setModel(new javax.swing.SpinnerNumberModel(200, 0, 2400, 1));
        gp2MLDiversionSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                gp2MLDiversionSpinnerStateChanged(evt);
            }
        });

        gp2MLDiversionPCTLabel.setText("% Label");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gp2MLDiversionCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gp2MLDiversionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gp2MLDiversionSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gp2MLDiversionPCTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(gp2MLDiversionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(gp2MLDiversionSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(gp2MLDiversionPCTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(gp2MLDiversionCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hsrPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hsrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.returnStatus = true;
        doClose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.returnStatus = false;
        doClose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ATMDiversionDialog divDialog = new ATMDiversionDialog(mainWindow, true);
        //divDialog.setSeed(mainWindow.getActiveSeed());
        //divDialog.setUserParams(mainWindow.getUserLevelParameters());
        divDialog.setLocationRelativeTo(null);
        divDialog.setVisible(true);
        divDialog.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void gp2MLDiversionSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_gp2MLDiversionSpinnerStateChanged
        updateGP2MLDiversionPCTLabel();
    }//GEN-LAST:event_gp2MLDiversionSpinnerStateChanged

    private void gp2MLDiversionCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gp2MLDiversionCBActionPerformed
        if (gp2MLDiversionCB.isSelected()) {
            gp2MLDiversionSpinner.setEnabled(true);
            gp2MLDiversionLabel.setForeground(Color.BLACK);
            gp2MLDiversionPCTLabel.setForeground(Color.BLACK);
        } else {
            gp2MLDiversionSpinner.setEnabled(false);
            gp2MLDiversionLabel.setForeground(Color.GRAY);
            gp2MLDiversionPCTLabel.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_gp2MLDiversionCBActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox fourLaneCB;
    private javax.swing.JCheckBox gp2MLDiversionCB;
    private javax.swing.JLabel gp2MLDiversionLabel;
    private javax.swing.JLabel gp2MLDiversionPCTLabel;
    private javax.swing.JSpinner gp2MLDiversionSpinner;
    private javax.swing.JSpinner hsr1LPct;
    private javax.swing.JSpinner hsr1LVPH;
    private javax.swing.JSpinner hsr2LPct;
    private javax.swing.JSpinner hsr2LVPH;
    private javax.swing.JSpinner hsr3LPct;
    private javax.swing.JSpinner hsr3LVPH;
    private javax.swing.JSpinner hsr4LPct;
    private javax.swing.JSpinner hsr4LVPH;
    private javax.swing.JSpinner hsr5LPct;
    private javax.swing.JSpinner hsr5LVPH;
    private javax.swing.JPanel hsrPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox oneLaneCB;
    private javax.swing.JComboBox shoulderCB;
    private javax.swing.JComboBox threeLaneCB;
    private javax.swing.JComboBox twoLaneCB;
    // End of variables declaration//GEN-END:variables
}
