package tpo.commands;

import tpo.services.ConsoleService;
import tpo.services.SpotifyMusicService;

public class ExitCommand implements ConsoleCommand {
    private static final String COMMAND_INFO = "Завершение работы программы";

    @Override
    public void execute(ConsoleService consoleService, SpotifyMusicService spotifyMusicService) {
        consoleService.show("Завершение работы!");
        consoleService.exit();
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
