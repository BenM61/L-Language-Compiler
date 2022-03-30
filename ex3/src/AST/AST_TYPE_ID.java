package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_TYPE_ID extends AST_TYPE {

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_TYPE_ID(String id, int line) {
    this.typeName = id;
    this.line = line;

    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.print("====================== type -> ID \n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "TYPE_ID"));

    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", typeName));

  }

  public TYPE SemantMe() {
    System.out.println("TYPE ID - semant me");
    TYPE res = findType(typeName);
    if (res == null) {
      System.out.format(">> ERROR(%d) non existing type %s (type_id)\n", line, res);
      printError(this.line);
    }

    // if this happens its a bug in the compiler, not in the input
    if (!res.name.equals(typeName)) {
      System.out.format(">> ERROR [%d]- type name isn't declared correctly! %s %s", line, res.name, typeName);
      printError(this.line);
    }

    System.out.format(">> [%d] recognized type %s\n", line, res.name);
    return res;
  }

}
