package AST;

import TYPES.*;

public class AST_STMT_NEWEXP extends AST_STMT {
  public AST_VAR var;
  public AST_NEWEXP exp;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_NEWEXP(AST_VAR var, AST_NEWEXP exp, int line) {
    this.var = var;
    this.exp = exp;
    this.line = line;

    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();
    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.print("====================== stmt -> var:v ASSIGN newExp:ne SEMICOLON \n");

  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "STMT_NEWEXP "));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, var and right...) */
    /**************************************/
    if (var != null)
      var.PrintMe();
    if (exp != null)
      exp.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_NEWEXP"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (var != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    if (exp != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
  }

  public TYPE SemantMe() {
    System.out.println("STMT NEWEXP - semant me");

    TYPE t1 = null;
    TYPE t2 = null;

    if (var == null || exp == null) {
      printError(this.line);
    }

    t1 = var.SemantMe();
    t2 = exp.SemantMe();

    if (t1 == null || t2 == null) {
      System.out.format(">> ERROR [%d] non existing type %s %s (stmt_newexp)\n", line, t1, t2);
      printError(line);
    }

    if (!(type_equals(t1, t2))) {
      System.out.format(">> ERROR [%d] type mismatch for type id = newExp; --- %s %s (stmt_newexp)\n", line, t1.name,
          t2.name);
      printError(this.line);
    }

    System.out.format("[%d] type match %s for type id = newExp;\n", line, t1.name);
    return null;
  }
}