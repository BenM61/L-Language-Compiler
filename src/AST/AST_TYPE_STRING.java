package AST;

import TYPES.*;

public class AST_TYPE_STRING extends AST_TYPE {

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_TYPE_STRING(int line) {

		this.line = line;
		this.typeName = "string";

		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== type -> TYPE_STRING \n");
	}

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "TYPE_STRING"));

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("string"));
	}

	public TYPE SemantMe() {
		System.out.format("TYPE_STRING" + "- semant me\n");
		return TYPE_STRING.getInstance();
	}
}