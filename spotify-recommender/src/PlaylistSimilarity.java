import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hc.core5.http.ParseException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

import auth.Authorization;


public class PlaylistSimilarity {
	static SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken("BQATY0SRbm9PbIJqGlrwSnm1l_NkI7xU-s-J8tC2VhrZS6Dx-B9ky0cF9VFfkmKoz3SX-X9C9lWkeyYgqt240L9CDg7-MG0elHiuqw_eUxaxqz7NVRRcEWpYpWmv8gwvhr1MfgW5uhhKYKxJocIvpch241DM0oCJOc0U")
            .build();
	
	
    public static void getPlaylist(String userId) {
    	ArrayList<String> featured = new ArrayList<String>(1000);
    	ArrayList<String> featuredId = new ArrayList<String>();
    	for (int i = 0; i < 10; i++) {
    		  featured.add("");
    		}
        GetListOfFeaturedPlaylistsRequest getListOfFeaturedPlaylistsRequest = spotifyApi
        	    .getListOfFeaturedPlaylists()
//        	          .country(CountryCode.SE)
//        	          .limit(10)
//        	          .offset(0)
//        	          .timestamp(new Date(1414054800000L))
        	    .build();
        
        	    try {
        	      FeaturedPlaylists featuredPlaylists = getListOfFeaturedPlaylistsRequest.execute();

        	      Paging<PlaylistSimplified> playlists = featuredPlaylists.getPlaylists();
        	      for(PlaylistSimplified pitems :  playlists.getItems()) {
        	    	  String p = pitems.getId();
        	    	  featuredId.add(p);
        	      }
        	    } catch (IOException | SpotifyWebApiException | ParseException e) {
        	      System.out.println("Error: " + e.getMessage());
        	    }
    	
    	
    
    	HashMap map = new HashMap();
		GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi
	    	    .getListOfUsersPlaylists(userId)
	//    	          .limit(10)
	//    	          .offset(0)
	    	    .build();
	    try {
	          Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
	          for (PlaylistSimplified playlist : playlistSimplifiedPaging.getItems()) {
	        	  map.put(playlist.getName(), playlist.getId());  
	          }
	          System.out.println(map.keySet());
	        } catch (IOException | SpotifyWebApiException | ParseException e) {
	          System.out.println("Error: " + e.getMessage());
	        }
	  	
	    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("choose a playlist");

	    String playlistName = myObj.nextLine();  // Read user input
	    
	    
//		GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
//				.getPlaylistsItems((String) map.get(playlistName))
//	//	          .fields("description")
//	//	          .limit(10)
//	//	          .offset(0)
//	//	          .market(CountryCode.SE)
//	//	          .additionalTypes("track,episode")
//		    .build();
//		    try {
//		      final Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsItemsRequest.execute();
//	
//		      System.out.println("Total: " + playlistTrackPaging.);
//		    } catch (IOException | SpotifyWebApiException | ParseException e) {
//		      System.out.println("Error: " + e.getMessage());
//		    }
		for (int i = 0; i < featuredId.size(); i++) {  
			
		    Map<String, Integer> count = new HashMap<String, Integer>();
			 GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(featuredId.get(i))
	//		          .fields("description")
	//		          .market(CountryCode.SE)
	//		          .additionalTypes("track,episode")
			    .build();
	
			    try {
			      Playlist playlist = getPlaylistRequest.execute();
			      Paging<PlaylistTrack> tracks = playlist.getTracks();
			      String n = "";
			      String curr = "";
			      for(PlaylistTrack t : tracks.getItems()) {
			    	  curr = t.getTrack().getName();
				    	n = n + " " + curr;
			      }
			    	featured.add(i, n);
			    } catch (IOException | SpotifyWebApiException | ParseException e) {
			      System.out.println("Error: " + e.getMessage());
			    }
			    
    }
		System.out.println(featured);
    
		String a = "";
    GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist((String) map.get(playlistName))
    		//		          .fields("description")
    		//		          .market(CountryCode.SE)
    		//		          .additionalTypes("track,episode")
    				    .build();
    		
    				    try {
    				      Playlist playlist = getPlaylistRequest.execute();
    				      Paging<PlaylistTrack> tracks = playlist.getTracks();
    				      
    				      String curr = "";
    				      for(PlaylistTrack t : tracks.getItems()) {
    				    	  curr = t.getTrack().getName();
    					    	a = a + " " + curr;
    				      }
    				    System.out.println(a);
    				    } catch (IOException | SpotifyWebApiException | ParseException e) {
    				      System.out.println("Error: " + e.getMessage());
    				    }
    
    	System.out.println(featured.get(0));			    
    				    
    }
    
    //compare a and elements of arraylist featured using cosine similarity or jaccard similarity
}
    
  
	  


