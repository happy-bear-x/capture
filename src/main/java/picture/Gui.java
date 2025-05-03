package picture;

import picture.config.Config;
import picture.config.ConfigUtils;
import picture.gui.CLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Gui extends JFrame {
    private Config config = Config.config;
    private TextField delay = new TextField(30);
    private JRadioButton enable = new JRadioButton("启用");
    private JRadioButton disable = new JRadioButton("停用");
    private JCheckBox autoStart = new JCheckBox();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private String path = config.getDefaultPath();
    static String appName = "Windows 启动应用程序";

    {
        delay.setText(config.getDelay() + "");
        buttonGroup.add(enable);
        buttonGroup.add(disable);
    }

    public Gui() throws HeadlessException {
        super(appName);
        this.setBounds(500, 350, 300, 250);
        this.setLayout(null);
        this.add(new CLayout(0, delay));

        if (config.isStart()) {
            enable.setSelected(true);
        } else {
            disable.setSelected(true);
        }
        autoStart.setSelected(config.isAutoStart());
        this.add(new CLayout(50, new JLabel("状态："), enable, disable));
        JLabel patchLabel = new JLabel("<html><a href=''>" + path + "</a></html>");
        patchLabel.setSize(100, 20);
        patchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int opt = fileChooser.showOpenDialog(Gui.this);
                if (opt == JFileChooser.APPROVE_OPTION) {
                    String newPath = fileChooser.getSelectedFile().getPath() + "\\";
                    path = newPath;
                    String newPatchText = "<html><a href=''>" + newPath + "</a></html>";
                    patchLabel.setText(newPatchText);
                }
            }

        });
        this.add(new CLayout(85, new JLabel("目录："), patchLabel));
        this.add(new CLayout(110, new JLabel("开机启动："), autoStart));
        JButton confirm = new JButton("确定");
        JButton hid = new JButton("隐藏");
        confirm.addActionListener(e -> {
            String text = delay.getText();
            if (text.matches("\\d+")) {
                int delay = Integer.parseInt(text);
                Config config = new Config();
                config.setDelay(delay);
                config.setStart(enable.isSelected());
                config.setDefaultPath(path);
                config.setAutoStart(false);
                try {
                    String appPath = System.getProperty("user.dir") + "\\Windows 启动应用程序.exe auto"; // 替换为你的程序路径
                    System.out.println(appPath);
                    if (autoStart.isSelected()) {
                        Runtime.getRuntime().exec(new String[]{"reg", "add", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "/v", appName, "/t", "REG_SZ", "/d", appPath, "/f"});
                        config.setAutoStart(true);
                    } else {
                        Runtime.getRuntime().exec(new String[]{"reg", "delete", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "/v", appName, "/f"});
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "设置开机启动失败：" + ex.getMessage());
                }
                ConfigUtils.updateConfig(config);
                JOptionPane.showMessageDialog(null, "操作成功");
            } else {
                JOptionPane.showMessageDialog(null, "成功");
            }
        });
        hid.addActionListener(e -> this.setVisible(false));
        this.add(new CLayout(150, hid, confirm));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        setLocationRelativeTo(null);
        try {
            Image icon = ImageIO.read(getClass().getResource("/icon.png"));
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 暂时注掉显示
//        this.setVisible(true);
    }


    public void msg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void showGui() {
        this.setVisible(true);
    }

    public void hideGui() {
        this.setVisible(false);
    }

    ;
}
