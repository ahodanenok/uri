package ahodanenok.uri.generic;

import ahodanenok.uri.Uri;

public final class GenericUri implements Uri {

    public final String scheme;

    // todo: how to create an uri from components with validation?
    public GenericUri(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    // todo: a method here for creating string representation or a separate class?
}
