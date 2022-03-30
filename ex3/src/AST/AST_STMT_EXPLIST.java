package AST;

import TYPES.*;

import SYMBOL_TABLE.*;

public class AST_STMT_EXPLIST extends AST_STMT {
  public String id;
  public AST_EXPLIST list;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_EXPLIST(String id, AST_EXPLIST list, int line) {
    this.id = id;
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
    System.out.print(String.format("AST %s NODE\n", "STMT_EXPLIST"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, list and right...) */
    /**************************************/
    if (list != null)
      list.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_EXPLIST(%s)", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (list != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

  }
  public TYPE SemantMe() 
  {
		System.out.println("STMT EXPLIST - semant me");
    return funcSig(id, list,this.line);
	}
}
