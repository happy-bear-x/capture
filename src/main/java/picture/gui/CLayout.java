package picture.gui;

import javax.swing.*;
import java.awt.*;

public class CLayout extends JPanel {

    public CLayout(int height, Component text, Component... components) {
        super();
        this.setBounds(0, height, 300, 30);
        this.setLayout(new FlowLayout());
        this.add(text);
        for (Component component : components) {
            this.add(component);
        }
    }
}
