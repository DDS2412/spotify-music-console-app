package tpo.services;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.playlists.AddTracksToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;

public class SpotifyMusicService {
    private SpotifyApi spotifyApi;

    public SpotifyMusicService(String accessToken){
        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
    }

    public SpotifyMusicService(String clientId, String clientSecret, String code){
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri("https://api-university.com/"))
                .build();

        setExtraSpotifyApiParams(code);
    }

    public Paging<PlaylistSimplified> getListOfCurrentUsersPlaylists() throws IOException, SpotifyWebApiException {
        GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists()
                .build();

        return getListOfCurrentUsersPlaylistsRequest.execute();
    }

    public Paging<Track> searchTracks(String query) throws IOException, SpotifyWebApiException {
        SearchTracksRequest searchTracksRequest = spotifyApi
                .searchTracks(query)
                .limit(9)
                .build();

        return searchTracksRequest.execute();
    }

    public void addTracksToPlayList(String playListId, String[] trackUris) throws IOException, SpotifyWebApiException {
        AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyApi
                .addTracksToPlaylist(playListId, trackUris)
                .build();

        SnapshotResult snapshotResult = addTracksToPlaylistRequest.execute();
    }

    public User getUserInfo() throws IOException, SpotifyWebApiException {
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi
                .getCurrentUsersProfile()
                .build();

        return getCurrentUsersProfileRequest.execute();
    }

    public Playlist createNewPlaylist(String userId, String playlistName) throws IOException, SpotifyWebApiException {
        CreatePlaylistRequest createPlaylistRequest = spotifyApi
                .createPlaylist(userId, playlistName)
                .build();

        return createPlaylistRequest.execute();
    }

    public Paging<Track> getPersonalTopTracks() throws IOException, SpotifyWebApiException {
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi
                .getUsersTopTracks()
                .limit(10)
                .build();

        return getUsersTopTracksRequest.execute();
    }

    public Paging<Artist> getPersonalTopArtists() throws IOException, SpotifyWebApiException{
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi
                .getUsersTopArtists()
                .limit(10)
                .build();

        return getUsersTopArtistsRequest.execute();
    }

    public TrackSimplified[]  getMusicRecommendations(String seed_artists, String seed_tracks) throws IOException, SpotifyWebApiException {
        GetRecommendationsRequest getRecommendationsRequest = spotifyApi
                .getRecommendations()
                .seed_artists(seed_artists)
                .seed_tracks(seed_tracks)
                .limit(10)
                .build();

        return getRecommendationsRequest.execute().getTracks();
    }

    private void setExtraSpotifyApiParams(String code) {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println(authorizationCodeCredentials.getAccessToken());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
