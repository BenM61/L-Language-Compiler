package AST;

public class AST_EXP_ID extends AST_EXP {
  public String id;

  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_EXP_ID(String id) {
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.print("====================== exp -> id\n");

    /*******************************/
    /* COPY INPUT DATA NENBERS ... */
    /*******************************/
    this.id = id;
  }

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "EXP_ID"));

    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_ID(%s)", id));

  }
}
