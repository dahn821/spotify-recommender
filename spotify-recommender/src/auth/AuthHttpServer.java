package auth;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
/**
 * taken from https://youtu.be/js5H9UbmmMY
 *
 */
public class AuthHttpServer {

    ServerConfiguration config;

    public AuthHttpServer(ServerConfiguration config) {
        this.config = config;
    }
    
    /**
     * 
     * @return code from the authentication process (PKCE)
     */
    public String start() {
        StringBuilder output = new StringBuilder();
        try {
            CountDownLatch latch = new CountDownLatch(1);
            HttpServer server = HttpServer
                    .create(new InetSocketAddress(config.getHost(), config.getPort()), 0);
            server.createContext(config.getContext(), exchange -> {
                String code = exchange.getRequestURI().toString().split("=")[1];
                output.append(code);
                String response = "";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                latch.countDown();
            });
            server.start();
            System.out.println("waiting for redirect URI");
            latch.await(config.getTimeout(), TimeUnit.SECONDS);
            server.stop(0);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output.toString();
    }
}
