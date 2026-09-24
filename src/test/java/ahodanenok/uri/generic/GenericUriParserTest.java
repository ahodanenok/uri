package ahodanenok.uri.generic;

import org.junit.jupiter.api.Test;

import ahodanenok.uri.Uri.HostType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericUriParserTest {

    @Test
    public void testParse() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://example.com:8042/over/there?name=ferret#nose");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals("example.com", uri.getHost());
        assertEquals("8042", uri.getPort());
        assertEquals("name=ferret", uri.getQuery());
        assertEquals("nose", uri.getFragment());
    }

    @Test
    public void testParseSchemeEmptyPath() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("test:");
        assertEquals("test", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeRelativePath_OneSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar:abc");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("abc", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeRelativePath_MultipleSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar:x/12/abc");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("x/12/abc", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeRelativePath_OneEmptySegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("data:a//c");
        assertEquals("data", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("a//c", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeRelativePath_MultipleEmptySegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("data:a///b////");
        assertEquals("data", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("a///b////", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeAbsolutePath_NoSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/");
        assertEquals("xyz", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeAbsolutePath_OneSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/foo");
        assertEquals("xyz", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/foo", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeAbsolutePath_MultipleSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/foo/bar/123");
        assertEquals("xyz", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/foo/bar/123", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParseSchemeAbsolutePath_MultipleSegmentsWithEmptySegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("xyz:/foo///bar");
        assertEquals("xyz", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(null, uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/foo///bar", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_hostIP4_GenericAddress() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_hostIP4_WithPathWithoutSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1/");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_hostIP4_WithPathSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://192.168.1.1/test");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("192.168.1.1", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/test", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_hostIP4_MinValue() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://0.0.0.0");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("0.0.0.0", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_hostIP4_MaxValue() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://255.255.255.255");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("255.255.255.255", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_hostIP4_SingleDigits() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://8.8.8.8");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("8.8.8.8", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_hostIP4_Localhost() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("foo://127.0.0.1");
        assertEquals("foo", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_4, uri.getHostType());
        assertEquals("127.0.0.1", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Generic() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://example.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("example.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Empty() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_EmptyWithPaty() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar:///path");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/path", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_OneChar() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a/b");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/b", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Dots() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://...");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("...", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_WithPathNoSegments() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://example.com/");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("example.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_WithPathSegment() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://example.com/data");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("example.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/data", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_SubDomains() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://www.sub.domain.example.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("www.sub.domain.example.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_ManySubDomains() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a.b.c.d.e.f.g.h.i.j");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a.b.c.d.e.f.g.h.i.j", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Localhost() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://localhost");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("localhost", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Digits() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://1234567890.xyz");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("1234567890.xyz", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_UnderscoreHyphen() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://ab-cd_ef.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("ab-cd_ef.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Tilde() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://~my~site~.org");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("~my~site~.org", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_$() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://hello$world.net");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("hello$world.net", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Ampersand() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://shop&go.store");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("shop&go.store", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Asterisk() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://user*name.biz");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("user*name.biz", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Plus() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://price+list.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("price+list.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Comma() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a,b,c.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a,b,c.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Semicolon() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://a;b;c.org");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("a;b;c.org", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_Equal() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://equal=name.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("equal=name.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_RegisteredName_PercentEncodedAscii() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("bar://site%20n%3F%2Fame.com");
        assertEquals("bar", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("site n?/ame.com", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
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
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("xn--d1acufc.xn--e1aybc", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_IPv6_Full() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("ip://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]");
        assertEquals("ip", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_6, uri.getHostType());
        assertEquals("FEDC:BA98:7654:3210:FEDC:BA98:7654:3210", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_IPv6_FullZeroes() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("ip://[0:0:0:0:0:0:0:0]");
        assertEquals("ip", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_6, uri.getHostType());
        assertEquals("0:0:0:0:0:0:0:0", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_IPvFuture() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("http://[v1.fe80::a+en1]/path");
        assertEquals("http", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.IP_V, uri.getHostType());
        assertEquals("v1.fe80::a+en1", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("/path", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_EmptyPort() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("p://site:");
        assertEquals("p", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("site", uri.getHost());
        assertEquals("", uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_EmptyPortWithPath() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("p://site:/data");
        assertEquals("p", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("site", uri.getHost());
        assertEquals("", uri.getPort());
        assertEquals("/data", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_Port() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("p://site:123");
        assertEquals("p", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("site", uri.getHost());
        assertEquals("123", uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_LongPort() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("p://site:9999999999999999999999999999999999999999999999");
        assertEquals("p", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("site", uri.getHost());
        assertEquals("9999999999999999999999999999999999999999999999", uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_PortWithPath() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("p://site:123/data");
        assertEquals("p", uri.getScheme());
        assertEquals(null, uri.getUserInfo());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("site", uri.getHost());
        assertEquals("123", uri.getPort());
        assertEquals("/data", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_UserInfo() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("data://admin@localhost");
        assertEquals("data", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("admin", uri.getUserInfo());
        assertEquals("localhost", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_UserInfoWithPassword() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("data://admin:123456@localhost");
        assertEquals("data", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals("admin:123456", uri.getUserInfo());
        assertEquals("localhost", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_Query_Empty() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("q://foo?");
        assertEquals("q", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals(null, uri.getUserInfo());
        assertEquals("foo", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals("", uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_Query_Slash() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("q://foo?redirect=/user/profile");
        assertEquals("q", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals(null, uri.getUserInfo());
        assertEquals("foo", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals("redirect=/user/profile", uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_Query_Question() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("q://foo?what?where?who?");
        assertEquals("q", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals(null, uri.getUserInfo());
        assertEquals("foo", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals("what?where?who?", uri.getQuery());
        assertEquals(null, uri.getFragment());
    }

    @Test
    public void testParse_Fragment_Empty() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("f://bar#");
        assertEquals("f", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals(null, uri.getUserInfo());
        assertEquals("bar", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals("", uri.getFragment());
    }

    @Test
    public void testParse_Fragment_Slash() {
        GenericUriParser parser = new GenericUriParser();
        GenericUri uri = parser.parse("f://bar#/user/profile");
        assertEquals("f", uri.getScheme());
        assertEquals(HostType.REGISTERED_NAME, uri.getHostType());
        assertEquals(null, uri.getUserInfo());
        assertEquals("bar", uri.getHost());
        assertEquals(null, uri.getPort());
        assertEquals("", uri.getPath());
        assertEquals(null, uri.getQuery());
        assertEquals("/user/profile", uri.getFragment());
    }

    // todo
    // @Test
    // public void testParse_Fragment_Hash() {
    //     GenericUriParser parser = new GenericUriParser();
    //     GenericUri uri = parser.parse("f://bar#a#b#c");
    // }
}
