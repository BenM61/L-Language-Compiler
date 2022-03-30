package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_CLASSDEC_EXTENDS extends AST_CLASSDEC {
	String fatherName;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_CLASSDEC_EXTENDS(String id1, String id2, AST_CFEILD_LIST l, int line) {
		this.id = id1;
		this.father = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(id2);
		this.fatherName = id2;
		this.data_members = l;
		this.line = line;

		System.out.print("====================== classDec -> CLASS ID:id1 EXTENDS ID:id2 LBRACE cFieldList:cl RBRACE\n");

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

	}

	/****************** outside CONSTRUCTOR code *******************/

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "CLASSDEC_EXTENDS"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (data_members != null)
			data_members.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CLASSDEC(%s)_EXTENDS(%s)", id, father));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (data_members != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, data_members.SerialNumber);
		}
	}

	public TYPE SemantMe() {
		System.out.format("CLASSDEC EXTENDS(%s) - semant me\n", id);
		this.father = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(this.fatherName);
		
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/

		TYPE isExist = SYMBOL_TABLE.getInstance().findInCurrScope(id);
		if (isExist != null) { // already has a varible with the same name
			System.out.format(">> ERROR [%d] already exist a variable with the (class) name " + id + " in the same scope",
					line);
			printError(line);
		}
		SYMBOL_TABLE.getInstance().beginScope("class-" + id + "-extends-" + this.father.name);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		AST_ARG_LIST fields = null;
		AST_TYPE_NAME_LIST funcs = null;
		TYPE t = null;
		for (AST_CFEILD_LIST it = data_members; it != null; it = it.tail) {
			System.out.println("BEFORE");
			t = it.head.SemantMe(); // enter garbage to the stack
			System.out.println("AFTER");
			AST_TYPE currType = null;

			if (it.head instanceof AST_CFEILD_VARDEC) {
				switch (t.name) {
					case "int": {
						currType = new AST_TYPE_INT(line);
						break;
					}
					case "string": {
						currType = new AST_TYPE_STRING(line);
						break;
					}
					case "void": {
						System.out.println(">> ERROR [" + line + "] void variable is illegal");
						printError(line);
					}
					default: {
						currType = new AST_TYPE_ID(t.name, line);
						break;
					}
				}
				AST_ARG curr = new AST_ARG(currType, ((AST_CFEILD_VARDEC) it.head).vd.id);
				fields = new AST_ARG_LIST(curr, fields);
			}

			if (it.head instanceof AST_CFEILD_FUNCDEC) {
				AST_TYPE_NAME curr = new AST_TYPE_NAME(t, ((AST_CFEILD_FUNCDEC) it.head).func.id);
				funcs = new AST_TYPE_NAME_LIST(curr, funcs);
			}
		}

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().cleanGarbage();
		TYPE_CLASS classType = new TYPE_CLASS(father, id, fields, funcs);
		SYMBOL_TABLE.getInstance().enter(id, classType);
		SYMBOL_TABLE.getInstance().beginScope("class-" + id + "-extends-" + this.father.name);
		for (AST_CFEILD_LIST it = data_members; it != null; it = it.tail) {
			t = it.head.SemantMe(); // enter real values to the stack
		}
		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/

		// TYPE_CLASS classType = new TYPE_CLASS(null, id, fields, funcs);
		// classType.data_members.printArgList();
		// System.out.println("------");
		// SYMBOL_TABLE.getInstance().enter(id, classType);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}
}
