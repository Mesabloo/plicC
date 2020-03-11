package plic.typechecker.error;

public class TypeCheckerError extends RuntimeException {
    public TypeCheckerError(TypeError underlying) {
        super(underlying.toString());
    }
}
