package AST;

public class AST_FUNCDEC_UTIL2 extends AST_Node {
  /****************/
  /* DATA MEMBERS */
  /****************/
  public AST_TYPE head;
  public String id;
  public AST_FUNCDEC_UTIL2 tail;

  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_FUNCDEC_UTIL2(AST_TYPE head, String id, AST_FUNCDEC_UTIL2 tail) {
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (tail != null)
      System.out.print("====================== funcdec util2 -> stmt stmts\n");
    if (tail == null)
      System.out.print("====================== funcdec util2 -> stmt      \n");

    /*******************************/
    /* COPY INPUT DATA NENBERS ... */
    /*******************************/
    this.head = head;
    this.id = id;
    this.tail = tail;
  }

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "FUNCDEC_UTIL2"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, head and right...) */
    /**************************************/
    if (head != null)
      head.PrintMe();
    if (tail != null)
      tail.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNCDEC_UTIL2(%s)\n param_type, param_name", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (head != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
    if (tail != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
  }
}
