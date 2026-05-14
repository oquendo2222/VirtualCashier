import javax.swing.SwingUtilities;

public class SupermercadoSimulacion {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SupermercadoUI ui = new SupermercadoUI();
            ui.setVisible(true);
        });
    }
}
