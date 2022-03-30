package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;

import java.util.Map;
import java.util.HashMap;

public class AST_STMT_IF extends AST_STMT {
  public AST_EXP cond;
  public AST_STMT_LIST body;
  public boolean infunc;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_IF(AST_EXP cond, AST_STMT_LIST body, int line) {
    this.cond = cond;
    this.body = body;
    this.line = line;

    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "STMT_IF"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, cond and right...) */
    /**************************************/
    if (cond != null)
      cond.PrintMe();
    if (body != null)
      body.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_IF"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (cond != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
    if (body != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
  }

  public TYPE SemantMe() {
    System.out.format("AST_STMT_IF" + "- semant me\n");

    if (cond.SemantMe() != TYPE_INT.getInstance()) {
      System.out.format(">> ERROR [%d:%d] condition inside IF is not integral\n", 2, 2);
      printError(this.line);
    }

    SYMBOL_TABLE.getInstance().beginScope("if");

    body.SemantMe();

    SYMBOL_TABLE.getInstance().endScope();

    infunc = SYMBOL_TABLE.getInstance().inFuncScope();

    return TYPE_INT.getInstance();
  }

  public TEMP IRme() {
    System.out.format("AST_STMT_IF" + "- IRme\n");

    /*******************************/
    /* [1] Allocate 2 fresh labels */
    /*******************************/

    String label_end = IRcommand.getFreshLabel("end");
    String label_start = IRcommand.getFreshLabel("start");

    /*********************************/
    /* [2] entry label for the while */
    /*********************************/
    IR.getInstance().Add_IRcommand(new IRcommand_Label(label_start));

    /********************/
    /* [3] cond.IRme(); */
    /********************/
    TEMP cond_temp = cond.IRme();

    /******************************************/
    /* [4] Jump conditionally to the loop end */
    /******************************************/
    IR.getInstance().Add_IRcommand(new IRcommand_Jump_beqz(cond_temp, label_end));

    /*******************/
    /* [5] body.IRme() */
    /*******************/

    if (infunc)
      ifScope(body);

    else
      body.IRme();
    // how do you store locals in if?

    /**********************/
    /* [7] Loop end label */
    /**********************/
    IR.getInstance().Add_IRcommand(new IRcommand_Label(label_end));

    /*******************/
    /* [8] return null */
    /*******************/
    return null;
  }

}
