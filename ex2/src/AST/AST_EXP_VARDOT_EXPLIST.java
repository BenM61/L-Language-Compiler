package AST;

public class AST_EXP_VARDOT_EXPLIST extends AST_EXP {
	public AST_VAR var;
	public String id;
	public AST_EXPLIST list;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VARDOT_EXPLIST(AST_VAR var, String id, AST_EXPLIST list) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> vardot_explist\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.list = list;
	}

	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE EXP_VARDOT_EXPLIST\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null)
			var.PrintMe();
		if (list != null)
			list.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_VARDOT_EXPLIST(%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (list != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

	}
}
