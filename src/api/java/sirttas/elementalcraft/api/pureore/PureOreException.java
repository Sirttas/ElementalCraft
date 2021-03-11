package sirttas.elementalcraft.api.pureore;

public class PureOreException extends RuntimeException {

	private static final long serialVersionUID = 2140385988053398117L;

	public PureOreException() {
		super();
	}

	public PureOreException(String message) {
		super(message);
	}

	public PureOreException(String message, Throwable cause) {
		super(message, cause);
	}

	public PureOreException(Throwable cause) {
		super(cause);
	}

}
