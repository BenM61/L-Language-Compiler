package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_FUNCDEC extends AST_Node {
  public AST_TYPE returnType;
  public String id;
  public AST_ARG_LIST arglist;
  public AST_STMT_LIST list;

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
    } else {
      System.out.format("[%d] return type is %s\n", line, returnTypeType.name);
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
    System.out.format("[%d] declared: ", line);
    // TYPE_CLASS.printFunc(new TYPE_FUNCTION(returnTypeType, id, argListTypes));

    // check you dont overwrite class func
    boolean flag = true;
    String className = SYMBOL_TABLE.getInstance().inClassScope();
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

    SYMBOL_TABLE.getInstance().enter(id, new TYPE_FUNCTION(returnTypeType, id, argListTypes));

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

    /*********************************************************/
    /* [6] Return value is irrelevant for func declarations */
    /*********************************************************/
    return new TYPE_FUNCTION(returnTypeType, id, argListTypes);
  }
}