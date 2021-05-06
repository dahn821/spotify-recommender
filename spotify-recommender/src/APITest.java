import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.hc.core5.http.ParseException;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import auth.Authorization;
public class APITest {
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
        Authorization a = new Authorization();
        System.out.println(a.getAuthorizationURI());
        Scanner input = new Scanner(System.in);

        System.out.println(
                "enter the URL into your browser and click agree, then enter the part after ?code= (should be a long string of alphanumeric characters): ");
        String code = input.next();
        SpotifyApi spotifyApi = a.apiFromCode(code);
        // write code here
        /*
         * NOTE: this access token will work for 1 hour, if you want to just copy + paste it instead
         * of going through the authentication process again.
         */
        String accessToken = spotifyApi.getAccessToken();
        System.out.println(accessToken);
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
//              .limit(10)
//              .offset(0)
//              .time_range("medium_term")
                .build();
        try {
            Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            System.out.println(Arrays.toString(artistPaging.getItems()));
            
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
