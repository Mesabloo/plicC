package control.copy;

/**
 * A simple interface to replace the buggy {@link Cloneable}.
 *
 * @param <A> The type to be copiable
 */
public interface Copiable<A> {
    /**
     * @return A copy of the initial class instance
     */
    A copy();
}
