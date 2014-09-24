/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DSS.DataStruct;

import atdm.DataStruct.ATDMScenario;

/**
 *
 * @author jltrask
 */
public class ATMUpdater {
    private final ATDMScenario atmScenario;
    private final PeriodATM[] periodATM;
    
    public ATMUpdater(ATDMScenario atmScenario, PeriodATM[] periodATM) {
        this.atmScenario = atmScenario;
        this.periodATM = periodATM;
    }
   
    public void update(int startPeriod) {
        
    }
    
    public PeriodATM[] getAllPeriodATM() {
        return periodATM;
    }
}
