import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_HEIGHT) / 25;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyparts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(80, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            g.drawImage(Toolkit.getDefaultToolkit().getImage("rasen_hintergrund.png"), 0, 0, this);
            g.setColor(new Color(255, 255, 255, 88));
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyparts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Rubik", Font.BOLD, 26));
            g.drawString("Punkte: " + applesEaten, 30, 30);

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        // Erzeugen eines neuen Apfels
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        // die hinteren Körperteile der Schlange werden in um eine Position verschoben
        for (int i = bodyparts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Brechnen der neuen Position des Kopfes anhand der Richtung
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        // überprüpft, ob die Schlange den Apfel erreicht, verlängert die Schlange und
        // generiert einen neuen Apfel und aktualisiert den Punktestand
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyparts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {

        // überprüft, ob der Kopf der Schlange mit dem eigenen Körper kollidiert
        for (int i = bodyparts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // Überprüft, ob die Schlange mit dem Rand kollidiert
        // links
        if (x[0] < 0) {
            running = false;
        }
        // rechts
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // oben
        if (y[0] < 0) {
            running = false;
        }
        // unten
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
    }

    public void gameOver(Graphics g) {
        // Anzeige des Fensters, wenn das Spiel vorbei ist
        g.setColor(Color.RED);
        g.setFont(new Font("Rubik", Font.BOLD, 100));
        g.drawString("GAME OVER", 100, SCREEN_HEIGHT / 2);
        g.setFont(new Font("Rubik", Font.BOLD, 35));
        g.setColor(Color.ORANGE);
        g.drawString("Punkte: " + applesEaten, 300, SCREEN_HEIGHT - 50);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
