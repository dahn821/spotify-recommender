import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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

/**
 * Instantiable class. Contains many API calls to perform recommendations for tracks
 * 
 * @author nlao
 *
 */
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

    /**
     * This algorithm will return a set of {@code Artist} objects as specified in the Spotify API
     * page. As such, methods such as {@code .getName()} can be called to retrieve the name of the
     * returned artist. For such operations, a for-each loop is recommended.
     * 
     * @param number number of artists to recommend (if there are not that many found by algorithm),
     *               then it will not be that large
     * @return a set of {@code size} containing at most {@code number} artists
     */
    public Set<Artist> recommendArtists(int number) {
        // get users followed artists
        // take some artists
        // find a genre
        // recommend a track/artist from the genre

        // if the user is following an artist already or artist is in their top numTopArtists, then
        // do not include
        Artist[] followedArtists = getFollowedArtists().getItems();
        int numTopArtists = 15;
        Artist[] userTopArtists = getUserTopArtists(numTopArtists).getItems();
        Set<Artist> combinedUserTopArtists = new HashSet<>(Arrays.asList(followedArtists));
        combinedUserTopArtists.addAll(Arrays.asList(userTopArtists));

        int limitTopTracks = 20;
        Track[] topTracks = getTopTracks(limitTopTracks).getItems();

        Set<ArtistSimplified> topTrackArtistsSimplified = new HashSet<>();
        for (Track track : topTracks) {
            ArtistSimplified[] artists = track.getArtists();
            for (ArtistSimplified artist : artists) {
                topTrackArtistsSimplified.add(artist);
            }
        }
        // converting artistsimplified to artist for consistency
        Set<Artist> topTrackArtists = new HashSet<>();
        for (ArtistSimplified artist : topTrackArtistsSimplified) {
            topTrackArtists.add(getArtistFromId(artist.getId()));
        }

        Set<Artist> relatedArtistsToTopArtists = new HashSet<>();
        for (Artist artist : combinedUserTopArtists) {
            relatedArtistsToTopArtists.addAll(Arrays.asList(getRelatedArtists(artist.getId())));
        }

        Set<Artist> recommendSet = new HashSet<>(topTrackArtists);
        recommendSet.addAll(relatedArtistsToTopArtists);
        recommendSet.removeAll(combinedUserTopArtists);
        Iterator<Artist> recommendIterator = recommendSet.iterator();
        Set<Artist> outputSet = new HashSet<>();
        // I kind of want to try using a HashMap w/ frequencies of artists instead of sets, that way
        // if an artist appears multiple times it is recommended
        while (outputSet.size() < number && recommendIterator.hasNext()) {
            outputSet.add(recommendIterator.next());
        }
        return outputSet;
    }

    Artist[] getRelatedArtists(String id) {
        try {
            return spotifyApi.getArtistsRelatedArtists(id).build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    Paging<PlaylistSimplified> getUserPlaylists() {
        try {
            return spotifyApi.getListOfCurrentUsersPlaylists().build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    PagingCursorbased<Artist> getFollowedArtists() {
        try {
            return spotifyApi.getUsersFollowedArtists(ModelObjectType.ARTIST).limit(3).build()
                    .execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    Paging<Artist> getUserTopArtists(int limit) {
        try {
            return spotifyApi.getUsersTopArtists().limit(limit).build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    Paging<Track> getTopTracks(int limit) {
        try {
            return spotifyApi.getUsersTopTracks().limit(limit).build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    Artist getArtistFromId(String id) {
        try {
            return spotifyApi.getArtist(id).build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
