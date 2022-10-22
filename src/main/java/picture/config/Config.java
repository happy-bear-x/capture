package picture.config;

import java.io.Serializable;
import java.util.Objects;

public class Config implements Serializable {
    public static Config config = new Config();
    public static boolean hasErr = false;
    // 延迟默认5s
    private int delay = 5000;

    // 默认存储路径
    private String defaultPath = "C:\\myText\\text\\";


    private String settingFile = defaultPath + ".setting.ini";



    private boolean start = false;

    private boolean show = true;


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Config) {
            Config other = (Config) obj;
            if (this.getDelay() != other.getDelay()) {
                return false;
            }
            if (!Objects.equals(this.getDefaultPath(), other.getDefaultPath())) {
                return false;
            }
            if (this.isStart() != other.isStart()) {
                return false;
            }
        }
        return true;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public String getSettingFile() {
        return settingFile;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
