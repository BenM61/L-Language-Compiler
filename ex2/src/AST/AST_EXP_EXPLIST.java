package AST;

public class AST_EXP_EXPLIST extends AST_EXP {
  public String id;
  public AST_EXPLIST list;

  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_EXP_EXPLIST(String id, AST_EXPLIST list) {
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.print("====================== exp -> -\n");

    /*******************************/
    /* COPY INPUT DATA NENBERS ... */
    /*******************************/
    this.id = id;
    this.list = list;
  }

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "EXP_EXPLIST"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, list and right...) */
    /**************************************/
    if (list != null)
      list.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_EXPLIST(%s)", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (list != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

  }

}
