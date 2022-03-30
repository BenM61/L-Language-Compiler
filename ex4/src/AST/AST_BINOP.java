package AST;

import TYPES.*;

import TEMP.*;
import IR.*;

public class AST_BINOP extends AST_Node {

    public AST_EXP left;
    public AST_EXP right;
    public TYPE leftType;
    int number;

    public AST_BINOP(int number, AST_EXP left, AST_EXP right, int line) {
        this.number = number;
        this.left = left;
        this.right = right;
        this.line = line;

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
        System.out.print(String.format("AST %s NODE\n", "AST_BINOP"));

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /* print node name and optional string (maybe only needed in binop nodes) */
        /***************************************/
        String s = "";
        switch (number) {
            case 0:
                s = "+";
            case 1:
                s = "-";
            case 2:
                s = "*";
            case 3:
                s = "/";
            case 4:
                s = ">";
            case 5:
                s = "<";
            case 6:
                s = "=";
        }
        if (left != null)
            left.PrintMe();
        if (right != null)
            right.PrintMe();

        if (left != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, left.SerialNumber);
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", s));
        if (right != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, right.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.format("BINOP - semant me\n", line);

        TYPE t1 = null;
        TYPE t2 = null;

        if (left != null)
            t1 = left.SemantMe();
        if (right != null)
            t2 = right.SemantMe();

        if (left == null || right == null) {
            printError(line);
        }
        this.leftType = t1; //// needed in IRme

        if (number == 6) // case =
        {
            if (type_equals(t1, t2) || type_equals(t2, t1)) // legal
                return TYPE_INT.getInstance();

            if ((t1 == TYPE_INT.getInstance()) || (t2 == TYPE_STRING.getInstance()) ||
                    (t1 == TYPE_STRING.getInstance()) || (t2 == TYPE_INT.getInstance())) // ilegal
            {
                System.out.format(">> ERROR [%d] can't compare primitive to non-primitive", line);
                printError(line);
            }

            if (t1 != TYPE_NIL.getInstance() && t2 != TYPE_NIL.getInstance()) {
                System.out.format(">> ERROR [%d] can't compare different types", line);
                printError(line);
            }

            return TYPE_INT.getInstance(); // legal
        }

        if (number == 0) // case +
        {
            if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance())) // legal (plus)
                return TYPE_INT.getInstance();

            if ((t1 == TYPE_STRING.getInstance()) && (t2 == TYPE_STRING.getInstance())) // legal (concat)
                return TYPE_STRING.getInstance();

            System.out.format(String.format(">> ERROR [%d] trying the + op between wrong types", line));
            printError(line);
        }

        // all other cases
        if ((t1 != TYPE_INT.getInstance()) || (t2 != TYPE_INT.getInstance())) { // ilegal
            System.out.format(">> ERROR [%d] trying binop between wrong types %s %s", line, t1.name, t2.name);
            printError(line);
        }

        // legal (a op b)
        if ((right instanceof AST_EXP_INT) && ((AST_EXP_INT) right).value == 0 && number == 3) {
            System.out.format(">> ERROR [%d] division by 0", line);
            printError(line);
        }
        return TYPE_INT.getInstance();
    }

    public TEMP IRme() {
        System.out.println("BINOP IRme");
        TEMP t1 = null;
        TEMP t2 = null;

        if (left != null)
            t1 = left.IRme();
        if (right != null)
            t2 = right.IRme();

        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

        if (number == 0) // +
        {
            // concat (srtings)
            if (leftType == TYPE_STRING.getInstance()) {
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_Concat_Strings(dst, t1, t2));
            }

            // add (ints)
            else {
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst, t1, t2));
            }
        }
        if (number == 2) // *
        {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst, t1, t2));
        }
        if (number == 6) // =
        {
            if (leftType == TYPE_STRING.getInstance()) {// strings
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst, t1, t2));
            } else
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Contents(dst, t1, t2));

        }

        if (number == 5) // <
        {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(dst, t1, t2));
        }
        if (number == 4) // >
        {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_GT_Integers(dst, t1, t2));
        }
        if (number == 1) // -
        {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_SUB_Integers(dst, t1, t2));
        }
        if (number == 3) // /
        {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_DIV_Integers(dst, t1, t2));
        }

        return dst;
    }

}