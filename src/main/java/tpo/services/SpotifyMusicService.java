package tpo.services;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

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

    public String getAccessToken(){
        return spotifyApi.getAccessToken();
    }

    public Paging<PlaylistSimplified> getListOfCurrentUsersPlaylists() throws IOException, SpotifyWebApiException {
        GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists()
                .build();

        return getListOfCurrentUsersPlaylistsRequest.execute();
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
