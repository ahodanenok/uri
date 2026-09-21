package ahodanenok.uri;

public interface Uri {

    String getScheme();

    HostType getHostType();

    String getHost();

    String getPath();

    public enum HostType {
        IP_4,
        IP_6,
        IP_V,
        REGISTERED_NAME;
    }
}
