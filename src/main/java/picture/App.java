package picture;

import picture.cmd.Server;
import picture.config.Config;
import picture.config.ConfigUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    public static void main(String[] args) {
        Server.notifyOldServer();
        ConfigUtils.getLocalSetting();
        Config config = Config.config;
        Gui gui = new Gui();
        new Thread(() -> Server.startServer(gui)).start();
        new Thread(() -> {
            while (true) {
                try {
                    if (config.isStart() && !Config.hasErr) {
                        LocalDateTime now = LocalDateTime.now();
                        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss").format(now);
                        CaptureTool.cutPic(format, config.getDefaultPath());
                    }
                } catch (Exception e) {
                    Config.hasErr = true;
                    gui.msg("不能保存文件，可能是文件路径乱码，请检查文件路径！");
                    System.out.println(e);
                }
                try {
                    Thread.sleep(config.getDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
