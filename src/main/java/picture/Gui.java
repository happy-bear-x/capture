package picture;

import picture.config.Config;
import picture.config.ConfigUtils;
import picture.gui.CLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gui extends JFrame {
    private Config config = Config.config;
    private TextField delay = new TextField(30);
    private JRadioButton enable = new JRadioButton("启用");
    private JRadioButton disable = new JRadioButton("停用");
    private ButtonGroup buttonGroup = new ButtonGroup();
    private String path = config.getDefaultPath();

    {
        delay.setText(config.getDelay() + "");
        buttonGroup.add(enable);
        buttonGroup.add(disable);
    }

    public Gui() throws HeadlessException {

        super("便签");
        this.setBounds(500, 350, 300, 250);
        this.setLayout(null);
        this.add(new CLayout(0, delay));

        if (config.isStart()) {
            enable.setSelected(true);
        } else {
            disable.setSelected(true);
        }
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
        this.add(new CLayout(100, new JLabel("目录："), patchLabel));
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
        this.setIconImage(new ImageIcon("C:\\Users\\admin\\Desktop\\icon.png").getImage());
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
