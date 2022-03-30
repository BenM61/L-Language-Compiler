package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.*;
import MIPS.*;

public class AST_VARDEC_EXP extends AST_VARDEC {
	public AST_EXP exp;
	String scope; // for IRme
	String class_name; // for IRme
	boolean inFunc; // for IRme

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

		if (t1 == null || !(type_equals(t1, t2)) || t1 instanceof TYPE_VOID || t1 instanceof TYPE_NIL) {
			String tname = "non-existing";
			if (t1 != null)
				tname = t1.name;
			System.out.format(">> ERROR [%d] type mismatch for var := exp. %s vs %s\n", line, tname, t2.name);
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

		class_name = SYMBOL_TABLE.getInstance().inClassScope();
		scope = SYMBOL_TABLE.getInstance().getScope();
		inFunc = SYMBOL_TABLE.getInstance().inFuncScope();
		if (scope.equals("class") && !(exp instanceof AST_EXP_INT ||
				exp instanceof AST_EXP_NIL ||
				exp instanceof AST_EXP_STRING ||
				exp instanceof AST_EXP_MINUS_INT)) {
			System.out.println(">> ERROR[" + line + "] cant declare non primitive variable in class");
			printError(line);
		}
		isOverride();
		SYMBOL_TABLE.getInstance().enter(id, t1);

		/******************************************************/
		/* [2] return type doesn't matter ------------------- */
		/******************************************************/
		return t1;
	}

	public TEMP IRme() {
		System.out.println("VARDEC EXP IRme");
		String TrueId = id;

		// global
		if (scope.equals("global") || (!inFunc && class_name != null)) { // global or class field
			if (!inFunc && class_name != null)
				TrueId = class_name + "_" + id;
			if (type instanceof AST_TYPE_STRING) {
				String value = ((AST_EXP_STRING) exp).s;
				IR.getInstance().Add_IRcommand(new IRcommand_Declare_Global_String(TrueId, value));
				if (class_name!=null)
					defaultFields.put(class_name+"_"+id, value);
			} else if (type instanceof AST_TYPE_INT) {
				int value = ((AST_EXP_INT) exp).value;
				IR.getInstance().Add_IRcommand(new IRcommand_Declare_Global_Int(TrueId, value));
				if (class_name!=null)
					defaultFields.put(class_name+"_"+id, String.valueOf(value));
			} else {
				IR.getInstance().Add_IRcommand(new IRcommand_Declare_Global_Object(TrueId));
			}
		}

		// local
		else {
			TEMP t = exp.IRme();

			IRcommand command = new IRcommand_Store_Local(id, t);
			command.offset = GetOffset(id);
			IR.getInstance().Add_IRcommand(command);

		}

		// ret value doesn't matter for a declaration
		return null;
	}
}