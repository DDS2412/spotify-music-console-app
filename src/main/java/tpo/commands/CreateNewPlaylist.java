package tpo.commands;

import tpo.ApplicationProxy;

public class CreateNewPlaylist implements ConsoleCommand {
    private static final String COMMAND_INFO = "Создание нового плейлиста";

    @Override
    public Boolean execute(ApplicationProxy applicationProxy) {
        applicationProxy.createNewPlaylist();

        return false;
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
