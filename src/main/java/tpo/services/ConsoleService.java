package tpo.services;

import java.util.Scanner;

public class ConsoleService {
    private Scanner scanner;

    public ConsoleService(){
        scanner = new Scanner(System.in);
    }

    public void show(String text) {
        System.out.println(text);
    }

    public String input(){
        return scanner.nextLine();
    }

    public void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
        }
        catch (Exception ex)
        {
            show("");
        }
    }
}
