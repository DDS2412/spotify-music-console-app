package tpo;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import javafx.print.Collation;
import tpo.services.ConsoleService;
import tpo.services.SpotifyMusicService;
import tpo.utils.Formatter;
import tpo.utils.TypeConverter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApplicationProxy {
    private static final String TRACK_OUTPUT_PATTERN = "%d) '%s' - '%s'";
    private static final String PLAYLIST_OUTPUT_PATTERN = "%d) Плей лист '%s' автора '%s' содержит %s";
    private static final String ARTIST_OUTPUT_PATTERN = "%d) '%s', список жанров: %s";

    private ConsoleService consoleService;
    private SpotifyMusicService spotifyMusicService;

    public ApplicationProxy(ConsoleService consoleService, SpotifyMusicService spotifyMusicService){
        this.consoleService = consoleService;
        this.spotifyMusicService = spotifyMusicService;
    }

    public void show(String text){
        consoleService.show(text);
    }

    public String input() { return consoleService.input(); }

    public void clear() { consoleService.clearConsole(); }

    public TrackSimplified[] findTracks(){
        try {
            consoleService.show("Введите подстроку для поиска трека");

            return Arrays
                    .stream(spotifyMusicService.searchTracks(consoleService.input()).getItems())
                    .map(TypeConverter::convertTrackToSimplified)
                    .toArray(TrackSimplified[]::new);
        } catch (IOException | SpotifyWebApiException ex) {
            show(ex.getMessage());
            return new TrackSimplified[0];
        }
    }

    public TrackSimplified[] selectTracksFrom(){
        TrackSimplified[] tracks = new TrackSimplified[0];

        boolean isBreak = false;
        do {
            show("Выберите каким образом добавлять трек:");
            show("1) С помощью поиска");
            show("2) На основе рекомендаций");
            show("3) Отмена добавления песни");

            Integer selectedParam = TypeConverter.tryParseInt(input());

            if(selectedParam != null){
                switch (selectedParam){
                    case 1 : {
                        tracks = findTracks();
                        isBreak = true;
                        break;
                    } case 2 : {
                        tracks = getMusicRecommendations();
                        isBreak = true;
                        break;
                    } case 3 : {
                        isBreak = true;
                        break;
                    }
                }
            }

            if(!isBreak) {
                show("Неверный входной формат!");
                show("Попробуйте еще раз!");
            }
        } while (!isBreak);

        return tracks;
    }

    public Optional<TrackSimplified> selectTrack(TrackSimplified[] tracks) {
        if(tracks.length > 0){
            consoleService.show("Введите номер выбранной песни. Для отмены выберите последний пункт");
            showTracks(tracks);

            consoleService.show(String.format("%d) Отмена добавления песни", tracks.length+1));
            Integer selectedTrack = TypeConverter.tryParseInt(consoleService.input());

            if (selectedTrack != null){
                selectedTrack--;

                if (selectedTrack >= 0 &&  selectedTrack < tracks.length){
                    return Optional.of(tracks[selectedTrack]);
                }
            }
        }

        return Optional.empty();
    }

    public PlaylistSimplified[] getCurrentPlaylists(){
        try {
            return spotifyMusicService.getListOfCurrentUsersPlaylists().getItems();
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());

            return new PlaylistSimplified[0];
        }
    }

    public Optional<PlaylistSimplified> selectPlaylist() {
        PlaylistSimplified[] playlists = getCurrentPlaylists();
        consoleService.show(String.format("Введите номер желаемого плейлиста или %d для создания нового. Для отмены выберите последний пункт", playlists.length+1));

        if(playlists.length > 0){
            showPlaylist(playlists);
            consoleService.show(String.format("%d) Создать новый плейлист", playlists.length+1));
            consoleService.show(String.format("%d) Отмена действия", playlists.length+2));
            Integer selectedPlayList = TypeConverter.tryParseInt(consoleService.input());

            if(selectedPlayList != null){
                selectedPlayList--;

                if(selectedPlayList >= 0 && selectedPlayList <= playlists.length + 1){
                    if(selectedPlayList != playlists.length && selectedPlayList != playlists.length + 1){
                        return Optional.of(playlists[selectedPlayList]);
                    } else if (selectedPlayList == playlists.length){
                        return createNewPlaylist().map(TypeConverter::convertPlaylistToSimplified);
                    }
                }
            }
        }

        return Optional.empty();
    }

    public void addTracksToPlaylist(TrackSimplified track, PlaylistSimplified playlist) {
        try {
            spotifyMusicService.addTracksToPlayList(playlist.getId(), new String[]{ track.getUri()});
        } catch (IOException | SpotifyWebApiException ex) {
            show(ex.getMessage());
        }
    }

    public void displayUserInfo(){
        try {
            show(String.format("Ваш ник - '%s'", spotifyMusicService.getUserInfo().getDisplayName()));
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
        }
    }

    public Optional<Playlist> createNewPlaylist() {
        try {
            User user = spotifyMusicService.getUserInfo();
            show("Введите название нового плейлиста");
            String playlistName = input();

            Playlist newPlaylist = spotifyMusicService.createNewPlaylist(user.getId(), playlistName);

            show(String.format("Плейлист '%s' успешно создан!", newPlaylist.getName()));
            return Optional.of(newPlaylist);
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
            return Optional.empty();
        }
    }

    public Artist[] getPersonalArtists(){
        try {
            return spotifyMusicService.getPersonalTopArtists().getItems();
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
            return new Artist[0];
        }
    }

    public TrackSimplified[] getPersonalTracks(){
        try {
            return Arrays
                    .stream(spotifyMusicService.getPersonalTopTracks().getItems())
                    .map(TypeConverter::convertTrackToSimplified)
                    .toArray(TrackSimplified[]::new);

        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
            return new TrackSimplified[0];
        }
    }

    public TrackSimplified[] getTracksFromPlayList(PlaylistSimplified playlist) {
        try {
            return Arrays
                    .stream(spotifyMusicService.getTracksFromPlaylist(playlist.getId()))
                    .map(TypeConverter::convertTrackToSimplified)
                    .toArray(TrackSimplified[]::new);

        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
            return new TrackSimplified[0];
        }
    }

    public Optional<TrackSimplified> removeTrackFromPlaylist(PlaylistSimplified playlist, TrackSimplified track) {
        try{
            spotifyMusicService.removeTrackFromPlaylist(playlist.getId(), track.getUri());
            return Optional.of(track);
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
            return Optional.empty();
        }
    }

    public void showTracks(TrackSimplified[] tracks){
        for(int i = 0; i < tracks.length; i++){
            consoleService.show(String.format(TRACK_OUTPUT_PATTERN,
                    i+1,
                    tracks[i].getName(),
                    Arrays.stream(tracks[i].getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(", "))));
        }
    }

    public void showArtists(Artist[] artists){
        for(int i = 0; i < artists.length; i++){
            Artist artist = artists[i];
            show(String.format(ARTIST_OUTPUT_PATTERN,
                    i+1,
                    artist.getName(),
                    String.join(", ", artist.getGenres())));
        }
    }

    public void showPlaylist(PlaylistSimplified[] playlist) {
        for (int i = 0; i < playlist.length; i++) {
            PlaylistSimplified playlistSimplified = playlist[i];

            show(String.format(PLAYLIST_OUTPUT_PATTERN,
                    i+1,
                    playlistSimplified.getName(),
                    playlistSimplified.getOwner().getDisplayName(),
                    Formatter.getFormattedStringWithTrackCount(playlistSimplified.getTracks().getTotal())));
        }
    }

    public TrackSimplified[] getMusicRecommendations(){
        try {
            do{
                show("Выберите по какому принципу выдавать рекомендации");
                show("1) По исполнителям");
                show("2) По трекам");

                Integer selectedParam = TypeConverter.tryParseInt(input());

                if(selectedParam != null){
                    if(selectedParam == 1 || selectedParam == 2) {
                        return selectedParam == 1 ? getMusicRecommendationsByArtists() : getMusicRecommendationsByTrack();
                    }
                }

                show("Неверный входной формат!");
                show("Попробуйте еще раз!");
            } while (true);

        } catch (Exception ex){
            show(ex.getMessage());
            return new TrackSimplified[0];
        }
    }

    public TrackSimplified[] getMusicRecommendationsByArtists(){
        try {
            String seed_artists = Arrays
                    .stream(getPersonalArtists())
                    .limit(5)
                    .map(Artist::getId)
                    .collect(Collectors.joining(","));
            if(!seed_artists.isEmpty()){
                return spotifyMusicService.getMusicRecommendations(seed_artists, "");
            }

            return new TrackSimplified[0];
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
            return new TrackSimplified[0];
        }
    }

    public TrackSimplified[] getMusicRecommendationsByTrack(){
        try {
            String seed_tracks = Arrays
                    .stream(getPersonalTracks())
                    .limit(5)
                    .map(TrackSimplified::getId)
                    .collect(Collectors.joining(","));
            if(!seed_tracks.isEmpty()){
                return spotifyMusicService.getMusicRecommendations("", seed_tracks);
            }

            return new TrackSimplified[0];
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());
            return new TrackSimplified[0];
        }
    }
}
