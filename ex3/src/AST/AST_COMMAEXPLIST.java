package AST;

import TYPES.*;

public class AST_COMMAEXPLIST extends AST_Node {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP head;
	public AST_COMMAEXPLIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_COMMAEXPLIST(AST_EXP head, AST_COMMAEXPLIST tail) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null)
			System.out.print("====================== commaexp -> - --\n");
		if (tail == null)
			System.out.print("====================== commaexp -> -      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}
	/****************** outside CONSTRUCTOR code *******************/

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "COMMAEXPLIST"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (head != null)
			head.PrintMe();
		if (tail != null)
			tail.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("COMMAEXPLIST"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);

	}

	public TYPE_LIST SemantMe(int ignore) {
		System.out.println("COMMAEXPLIST semant me");
		if (tail == null) {
			return new TYPE_LIST(head.SemantMe(), null);
		} else {
			return new TYPE_LIST(head.SemantMe(), tail.SemantMe(1));
		}
	}

}
