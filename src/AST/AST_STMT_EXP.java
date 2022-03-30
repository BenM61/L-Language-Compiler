package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_EXP extends AST_STMT {
  public AST_EXP e;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_EXP(AST_EXP e, int line) {
    this.e = e;
    this.line = line;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.print("====================== stmt -> RETURN exp SEMICOLON	\n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "STMT_EXP"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, e and right...) */
    /**************************************/
    if (e != null)
      e.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_EXP"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (e != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e.SerialNumber);

  }

  public TYPE SemantMe() {
    System.out.println("STMT EXP - semant me");
    TYPE ty = e.SemantMe();
    if (ty == null) {
      return null;
    }
    String returnName = ty.name;

    int a = SYMBOL_TABLE.getInstance().findFunc(returnName);
    if (a == 0) {
      System.out.println("=======Error in return statement!");
      printError(line);
    }
    return ty;
  }

  public TEMP IRme() {
    TEMP retVal = e.IRme();
    IR.getInstance().Add_IRcommand(new IRcommand_Return(retVal));
    return null;
  }
}