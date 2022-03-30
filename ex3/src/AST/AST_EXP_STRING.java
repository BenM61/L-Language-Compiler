package AST;

import TYPES.*;

public class AST_EXP_STRING extends AST_EXP {
	public String s;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(Object s) {
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.s = ((String) s).split("\"")[1];
	}

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "EXP_STRING"));

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", s));
	}

	public TYPE SemantMe() {
		System.out.println("EXP STRING (recoginzed string)- semant me");
		return TYPE_STRING.getInstance();
	}

}
