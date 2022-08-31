

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Model extends JPanel implements ActionListener {

    private Dimension d;
    private static int dyingSecondsCount=5;
    public static ScheduledExecutorService executor ;
    private final Font smallFont = new Font("Arial",Font.BOLD, 18);
    private boolean inGame = false;
    private boolean powerup=false;
    private boolean dying=false;
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 12;
    private final int PacMan_SPEED = 6;

    private int N_GHOSTS = 4;
    private int lives, score;

    private String highScore= "";
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

    private Image heart, ghost, scaredghost;
    private Image up, down, left, right;

    private Image title;

    //Sound sound=new Sound();

    private int PacMan_x, PacMan_y, PacMand_x, PacMand_y;
    private int req_dx, req_dy;

    private final int[] levelData = {


            19, 26, 26, 18, 18, 26, 18, 18, 18, 26, 18, 18, 26, 26, 22,
            21, 0, 0, 17, 20, 0, 17, 16, 20, 0, 17, 20, 0, 0, 21,
            21, 0, 35, 16, 20, 0, 25, 24, 28, 0, 17, 16, 38, 0, 21,
            17,  26,  16,  16,  20,  0, 0, 0, 0, 0, 17, 16, 16, 26, 20,
            21, 0, 17, 24, 16, 18, 18, 18, 18, 18, 16, 24, 20, 0, 21,
            21, 0, 21, 0, 17, 16, 24, 16, 24, 16, 20, 0, 21,0 , 21,
            17, 26, 28, 0, 17, 20, 0, 21, 0, 17, 20,  0,  25,  26, 20,
            21, 0, 0, 0, 17, 20, 0, 29, 0, 17, 20, 0, 0 ,  0, 21,
            17, 26, 22, 0, 17, 20, 0, 0, 0, 17, 20, 0, 19, 26, 20,
            21, 0, 21, 0, 17, 16, 18, 18, 18, 16, 20, 0, 21, 0, 21,
            21, 0, 17,18, 16, 24, 24, 24, 24, 24, 16, 18, 20, 0, 21,
            17, 26,  16,  16,  20,  0,  0,  0, 0, 0, 17, 16, 16, 26, 20,
            21, 0, 41, 16, 20, 0, 19, 18,22, 0, 17, 16, 44, 0, 21,
            21, 0, 0, 17, 20, 0, 17, 16, 20, 0, 17, 20, 0, 0, 21,
            25, 26, 26, 24, 24, 26, 24, 24, 24, 26, 24, 24, 26, 26, 28


    };

    private final int[] level2data={

            35, 26, 26, 18, 18, 22,  0,  0,  0, 19, 18, 18, 26, 26, 38,
            21, 0, 0, 17, 16, 16, 26, 26, 26, 16, 16, 20, 0, 0, 21,
            25, 26, 18, 16, 16, 20, 0, 0, 0, 17, 16, 16, 18, 26, 28,
            0,  0,  17,  16,  24,  16, 22, 0, 19, 16, 24, 16, 20, 0, 0,
            19, 18, 16, 20, 0, 17, 20, 0, 17, 20, 0, 17, 16, 18, 22,
            17, 16, 24, 28, 0, 17, 16, 18, 16, 20, 0, 25, 24,  16, 20,
            17, 20, 0, 0, 0, 17, 24, 24, 24, 20, 0,  0,  0,   17, 20,
            17, 16, 26, 26, 26, 20, 0, 0, 0, 17, 26, 26,  26,  16, 20,
            17, 20, 0, 0, 0, 17, 18, 18, 18, 20, 0, 0, 0, 17, 20,
            17, 16, 18, 22, 0, 17, 16, 24, 16, 20, 0, 19, 18, 16, 20,
            25, 24,  16,  20,  0,  17,  20,   0, 17, 20, 0, 17, 16, 24, 28,
            0, 0, 17, 16, 18, 16, 28, 0, 25, 16, 18, 16, 20, 0, 0,
            19, 26, 24, 16, 16, 20, 0, 0, 0, 17, 16, 16, 24, 26, 22,
            21, 0, 0, 17, 16, 16, 26, 26, 26, 16, 16, 20, 0, 0, 21,
            41, 26, 26, 24, 24, 28, 0, 0, 0, 25, 24, 24, 26, 26, 44

    };

    private final int[] validSpeeds = {1, 2, 3, 4, 5, 6};
    private final int maxSpeed = 6;

    private int currentSpeed = 2;
    private int[] screenData;

    private Timer timer;

    public Model() {

        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }


    private void loadImages() {
        down = new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\down.gif").getImage();
        up = new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\up.gif").getImage();
        left = new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\left.gif").getImage();
        right = new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\right.gif").getImage();
        ghost = new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\ghost.gif").getImage();
        heart = new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\heart.png").getImage();
        title = new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\title.jpg").getImage();
        scaredghost=new ImageIcon("C:\\Users\\User\\Desktop\\Diplom\\PacMan\\images\\scaredghost.gif").getImage();
    }
    private void initVariables() {

        screenData = new int[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(70, this);
        timer.start();
    }

    private void playGame(Graphics2D g2d) throws IOException {

        if (dying) {

            death();

        } else {

            movePacMan();
            drawPacMan(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g2d) {

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);


            String start = "Press SPACE to start";
            g2d.setColor(Color.yellow);
            g2d.drawString(start, 87, 185);

        g2d.drawString("HighScore: "+highScore, 95, 300);



    }

    private void showGameOverScreen(Graphics g2d){
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);
        String title = "GAME OVER";
        String finalscore="Your final score was:";
        String restart = "Press SPACE to restart";
        g2d.setColor(Color.yellow);
        g2d.drawString(title,130,170);
        g2d.drawString(finalscore+score, 85, 192);
        g2d.drawString("HighScore: "+highScore, 95, 222);
        g2d.drawString(restart, 90, 250);


    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(0, 255, 0));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 76, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE+1, this);
        }
    }

    public void checkScore() throws IOException {

        System.out.println(highScore);

        if(score>Integer.parseInt((highScore.split(":")[1]))){
            //new record
            String name=JOptionPane.showInputDialog("New high score! What is your name?");
            highScore=name+":"+score;

            File scoreFile=new File("highscore.dat");
            if(!scoreFile.exists())
            {
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            FileWriter writeFile=null;
            BufferedWriter writer=null;
            try{
                writeFile=new FileWriter(scoreFile);
                writer=new BufferedWriter(writeFile);
                writer.write(this.highScore);
            } catch (Exception e) {
                //errors
            }
            finally {
                try {
                    if (writer != null)
                        writer.close();
                }
                catch (Exception e) {

                }
            }

        }
    }

    private void drawPowerUpTime(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(255, 170, 0));
        String s = "Power Up: " + dyingSecondsCount;
        g.drawString(s, SCREEN_SIZE / 2-50, SCREEN_SIZE + 16);

    }

    private void checkMaze() {

        int i = 0;
        boolean finishedLevel1 = false;

        while (i < N_BLOCKS * N_BLOCKS && finishedLevel1) {
                finishedLevel1=true;
            i++;
        }

        if(score==182||score==374||score==566)
        {
            finishedLevel1=true;
        }

        if (finishedLevel1) {

            score += 10;

            if(N_GHOSTS<MAX_GHOSTS) {
                N_GHOSTS++;
            }

            if(lives<3)
            {
                lives++;
            }

            initLevel();
        }
    }

    private void death() throws IOException {

        lives--;

        if (lives == 0) {
            inGame = false;
            checkScore();
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {

        int pos;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }

            }
            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);

            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            if (PacMan_x > (ghost_x[i] - 12) && PacMan_x < (ghost_x[i] + 12)
                    && PacMan_y > (ghost_y[i] - 12) && PacMan_y < (ghost_y[i] + 12)
                    && inGame && powerup) {

                //ghost_y[i] = 110;
                //ghost_x[i] = 165;

            }else if (PacMan_x > (ghost_x[i] - 12) && PacMan_x < (ghost_x[i] + 12)
                    && PacMan_y > (ghost_y[i] - 12) && PacMan_y < (ghost_y[i] + 12)
                    && inGame) {

                dying = true;

            }


        }
    }


    private void drawGhost(Graphics2D g2d, int x, int y) {
        if(!powerup){
            g2d.drawImage(ghost, x, y, this);
        }
        else if(powerup)
        {
            g2d.drawImage(scaredghost, x, y, this);
        }
    }


    private void movePacMan() {

        int pos;
        int ch;

        if (PacMan_x % BLOCK_SIZE == 0 && PacMan_y % BLOCK_SIZE == 0) {
            pos = PacMan_x / BLOCK_SIZE + N_BLOCKS * (int) (PacMan_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (int) (ch & 15);
                score++;
            }

            if ((ch & 32) != 0) {
                if(!powerup) {

                    powerup = true;
                    dyingSecondsCount = 5;
                    executor = Executors.newScheduledThreadPool(0);
                    executor.scheduleAtFixedRate(setDying, 0, 1, TimeUnit.SECONDS);
                    N_GHOSTS=N_GHOSTS-1;
                }
                else if(powerup)
                {
                    dyingSecondsCount = 5;
                    screenData[pos] = (int) (ch & 15);
                    score += 5;
                    if(lives!=3)
                    {
                        lives++;
                    }
                }


            }



            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    PacMand_x = req_dx;
                    PacMand_y = req_dy;
                }
            }

            // Check for standstill
            if ((PacMand_x == -1 && PacMand_y == 0 && (ch & 1) != 0)
                    || (PacMand_x == 1 && PacMand_y == 0 && (ch & 4) != 0)
                    || (PacMand_x == 0 && PacMand_y == -1 && (ch & 2) != 0)
                    || (PacMand_x == 0 && PacMand_y == 1 && (ch & 8) != 0)) {
                PacMand_x = 0;
                PacMand_y = 0;
            }
        }
        PacMan_x = PacMan_x + PacMan_SPEED * PacMand_x;
        PacMan_y = PacMan_y + PacMan_SPEED * PacMand_y;
    }


    private void drawPacMan(Graphics2D g2d) {

        if (req_dx == -1) {
            g2d.drawImage(left, PacMan_x + 1, PacMan_y + 1, this);
        } else if (req_dx == 1) {
            g2d.drawImage(right, PacMan_x + 1, PacMan_y + 1, this);
        } else if (req_dy == -1) {
            g2d.drawImage(up, PacMan_x + 1, PacMan_y + 1, this);
        } else {
            g2d.drawImage(down, PacMan_x + 1, PacMan_y + 1, this);
        }
    }



    private void drawMaze(Graphics2D g2d) {

        int i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(25,25,166));
                g2d.setStroke(new BasicStroke(5));

                if ((levelData[i] == 0)) {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                if (((screenData[i]&32)!=0))
                {
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 5, y + 5, 12, 12);
                }

                i++;
            }
        }
    }

    private void initGame() {

        lives = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 4;
        currentSpeed = 2;

        //playMusic(0);
    }

    private void initLevel() {

        int i;

        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }


        continueLevel();
    }

    private void continueLevel() {

        int dx = 1;
        int random;

        for (int i = 0; i < N_GHOSTS; i++) {

            ghost_y[i] = 3 * BLOCK_SIZE; //start position
            ghost_x[i] = 4 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        PacMan_x = 10 * BLOCK_SIZE;  //start position
        PacMan_y = 11 * BLOCK_SIZE;
        PacMand_x = 0;	//reset direction move
        PacMand_y = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        dying = false;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);

        if(powerup==true)
        {
            drawPowerUpTime(g2d);
        }

        if (inGame) {
            try {
                playGame(g2d);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (lives==0){
            showGameOverScreen(g2d);
        } else {
            showIntroScreen(g2d);
            g.drawImage(title, 7, 95, 350, 70, this) ;
        }

        if (highScore.equals(""));
        {
            highScore=this.getHighScoreValue();
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }



    //controls
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }


    }
    Runnable setDying= new Runnable() {
        public void run() {
            powerup = true;
            dyingSecondsCount--;

            if(dyingSecondsCount==0){
                powerup=false;
                N_GHOSTS=N_GHOSTS+1;
                executor.shutdownNow();
            }
        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public String getHighScoreValue()
    {
        //format: Ivan:100
        FileReader readFile=null;
        BufferedReader reader=null;
        try {
             readFile =new FileReader("highscore.dat");
             reader = new BufferedReader(readFile);
            return reader.readLine();
        } catch (Exception e) {
            return "nobody:0";
        }
        finally {
            try {
                if(reader!=null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

   /* public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic()
    {
        sound.stop();
    }

    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }
*/
}