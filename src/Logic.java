import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    // attribut yang menyimpan tinggi dan lebar frame
    int frameWidth = 360;
    int frameHeight = 640;

    // attribut yang menyimpan nilai default player
    int playerSPX = frameWidth/2;
    int playerSPY = frameHeight/2;
    int playerWidth = 34;
    int playerHeight = 24;

    // attribut yang menyimpan nilai default pipa
    int pipeSPX = frameWidth;
    int pipeSPY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -2;

    // attribut physics game
    Timer gameloop;
    Timer pipeCd; // cooldown kemunculan pipa
    int gravity = 1;

    int gameState = 0; // gameState | 0 = main menu, 1 = Playing, 2 = Game Over
    // attribut view dan player
    View view;
    Image birdImage;
    Player player;
    int score = 0; // attribut penyimpan skor

    // attribut list pipa dan scorer
    Image upperPipeImg;
    Image lowerPipeImg;
    ArrayList<Pipe> pipes;
    ArrayList<Scorer> scorers; // scorers untuk list scorer

    public Logic(){
        // instansiasi player
        birdImage = new ImageIcon(getClass().getResource("assets/Image/bird.png")).getImage();
        player = new Player(playerSPX, playerSPY, playerWidth, playerHeight, birdImage);

        // instansiasi pipa
        upperPipeImg = new ImageIcon(getClass().getResource("assets/Image/upperPipe.png")).getImage();
        lowerPipeImg = new ImageIcon(getClass().getResource("assets/Image/lowerPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();
        scorers = new ArrayList<Scorer>();

        // instansiasi game loop
        gameloop = new Timer(1000/60, this);
        gameloop.start();

        // instansiasi cooldown pipa
        pipeCd = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pipa");
                placePipes();
            }
        });
    }

    // fungsi yang mengecek collision player dan pipa
    public boolean checkCollision(Player player, Pipe pipe){
        // simpan attribut player
        int playerX = player.getPosX();
        int playerY = player.getPosY();
        int playerW = player.getWidth();
        int playerH = player.getHeight();

        // simpan attribut pipa
        int pipeX = pipe.getPosX();
        int pipeY = pipe.getPosY();
        int pipeW = pipe.getWidth();
        int pipeH = pipe.getHeight();

        // logika pengecekan collision
        return playerX + playerW >= pipeX && playerX <= pipeX + pipeW && playerY + playerH >= pipeY && playerY <= pipeY + pipeH;
    }

    public void move(){
        // implementasi akselerasi gravitasi serta menerapkan pergerakan player
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));
        player.setPosY(Math.min(player.getPosY(), frameHeight - playerHeight));

        // mengecek apakah player menyentuh batas atas dan bawah frame
        if(player.getPosY() + player.getHeight() >= frameHeight || player.getPosY() <= 0){
            gameState = 2;
            pipeCd.stop();
            SoundPlayer.playSound("src/assets/audio/catLaugh.wav");
            return;
        }

        // menerapkan logika pipa
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX()); // menggerakkan pipa
            if(checkCollision(player, pipe)){ // cek collision pipa dan player
                gameState = 2;
                pipeCd.stop();
                SoundPlayer.playSound("src/assets/audio/catLaugh.wav");
                return;
            }
            if(pipe.getPosX() + pipe.getWidth() < 0){ // cek apakah pipa sudah melewati batas kiri frame
                pipes.remove(pipe);
                i--;
            }
        }
        // menerapkan logika scorer
        for(int i = 0; i < scorers.size(); i++){
            Scorer scorer = scorers.get(i);
            scorer.setPosX(scorer.getPosX() + scorer.getVeloX()); // menggerakkan scorer
            if(scorer.getPosX() <= player.getPosX()){ // cek apakah scorer sudah dilewati player
                score++;
                scorers.remove(scorer);
            }
        }
    }

    // method merestart game
    public void restart(){
        player.setPosX(playerSPX);
        player.setPosY(playerSPY);
        player.setVelocityY(0);
        score = 0;
        pipes.clear();
        scorers.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e){ // method terpanggil selama berjalannya program
        if(gameState == 1){ // jika gameState sedang playing
            move();
            if(view != null){
                view.repaint();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e){

    }
    public void keyPressed(KeyEvent e){
        if(gameState == 0) { // jika gameState sedang di menu
            if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP){
                gameState = 1;
                pipeCd.start();
            }else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                System.exit(0);
            }
        }else if(gameState == 1){ // jika gameState sedang Playing
            if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                player.setVelocityY(-10);
                SoundPlayer.playSound("src/assets/audio/shortAugh.wav");
            }
        }else{ // jika gameState sedang game over
            if (e.getKeyCode() == KeyEvent.VK_R) {
                gameState = 1;
                pipeCd.restart();
                restart();
            }
        }
    }
    public void keyReleased(KeyEvent e){

    }

    public void setView(View view){
        this.view = view;
    }

    public Player getPlayer(){
        return player;
    }

    public ArrayList<Pipe> getPipes(){
        return pipes;
    }

    public void placePipes(){ // method meletakkan pipa
        // inisialisasi random posisi Y
        int randomPosY = (int) (pipeSPY - (pipeHeight / 4.0) - Math.random() * (pipeHeight / 2.0));
        int openingSpace = frameHeight / 4;

        // instansiasi scorer baru yang mengikuti pipa
        Scorer scorer = new Scorer(pipeSPX, pipeVelocityX);
        scorers.add(scorer);

        // instansiasi pipa atas
        Pipe upperPipe = new Pipe(pipeSPX, randomPosY, pipeWidth, pipeHeight, upperPipeImg, pipeVelocityX);
        pipes.add(upperPipe);

        // instansiasi pipa bawah
        Pipe lowerPipe = new Pipe(pipeSPX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImg, pipeVelocityX);
        pipes.add(lowerPipe);
    }

    public int getScore(){
        return score;
    }

    public int getGameState(){
        return gameState;
    }
}
