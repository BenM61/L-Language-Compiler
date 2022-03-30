package AST;

public class AST_ARG extends AST_Node {
  public AST_TYPE t;
  public String id;
  public String val; // for IRme

  public AST_ARG(AST_TYPE t, String id) {
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    this.t = t;
    this.id = id;
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "ARG"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
    /**************************************/
    if (t != null)
      t.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ARG(%s)", id));

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
