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
    private static final String PLAYLIST_OUTPUT_PATTERN = "%d) плей лист '%s' автора '%s' содержит %s";

    private ConsoleService consoleService;
    private SpotifyMusicService spotifyMusicService;

    public ApplicationProxy(ConsoleService consoleService, SpotifyMusicService spotifyMusicService){
        this.consoleService = consoleService;
        this.spotifyMusicService = spotifyMusicService;
    }

    public void show(String text){
        consoleService.show(text);
    }

    public void exit(){
        consoleService.exit();
    }

    public String input() { return consoleService.input(); }

    public Optional<Track> selectTrack() {
        try {
            consoleService.show("Введите подстроку для поиска трека");
            Track[] tracks = spotifyMusicService.searchTracks(consoleService.input()).getItems();

            consoleService.show("Введите номер выбранной песни. Для отмены выберите последний пункт");
            for(int i = 0; i < tracks.length; i++){
                consoleService.show(String.format(TRACK_OUTPUT_PATTERN,
                        i,
                        tracks[i].getName(),
                        Arrays.stream(tracks[i].getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(", "))));
            }

            consoleService.show(String.format("%d) Вернуться обратно", tracks.length));
            Integer selectedTrack = TypeConverter.tryParseInt(consoleService.input());

            if (selectedTrack != null &&  selectedTrack != tracks.length){
                return Optional.of(tracks[selectedTrack]);
            } else {
                return Optional.empty();
            }
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());

            return Optional.empty();
        }
    }

    public void showCurrentPlaylists() {
        showPlaylist(getCurrentPlaylists());
    }

    public PlaylistSimplified[] getCurrentPlaylists(){
        try {

            return spotifyMusicService.getListOfCurrentUsersPlaylists().getItems();
        } catch (SpotifyWebApiException | IOException ex) {
            show(ex.getMessage());

            return new PlaylistSimplified[0];
        }
    }

    public void showPlaylist(PlaylistSimplified[] playlist) {
        for (int i = 0; i < playlist.length; i++) {
            PlaylistSimplified playlistSimplified = playlist[i];

            consoleService.show(String.format(PLAYLIST_OUTPUT_PATTERN, i, playlistSimplified.getName(),
                    playlistSimplified.getOwner().getDisplayName(),
                    Formatter.getFormattedStringWithTrackCount(playlistSimplified.getTracks().getTotal())));
        }
    }

    public Optional<PlaylistSimplified> selectPlaylist() {
        PlaylistSimplified[] playlists = getCurrentPlaylists();
        consoleService.show(String.format("Введите номер желаемого плейлиста или %d для создания нового. Для отмены выберите последний пункт", playlists.length));

        showPlaylist(playlists);
        consoleService.show(String.format("%d) Создать новый плейлист", playlists.length));
        consoleService.show(String.format("%d) Вернуться обратно", playlists.length+1));
        Integer selectedPlayList = TypeConverter.tryParseInt(consoleService.input());

        if(selectedPlayList != null && selectedPlayList != playlists.length + 1 && selectedPlayList != playlists.length){
            return Optional.of(playlists[selectedPlayList]);
        } else if (selectedPlayList == playlists.length){
            return createNewPlaylist().map(TypeConverter::convertPlaylistToSimplified);
        } else {
            return Optional.empty();
        }
    }

    public void addTracksToPlaylist(Track track, PlaylistSimplified playlist) {
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
}
