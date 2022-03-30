package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
public class AST_STMT_WHILE extends AST_STMT {
  public AST_EXP cond;
  public AST_STMT_LIST body;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_WHILE(AST_EXP cond, AST_STMT_LIST body, int line) {
    this.cond = cond;
    this.body = body;
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
    System.out.print(String.format("AST %s NODE\n", "STMT_WHILE"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, cond and right...) */
    /**************************************/
    if (cond != null)
      cond.PrintMe();
    if (body != null)
      body.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_WHILE"));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (cond != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
    if (body != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
  }
  public TYPE SemantMe()
	{

		if (cond.SemantMe() != TYPE_INT.getInstance())
		{
			System.out.format(">> ERROR [%d:%d] condition inside WHILE is not integral\n",2,2);
      printError(this.line);
		}
		
		SYMBOL_TABLE.getInstance().beginScope("while");

		body.SemantMe();

		SYMBOL_TABLE.getInstance().endScope();

		return TYPE_INT.getInstance();	
	}	
  
}
