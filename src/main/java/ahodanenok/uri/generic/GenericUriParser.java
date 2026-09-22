package ahodanenok.uri.generic;

import ahodanenok.uri.Uri.HostType;
import ahodanenok.uri.UriParser;
import ahodanenok.uri.UriParseException;

public final class GenericUriParser implements UriParser<GenericUri> {


    @Override
    public GenericUri parse(String uri) {
        ParseState state = new ParseState(uri);
        String scheme = readScheme(state);
        expect(':', state);
        HierarchyPart hierarchyPart = readHierarchyPart(state);
        Host host = hierarchyPart.host();


        return new GenericUri(
            scheme,
            hierarchyPart.userInfo(),
            host != null ? host.type() : null,
            host != null ? host.value() : null,
            hierarchyPart.port(),
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

    //  hier-part  = "//" authority path-abempty
    //               / path-absolute
    //               / path-rootless
    //               / path-empty
    private HierarchyPart readHierarchyPart(ParseState state) {
        if (peek(0, state) == '/') {
            if (peek(1, state) == '/') {
                // authority   = [ userinfo "@" ] host [ ":" port ]
                read(state); // skip /
                read(state); // skip /

                String userInfo = readUserInfo(state);

                Host host = readHost(state);

                // port = *DIGIT
                String port;
                if (peek(state) == ':') {
                    expect(':', state);
                    while (isDigit(peek(state))) {
                        readBuf(state);
                    }
                    port = state.bufToString();
                } else {
                    port = null;
                }

                // path-abempty
                while (peek(state) == '/') {
                    readBuf(state);
                    readSegmentToBuffer(state);
                }

                return new HierarchyPart(userInfo, host, port, state.bufToString());
            } else {
                // path-absolute
                readBuf(state);
                if (isPathChar(state)) {
                    readSegmentToBuffer(state);
                    while (peek(state) == '/') {
                        readBuf(state);
                        readSegmentToBuffer(state);
                    }
                }

                return new HierarchyPart(null, null, null, state.bufToString());
            }
        } else if (isPathChar(state)) {
            // path-rootless
            readSegmentToBuffer(state);
            while (peek(state) == '/') {
                readBuf(state);
                readSegmentToBuffer(state);
            }

            return new HierarchyPart(null, null, null, state.bufToString());
        } else {
            // path-empty
            return new HierarchyPart(null, null, null, "");
        }
    }

    // userinfo    = *( unreserved / pct-encoded / sub-delims / ":" )
    private String readUserInfo(ParseState state) {
        state.mark();
        String userInfo = null;
        while (true) {
            int ch = peek(state);
            if (isUnreserved(ch)
                    || isSubDelimiter(ch)
                    || ch == ':') {
                readBuf(state);
            } else if (isPercentEncoded(state)) {
                readPercentEncodedBuf(state);
            } else if (ch == '@') {
                expect('@', state);
                userInfo = state.bufToString();
                break;
            } else {
                break;
            }
        }
        if (userInfo == null) {
            state.rewind();
        }

        return userInfo;
    }

    // host = IP-literal / IPv4address / reg-name
    private Host readHost(ParseState state) {
        int ch = peek(state);
        if (ch == -1) {
            return new Host(HostType.REGISTERED_NAME, "");
        }

        // IP-literal  = "[" ( IPv6address / IPvFuture  ) "]"
        if (ch == '[') {
            read(state); // skip [
            state.mark();
            Host ip6 = readIpAddressV6(state);
            if (ip6 != null) {
                expect(']', state);
                return ip6;
            }
            ch = peek(state);
            if (ch != 'v' && ch != 'V') {
                state.rewind();
                throw new UriParseException("illegal IPv6 address");
            }


            Host ipv;
            try {
                ipv = readIpAddressFuture(state);
            } catch (UriParseException e) {
                state.rewind();
                throw e;
            }
            expect(']', state);
            return ipv;
        }

        state.mark();
        Host ip4 = readIpAddressV4(state);
        if (ip4 != null) {
            return ip4;
        } else {
            state.rewind();
        }

        return readRegisteredName(state);
    }

    // IPvFuture   = "v" 1*HEXDIG "." 1*( unreserved / sub-delims / ":" )
    private Host readIpAddressFuture(ParseState state) {
        int ch;

        ch = peek(state);
        if (ch == 'v' || ch == 'V') {
            readBuf(state);
        } else {
            throw new UriParseException("illegal IPvFuture address");
        }

        if (!isHexDigit(peek(state))) {
            throw new UriParseException("illegal IPvFuture address");
        }
        readBuf(state);
        expectBuf('.', state);

        ch = peek(state);
        if (!isUnreserved(ch) && !isSubDelimiter(ch) && ch != ':') {
            throw new UriParseException("illegal IPvFuture address");
        }
        readBuf(state);
        while (true) {
            ch = peek(state);
            if (isUnreserved(ch) || isSubDelimiter(ch) || ch == ':') {
                readBuf(state);
            } else {
                break;
            }
        }

        return new Host(HostType.IP_V, state.bufToString());
    }

    // IPv6address =                  6( h16 ":" ) ls32
    //   /                       "::" 5( h16 ":" ) ls32
    //   / [               h16 ] "::" 4( h16 ":" ) ls32
    //   / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
    //   / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
    //   / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
    //   / [ *4( h16 ":" ) h16 ] "::"              ls32
    //   / [ *5( h16 ":" ) h16 ] "::"              h16
    //   / [ *6( h16 ":" ) h16 ] "::"
    private Host readIpAddressV6(ParseState state) {
        // todo: support all ip6 formats
        boolean read = matchIpAddressV6Buf_h16(state)
            && matchBuf(':', state)
            && matchIpAddressV6Buf_h16(state)
            && matchBuf(':', state)
            && matchIpAddressV6Buf_h16(state)
            && matchBuf(':', state)
            && matchIpAddressV6Buf_h16(state)
            && matchBuf(':', state)
            && matchIpAddressV6Buf_h16(state)
            && matchBuf(':', state)
            && matchIpAddressV6Buf_h16(state)
            && matchBuf(':', state)
            && matchIpAddressV6Buf_h16(state)
            && matchBuf(':', state)
            && matchIpAddressV6Buf_h16(state);
        if (!read) {
            return null;
        }

        return new Host(HostType.IP_6, state.bufToString());
    }

    // h16  = 1*4HEXDIG
    //        ; 16 bits of address represented in hexadecimal
    private boolean matchIpAddressV6Buf_h16(ParseState state) {
        if (isHexDigit(peek(state))) {
            readBuf(state);
            if (isHexDigit(peek(state))) {
                readBuf(state);
                if (isHexDigit(peek(state))) {
                    readBuf(state);
                    if (isHexDigit(peek(state))) {
                        readBuf(state);
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    // ls32        = ( h16 ":" h16 ) / IPv4address
    //               ; least-significant 32 bits of address
    private boolean matchIpAddressV6Buf_ls32(ParseState state) {
        // todo: impl
        return false;
    }


    // IPv4address = dec-octet "." dec-octet "." dec-octet "." dec-octet
    private Host readIpAddressV4(ParseState state) {
        boolean read = matchDecimalOctetBuf(state)
            && matchBuf('.', state)
            && matchDecimalOctetBuf(state)
            && matchBuf('.', state)
            && matchDecimalOctetBuf(state)
            && matchBuf('.', state)
            && matchDecimalOctetBuf(state);
        if (!read) {
            return null;
        }

        return new Host(HostType.IP_4, state.bufToString());
    }

    // reg-name = *( unreserved / pct-encoded / sub-delims )
    private Host readRegisteredName(ParseState state) {
        while (true) {
            int ch = peek(state);
            if (isUnreserved(ch) || isSubDelimiter(ch)) {
                readBuf(state);
            } else if (isPercentEncoded(state)) {
                readPercentEncodedBuf(state);
            } else {
                break;
            }
        }

        if (state.buf.length() == 0) {
            return new Host(HostType.REGISTERED_NAME, "");
        }

        return new Host(HostType.REGISTERED_NAME, state.bufToString());
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
                readBuf(state);
                readBuf(state);
                readBuf(state);
                return true;
            } else if (ch2 >= '0' && ch2 <= '4' && isDigit(ch3)) {
                readBuf(state);
                readBuf(state);
                readBuf(state);
                return true;
            }
        } else if (ch1 == '1' && isDigit(ch2) && isDigit(ch3)) {
            readBuf(state);
            readBuf(state);
            readBuf(state);
            return true;
        } else if (ch1 >= '1' && ch1 <= '9' && isDigit(ch2)) {
            readBuf(state);
            readBuf(state);
            return true;
        } else if (isDigit(ch1)) {
            readBuf(state);
            return true;
        }

        return false;
    }

    private void readSegmentToBuffer(ParseState state) {
        while (isPathChar(state)) {
            readPathCharBuf(state);
        }
    }

    private void readPathCharBuf(ParseState state) {
        if (peek(state) == '%') {
            readPercentEncodedBuf(state);
        } else {
            readBuf(state);
        }
    }

    // todo: utf8
    private void readPercentEncodedBuf(ParseState state) {
        expect('%', state);
        int h1 = readHexDigit(state);
        if (h1 == -1) {
            return; // todo: error?
        }

        int h2 = readHexDigit(state);
        if (h2 == -1) {
            return; // todo: error?
        }

        state.buf.append((char) ((h1 << 4) | h2));
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
            case 'A', 'a' -> 10;
            case 'B', 'b' -> 11;
            case 'C', 'c' -> 12;
            case 'D', 'd' -> 13;
            case 'E', 'e' -> 14;
            case 'F', 'f' -> 15;
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

    private void readBuf(ParseState state) {
        if (state.position >= state.input.length()) {
            return;
        }

        state.buf.append((char) state.input.charAt(state.position++));
    }

    private void expect(char ch, ParseState state) {
        if (peek(state) != ch) {
            throw new UriParseException("expected " + ch);
        }

        read(state);
    }

    private void expectBuf(char ch, ParseState state) {
        if (peek(state) != ch) {
            throw new UriParseException("expected " + ch);
        }

        state.buf.append((char) read(state));
    }

    private boolean matchBuf(char ch, ParseState state) {
        if (peek(state) == ch) {
            readBuf(state);
            return true;
        } else {
            return false;
        }
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
            || (ch >= 'A' && ch <= 'F')
            || (ch >= 'a' && ch <= 'f');
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
        Host host,
        String port,
        String path
    ) {}

    private record Host(
        HostType type,
        String value
    ) {}
}
