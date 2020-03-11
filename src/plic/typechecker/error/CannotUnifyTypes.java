package plic.typechecker.error;

import plic.core.DeclarationNode;

public class CannotUnifyTypes extends TypeError {
    private DeclarationNode.Type t1;
    private DeclarationNode.Type t2;

    public CannotUnifyTypes(DeclarationNode.Type ty1, DeclarationNode.Type ty2) {
        this.t1 = ty1;
        this.t2 = ty2;
    }

    @Override
    public String toString() {
        return "Cannot unify type '" + t1.toString() + "' and type '" + t2.toString() + "'";
    }
}
