package ahodanenok.uri.generic;

import ahodanenok.uri.HostType;
import ahodanenok.uri.Uri;
import ahodanenok.uri.UriReference;

final class GenericRelativeUriReference implements UriReference<GenericUri> {

    private final String userInfo;
    private final HostType hostType;
    private final String host;
    private final String port;
    private final String path;
    private final String query;
    private final String fragment;

    public GenericRelativeUriReference(
            String userInfo,
            HostType hostType,
            String host,
            String port,
            String path,
            String query,
            String fragment) {
        this.userInfo = userInfo;
        this.hostType = hostType;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
    }

    public String getScheme() {
        return null;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public HostType getHostType() {
        return hostType;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getFragment() {
        return fragment;
    }

    @Override
    public GenericUri resolve(GenericUri uri) {
        return uri; // todo
    }
}
