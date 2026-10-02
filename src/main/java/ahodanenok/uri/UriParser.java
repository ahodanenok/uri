package ahodanenok.uri;

public interface UriParser<T extends Uri> {

    T parse(String str);

    UriReference<T> parseReference(String str);
}
