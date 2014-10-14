package GUI.seedEditAndIOHelper;

import GUI.major.MainWindow;
import coreEngine.Helper.CEConst;
import coreEngine.Helper.CETime;
import coreEngine.Seed;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * This is a dialog for seed global input Called either when create a new seed
 * or when configure an existing seed
 *
 * @author Shu Liu
 */
public class SeedGlobalDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    private static final String[] terrainNames
            = new String[]{CEConst.STR_TERRAIN_LEVEL, CEConst.STR_TERRAIN_MOUNTAINOUS,
                CEConst.STR_TERRAIN_ROLLING, CEConst.STR_TERRAIN_VARYING_OR_OTHER};

    private static final String[] separationNames
            = new String[]{CEConst.STR_ML_SEPARATION_MARKING, CEConst.STR_ML_SEPARATION_BUFFER,
                CEConst.STR_ML_SEPARATION_BARRIER};

    private static final String[] methodNames
            = new String[]{CEConst.STR_ML_METHOD_HOV, CEConst.STR_ML_METHOD_HOT};

    private boolean inputCheckPassed = true;

    private final boolean isNewSeed;
    private final MainWindow mainWindow;
    private Seed seed;

    /**
     * Creates new form NewOkCancelDialog
     *
     * @param seed
     * @param mainWindow
     */
    public SeedGlobalDialog(Seed seed, MainWindow mainWindow) {
        super(mainWindow, true);
        initComponents();

        this.mainWindow = mainWindow;

        //set starting position
        this.setLocationRelativeTo(this.getRootPane());
        terrainComboBox.setModel(new DefaultComboBoxModel(terrainNames));
        separationCB.setModel(new DefaultComboBoxModel(separationNames));
        MLTypeCB.setModel(new DefaultComboBoxModel(methodNames));

        //default visbility
        laneWidthText.setVisible(false);
        lateralClearanceText.setVisible(false);
        rampMeteringText.setVisible(false);
        mlOccLabel.setVisible(false);
        mlOccText.setVisible(false);

        if (seed == null) {
            //is new seed
            this.seed = new Seed();
            isNewSeed = true;

            generalJPanel.remove(addSegButton);
            generalJPanel.remove(delSegButton);
            generalJPanel.remove(addPeriodButton);
            generalJPanel.remove(delPeriodButton);
            generalJPanel.setLayout(new java.awt.GridLayout(4, 4));
        } else {
            this.seed = seed;
            isNewSeed = false;
            loadExisting();
        }

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    private void loadExisting() {
        seedNameText.setText(seed.getValueString(CEConst.IDS_PROJECT_NAME));
        startHour.setSelectedIndex(seed.getStartTime().hour);
        startMin.setSelectedIndex(seed.getStartTime().minute / 15);
        endHour.setSelectedIndex(seed.getEndTime().hour);
        endMin.setSelectedIndex(seed.getEndTime().minute / 15);
        numOfSegmentsText.setText(seed.getValueString(CEConst.IDS_NUM_SEGMENT));
        jamDensityText.setText(seed.getValueString(CEConst.IDS_JAM_DENSITY));
        capacityDropText.setText(Integer.toString(seed.getValueInt(CEConst.IDS_CAPACITY_ALPHA)));
        gpOccText.setText(Float.toString(seed.getValueFloat(CEConst.IDS_OCCU_GP)));
        mlOccText.setText(Float.toString(seed.getValueFloat(CEConst.IDS_OCCU_ML)));
        ffsKnownCheck.setSelected(seed.isFreeFlowSpeedKnown());//getValueString(CEConst.IDS_FFS_KNOWN).equals("true"));
        useRampMeteringCheck.setSelected(seed.isRampMeteringUsed());//getValueString(CEConst.IDS_RM_USED).equals("true"));
        manageLaneCheck.setSelected(seed.isManagedLaneUsed());
        manageLaneCheck.setEnabled(seed.getValueInt(CEConst.IDS_NUM_SCEN) == 0);
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Fill Global Values"));

        terrainCheck.setSelected(false);
        ERCheck.setSelected(false);
        ERText.setText("");
        ETCheck.setSelected(false);
        ETText.setText("");
        RVCheck.setSelected(false);
        RVText.setText("");
        accDecLengthCheck.setSelected(false);
        accDecLengthText.setText("");
        laneWidthCheck.setSelected(false);
        laneWidthText.setText("");
        lateralClearanceCheck.setSelected(false);
        lateralClearanceText.setText("");
        mainlineFFSCheck.setSelected(false);
        mainlineFFSText.setText("");
        numOfMainlineLanesCheck.setSelected(false);
        numOfMainlineLanesText.setText("");
        numOfRampLanesCheck.setSelected(false);
        numOfRampLanesText.setText("");
        rampFFSCheck.setSelected(false);
        rampFFSText.setText("");
        rampMeteringCheck.setSelected(false);
        rampMeteringText.setText("");
        truckCheck.setSelected(false);
        truckText.setText("");

        separationCheck.setSelected(false);
        MLTypeCheck.setSelected(false);
        numOfMLLanesCheck.setSelected(false);
        numOfMLLanesText.setText("");
        MLFFSCheck.setSelected(false);
        MLFFSText.setText("");
        numOfMLRampLanesCheck.setSelected(false);
        numOfMLRampLanesText.setText("");
        rampMLFFSCheck.setSelected(false);
        rampMLFFSText.setText("");
        accDecLengthMLCheck.setSelected(false);
        accDecLengthMLText.setText("");
        truckMLCheck.setSelected(false);
        truckMLText.setText("");
        RVMLCheck.setSelected(false);
        RVMLText.setText("");

        startHour.setEnabled(false);
        startMin.setEnabled(false);
        endHour.setEnabled(false);
        endMin.setEnabled(false);
        numOfSegmentsText.setEnabled(false);
    }

    /**
     * Getter for return status
     *
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fillMLJPanel = new javax.swing.JPanel();
        separationCheck = new javax.swing.JCheckBox();
        separationCB = new javax.swing.JComboBox();
        accDecLengthMLCheck = new javax.swing.JCheckBox();
        accDecLengthMLText = new javax.swing.JTextField();
        numOfMLLanesCheck = new javax.swing.JCheckBox();
        numOfMLLanesText = new javax.swing.JTextField();
        numOfMLRampLanesCheck = new javax.swing.JCheckBox();
        numOfMLRampLanesText = new javax.swing.JTextField();
        MLFFSCheck = new javax.swing.JCheckBox();
        MLFFSText = new javax.swing.JTextField();
        rampMLFFSCheck = new javax.swing.JCheckBox();
        rampMLFFSText = new javax.swing.JTextField();
        truckMLCheck = new javax.swing.JCheckBox();
        truckMLText = new javax.swing.JTextField();
        RVMLCheck = new javax.swing.JCheckBox();
        RVMLText = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        checkButton = new javax.swing.JButton();
        MLTypeCheck = new javax.swing.JCheckBox();
        MLTypeCB = new javax.swing.JComboBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        generalJPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        seedNameText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        numOfSegmentsText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        startHour = new javax.swing.JComboBox();
        startMin = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        endHour = new javax.swing.JComboBox();
        endMin = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jamDensityText = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        capacityDropText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        gpOccText = new javax.swing.JTextField();
        mlOccLabel = new javax.swing.JLabel();
        mlOccText = new javax.swing.JTextField();
        addSegButton = new javax.swing.JButton();
        delSegButton = new javax.swing.JButton();
        addPeriodButton = new javax.swing.JButton();
        delPeriodButton = new javax.swing.JButton();
        optionsJPanel = new javax.swing.JPanel();
        ffsKnownCheck = new javax.swing.JCheckBox();
        useRampMeteringCheck = new javax.swing.JCheckBox();
        manageLaneCheck = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        fillGPJPanel = new javax.swing.JPanel();
        terrainCheck = new javax.swing.JCheckBox();
        terrainComboBox = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        numOfMainlineLanesCheck = new javax.swing.JCheckBox();
        numOfMainlineLanesText = new javax.swing.JTextField();
        mainlineFFSCheck = new javax.swing.JCheckBox();
        mainlineFFSText = new javax.swing.JTextField();
        laneWidthCheck = new javax.swing.JCheckBox();
        laneWidthText = new javax.swing.JTextField();
        lateralClearanceCheck = new javax.swing.JCheckBox();
        lateralClearanceText = new javax.swing.JTextField();
        numOfRampLanesCheck = new javax.swing.JCheckBox();
        numOfRampLanesText = new javax.swing.JTextField();
        rampFFSCheck = new javax.swing.JCheckBox();
        rampFFSText = new javax.swing.JTextField();
        accDecLengthCheck = new javax.swing.JCheckBox();
        accDecLengthText = new javax.swing.JTextField();
        rampMeteringCheck = new javax.swing.JCheckBox();
        rampMeteringText = new javax.swing.JTextField();
        truckCheck = new javax.swing.JCheckBox();
        truckText = new javax.swing.JTextField();
        RVCheck = new javax.swing.JCheckBox();
        RVText = new javax.swing.JTextField();
        ETCheck = new javax.swing.JCheckBox();
        ETText = new javax.swing.JTextField();
        ERCheck = new javax.swing.JCheckBox();
        ERText = new javax.swing.JTextField();

        fillMLJPanel.setLayout(new java.awt.GridLayout(7, 4));

        separationCheck.setSelected(true);
        separationCheck.setText("Separation Type");
        separationCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                separationCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(separationCheck);

        separationCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Marking", "Buffer", "Barrier" }));
        fillMLJPanel.add(separationCB);

        accDecLengthMLCheck.setSelected(true);
        accDecLengthMLCheck.setText("Ramp Acc/Dec Length (ft)");
        accDecLengthMLCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        accDecLengthMLCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                accDecLengthMLCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(accDecLengthMLCheck);

        accDecLengthMLText.setText("500");
        accDecLengthMLText.setPreferredSize(new java.awt.Dimension(50, 28));
        accDecLengthMLText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                accDecLengthMLTextFocusGained(evt);
            }
        });
        fillMLJPanel.add(accDecLengthMLText);

        numOfMLLanesCheck.setSelected(true);
        numOfMLLanesCheck.setText("Num Of ML Lanes");
        numOfMLLanesCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        numOfMLLanesCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                numOfMLLanesCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(numOfMLLanesCheck);

        numOfMLLanesText.setText("1");
        numOfMLLanesText.setPreferredSize(new java.awt.Dimension(50, 28));
        numOfMLLanesText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numOfMLLanesTextFocusGained(evt);
            }
        });
        fillMLJPanel.add(numOfMLLanesText);

        numOfMLRampLanesCheck.setSelected(true);
        numOfMLRampLanesCheck.setText("Num Of ML Ramp Lanes");
        numOfMLRampLanesCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        numOfMLRampLanesCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                numOfMLRampLanesCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(numOfMLRampLanesCheck);

        numOfMLRampLanesText.setText("1");
        numOfMLRampLanesText.setPreferredSize(new java.awt.Dimension(50, 28));
        numOfMLRampLanesText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numOfMLRampLanesTextFocusGained(evt);
            }
        });
        fillMLJPanel.add(numOfMLRampLanesText);

        MLFFSCheck.setSelected(true);
        MLFFSCheck.setText("ML FFS (mph)");
        MLFFSCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        MLFFSCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MLFFSCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(MLFFSCheck);

        MLFFSText.setText("70");
        MLFFSText.setPreferredSize(new java.awt.Dimension(50, 28));
        MLFFSText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                MLFFSTextFocusGained(evt);
            }
        });
        fillMLJPanel.add(MLFFSText);

        rampMLFFSCheck.setSelected(true);
        rampMLFFSCheck.setText("ML Ramp FFS (mph)");
        rampMLFFSCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        rampMLFFSCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rampMLFFSCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(rampMLFFSCheck);

        rampMLFFSText.setText("45");
        rampMLFFSText.setPreferredSize(new java.awt.Dimension(50, 28));
        rampMLFFSText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rampMLFFSTextFocusGained(evt);
            }
        });
        fillMLJPanel.add(rampMLFFSText);

        truckMLCheck.setSelected(true);
        truckMLCheck.setText("ML Truck (%)");
        truckMLCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        truckMLCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                truckMLCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(truckMLCheck);

        truckMLText.setText("5");
        truckMLText.setPreferredSize(new java.awt.Dimension(50, 28));
        truckMLText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                truckMLTextFocusGained(evt);
            }
        });
        fillMLJPanel.add(truckMLText);

        RVMLCheck.setSelected(true);
        RVMLCheck.setText("ML RV (%)");
        RVMLCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        RVMLCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RVMLCheckItemStateChanged(evt);
            }
        });
        fillMLJPanel.add(RVMLCheck);

        RVMLText.setText("0");
        RVMLText.setPreferredSize(new java.awt.Dimension(50, 28));
        RVMLText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                RVMLTextFocusGained(evt);
            }
        });
        fillMLJPanel.add(RVMLText);
        fillMLJPanel.add(jLabel13);
        fillMLJPanel.add(jLabel14);
        fillMLJPanel.add(jLabel15);
        fillMLJPanel.add(jLabel16);
        fillMLJPanel.add(jLabel17);
        fillMLJPanel.add(jLabel18);
        fillMLJPanel.add(jLabel19);
        fillMLJPanel.add(jLabel20);
        fillMLJPanel.add(jLabel21);
        fillMLJPanel.add(jLabel22);
        fillMLJPanel.add(jLabel23);
        fillMLJPanel.add(jLabel24);

        checkButton.setText("Verify Data");
        checkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkButtonActionPerformed(evt);
            }
        });

        MLTypeCheck.setSelected(true);
        MLTypeCheck.setText("Managed Lane Type");
        MLTypeCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MLTypeCheckItemStateChanged(evt);
            }
        });

        MLTypeCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HOV", "HOT" }));

        setTitle("Project Properties");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

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

        generalJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("General Information"));
        generalJPanel.setLayout(new java.awt.GridLayout(5, 4));

        jLabel8.setText(" Project Name");
        jLabel8.setPreferredSize(new java.awt.Dimension(70, 16));
        generalJPanel.add(jLabel8);

        seedNameText.setText("New Project");
        seedNameText.setPreferredSize(new java.awt.Dimension(90, 28));
        generalJPanel.add(seedNameText);

        jLabel1.setText(" Number Of HCM Segments");
        generalJPanel.add(jLabel1);

        numOfSegmentsText.setText("7");
        numOfSegmentsText.setPreferredSize(new java.awt.Dimension(50, 28));
        numOfSegmentsText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numOfSegmentsTextFocusGained(evt);
            }
        });
        generalJPanel.add(numOfSegmentsText);

        jLabel2.setText(" Study Period Start Time (hh:mm)");
        generalJPanel.add(jLabel2);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        startHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        startHour.setSelectedIndex(17);
        jPanel1.add(startHour);

        startMin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "15", "30", "45" }));
        jPanel1.add(startMin);

        generalJPanel.add(jPanel1);

        jLabel3.setText(" Study Period End Time (hh:mm)");
        generalJPanel.add(jLabel3);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        endHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        endHour.setSelectedIndex(18);
        jPanel2.add(endHour);

        endMin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "15", "30", "45" }));
        jPanel2.add(endMin);

        generalJPanel.add(jPanel2);

        jLabel9.setText(" Jam Density (pc/mi/ln)");
        generalJPanel.add(jLabel9);

        jamDensityText.setText("190");
        jamDensityText.setPreferredSize(new java.awt.Dimension(50, 28));
        jamDensityText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jamDensityTextFocusGained(evt);
            }
        });
        generalJPanel.add(jamDensityText);

        jLabel10.setText(" Capacity Drop due to Congestion (%)");
        generalJPanel.add(jLabel10);

        capacityDropText.setText("5");
        capacityDropText.setPreferredSize(new java.awt.Dimension(50, 28));
        capacityDropText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                capacityDropTextFocusGained(evt);
            }
        });
        generalJPanel.add(capacityDropText);

        jLabel4.setText(" GP Vehicle Occupancy (p/veh)");
        generalJPanel.add(jLabel4);

        gpOccText.setText("1.0");
        gpOccText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                gpOccTextFocusGained(evt);
            }
        });
        generalJPanel.add(gpOccText);

        mlOccLabel.setText(" ML Vehicle Occupancy (p/veh)");
        generalJPanel.add(mlOccLabel);

        mlOccText.setText("1.0");
        mlOccText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mlOccTextFocusGained(evt);
            }
        });
        generalJPanel.add(mlOccText);

        addSegButton.setText("+Segment");
        addSegButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSegButtonActionPerformed(evt);
            }
        });
        generalJPanel.add(addSegButton);

        delSegButton.setText("-Segment");
        delSegButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delSegButtonActionPerformed(evt);
            }
        });
        generalJPanel.add(delSegButton);

        addPeriodButton.setText("+Period");
        addPeriodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPeriodButtonActionPerformed(evt);
            }
        });
        generalJPanel.add(addPeriodButton);

        delPeriodButton.setText("-Period");
        delPeriodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delPeriodButtonActionPerformed(evt);
            }
        });
        generalJPanel.add(delPeriodButton);

        optionsJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis Options"));
        optionsJPanel.setLayout(new java.awt.GridLayout(1, 0));

        ffsKnownCheck.setSelected(true);
        ffsKnownCheck.setText("Free Flow Speed Known");
        ffsKnownCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ffsKnownCheckItemStateChanged(evt);
            }
        });
        optionsJPanel.add(ffsKnownCheck);

        useRampMeteringCheck.setText("Use Ramp Metering");
        useRampMeteringCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useRampMeteringCheckItemStateChanged(evt);
            }
        });
        optionsJPanel.add(useRampMeteringCheck);

        manageLaneCheck.setText("Managed Lanes Analysis");
        manageLaneCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                manageLaneCheckItemStateChanged(evt);
            }
        });
        optionsJPanel.add(manageLaneCheck);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Prefill Global Values"));

        fillGPJPanel.setLayout(new java.awt.GridLayout(7, 4));

        terrainCheck.setSelected(true);
        terrainCheck.setText("Terrain");
        terrainCheck.setPreferredSize(new java.awt.Dimension(100, 28));
        terrainCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                terrainCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(terrainCheck);

        terrainComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        terrainComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terrainComboBoxActionPerformed(evt);
            }
        });
        fillGPJPanel.add(terrainComboBox);
        fillGPJPanel.add(jLabel11);
        fillGPJPanel.add(jLabel6);

        numOfMainlineLanesCheck.setSelected(true);
        numOfMainlineLanesCheck.setText("Num Of Mainline Lanes");
        numOfMainlineLanesCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        numOfMainlineLanesCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                numOfMainlineLanesCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(numOfMainlineLanesCheck);

        numOfMainlineLanesText.setText("3");
        numOfMainlineLanesText.setPreferredSize(new java.awt.Dimension(50, 28));
        numOfMainlineLanesText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numOfMainlineLanesTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(numOfMainlineLanesText);

        mainlineFFSCheck.setSelected(true);
        mainlineFFSCheck.setText("Mainline FFS (mph)");
        mainlineFFSCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        mainlineFFSCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mainlineFFSCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(mainlineFFSCheck);

        mainlineFFSText.setText("70");
        mainlineFFSText.setPreferredSize(new java.awt.Dimension(50, 28));
        mainlineFFSText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mainlineFFSTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(mainlineFFSText);

        laneWidthCheck.setText("Lane Width (ft)");
        laneWidthCheck.setEnabled(false);
        laneWidthCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        laneWidthCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                laneWidthCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(laneWidthCheck);

        laneWidthText.setText("12");
        laneWidthText.setPreferredSize(new java.awt.Dimension(50, 28));
        laneWidthText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                laneWidthTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(laneWidthText);

        lateralClearanceCheck.setText("Lateral Clearance (ft)");
        lateralClearanceCheck.setEnabled(false);
        lateralClearanceCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        lateralClearanceCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lateralClearanceCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(lateralClearanceCheck);

        lateralClearanceText.setText("4");
        lateralClearanceText.setPreferredSize(new java.awt.Dimension(50, 28));
        lateralClearanceText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lateralClearanceTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(lateralClearanceText);

        numOfRampLanesCheck.setSelected(true);
        numOfRampLanesCheck.setText("Num Of Ramp Lanes");
        numOfRampLanesCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        numOfRampLanesCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                numOfRampLanesCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(numOfRampLanesCheck);

        numOfRampLanesText.setText("1");
        numOfRampLanesText.setPreferredSize(new java.awt.Dimension(50, 28));
        numOfRampLanesText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numOfRampLanesTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(numOfRampLanesText);

        rampFFSCheck.setSelected(true);
        rampFFSCheck.setText("Ramp FFS (mph)");
        rampFFSCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        rampFFSCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rampFFSCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(rampFFSCheck);

        rampFFSText.setText("45");
        rampFFSText.setPreferredSize(new java.awt.Dimension(50, 28));
        rampFFSText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rampFFSTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(rampFFSText);

        accDecLengthCheck.setSelected(true);
        accDecLengthCheck.setText("Ramp Acc/Dec Length (ft)");
        accDecLengthCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        accDecLengthCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                accDecLengthCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(accDecLengthCheck);

        accDecLengthText.setText("500");
        accDecLengthText.setPreferredSize(new java.awt.Dimension(50, 28));
        accDecLengthText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                accDecLengthTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(accDecLengthText);

        rampMeteringCheck.setText("Ramp Metering Rate (veh/h)");
        rampMeteringCheck.setEnabled(false);
        rampMeteringCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        rampMeteringCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rampMeteringCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(rampMeteringCheck);

        rampMeteringText.setText("2100");
        rampMeteringText.setPreferredSize(new java.awt.Dimension(50, 28));
        rampMeteringText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rampMeteringTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(rampMeteringText);

        truckCheck.setSelected(true);
        truckCheck.setText("Truck (%)");
        truckCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        truckCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                truckCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(truckCheck);

        truckText.setText("5");
        truckText.setPreferredSize(new java.awt.Dimension(50, 28));
        truckText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                truckTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(truckText);

        RVCheck.setSelected(true);
        RVCheck.setText("RV (%)");
        RVCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        RVCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RVCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(RVCheck);

        RVText.setText("0");
        RVText.setPreferredSize(new java.awt.Dimension(50, 28));
        RVText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                RVTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(RVText);

        ETCheck.setSelected(true);
        ETCheck.setText("Truck-PC Equivalent");
        ETCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        ETCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ETCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(ETCheck);

        ETText.setText("1.5");
        ETText.setPreferredSize(new java.awt.Dimension(50, 28));
        ETText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ETTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(ETText);

        ERCheck.setSelected(true);
        ERCheck.setText("RV-PC Equivalent");
        ERCheck.setPreferredSize(new java.awt.Dimension(200, 28));
        ERCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ERCheckItemStateChanged(evt);
            }
        });
        fillGPJPanel.add(ERCheck);

        ERText.setText("1.2");
        ERText.setPreferredSize(new java.awt.Dimension(50, 28));
        ERText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ERTextFocusGained(evt);
            }
        });
        fillGPJPanel.add(ERText);

        jTabbedPane1.addTab("General Purpose Segments", fillGPJPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addComponent(optionsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(generalJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(generalJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionsJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doInputCheck();
        if (inputCheckPassed) {
            setupSeed();
            doClose(RET_OK);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void manageLaneCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_manageLaneCheckItemStateChanged
        if (manageLaneCheck.isSelected()) {
            jTabbedPane1.addTab("Managed Lanes Segments", fillMLJPanel);
            mlOccText.setVisible(true);
            mlOccLabel.setVisible(true);
        } else {
            jTabbedPane1.remove(fillMLJPanel);
            mlOccText.setVisible(false);
            mlOccLabel.setVisible(false);
        }
    }//GEN-LAST:event_manageLaneCheckItemStateChanged

    private void ffsKnownCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ffsKnownCheckItemStateChanged
        mainlineFFSCheck.setEnabled(ffsKnownCheck.isSelected());
        mainlineFFSText.setVisible(ffsKnownCheck.isSelected() && mainlineFFSCheck.isSelected());
        laneWidthCheck.setEnabled(!ffsKnownCheck.isSelected());
        laneWidthText.setVisible(!ffsKnownCheck.isSelected() && laneWidthCheck.isSelected());
        lateralClearanceCheck.setEnabled(!ffsKnownCheck.isSelected());
        lateralClearanceText.setVisible(!ffsKnownCheck.isSelected() && lateralClearanceCheck.isSelected());
    }//GEN-LAST:event_ffsKnownCheckItemStateChanged

    private void useRampMeteringCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_useRampMeteringCheckItemStateChanged
        rampMeteringCheck.setEnabled(useRampMeteringCheck.isSelected());
        rampMeteringText.setVisible(useRampMeteringCheck.isSelected() && rampMeteringCheck.isSelected());
    }//GEN-LAST:event_useRampMeteringCheckItemStateChanged

    private void checkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkButtonActionPerformed
        doInputCheck();
    }//GEN-LAST:event_checkButtonActionPerformed

    private void terrainCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_terrainCheckItemStateChanged
        terrainComboBox.setVisible(terrainCheck.isSelected());
    }//GEN-LAST:event_terrainCheckItemStateChanged

    private void numOfMainlineLanesCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numOfMainlineLanesCheckItemStateChanged
        numOfMainlineLanesText.setVisible(numOfMainlineLanesCheck.isSelected());
    }//GEN-LAST:event_numOfMainlineLanesCheckItemStateChanged

    private void laneWidthCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_laneWidthCheckItemStateChanged
        laneWidthText.setVisible(laneWidthCheck.isSelected());
    }//GEN-LAST:event_laneWidthCheckItemStateChanged

    private void numOfRampLanesCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numOfRampLanesCheckItemStateChanged
        numOfRampLanesText.setVisible(numOfRampLanesCheck.isSelected());
    }//GEN-LAST:event_numOfRampLanesCheckItemStateChanged

    private void accDecLengthCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_accDecLengthCheckItemStateChanged
        accDecLengthText.setVisible(accDecLengthCheck.isSelected());
    }//GEN-LAST:event_accDecLengthCheckItemStateChanged

    private void truckCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_truckCheckItemStateChanged
        truckText.setVisible(truckCheck.isSelected());
    }//GEN-LAST:event_truckCheckItemStateChanged

    private void RVCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RVCheckItemStateChanged
        RVText.setVisible(RVCheck.isSelected());
    }//GEN-LAST:event_RVCheckItemStateChanged

    private void mainlineFFSCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mainlineFFSCheckItemStateChanged
        mainlineFFSText.setVisible(mainlineFFSCheck.isSelected());
    }//GEN-LAST:event_mainlineFFSCheckItemStateChanged

    private void lateralClearanceCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lateralClearanceCheckItemStateChanged
        lateralClearanceText.setVisible(lateralClearanceCheck.isSelected());
    }//GEN-LAST:event_lateralClearanceCheckItemStateChanged

    private void rampFFSCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rampFFSCheckItemStateChanged
        rampFFSText.setVisible(rampFFSCheck.isSelected());
    }//GEN-LAST:event_rampFFSCheckItemStateChanged

    private void rampMeteringCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rampMeteringCheckItemStateChanged
        rampMeteringText.setVisible(rampMeteringCheck.isSelected());
    }//GEN-LAST:event_rampMeteringCheckItemStateChanged

    private void ETCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ETCheckItemStateChanged
        ETText.setVisible(ETCheck.isSelected());
    }//GEN-LAST:event_ETCheckItemStateChanged

    private void ERCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ERCheckItemStateChanged
        ERText.setVisible(ERCheck.isSelected());
    }//GEN-LAST:event_ERCheckItemStateChanged

    private void separationCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_separationCheckItemStateChanged
        separationCB.setVisible(separationCheck.isSelected());
    }//GEN-LAST:event_separationCheckItemStateChanged

    private void MLTypeCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_MLTypeCheckItemStateChanged
        MLTypeCB.setVisible(MLTypeCheck.isSelected());
    }//GEN-LAST:event_MLTypeCheckItemStateChanged

    private void numOfMLLanesCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numOfMLLanesCheckItemStateChanged
        numOfMLLanesText.setVisible(numOfMLLanesCheck.isSelected());
    }//GEN-LAST:event_numOfMLLanesCheckItemStateChanged

    private void MLFFSCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_MLFFSCheckItemStateChanged
        MLFFSText.setVisible(MLFFSCheck.isSelected());
    }//GEN-LAST:event_MLFFSCheckItemStateChanged

    private void numOfMLRampLanesCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_numOfMLRampLanesCheckItemStateChanged
        numOfMLRampLanesText.setVisible(numOfMLRampLanesCheck.isSelected());
    }//GEN-LAST:event_numOfMLRampLanesCheckItemStateChanged

    private void rampMLFFSCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rampMLFFSCheckItemStateChanged
        rampMLFFSText.setVisible(rampMLFFSCheck.isSelected());
    }//GEN-LAST:event_rampMLFFSCheckItemStateChanged

    private void truckMLCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_truckMLCheckItemStateChanged
        truckMLText.setVisible(truckMLCheck.isSelected());
    }//GEN-LAST:event_truckMLCheckItemStateChanged

    private void RVMLCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RVMLCheckItemStateChanged
        RVMLText.setVisible(RVMLCheck.isSelected());
    }//GEN-LAST:event_RVMLCheckItemStateChanged

    private void accDecLengthMLCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_accDecLengthMLCheckItemStateChanged
        accDecLengthMLText.setVisible(accDecLengthMLCheck.isSelected());
    }//GEN-LAST:event_accDecLengthMLCheckItemStateChanged

    private void addSegButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSegButtonActionPerformed
        mainWindow.addSegment();
    }//GEN-LAST:event_addSegButtonActionPerformed

    private void delSegButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delSegButtonActionPerformed
        mainWindow.delSegment();
    }//GEN-LAST:event_delSegButtonActionPerformed

    private void addPeriodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPeriodButtonActionPerformed
        mainWindow.addPeriod();
    }//GEN-LAST:event_addPeriodButtonActionPerformed

    private void delPeriodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delPeriodButtonActionPerformed
        mainWindow.delPeriod();
    }//GEN-LAST:event_delPeriodButtonActionPerformed

    private void numOfMLLanesTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numOfMLLanesTextFocusGained
        numOfMLLanesText.setForeground(Color.black);
    }//GEN-LAST:event_numOfMLLanesTextFocusGained

    private void numOfMLRampLanesTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numOfMLRampLanesTextFocusGained
        numOfMLRampLanesText.setForeground(Color.black);
    }//GEN-LAST:event_numOfMLRampLanesTextFocusGained

    private void accDecLengthMLTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accDecLengthMLTextFocusGained
        accDecLengthMLText.setForeground(Color.black);
    }//GEN-LAST:event_accDecLengthMLTextFocusGained

    private void truckMLTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_truckMLTextFocusGained
        truckMLText.setForeground(Color.black);
    }//GEN-LAST:event_truckMLTextFocusGained

    private void MLFFSTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MLFFSTextFocusGained
        MLFFSText.setForeground(Color.black);
    }//GEN-LAST:event_MLFFSTextFocusGained

    private void rampMLFFSTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rampMLFFSTextFocusGained
        rampMLFFSText.setForeground(Color.black);
    }//GEN-LAST:event_rampMLFFSTextFocusGained

    private void RVMLTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_RVMLTextFocusGained
        RVMLText.setForeground(Color.black);
    }//GEN-LAST:event_RVMLTextFocusGained

    private void numOfSegmentsTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numOfSegmentsTextFocusGained
        numOfSegmentsText.setForeground(Color.black);
    }//GEN-LAST:event_numOfSegmentsTextFocusGained

    private void capacityDropTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_capacityDropTextFocusGained
        capacityDropText.setForeground(Color.black);
    }//GEN-LAST:event_capacityDropTextFocusGained

    private void jamDensityTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jamDensityTextFocusGained
        jamDensityText.setForeground(Color.black);
    }//GEN-LAST:event_jamDensityTextFocusGained

    private void gpOccTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_gpOccTextFocusGained
        gpOccText.setForeground(Color.black);
    }//GEN-LAST:event_gpOccTextFocusGained

    private void mlOccTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mlOccTextFocusGained
        mlOccText.setForeground(Color.black);
    }//GEN-LAST:event_mlOccTextFocusGained

    private void numOfMainlineLanesTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numOfMainlineLanesTextFocusGained
        numOfMainlineLanesText.setForeground(Color.black);
    }//GEN-LAST:event_numOfMainlineLanesTextFocusGained

    private void laneWidthTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_laneWidthTextFocusGained
        laneWidthText.setForeground(Color.black);
    }//GEN-LAST:event_laneWidthTextFocusGained

    private void numOfRampLanesTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numOfRampLanesTextFocusGained
        numOfRampLanesText.setForeground(Color.black);
    }//GEN-LAST:event_numOfRampLanesTextFocusGained

    private void accDecLengthTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_accDecLengthTextFocusGained
        accDecLengthText.setForeground(Color.black);
    }//GEN-LAST:event_accDecLengthTextFocusGained

    private void truckTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_truckTextFocusGained
        truckText.setForeground(Color.black);
    }//GEN-LAST:event_truckTextFocusGained

    private void ETTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ETTextFocusGained
        ETText.setForeground(Color.black);
    }//GEN-LAST:event_ETTextFocusGained

    private void mainlineFFSTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mainlineFFSTextFocusGained
        mainlineFFSText.setForeground(Color.black);
    }//GEN-LAST:event_mainlineFFSTextFocusGained

    private void lateralClearanceTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lateralClearanceTextFocusGained
        lateralClearanceText.setForeground(Color.black);
    }//GEN-LAST:event_lateralClearanceTextFocusGained

    private void rampFFSTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rampFFSTextFocusGained
        rampFFSText.setForeground(Color.black);
    }//GEN-LAST:event_rampFFSTextFocusGained

    private void rampMeteringTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rampMeteringTextFocusGained
        rampMeteringText.setForeground(Color.black);
    }//GEN-LAST:event_rampMeteringTextFocusGained

    private void RVTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_RVTextFocusGained
        RVText.setForeground(Color.black);
    }//GEN-LAST:event_RVTextFocusGained

    private void ERTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ERTextFocusGained
        ERText.setForeground(Color.black);
    }//GEN-LAST:event_ERTextFocusGained

    private void terrainComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terrainComboBoxActionPerformed
        ETCheck.setSelected(true);
        ERCheck.setSelected(true);
        switch ((String) terrainComboBox.getSelectedItem()) {
            case CEConst.STR_TERRAIN_MOUNTAINOUS:
                ETText.setText("4.5");
                ERText.setText("4.0");
                break;
            case CEConst.STR_TERRAIN_ROLLING:
                ETText.setText("2.5");
                ERText.setText("2.0");
                break;
            case CEConst.STR_TERRAIN_LEVEL:
                ETText.setText("1.5");
                ERText.setText("1.2");
                break;
        }
    }//GEN-LAST:event_terrainComboBoxActionPerformed

    private void doClose(int retStatus) {
        if (retStatus == RET_CANCEL) {
            seed = null;
        }

        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    private void setupSeed() {
        //save general data to seed
        seed.setValue(CEConst.IDS_PROJECT_NAME, seedNameText.getText());
        seed.setFreeFlowSpeedKnown(ffsKnownCheck.isSelected());
        seed.setRampMeteringUsed(useRampMeteringCheck.isSelected());
        seed.setValue(CEConst.IDS_JAM_DENSITY, Float.parseFloat(jamDensityText.getText()));
        seed.setValue(CEConst.IDS_CAPACITY_ALPHA, Integer.parseInt(capacityDropText.getText()));

        seed.setValue(CEConst.IDS_OCCU_GP, Float.parseFloat(gpOccText.getText()));

        if (manageLaneCheck.isSelected()) {
            seed.setValue(CEConst.IDS_OCCU_ML, Float.parseFloat(mlOccText.getText()));
        }

        seed.setManagedLaneUsed(manageLaneCheck.isSelected());

        if (isNewSeed) {
            seed.setValue(CEConst.IDS_START_TIME, new CETime(Integer.parseInt((String) startHour.getSelectedItem()), Integer.parseInt((String) startMin.getSelectedItem())));
            seed.setValue(CEConst.IDS_END_TIME, new CETime(Integer.parseInt((String) endHour.getSelectedItem()), Integer.parseInt((String) endMin.getSelectedItem())));
            seed.generateSegments(Integer.parseInt(numOfSegmentsText.getText()));
            saveDefaultValueToSeed();
        } else {
            if (willOverwrite()) {
                int n = JOptionPane.showConfirmDialog(this,
                        "Warning: Fill data will overwrite some of existing data",
                        "Warning",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (n == JOptionPane.OK_OPTION) {
                    saveDefaultValueToSeed();
                }
            }
        }
    }

    private boolean willOverwrite() {
        return terrainCheck.isSelected()
                || mainlineFFSCheck.isSelected()
                || lateralClearanceCheck.isSelected()
                || rampFFSCheck.isSelected()
                || rampMeteringCheck.isSelected()
                || ETCheck.isSelected()
                || ERCheck.isSelected()
                || numOfMainlineLanesCheck.isSelected()
                || laneWidthCheck.isSelected()
                || numOfRampLanesCheck.isSelected()
                || accDecLengthCheck.isSelected()
                || truckCheck.isSelected()
                || RVCheck.isSelected()
                || separationCheck.isSelected()
                || MLTypeCheck.isSelected()
                || numOfMLLanesCheck.isSelected()
                || MLFFSCheck.isSelected()
                || numOfMLRampLanesCheck.isSelected()
                || rampMLFFSCheck.isSelected()
                || accDecLengthMLCheck.isSelected()
                || truckMLCheck.isSelected()
                || RVMLCheck.isSelected();
    }

    private void saveDefaultValueToSeed() {
        //General Purpose Lane Prefill
        if (terrainCheck.isEnabled() && terrainCheck.isSelected()) {
            int terrain;
            switch ((String) terrainComboBox.getSelectedItem()) {
                case CEConst.STR_TERRAIN_MOUNTAINOUS:
                    terrain = CEConst.TERRAIN_MOUNTAINOUS;
                    break;
                case CEConst.STR_TERRAIN_ROLLING:
                    terrain = CEConst.TERRAIN_ROLLING;
                    break;
                case CEConst.STR_TERRAIN_VARYING_OR_OTHER:
                    terrain = CEConst.TERRAIN_VARYING_OR_OTHER;
                    break;
                default:
                    terrain = CEConst.TERRAIN_LEVEL;
                    break;
            }
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_TERRAIN, terrain, seg);
            }
        }

        if (numOfMainlineLanesCheck.isEnabled() && numOfMainlineLanesCheck.isSelected()) {
            int num = Integer.parseInt(numOfMainlineLanesText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_MAIN_NUM_LANES_IN, num, seg, period);
                }
            }
        }

        if (mainlineFFSCheck.isEnabled() && mainlineFFSCheck.isSelected()) {
            int num = Integer.parseInt(mainlineFFSText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_MAIN_FREE_FLOW_SPEED, num, seg, period);
                }
            }
        }

        if (laneWidthCheck.isEnabled() && laneWidthCheck.isSelected()) {
            int num = Integer.parseInt(laneWidthText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_LANE_WIDTH, num, seg);
            }
        }

        if (lateralClearanceCheck.isEnabled() && lateralClearanceCheck.isSelected()) {
            int num = Integer.parseInt(lateralClearanceText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_LATERAL_CLEARANCE, num, seg);
            }
        }

        if (numOfRampLanesCheck.isEnabled() && numOfRampLanesCheck.isSelected()) {
            int num = Integer.parseInt(numOfRampLanesText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_NUM_ON_RAMP_LANES, num, seg, period);
                    seed.setValue(CEConst.IDS_NUM_OFF_RAMP_LANES, num, seg, period);
                }
            }
        }

        if (rampFFSCheck.isEnabled() && rampFFSCheck.isSelected()) {
            int num = Integer.parseInt(rampFFSText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ON_RAMP_FREE_FLOW_SPEED, num, seg, period);
                    seed.setValue(CEConst.IDS_OFF_RAMP_FREE_FLOW_SPEED, num, seg, period);
                }
            }
        }

        if (accDecLengthCheck.isEnabled() && accDecLengthCheck.isSelected()) {
            int num = Integer.parseInt(accDecLengthText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_ACC_DEC_LANE_LENGTH, num, seg);
            }
        }

        if (rampMeteringCheck.isEnabled() && rampMeteringCheck.isSelected()) {
            int num = Integer.parseInt(rampMeteringText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ON_RAMP_METERING_RATE, num, seg, period);
                }
            }
        }

        if (truckCheck.isEnabled() && truckCheck.isSelected()) {
            int num = Integer.parseInt(truckText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_TRUCK_PERCENTAGE, num, seg, period);
                }
            }
        }

        if (ETCheck.isEnabled() && ETCheck.isSelected()) {
            float num = Float.parseFloat(ETText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_TRUCK_CAR_ET, num, seg);
            }
        }

        if (RVCheck.isEnabled() && RVCheck.isSelected()) {
            int num = Integer.parseInt(RVText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_RV_PERCENTAGE, num, seg, period);
                }
            }
        }

        if (ERCheck.isEnabled() && ERCheck.isSelected()) {
            float num = Float.parseFloat(ERText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_RV_CAR_ER, num, seg);
            }
        }

        //Managed Lane Prefill
        if (manageLaneCheck.isSelected() && separationCheck.isSelected()) {
            int separation;
            switch ((String) separationCB.getSelectedItem()) {
                case CEConst.STR_ML_SEPARATION_BARRIER:
                    separation = CEConst.ML_SEPARATION_BARRIER;
                    break;
                case CEConst.STR_ML_SEPARATION_BUFFER:
                    separation = CEConst.ML_SEPARATION_BUFFER;
                    break;
                default:
                    separation = CEConst.ML_SEPARATION_MARKING;
                    break;
            }
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_ML_SEPARATION_TYPE, separation, seg);
            }
        }

        if (manageLaneCheck.isSelected() && MLTypeCheck.isSelected()) {
            int terrain;
            switch ((String) MLTypeCB.getSelectedItem()) {
                case CEConst.STR_ML_METHOD_HOT:
                    terrain = CEConst.ML_METHOD_HOT;
                    break;
                default:
                    terrain = CEConst.ML_METHOD_HOV;
                    break;
            }
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                seed.setValue(CEConst.IDS_ML_METHOD_TYPE, terrain, seg);
            }
        }

        if (manageLaneCheck.isSelected() && numOfMLLanesCheck.isSelected()) {
            int num = Integer.parseInt(numOfMLLanesText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ML_NUM_LANES, num, seg, period);
                }
            }
        }

        if (manageLaneCheck.isSelected() && MLFFSCheck.isSelected()) {
            int num = Integer.parseInt(MLFFSText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ML_FREE_FLOW_SPEED, num, seg, period);
                }
            }
        }

        if (manageLaneCheck.isSelected() && numOfMLRampLanesCheck.isSelected()) {
            int num = Integer.parseInt(numOfMLRampLanesText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ML_NUM_ON_RAMP_LANES, num, seg, period);
                    seed.setValue(CEConst.IDS_ML_NUM_OFF_RAMP_LANES, num, seg, period);
                }
            }
        }

        if (manageLaneCheck.isSelected() && rampMLFFSCheck.isSelected()) {
            int num = Integer.parseInt(rampMLFFSText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ML_ON_RAMP_FREE_FLOW_SPEED, num, seg, period);
                    seed.setValue(CEConst.IDS_ML_OFF_RAMP_FREE_FLOW_SPEED, num, seg, period);
                }
            }
        }

        if (manageLaneCheck.isSelected() && accDecLengthMLCheck.isSelected()) {
            int num = Integer.parseInt(accDecLengthMLText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ML_ACC_DEC_LANE_LENGTH, num, seg, period);
                }
            }
        }

        if (manageLaneCheck.isSelected() && truckMLCheck.isSelected()) {
            int num = Integer.parseInt(truckMLText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ML_TRUCK_PERCENTAGE, num, seg, period);
                }
            }
        }

        if (manageLaneCheck.isSelected() && RVMLCheck.isSelected()) {
            int num = Integer.parseInt(RVMLText.getText());
            for (int seg = 0; seg < seed.getValueInt(CEConst.IDS_ML_NUM_SEGMENT); seg++) {
                for (int period = 0; period < seed.getValueInt(CEConst.IDS_NUM_PERIOD); period++) {
                    seed.setValue(CEConst.IDS_ML_RV_PERCENTAGE, num, seg, period);
                }
            }
        }
    }

    private void doInputCheck() {
        //GP
        inputCheckPassed = true;
        inputCheckPassed = checkPositiveInt(numOfSegmentsText) && inputCheckPassed;
        inputCheckPassed = checkAnalysisPeriodTime() && inputCheckPassed;
        inputCheckPassed = checkPositiveFloat(jamDensityText) && inputCheckPassed;
        inputCheckPassed = checkNonNegInt(capacityDropText) && inputCheckPassed;
        inputCheckPassed = checkPositiveFloat(gpOccText) && inputCheckPassed;
        inputCheckPassed = checkPositiveFloat(mlOccText) && inputCheckPassed;

        inputCheckPassed = (!numOfMainlineLanesText.isVisible() || checkPositiveInt(numOfMainlineLanesText)) && inputCheckPassed;
        inputCheckPassed = (!mainlineFFSText.isVisible() || checkPositiveInt(mainlineFFSText)) && inputCheckPassed;
        inputCheckPassed = (!laneWidthText.isVisible() || checkPositiveInt(laneWidthText)) && inputCheckPassed;
        inputCheckPassed = (!lateralClearanceText.isVisible() || checkPositiveInt(lateralClearanceText)) && inputCheckPassed;
        inputCheckPassed = (!numOfRampLanesText.isVisible() || checkPositiveInt(numOfRampLanesText)) && inputCheckPassed;
        inputCheckPassed = (!rampFFSText.isVisible() || checkPositiveInt(rampFFSText)) && inputCheckPassed;
        inputCheckPassed = (!accDecLengthText.isVisible() || checkPositiveInt(accDecLengthText)) && inputCheckPassed;
        inputCheckPassed = (!rampMeteringText.isVisible() || checkPositiveInt(rampMeteringText)) && inputCheckPassed;

        inputCheckPassed = (!truckText.isVisible() || checkNonNegFloat(truckText)) && inputCheckPassed;
        inputCheckPassed = (!ETText.isVisible() || checkNonNegFloat(ETText)) && inputCheckPassed;
        inputCheckPassed = (!RVText.isVisible() || checkNonNegFloat(RVText)) && inputCheckPassed;
        inputCheckPassed = (!ERText.isVisible() || checkNonNegFloat(ERText)) && inputCheckPassed;

        //ML
        inputCheckPassed = (!manageLaneCheck.isSelected() || !numOfMLLanesText.isVisible() || checkPositiveInt(numOfMLLanesText)) && inputCheckPassed;
        inputCheckPassed = (!manageLaneCheck.isSelected() || !MLFFSText.isVisible() || checkPositiveInt(MLFFSText)) && inputCheckPassed;
        inputCheckPassed = (!manageLaneCheck.isSelected() || !numOfMLRampLanesText.isVisible() || checkPositiveInt(numOfMLRampLanesText)) && inputCheckPassed;
        inputCheckPassed = (!manageLaneCheck.isSelected() || !rampMLFFSText.isVisible() || checkPositiveInt(rampMLFFSText)) && inputCheckPassed;
        inputCheckPassed = (!manageLaneCheck.isSelected() || !accDecLengthMLText.isVisible() || checkPositiveInt(accDecLengthMLText)) && inputCheckPassed;

        inputCheckPassed = (!manageLaneCheck.isSelected() || !truckMLText.isVisible() || checkNonNegFloat(truckMLText)) && inputCheckPassed;
        inputCheckPassed = (!manageLaneCheck.isSelected() || !RVMLText.isVisible() || checkNonNegFloat(RVMLText)) && inputCheckPassed;

        //inputCheckPassed = checkReliabilityInput() && inputCheckPassed;
        if (!inputCheckPassed) {
            JOptionPane.showMessageDialog(this, "Error found. Please check input data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkPositiveInt(JComponent input) {
        JTextField tf = (JTextField) input;
        if (input.isEnabled() && input.isVisible()) {
            try {
                int num = Integer.parseInt(tf.getText());
                if (num > 0) {
                    tf.setForeground(Color.black);
                    return true;
                } else {
                    tf.setForeground(Color.red);
                    return false;
                }
            } catch (NumberFormatException e) {
                tf.setForeground(Color.red);
                return false;
            }
        } else {
            //pass not enabled text
            tf.setForeground(Color.black);
            return true;
        }
    }

    private boolean checkNonNegInt(JComponent input) {
        JTextField tf = (JTextField) input;
        if (input.isEnabled() && input.isVisible()) {
            try {
                int num = Integer.parseInt(tf.getText());
                if (num >= 0) {
                    tf.setForeground(Color.black);
                    return true;
                } else {
                    tf.setForeground(Color.red);
                    return false;
                }
            } catch (NumberFormatException e) {
                tf.setForeground(Color.red);
                return false;
            }
        } else {
            //pass not enabled text
            tf.setForeground(Color.black);
            return true;
        }
    }

    private boolean checkNonNegFloat(JComponent input) {
        JTextField tf = (JTextField) input;
        if (input.isEnabled() && input.isVisible()) {
            try {
                float num = Float.parseFloat(tf.getText());
                if (num >= 0) {
                    tf.setForeground(Color.black);
                    return true;
                } else {
                    tf.setForeground(Color.red);
                    return false;
                }
            } catch (NumberFormatException e) {
                tf.setForeground(Color.red);
                return false;
            }
        } else {
            //pass not enabled text
            tf.setForeground(Color.black);
            return true;
        }
    }

    private boolean checkPositiveFloat(JComponent input) {
        JTextField tf = (JTextField) input;
        if (input.isEnabled() && input.isVisible()) {
            try {
                float num = Float.parseFloat(tf.getText());
                if (num > 0) {
                    tf.setForeground(Color.black);
                    return true;
                } else {
                    tf.setForeground(Color.red);
                    return false;
                }
            } catch (NumberFormatException e) {
                tf.setForeground(Color.red);
                return false;
            }
        } else {
            //pass not enabled text
            tf.setForeground(Color.black);
            return true;
        }
    }

    private boolean checkAnalysisPeriodTime() {
        int start_HH = Integer.parseInt((String) startHour.getSelectedItem());
        int start_MM = Integer.parseInt((String) startMin.getSelectedItem());
        int end_HH = Integer.parseInt((String) endHour.getSelectedItem());
        int end_MM = Integer.parseInt((String) endMin.getSelectedItem());
        if (start_HH < end_HH || (start_HH == end_HH && start_MM < end_MM)) {
            startHour.setForeground(Color.black);
            startMin.setForeground(Color.black);
            endHour.setForeground(Color.black);
            endMin.setForeground(Color.black);
            return true;
        } else {
            startHour.setForeground(Color.red);
            startMin.setForeground(Color.red);
            endHour.setForeground(Color.red);
            endMin.setForeground(Color.red);
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ERCheck;
    private javax.swing.JTextField ERText;
    private javax.swing.JCheckBox ETCheck;
    private javax.swing.JTextField ETText;
    private javax.swing.JCheckBox MLFFSCheck;
    private javax.swing.JTextField MLFFSText;
    private javax.swing.JComboBox MLTypeCB;
    private javax.swing.JCheckBox MLTypeCheck;
    private javax.swing.JCheckBox RVCheck;
    private javax.swing.JCheckBox RVMLCheck;
    private javax.swing.JTextField RVMLText;
    private javax.swing.JTextField RVText;
    private javax.swing.JCheckBox accDecLengthCheck;
    private javax.swing.JCheckBox accDecLengthMLCheck;
    private javax.swing.JTextField accDecLengthMLText;
    private javax.swing.JTextField accDecLengthText;
    private javax.swing.JButton addPeriodButton;
    private javax.swing.JButton addSegButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField capacityDropText;
    private javax.swing.JButton checkButton;
    private javax.swing.JButton delPeriodButton;
    private javax.swing.JButton delSegButton;
    private javax.swing.JComboBox endHour;
    private javax.swing.JComboBox endMin;
    private javax.swing.JCheckBox ffsKnownCheck;
    private javax.swing.JPanel fillGPJPanel;
    private javax.swing.JPanel fillMLJPanel;
    private javax.swing.JPanel generalJPanel;
    private javax.swing.JTextField gpOccText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jamDensityText;
    private javax.swing.JCheckBox laneWidthCheck;
    private javax.swing.JTextField laneWidthText;
    private javax.swing.JCheckBox lateralClearanceCheck;
    private javax.swing.JTextField lateralClearanceText;
    private javax.swing.JCheckBox mainlineFFSCheck;
    private javax.swing.JTextField mainlineFFSText;
    private javax.swing.JCheckBox manageLaneCheck;
    private javax.swing.JLabel mlOccLabel;
    private javax.swing.JTextField mlOccText;
    private javax.swing.JCheckBox numOfMLLanesCheck;
    private javax.swing.JTextField numOfMLLanesText;
    private javax.swing.JCheckBox numOfMLRampLanesCheck;
    private javax.swing.JTextField numOfMLRampLanesText;
    private javax.swing.JCheckBox numOfMainlineLanesCheck;
    private javax.swing.JTextField numOfMainlineLanesText;
    private javax.swing.JCheckBox numOfRampLanesCheck;
    private javax.swing.JTextField numOfRampLanesText;
    private javax.swing.JTextField numOfSegmentsText;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel optionsJPanel;
    private javax.swing.JCheckBox rampFFSCheck;
    private javax.swing.JTextField rampFFSText;
    private javax.swing.JCheckBox rampMLFFSCheck;
    private javax.swing.JTextField rampMLFFSText;
    private javax.swing.JCheckBox rampMeteringCheck;
    private javax.swing.JTextField rampMeteringText;
    private javax.swing.JTextField seedNameText;
    private javax.swing.JComboBox separationCB;
    private javax.swing.JCheckBox separationCheck;
    private javax.swing.JComboBox startHour;
    private javax.swing.JComboBox startMin;
    private javax.swing.JCheckBox terrainCheck;
    private javax.swing.JComboBox terrainComboBox;
    private javax.swing.JCheckBox truckCheck;
    private javax.swing.JCheckBox truckMLCheck;
    private javax.swing.JTextField truckMLText;
    private javax.swing.JTextField truckText;
    private javax.swing.JCheckBox useRampMeteringCheck;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    /**
     * Getter for Seed instance
     *
     * @return Seed instance
     */
    public Seed getSeed() {
        return seed;
    }

}
