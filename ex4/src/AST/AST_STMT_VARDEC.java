package AST;

import TYPES.*;
import TEMP.*;

public class AST_STMT_VARDEC extends AST_STMT {
  public AST_VARDEC v;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_VARDEC(AST_VARDEC v) {
    this.v = v;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    System.out.print("====================== stmt -> varDec\n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "STMT_VARDEC"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, v and right...) */
    /**************************************/
    if (v != null)
      v.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_VARDEC"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (v != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, v.SerialNumber);

  }

  public TYPE SemantMe() {
    System.out.println("STMT VARDEC - semant me");
    v.SemantMe();
    return null;
  }

  public TEMP IRme() {
    System.out.println("STMT VARDEC - ir me");
    v.IRme();
    return null;
  }
}