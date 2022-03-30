package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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

	public TEMP IRme() {
		TYPE_CLASS fatherclass = father;
		boolean f = false;
		// ############first part (taking father fields and funcs)#################
		ArrayList<ArrayList<ArrayList<String>>> fieldlist = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> funclist = new ArrayList<ArrayList<String>>();
		while (fatherclass != null) {
			for (AST_TYPE_NAME_LIST it = fatherclass.functions; it != null; it = it.tail) {
				int n = funclist.size();
				for (int i = 0; i < n; i++) {
					f = false;
					if (((funclist.get(i)).get(0)).equals(it.head.name)) {
						ArrayList<String> temp = funclist.get(i);
						funclist.remove(i);
						funclist.add(0, temp);
						break;
					}
				}
				if (f == false) {
					ArrayList<String> funcOfFather = new ArrayList<String>();
					funcOfFather.add(it.head.name);
					funcOfFather.add(fatherclass.name);
					funclist.add(0, funcOfFather);
				}
			}

			for (AST_ARG_LIST it = fatherclass.data_members; it != null; it = it.tail) {
				f = false;
				int n = fieldlist.size();
				for (int i = 0; i < n; i++) {
					if ((((fieldlist.get(i)).get(0)).get(0)).equals(it.head.id)) {
						f = true;
						ArrayList<ArrayList<String>> temp = fieldlist.get(i);
						fieldlist.remove(i);
						fieldlist.add(0, temp);
						break;
					}
				}
				if (f == true)
					continue;

				ArrayList<String> fieldOfFather = new ArrayList<String>();
				fieldOfFather.add(it.head.id);
				if (it.head.t instanceof AST_TYPE_STRING)
					fieldOfFather.add("1");
				else
					fieldOfFather.add("0");
				ArrayList<ArrayList<String>> field = new ArrayList<ArrayList<String>>();
				field.add(fieldOfFather);
				ArrayList<String> fatherarray = new ArrayList<String>();
				fatherarray.add(fatherclass.name);
				field.add(fatherarray);
				fieldlist.add(0, field);
			}
			fatherclass = fatherclass.father;
		}

		// #############################################################3
		// #################second part - this class fields and funcs
		for (AST_CFEILD_LIST it = data_members; it != null; it = it.tail) {
			AST_CFIELD field = (AST_CFIELD) (it.head);
			if (field instanceof AST_CFEILD_VARDEC) { // field
				f = false;
				AST_CFEILD_VARDEC a = (AST_CFEILD_VARDEC) field;
				AST_VARDEC b = (AST_VARDEC) (a.vd);
				int n = fieldlist.size();
				for (int i = 0; i < n; i++) {
					if ((((fieldlist.get(i)).get(0)).get(0)).equals(b.id)) {
						f = true;
						((fieldlist.get(i)).get(1)).set(0, id);
						break;
					}
				}
				if (f == false) {
					ArrayList<String> fieldOfMe = new ArrayList<String>();
					fieldOfMe.add(b.id);
					if (b.type instanceof AST_TYPE_STRING)
						fieldOfMe.add("1");
					else
						fieldOfMe.add("0");
					ArrayList<ArrayList<String>> field2 = new ArrayList<ArrayList<String>>();
					field2.add(fieldOfMe);
					ArrayList<String> mearray = new ArrayList<String>();
					mearray.add(id);
					field2.add(mearray);
					fieldlist.add(field2);
				}
				continue;
			}
			if (field instanceof AST_CFEILD_FUNCDEC) { // func
				f = false;
				AST_CFEILD_FUNCDEC a = (AST_CFEILD_FUNCDEC) field;
				AST_FUNCDEC b = (AST_FUNCDEC) (a.func);
				int n = funclist.size();
				for (int i = 0; i < n; i++) {
					if (((funclist.get(i)).get(0)).equals(b.id)) {
						f = true;
						(funclist.get(i)).set(1, id);
						break;
					}
				}
				if (f == false) {
					ArrayList<String> funcOfMe = new ArrayList<String>();
					funcOfMe.add(b.id);
					funcOfMe.add(id);
					funclist.add(funcOfMe);
				}

			}
		}

		// #######################part 3 - offsets ################################
		int funcCnt = funclist.size();
		int fieldCnt = 0;
		Map<String, Integer> funcOff = new HashMap<>();
		ArrayList<String> fields = new ArrayList<>();
		for (int i = 0; i < fieldlist.size(); i++) {
			// fieldCnt += 1;
			String off = String.valueOf(fieldCnt * 4 + 4);
			offsets.put(id + "_" + (((fieldlist.get(i)).get(0)).get(0)), off);
			fieldCnt += 1;
			fields.add(((fieldlist.get(i)).get(0)).get(0));
		}
		for (int i = 0; i < funclist.size(); i++) {
			offsets.put(id + "_" + ((funclist.get(i)).get(0)), ((funclist.get(i)).get(1)) + "_" + ((funclist.get(i)).get(0)));
			funcOff.put(((funclist.get(i)).get(0)), i * 4);
		}

		classFuncsOff.put(id, funcOff);

		// System.out.println(fieldlist);
		// System.out.println(funclist);
		// #######################part 4 - mips function
		// ################################
		// son funcs
		for (AST_CFEILD_LIST it = data_members; it != null; it = it.tail) {
			if (it.head instanceof AST_CFEILD_FUNCDEC)
				it.head.IRme();
		}
		IR.getInstance().Add_IRcommand(new IRcommand_declareClass(id, funclist, fieldlist));
		// fields
		AST_CFEILD_LIST temp = data_members;
		for (int i = 0; i < fieldlist.size(); i++) {
			String tf = fieldlist.get(i).get(1).get(0);
			String n = fieldlist.get(i).get(0).get(0);
			if (!(tf.equals(id))) // father fields
			{
				String v = defaultFields.get(tf + "_" + n);
				if (v == null)
					IR.getInstance().Add_IRcommand(new IRcommand_Declare_Global_Object(id + "_" + n));
				else if (fieldlist.get(i).get(0).get(1).equals("1")) // string
					IR.getInstance().Add_IRcommand(new IRcommand_Declare_Global_String(id + "_" + n, v));
				else// int
					IR.getInstance().Add_IRcommand(new IRcommand_Declare_Global_Int(id + "_" + n, Integer.valueOf(v)));
			} else // son feilds
			{
				for (AST_CFEILD_LIST it = temp; it != null; it = it.tail) {
					if (it.head instanceof AST_CFEILD_VARDEC &&
							((AST_CFEILD_VARDEC) it.head).vd.id.equals(n)) {
						it.head.IRme();
						temp = data_members;
						break;
					}
				}
			}
		}

		classSize.put(id, fieldlist.size() * 4 + 4);
		classfields.put(id, fields);
		// System.out.println(classfields);
		return null;
	}
}
