import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerSPX = frameWidth/2;
    int playerSPY = frameHeight/2;
    int playerWidth = 34;
    int playerHeight = 24;

    int pipeSPX = frameWidth;
    int pipeSPY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -2;


    Timer gameloop;
    Timer pipeCd;
    int gravity = 1;

    int gameState = 0;
    View view;
    Image birdImage;
    Player player;
    int score = 0;

    Image upperPipeImg;
    Image lowerPipeImg;
    ArrayList<Pipe> pipes;
    ArrayList<Scorer> scorers;

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerSPX, playerSPY, playerWidth, playerHeight, birdImage);

        upperPipeImg = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        lowerPipeImg = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();
        scorers = new ArrayList<Scorer>();

        gameloop = new Timer(1000/60, this);
        gameloop.start();

        pipeCd = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pipa");
                placePipes();
            }
        });
    }

    public boolean checkCollision(Player player, Pipe pipe){
        int playerX = player.getPosX();
        int playerY = player.getPosY();
        int playerW = player.getWidth();
        int playerH = player.getHeight();

        int pipeX = pipe.getPosX();
        int pipeY = pipe.getPosY();
        int pipeW = pipe.getWidth();
        int pipeH = pipe.getHeight();

        return playerX + playerW >= pipeX && playerX <= pipeX + pipeW && playerY + playerH >= pipeY && playerY <= pipeY + pipeH;
    }

    public void move(){
        // player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));
        player.setPosY(Math.min(player.getPosY(), frameHeight - playerHeight));

        for(Pipe pipe : pipes){
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
            if(checkCollision(player, pipe)){
                gameState = 2;
                pipeCd.stop();
            }
        }
        for(Scorer scorer : scorers){
            scorer.setPosX(scorer.getPosX() + scorer.getVeloX());
            if(scorer.getPosX() <= player.getPosX()){
                score++;
                scorers.remove(scorer);
            }
        }
    }

    public void restart(){
        player.setPosX(playerSPX);
        player.setPosY(playerSPY);
        score = 0;
        pipes.clear();
        scorers.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(gameState == 1){
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
        if(gameState == 0) {
            gameState = 1;
            pipeCd.start();
        }else if(gameState == 1){
            if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                player.setVelocityY(-5);
            }
        }else{
            if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                gameState = 1;
                pipeCd.restart();
                restart();
            }
        }
    }
    public void keyReleased(KeyEvent e){
        if(gameState == 1) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
                player.setVelocityY(5);
            }
        }
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

    public void placePipes(){
        int randomPosY = (int) (pipeSPY - (pipeHeight / 4.0) - Math.random() * (pipeHeight / 2.0));
        int openingSpace = frameHeight / 4;

        Scorer scorer = new Scorer(pipeSPX, pipeVelocityX);
        scorers.add(scorer);

        Pipe upperPipe = new Pipe(pipeSPX, randomPosY, pipeWidth, pipeHeight, upperPipeImg, pipeVelocityX);
        pipes.add(upperPipe);

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
