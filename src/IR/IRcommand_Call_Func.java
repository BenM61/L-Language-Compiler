/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class IRcommand_Call_Func extends IRcommand {
  public TEMP t;
  String startLabel;
  public TEMP_LIST tempList;

  // t = call f(t1, t2, ...) (f's location is startLabel, templist = [1t1,t2,...])
  public IRcommand_Call_Func(TEMP t, String startLabel, TEMP_LIST tempList) {
	    this.t = t;
	    this.startLabel = startLabel;
	    this.tempList = tempList;
	  }

	  public void MIPSme() {
	    System.out.println("IRcommand_Call_Func" + " - MIPSme");

	    TEMP_LIST reversed = null;
	    if (tempList != null) {
	      reversed = tempList.reverseList();
	    }

	    MIPSGenerator.getInstance().call_func(t, startLabel, reversed);
	  }
}
