import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hc.core5.http.ParseException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.ForbiddenException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
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
     * 
     * @return
     */
    public List<Track> recommendedTracks() {
        // consider favorite artists
        return null;
    }

    public List<Artist> recommendArtists() {
        // get users followed artists
        // take some artists
        // find a genre
        // recommend a track/artist from the genre
        
        // if the user is following an artist already or artist is in their top 20, then do not
        // include
        Artist[] followedArtists = getFollowedArtists().getItems();
        Artist[] userTopArtists = getUserTopArtists().getItems();
        Set<Artist> combinedUserTopArtists = new HashSet<Artist>(Arrays.asList(followedArtists));
        combinedUserTopArtists.addAll(Arrays.asList(userTopArtists));
        int limit = 20;
        Track[] topTracks = getTopTracks(limit).getItems();
        List<ArtistSimplified> topTrackArtists = new ArrayList<>();
        for (Track track : topTracks) {
            ArtistSimplified[] artists = track.getArtists();
            for(ArtistSimplified artist : artists) {
                topTrackArtists.add(artist);
            }
        }
        
        
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

    /**
     * weight heavily in the recommender
     * 
     * @return "list" of artists
     */
    public PagingCursorbased<Artist> getFollowedArtists() {
        try {
            return spotifyApi.getUsersFollowedArtists(ModelObjectType.ARTIST).limit(3).build()
                    .execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Paging<Artist> getUserTopArtists() {
        try {
            return spotifyApi.getUsersTopArtists().build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Paging<Track> getTopTracks(int limit) {
        try {
            return spotifyApi.getUsersTopTracks().limit(limit).build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
