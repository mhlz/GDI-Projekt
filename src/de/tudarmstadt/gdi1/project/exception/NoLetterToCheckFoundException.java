package de.tudarmstadt.gdi1.project.exception;

/**
 * gets thrown if a Cipher can't find a Letter to check
 */
public class NoLetterToCheckFoundException extends RuntimeException {
	/**
	 * Constructs a new runtime exception with {@code null} as its
	 * detail message.  The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	public NoLetterToCheckFoundException() {
		super();
	}

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 */
	public NoLetterToCheckFoundException(String message) {
		super(message);
	}
}
