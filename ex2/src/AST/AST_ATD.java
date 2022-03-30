package AST;

public class AST_ATD extends AST_Node {
	public AST_TYPE type;
	public String id;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_ATD(String id, AST_TYPE type) {
		this.type = type;
		this.id = id;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "ATD"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (type != null)
			type.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ATD(%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (type != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);

	}
}