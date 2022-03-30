package AST;
import TYPES.*;

public class AST_STMT_VARDOT_EXPLIST extends AST_STMT {
  public AST_VAR var;
  public String id;
  public AST_EXPLIST list;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_VARDOT_EXPLIST(AST_VAR var, String id, AST_EXPLIST list) {
    this.var = var;
    this.id = id;
    this.list = list;
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
    System.out.print(String.format("AST %s NODE\n", "VARDOT_EXPLIST"));

    /**************************************/
    /* RECURSIVELY PRINT non-null(!) sons (list, var and right...) */
    /**************************************/
    if (var != null)
      var.PrintMe();
    if (list != null)
      list.PrintMe();
    /***************************************/
    /* PRINT Node to AST GRAPHVIZ DOT file */
    /* print node name and optional string (maybe only needed in binop nodes) */
    /***************************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VARDOT_EXPLIST(%s)", id));

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /*
     * Print Edges to every son!
     */
    /****************************************/
    if (var != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    if (list != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
  }

  public TYPE SemantMe(){
  System.out.println("STMT VARDOT EXPLIST - semant me not completed!");
		TYPE t1 = var.SemantMe();
		if (t1 == null || !(t1 instanceof TYPE_CLASS)) //not a class
		{
			System.out.println(">> ERROR ["+line+"] var.dot is of wrong class");
			printError(line);
		}
		TYPE t2 = isFuncOfClass(t1.name,id,list,this.line);
    if (t2 == null)
		{
			System.out.println(">> ERROR ["+line+"] var.dot is wrong");
			printError(line);
		}
		
		return t2;
  }
}
