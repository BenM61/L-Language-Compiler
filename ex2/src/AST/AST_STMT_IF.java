package AST;

public class AST_STMT_IF extends AST_STMT {
  public AST_EXP cond;
  public AST_STMT_LIST body;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_IF(AST_EXP cond, AST_STMT_LIST body) {
    this.cond = cond;
    this.body = body;
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
}