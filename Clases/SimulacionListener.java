import java.util.List;

public interface SimulacionListener {
    void onCajeraInicio(String cajera, String cliente, long segundos);
    void onProductoProcesado(String cajera, String producto, double precio, int tiempoProducto, int acumulado);
    void onCajeraFinalizada(String cajera, String cliente, double total, int tiempoAtencion, long relojSegundos);
    void onReporteFinal(List<Cajera> cajeras, double totalRecaudado, double promedio, int tiempoMaximo, long tiempoTotal);
}
