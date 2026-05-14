public class Producto {
    private String nombre;
    private double precio;
    private int tiempoProcesamientoSegundos;

    public Producto(String nombre, double precio, int tiempoProcesamientoSegundos) {
        this.nombre = nombre;
        this.precio = precio;
        this.tiempoProcesamientoSegundos = tiempoProcesamientoSegundos;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getTiempoProcesamientoSegundos() {
        return tiempoProcesamientoSegundos;
    }
}
