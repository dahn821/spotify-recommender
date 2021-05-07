import com.wrapper.spotify.SpotifyApi;
import java.util.Scanner;
import auth.Authorization;

public class Main {
    
    public void ask() {
        System.out.println("What recommendation would you like?");
        System.out.println("Enter 1 for Songs, 2 for Artists, 3 for Playlists);
        Scanner in = new Scanner(System.in);
        int desiredRec = Integer.parseInt(in.next());
                           
        if (desiredRec == 1) {
            
        } else if (desiredRec == 2) {
            
        } else if (desiredRec == 3) {
            getPlaylist(userId);                   
        }
        
                           
    }
    public static void main(String[] args) {
        // links user's account
        Authorization authorizer = new Authorization();
        System.out.println(authorizer.getAuthorizationURI());
        String code = authorizer.handleRedirectUri();
        SpotifyApi spotifyApi = authorizer.apiFromCode(code);
        
        // recommendation process
        ask();
    }
}
