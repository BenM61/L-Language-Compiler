package AST;

import TEMP.TEMP_LIST;
import TYPES.TYPE_LIST;

public class AST_EXPLIST extends AST_Node {
  /****************/
  /* DATA MEMBERS */
  /****************/
  public AST_EXP head;
  public AST_EXPLIST tail;

  /******************/
  /* CONSTRUCTOR(S) */
  /******************/
  public AST_EXPLIST(AST_EXP head, AST_EXPLIST tail, int line) {
    /******************************/
    /* SET A UNIQUE SERIAL NUMBER */
    /******************************/
    SerialNumber = AST_Node_Serial_Number.getFresh();

    /***************************************/
    /* PRINT CORRESPONDING DERIVATION RULE */
    /***************************************/
    if (tail != null)
      System.out.print("====================== explis -> exp, exps\n");
    if (tail == null)
      System.out.print("====================== explist -> exp      \n");

    /*******************************/
    /* COPY INPUT DATA NENBERS ... */
    /*******************************/
    this.head = head;
    this.tail = tail;
    this.line = line;
  }

  /******************************************************/
  /* The printing message for a statement list AST node */
  /******************************************************/
  public void PrintMe() {
    /**************************************/
    /* AST NODE TYPE = AST STATEMENT LIST */
    /**************************************/
    System.out.print("AST EXPLIST NODE\n");

    /*************************************/
    /* RECURSIVELY PRINT HEAD + TAIL ... */
    /*************************************/
    if (head != null)
      head.PrintMe();
    if (tail != null)
      tail.PrintMe();

    /**********************************/
    /* PRINT to AST GRAPHVIZ DOT file */
    /**********************************/
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "EXPLIST");

    /****************************************/
    /* PRINT Edges to AST GRAPHVIZ DOT file */
    /****************************************/
    if (head != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
    if (tail != null)
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
  }

  public TYPE_LIST SemantMe(int ignore) {
    System.out.println("EXPLIST - semant me");

    if (tail == null) {
      return new TYPE_LIST(head.SemantMe(), null);
    } else {
      return new TYPE_LIST(head.SemantMe(), tail.SemantMe(0));
    }
  }

  public TEMP_LIST IRme(int ignore) {
    System.out.println("EXPLIST - IRme");
    if ((head == null) && (tail == null)) {
      return null;
    } else if (tail == null) {
      return new TEMP_LIST(head.IRme(), null);
    } else {
      return new TEMP_LIST(head.IRme(), tail.IRme(0));
    }
  }
}
