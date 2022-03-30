package AST;

public class AST_BINOP extends AST_Node {

    int number;

    public AST_BINOP(int number) {
        this.number = number;

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

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", s));
    }

}