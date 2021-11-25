package snake_game_test;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class gamePanel extends JPanel implements ActionListener {
    private final int GAME_WIDTH = 600;
    private final int GAME_TRU_WIDTH = 800;

    private final int GAME_HEIGHT = 600;
    private final int CELL = 25;
    private final int UNIT = (GAME_HEIGHT/CELL);
    private final int UNITS = (GAME_HEIGHT*GAME_WIDTH)/(CELL*CELL);
    static final int DELAY = 70;
    private int x[] = new int[UNITS];
    private int y[] = new int[UNITS];
    private int body = 2;
    private int appleEaten = 0;
    private int appleX;
    private int appleY;
    private boolean applepos_check;
    boolean running = true;
    char direction = 'R';
    Timer timer;
    boolean timer_tran;
    Random rand;
    ClassLoader cl = this.getClass().getClassLoader();
    private JButton repButton = new JButton("Replay");
    private JButton clsButton = new JButton("Close");

    private BufferedImage image;
    private BufferedImage image2;


    {
        try {
            image = ImageIO.read(new File("resources/picture1.png"));
            image2 = ImageIO.read(new File("resources/doge.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public gamePanel(){
        rand = new Random();
        setPreferredSize(new Dimension(GAME_TRU_WIDTH, GAME_HEIGHT));
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        setBackground(Color.BLACK);
        gameStart();
        apple_spawn();
        repButton.setFont(new Font(null, Font.BOLD, 25));
        repButton.setBounds(250, 160, 125, 40);
        repButton.addActionListener(this);
        repButton.setBackground(Color.WHITE);
        repButton.setForeground(Color.BLACK);
        clsButton.setFont(new Font(null, Font.BOLD, 25));
        clsButton.setBounds(425, 160, 125, 40);
        clsButton.addActionListener(this);
        clsButton.setBackground(Color.WHITE);
        clsButton.setForeground(Color.BLACK);
    }

    public void gameStart(){
        if(true) {
            timer = new Timer(DELAY, this);
            timer.start();
            timer_tran = true;
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        paintgame(g);
    }
    public void paintgame(Graphics g){
        if (running){
//            for (int i = 0; i <= UNIT; i++){
//                g.drawLine(0, i*CELL, GAME_WIDTH, i*CELL);
//                g.drawLine(i*CELL, 0, i*CELL, GAME_HEIGHT);
//            }
            g.setColor(Color.WHITE);
            g.fillRect(UNIT*CELL, 0, 3, GAME_HEIGHT);
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, CELL, CELL);
            int color = 0;
            for (int i = 0; i < body; i++) {
                if (color < 240) {
                    color += 15;
                }
                g.setColor(new Color(15, 255, color));
                g.fillRect(x[i], y[i], CELL, CELL);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score" ,
                    GAME_WIDTH + (GAME_TRU_WIDTH - GAME_WIDTH - metrics.stringWidth("Score"))/2,
                    g.getFont().getSize() + GAME_HEIGHT/10);
            g.drawString(String.format("%05d", appleEaten) ,
                    GAME_WIDTH + (GAME_TRU_WIDTH - GAME_WIDTH - metrics.stringWidth(String.format("%05d", appleEaten) ))/2,
                    g.getFont().getSize()*2 + GAME_HEIGHT/8);


            g.drawImage(image, GAME_WIDTH - 25, GAME_HEIGHT/2, 235, 290, null);

        } else {
            gameOver(g);
        }
    }
    public void move(){
        for (int i = body; i >0 ; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'R':
                x[0] = x[0] + CELL;
                break;
            case 'L':
                x[0] = x[0] - CELL;
                break;
            case 'U':
                y[0] = y[0] - CELL;
                break;
            case 'D':
                y[0] = y[0] + CELL;
                break;
            default:
                break;
        }
    }
    public void apple_spawn(){
        while(true) {
            applepos_check = false;
            appleX = rand.nextInt(UNIT) * CELL;
            appleY = rand.nextInt(UNIT) * CELL;
            for (int i = 0; i < body; i++ ){
                if (appleX != x[i] && appleY != y[i])
                    applepos_check = true;
            }
            if (applepos_check){
                break;
            }
        }
    }
    public void an_tao_chua(){
        if(x[0] == appleX && y[0] == appleY){
            apple_spawn();
            body++;
            appleEaten++;
        }
    }
    public void checkcollision(){
        for (int i = body; i > 0; i--) {
            if (x[i] == x[0] && y[i] == y[0]){
                timer.stop();
                timer_tran = false;
                running = false;
            }
        }
        if (x[0] > GAME_WIDTH - UNIT || x[0] < -10 || y[0] > GAME_HEIGHT - UNIT || y[0] < -10){
            timer.stop();
            timer_tran = false;
            running = false;
        }
    }

    public void resetGame(){
        for (int i = 0; i < body; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        direction = 'R';
        appleEaten = 0;
        body = 2;
        running = true;
        remove(clsButton);
        remove(repButton);
        timer.start();
        timer_tran = true;
    }
    public void gameOver(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SERIF, Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + appleEaten, (GAME_TRU_WIDTH -
                metrics.stringWidth("Score: " + appleEaten))/2, metrics.getHeight());
        g.setFont(new Font(null, Font.BOLD, 70));
        g.drawString("You lose",
                (GAME_TRU_WIDTH - metrics.stringWidth("You lose"))/2 - 53,
                GAME_HEIGHT/2 - 170);
        g.drawImage(image2, 200, 200, 400, 400, null);


        add(repButton);
        add(clsButton);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            an_tao_chua();
            checkcollision();
        }
        repaint();
        if(e.getSource() == repButton){
            resetGame();
        }
        if(e.getSource() == clsButton){
            System.exit(0);
        }
    }

    public class MyKeyAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_SPACE:
                    if (timer_tran) {
                        timer.stop();
                        timer_tran = false;
                    } else {
                        timer.start();
                        timer_tran = true;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
//                        head_pic = head_L;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
//                        head_pic = head_R;

                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
//                        head_pic = head_U;

                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
//                        head_pic = head_D;

                    }
                    break;

            }
        }
    }
}
