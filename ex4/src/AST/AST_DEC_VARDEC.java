package AST;

import TYPES.*;
import TEMP.*;

public class AST_DEC_VARDEC extends AST_DEC {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_VARDEC v;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_VARDEC(AST_VARDEC v) {
		this.v = v;

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (v != null)
			System.out.print("====================== dec -> varDec\n");
	}

	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "VARDEC"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (v != null)
			v.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VARDEC"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (v != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, v.SerialNumber);
		}
	}

	public TYPE SemantMe() {
		System.out.println("DEC VARDEC" + "- semantme");
		if (v != null) {
			return v.SemantMe();
		}
		
		return null;
	}
	public TEMP IRme() {
		System.out.println("DEC_VARDEC" + "- IRme");
		if (v != null) {
			return v.IRme();
		}	
		return null;
	}

}