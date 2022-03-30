package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.*;

public class AST_ATD extends AST_Node {
	public AST_TYPE type;
	public String id;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_ATD(String id, AST_TYPE type, int line) {
		this.type = type;
		this.id = id;
		this.line = line;

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== 		arrayTypedef ::= 	array ID = type[]; \n");

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	}

	/*************************************************/
	/* The printing message ------------------------ */
	/*************************************************/
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE- change XXX with this class name */
		/*************************************/
		System.out.print(String.format("AST %s NODE\n", "ATD"));

		/**************************************/
		/* RECURSIVELY PRINT non-null(!) sons (list, left and right...) */
		/**************************************/
		if (type != null)
			type.PrintMe();
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/* print node name and optional string (maybe only needed in binop nodes) */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ATD(%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/*
		 * Print Edges to every son!
		 */
		/****************************************/
		if (type != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);

	}

	public TYPE SemantMe() {

		System.out.println("ATD" + "- semantme");

		TYPE typeType = type.SemantMe();

		// arrays declared in global scope- actually always true thanks to parsing
		if (!(SYMBOL_TABLE.getInstance().getScope().equals("global"))) {
			System.out.format(">> ERROR [%d] array dec in wrong scope\n", line);
			printError(line);
		}

		// array's type must be predefined
		if (typeType == null || typeType instanceof TYPE_VOID || typeType instanceof TYPE_NIL) {
			System.out.format(">> ERROR [%d] non existing type\n", line);
			printError(line);
		}

		// id must be new
		if (SYMBOL_TABLE.getInstance().find(id) != null) {
			System.out.format(">> ERROR [%d] %s is already declared.\n", line, id);
			printError(line);
		}

		// if all passed, add to symbol table
		SYMBOL_TABLE.getInstance().enter(id, new TYPE_ARRAY(typeType, id));

		// return val is irrelevent
		return null;
	}

	public TEMP IRme() {
		System.out.println("ATD- IRme");
		return null;
	}
}