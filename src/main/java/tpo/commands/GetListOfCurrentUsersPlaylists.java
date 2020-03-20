package tpo.commands;

import tpo.ApplicationProxy;


public class GetListOfCurrentUsersPlaylists  implements ConsoleCommand{
    private static final String COMMAND_INFO = "Список существующих плей-листов пользователя";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        try {
            applicationProxy.showPlaylist(applicationProxy.getCurrentPlaylists());
        } catch (Exception ex) {
            applicationProxy.show(ex.getMessage());
        }
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
