package AST;

public class AST_EXP_LIST extends AST_EXPLIST {
  public AST_EXP exp;
  public AST_COMMAEXPLIST list;

  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_EXP_LIST(AST_EXP exp, AST_COMMAEXPLIST list) {
    this.exp = exp;
    this.list = list;
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
    System.out.print(String.format("AST %s NODE\n", "EXP_LIST"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, exp and right...) */
    /**************************************/
    if (exp != null)
      exp.PrintMe();
    if (list != null)
      list.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_LIST"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (exp != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    if (list != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
  }
}
