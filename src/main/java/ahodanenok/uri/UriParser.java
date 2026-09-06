package ahodanenok.uri;

public interface UriParser<T extends Uri> {

    T parse(String str);
}
