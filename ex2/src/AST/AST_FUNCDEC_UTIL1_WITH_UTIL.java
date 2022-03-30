package AST;

public class AST_FUNCDEC_UTIL1_WITH_UTIL extends AST_FUNCDEC_UTIL1 {
  public AST_TYPE t;
  public String id;
  public AST_FUNCDEC_UTIL2 u2;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_FUNCDEC_UTIL1_WITH_UTIL(AST_TYPE t, String id, AST_FUNCDEC_UTIL2 u2) {
    this.t = t;
    this.id = id;
    this.u2 = u2;
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
    System.out.print(String.format("AST %s NODE\n", "FUNCDEC_UTIL1_WITH_UTIL"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, t and right...) */
    /**************************************/
    if (t != null)
      t.PrintMe();
    if (u2 != null)
      u2.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
        String.format("FUNCDEC_UTIL1_WITH_UTIL(%s)\n param_type, param_name", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (t != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);
    if (u2 != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, u2.SerialNumber);
  }
}