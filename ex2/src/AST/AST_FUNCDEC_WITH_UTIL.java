package AST;

public class AST_FUNCDEC_WITH_UTIL extends AST_FUNCDEC {
      public AST_TYPE t;
      public String id;
      public AST_FUNCDEC_UTIL1 f;
      public AST_STMT_LIST list;

      /*******************/
      /* CONSTRUCTOR(S) */
      /*******************/
      public AST_FUNCDEC_WITH_UTIL(AST_TYPE t, String id, AST_FUNCDEC_UTIL1 f, AST_STMT_LIST list) {
            this.t = t;
            this.id = id;
            this.list = list;
            this.f = f;
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
            System.out.print(String.format("AST %s NODE\n", "FUNCDEC_WITH_UTIL"));

            /**************************************/
            /* RECURSIVELY PRINT non-null(!) sons (list, t and right...) */
            /**************************************/
            if (t != null)
                  t.PrintMe();
            if (f != null)
                  f.PrintMe();
            if (list != null)
                  list.PrintMe();
            /***************************************/
            /* PRINT Node to AST GRAPHVIZ DOT file */
            /* print node name and optional string (maybe only needed in binop nodes) */
            /***************************************/
            AST_GRAPHVIZ.getInstance().logNode(SerialNumber,
                        String.format("FUNCDEC_WITH_UTIL(%s)\n param_type, func_name", id));

            /****************************************/
            /* PRINT Edges to AST GRAPHVIZ DOT file */
            /*
             * Print Edges to every son!
             */
            /****************************************/
            if (t != null)
                  AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);
            if (f != null)
                  AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, f.SerialNumber);
            if (list != null)
                  AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
      }
}