import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    private Logic logic;

    int width = 360;
    int height = 640;
    public View(Logic logic){
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);

        setFocusable(true);
        addKeyListener(logic);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        Player player = logic.getPlayer();
        if(player != null){
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        }
        ArrayList<Pipe> pipes = logic.getPipes();
        for(Pipe pipe : pipes){
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
        if(logic.getGameState() == 0){
            g.drawString("Press SPACE to play", 90, 480);
        }else if(logic.getGameState() == 1){
            g.drawString("Score:" + String.valueOf(logic.getScore()), 90, 160);
        }else{
            g.drawString("Game Over!", 90, 320);
            g.drawString("Press SPACE to restart", 90, 480);
            g.drawString("Score:" + String.valueOf(logic.getScore()), 90, 160);
        }

    }
}
