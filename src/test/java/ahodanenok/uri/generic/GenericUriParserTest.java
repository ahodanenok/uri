package ahodanenok.uri.generic;

import org.junit.jupiter.api.Test;

import ahodanenok.uri.Uri.HostType;

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
    public void testParse_hostIP4_GenericAddress() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1");
        assertEquals("foo", uri.getScheme());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_WithPathWithoutSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1/");
        assertEquals("foo", uri.getScheme());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals("/", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_WithPathSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1/test");
        assertEquals("foo", uri.getScheme());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals("/test", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_MinValue() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://0.0.0.0");
        assertEquals("foo", uri.getScheme());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("0.0.0.0", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_MaxValue() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://255.255.255.255");
        assertEquals("foo", uri.getScheme());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("255.255.255.255", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_SingleDigits() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://8.8.8.8");
        assertEquals("foo", uri.getScheme());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("8.8.8.8", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_hostIP4_Localhost() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://127.0.0.1");
        assertEquals("foo", uri.getScheme());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("127.0.0.1", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Generic() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://example.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("example.com", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Empty() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_EmptyWithPaty() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar:///path");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("", uri.getHost());
        assertEquals("/path", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_OneChar() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a/b");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a", uri.getHost());
        assertEquals("/b", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Dots() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://...");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("...", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_WithPathNoSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://example.com/");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("example.com", uri.getHost());
        assertEquals("/", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_WithPathSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://example.com/data");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("example.com", uri.getHost());
        assertEquals("/data", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_SubDomains() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://www.sub.domain.example.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("www.sub.domain.example.com", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_ManySubDomains() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a.b.c.d.e.f.g.h.i.j");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a.b.c.d.e.f.g.h.i.j", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Localhost() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://localhost");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("localhost", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Digits() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://1234567890.xyz");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("1234567890.xyz", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_UnderscoreHyphen() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://ab-cd_ef.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("ab-cd_ef.com", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Tilde() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://~my~site~.org");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("~my~site~.org", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_$() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://hello$world.net");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("hello$world.net", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Ampersand() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://shop&go.store");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("shop&go.store", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Asterisk() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://user*name.biz");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("user*name.biz", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Plus() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://price+list.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("price+list.com", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Comma() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a,b,c.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a,b,c.com", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Semicolon() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a;b;c.org");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a;b;c.org", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_Equal() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://equal=name.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("equal=name.com", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_RegisteredName_PercentEncodedAscii() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://site%20n%3F%2Fame.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("site n?/ame.com", uri.getHost());
        assertEquals("", uri.getPath());
    }

    // @Test
    // public void testParse_RegisteredName_PercentEncodedUtf8() {
    //     GenericUriParser parser = new GenericUriParser();
    //     GenericUri uri = parser.parse("bar://%d1%82%d0%b5%d1%81%d1%82");
    //     assertEquals("bar", uri.getScheme());
    //     assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
    //     assertEquals("тест", uri.getHost());
    //     assertEquals("", uri.getPath());
    // }

    @Test
    public void testParse_RegisteredName_Punycode() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://xn--d1acufc.xn--e1aybc");
        assertEquals("bar", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("xn--d1acufc.xn--e1aybc", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_IPv6_Full() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("ip://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]");
        assertEquals("ip", uri.getScheme());
        assertEquals(HostType.IP_6, uri.getHostType());
        assertEquals("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_IPv6_FullZeroes() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("ip://[0:0:0:0:0:0:0:0]");
        assertEquals("ip", uri.getScheme());
        assertEquals(HostType.IP_6, uri.getHostType());
        assertEquals("0:0:0:0:0:0:0:0", uri.getHost());
        assertEquals("", uri.getPath());
    }

    @Test
    public void testParse_IPvFuture() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("http://[v1.fe80::a+en1]/path");
        assertEquals("http", uri.getScheme());
        assertEquals(HostType.IP_V, uri.getHostType());
        assertEquals("v1.fe80::a+en1", uri.getHost());
        assertEquals("/path", uri.getPath());
    }
}
