import javax.swing.*;
import java.awt.*;

/**
 * Created by HadiDeknache on 2016-06-06.
 */
public class Snake extends JFrame{
    public Snake(){

        add(new Board());
        setResizable(false);
        pack();
        setTitle("Snake-Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame ex = new Snake();
                ex.setVisible(true);
            }
        });
    }
}
