package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ID extends AST_STMT {
  public String id;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_ID(String id, int line) {
    this.id = id;
    this.line = line;
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
    System.out.print(String.format("AST %s NODE\n", "STMT_ID"));

    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_ID(%s)", id));
  }
  public TYPE SemantMe()
	{
    System.out.println("STMT ID - semant me");
    return funcSig(id,null,this.line);
  }
}