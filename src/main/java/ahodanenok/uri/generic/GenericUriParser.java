package ahodanenok.uri.generic;

import ahodanenok.uri.UriParser;
import ahodanenok.uri.UriParseException;

public final class GenericUriParser implements UriParser<GenericUri> {


    @Override
    public GenericUri parse(String uri) {
        ParseState state = new ParseState(uri);
        String scheme = readScheme(state);

        return new GenericUri(scheme);
    }

    // scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    private String readScheme(ParseState state) {
        if (peek(state) == -1) {
            throw new UriParseException("eof");
        }

        String scheme = "";
        int ch = read(state);
        if (!isAlpha(ch)) {
            throw new UriParseException("not alpha");
        }
        scheme += (char) ch;

        while ((ch = peek(state)) != -1 && ch != ':') {
            ch = read(state);
            if (isAlpha(ch) || isDigit(ch) || ch == '+' || ch == '-' || ch == '.') {
                scheme += (char) ch;
            } else {
                throw new UriParseException("unexpected char");
            }
        }

        return scheme;
    }

    private int peek(ParseState state) {
        if (state.position >= state.input.length()) {
            return -1;
        }

        return state.input.charAt(state.position);
    }

    private int read(ParseState state) {
        if (state.position >= state.input.length()) {
            return -1;
        }

        return state.input.charAt(state.position++);
    }

    // ALPHA =  %x41-5A / %x61-7A   ; A-Z / a-z
    private boolean isAlpha(int ch) {
        return (ch >= 'a' && ch <= 'z')
            || (ch >= 'A' && ch <= 'Z');
    }

    // DIGIT =  %x30-39 ; 0-9
    private boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }

    private static class ParseState {

        private final String input;
        private int position;

        ParseState(String input) {
            this.input = input;
            this.position = 0;
        }
    }
}
