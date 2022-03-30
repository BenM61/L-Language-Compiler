package AST;

public class AST_FUNCDEC_UTIL1_SIMPLE extends AST_FUNCDEC_UTIL1 {
  public AST_TYPE t;
  public String id;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_FUNCDEC_UTIL1_SIMPLE(AST_TYPE t, String id) {
    this.t = t;
    this.id = id;
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
    System.out.print(String.format("AST %s NODE\n", "FUNCDEC_UTIL1_SIMPLE"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, t and right...) */
    /**************************************/
    if (t != null)
      t.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
        String.format("FUNCDEC_UTIL1_SIMPLE(%s)\n return type, func_name", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (t != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);
  }
}