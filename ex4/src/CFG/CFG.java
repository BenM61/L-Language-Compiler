/***********/
/* PACKAGE */
/***********/
package CFG;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import CFG.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import CFG.colorGraph.node;
import TEMP.*;
import MIPS.*;
import IR.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class CFG {
	basicBlock head; // for build
	basicBlock tail;// for liveness
	HashMap<String, String> IRtoMIPS = new HashMap<String, String>();

	public CFG() {
		IRcommand h = IR.getInstance().head;
		IRcommandList t = IR.getInstance().tail;
		int lineCounter = 0;
		this.head = new basicBlock(lineCounter, h);
		lineCounter++;
		basicBlock curr = this.head;
		basicBlock next;

		while (t != null) {
			h = t.head;
			t = t.tail;
			next = new basicBlock(lineCounter, h);
			lineCounter++;
			curr.direct = next;
			next.father = curr;
			curr = next;
		}
		this.tail = curr;
		// at this point we have the direct line of things, lets take care of the jumps
		// go over the CFG graph this time
		curr = this.head;
		while (curr != null) {
			Boolean isBranch = (curr.line.IRname.equals("IRcommand_Conditional_Jump"));
			if (isBranch) {
				next = this.head;
				String l = ((IRcommand_Conditional_Jump) curr.line).label;
				while (next != null) {
					Boolean isLabel = next.line.getClass().toString().equals("class IR.IRcommand_Label");
					if (isLabel) {
						if (((IRcommand_Label) next.line).label_name == l) {// we found it!
							curr.condWorked = next; // we have no need to father it
						}
						break;// we found it, stop iterating
					}
					next = next.direct;
				}
			}
			curr = curr.direct;

		}
	}

	public void liveness() {
		basicBlock curr = this.tail; // since we are going buttom-up
		Boolean notTail = false;
		while (curr != null) {
			if (notTail) {
				Iterator<String> d = curr.direct.inSet.iterator();
				while (d.hasNext()) {
					String elem = d.next();
					curr.outSet.add(elem);
				}
				if (curr.condWorked != null) {
					Iterator<String> w = curr.condWorked.inSet.iterator();
					while (w.hasNext()) {
						String elem = w.next();
						curr.outSet.add(elem);
					}
				}
			} // now in is full
			notTail = true;
			Boolean isBinop = curr.line.IRname.equals("IRcommand_Binop");
			Boolean isAssign = curr.line.IRname.equals("IRcommand_Assign");
			Boolean isTwo = curr.line.IRname.equals("IRcommand_Two_Temps");
			Iterator<String> i = curr.outSet.iterator();
			while (i.hasNext()) {
				curr.inSet.add(i.next());
			}
			// System.out.println(curr.inSet);
			// System.out.println(curr.FuncScope);
			if (isBinop) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_Binop) curr.line).dst.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Binop) curr.line).t1.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Binop) curr.line).t2.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope.remove(("Temp_" + (Integer.toString(((IRcommand_Binop) curr.line).dst.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_Binop) curr.line).t1.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_Binop) curr.line).t2.getSerialNumber()))));
				}
			}
			if (isAssign) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_Assign) curr.line).dst.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope.remove(("Temp_" + (Integer.toString(((IRcommand_Assign) curr.line).dst.getSerialNumber()))));
				}
			}
			if (isTwo) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_Two_Temps) curr.line).dst.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Two_Temps) curr.line).val.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope
							.remove(("Temp_" + (Integer.toString(((IRcommand_Two_Temps) curr.line).dst.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_Two_Temps) curr.line).val.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Store_Local")) {
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Store_Local) curr.line).src.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_Store_Local) curr.line).src.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Store_Global")) {
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Store_Global) curr.line).dst.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope
							.add(("Temp_" + (Integer.toString(((IRcommand_Store_Global) curr.line).dst.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_ThisDotField")) {
				if (((IRcommand_ThisDotField) curr.line).cfg == false) {
					curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_ThisDotField) curr.line).dst.getSerialNumber()))));
					if (curr.inFunc)
						curr.FuncScope
								.add(("Temp_" + (Integer.toString(((IRcommand_ThisDotField) curr.line).dst.getSerialNumber()))));
				} else {
					curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_ThisDotField) curr.line).dst.getSerialNumber()))));
					if (curr.inFunc)
						curr.FuncScope
								.remove(("Temp_" + (Integer.toString(((IRcommand_ThisDotField) curr.line).dst.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Call_Func")) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_Call_Func) curr.line).t.getSerialNumber()))));
				TEMP h;
				TEMP_LIST t = ((IRcommand_Call_Func) curr.line).tempList;
				while (t != null) {
					h = t.head;
					curr.inSet.add(("Temp_" + (Integer.toString(h.getSerialNumber()))));
					t = t.tail;
				}
				if (curr.inFunc) {
					curr.FuncScope.remove(("Temp_" + (Integer.toString(((IRcommand_Call_Func) curr.line).t.getSerialNumber()))));
					t = ((IRcommand_Call_Func) curr.line).tempList;
					while (t != null) {
						h = t.head;
						curr.FuncScope.add(("Temp_" + (Integer.toString(h.getSerialNumber()))));
						t = t.tail;
					}
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_virtual_call")) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_virtual_call) curr.line).dst.getSerialNumber()))));
				curr.inSet
						.add(("Temp_" + (Integer.toString(((IRcommand_virtual_call) curr.line).classTemp.getSerialNumber()))));
				TEMP h;
				TEMP_LIST t = ((IRcommand_virtual_call) curr.line).args;
				while (t != null) {
					h = t.head;
					curr.inSet.add(("Temp_" + (Integer.toString(h.getSerialNumber()))));
					t = t.tail;
				}
				if (curr.inFunc) {
					curr.FuncScope
							.remove(("Temp_" + (Integer.toString(((IRcommand_virtual_call) curr.line).dst.getSerialNumber()))));
					curr.FuncScope
							.add(("Temp_" + (Integer.toString(((IRcommand_virtual_call) curr.line).classTemp.getSerialNumber()))));
					t = ((IRcommand_virtual_call) curr.line).args;
					while (t != null) {
						h = t.head;
						curr.FuncScope.add(("Temp_" + (Integer.toString(h.getSerialNumber()))));
						t = t.tail;
					}
				}
			}
			if (curr.line.IRname.equals("IRcommand_Conditional_Jump")) {
				if ((curr.line.getClass().toString().equals("class IR.IRcommand_Jump_bnez"))
						|| (curr.line.getClass().toString().equals("class IR.IRcommand_Jump_beqz"))) {
					curr.inSet
							.add(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) curr.line).oprnd1.getSerialNumber()))));
					if (curr.inFunc) {
						curr.FuncScope
								.add(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) curr.line).oprnd1.getSerialNumber()))));
					}
				} else if ((curr.line.getClass().toString().equals("class IR.IRcommand_Jump_Label"))) {
				} else {
					curr.inSet
							.add(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) curr.line).oprnd1.getSerialNumber()))));
					curr.inSet
							.add(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) curr.line).oprnd2.getSerialNumber()))));
					if (curr.inFunc) {
						curr.FuncScope
								.add(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) curr.line).oprnd1.getSerialNumber()))));
						curr.FuncScope
								.add(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) curr.line).oprnd2.getSerialNumber()))));
					}
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_array_set")) {
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_array_set) curr.line).array.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_array_set) curr.line).index.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_array_set) curr.line).val.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_array_set) curr.line).array.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_array_set) curr.line).index.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_array_set) curr.line).val.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Prologue")) {
				curr.inFunc = false;
				if (curr.FuncScope != null) {
					Iterator<String> iter = curr.FuncScope.iterator();
					while (iter.hasNext()) {
						String temp = iter.next();
						curr.inSet.remove(temp);
					}
					curr.FuncScope = new HashSet<String>();
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Epilogue")) {
				curr.inFunc = true;
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Array_Access")) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_Array_Access) curr.line).dst.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Array_Access) curr.line).t1.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Array_Access) curr.line).t2.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope
							.remove(("Temp_" + (Integer.toString(((IRcommand_Array_Access) curr.line).dst.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_Array_Access) curr.line).t1.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_Array_Access) curr.line).t2.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Load_Local")) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_Load_Local) curr.line).dst.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope
							.remove(("Temp_" + (Integer.toString(((IRcommand_Load_Local) curr.line).dst.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_store_field")) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_store_field) curr.line).val.getSerialNumber()))));
				if (curr.inFunc)
					curr.FuncScope
							.remove(("Temp_" + (Integer.toString(((IRcommand_store_field) curr.line).val.getSerialNumber()))));
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Field_Access")) {
				curr.inSet.remove(("Temp_" + (Integer.toString(((IRcommand_Field_Access) curr.line).dst.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Field_Access) curr.line).src.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope
							.remove(("Temp_" + (Integer.toString(((IRcommand_Field_Access) curr.line).dst.getSerialNumber()))));
					curr.FuncScope
							.add(("Temp_" + (Integer.toString(((IRcommand_Field_Access) curr.line).src.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_field_set")) {
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_field_set) curr.line).dst.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_field_set) curr.line).val.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_field_set) curr.line).dst.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_field_set) curr.line).val.getSerialNumber()))));
				}
			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_Return")) {
				curr.inFunc = true;
				/*
				 * if (curr.FuncScope != null){
				 * Iterator<String> iter = curr.FuncScope.iterator();
				 * while(iter.hasNext()){
				 * String temp = iter.next();
				 * curr.inSet.remove(temp);
				 * }
				 * }
				 * curr.FuncScope = new HashSet<String> ();
				 */
				if (((IRcommand_Return) curr.line).RetVal != null) {
					curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_Return) curr.line).RetVal.getSerialNumber()))));
				}

			}
			if (curr.line.getClass().toString().equals("class IR.IRcommand_New_Array")) {
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_New_Array) curr.line).src.getSerialNumber()))));
				curr.inSet.add(("Temp_" + (Integer.toString(((IRcommand_New_Array) curr.line).dst.getSerialNumber()))));
				if (curr.inFunc) {
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_New_Array) curr.line).src.getSerialNumber()))));
					curr.FuncScope.add(("Temp_" + (Integer.toString(((IRcommand_New_Array) curr.line).dst.getSerialNumber()))));
				}
			}
			if (curr.father != null) {
				curr.father.inFunc = curr.inFunc;
				curr.father.FuncScope = curr.FuncScope;
			}
			System.out.println(curr.line.getClass());
			System.out.println(curr.inSet);
			curr = curr.father;
		}
	}

	public void K_color() {
		Stack<node> stack = new Stack<node>();// here we put the nodes we delete
		colorGraph g = new colorGraph(this.head);
		// simplify stage
		Iterator<node> simplfy = g.overall.iterator();
		while (simplfy.hasNext()) {
			node curr = simplfy.next();
			if (curr.activeNeig < 10) {
				g.valid.remove(curr);
				stack.push(curr);
				Iterator<node> r = g.overall.iterator();
				while (r.hasNext()) {
					node removing = r.next();
					if (removing.neig.contains(curr.name)) {
						removing.activeNeig--;
					}
				}
			}
		}
		if (!g.isEmpty()) {
			System.out.println("AAAAAAAAAAAAAAAAA");
		} // need to scream something because we failed to get an empty graph
		// now the coloring:
		while (!stack.isEmpty()) {
			node toAdd = stack.pop();
			g.valid.add(toAdd); // now he is back to graph
			HashSet<String> col = new HashSet<String>();
			col.add("0");
			col.add("1");
			col.add("2");
			col.add("3");
			col.add("4");
			col.add("5");
			col.add("6");
			col.add("7");
			col.add("8");
			Iterator<String> n = toAdd.neig.iterator();
			while (n.hasNext()) {
				String neigName = n.next();
				node actualNeig = g.find(neigName);
				if (actualNeig != null) {
					col.remove(actualNeig.paint); // becuase we can't choose that color anymore
				}
			}
			toAdd.paint = col.iterator().next();
		}
		// if we reach here the stack is empty and everyhting has color now
		// also, everyone are in the valid list
		Iterator<node> lastOne = g.valid.iterator();
		while (lastOne.hasNext()) {
			node curr = lastOne.next();
			this.IRtoMIPS.put(curr.name, curr.paint);
		}

		this.IRtoMIPS.put("dead", "9");
		changeIR();
	}

	public void changeIR() {
		IRcommand h = IR.getInstance().head;
		IRcommandList t = IR.getInstance().tail;
		while (t != null) {
			if (h.IRname.equals("IRcommand_Binop")) {
				String theNum;
				if (((IRcommand_Binop) h).dst.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Binop) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Binop) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_Binop) h).dst.changed = true;
				}
				if (((IRcommand_Binop) h).t1.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Binop) h).t1.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Binop) h).t1.serial = Integer.parseInt(theNum);
					((IRcommand_Binop) h).t1.changed = true;
				}
				if (((IRcommand_Binop) h).t2.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Binop) h).t2.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Binop) h).t2.serial = Integer.parseInt(theNum);
					((IRcommand_Binop) h).t2.changed = true;
				}
			}
			if (h.IRname.equals("IRcommand_Assign")) {
				if (((IRcommand_Assign) h).dst.changed == false) {
					String theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Assign) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Assign) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_Assign) h).dst.changed = true;
				}
			}
			if (h.IRname.equals("IRcommand_Two_Temps")) {
				String theNum;
				if (((IRcommand_Two_Temps) h).dst.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Two_Temps) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Two_Temps) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_Two_Temps) h).dst.changed = true;
				}
				if (((IRcommand_Two_Temps) h).val.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Two_Temps) h).val.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Two_Temps) h).val.serial = Integer.parseInt(theNum);
					((IRcommand_Two_Temps) h).val.changed = true;
				}

			}
			if (h.getClass().toString().equals("class IR.IRcommand_Store_Local")) {
				if (((IRcommand_Store_Local) h).src.changed == false) {
					String theNum = IRtoMIPS
							.get(("Temp_" + (Integer.toString(((IRcommand_Store_Local) h).src.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Store_Local) h).src.serial = Integer.parseInt(theNum);
					((IRcommand_Store_Local) h).src.changed = true;
				}

			}
			if (h.getClass().toString().equals("class IR.IRcommand_Call_Func")) {
				String theNum;
				if (((IRcommand_Call_Func) h).t.changed == false) {
					theNum = this.IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Call_Func) h).t.getSerialNumber()))));
					if (theNum == null) {
						theNum = this.IRtoMIPS.get("dead");
					}
					((IRcommand_Call_Func) h).t.serial = Integer.parseInt(theNum);
					((IRcommand_Call_Func) h).t.changed = true;
				}
				TEMP x;
				TEMP_LIST y = ((IRcommand_Call_Func) h).tempList;
				while (y != null) {
					x = y.head;
					if (x.changed == false) {
						theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(x.getSerialNumber()))));
						if (theNum == null) {
							theNum = IRtoMIPS.get("dead");
						}
						x.serial = Integer.parseInt(theNum);
						x.changed = true;
					}
					y = y.tail;

				}
			}

			if (h.IRname.equals("class IR.IRcommand_Conditional_Jump")) {
				if ((h.getClass().toString().equals("class IR.IRcommand_Jump_bnez"))
						|| (h.getClass().toString().equals("class IR.IRcommand_Jump_beqz"))) {
					if (((IRcommand_Conditional_Jump) h).oprnd1.changed == false) {
						String theNum = IRtoMIPS
								.get(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) h).oprnd1.getSerialNumber()))));
						if (theNum == null) {
							theNum = IRtoMIPS.get("dead");
						}
						((IRcommand_Conditional_Jump) h).oprnd1.serial = Integer.parseInt(theNum);
						((IRcommand_Conditional_Jump) h).oprnd1.changed = true;
					}
				} else if ((h.getClass().toString().equals("class IR.IRcommand_Jump_Label"))) {
				} else {
					String theNum;
					if (((IRcommand_Conditional_Jump) h).oprnd1.changed == false) {
						theNum = IRtoMIPS
								.get(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) h).oprnd1.getSerialNumber()))));
						if (theNum == null) {
							theNum = IRtoMIPS.get("dead");
						}
						((IRcommand_Conditional_Jump) h).oprnd1.serial = Integer.parseInt(theNum);
						((IRcommand_Conditional_Jump) h).oprnd1.changed = true;
					}
					if (((IRcommand_Conditional_Jump) h).oprnd2.changed == false) {
						theNum = IRtoMIPS
								.get(("Temp_" + (Integer.toString(((IRcommand_Conditional_Jump) h).oprnd2.getSerialNumber()))));
						if (theNum == null) {
							theNum = IRtoMIPS.get("dead");
						}
						((IRcommand_Conditional_Jump) h).oprnd2.serial = Integer.parseInt(theNum);
						((IRcommand_Conditional_Jump) h).oprnd2.changed = true;

					}

				}

			}
			if (h.getClass().toString().equals("class IR.IRcommand_array_set")) {
				String theNum;
				if (((IRcommand_array_set) h).array.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_array_set) h).array.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_array_set) h).array.serial = Integer.parseInt(theNum);
					((IRcommand_array_set) h).array.changed = true;
				}
				if (((IRcommand_array_set) h).index.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_array_set) h).index.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_array_set) h).index.serial = Integer.parseInt(theNum);
					((IRcommand_array_set) h).index.changed = true;
				}
				if (((IRcommand_array_set) h).val.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_array_set) h).val.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_array_set) h).val.serial = Integer.parseInt(theNum);
					((IRcommand_array_set) h).val.changed = true;
				}

			}
			if (h.getClass().toString().equals("class IR.IRcommand_Array_Access")) {
				String theNum;
				if (((IRcommand_Array_Access) h).dst.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Array_Access) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Array_Access) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_Array_Access) h).dst.changed = true;
				}
				if (((IRcommand_Array_Access) h).t1.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Array_Access) h).t1.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Array_Access) h).t1.serial = Integer.parseInt(theNum);
					((IRcommand_Array_Access) h).t1.changed = true;
				}
				if (((IRcommand_Array_Access) h).t2.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Array_Access) h).t2.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Array_Access) h).t2.serial = Integer.parseInt(theNum);
					((IRcommand_Array_Access) h).t2.changed = true;
				}

			}
			if (h.getClass().toString().equals("class IR.IRcommand_Load_Local")) {
				if (((IRcommand_Load_Local) h).dst.changed == false) {
					String theNum = IRtoMIPS
							.get(("Temp_" + (Integer.toString(((IRcommand_Load_Local) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Load_Local) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_Load_Local) h).dst.changed = true;
				}
			}
			if (h.getClass().toString().equals("class IR.IRcommand_Field_Access")) {
				String theNum;
				if (((IRcommand_Field_Access) h).dst.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Field_Access) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Field_Access) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_Field_Access) h).dst.changed = true;
				}
				if (((IRcommand_Field_Access) h).src.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_Field_Access) h).src.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Field_Access) h).src.serial = Integer.parseInt(theNum);
					((IRcommand_Field_Access) h).src.changed = true;
				}
			}

			if (h.getClass().toString().equals("class IR.IRcommand_field_set")) {
				String theNum;
				if (((IRcommand_field_set) h).dst.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_field_set) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_field_set) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_field_set) h).dst.changed = true;
				}
				if (((IRcommand_field_set) h).val.changed == false) {
					theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_field_set) h).val.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_field_set) h).val.serial = Integer.parseInt(theNum);
					((IRcommand_field_set) h).val.changed = true;
				}
			}
			if (h.getClass().toString().equals("class IR.IRcommand_store_field")) {
				if (((IRcommand_store_field) h).val.changed == false) {
					String theNum = IRtoMIPS
							.get(("Temp_" + (Integer.toString(((IRcommand_store_field) h).val.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_store_field) h).val.serial = Integer.parseInt(theNum);
					((IRcommand_store_field) h).val.changed = true;
				}
			}
			if (h.getClass().toString().equals("class IR.IRcommand_Return")) {
				if (((IRcommand_Return) h).RetVal != null) {
					if (((IRcommand_Return) h).RetVal.changed == false) {
						String theNum = IRtoMIPS
								.get(("Temp_" + (Integer.toString(((IRcommand_Return) h).RetVal.getSerialNumber()))));
						if (theNum == null) {
							theNum = IRtoMIPS.get("dead");
						}
						((IRcommand_Return) h).RetVal.serial = Integer.parseInt(theNum);
						((IRcommand_Return) h).RetVal.changed = true;
					}
				}

			}
			if (h.getClass().toString().equals("class IR.IRcommand_New_Array")) {
				if (((IRcommand_New_Array) h).dst.changed == false) {
					String theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_New_Array) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_New_Array) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_New_Array) h).dst.changed = true;
				}
				if (((IRcommand_New_Array) h).src.changed == false) {
					String theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(((IRcommand_New_Array) h).src.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_New_Array) h).src.serial = Integer.parseInt(theNum);
					((IRcommand_New_Array) h).src.changed = true;
				}

			}
			if (h.getClass().toString().equals("class IR.IRcommand_Store_Global")) {
				if (((IRcommand_Store_Global) h).dst.changed == false) {
					String theNum = IRtoMIPS
							.get(("Temp_" + (Integer.toString(((IRcommand_Store_Global) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_Store_Global) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_Store_Global) h).dst.changed = true;
				}
			}
			if (h.getClass().toString().equals("class IR.IRcommand_ThisDotField")) {
				if (((IRcommand_ThisDotField) h).dst.changed == false) {
					String theNum = IRtoMIPS
							.get(("Temp_" + (Integer.toString(((IRcommand_ThisDotField) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = IRtoMIPS.get("dead");
					}
					((IRcommand_ThisDotField) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_ThisDotField) h).dst.changed = true;
				}
			}
			if (h.getClass().toString().equals("class IR.IRcommand_virtual_call")) {
				String theNum;
				if (((IRcommand_virtual_call) h).dst.changed == false) {
					theNum = this.IRtoMIPS
							.get(("Temp_" + (Integer.toString(((IRcommand_virtual_call) h).dst.getSerialNumber()))));
					if (theNum == null) {
						theNum = this.IRtoMIPS.get("dead");
					}
					((IRcommand_virtual_call) h).dst.serial = Integer.parseInt(theNum);
					((IRcommand_virtual_call) h).dst.changed = true;
				}
				if (((IRcommand_virtual_call) h).classTemp.changed == false) {
					theNum = this.IRtoMIPS
							.get(("Temp_" + (Integer.toString(((IRcommand_virtual_call) h).classTemp.getSerialNumber()))));
					if (theNum == null) {
						theNum = this.IRtoMIPS.get("dead");
					}
					((IRcommand_virtual_call) h).classTemp.serial = Integer.parseInt(theNum);
					((IRcommand_virtual_call) h).classTemp.changed = true;
				}
				TEMP x;
				TEMP_LIST y = ((IRcommand_virtual_call) h).args;
				while (y != null) {
					x = y.head;
					if (x.changed == false) {
						theNum = IRtoMIPS.get(("Temp_" + (Integer.toString(x.getSerialNumber()))));
						if (theNum == null) {
							theNum = IRtoMIPS.get("dead");
						}
						x.serial = Integer.parseInt(theNum);
						x.changed = true;
					}
					y = y.tail;

				}
			}

			h = t.head;
			t = t.tail;
		}
	}
}