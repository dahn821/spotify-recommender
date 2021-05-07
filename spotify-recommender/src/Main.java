import com.wrapper.spotify.SpotifyApi;

import java.io.IOException;
import java.util.Scanner;
import auth.Authorization;

public class Main {
	static String username;
	static SpotifyApi spotifyApi;
	
    public static void ask() {
        System.out.println("What recommendation would you like?");
        System.out.println("Enter 1 for Songs, 2 for Artists, 3 for Playlists");
        Scanner in = new Scanner(System.in);
        int desiredRec = Integer.parseInt(in.next());
                           
        if (desiredRec == 1) {
            
        } else if (desiredRec == 2) {
            
        } else if (desiredRec == 3) {
        	System.out.println("*All inputs are case sensitive*");
            System.out.println("What is your Spotify username/ID");
            in.nextLine();
            username = in.nextLine(); 
            
            
    			System.out.println("Do you want a recommendation from the current top feature playlists or would you like to choose a genre; type g for genre or f for featured");
    			String type = in.nextLine(); 
    		PlaylistSimilarity.getPlaylist(spotifyApi, username, type);                  
        }
        
                           
    }
    public static void main(String[] args) {
        // links user's account
        Authorization authorizer = new Authorization();
        System.out.println(authorizer.getAuthorizationURI());
        String code = authorizer.handleRedirectUri();
        spotifyApi = authorizer.apiFromCode(code);
        String url_open = authorizer.getAuthorizationURI();
        try {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // recommendation process
        ask();
    }
}
