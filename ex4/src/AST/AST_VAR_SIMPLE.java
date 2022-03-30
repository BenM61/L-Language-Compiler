package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_VAR_SIMPLE extends AST_VAR {
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	int inGlobal = 0; // needed for IRme
	public String className = null;
	public TYPE thisT;
	public boolean cfgVar = false;

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
		System.out.format("====================== var -> ID\n");
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
		TYPE res = SYMBOL_TABLE.getInstance().findInCurrScope(name);
		if (res != null && SYMBOL_TABLE.getInstance().getScope().equals("global"))
			inGlobal = 1;
		className = SYMBOL_TABLE.getInstance().inClassScope();
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
			boolean s = SYMBOL_TABLE.getInstance().inFuncScope();
			if (s)
				res = SYMBOL_TABLE.getInstance().findInFuncScope(name);

		}

		if (res == null) { // find in global scope
			res = SYMBOL_TABLE.getInstance().find(name);
			if (res != null)
				inGlobal = 1;
		}

		if (res == null) {
			System.out.println(">> ERROR[" + line + "]");
			printError(line);
		}
		thisT = res;
		return res;
	}

	public TEMP IRme() {
		System.out.format("VAR SIMPLE - IRme (%s)\n", name);

		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();

		if (inGlobal == 1) {
			IR.getInstance().Add_IRcommand(new IRcommand_Load_Global(t, name));
		} else { ///// THATS NOT ENTIRELY RIGHT
			String realN = name;
			boolean c = false;
			IRcommand command;
			if (className != null && offsets.get(name) == null) { // var is class field
				c = true;
				realN = className + "_" + name;
			}
			if (c == true && (!(thisT instanceof TYPE_CLASS) || !(((TYPE_CLASS) thisT).name.equals(className)))) // var is
			{																// field and																																																			// itself
				command = new IRcommand_ThisDotField(realN, t);
				((IRcommand_ThisDotField)command).cfg = cfgVar;
			}
			else
				command = new IRcommand_Load_Local(realN, t);

			command.offset = GetOffset(realN);
			IR.getInstance().Add_IRcommand(command);
		}
		return t;
	}
}