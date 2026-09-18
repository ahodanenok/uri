package ahodanenok.uri.generic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericUriParserTest {

    //@Test
    public void testParse() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://example.com:8042/over/there?name=ferret#nose");
        assertEquals("foo", uri.getScheme());
    }

    @Test
    public void testParseSchemeEmptyPath() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("test:");
        assertEquals("test", uri.getScheme());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParseSchemeRelativePath_OneSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar:abc");
        assertEquals("bar", uri.getScheme());
        assertEquals("abc", uri.getPath());
    }

    @Test
    public void testParseSchemeRelativePath_MultipleSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar:x/12/abc");
        assertEquals("bar", uri.getScheme());
        assertEquals("x/12/abc", uri.getPath());
    }

    @Test
    public void testParseSchemeRelativePath_OneEmptySegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("data:a//c");
        assertEquals("data", uri.getScheme());
        assertEquals("a//c", uri.getPath());
    }

    @Test
    public void testParseSchemeRelativePath_MultipleEmptySegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("data:a///b////");
        assertEquals("data", uri.getScheme());
        assertEquals("a///b////", uri.getPath());
    }

    @Test
    public void testParseSchemeAbsolutePath_NoSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/");
        assertEquals("xyz", uri.getScheme());
        assertEquals("/", uri.getPath());
    }

    @Test
    public void testParseSchemeAbsolutePath_OneSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/foo");
        assertEquals("xyz", uri.getScheme());
        assertEquals("/foo", uri.getPath());
    }

    @Test
    public void testParseSchemeAbsolutePath_MultipleSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/foo/bar/123");
        assertEquals("xyz", uri.getScheme());
        assertEquals("/foo/bar/123", uri.getPath());
    }

    @Test
    public void testParseSchemeAbsolutePath_MultipleSegmentsWithEmptySegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/foo///bar");
        assertEquals("xyz", uri.getScheme());
        assertEquals("/foo///bar", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_001() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1");
        assertEquals("foo", uri.getScheme());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_002() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1/");
        assertEquals("foo", uri.getScheme());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals("/", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_003() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1/test");
        assertEquals("foo", uri.getScheme());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals("/test", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_004() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://0.0.0.0");
        assertEquals("foo", uri.getScheme());
        assertEquals("0.0.0.0", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_005() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://8.8.8.8");
        assertEquals("foo", uri.getScheme());
        assertEquals("8.8.8.8", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_006() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://127.0.0.1");
        assertEquals("foo", uri.getScheme());
        assertEquals("127.0.0.1", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_007() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://255.255.255.255");
        assertEquals("foo", uri.getScheme());
        assertEquals("255.255.255.255", uri.getHost());
        assertEquals("", uri.getPath());
    }
}
