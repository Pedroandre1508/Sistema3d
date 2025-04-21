
import Projeto.App;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Computação Gráfica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.add(new App()); 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

