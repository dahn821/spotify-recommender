import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.hc.core5.http.ParseException;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Category;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfCategoriesRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

import auth.Authorization;
public class APITest {
	static String accessToken;
	static String username;

// // For all requests an access token is needed
//    SpotifyApi spotifyApi = new SpotifyApi.Builder()
//            .setAccessToken("taHZ2SdB-bPA3FsK3D7ZN5npZS47cMy-IEySVEGttOhXmqaVAIo0ESvTCLjLBifhHOHOIuhFUKPW1WMDP7w6dj3MAZdWT8CLI2MkZaXbYLTeoDvXesf2eeiLYPBGdx8tIwQJKgV8XdnzH_DONk")
//            .build();
//
//    // Create a request object with the optional parameter "market"
//    final GetSomethingRequest getSomethingRequest = spotifyApi.getSomething("qKRpDADUKrFeKhFHDMdfcu")
//            .market(CountryCode.SE)
//            .build();
//
//    void getSomething_Sync() {
//      try {
//        // Execute the request synchronous
//        final Something something = getSomethingRequest.execute();
//
//        // Print something's name
//        System.out.println("Name: " + something.getName());
//      } catch (Exception e) {
//        System.out.println("Something went wrong!\n" + e.getMessage());
//      }
//    }
//
//    void getSomething_Async() {
//      try {
//        // Execute the request asynchronous
//        final Future<Something> somethingFuture = getSomethingRequest.executeAsync();
//
//        // Do other things...
//
//        // Wait for the request to complete
//        final Something something = somethingFuture.get();
//
//        // Print something's name
//        System.out.println("Name: " + something.getName());
//      } catch (Exception e) {
//        System.out.println("Something went wrong!\n" + e.getMessage());
//      }
//    }
    public static void main(String[] args) {
    	SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken("BQBLxC95lu8zEIv8rQjEhl2pOnTjZObqpGOV0xrXsotWEpapCB6qBEUuyHcRhcMKlc435xo0FGflm36xvnPepjoyfjm0Qf9yQBV6BaEu0Iaf7h1h0j23DjxaAZhLJFkWz2uCNWLoKnZ06aLcSgxkNT4QXLErCpGowV4z")
                .build();
//    	Authorization authorizer = new Authorization();
//    	System.out.println(authorizer.getAuthorizationURI());
//        String code = authorizer.handleRedirectUri();
//        SpotifyApi spotifyApi = authorizer.apiFromCode(code);
//        accessToken = spotifyApi.getAccessToken();
//        System.out.println(accessToken);
//        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
//              .limit(10)
//              .offset(0)
//              .time_range("short_term")
//              .build();
//        try {
//            Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
//            for (Artist artist : artistPaging.getItems()) {
//                System.out.println(artist.toString());
//            }
//        } catch (ParseException | SpotifyWebApiException | IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        
    	Scanner myObj = new Scanner(System.in);  // Create a Scanner object
    	System.out.println("*All inputs are case sensitive*");
        System.out.println("What is your Spotify username/ID");
        username = myObj.nextLine(); 
        
		    
		    System.out.println("Do you want a recommendation from the current top feature playlists or would you like to choose a genre; type g for genre or f for featured");
		    String type = myObj.nextLine(); 
		    
		 PlaylistSimilarity.getPlaylist(spotifyApi, username, type);
    }
}

    
    

