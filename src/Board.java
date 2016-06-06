import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by HadiDeknache on 2016-06-06.
 */
public class Board extends JPanel implements ActionListener {
    private final int dot = 10;
    private final int width = 550;
    private final int height = 550;
    private final int allDots = 900;
    private final int randPos = 29;
    private final int delay= 140;

    private final int y[] =new int [allDots];
    private final int x[] = new int [allDots];
    private int dots;
    private int foodx;
    private int foody;

    private boolean leftDirection = false;
    private boolean rigthDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean gameOn = true;

    private  Timer timer;
    private Image body;
    private Image food;
    private Image head;
    private int playMsg;
    private int points=0;

    public Board(){
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setVisible(true);

        setPreferredSize(new Dimension(width,height));
        setImages();
        initGame();
    }
    private void setImages(){
        ImageIcon imgbody = new ImageIcon("/Users/HadiDeknache/IdeaProjects/SnakeGame/body.png");

        ImageIcon imgFood = new ImageIcon("/Users/HadiDeknache/IdeaProjects/SnakeGame/food.png");

        ImageIcon imgHead = new ImageIcon("/Users/HadiDeknache/IdeaProjects/SnakeGame/head.png");

        body = imgbody.getImage();
        food = imgFood.getImage();
        head = imgHead.getImage();
    }

    private void initGame(){
        dots = 3;

        for (int i = 0; i<dots; i++){
            x[i] = 50 - i * 10;
            y[i] = 50;
        }
        locateFood();
        timer = new Timer(delay,this);
        timer.start();
    }
    @Override
    public  void paintComponent (Graphics g){
        super.paintComponent(g);
        draw(g);


    }
    private void draw(Graphics g){
        if(gameOn){
            g.drawImage(food,foodx,foody,this);
            setPoints(g);
            for(int m=0; m<dots;m++){
                if (m==0){
                    g.drawImage(head,x[m],y[m],this);
                }
                else{
                    g.drawImage(body,x[m],y[m],this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        }
        else{
            gameOver(g);
            if(!gameOn){
                playMsg= JOptionPane.showConfirmDialog(null,"Do you want to play again?","Play again?",JOptionPane.YES_NO_OPTION);
                if(playMsg == JOptionPane.YES_OPTION){
                    gameOn=true;
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JFrame ex = new Snake();
                            ex.setVisible(true);
                        }
                    });

                }
                else{
                    System.exit(1);
                }
            }
        }
    }
    private void gameOver(Graphics g){
        String msg = "Game Over!";
        Font small = new Font("Helvetica",Font.ITALIC,16);
        FontMetrics metrics = getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg,(width-metrics.stringWidth(msg))/2,height/2);

    }
    private void setPoints(Graphics g){
        String point = Integer.toString(points);
        Font small = new Font("Helvetica",Font.ITALIC,14);
        FontMetrics metrics = getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(point,(50-metrics.stringWidth(point))/2,50/2);
    }
    private void checkFood(){
        if((x[0]==foodx) && (y[0]==foody)){
            AudioClip clip = null;
            {
                try {
                    URL url = new File("/Users/HadiDeknache/IdeaProjects/SnakeGame/eat.wav").toURI().toURL();//filens placering
                    clip = Applet.newAudioClip(url);
                } catch (MalformedURLException e) {
                }
                if (clip != null) {
                    clip.play();
                }
            }
            dots+=2;
            points+=10;
            locateFood();
        }
    }
    private void move(){
        for(int z = dots; z>0;z--){
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }
        if (leftDirection){
            x[0] -= dot;
        }
        if(rigthDirection){
            x[0] += dot;
        }
        if(upDirection){
            y[0] -=dot;
        }
        if(downDirection){
            y[0] +=dot;
        }
    }

    private void checkCollision(){
        for(int v = dots; v > 0; v--){
            if ((v > 4) && (x[0]==x[v]) && (y[0] == y[v])){
                gameOn = false;
            }
        }
        if (y[0] >= height) {
            gameOn = false;
        }

        if (y[0] < 0) {
            gameOn = false;
        }

        if (x[0] >= width) {
            gameOn = false;
        }

        if (x[0] < 0) {
            gameOn = false;
        }

        if(!gameOn) {
            timer.stop();

        }
    }
    private void playSound(){
    String bip = "/Users/HadiDeknache/IdeaProjects/SnakeGame/eat.wav";
    Media hit = new Media(bip);
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    mediaPlayer.play();
    }
    private void locateFood(){
        int rand = (int) (Math.random() * randPos);
        foodx = ((rand * dot));

        rand= (int) (Math.random() * randPos);
        foody = ((rand*dot));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameOn){
            checkFood();
            checkCollision();
            move();
        }
        repaint();
    }
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rigthDirection)){
                leftDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)){
                rigthDirection = true;
                upDirection=false;
                downDirection=false;
            }
            if ((key==KeyEvent.VK_UP) && (!downDirection)){
                upDirection=true;
                rigthDirection = false;
                leftDirection = false;
            }
            if((key==KeyEvent.VK_DOWN)&&(!upDirection)){
                downDirection=true;
                leftDirection=false;
                rigthDirection=false;
            }

        }

    }


}
