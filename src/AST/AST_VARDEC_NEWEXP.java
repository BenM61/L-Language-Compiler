package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_VARDEC_NEWEXP extends AST_VARDEC {
  public AST_NEWEXP exp;
  public TYPE t; // for IRme

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/

  public AST_VARDEC_NEWEXP(AST_TYPE type, String id, AST_NEWEXP exp, int line) {
    this.type = type;
    this.id = id;
    this.exp = exp;
    this.line = line;

    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (type != null && exp != null)
      System.out.print("====================== varDec -> type ID ASSIGN newExp SEMICOLON \n");

  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "VARDEC_NEWEXP"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, type and right...) */
    /**************************************/
    if (type != null)
      type.PrintMe();
    if (exp != null)
      exp.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VARDEC_NEWEXP(%s)", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (type != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
    if (exp != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
  }

  public TYPE SemantMe() {
    System.out.println("VARDEC NEWEXP - semant me");

    TYPE t1 = type.SemantMe();

    TYPE t2 = exp.SemantMe();

    /******************************************************/
    /* [0] check if the two types are the same */
    /******************************************************/

    if (t1 == null || t2 == null || t1 instanceof TYPE_NIL || t1 instanceof TYPE_VOID) {
      System.out.format(">> ERROR [%d] non existing type %s %s (vardec_newexp)\n", line, t1, t2);
      printError(this.line);
    }

    if (!(type_equals(t1, t2))) {
      System.out.format(">> ERROR [%d] type mismatch for type id = newExp;  --- %s %s  (vardec_newexp)\n", line,
          t1.name, t2.name);
      printError(this.line);
    }

    /******************************************************/
    /* [1] check if variable is already declared in scope */
    /******************************************************/
    String scope = SYMBOL_TABLE.getInstance().getScope();

    if (scope.equals("class")) {
      System.out.format(">> ERROR [%d] cant declare non primitive variable\n", line);
      printError(this.line);
    }

    TYPE res = SYMBOL_TABLE.getInstance().findInCurrScope(id);
    if (res != null) {
      System.out.format(">> ERROR [%d] %s is already declared.\n", line, id);
      printError(this.line);
    } else {
      isOverride();
      SYMBOL_TABLE.getInstance().enter(id, t1);
    }

    /******************************************************/
    /* [2] return type doesn't matter */
    /******************************************************/
    t = t1;

    // for irme
    this.scope = scope;

    return t1;
  }

  public TEMP IRme() {
    System.out.println("VARDEC NEWEXP - IRme");

    TEMP t = exp.IRme();

    if (scope.equals("global")) {
      // can only use "new" on local context or inside class dec
      printError(666);
    } else {
      // class case?

      // local case
      IRcommand command = new IRcommand_Store_Local(id, t);

      command.offset = GetOffset(id);

      IR.getInstance().Add_IRcommand(command);
    }

    return null;
  }

}