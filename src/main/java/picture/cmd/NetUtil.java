package picture.cmd;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NetUtil {
    private static String PASS = "abc_123_abc_xcd";
    public static String OPEN = "CLOSE";
    public static String OPEN_MSG = PASS + OPEN;


    public static boolean sendMsg(Socket socket, String msg) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(getBytes(msg));
            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String getMsg(Socket socket) {
        try {
            return readBytes(socket.getInputStream().readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] getBytes(String msg) {
        return (PASS + msg).getBytes();
    }

    public static String readBytes(byte[] bytes) {
        String widthPass = new String(bytes);
        if (widthPass.startsWith(PASS)) {
            return widthPass.substring(PASS.length());
        }
        return "";
    }
}
