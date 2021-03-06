package AST;

import TYPES.*;
import TEMP.*;

public class AST_DEC_ATD extends AST_DEC {
  public AST_ATD array;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_DEC_ATD(AST_ATD array) {
    this.array = array;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (array != null)
      System.out.print("====================== dec -> ATD\n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "DEC_ATD"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
    /**************************************/
    if (array != null)
      array.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC_ATD"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (array != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, array.SerialNumber);
  }

  public TYPE SemantMe() {

    System.out.println("DEC_ATD" + "- semantme");

    array.SemantMe();
    return null;
  }

  public TEMP IRme() {
    System.out.println("DEC_ATD" + "- IRme");

    array.IRme();
    return null;
  }

}