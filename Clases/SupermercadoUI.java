import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SupermercadoUI extends JFrame {
    private DefaultTableModel tablaModelo;
    private JTextArea logArea;
    private JButton iniciarButton;
    private JLabel totalLabel;
    private JLabel promedioLabel;
    private JLabel maximoLabel;
    private JLabel relojLabel;

    public SupermercadoUI() {
        setTitle("Simulación de Cobro - Supermercado");
        setSize(760, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        iniciarButton = new JButton("Iniciar simulación");
        iniciarButton.addActionListener(e -> ejecutarSimulacion());

        tablaModelo = new DefaultTableModel(new Object[]{"Cajera", "Cliente", "Total recaudado", "Tiempo de atención"}, 0);
        JTable tabla = new JTable(tablaModelo);
        tabla.setEnabled(false);
        JScrollPane tablaScroll = new JScrollPane(tabla);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(0, 220));

        JPanel estadisticasPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        totalLabel = new JLabel("Total recaudado: $0.00");
        promedioLabel = new JLabel("Promedio por cajera: $0.00");
        maximoLabel = new JLabel("Tiempo máximo de atención: 0 segundos");
        relojLabel = new JLabel("Tiempo total de cobro: 0 segundos");
        estadisticasPanel.add(totalLabel);
        estadisticasPanel.add(promedioLabel);
        estadisticasPanel.add(maximoLabel);
        estadisticasPanel.add(relojLabel);

        panelPrincipal.add(iniciarButton, BorderLayout.NORTH);
        panelPrincipal.add(tablaScroll, BorderLayout.CENTER);
        panelPrincipal.add(logScroll, BorderLayout.SOUTH);
        panelPrincipal.add(estadisticasPanel, BorderLayout.EAST);

        getContentPane().add(panelPrincipal);
    }

    private void ejecutarSimulacion() {
        iniciarButton.setEnabled(false);
        tablaModelo.setRowCount(0);
        logArea.setText("");
        totalLabel.setText("Total recaudado: $0.00");
        promedioLabel.setText("Promedio por cajera: $0.00");
        maximoLabel.setText("Tiempo máximo de atención: 0 segundos");
        relojLabel.setText("Tiempo total de cobro: 0 segundos");

        SwingUtilities.invokeLater(() -> {
            tablaModelo.addRow(new Object[]{"Cajera Ana", "Cliente 1", "-", "-"});
            tablaModelo.addRow(new Object[]{"Cajera Luis", "Cliente 2", "-", "-"});
            tablaModelo.addRow(new Object[]{"Cajera María", "Cliente 3", "-", "-"});
        });

        SimulacionListener listener = new SimulacionListener() {
            @Override
            public void onCajeraInicio(String cajera, String cliente, long segundos) {
                appendLog(String.format("📌 %s comienza a atender a %s en el tiempo %ds", cajera, cliente, segundos));
            }

            @Override
            public void onProductoProcesado(String cajera, String producto, double precio, int tiempoProducto, int acumulado) {
                appendLog(String.format("  → %s procesa %s - Precio: $%.2f", cajera, producto, precio));
                appendLog(String.format("    ✔ %s procesado en %ds (acumulado: %ds)", producto, tiempoProducto, acumulado));
            }

            @Override
            public void onCajeraFinalizada(String cajera, String cliente, double total, int tiempoAtencion, long relojSegundos) {
                appendLog(String.format("✅ %s terminó con %s - Total: $%.2f - Tiempo total: %ds (reloj: %ds)", cajera, cliente, total, tiempoAtencion, relojSegundos));
                actualizarTabla(cajera, cliente, total, tiempoAtencion);
            }

            @Override
            public void onReporteFinal(List<Cajera> cajeras, double totalRecaudado, double promedio, int tiempoMaximo, long tiempoTotal) {
                actualizarEstadisticas(totalRecaudado, promedio, tiempoMaximo, tiempoTotal);
                appendLog("\n===================================");
                appendLog("🏁 SIMULACIÓN FINALIZADA");
                appendLog("===================================");
            }
        };

        new Thread(() -> {
            long inicioGlobal = System.currentTimeMillis();
            List<Cajera> cajeras = crearCajeras(inicioGlobal, listener);
            cajeras.forEach(Thread::start);

            try {
                for (Cajera cajera : cajeras) {
                    cajera.join();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long finGlobal = System.currentTimeMillis();
            long tiempoTotalSegundos = (finGlobal - inicioGlobal) / 1000;
            double totalRecaudadoGlobal = 0;
            int tiempoMaximo = 0;
            for (Cajera cajera : cajeras) {
                totalRecaudadoGlobal += cajera.getTotalRecaudado();
                tiempoMaximo = Math.max(tiempoMaximo, cajera.getTiempoAtencion());
            }
            double tiempoPromedio = totalRecaudadoGlobal / cajeras.size();
            listener.onReporteFinal(cajeras, totalRecaudadoGlobal, tiempoPromedio, tiempoMaximo, tiempoTotalSegundos);
            SwingUtilities.invokeLater(() -> iniciarButton.setEnabled(true));
        }).start();
    }

    private void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void actualizarTabla(String cajera, String cliente, double total, int tiempoAtencion) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tablaModelo.getRowCount(); i++) {
                if (tablaModelo.getValueAt(i, 0).equals(cajera)) {
                    tablaModelo.setValueAt(cliente, i, 1);
                    tablaModelo.setValueAt(String.format("$%.2f", total), i, 2);
                    tablaModelo.setValueAt(tiempoAtencion + " s", i, 3);
                    break;
                }
            }
        });
    }

    private void actualizarEstadisticas(double total, double promedio, int maximo, long tiempoTotal) {
        SwingUtilities.invokeLater(() -> {
            totalLabel.setText(String.format("Total recaudado: $%.2f", total));
            promedioLabel.setText(String.format("Promedio por cajera: $%.2f", promedio));
            maximoLabel.setText(String.format("Tiempo máximo de atención: %d segundos", maximo));
            relojLabel.setText(String.format("Tiempo total de cobro: %d segundos", tiempoTotal));
        });
    }

    private List<Cajera> crearCajeras(long inicioGlobal, SimulacionListener listener) {
        Producto p1 = new Producto("Arroz", 2.5, 2);
        Producto p2 = new Producto("Leche", 1.2, 1);
        Producto p3 = new Producto("Pan", 0.8, 1);
        Producto p4 = new Producto("Huevos", 3.0, 3);
        Producto p5 = new Producto("Queso", 4.5, 2);
        Producto p6 = new Producto("Manzanas", 2.0, 1);

        List<Producto> compra1 = new ArrayList<>();
        compra1.add(p1);
        compra1.add(p2);
        Cliente cliente1 = new Cliente("Cliente 1", compra1);

        List<Producto> compra2 = new ArrayList<>();
        compra2.add(p3);
        compra2.add(p4);
        Cliente cliente2 = new Cliente("Cliente 2", compra2);

        List<Producto> compra3 = new ArrayList<>();
        compra3.add(p5);
        compra3.add(p6);
        Cliente cliente3 = new Cliente("Cliente 3", compra3);

        List<Cajera> cajeras = new ArrayList<>();
        cajeras.add(new Cajera("Cajera Ana", cliente1, inicioGlobal, listener));
        cajeras.add(new Cajera("Cajera Luis", cliente2, inicioGlobal, listener));
        cajeras.add(new Cajera("Cajera María", cliente3, inicioGlobal, listener));

        return cajeras;
    }
}
