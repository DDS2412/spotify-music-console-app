package tpo.commands;

import tpo.ApplicationProxy;

public class FindAndAddMusicToPlaylist implements ConsoleCommand {
    private static final String COMMAND_INFO = "Поиск и добавление песни в плей лист";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        try {
            applicationProxy
                    .selectTrack()
                    .ifPresent(track -> applicationProxy
                            .selectPlaylist()
                            .ifPresent(playlist -> applicationProxy.addTracksToPlaylist(track, playlist)));

        } catch (Exception ex){
            applicationProxy.show(ex.getMessage());
        }
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
