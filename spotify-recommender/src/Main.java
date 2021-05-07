import com.wrapper.spotify.SpotifyApi;

import auth.Authorization;

public class Main {
    public static void main(String[] args) {
        Authorization authorizer = new Authorization();
        System.out.println(authorizer.getAuthorizationURI());
        String code = authorizer.handleRedirectUri();
        SpotifyApi spotifyApi = authorizer.apiFromCode(code);
        
        //add user functionality here
    }
}
