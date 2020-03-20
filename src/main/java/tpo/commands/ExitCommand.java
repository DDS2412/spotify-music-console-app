package tpo.commands;

import tpo.ApplicationProxy;

public class ExitCommand implements ConsoleCommand {
    private static final String COMMAND_INFO = "Завершение работы программы";

    @Override
    public void execute(ApplicationProxy applicationProxy) {
        applicationProxy.show("Завершение работы!");
        applicationProxy.exit();
    }

    @Override
    public String getCommandInfo() {
        return COMMAND_INFO;
    }
}
