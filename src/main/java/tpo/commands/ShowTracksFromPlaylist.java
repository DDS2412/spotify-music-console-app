package tpo.commands;

import tpo.ApplicationProxy;

public class ShowTracksFromPlaylist implements ConsoleCommand {
    private static final String COMMAND_INFO = "Отображение треков из плейлиста пользователя";

    @Override
    public Boolean execute(ApplicationProxy applicationProxy) {
        applicationProxy.show("Выберите плейлист, треки которого вы хотите посмотреть");
        applicationProxy
                .selectPlaylist()
                .ifPresent(playlist -> {
                    applicationProxy.show(String.format("Треки из плейлиста %s", playlist.getName()));
                    applicationProxy.showTracks(applicationProxy.getTracksFromPlayList(playlist));
                });

        return false;
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
