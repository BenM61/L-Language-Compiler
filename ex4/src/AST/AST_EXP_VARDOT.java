package AST;

import TYPES.*;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.*;

public class AST_EXP_VARDOT extends AST_EXP {
	public AST_VAR var;
	public String id;
	public TYPE_CLASS tl; //for ir

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VARDOT(AST_VAR var, String id, int line) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> vardot\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.line = line;
	}

	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE EXP_VARDOT\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null)
			var.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_VARDOT(%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
	}

	public TYPE SemantMe() {
		///a.g();
		System.out.println("EXP VARDOT - semant me");
		TYPE t1 = var.SemantMe();
		if (t1 == null || !(t1 instanceof TYPE_CLASS)) //not a class
		{
			System.out.println(">> ERROR ["+line+"] var.dot is of wrong class");
			printError(line);
		}
		tl = (TYPE_CLASS)t1;

		TYPE t2 = isFuncOfClass(t1.name,id,null,this.line);
		if (t2 == null)
		{
			System.out.println(">> ERROR ["+line+"] var.dot is wrong");
			printError(line);
		}
		
		return t2;
		
	}
	public TEMP IRme()
	{
	System.out.println("EXP VARDOT - IRme");
	return vardotIR(var, null, tl, id);
	}
}