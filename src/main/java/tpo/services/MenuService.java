package tpo.services;

import tpo.commands.*;

import java.util.HashMap;

public class MenuService {
    private SpotifyMusicService spotifyMusicService;
    private ConsoleService consoleService;

    private HashMap<String, ConsoleCommand> consoleCommands = new HashMap<>();

    public MenuService(SpotifyMusicService spotifyMusicService){
        this.spotifyMusicService = spotifyMusicService;
        this.consoleService = new ConsoleService();

        setConsoleCommands();
    }

    public void Run() {
        consoleService.show("Введите команду для начала работы!");

        do {
            executeCommand(consoleService.input());

        } while (true);
    }

    private void executeCommand(String command) {
        command = command.toLowerCase();

        if(consoleCommands.containsKey(command)){
            consoleCommands.get(command).execute(consoleService, spotifyMusicService);

        } else {
            consoleService.show(String.format("Команды %s не существует!", command));
        }
    }

    private void setConsoleCommands(){
        consoleCommands.put("exit", new ExitCommand());
        consoleCommands.put("help", new ConsoleCommand() {
            @Override
            public void execute(ConsoleService consoleService, SpotifyMusicService spotifyMusicService) {
                consoleCommands
                        .forEach((commandName, consoleCommand) -> consoleService.show(String.format("%s - %s", commandName, consoleCommand.getCommandInfo())));
            }

            @Override
            public String getCommandInfo() {
                return "Отображение информации о всех командах";
            }
        });
        consoleCommands.put("access_token", new ShowAccessToken());
        consoleCommands.put("get_playlist", new GetListOfCurrentUsersPlaylists());
    }
}
