package AST;

import TYPES.*;
import TEMP.*;

public class AST_CFEILD_FUNCDEC extends AST_CFIELD {
	public AST_FUNCDEC func;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_CFEILD_FUNCDEC(AST_FUNCDEC func) {
		this.func = func;

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	}

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "CFEILD_FUNCDEC"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (func != null)
			func.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CFEILD_FUNCDEC"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (func != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, func.SerialNumber);
		}
	}

	public TYPE SemantMe() {
		System.out.println("CFEILD FUNCDEC semant me");
		return func.SemantMe();
	}

	public TEMP IRme() {
		System.out.println("CFEILD_FUNCDEC - IRme");
		func.IRme();
		return null;
	}
}
