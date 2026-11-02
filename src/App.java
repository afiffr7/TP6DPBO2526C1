import javax.swing.*;

public class App {
    public static void main(String[] args){
        // Instansiasi Frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Instansiasi Logic dan View dari game
        Logic logic = new Logic();
        View tampilan = new View(logic);
        logic.setView(tampilan);
        tampilan.requestFocus();

        frame.add(tampilan);
        frame.pack();
        frame.setVisible(true);
    }
}
