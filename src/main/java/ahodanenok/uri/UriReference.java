package ahodanenok.uri;

public interface UriReference<T extends Uri> {

    String getScheme();

    String getUserInfo();

    HostType getHostType();

    String getHost();

    String getPort();

    String getPath();

    String getQuery();

    String getFragment();

    T resolve(T baseUri);
}
