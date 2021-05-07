import com.wrapper.spotify.SpotifyApi;
import java.util.Scanner;
import auth.Authorization;

public class Main {
    
    public void ask() {
        Scanner in = new Scanner(System.in);
        System.out.println("*All inputs are case sensitive*");
        System.out.println("What is your Spotify username/ID");
        String username = in.nextLine();
        
        System.out.println("What recommendation would you like?");
        System.out.println("Enter 1 for Songs, 2 for Artists, 3 for Playlists);
        int desiredRec = Integer.parseInt(in.nextLine());
                           
        if (desiredRec == 1) {
            
        } else if (desiredRec == 2) {
            
        } else if (desiredRec == 3) {
            System.out.println("Do you want a recommendation from the current top feature playlists or would you like to choose a genre?");
            System.out.println("(type g for genre or f for featured)");
		    String type = in.nextLine(); 
            PlaylistSimilarity.getPlaylist(spotifyApi, username, type);                   
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
