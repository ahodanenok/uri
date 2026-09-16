package ahodanenok.uri.generic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericUriParserTest {

    @Test
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
}
