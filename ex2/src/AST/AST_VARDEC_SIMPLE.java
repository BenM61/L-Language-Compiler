package AST;

public class AST_VARDEC_SIMPLE extends AST_VARDEC {
  public AST_TYPE type;
  public String id;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_VARDEC_SIMPLE(AST_TYPE type, String id) {
    this.type = type;
    this.id = id;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (type != null)
      System.out.print("====================== varDec -> type ID SEMICOLON \n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "VARDEC_SIMPLE"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, type and right...) */
    /**************************************/
    if (type != null)
      type.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VARDEC_SIMPLE(%s)", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (type != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
  }
}