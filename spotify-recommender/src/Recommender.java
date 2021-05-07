import java.io.IOException;
import java.util.List;

import org.apache.hc.core5.http.ParseException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.ForbiddenException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;

public class Recommender {
    
    SpotifyApi spotifyApi;
    
    public Recommender(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }
    
    /**
     * Does not need any parameters because the api object itself contains the access code
     * @return
     */
    public List<Track> recommendedTracks() {
        //consider favorite artists
    }
    
    public Paging<PlaylistSimplified> getUserPlaylists() {
        try {
            return spotifyApi.getListOfCurrentUsersPlaylists().build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    
    
}
