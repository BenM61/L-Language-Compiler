package AST;

public class AST_VARDEC_EXP extends AST_VARDEC {
	public AST_TYPE type;
	public String id;
	public AST_EXP exp;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_VARDEC_EXP(AST_TYPE type, String id, AST_EXP exp) {
		this.type = type;
		this.id = id;
		this.exp = exp;

		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (type != null && exp != null)
			System.out.print("====================== varDec -> type ID ASSIGN exp SEMICOLON \n");
	}

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "VARDEC_EXP"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (exp != null)
			exp.PrintMe();
		if (type != null)
			type.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VARDEC_EXP(%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (type != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
		}
		if (exp != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
		}
	}
}