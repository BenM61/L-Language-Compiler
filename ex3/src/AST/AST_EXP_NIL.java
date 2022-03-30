package AST;
import TYPES.*;

public class AST_EXP_NIL extends AST_EXP {
  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_EXP_NIL() {
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
    System.out.print(String.format("AST %s NODE\n", "EXP_NIL"));

    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_NIL"));

  }
  public TYPE SemantMe()
	  {
      System.out.println("EXP NIL - semant me");
      return TYPE_NIL.getInstance();
    }
}
