package AST;

public class AST_CLASSDEC_EXTENDS extends AST_CLASSDEC {
	public String id1;
	public String id2;
	public AST_CFEILD_LIST l;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_CLASSDEC_EXTENDS(String id1, String id2, AST_CFEILD_LIST l) {
		this.id1 = id1;
		this.id2 = id2;
		this.l = l;
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
		System.out.print(String.format("AST %s NODE\n", "CLASSDEC_EXTENDS"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (l != null)
			l.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CLASSDEC(%s)_EXTENDS(%s)", id1, id2));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (l != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, l.SerialNumber);
		}
	}
}