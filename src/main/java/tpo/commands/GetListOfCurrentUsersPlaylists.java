package tpo.commands;

import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import tpo.services.ConsoleService;
import tpo.services.SpotifyMusicService;


public class GetListOfCurrentUsersPlaylists  implements ConsoleCommand{
    private static final String COMMAND_INFO = "Список существующих плей-листов пользователя";
    private static final String OUTPUT_PATTERN = "%d) плей лист '%s' автора '%s' содержит %s";

    @Override
    public void execute(ConsoleService consoleService, SpotifyMusicService spotifyMusicService) {
        try {
            Paging<PlaylistSimplified> listOfCurrentUsersPlaylists = spotifyMusicService.getListOfCurrentUsersPlaylists();

            for(int i = 0; i < listOfCurrentUsersPlaylists.getItems().length; i++){
                PlaylistSimplified playlistSimplified = listOfCurrentUsersPlaylists.getItems()[i];

                consoleService.show(String.format(OUTPUT_PATTERN, i, playlistSimplified.getName(),
                        playlistSimplified.getOwner().getDisplayName(),
                        getFormattedStringWithTrackCount(playlistSimplified.getTracks().getTotal())));
            }
        } catch (Exception ex) {
            consoleService.show(ex.getMessage());
        }
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }

    private String getFormattedStringWithTrackCount(Integer totalTracks){
        String formattedString = "треков";

        if (totalTracks == 1) {
            formattedString = " трек";
        } else if (totalTracks >= 2 && totalTracks <= 4){
            formattedString = " трека";
        }

        return totalTracks + formattedString;
    }
}
