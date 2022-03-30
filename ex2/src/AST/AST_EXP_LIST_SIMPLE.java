package AST;

public class AST_EXP_LIST_SIMPLE extends AST_EXPLIST {
  public AST_EXP exp;

  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_EXP_LIST_SIMPLE(AST_EXP exp) {
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
    System.out.print(String.format("AST %s NODE\n", "EXP_LIST_SIMPLE"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, exp and right...) */
    /**************************************/
    if (exp != null)
      exp.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_LIST_SIMPLE"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (exp != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);

  }
}
