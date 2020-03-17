package tpo.commands;

import tpo.services.ConsoleService;
import tpo.services.SpotifyMusicService;

public interface ConsoleCommand {
    void execute(ConsoleService consoleService, SpotifyMusicService spotifyMusicService);

    String getCommandInfo();
}
