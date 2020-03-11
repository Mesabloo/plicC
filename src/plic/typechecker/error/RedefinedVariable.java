package plic.typechecker.error;

public class RedefinedVariable extends TypeError {
    private String name;

    public RedefinedVariable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Variable '" + this.name + "' is already defined";
    }
}
