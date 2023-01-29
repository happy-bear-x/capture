package picture.config;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigUtils {

    private static String splitSymbol = "@@";
    private static Config config = Config.config;

    /**
     * 获取本地设置
     */
    public static void getLocalSetting() {
        //首先创建一个目录
        File deftDir = new File(config.getDefaultPath());
        if (!deftDir.isDirectory() || !deftDir.exists()) {
            deftDir.mkdirs();
        }
        File file = new File(Config.config.getSettingFile());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        try {
            String setting = new String(Files.readAllBytes(Paths.get(config.getSettingFile())));
            Config config = strToConfig(setting);
            if (!Config.config.equals(config)) {
                updateConfig(config);
            }
        } catch (IOException ignored) {

        }
    }

    /**
     * 更新配置
     *
     * @param newConfig
     */
    public static void updateConfig(Config newConfig) {
        if (newConfig == null) {
            return;
        }
        config.setStart(newConfig.isStart());
        int delay = newConfig.getDelay();
        if (delay > 0) {
            if (delay < 500) {
                delay = 500;
            }
            config.setDelay(delay);
            System.out.println("delay:" + config.getDelay());
        }
        if (newConfig.getDefaultPath() != null && newConfig.getDefaultPath().length() > 0) {
            config.setDefaultPath(newConfig.getDefaultPath());
        }
        File file = new File(config.getSettingFile());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        try {
            FileWriter writer = new FileWriter(config.getSettingFile(), false);
            writer.write(configToStr(config));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Config.hasErr = false;
    }

    public static String configToStr(Config config) {
        return splitSymbol + config.getDelay() + splitSymbol + (config.isStart() ? 1 : 0) + splitSymbol
                + config.getDefaultPath() + splitSymbol + (config.isShow() ? 1 : 0);
    }

    public static Config strToConfig(String confStr) {
        Config config = new Config();
        String[] split = confStr.split(splitSymbol);
        if (split.length > 1) {
            try {
                config.setDelay(Integer.parseInt(split[0]));
                config.setStart("1".equals(split[1]));
                config.setDefaultPath(split[2]);
                config.setShow("1".equals(split[3]));
            } catch (Exception ignored) {
                System.out.println(confStr);
                ignored.printStackTrace();
                updateConfig(config);
            }
        }
        return config;
    }
}
