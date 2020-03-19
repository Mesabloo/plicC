package plic.typechecker.error;

import plic.core.DeclarationNode;
import plic.typechecker.core.Type;

public class CannotUnifyTypes extends TypeError {
    private Type t1;
    private Type t2;

    public CannotUnifyTypes(Type ty1, Type ty2) {
        this.t1 = ty1;
        this.t2 = ty2;
    }

    @Override
    public String toString() {
        return "Cannot unify type '" + t1.toString() + "' and type '" + t2.toString() + "'";
    }
}
