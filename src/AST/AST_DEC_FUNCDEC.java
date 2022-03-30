package AST;

import TEMP.TEMP;
import TYPES.*;

public class AST_DEC_FUNCDEC extends AST_DEC {
  public AST_FUNCDEC func;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_DEC_FUNCDEC(AST_FUNCDEC func) {
    this.func = func;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (func != null)
      System.out.print("====================== dec -> funcDec\n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "DEC_FUNCDEC"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, func and right...) */
    /**************************************/
    if (func != null)
      func.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC_FUNCDEC"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (func != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, func.SerialNumber);

  }

  public TYPE SemantMe() {
    System.out.println("DEC_FUNCDEC" + "- semantme");
    func.SemantMe();
    /*********************************************************/
    /* [1] Return value is irrelevant for the program itself */
    /*********************************************************/
    return null;
  }

  public TEMP IRme() {
    System.out.println("DEC_FUNCDEC" + "- IRme");

    func.IRme();

    return null;
  }
}
