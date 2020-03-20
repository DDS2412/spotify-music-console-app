package tpo.commands;

import tpo.ApplicationProxy;

public class CreateNewPlaylist implements ConsoleCommand {
    private static final String COMMAND_INFO = "Создание нового плейлиста";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        applicationProxy.createNewPlaylist();
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
