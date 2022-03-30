package AST;

import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_CFEILD_LIST extends AST_Node {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_CFIELD head;
	public AST_CFEILD_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFEILD_LIST(AST_CFIELD head, AST_CFEILD_LIST tail) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null)
			System.out.print("====================== cfeilds -> cfeild cfeilds\n");
		if (tail == null)
			System.out.print("====================== cfeilds -> cfeild      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "CFEILD_LIST"));

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
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CFEILD_LIST"));

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
		System.out.println("CFEILD LIST - semant me");

		if (tail == null) {
			return new TYPE_LIST(head.SemantMe(), null);
		} else {
			return new TYPE_LIST(head.SemantMe(), tail.SemantMe(0));
		}
	}

	public TEMP_LIST IRme(int ignore) {
		System.out.println("CFEILD_LIST - IRme");

		if ((head == null) && (tail == null)) {
			return null;
		} else if ((head != null) && (tail == null)) {
			return new TEMP_LIST(head.IRme(), null);
		} else {
			return new TEMP_LIST(head.IRme(), tail.IRme(0));
		}
	}
}
