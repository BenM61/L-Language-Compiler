package AST;

import IR.IR;
import IR.IRcommandConstInt;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;
import TYPES.*;

public class AST_EXP_MINUS_INT extends AST_EXP {
  public int value;

  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_EXP_MINUS_INT(int value) {
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /*******************************/
    /* COPY INPUT DATA NENBERS ... */
    /*******************************/
    this.value = -value;

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.format("====================== exp -> -INT(%d)\n", value);

  }

  /************************************************/
  /* The printing message for an INT EXP AST node */
  /************************************************/
  public void PrintMe() {
    /*******************************/
    /* AST NODE TYPE = AST INT EXP */
    /*******************************/
    System.out.format("AST NODE INT( %d )\n", value);

    /*********************************/
    /* Print to AST GRAPHIZ DOT file */
    /*********************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%d", value));
  }

  public TYPE SemantMe() {
    System.out.println("EXP MINUS INT - semant me");
    return TYPE_INT.getInstance();
  }

  public TEMP IRme() {
    System.out.println("EXP MINUS INT - IRme");

    TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
    IR.getInstance().Add_IRcommand(new IRcommandConstInt(t, value));
    return t;
  }
}
