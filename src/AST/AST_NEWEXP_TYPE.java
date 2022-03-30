package AST;

import TYPES.*;
import IR.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_NEWEXP_TYPE extends AST_NEWEXP {
  public AST_TYPE t;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_NEWEXP_TYPE(AST_TYPE t, int line) {
    this.t = t;
    this.line = line;
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();
    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    System.out.print("====================== newExp -> NEW type:t \n");
  }

  /****************** outside CONSTRUCTOR code *******************/

  /*************************************************/
  /* The printing message for a XXX node */
  /*************************************************/
  public void PrintMe() {

    /*************************************/
    /* AST NODE TYPE- change XXX with this class name */
    /*************************************/
    System.out.print(String.format("AST %s NODE\n", "NEWEXP_TYPE"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, t and right...) */
    /**************************************/
    if (t != null)
      t.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("NEWEXP_TYPE"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (t != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);

  }

  public TYPE SemantMe() {
    System.out.println("NEWEXP TYPE - sament me");

    if (t.typeName.equals("void") || t.typeName.equals("nil")) {
      System.out.println(">> ERROR [" + line + "] cant declare type void/nil");
      printError(line);
    }
    // should return type and not ast type!!
    if (t.typeName.equals("int")) {
      return TYPE_INT.getInstance();
    }
    if (t.typeName.equals("string")) {
      return TYPE_STRING.getInstance();
    }

    TYPE cl = SYMBOL_TABLE.getInstance().findClass(t.typeName);
    if (cl == null) {
      System.out.println(">> ERROR [" + line + "] cant declate " + t.typeName + " type");
      printError(line);
    }
    return cl;
  }

  public TEMP IRme() {
    System.out.println("NEWEXP TYPE- IRme");

    TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
    IR.getInstance().Add_IRcommand(new IRcommand_New_Class_Object(t1, t.typeName));
    return t1;
  }
}