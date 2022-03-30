package AST;

public class AST_STMT_NEWEXP extends AST_STMT {
  public AST_VAR var;
  public AST_NEWEXP exp;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_NEWEXP(AST_VAR var, AST_NEWEXP exp) {
    this.var = var;
    this.exp = exp;
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
    System.out.print(String.format("AST %s NODE\n", "STMT_NEWEXP "));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, var and right...) */
    /**************************************/
    if (var != null)
      var.PrintMe();
    if (exp != null)
      exp.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_NEWEXP"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (var != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    if (exp != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
  }
}