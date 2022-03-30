package AST;
import TYPES.*;

public class AST_EXP_VARDOT_EXPLIST extends AST_EXP {
	public AST_VAR var;
	public String id;
	public AST_EXPLIST list;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VARDOT_EXPLIST(AST_VAR var, String id, AST_EXPLIST list,int line) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> vardot_explist\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.list = list;
		this.line = line;
	}

	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE EXP_VARDOT_EXPLIST\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null)
			var.PrintMe();
		if (list != null)
			list.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_VARDOT_EXPLIST(%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (list != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

	}
	public TYPE SemantMe() {
		///a.g(); or a.g(a,b);
		System.out.println("EXP VARDOT EXPLIST - semant me");
		TYPE t1 = var.SemantMe();
		if (t1 == null || !(t1 instanceof TYPE_CLASS)) //not a class
		{
			System.out.println(">> ERROR ["+line+"] var.dot is of wrong class");
			printError(line);
		}

		TYPE t2 = isFuncOfClass(t1.name,id,list,this.line);
		if (t2 == null)
		{
			System.out.println(">> ERROR ["+line+"] var.dot is wrong");
			printError(line);
		}
		
		return t2;
		
	}
}
