package tpo.commands;

import tpo.ApplicationProxy;

public interface ConsoleCommand {
    void execute(ApplicationProxy applicationProxy);

    String getCommandInfo();
}
