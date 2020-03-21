import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.junit.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import tpo.ApplicationProxy;
import tpo.services.ConsoleService;
import tpo.services.MenuService;
import tpo.services.SpotifyMusicService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Optional;
import org.junit.platform.runner.JUnitPlatform;

@RunWith(JUnitPlatform.class)
public class ApplicationProxyTest {
    private static final String ACCESS_TOKEN = "BQDY8wz2Qn3z1bMfZn_g3mCpTNXE-MCBd_AVfltENVQPn4Ker5c4M-swWOWiCvSzBeIqFTqMnZYEloMdEmWhZRZt6N1qUkJoAI-x65Edh1skTu0CR1ixgRmt_94uZ7imArOHYTylCiXxILYrI4jtwwpcJobrxNP1chhnfJMnuM8q6_9B3XXOcR47oB7s6rsthxNsGqHrnrTSY_NMnQ4riUZ-o2wr";

    private TrackSimplified[] tracksMock;

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    public ApplicationProxyTest(){
        tracksMock = getTrackSimplifiedMock();
    }

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void shouldReadFromConsole(){
        String testResult = "Привет мир!";
        String testText = "Привет мир!";
        provideInput(testText);

        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));

        String actualResult = applicationProxy.input();

        Assert.assertEquals(testResult, actualResult);
    }

    @Test
    public void shouldWriteToConsole(){
        String testResult = "Привет мир!";
        String testText = "Привет мир!";

        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        applicationProxy.show(testText);

        Assert.assertEquals(testResult, getOutput().strip());
    }

    @Test
    public void shouldClearConsole(){
        String testResult = "";
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        applicationProxy.clear();

        Assert.assertEquals(testResult, getOutput().strip());

    }

    @Test
    public void shouldExit(){
        provideInput("exit");

        MenuService menuService = new MenuService(new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(menuService.Run());
    }

    @Test
    public void shouldDisplayUserInfo(){
        String testResult = "Ваш ник - 'ZERO'";
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        applicationProxy.displayUserInfo();
        Assert.assertEquals(testResult, getOutput().strip());
    }

    @Test
    public void shouldFindTracksByName(){
        String testResult = "Against Myself";
        String testText = "Against Myself";

        provideInput(testText);
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(Arrays.stream(applicationProxy.findTracks()).anyMatch(track -> track.getName().contains(testResult)));
    }

    @Test
    public void shouldSelectTracksFromQuery(){
        String testResult = "Against Myself";
        String testText = "Against Myself";
        provideInput("1\n" + testText);

        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(Arrays.stream(applicationProxy.selectTracksFrom()).anyMatch(track -> track.getName().contains(testResult)));
    }

    @Test
    public void shouldSelectTrack(){
        String testResult = "Lakes of Flame";

        provideInput("1");
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Optional<TrackSimplified> actual = applicationProxy.selectTrack(tracksMock);

        actual.ifPresent(trackSimplified -> Assert.assertEquals(testResult, trackSimplified.getName()));
    }

    @Test
    public void shouldContainSixCurrentPlaylists(){
        int testResult = 6;

        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertEquals(testResult, applicationProxy.getCurrentPlaylists().length);
    }

    @Test
    public void shouldSelectSecondTestPlaylist(){
        String testResult = "TestPlaylist";

        provideInput("2");

        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Optional<PlaylistSimplified> actualPlaylist = applicationProxy.selectPlaylist();

        actualPlaylist.ifPresent(playlist -> Assert.assertEquals(testResult, playlist.getName()));
    }

    @Test
    public void shouldAddTrackToThirdPlaylist(){
        String testResult = "Lakes of Flame";

        provideInput("3");
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Optional<PlaylistSimplified> actualPlaylist = applicationProxy.selectPlaylist();
        actualPlaylist.ifPresent(playlist -> {
            applicationProxy.addTracksToPlaylist(tracksMock[0], playlist);
            Assert.assertTrue(Arrays.stream(applicationProxy.getTracksFromPlayList(playlist)).anyMatch(track -> track.getName().contains(testResult)));
            applicationProxy.removeTrackFromPlaylist(playlist, tracksMock[0]);
        });
    }

    @Test
    public void shouldCreateNewPlaylist(){
        String testResult = "TestPlaylist3";
        String testTest = "TestPlaylist3";
        provideInput(testTest);

        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        applicationProxy.createNewPlaylist().ifPresent(actual -> Assert.assertEquals(testResult ,actual.getName()));
    }

    @Test
    public void shouldReturnPersonalArtists(){
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(applicationProxy.getPersonalArtists().length > 0);
    }

    @Test
    public void shouldReturnPersonalTracks(){
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(applicationProxy.getPersonalTracks().length > 0);
    }

    @Test
    public void shouldReturnTrackFromPlaylist(){
        provideInput("1");
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Optional<PlaylistSimplified> optionalPlaylist = applicationProxy.selectPlaylist();
        if(optionalPlaylist.isPresent()){
            Assert.assertTrue(applicationProxy.getTracksFromPlayList(optionalPlaylist.get()).length > 0);
        }
    }

    @Test
    public void shouldReturnMusicRecommendations(){
        provideInput("2");
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(applicationProxy.getMusicRecommendations().length > 0);
    }

    @Test
    public void shouldMusicRecommendationsByArtists(){
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(applicationProxy.getMusicRecommendationsByArtists().length > 0);
    }

    @Test
    public void shouldMusicRecommendationsByTrack(){
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(applicationProxy.getMusicRecommendationsByTrack().length > 0);
    }

    @Test
    public void shouldWriteInfoAboutTracksInConsole(){
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        applicationProxy.showTracks(tracksMock);

        Assert.assertFalse(getOutput().strip().isEmpty());
    }

    @Test
    public void shouldWriteInfoAboutPlaylistInConsole(){
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        applicationProxy.showPlaylist(applicationProxy.getCurrentPlaylists());

        Assert.assertFalse(getOutput().strip().isEmpty());
    }

    @Test
    public void shouldFindAndAddTrackToPlaylist(){
        String testResult = "Deviate";

        provideInput("add_track\n1\nDeviate\n1\n1\nexit");

        MenuService menuService = new MenuService(new SpotifyMusicService(ACCESS_TOKEN));
        menuService.Run();
        provideInput("1\n1");
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        TrackSimplified trackSimplified = applicationProxy.getTracksFromPlayList(applicationProxy.selectPlaylist().get())[2];
        Assert.assertTrue(trackSimplified.getName().strip().contains(testResult));
        applicationProxy.removeTrackFromPlaylist(applicationProxy.selectPlaylist().get(), trackSimplified);
    }

    @Test
    public void shouldRemoveTrackFromPlaylist(){
        String testResult = "Deviate";
        provideInput("add_track\n1\nDeviate\n1\n1\nremove_t\n1\n1\nexit");

        MenuService menuService = new MenuService(new SpotifyMusicService(ACCESS_TOKEN));
        menuService.Run();

        provideInput("1");
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Assert.assertTrue(applicationProxy.getTracksFromPlayList(applicationProxy.selectPlaylist().get()).length == 0);
    }

    @Test
    public void shouldReturnHelpInfo(){
        String testResult = "Введите команду для начала работы!\r\n" +
                "\r\n" +
                "create_playlist - Создание нового плейлиста\r\n" +
                "exit - Завершение работы программы\r\n" +
                "help - Отображение информации о всех командах\r\n" +
                "top_track - Рекомендуемые треки\r\n" +
                "remove_t - Удаление трека из плейлиста пользователя\r\n" +
                "top_artist - Рекомендуемые исполнители\r\n" +
                "r_tracks - Отображение рекомендованной музыки\r\n" +
                "add_track - Поиск и добавление песни в плей лист\r\n" +
                "show_tracks - Отображение треков из плейлиста пользователя\r\n" +
                "get_playlist - Список существующих плей-листов пользователя\r\n" +
                "user - Информация о пользователе\r\n" +
                "Выполнение команды завершено, для продолжения введите следующую команду\r\n" +
                "\r\n" +
                "Завершение работы!\r\n" +
                "Завершение работы программы!";

        provideInput("help\nexit");
        MenuService menuService = new MenuService(new SpotifyMusicService(ACCESS_TOKEN));
        menuService.Run();
        Assert.assertEquals(testResult, getOutput().strip());
    }

    @Test
    public void shouldShowTracksFromPlaylist(){
        String testResult ="Введите команду для начала работы!\r\n" +
                "\r\n" +
                "Выберите плейлист, треки которого вы хотите посмотреть\r\n" +
                "Введите номер желаемого плейлиста или 7 для создания нового. Для отмены выберите последний пункт\r\n" +
                "1) Плей лист 'TestPlaylist3' автора 'ZERO' содержит 0 треков\r\n" +
                "2) Плей лист 'TestPlaylist' автора 'ZERO' содержит 2 трека\r\n" +
                "3) Плей лист 'TestPlaylist2' автора 'ZERO' содержит 0 треков\r\n" +
                "4) Плей лист 'Вроде Металл' автора 'ZERO' содержит 5 треков\r\n" +
                "5) Плей лист 'Good L_ck (Yo_'re F_cked) – Celldweller' автора 'ZERO' содержит 12 треков\r\n" +
                "6) Плей лист 'Let You Down – NF' автора 'ZERO' содержит 5 треков\r\n" +
                "7) Создать новый плейлист\r\n" +
                "8) Отмена действия\r\n" +
                "Треки из плейлиста TestPlaylist\r\n" +
                "1) 'Lakes of Flame' - 'Blue Stahli'\r\n" +
                "2) 'Give Into' - 'NUTRONIC'\r\n" +
                "Выполнение команды завершено, для продолжения введите следующую команду\r\n" +
                "\r\n" +
                "Завершение работы!\r\n" +
                "Завершение работы программы!";

        provideInput("show_tracks\n2\nexit");

        MenuService menuService = new MenuService(new SpotifyMusicService(ACCESS_TOKEN));
        menuService.Run();
        Assert.assertEquals(testResult, getOutput().strip());
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    private TrackSimplified[] getTrackSimplifiedMock() {
        provideInput("2");
        ApplicationProxy applicationProxy = new ApplicationProxy(new ConsoleService(), new SpotifyMusicService(ACCESS_TOKEN));
        Optional<PlaylistSimplified> optionalPlaylist = applicationProxy.selectPlaylist();
        if(optionalPlaylist.isPresent()){
            return  applicationProxy.getTracksFromPlayList(optionalPlaylist.get());
        }

        return new TrackSimplified[0];
    }
}
