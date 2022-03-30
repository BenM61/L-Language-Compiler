package AST;

import TYPES.*;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.*;

public class AST_EXP_INT extends AST_EXP {
	public int value;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_INT(int value) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/******************************/
		this.value = value;

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> INT(%d)\n", value);
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe() {
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST EXP_INT( %d )\n", value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%d", value));
	}

	public TYPE SemantMe() {
		System.out.println("EXP INT (recoginzed int)- semant me");
		return TYPE_INT.getInstance();
	}

	public TEMP IRme() {
		System.out.println("EXP INT- IRme");

		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(t, value));
		return t;
	}
}