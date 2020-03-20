package tpo.services;

import tpo.ApplicationProxy;
import tpo.commands.*;

import java.util.HashMap;

public class MenuService {
    private ApplicationProxy applicationProxy;

    private HashMap<String, ConsoleCommand> consoleCommands = new HashMap<>();

    public MenuService(SpotifyMusicService spotifyMusicService){
        applicationProxy = new ApplicationProxy(new ConsoleService(), spotifyMusicService);

        setConsoleCommands();
    }

    public void Run() {
        applicationProxy.show("Введите команду для начала работы!");

        do {
            executeCommand(applicationProxy.input());
            applicationProxy.show("Выполнение команды завершено, для продолжения введите следующую команду");
        } while (true);
    }

    private void executeCommand(String command) {
        command = command.toLowerCase();

        if(consoleCommands.containsKey(command)){
            consoleCommands.get(command).execute(applicationProxy);

        } else {
            applicationProxy.show(String.format("Команды %s не существует!", command));
        }
    }

    private void setConsoleCommands(){
        consoleCommands.put("exit", new ExitCommand());
        consoleCommands.put("help", new ConsoleCommand() {
            @Override
            public void execute(ApplicationProxy applicationProxy) {
                consoleCommands
                        .forEach((commandName, consoleCommand) -> applicationProxy.show(String.format("%s - %s", commandName, consoleCommand.getCommandInfo())));
            }

            @Override
            public String getCommandInfo() {
                return "Отображение информации о всех командах";
            }
        });
        consoleCommands.put("get_playlist", new GetListOfCurrentUsersPlaylists());
        consoleCommands.put("add_track", new FindAndAddMusicToPlaylist());
        consoleCommands.put("user", new GetUserInfo());
        consoleCommands.put("create_playlist", new CreateNewPlaylist());
    }
}
