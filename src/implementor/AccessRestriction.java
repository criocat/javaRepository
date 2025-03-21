package info.kgeorgiy.ja.razinkov.implementor;

/**
 * Enum that represents access restrictions of abstract method.
 *
 * @author Razinkov Ivan
 */
public enum AccessRestriction {
    /**
     * uses when method is not abstract
     */
    OVERRIDDEN,
    /**
     * uses when abstract method is public
     */
    PUBLIC,
    /**
     * uses when abstract method is protected
     */
    PROTECTED,
    /**
     * uses when abstract method is package-private
     */
    PACKAGE,
    /**
     * uses when abstract method is private
     */
    PRIVATE;
}
