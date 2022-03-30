/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class IRcommand_virtual_call extends IRcommand {
  public TEMP dst;
  public TEMP classTemp;
  String funcName;
  public TEMP_LIST args;

  // dst = virtual_call classTemp, funcName, args
  // dst = classTemp.funcName(args)
  public IRcommand_virtual_call(TEMP dst, TEMP classTemp, String funcName, TEMP_LIST tempList) {
    this.dst = dst;
    this.classTemp = classTemp;
    this.funcName = funcName;
    this.args = tempList;   
    
  }

  public void MIPSme() {
    System.out.println("IRcommand_virtual_call" + " - MIPSme");
    MIPSGenerator.getInstance().virtual_call(dst, classTemp, offset, args);
  }
}