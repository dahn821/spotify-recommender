import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.hc.core5.http.ParseException;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.ForbiddenException;
import com.wrapper.spotify.model_objects.AbstractModelObject;
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
     * Does not need any parameters because the API object itself contains the access code. This
     * algorithm returns a list of {@code track} objects (not ordered by anything in this case) to
     * recommend to the user. As such, methods such as {@code .getName()} can be called to retrieve
     * the name of the returned artist. For such operations, a for-each loop is recommended. Known
     * issue: {@code track.getName()} will return "????" if using non-ASCII characters. Also, should
     * give links to the tracks because sometimes many songs have the same name (the links are also
     * subject to the "????" problem for some reason).
     * 
     * @param number number of tracks to recommend
     * @return a list of recommended tracks
     */
    public List<Track> recommendTracks(int number) {
        // consider favorite artists
        List<Artist> recommendedArtists = recommendArtists(15);
        Set<Track> userTopTracks = new HashSet<>(Arrays.asList(getTopTracks(50).getItems()));
        List<Track> allTracks = new ArrayList<>();
        recommendedArtists.forEach(artist -> {
            allTracks.addAll(Arrays.asList(getTopTracksOfArtist(artist.getId())));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        // randomizes it to prevent clustering of songs w/ same genre
        Collections.shuffle(allTracks);

        // removes top tracks since user already listens to them a lot
        allTracks.removeAll(userTopTracks);

        List<Track> outputList = new ArrayList<>();
        allTracks.stream().limit(number).forEach(track -> outputList.add(track));
        return outputList;
    }

    /**
     * This algorithm will return a list of {@code Artist} objects as specified in the Spotify API
     * page. As such, methods such as {@code .getName()} can be called to retrieve the name of the
     * returned artist. For such operations, a for-each loop is recommended.
     * 
     * @param number number of artists to recommend (if there are not that many found by algorithm),
     *               then it will not be that large
     * @return a list of {@code size()} containing at most {@code number} artists
     */
    public List<Artist> recommendArtists(int number) {

        // if the user is following an artist already or artist is in their top numTopArtists, then
        // do not include
        Artist[] followedArtists = getFollowedArtists().getItems();
        int numTopArtists = 15;
        Artist[] userTopArtists = getUserTopArtists(numTopArtists).getItems();
        Set<Artist> combinedUserTopArtists = new HashSet<>(Arrays.asList(followedArtists));
        combinedUserTopArtists.addAll(Arrays.asList(userTopArtists));

        int limitTopTracks = 20;
        Track[] topTracks = getTopTracks(limitTopTracks).getItems();

        Map<Artist, Integer> topTrackArtists = new HashMap<>();
        for (Track track : topTracks) {
            ArtistSimplified[] artists = track.getArtists();
            for (ArtistSimplified artist : artists) {
                // converting artistsimplified to artist for consistency (there might be a better
                // way to do
                // this)
                updateFrequency(topTrackArtists, getArtistFromId(artist.getId()), 1);
            }
        }

        Map<Artist, Integer> relatedArtistsToTopArtists = new HashMap<>();
        for (Artist artist : combinedUserTopArtists) {
            List<Artist> relatedArtists = Arrays.asList(getRelatedArtists(artist.getId()));
            for (Artist related : relatedArtists) {
                updateFrequency(relatedArtistsToTopArtists, related, 1);
            }
        }

        Map<Artist, Integer> recommended = new HashMap<>(topTrackArtists);
        for (Artist artist : relatedArtistsToTopArtists.keySet()) {
            updateFrequency(recommended, artist, relatedArtistsToTopArtists.get(artist));
        }
        for (Artist artist : combinedUserTopArtists) {
            recommended.remove(artist);
        }
        List<Artist> outputList = new ArrayList<>();
        // logic partially retrieved from StackOverflow
        recommended.entrySet().stream()
                .sorted(Map.Entry.<Artist, Integer>comparingByValue().reversed())
                // not completely functional for some reason
                .filter(entry -> !combinedUserTopArtists.contains(entry.getKey())).limit(number)
                .forEachOrdered(entry -> outputList.add(entry.getKey()));
        return outputList;
    }

    /**
     * helper method to treat the map like a histogram.
     * 
     * @param map    map of artists -> frequencies
     * @param artist artist to insert into the map
     */
    private static void updateFrequency(Map<Artist, Integer> map, Artist artist, int frequency) {
        if (map.containsKey(artist)) {
            int currFreq = map.get(artist);
            map.replace(artist, currFreq + frequency);
        } else {
            map.put(artist, 1);
        }

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

    Track[] getTopTracksOfArtist(String id) {
        try {
            return spotifyApi.getArtistsTopTracks(id, CountryCode.US).build().execute();
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
