package auth;

/**
 * with inspiration from https://youtu.be/js5H9UbmmMY. Simply provides server details for redirect.
 * @author nlao
 */
final public class ServerConfiguration {
    
    private String host = "localhost";
    private int port = 8080;
    private String context = "/";
    private String redirectUri = "http://" + host + ":" + port + context;
    private int timeout = 60;
    
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
    public String getContext() {
        return context;
    }
    public String getRedirectUri() {
        return redirectUri;
    }
    public int getTimeout() {
        return timeout;
    }
    
    
}
