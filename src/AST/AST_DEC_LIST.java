package AST;

import TYPES.*;
import TEMP.*;

public class AST_DEC_LIST extends AST_Node {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC head;
	public AST_DEC_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null)
			System.out.print("====================== decs -> dec decs\n");
		if (tail == null)
			System.out.print("====================== decs -> dec      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/*************************************************/
	/* The printing message for a node */
	/*************************************************/
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "DEC_LIST"));

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
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC_LIST"));

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

	public TYPE SemantMe() {
		System.out.println("DEC_LIST" + "- semantme");
		if (head != null) {
			head.SemantMe();
		}
		if (tail != null) {
			tail.SemantMe();
		}
		return null;
	}

	public TEMP IRme() {
		System.out.println("DEC_LIST" + "- IRme");

		if (head != null)
			head.IRme();
		if (tail != null)
			tail.IRme();

		return null;
	}
}
