package plic.typechecker.error;

public class UndeclaredVariable extends TypeError {
    private String name;

    public UndeclaredVariable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Variable '" + this.name + "' has not yet been declared";
    }
}
