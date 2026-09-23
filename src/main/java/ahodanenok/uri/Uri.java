package ahodanenok.uri;

public interface Uri {

    String getScheme();

    String getUserInfo();

    HostType getHostType();

    String getHost();

    String getPort();

    String getPath();

    String getQuery();

    public enum HostType {
        IP_4,
        IP_6,
        IP_V,
        REGISTERED_NAME;
    }
}
