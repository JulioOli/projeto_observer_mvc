import controller.AcoesController;
import javax.swing.*;
import model.AcoesBolsa;
import view.AcoesView;

/**
 * Classe principal que inicia a aplicação.
 */
public class MonitorAcoes {
    public static void main(String[] args) {
        // Usa o evento de despacho da AWT para garantir thread-safety com Swing
        SwingUtilities.invokeLater(() -> {
            // Cria instâncias do modelo, view e controller
            AcoesBolsa modelo = new AcoesBolsa();
            AcoesView view = new AcoesView();
            AcoesController controller = new AcoesController(modelo, view);
            
            // Adiciona um listener para fechar corretamente a aplicação
            view.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    controller.finalizar();
                    System.exit(0);
                }
            });
        });
    }
}
