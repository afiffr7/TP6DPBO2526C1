import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    // attribut logika game dan label skor
    private Logic logic;
    private JLabel score = new JLabel();

    // menyimpan ukuran frame
    int width = 360;
    int height = 640;

    public View(Logic logic){
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);

        // setting label score
        score.setVisible(true);
        this.add(score);

        setFocusable(true);
        addKeyListener(logic);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        // draw player
        Player player = logic.getPlayer();
        if(player != null){
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        }
        // draw pipa
        ArrayList<Pipe> pipes = logic.getPipes();
        for(Pipe pipe : pipes){
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
        // draw guide string
        if(logic.getGameState() == 0){ // main menu
            g.drawString("Press SPACE/UP to play", 90, 480);
            g.drawString("Press ESC to exit", 90, 500);
        }else if(logic.getGameState() == 2){ // game over
            g.drawString("Game Over!", 90, 320);
            g.drawString("Press R to restart", 90, 480);
        }
        score.setText("Score:" + String.valueOf(logic.getScore())); // update label score
    }
}
