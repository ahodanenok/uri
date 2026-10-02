package ahodanenok.uri.generic;

import org.junit.jupiter.api.Test;

import ahodanenok.uri.HostType;
import ahodanenok.uri.UriParseException;
import ahodanenok.uri.UriReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenericUriParserRefTest {

    @Test
    public void testParse() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference(
            "foo://example.com:8042/over/there?name=ferret#nose");
        assertEquals("foo", ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, ref.getHostType());
        assertEquals("example.com", ref.getHost());
        assertEquals("8042", ref.getPort());
        assertEquals("name=ferret", ref.getQuery());
        assertEquals("nose", ref.getFragment());
    }

    @Test
    public void testParse_Relative_EmptyPath() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("", ref.getPath());
        assertEquals(null, ref.getQuery());
        assertEquals(null, ref.getFragment());
    }

    @Test
    public void testParse_Relative_EmptyPathWithQuery() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("?x=1");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("", ref.getPath());
        assertEquals("x=1", ref.getQuery());
        assertEquals(null, ref.getFragment());
    }

    @Test
    public void testParse_Relative_EmptyPathWithFragment() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("#abc");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("", ref.getPath());
        assertEquals(null, ref.getQuery());
        assertEquals("abc", ref.getFragment());
    }

    @Test
    public void testParse_Relative_RelativePath() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("foo");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("foo", ref.getPath());
        assertEquals(null, ref.getQuery());
        assertEquals(null, ref.getFragment());
    }

    @Test
    public void testParse_Relative_RelativePathWithQuery() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("foo?bar");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("foo", ref.getPath());
        assertEquals("bar", ref.getQuery());
        assertEquals(null, ref.getFragment());
    }

    @Test
    public void testParse_Relative_RelativePathWithFragment() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("foo#bar");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("foo", ref.getPath());
        assertEquals(null, ref.getQuery());
        assertEquals("bar", ref.getFragment());
    }

    @Test
    public void testParse_Relative_AbsolutePathEmpty() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("/");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("/", ref.getPath());
        assertEquals(null, ref.getQuery());
        assertEquals(null, ref.getFragment());
    }

    @Test
    public void testParse_Relative_AbsolutePathEmptyWithSegment() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("/data");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("/data", ref.getPath());
        assertEquals(null, ref.getQuery());
        assertEquals(null, ref.getFragment());
    }

    @Test
    public void testParse_Relative_AbsolutePathEmptyWithSegmentWithQuery() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("/data?page=1");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("/data", ref.getPath());
        assertEquals("page=1", ref.getQuery());
        assertEquals(null, ref.getFragment());
    }

    @Test
    public void testParse_Relative_AbsolutePathEmptyWithSegmentWithFragment() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("/data#section1");
        assertEquals(null, ref.getScheme());
        assertEquals(null, ref.getUserInfo());
        assertEquals(null, ref.getHostType());
        assertEquals(null, ref.getHost());
        assertEquals(null, ref.getPort());
        assertEquals("/data", ref.getPath());
        assertEquals(null, ref.getQuery());
        assertEquals("section1", ref.getFragment());
    }

    @Test
    public void testParse_Relative_Full() {
        GenericUriParser parser = new GenericUriParser();
        UriReference<GenericUri> ref = parser.parseReference("//admin@foo:123/bar?read#p/2");
        assertEquals(null, ref.getScheme());
        assertEquals("admin", ref.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, ref.getHostType());
        assertEquals("foo", ref.getHost());
        assertEquals("123", ref.getPort());
        assertEquals("/bar", ref.getPath());
        assertEquals("read", ref.getQuery());
        assertEquals("p/2", ref.getFragment());
    }
}
