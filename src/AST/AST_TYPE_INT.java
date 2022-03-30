package AST;

import TYPES.*;

public class AST_TYPE_INT extends AST_TYPE {

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_TYPE_INT(int line) {
    this.line = line;
    this.typeName = "int";
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.print("====================== type -> TYPE_INT \n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "TYPE_INT"));

    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("int"));
  }

  public TYPE SemantMe() {
    System.out.format("TYPE_INT" + "- semant me\n");
    return TYPE_INT.getInstance();
  }
}