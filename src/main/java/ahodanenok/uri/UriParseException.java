package ahodanenok.uri;

public class UriParseException extends RuntimeException {

    private final int position;

    public UriParseException(int position, String msg) {
        super("position " + position + ": " + msg);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
