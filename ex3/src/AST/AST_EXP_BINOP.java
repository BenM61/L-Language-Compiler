package AST;
import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP {
	int OP;
	public AST_BINOP binop;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_BINOP binop) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.binop = binop;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST EXP_BINOP NODE\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		
		if (binop != null)
			binop.PrintMe();
		

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_BINOP"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		
		if (binop != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, binop.SerialNumber);
		
	}
	public TYPE SemantMe()
	{
		System.out.println("EXP BINOP - semant me");
		return binop.SemantMe();
	}
}
