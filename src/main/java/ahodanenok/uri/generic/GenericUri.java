package ahodanenok.uri.generic;

import ahodanenok.uri.Uri;

public final class GenericUri implements Uri {

    private final String scheme;
    private final String path;

    // todo: how to create an uri from components with validation?
    public GenericUri(String scheme, String path) {
        this.scheme = scheme;
        this.path = path;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public String getPath() {
        return path;
    }

    // todo: a method here for creating string representation or a separate class?
}
