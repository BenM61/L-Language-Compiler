package AST;

import TYPES.*;

import SYMBOL_TABLE.*;

import java.io.PrintWriter;

public abstract class AST_Node {
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating */
	/* a graphviz dot format of the AST ... */
	/*******************************************/
	public int SerialNumber;

	public int line;

	public String typeName;

	private static String file;

	/***********************************************/

	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe() {
		System.out.print("AST NODE UNKNOWN\n");
	}

	// should also update typeName
	public TYPE SemantMe() {
		System.out.println("DEFAULT SEMANTME!!!");
		return null;
	}

	public TYPE_LIST SemantMe(int ignore) {
		System.out.println("DEFAULT SEMANTME!!!");
		return null;
	}

	// only valid after SemantMe() call!
	public String getTypeName() {
		return typeName;
	}

	public void getFile(String file) {
		this.file = file;
	}

	public void printError(int line) {
		try {
			PrintWriter file_writer;
			file_writer = new PrintWriter(this.file);
			file_writer.print("ERROR");
			file_writer.print("(");
			file_writer.print(line);
			file_writer.print(")");
			file_writer.close();
			System.exit(0);
		} catch (Exception e) {
		}
	}

	public boolean type_equals(TYPE t1, TYPE t2) {
		// primitive case
		if (t1 == t2) {
			return true;
		}

		if (t1 == TYPE_INT.getInstance() || t1 == TYPE_STRING.getInstance() || t1 == TYPE_VOID.getInstance() ||
				t2 == TYPE_INT.getInstance() || t2 == TYPE_STRING.getInstance() || t2 == TYPE_VOID.getInstance()) {
			return false;
		}

		// non primitive
		if ((t1 instanceof TYPE_NIL) || (t2 instanceof TYPE_NIL)) {
			return true;
		}

		// array case
		if (t1.isArray() && t2.isArray()) {
			// todo: should check if t2 is son of t1 instead of equality
			return t1.name.equals(t2.name) || t2.name.equals(((TYPE_ARRAY) t1).entryType.name + "[]")
					|| type_equals(((TYPE_ARRAY) t1).entryType, ((TYPE_ARRAY) t2).entryType);
		}

		// class case ...
		if (t1.isClass() && t2.isClass() && t1.name.equals(t2.name))
			return true;

		// inheritance
		if (t2.isClass() && t1.isClass()) {
			TYPE_CLASS father = ((TYPE_CLASS) t2).father;
			while (father != null) {
				if (father.name.equals(t1.name))
					return true;
				father = father.father;
			}

			// class wasnt declared yet
			String cllass = SYMBOL_TABLE.getInstance().inClassScope();
			if (cllass != null && cllass.equals(t2.name) && t1.isClass()) {
				String fatherName = SYMBOL_TABLE.getInstance().findExtendsClass(t2.name);
				if (fatherName.equals(t1.name))
					return true;
				TYPE_CLASS fatherC = ((TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherName)).father;
				while (fatherC != null) {
					if (fatherC.name.equals(t1.name))
						return true;
					fatherC = fatherC.father;
				}
			}
		}

		// function case(?) ...

		return false;
	}

	public TYPE funcSig(String id, AST_EXPLIST list, int line) {
		TYPE type = SYMBOL_TABLE.getInstance().isRealFunc(id, list);

		if (type == null) {
			System.out.format(">> ERROR [%d] " + id + " is not a function or the parameters given are wrong!", line);
			printError(line);
		}
		return type;
	}

	// find only for types!!!
	public TYPE findType(String name) {
		TYPE t = SYMBOL_TABLE.getInstance().find(name);

		// if class was already declared then t!=null
		if (t == null) {
			// check if its equal class boundary before class was declared
			String cl = SYMBOL_TABLE.getInstance().inClassScope();
			if (cl != null && cl.equals(name)) {
				// only comes here BEFORE we declare the class!
				t = new TYPE_CLASS(null, name, null, null);
			}
		}
		return t;
	}

	public TYPE isFuncOfClass(String className, String funcName, AST_EXPLIST funcArgs, int line) {

		if (SYMBOL_TABLE.getInstance().inClassScope() != null &&
				SYMBOL_TABLE.getInstance().inClassScope().equals(className)) { // should find func in current class scope (going
																																				// up till class and search for func)
			TYPE f = SYMBOL_TABLE.getInstance().findInClassScope(funcName);
			if (f instanceof TYPE_FUNCTION)
				return funcSig(funcName, funcArgs, line);
			// inheritance case
			String extendClass = SYMBOL_TABLE.getInstance().findExtendsClass(className);
			if (extendClass != null) {
				TYPE_CLASS father = (TYPE_CLASS) (SYMBOL_TABLE.getInstance().find(className));
				while (father != null) {
					AST_TYPE_NAME_LIST funcs = father.functions;
					for (AST_TYPE_NAME_LIST it = funcs; it != null; it = it.tail) {
						if (it.head.name.equals(funcName))
							return SYMBOL_TABLE.getInstance().compareFuncs((TYPE_FUNCTION) it.head.type, funcArgs, line);
					}
					father = father.father;
				}
			}
			return null;
		}

		// class was already declared
		TYPE cl = SYMBOL_TABLE.getInstance().find(className);
		if (cl == null || !(cl.isClass())) // there isnt such a class
		{
			System.out.println(">> ERROR [" + line + "] there isnt such a class!");
			printError(line);
		}

		TYPE_CLASS currClass = (TYPE_CLASS) cl;
		TYPE_FUNCTION a = null;
		while (currClass != null) {
			for (AST_TYPE_NAME_LIST it = currClass.functions; it != null; it = it.tail) {
				if (it.head.name.equals(funcName)) {
					a = (TYPE_FUNCTION) it.head.type;
					break;
				}
			}
			currClass = currClass.father;
		}

		if (a == null) // no such func
		{
			System.out.println(">> ERROR [" + line + "] there isnt such a func!");
			printError(line);
		}
		// should compare a params to func args - getting func args type by semant me
		return SYMBOL_TABLE.getInstance().compareFuncs(a, funcArgs, line);

	}

}
