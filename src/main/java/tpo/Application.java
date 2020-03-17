package tpo;

import tpo.services.MenuService;
import tpo.services.SpotifyMusicService;

public class Application {
    public static void main(String[] args) {
        if(args.length != 1 && args.length != 3){
            System.out.println("Введите один из следующих наборов параметров для работы с SpotifyApi:");
            System.out.println("1) Client ID, Client Secret и код, полученный при получении доступа");
            System.out.println("2) AccessToken");
        } else {
            SpotifyMusicService spotifyMusicService =
                    args.length == 3 ? new SpotifyMusicService(args[0], args[1], args[2]) : new SpotifyMusicService(args[0]);

            MenuService menuService = new MenuService(spotifyMusicService);
            menuService.Run();
        }
    }
}
