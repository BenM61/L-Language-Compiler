package AST;
import TYPES.*;
import TEMP.*;
public class AST_DEC_CLASSDEC extends AST_DEC {
  public AST_CLASSDEC cd;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_DEC_CLASSDEC(AST_CLASSDEC cd) {
    this.cd = cd;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (cd != null)
      System.out.print("====================== dec -> classDec\n");

  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "DEC_CLASSDEC"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
    /**************************************/
    if (cd != null)
      cd.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC_CLASSDEC"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (cd != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cd.SerialNumber);

  }
  
	public TYPE SemantMe() {
    System.out.println("DEC CLASSDEC - semant me");
    return cd.SemantMe();
	}

	public TEMP IRme() {
    System.out.println("DEC_CLASSDEC" + "- IRme");
    cd.IRme();
    return null;
}
}
