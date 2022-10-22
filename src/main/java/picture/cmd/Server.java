package picture.cmd;

import picture.ExecutorUtils;
import picture.Gui;
import picture.config.Config;
import picture.config.ConfigUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class Server {

    static Map<Socket, Thread> socketThreadMap = new HashMap<>();


    /**
     * 默认自身server端口
     */
    private static int port = 2022;


    public static int SECOND_PORT = 8;

    private static ExecutorService executorService = ExecutorUtils.getExecService();

    public static void startServer(Gui gui) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
//            cleanConnect();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(() -> {
                    String str = NetUtil.getMsg(clientSocket);
                    if (str.length() > 0) {
                        if (NetUtil.OPEN.equals(str)) {
                            try {
                                clientSocket.shutdownInput();
                                NetUtil.sendMsg(clientSocket, NetUtil.OPEN);
                                clientSocket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Config config = ConfigUtils.strToConfig(str);
                        gui.setVisible(config.isShow());
                        ConfigUtils.updateConfig(config);
                    }
                });
                thread.start();
                socketThreadMap.put(clientSocket, thread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAlive(Socket socket) {
        try {
            socket.sendUrgentData(0xFF);
            return true;
        } catch (IOException ignored) {

        }
        return false;
    }

    public static void sendConfig(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(Config.config);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void notifyClient() {
        for (Socket socket : socketThreadMap.keySet()) {
            sendConfig(socket);
        }
    }

    public static void notifyOldServer() {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(NetUtil.getBytes(NetUtil.OPEN));
            outputStream.flush();
            socket.shutdownOutput();
            String str = NetUtil.getMsg(socket);
            // 前服务收到消息，后面的程序停止运行
            if (NetUtil.OPEN.equals(str)) {
                System.exit(0);
            }
        } catch (Exception ignored) {
        }
    }

    private static void cleanConnect() {
        executorService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
                if (!socketThreadMap.isEmpty()) {
                    for (Socket socket : socketThreadMap.keySet()) {
                        if (!isAlive(socket)) {
                            Thread thread = socketThreadMap.get(socket);
                            if (Objects.nonNull(thread)) {
                                thread.interrupt();
                            }
                            socketThreadMap.remove(socket);
                        }
                    }
                }
            }
        });
    }


}
