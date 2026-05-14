public class Cajera extends Thread {
    private String nombre;
    private Cliente cliente;
    private long startTime;
    private double totalRecaudado;
    private int tiempoAtencion;
    private SimulacionListener listener;

    public Cajera(String nombre, Cliente cliente, long startTime) {
        this(nombre, cliente, startTime, null);
    }

    public Cajera(String nombre, Cliente cliente, long startTime, SimulacionListener listener) {
        this.nombre = nombre;
        this.cliente = cliente;
        this.startTime = startTime;
        this.totalRecaudado = 0;
        this.tiempoAtencion = 0;
        this.listener = listener;
    }

    @Override
    public void run() {
        long inicioLocal = System.currentTimeMillis();
        long tiempoInicio = (inicioLocal - startTime) / 1000;
        if (listener != null) {
            listener.onCajeraInicio(nombre, cliente.getNombre(), tiempoInicio);
        } else {
            System.out.println("📌 " + nombre + " comienza a atender a " + cliente.getNombre() + " en el tiempo " + tiempoInicio + "s");
        }

        double totalCompra = 0;
        int tiempoAcumulado = 0;

        for (Producto producto : cliente.getProductos()) {
            if (listener != null) {
                listener.onProductoProcesado(nombre, producto.getNombre(), producto.getPrecio(), producto.getTiempoProcesamientoSegundos(), tiempoAcumulado + producto.getTiempoProcesamientoSegundos());
            } else {
                System.out.println("  → " + nombre + " procesa " + producto.getNombre() + " - Precio: $" + producto.getPrecio());
            }

            totalCompra += producto.getPrecio();
            try {
                Thread.sleep(producto.getTiempoProcesamientoSegundos() * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            tiempoAcumulado += producto.getTiempoProcesamientoSegundos();
            if (listener == null) {
                System.out.println("    ✔ " + producto.getNombre() + " procesado en " + producto.getTiempoProcesamientoSegundos() + "s (acumulado: " + tiempoAcumulado + "s)");
            }
        }

        long tiempoFinal = (System.currentTimeMillis() - startTime) / 1000;
        if (listener != null) {
            listener.onCajeraFinalizada(nombre, cliente.getNombre(), totalCompra, tiempoAcumulado, tiempoFinal);
        } else {
            System.out.println("✅ " + nombre + " terminó con " + cliente.getNombre() + " - Total: $" + totalCompra + " - Tiempo total: " + tiempoAcumulado + "s (reloj: " + tiempoFinal + "s)");
        }

        this.totalRecaudado = totalCompra;
        this.tiempoAtencion = tiempoAcumulado;
    }

    public String getNombre() {
        return nombre;
    }

    public double getTotalRecaudado() {
        return totalRecaudado;
    }

    public int getTiempoAtencion() {
        return tiempoAtencion;
    }

    public String getNombreCliente() {
        return cliente.getNombre();
    }
}
