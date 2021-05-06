import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.hc.core5.http.ParseException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import auth.Authorization;

public class Tester {
    public static void main(String[] args) {
        Authorization a = new Authorization();
        System.out.println(a.getAuthorizationURI());
        a.handleRedirectUri();
//        Scanner input = new Scanner(System.in);
//
//        System.out.println(
//                "enter the URL into your browser and click agree, then enter the part after ?code= (should be a long string of alphanumeric characters): ");
//        String code = input.next();
//        SpotifyApi spotifyApi = a.apiFromCode(code);
//        // write code here
//        /*
//         * NOTE: this access token will work for 1 hour, if you want to just copy + paste it instead
//         * of going through the authentication process again.
//         */
//        String accessToken = spotifyApi.getAccessToken();
//        System.out.println(accessToken);
//        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
////              .limit(10)
////              .offset(0)
////              .time_range("medium_term")
//                .build();
        try {
            Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            System.out.println(Arrays.toString(artistPaging.getItems()));
            for (Artist artist : artistPaging.getItems()) {
                System.out.println(artist.toString());
            }
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
