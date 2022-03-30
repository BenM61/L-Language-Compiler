package AST;

public class AST_CLASSDEC_SIMPLE extends AST_CLASSDEC {
  public String id;
  public AST_CFEILD_LIST l;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_CLASSDEC_SIMPLE(String id, AST_CFEILD_LIST l) {
    this.l = l;
    this.id = id;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (l != null)
      System.out.print("====================== classDec -> CLASS ID: LBRACE cFieldList RBRACE\n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "CLASSDEC_SIMPLE"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
    /**************************************/
    if (l != null)
      l.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CLASSDEC_SIMPLE(%s)", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (l != null) {
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, l.SerialNumber);
    }
  }
}