package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_VARDEC extends AST_Node {
    public AST_TYPE type;
    public String id;
    public String scope; // for IRme

    public void isOverride() {
        String c = SYMBOL_TABLE.getInstance().inClassScope();
        if (c == null)
            return;
        String father = SYMBOL_TABLE.getInstance().findExtendsClass(c);
        if (father == null)
            return;
        TYPE_CLASS fatherClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(father);
        for (AST_ARG_LIST it = fatherClass.data_members; it != null; it = it.tail) {
            if (id.equals(it.head.id) && it.head.t.typeName.equals(type.typeName))
                return;
            if (id.equals(it.head.id)) {
                System.out.println(">> ERROR [" + line + "] cant override the field!");
                printError(line);
            }
        }
        return;
    }
}
