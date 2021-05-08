import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.Scanner;
import auth.Authorization;

public class Main {
	static String username;
	static SpotifyApi spotifyApi;
	
                            
    public static void main(String[] args) {
        // links user's account
        Authorization authorizer = new Authorization();
        System.out.println(authorizer.getAuthorizationURI());
        String code = authorizer.handleRedirectUri();
        spotifyApi = authorizer.apiFromCode(code);
        // recommendation process
        Scanner in = new Scanner(System.in);
        System.out.println("*All inputs are case sensitive*");
        System.out.println("What is your Spotify username/ID");
        username = in.nextLine();
        
        System.out.println("What recommendation would you like?");
        System.out.println("Enter 1 for Songs, 2 for Artists, 3 for Playlists");
        int desiredRec = Integer.parseInt(in.next());
    	Recommender rec = new Recommender(spotifyApi);

        if (desiredRec == 1) {
            System.out.println("How many tracks would you like recommended?");
            int noOfTracks = Integer.parseInt(in.next());
            System.out.println("Track Recommendations: ");
            for (Track track : rec.recommendTracks(noOfTracks)) {
            	System.out.println(track.getName());
	    	}
        } else if (desiredRec == 2) {
            System.out.println("How many artists would you like recommended?");
            int noOfArtists = Integer.parseInt(in.next());
            System.out.println("Artist Recommendations: ");
            for (Artist artist : rec.recommendArtists(noOfArtists)) {
            	System.out.println(artist.getName());
            }
        } else if (desiredRec == 3) {
            System.out.println("Do you want a recommendation from the current top feature playlists or would you like to choose a genre?");
            System.out.println("(type g for genre or f for featured)");
		    String type = in.nextLine(); 
            PlaylistSimilarity.getPlaylist(spotifyApi, username, type);
        }
        in.close();
    }
}
