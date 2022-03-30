package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_ID extends AST_STMT {
  public String id;
  public TYPE_FUNCTION func; // for IRme

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

    System.out.print("====================== stmt -> ID();\n");
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

  public TYPE SemantMe() {
    System.out.println("STMT_ID - semant me");
    TYPE t = funcSig(id, null, this.line);

    this.func = (TYPE_FUNCTION) (SYMBOL_TABLE.getInstance().find(id));

    return t;
  }

  public TEMP IRme() {
    System.out.println("STMT_ID - IRme");

    TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();

    String startLabel = null;
    if (id.equals("PrintInt")) {
      startLabel = "PrintInt";
    } else if (id.equals("PrintString")) {
      startLabel = "PrintString";
    } else {
      startLabel = this.func.startLabel;
    }

    IR.getInstance().Add_IRcommand(new IRcommand_Call_Func(t, startLabel, null));

    return t;
  }
}