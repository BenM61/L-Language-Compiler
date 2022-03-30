package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

import SYMBOL_TABLE.*;

public class AST_VARDEC_EXP extends AST_VARDEC {
	public AST_EXP exp;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_VARDEC_EXP(AST_TYPE type, String id, AST_EXP exp, int line) {
		this.type = type;
		this.id = id;
		this.exp = exp;
		this.line = line;

		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (type != null && exp != null)
			System.out.print("====================== varDec -> type ID ASSIGN exp SEMICOLON \n");
	}

	/*************************************************/
	/* The printing message for a XXX node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "VARDEC_EXP"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (exp != null)
			exp.PrintMe();
		if (type != null)
			type.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VARDEC_EXP(%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (type != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
		}
		if (exp != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
		}
	}

	public TYPE SemantMe() {
		System.out.println("VARDEC EXP semant me");
		TYPE t1 = null;
		TYPE t2 = null;

		/******************************************************/
		/* [0] check if the two types are the same */
		/******************************************************/
		if (type == null || exp == null) {
			System.out.println(">>>ERROR type or name are null");
			printError(this.line);
		}

		t1 = findType(type.typeName);
		t2 = exp.SemantMe();

		if (!(type_equals(t1, t2)) || t1 instanceof TYPE_VOID || t1 instanceof TYPE_NIL) {
			System.out.format(">> ERROR [%d] type mismatch for var := exp. %s vs %s\n", line, t1.name, t2.name);
			printError(this.line);
		}

		/******************************************************/
		/* [1] check if variable is already declared in scope */
		/******************************************************/
		TYPE res = SYMBOL_TABLE.getInstance().findInCurrScope(id);
		if (res != null) {
			System.out.format(">> ERROR [%d] %s is already declared.\n", line, id);
			printError(this.line);
		}

		String scope = SYMBOL_TABLE.getInstance().getScope();
		if (scope.equals("class") && !(exp instanceof AST_EXP_INT ||
				exp instanceof AST_EXP_NIL ||
				exp instanceof AST_EXP_STRING ||
				exp instanceof AST_EXP_MINUS_INT)) {
			System.out.println(">> ERROR[" + line + "] cant declare non primitive variable in class");
			printError(line);
		}
		isOverride();
		SYMBOL_TABLE.getInstance().enter(id, t1);
		System.out.format("[%d] %s of type %s is now declared.\n", line, id, t1.name);

		/******************************************************/
		/* [2] return type doesn't matter ------------------- */
		/******************************************************/
		return t1;
	}
}