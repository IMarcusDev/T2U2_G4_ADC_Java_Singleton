package main.java.ec.edu.espe;
import javax.swing.SwingUtilities;
import main.java.ec.edu.espe.presentacion.EstudianteView;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EstudianteView view = new EstudianteView();
            view.setVisible(true);
        });
    }
}