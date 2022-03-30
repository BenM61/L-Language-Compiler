package AST;

import TYPES.*;
import TEMP.*;

public class AST_CFEILD_VARDEC extends AST_CFIELD {
	public AST_VARDEC vd;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_CFEILD_VARDEC(AST_VARDEC vd) {
		this.vd = vd;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (vd != null)
			System.out.print("====================== cfield -> varDec\n");
	}

	/****************** outside CONSTRUCTOR code *******************/

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "CFEILD_VARDEC"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (vd != null)
			vd.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CFEILD_VARDEC"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (vd != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, vd.SerialNumber);
		}
	}

	public TYPE SemantMe() {
		System.out.println("CFEILD VARDEC - semant me");
		return vd.SemantMe();

	}
	public TEMP IRme(){
		System.out.println("CFEILD_VARDEC" + "- IRme");
		vd.IRme();
		return null;
	}

}
