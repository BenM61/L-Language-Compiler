package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

public class IRcommand_field_set extends IRcommand {

  public TEMP dst;
  String varn;
  public TEMP val;

  // t1.varName = val;
  // 
  public IRcommand_field_set(TEMP t1, String varName, TEMP val) {
    this.dst = t1;
    this.val = val;
    this.varn = varName;
  }

  public void MIPSme() {
    System.out.println("IRcommand_field_set" + "- MIPSme");
    MIPSGenerator.getInstance().field_set(dst,offset,val);

  }

}