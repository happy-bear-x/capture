package picture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CaptureTool {
    // 图片类型
    public static String IMG_TYPE = "jpg";

    public static void cutPic(String imageName, String path) throws AWTException, IOException {
        if (!path.endsWith("\\") || !path.endsWith("/")) {
            path = path + "/";
        }
        System.out.println("开始截图");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        //判断文件是否存在，存在就改名字
        String fileName = imageName + "." + IMG_TYPE;
        String pathname = path + fileName;
        while (new File(pathname).exists()) {
            pathname = path + imageName + "_1." +IMG_TYPE;
        }
        ImageIO.write(image, IMG_TYPE, new File(pathname));
    }


}
