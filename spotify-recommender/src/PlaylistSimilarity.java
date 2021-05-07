import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.hc.core5.http.ParseException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.FeaturedPlaylists;
import com.wrapper.spotify.model_objects.specification.Category;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfCategoriesRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;


public class PlaylistSimilarity {
	static ArrayList<Set<String>> inters = new ArrayList<Set<String>>();
    public static void getPlaylist(SpotifyApi spotifyApi, String userId, String type) {
	    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//	    Similarity based on genre
    	if(type.equals("g")){
    		ArrayList<String[]>categoryList = new ArrayList<String[]>(1000);
        	ArrayList<String> categoryIds = new ArrayList<String>();
            GetListOfCategoriesRequest getListOfCategoriesRequest = spotifyApi.getListOfCategories()
//                  .country(CountryCode.SE)
//                  .limit(10)
//                  .offset(0)
//                  .locale("sv_SE")
            .build();

            HashMap<String, String> categories = new HashMap<String, String>();
            try {
              final Paging<Category> categoryPaging = getListOfCategoriesRequest.execute();
              for (Category category : categoryPaging.getItems()) {
            	  categories.put(category.getName(), category.getId());
                System.out.println(category.getName());
            }
            } catch (IOException | SpotifyWebApiException | ParseException e) {
              System.out.println("Error: " + e.getMessage());
            }
          
            System.out.println("Select a category from above");

           String categoryName = myObj.nextLine(); 
            
           String catId = (String) categories.get(categoryName);
            
           
            
    	   GetCategorysPlaylistsRequest getCategoryRequest = spotifyApi.getCategorysPlaylists(catId)
//    		          .country(CountryCode.SE)
//    		          .limit(10)
//    		          .offset(0)
    		    .build();

    		    try {
    		      final Paging<PlaylistSimplified> playlistSimplifiedPaging = getCategoryRequest.execute();
    		
        	      for(PlaylistSimplified pitems :  playlistSimplifiedPaging.getItems()) {
        	    	  String p = pitems.getId();
        	    	  categoryIds.add(p);
        	      }
    		    } catch (IOException | SpotifyWebApiException | ParseException e) {
    		      System.out.println("Error: " + e.getMessage());
    		    }
    		    
    		    HashMap<String, String> map = new HashMap<String, String>();
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
        	  	

        	    System.out.println("choose one of your playlists from above");

        	    String playlistName = myObj.nextLine();  // Read user input

        	    String[] yay = {};
        	    
        	    
        		for (int i = 0; i < categoryIds.size(); i++) {  
        			 GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(categoryIds.get(i))
        	//		          .fields("description")
        	//		          .market(CountryCode.SE)
        	//		          .additionalTypes("track,episode")
        			    .build();
        	
        			    try {
        			      Playlist playlist = getPlaylistRequest.execute();
        			      Paging<PlaylistTrack> tracks = playlist.getTracks();
        			      String n = " ";
        			      for(PlaylistTrack t : tracks.getItems()) {
        			    	  if(t.getTrack() != null) {
        			    		  n = n + t.getTrack().getName();
        			    	  }
        			      }
        			       yay = n.split(" ");
        			    	categoryList.add(i, yay);
        			    } catch (IOException | SpotifyWebApiException | ParseException e) {
        			      System.out.println("Error: " + e.getMessage());
        			    }
        			    
            }
//        		System.out.println(featured);
            
        		
//        		Getting titles of your playlist
        		String[] a = {};
            GetPlaylistRequest getPlaylistRequest2 = spotifyApi.getPlaylist((String) map.get(playlistName))
            		//		          .fields("description")
            		//		          .market(CountryCode.SE)
            		//		          .additionalTypes("track,episode")
            				    .build();
            		
            				    try {
            				      Playlist playlist = getPlaylistRequest2.execute();
            				      Paging<PlaylistTrack> tracks = playlist.getTracks();
            				      
            				      String curr = "";
            				      for(PlaylistTrack t : tracks.getItems()) {
            				    	  curr = curr + " " + t.getTrack().getName();
            				    	  
            				      }
            				      a = curr.split(" ");   				 
//            				    System.out.println(a);
            				    } catch (IOException | SpotifyWebApiException | ParseException e) {
            				      System.out.println("Error: " + e.getMessage());
            				    }		    
            				    
            	ArrayList<Double> similarities = new ArrayList<Double>();
            	Double jSim;
            	for(int i = 0; i < categoryList.size(); i++) {
            		jSim = jaccardSimilarity(a, categoryList.get(i));
//            		System.out.println(jSim);
            		similarities.add(jSim);
            	}
            	Double max = Double.MIN_VALUE;
            	int index = -1;

            	for(int i = 0; i < similarities.size(); i ++){
            	    if(max < similarities.get(i)){
            	        max = similarities.get(i);
            	        index = i;
            	    }
            	}
            	
        		 GetPlaylistRequest getPlaylistRequest1 = spotifyApi.getPlaylist(categoryIds.get(index))
        	//		          .fields("description")
        	//		          .market(CountryCode.SE)
        	//		          .additionalTypes("track,episode")
        			    .build();
        		 
        		    try {
        			      Playlist playlist = getPlaylistRequest1.execute();
        			      System.out.println("Name of playlist: " + playlist.getName());
        			      System.out.println("URL: " + playlist.getHref());
        			      System.out.println("Description: " + playlist.getDescription());
        			      System.out.println("We chose this playlist based on these overlapping words: " + inters.get(index));
        			    } catch (IOException | SpotifyWebApiException | ParseException e) {
        			      System.out.println("Error: " + e.getMessage());
        			    } 
    		    
//    	Similarity based on featured lists 
    	} else if (type.equals("f")) {
    		ArrayList<String[]>featured = new ArrayList<String[]>(1000);
        	ArrayList<String> featuredId = new ArrayList<String>();


            GetListOfFeaturedPlaylistsRequest getListOfFeaturedPlaylistsRequest = spotifyApi
            	    .getListOfFeaturedPlaylists()
//            	          .country(CountryCode.SE)
//            	          .limit(10)
//            	          .offset(0)
//            	          .timestamp(new Date(1414054800000L))
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
        	
        	
        
        	HashMap<String, String> map = new HashMap<String, String>();
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
    	  	

    	    System.out.println("choose one of your playlists from above");

    	    String playlistName = myObj.nextLine();  // Read user input
    	    
    	    
//    		GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
//    				.getPlaylistsItems((String) map.get(playlistName))
//    	//	          .fields("description")
//    	//	          .limit(10)
//    	//	          .offset(0)
//    	//	          .market(CountryCode.SE)
//    	//	          .additionalTypes("track,episode")
//    		    .build();
//    		    try {
//    		      final Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsItemsRequest.execute();
    //	
//    		      System.out.println("Total: " + playlistTrackPaging.);
//    		    } catch (IOException | SpotifyWebApiException | ParseException e) {
//    		      System.out.println("Error: " + e.getMessage());
//    		    }
    	    String[] yay = {};
    		for (int i = 0; i < featuredId.size(); i++) {  
//    		    Map<String, Integer> count = new HashMap<String, Integer>();
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
    			       yay = n.split(" ");
    			    	featured.add(i, yay);
    			    } catch (IOException | SpotifyWebApiException | ParseException e) {
    			      System.out.println("Error: " + e.getMessage());
    			    }
    			    
        }
//    		System.out.println(featured);
        
    		
//    		Getting titles of your playlist
    		String[] a = {};
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
        				    	  curr = curr + " " + t.getTrack().getName();
        				    	  
        				      }
        				      a = curr.split(" ");   				 
//        				    System.out.println(a);
        				    } catch (IOException | SpotifyWebApiException | ParseException e) {
        				      System.out.println("Error: " + e.getMessage());
        				    }		    
        				    
        	ArrayList<Double> similarities = new ArrayList<Double>();
        	Double jSim;
        	for(int i = 0; i < featured.size(); i++) {
        		jSim = jaccardSimilarity(a, featured.get(i));
//        		System.out.println(jSim);
        		similarities.add(jSim);
        	}
        	Double max = Double.MIN_VALUE;
        	int index = -1;

        	for(int i = 0; i < similarities.size(); i ++){
        	    if(max < similarities.get(i)){
        	        max = similarities.get(i);
        	        index = i;
        	    }
        	}
        	
    		 GetPlaylistRequest getPlaylistRequest1 = spotifyApi.getPlaylist(featuredId.get(index))
    	//		          .fields("description")
    	//		          .market(CountryCode.SE)
    	//		          .additionalTypes("track,episode")
    			    .build();
    		 
    		    try {
    			      Playlist playlist = getPlaylistRequest1.execute();
    			      System.out.println("Name of playlist: " + playlist.getName());
    			      System.out.println("URL: " + playlist.getHref());
    			      System.out.println("Description: " + playlist.getDescription());
    			      System.out.println("We chose this playlist based on these overlapping key words: " + inters.get(index));
    			    } catch (IOException | SpotifyWebApiException | ParseException e) {
    			      System.out.println("Error: " + e.getMessage());
    			    }
    	} else  {
		    System.out.println("Please type g for genre or f for featured (lowercase please :))");
		    type = myObj.nextLine(); 
		    String username = Main.username;
		 PlaylistSimilarity.getPlaylist(spotifyApi, username, type);
    	}
    }
    
//    Jaccard Similarity function
    public static Double jaccardSimilarity(String[] d1, String[] d2) {
        Set<String> intersection = new HashSet<String>();
        Set<String> union = new HashSet<String>();
        boolean unionFilled = false;
        int leftLength = d1.length;
        int rightLength = d2.length;

        for (int leftIndex = 0; leftIndex < leftLength; leftIndex++) {
            union.add(d1[leftIndex]);
            for (int rightIndex = 0; rightIndex < rightLength; rightIndex++) {
                if (!unionFilled) {
                    union.add(d2[rightIndex]);
                }
                if (d1[leftIndex].equals(d2[rightIndex])) {
                    intersection.add(d1[leftIndex]);
                }
            }
            unionFilled = true;
        }
        inters.add(intersection);
        return Double.valueOf(intersection.size()) / Double.valueOf(union.size());
    }
    //compare a and elements of arraylist featured using cosine similarity or jaccard similarity
}
    
  
	  


