package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;

public class AST_STMT_RETURN extends AST_STMT {

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_RETURN(int line) {
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();
    this.line = line;

    System.out.print("====================== stmt -> return;\n");

  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "STMT_RETURN"));

    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_RETURN"));
  }

  public TYPE SemantMe() {
    System.out.println("STMT RETURN - semant me");
    int a = SYMBOL_TABLE.getInstance().findFunc("void");
    if (a == 0) {
      System.out.println("=======Error in return statement!");
      printError(line);
    }
    return TYPE_VOID.getInstance();
  }

  public TEMP IRme() {
    IR.getInstance().Add_IRcommand(new IRcommand_Return(null));
    return null;
  }
}