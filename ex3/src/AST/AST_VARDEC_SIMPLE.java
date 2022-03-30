package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VARDEC_SIMPLE extends AST_VARDEC {

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_VARDEC_SIMPLE(AST_TYPE type, String id, int line) {
    this.type = type;
    this.id = id;
    this.line = line;

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

  public TYPE SemantMe() {
    System.out.println("VARDEC SIMPLE - semant me");

    TYPE t1 = findType(type.typeName);
    if (t1 == null || t1 instanceof TYPE_VOID || t1 instanceof TYPE_NIL) {
      System.out.format(">> ERROR [%d] type " + type.typeName + " does not exist", line);
      printError(line);
    }

    TYPE t2 = SYMBOL_TABLE.getInstance().findInCurrScope(id);
    if (t2 != null) {
      System.out.format(">> [%d] ERROR variable " + id + " already exist exist", line);
      printError(line);
    }

    /***************************************************/
    /* [3] Enter the Type to the Symbol Table */
    /***************************************************/
    isOverride();

    SYMBOL_TABLE.getInstance().enter(id, t1);
    return t1;
  }
}