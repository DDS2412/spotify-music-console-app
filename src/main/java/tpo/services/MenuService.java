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

    public Boolean Run() {
        applicationProxy.show("Введите команду для начала работы!");
        Boolean isExit;

        do {
            String command = applicationProxy.input();
            applicationProxy.clear();

            isExit = executeCommand(command);
            if (!isExit){
                applicationProxy.show("Выполнение команды завершено, для продолжения введите следующую команду");
            } else {
                applicationProxy.show("Завершение работы программы!");
            }

        } while (!isExit);

        return isExit;
    }

    private Boolean executeCommand(String command) {
        Boolean isExit;
        command = command.toLowerCase().strip();

        if(consoleCommands.containsKey(command)){
            isExit = consoleCommands.get(command).execute(applicationProxy);

        } else {
            applicationProxy.show(String.format("Команды %s не существует!", command));
            isExit = false;
        }

        return isExit;
    }

    private void setConsoleCommands(){
        consoleCommands.put("exit", new ExitCommand());
        consoleCommands.put("help", new ConsoleCommand() {
            @Override
            public Boolean execute(ApplicationProxy applicationProxy) {
                consoleCommands
                        .forEach((commandName, consoleCommand) -> applicationProxy.show(String.format("%s - %s", commandName, consoleCommand.getCommandInfo())));

                return false;
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
        consoleCommands.put("top_track", new ShowTopTracks());
        consoleCommands.put("top_artist", new ShowTopArtists());
        consoleCommands.put("r_tracks", new ShowMusicRecommendations());
        consoleCommands.put("remove_t", new RemoveTrackFromPlaylist());
        consoleCommands.put("show_tracks", new ShowTracksFromPlaylist());
    }
}
