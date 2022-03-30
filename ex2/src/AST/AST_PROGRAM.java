package AST;

public class AST_PROGRAM extends AST_Node {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_LIST list;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PROGRAM(AST_DEC_LIST list) {
		this.list = list;

		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== program -> decs \n");
	}

	@Override
	public void PrintMe() {
		System.out.print("AST PROGRAM NODE\n");
		list.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("PROGRAM"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
	}

}
