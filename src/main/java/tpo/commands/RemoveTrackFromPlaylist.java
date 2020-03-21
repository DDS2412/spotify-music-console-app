package tpo.commands;

import tpo.ApplicationProxy;


public class RemoveTrackFromPlaylist implements ConsoleCommand {
    private static final String COMMAND_INFO = "Удаление трека из плейлиста пользователя";

    @Override
    public Boolean execute(ApplicationProxy applicationProxy) {
        applicationProxy.show("Выберите плейлист, из которого хотите удалить трек");
        applicationProxy
                .selectPlaylist()
                .ifPresent(playlist -> {
                    applicationProxy.show("Выберите трек, который будет удален");
                    applicationProxy
                            .selectTrack(applicationProxy.getTracksFromPlayList(playlist))
                            .ifPresent(track ->
                                    applicationProxy
                                            .removeTrackFromPlaylist(playlist, track)
                                            .ifPresentOrElse(
                                                    deletedTrack -> applicationProxy.show(String.format("Трек '%s' был удален", deletedTrack.getName())),
                                                    () -> applicationProxy.show("Ошибка во время удаления трека")));
                });

        return false;
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
