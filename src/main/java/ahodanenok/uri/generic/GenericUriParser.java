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


        return new GenericUri(
            scheme,
            hierarchyPart.host(),
            hierarchyPart.path());
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
            read(state); // skip /
            if (peek(state) == '/') {
                read(state); // skip /
                // authority   = [ userinfo "@" ] host [ ":" port ]
                // userinfo    = *( unreserved / pct-encoded / sub-delims / ":" )
                // host        = IP-literal / IPv4address / reg-name
                String host = readHost(state);
                // IP-literal  = "[" ( IPv6address / IPvFuture  ) "]"
                // IPvFuture   = "v" 1*HEXDIG "." 1*( unreserved / sub-delims / ":" )
                // IPv6address = 6( h16 ":" ) ls32
                //   /                       "::" 5( h16 ":" ) ls32
                //   / [               h16 ] "::" 4( h16 ":" ) ls32
                //   / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
                //   / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
                //   / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
                //   / [ *4( h16 ":" ) h16 ] "::"              ls32
                //   / [ *5( h16 ":" ) h16 ] "::"              h16
                //   / [ *6( h16 ":" ) h16 ] "::"

                // ls32        = ( h16 ":" h16 ) / IPv4address
                //               ; least-significant 32 bits of address

                // h16         = 1*4HEXDIG
                //               ; 16 bits of address represented in hexadecimal
                // IPv4address = dec-octet "." dec-octet "." dec-octet "." dec-octet

                // dec-octet   = DIGIT                 ; 0-9
                //               / %x31-39 DIGIT         ; 10-99
                //               / "1" 2DIGIT            ; 100-199
                //               / "2" %x30-34 DIGIT     ; 200-249
                //               / "25" %x30-35          ; 250-255
                // reg-name    = *( unreserved / pct-encoded / sub-delims )
                // port        = *DIGIT

                // path-abempty
                 while (peek(state) == '/') {
                    state.buf.append((char) read(state));
                    readSegmentToBuffer(state);
                }

                return new HierarchyPart(null, host, null, state.bufToString());
            } else {
                // path-absolute
                state.buf.append('/');
                if (isPathChar(state)) {
                    readSegmentToBuffer(state);
                    while (peek(state) == '/') {
                        state.buf.append((char) read(state));
                        readSegmentToBuffer(state);
                    }
                }

                return new HierarchyPart(null, null, null, state.bufToString());
            }
        } else if (isPathChar(state)) {
            // path-rootless
            readSegmentToBuffer(state);
            while (peek(state) == '/') {
                state.buf.append((char) read(state));
                readSegmentToBuffer(state);
            }

            return new HierarchyPart(null, null, null, state.bufToString());
        } else {
            // path-empty
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

    // host = IP-literal / IPv4address / reg-name
    private String readHost(ParseState state) {
        int ch = peek(state);
        if (ch == -1) {
            throw new UriParseException("host missing");
        }

        if (ch == '[') {
            // todo: impl
            return null;
        }
        if (ch == 'v') {
            // todo: impl
            return null;
        }

        String ip4 = readIpAddressV4(state);
        if (ip4 != null) {
            return ip4;
        }

        String regName = readRegName(state);
        if (regName != null) {
            return regName;
        }

        throw new UriParseException("host missing");
    }

    private String readIpAddressV4(ParseState state) {
        // IPv4address = dec-octet "." dec-octet "." dec-octet "." dec-octet
        state.mark();
        boolean read = matchDecimalOctetBuf(state)
            && matchBuf('.', state)
            && matchDecimalOctetBuf(state)
            && matchBuf('.', state)
            && matchDecimalOctetBuf(state)
            && matchBuf('.', state)
            && matchDecimalOctetBuf(state);
        if (!read) {
            state.rewind();
            return null;
        }

        return state.bufToString();
    }

    private boolean matchBuf(char ch, ParseState state) {
        if (peek(state) == ch) {
            read(state);
            state.buf.append(ch);
            return true;
        } else {
            return false;
        }
    }

    private String readRegName(ParseState state) {
        return null; // todo: impl
    }

    // dec-octet = DIGIT                   ; 0-9
    //             / %x31-39 DIGIT         ; 10-99
    //             / "1" 2DIGIT            ; 100-199
    //             / "2" %x30-34 DIGIT     ; 200-249
    //             / "25" %x30-35          ; 250-255
    private boolean matchDecimalOctetBuf(ParseState state) {
        int ch1 = peek(0, state);
        int ch2 = peek(1, state);
        int ch3 = peek(2, state);
        if (ch1 == '2') {
            if (ch2 == '5' && ch3 >= '0' && ch3 <= '5') {
                state.buf.append((char) read(state));
                state.buf.append((char) read(state));
                state.buf.append((char) read(state));
                return true;
            } else if (ch2 >= '0' && ch2 <= '4' && isDigit(ch3)) {
                state.buf.append((char) read(state));
                state.buf.append((char) read(state));
                state.buf.append((char) read(state));
                return true;
            }
        } else if (ch1 == '1' && isDigit(ch2) && isDigit(ch3)) {
            state.buf.append((char) read(state));
            state.buf.append((char) read(state));
            state.buf.append((char) read(state));
            return true;
        } else if (ch1 >= '1' && ch1 <= '9' && isDigit(ch2)) {
            state.buf.append((char) read(state));
            state.buf.append((char) read(state));
            return true;
        } else if (isDigit(ch1)) {
            state.buf.append((char) read(state));
            return true;
        }

        return false;
    }

    private void readSegmentToBuffer(ParseState state) {
        while (isPathChar(state)) {
            state.buf.append((char) readPathChar(state));
        }
    }

    private int readPathChar(ParseState state) {
        if (peek(state) == '%') {
            return readPercentEncodedChar(state);
        } else {
            return read(state);
        }
    }

    private int readPercentEncodedChar(ParseState state) {
        read(state); // skip %
        int h1 = readHexDigit(state);
        if (h1 == -1) {
            return -1;
        }

        int h2 = readHexDigit(state);
        if (h2 == -1) {
            return -1;
        }

        return (char) ((h1 << 4) | h2);
    }

    private int readHexDigit(ParseState state) {
        return switch (read(state)) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'A' -> 10;
            case 'B' -> 11;
            case 'C' -> 12;
            case 'D' -> 13;
            case 'E' -> 14;
            case 'F' -> 15;
            default -> throw new UriParseException("not a hex digit");
        };
    }

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
        private StringBuilder buf;

        private int markPosition = -1;
        private int markBufLength = -1;

        ParseState(String input) {
            this.input = input;
            this.position = 0;
            this.buf = new StringBuilder();
        }

        String bufToString() {
            String s = buf.toString();
            buf.setLength(0);
            return s;
        }

        void mark() {
            markPosition = position;
            markBufLength = buf.length();
        }

        void rewind() {
            position = markPosition;
            markPosition = -1;
            buf.setLength(markBufLength);
            markBufLength = -1;
        }
    }

    private record HierarchyPart(
        String userInfo,
        String host,
        Integer port,
        String path
    ) {}
}
