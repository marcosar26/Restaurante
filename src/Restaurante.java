import java.util.Objects;

public record Restaurante(String nombre, String direccion, String ciudad, String estado, String zip) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Restaurante) o;
        return nombre.equals(that.nombre) && direccion.equals(that.direccion) && ciudad.equals(that.ciudad) && estado.equals(that.estado) && zip.equals(that.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, direccion, ciudad, estado, zip);
    }

    @Override
    public String toString() {
        return "---Restaurante---" + System.lineSeparator() + "Nombre: " + this.nombre + System.lineSeparator() + "Dirección: " + this.direccion + System.lineSeparator() + "Ciudad: " + this.ciudad + System.lineSeparator() + "País: " + this.estado + System.lineSeparator() + "Código postal: " + this.zip + System.lineSeparator();
    }
}
