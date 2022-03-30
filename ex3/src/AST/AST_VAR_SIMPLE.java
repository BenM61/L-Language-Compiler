package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SIMPLE extends AST_VAR {
	/************************/
	/* simple variable name */
	/************************/
	public String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int line) {
		this.line = line;
		this.name = name;

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n", name);
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe() {
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE_VAR( %s )\n", name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("SIMPLE_VAR(%s)", name));
	}

	public TYPE SemantMe() {
		System.out.format("[%d] VAR SIMPLE - semant me", line);
		TYPE res = SYMBOL_TABLE.getInstance().findInCurrScope(name);
		String className = SYMBOL_TABLE.getInstance().inClassScope();
		if (res == null) {
			// if class wasnt declared yet
			TYPE fieldType = SYMBOL_TABLE.getInstance().findInClassScope(name);
			if (className != null && fieldType != null)
				return fieldType;

			// inheritance case
			else if (className != null) {
				String fatherName = SYMBOL_TABLE.getInstance().findExtendsClass(className);
				if (fatherName != null) {
					TYPE_CLASS fatherClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherName);
					while (fatherClass != null) {
						for (AST_ARG_LIST it = fatherClass.data_members; it != null; it = it.tail) {
							if (it.head.id.equals(name)) {
								String resName = it.head.t.typeName;
								return res = SYMBOL_TABLE.getInstance().find(resName);
							}
						}
						fatherClass = fatherClass.father;
					}
				}
				System.out.println(">> ERROR [" + line + "] no such class field");
				printError(line);
			}
		}

		if (res == null) {
			System.out.println("Searching (if inside a) func scope\n");
			boolean s = SYMBOL_TABLE.getInstance().inFuncScope();
			if (s)
				res = SYMBOL_TABLE.getInstance().findInFuncScope(name);

		}
		if (res == null) { // find in global scope
			res = SYMBOL_TABLE.getInstance().find(name);
		}

		if (res == null) {
			System.out.println(">> ERROR[" + line + "]");
			printError(line);
		}
		return res;
	}
}
