package mk.ukim.finki.wp.assertions;

public class AssertionsException extends RuntimeException {

    public AssertionsException(String message, Object expected, Object actual) {
        super(String.format("%s:\texpected: <%s>\tactual:\t<%s>", message, expected.toString(), actual.toString()));
    }
}
