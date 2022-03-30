package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.*;
import IR.*;

import java.util.Map;
import java.util.HashMap;

public class AST_FUNCDEC extends AST_Node {
  public AST_TYPE returnType;
  public String id;
  public AST_ARG_LIST arglist;
  public AST_STMT_LIST list;
  public String className; // nedded for IRme
  public TYPE_FUNCTION func; // needed for IRme

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_FUNCDEC(AST_TYPE returnType, String id, AST_ARG_LIST arglist, AST_STMT_LIST list, int line) {
    this.returnType = returnType;
    this.id = id;
    this.arglist = arglist;
    this.list = list;
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
    System.out.print(String.format("AST %s NODE\n", "AST_FUNCDEC"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, t and right...) */
    /**************************************/
    if (returnType != null) {
      returnType.PrintMe();
    }
    if (arglist != null) {
      arglist.PrintMe();
    }
    if (list != null) {
      list.PrintMe();
    }
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
        String.format("FUNCDEC(%s)\n return type, func_name", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (returnType != null) {
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, returnType.SerialNumber);
    }
    if (arglist != null) {
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, arglist.SerialNumber);
    }
    if (list != null) {
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
    }
  }

  public TYPE SemantMe() {

    System.out.println("FUNCDEC- semantme(" + id + ")");

    TYPE returnTypeType = null;
    TYPE_LIST argListTypes = null;
    TYPE t;

    /*******************/
    /* [0] return type */
    /*******************/
    returnTypeType = findType(returnType.typeName);

    if (returnTypeType == null || returnTypeType instanceof TYPE_NIL) {
      System.out.format(">> ERROR [%d] non existing return type %s\n", line, returnType.typeName);
      printError(line);
    }

    /***************************/
    /* [2] Semant Input Params */
    /***************************/
    for (AST_ARG_LIST it = arglist; it != null; it = it.tail) {
      t = findType(it.head.t.typeName);

      if (t == null) {
        System.out.format(">> ERROR [%d] non existing type %s\n", line, it.head.t.typeName);
        printError(line);
      }

      if (t instanceof TYPE_NIL || t instanceof TYPE_VOID) {
        System.out.format(">> ERROR [%d] cant decalre function with nil/void");
        printError(line);
      }
      for (AST_ARG_LIST it2 = arglist; it2 != null && it2 != it; it2 = it2.tail) {
        if (it.head.id.equals(it2.head.id)) {
          System.out.format(">> ERROR  2 args with the same name");
          printError(line);
        }
      }

      argListTypes = new TYPE_LIST(t, argListTypes);
    }

    // reverse list
    if (argListTypes != null) {
      argListTypes = argListTypes.reverseList();
    }

    /***************************************************/
    /* [5] Enter the Function Type to the Symbol Table */
    /***************************************************/

    // check you dont overwrite class func
    boolean flag = true;
    className = SYMBOL_TABLE.getInstance().inClassScope();
    if (className != null) {
      String father = SYMBOL_TABLE.getInstance().findExtendsClass(className);
      if (father != null) {
        TYPE_CLASS fatherClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(father);
        while (fatherClass != null && flag) {
          AST_TYPE_NAME_LIST funcs = fatherClass.functions;
          for (AST_TYPE_NAME_LIST it = funcs; it != null; it = it.tail) {
            TYPE_FUNCTION currF = (TYPE_FUNCTION) it.head.type;
            if (currF.name.equals(id)) {
              if (!(currF.returnType.name.equals(returnTypeType.name))) {
                System.out.println(">> ERROR [" + line + "] cant overwrite the function!");
                printError(line);
              }
              TYPE_LIST params = currF.params;
              for (TYPE_LIST it2 = argListTypes; it2 != null; it2 = it2.tail) {
                if (params == null || params.head == null
                    || !(it2.head.name.equals(params.head.name))) {
                  System.out.println(">> ERROR [" + line + "] cant overwrite the function!");
                  printError(line);
                }
                params = params.tail;
              }
              if (params != null) {
                System.out.println(">> ERROR [" + line + "] cant overwrite the function!");
                printError(line);
              }
              flag = false;
              break;
            }
          }
          fatherClass = fatherClass.father;
        }
      }
    }

    this.func = new TYPE_FUNCTION(returnTypeType, id, argListTypes);
    SYMBOL_TABLE.getInstance().enter(id, func);

    /****************************/
    /* [1] Begin Function Scope */
    /****************************/
    SYMBOL_TABLE.getInstance().beginScope("func-" + id + "-" + returnTypeType.name);

    /********************** */
    for (AST_ARG_LIST it = arglist; it != null; it = it.tail) {
      t = findType(it.head.t.typeName);
      SYMBOL_TABLE.getInstance().enter(it.head.id, t);
    }
    /************************* */

    /*******************/
    /* [3] Semant Body */
    /*******************/
    list.SemantMe();

    /*****************/
    /* [4] End Scope */
    /*****************/
    SYMBOL_TABLE.getInstance().endScope();

    return func;
  }

  public TEMP IRme() {
    System.out.println("FUNCDEC" + "- IRme(" + id + ")");

    if (id.equals("main")) {
      this.id = "user_main";
    }

    // signature IRme ...

    //////////////////////// calculate the var
    //////////////////////// list//////////////////////////////////////////

    ///////////// arguments/////////////////////////
    int argCnt = 0; // number of arguments
    if (className != null)
      argCnt += 1; // this

    for (AST_ARG_LIST it = arglist; it != null; it = it.tail) {
      String off = String.valueOf(8 + 4 * argCnt);
      offsets.put(it.head.id, off);
      argCnt += 1;
    }

    /////////// local variables///////////////////////
    int varCnt = 0; // number of local variables
    for (AST_STMT_LIST it = list; it != null; it = it.tail) {
      if (it.head instanceof AST_STMT_VARDEC) {
        varCnt += 1;
        continue;
      }
      if (it.head instanceof AST_STMT_IF || it.head instanceof AST_STMT_WHILE) {
        varCnt += localsInIfOrWhile(it.head);
      }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // prologue
    String labelStart = null;
    if (id.equals("user_main")) {
      labelStart = id;
    } else {
      if (className != null)
        labelStart = className + "_" + id;
      else {
        labelStart = IRcommand.getFreshLabel("start_" + id);
        offsets.put(id, labelStart);
      }
    }

    this.func.startLabel = labelStart;

    IR.getInstance().Add_IRcommand(new IRcommand_Label(labelStart));
    IR.getInstance().Add_IRcommand(new IRcommand_Prologue(varCnt));

    // body
    varCnt = 0;
    for (AST_STMT_LIST it = list; it != null; it = it.tail) {
      if (it.head instanceof AST_STMT_VARDEC) {
        varCnt += 1;
        AST_STMT_VARDEC a = (AST_STMT_VARDEC) (it.head);
        AST_VARDEC b = (AST_VARDEC) (a.v);
        String off = String.valueOf(varCnt * (-4) + -40);
        offsets.put(b.id, off);
      }
      if (it.head instanceof AST_STMT_IF || it.head instanceof AST_STMT_WHILE) {
        varsInFunc = varCnt;
        varCnt += localsInIfOrWhile(it.head);
      }
      it.head.IRme();
    }

    // epilogue
    IR.getInstance().Add_IRcommand(new IRcommand_Epilogue());

    System.out.println(offsets);
    return null;
  }
}
