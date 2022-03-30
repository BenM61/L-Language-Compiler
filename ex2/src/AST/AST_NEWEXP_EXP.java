package AST;

public class AST_NEWEXP_EXP extends AST_NEWEXP {
  public AST_TYPE t;
  public AST_EXP e;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_NEWEXP_EXP(AST_TYPE t, AST_EXP e) {
    this.t = t;
    this.e = e;
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
    System.out.print(String.format("AST %s NODE\n", "NEWEXP_EXP"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, t and right...) */
    /**************************************/
    if (t != null)
      t.PrintMe();
    if (e != null)
      e.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("NEWEXP_EXP"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (t != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);
    if (e != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e.SerialNumber);
  }
}