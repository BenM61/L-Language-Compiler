package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

public class AST_STMT_NEWEXP extends AST_STMT {
  public AST_VAR var;
  public AST_NEWEXP exp;
  public TYPE scope; // for irme
  public String inclass; // for irme

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
    System.out.println("STMT_NEWEXP - semant me");

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

    if (var instanceof AST_VAR_SIMPLE && SYMBOL_TABLE.getInstance().inFuncScope()) {
      scope = SYMBOL_TABLE.getInstance().findInFuncScope(((AST_VAR_SIMPLE) var).name);
    }
    inclass = SYMBOL_TABLE.getInstance().inClassScope();

    return null;
  }

  public TEMP IRme() {
    System.out.println("STMT_NEWEXP - IRme");

    TEMP value = exp.IRme();

    if (var instanceof AST_VAR_SIMPLE) {
      String name = ((AST_VAR_SIMPLE) var).name;
      // global
      if (((AST_VAR_SIMPLE) var).inGlobal == 1)
        IR.getInstance().Add_IRcommand(new IRcommand_Store_Global(value, name));
      // local
      else {
        if (scope != null) // inside func scope and local of func
        {
          String varName = ((AST_VAR_SIMPLE) var).name;
          IRcommand cRcommand = new IRcommand_Store_Local(varName, value);
          IR.getInstance().Add_IRcommand(cRcommand);
          cRcommand.offset = GetOffset(varName);
        } else if (inclass != null) { // can be field in func
          String varName = inclass + "_" + ((AST_VAR_SIMPLE) var).name;
          IRcommand c = new IRcommand_store_field(inclass, varName, value);
          c.offset = GetOffset(varName);
          IR.getInstance().Add_IRcommand(c);

          // String varName = inclass + "&" + ((AST_VAR_SIMPLE) var).name;
          // using the store global to store inside a label even tho its local!
          // IR.getInstance().Add_IRcommand(new IRcommand_Store_Global(value, varName));
        } else {
          System.out.println(":((((");
        }
      }

    } else if (var instanceof AST_VAR_FIELD) {
      TEMP t1 = ((AST_VAR_FIELD) var).var.IRme();
      String f_name = ((AST_VAR_FIELD) var).fieldName;
      String c = ((AST_VAR_FIELD) var).classN;
      IRcommand r = new IRcommand_field_set(t1, f_name, value);
      r.offset = GetOffset(c + "_" + f_name);
      IR.getInstance().Add_IRcommand(r);

    } else { // var instanceof AST_VAR_SUBSCRIPT [Working]

      AST_VAR_SUBSCRIPT subVar = (AST_VAR_SUBSCRIPT) var;
      TEMP array = subVar.var.IRme();
      TEMP index = subVar.subscript.IRme();
      IR.getInstance().Add_IRcommand(new IRcommand_array_set(array, index, value));
    }

    return null;
  }
}