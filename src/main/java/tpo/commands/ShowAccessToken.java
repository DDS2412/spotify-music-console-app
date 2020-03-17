package tpo.commands;

import tpo.services.ConsoleService;
import tpo.services.SpotifyMusicService;

public class ShowAccessToken implements ConsoleCommand {
    private static final String COMMAND_INFO = "Отображает AccessToken";

    @Override
    public void execute(ConsoleService consoleService, SpotifyMusicService spotifyMusicService) {
        consoleService.show("Информация о AccessToken");
        consoleService.show(spotifyMusicService.getAccessToken());
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
