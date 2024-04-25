import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    //image attributes
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    //player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    //int velocityY;
    Player player;

    //pipes attributes
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;
    Timer gameLoop;
    Timer pipesCooldown;

    int gravity = 1;

    JLabel scoreLabel;

    //constructor
    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        //setBackground(Color.blue);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("pipa");
                placePipes();
            }
        });
        pipesCooldown.start();
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        // Initialize score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel, BorderLayout.NORTH);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
    }

    public void move () {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        if (player.getPosY() + player.getHeight() >= frameHeight) {
            gameOver();
            return;
        }
        // Memeriksa tabrakan
        if (checkCollision()) {
            gameOver();
            return; // Jangan melanjutkan pergerakan jika game over
        }

        // Memeriksa peningkatan skor
        increaseScore();

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
        }

        // Menghapus pipa yang sudah melewati layar
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            if (pipe.getPosX() + pipe.getWidth() < 0) {
                pipes.remove(pipe);
                i--; // Mengurangi indeks karena elemen dihapus
            }
        }
        // Update score label
        updateScoreLabel();

        repaint();
    }

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSPace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSPace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public boolean checkCollision() {
        Rectangle playerBounds = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
        for (Pipe pipe : pipes) {
            Rectangle pipeBounds = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight());
            if (playerBounds.intersects(pipeBounds)) {
                return true; // Tabrakan terdeteksi
            }
        }
        return false; // Tidak ada tabrakan
    }

    // Update the score label
    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }
    int score = 0;

    public void increaseScore() {
        // Buat variabel boolean untuk melacak apakah pemain telah melewati sepasang pipa atas dan bawah pada iterasi saat ini
        boolean passedPair = false;
        for (int i = 0; i < pipes.size() - 1; i += 2) {
            Pipe upperPipe = pipes.get(i);
            Pipe lowerPipe = pipes.get(i + 1);
            // Periksa apakah sepasang pipa atas dan bawah telah dilewati dan pemain belum melewati sepasang tersebut pada iterasi sebelumnya
            if (upperPipe.getPosX() + upperPipe.getWidth() < player.getPosX() && !upperPipe.getPassed() && !passedPair) {
                score++;
                upperPipe.setPassed(true);
                lowerPipe.setPassed(true);
                passedPair = true; // Setel menjadi true agar sepasang pipa hanya dihitung sekali
            }
        }
    }



    public void gameOver() {
        gameLoop.stop();
        pipesCooldown.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score + "\npress R to restart the game ", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        }else if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }
    public void restartGame() {
        // Mengatur ulang semua variabel dan nilai skor
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        updateScoreLabel();

        // Mulai ulang timer untuk permainan dan pembuatan pipa
        gameLoop.start();
        pipesCooldown.start();

        // Membuat frame menjadi fokus kembali agar tombol dapat didengar
        requestFocus();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
