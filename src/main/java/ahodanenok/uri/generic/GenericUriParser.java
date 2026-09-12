package ahodanenok.uri.generic;

import ahodanenok.uri.UriParser;
import ahodanenok.uri.UriParseException;

public final class GenericUriParser implements UriParser<GenericUri> {


    @Override
    public GenericUri parse(String uri) {
        ParseState state = new ParseState(uri);
        String scheme = readScheme(state);
        expect(':', state);
        HierarchyPart hierarchyPart = readHierarchyPart(state);


        return new GenericUri(scheme, hierarchyPart.path());
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

    private HierarchyPart readHierarchyPart(ParseState state) {
        if (peek(state) == '/') {
            return new HierarchyPart(null, null, null, "1"); // todo: impl
        } else if (isPathChar(state)) {
            return new HierarchyPart(null, null, null, "2"); // todo: impl
        } else {
            return new HierarchyPart(null, null, null, "");
        }
    }

    // URI         = scheme ":" hier-part [ "?" query ] [ "#" fragment ]

    //   hier-part   = "//" authority path-abempty
    //               / path-absolute
    //               / path-rootless
    //               / path-empty

// path          = path-abempty    ; begins with "/" or is empty
//                     / path-absolute   ; begins with "/" but not "//"
//                     / path-noscheme   ; begins with a non-colon segment
//                     / path-rootless   ; begins with a segment
//                     / path-empty      ; zero characters

//       path-abempty  = *( "/" segment )
//       path-absolute = "/" [ segment-nz *( "/" segment ) ]
//       path-noscheme = segment-nz-nc *( "/" segment )
//       path-rootless = segment-nz *( "/" segment )
//       path-empty    = 0<pchar>
// segment       = *pchar
//       segment-nz    = 1*pchar
//       segment-nz-nc = 1*( unreserved / pct-encoded / sub-delims / "@" )
//                     ; non-zero-length segment without any colon ":"

//       pchar         = unreserved / pct-encoded / sub-delims / ":" / "@"

    private int peek(ParseState state) {
        return peek(0, state);
    }

    private int peek(int n, ParseState state) {
        if (state.position + n >= state.input.length()) {
            return -1;
        }

        return state.input.charAt(state.position + n);
    }

    private int read(ParseState state) {
        if (state.position >= state.input.length()) {
            return -1;
        }

        return state.input.charAt(state.position++);
    }

    private void expect(char ch, ParseState state) {
        if (peek(state) != ch) {
            throw new UriParseException("expected " + ch);
        }

        read(state);
    }

    private boolean isAlpha(int ch) {
        return (ch >= 'a' && ch <= 'z')
            || (ch >= 'A' && ch <= 'Z');
    }

    private boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isHexDigit(int ch) {
        return isDigit(ch)
            || (ch >= 'A' && ch <= 'F');
    }

    private boolean isPathChar(ParseState state) {
        int ch = peek(state);
        return isUnreserved(ch)
            || isPercentEncoded(state)
            || isSubDelimiter(ch)
            || ch == ':'
            || ch == '@';
    }

    private boolean isUnreserved(int ch) {
        return isAlpha(ch)
            || isDigit(ch)
            || ch == '-'
            || ch == '.'
            || ch == '_'
            || ch == '~';
    }

    private boolean isPercentEncoded(ParseState state) {
        return peek(0, state) == '%'
            && isHexDigit(peek(1, state))
            && isHexDigit(peek(2, state));
    }

    private boolean isSubDelimiter(int ch) {
        return ch == '!'
            || ch == '$'
            || ch == '&'
            || ch == '\''
            || ch == '('
            || ch == ')'
            || ch == '*'
            || ch == '+'
            || ch == ','
            || ch == ';'
            || ch == '=';
    }

    private static class ParseState {

        private final String input;
        private int position;

        ParseState(String input) {
            this.input = input;
            this.position = 0;
        }
    }

    private record HierarchyPart(
        String userInfo,
        String host,
        Integer port,
        String path
    ) {}
}
