package AST;

import TYPES.*;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ASSIGN extends AST_STMT {
	/***************/
	/* var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	public TYPE scope; // for irme
	public String inclass; // for irme
	public String ty;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var, AST_EXP exp, int line) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
		this.line = line;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe() {
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null)
			var.PrintMe();
		if (exp != null)
			exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "ASSIGN\nleft := right\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (exp != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
	}

	public TYPE SemantMe() {
		System.out.println("STMT_ASSIGN - semant me");

		TYPE t1 = null;
		TYPE t2 = null;

		if (var == null || exp == null) {
			printError(this.line);
		}

		t1 = var.SemantMe();
		t2 = exp.SemantMe();
		ty = t2.name;

		if (t1 == null || t2 == null) {
			System.out.format(">> ERROR [%d] non existing type\n", line);
			printError(line);
		}

		if (!(type_equals(t1, t2))) {
			System.out.format(">> ERROR [%d] type mismatch for var := exp (stmt assgn)\n", line);
			printError(this.line);
		}

		if (var instanceof AST_VAR_SIMPLE && SYMBOL_TABLE.getInstance().inFuncScope()) {
			scope = SYMBOL_TABLE.getInstance().findInFuncScope(((AST_VAR_SIMPLE) var).name);
		}
		inclass = SYMBOL_TABLE.getInstance().inClassScope();

		return null;
	}

	public TEMP IRme() {
		System.out.println("STMT_ASSIGN - IRme");

		if (var instanceof AST_VAR_SIMPLE) {
			TEMP value = exp.IRme();
			((AST_VAR_SIMPLE) var).cfgVar = true;
			String name = ((AST_VAR_SIMPLE) var).name;
			// global
			if (((AST_VAR_SIMPLE) var).inGlobal == 1)
				IR.getInstance().Add_IRcommand(new IRcommand_Store_Global(value, name));
			// local
			else {
				if (scope != null) // inside func scope and local of func
				{
					String varName = ((AST_VAR_SIMPLE) var).name;
					IRcommand cRcommand = new IRcommand_Store_Local(varName, value);
					IR.getInstance().Add_IRcommand(cRcommand);
					cRcommand.offset = GetOffset(varName);
				} else if (inclass != null) { // can be field in func
					String varName = inclass + "_" + ((AST_VAR_SIMPLE) var).name;
					IRcommand c = new IRcommand_store_field(inclass, varName, value);
					c.offset = GetOffset(varName);
					IR.getInstance().Add_IRcommand(c);

					// String varName = inclass + "&" + ((AST_VAR_SIMPLE) var).name;
					// using the store global to store inside a label even tho its local!
					// IR.getInstance().Add_IRcommand(new IRcommand_Store_Global(value, varName));
				}

			}
		} else if (var instanceof AST_VAR_FIELD) {
			TEMP t1 = ((AST_VAR_FIELD) var).var.IRme();
			String f_name = ((AST_VAR_FIELD) var).fieldName;
			String c = ((AST_VAR_FIELD) var).classN;
			TEMP value = exp.IRme();
			IRcommand r = new IRcommand_field_set(t1, f_name, value);
			r.offset = GetOffset(c + "_" + f_name);
			IR.getInstance().Add_IRcommand(r);
			if (((AST_VAR_FIELD) var).var instanceof AST_VAR_SIMPLE)
				((AST_VAR_SIMPLE) ((AST_VAR_FIELD) var).var).cfgVar = true;

		} else { // var instanceof AST_VAR_SUBSCRIPT [Working]

			AST_VAR_SUBSCRIPT subVar = (AST_VAR_SUBSCRIPT) var;
			TEMP array = subVar.var.IRme();
			TEMP index = subVar.subscript.IRme();
			TEMP value = exp.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_array_set(array, index, value));
		}

		return null;
	}

}