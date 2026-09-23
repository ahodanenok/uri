package ahodanenok.uri.generic;

import ahodanenok.uri.Uri;

public final class GenericUri implements Uri {

    private final String scheme;
    private final String userInfo;
    private final HostType hostType;
    private final String host;
    private final String port;
    private final String path;
    private final String query;

    // todo: how to create an uri from components with validation?
    public GenericUri(
            String scheme,
            String userInfo,
            HostType hostType,
            String host,
            String port,
            String path,
            String query) {
        this.scheme = scheme;
        this.userInfo = userInfo;
        this.hostType = hostType;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public String getUserInfo() {
        return userInfo;
    }

    @Override
    public HostType getHostType() {
        return hostType;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getQuery() {
        return query;
    }

    // todo: a method here for creating string representation or a separate class?
}
