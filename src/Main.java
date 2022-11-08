import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var sc = new Scanner(System.in);
        int respuesta;

        do {
            System.out.println("MENÚ");
            System.out.println("1. Añadir restaurante.");
            System.out.println("2. Listar.");
            System.out.println("3. Buscar por nombre.");
            System.out.println("4. Ordenar por ciudad.");
            System.out.println("5. Salir.");

            System.out.print("Selecciona opción: ");
            respuesta = sc.nextInt();

            switch (respuesta) {
                default -> System.out.println("Opción incorrecta, vuelva a intentarlo.");
                case 1 -> addRestaurante();
                case 2 -> listar();
                case 3 -> buscarPorNombre();
                case 4 -> ordenarPorCiudad();
                case 5 -> System.out.println("Terminando programa...");
            }
        } while (respuesta != 5);
    }

    private static List<Restaurante> leerDatos() {
        var restaurantes = new ArrayList<Restaurante>();
        try {
            var file = new File("restaurantes.csv");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (linea.length() == 0 || datos.length != 5) continue;
                var restaurante = new Restaurante(datos[0], datos[1].replace("\"", ""), datos[2], datos[3], datos[4]);
                restaurantes.add(restaurante);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado.");
        }
        return restaurantes;
    }

    private static void escribirDatos(String ruta, boolean eliminar, Restaurante... restaurantes) {
        try {
            final var path = Paths.get(ruta);
            if (eliminar) {
                File file = path.toFile();
                if (file.delete()) System.gc();
            }
            for (Restaurante restaurante : restaurantes) {
                String sb = restaurante.nombre() + "," + "\"" + restaurante.direccion() + "\"" + "," + restaurante.ciudad() + "," + restaurante.estado() + "," + restaurante.zip();
                Files.write(path, sb.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
                Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            }
        } catch (InvalidPathException e) {
            System.out.println("Error en la ruta.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo.");
        }
    }

    private static void addRestaurante() {
        var scanner = new Scanner(System.in);

        System.out.print("Introduzca nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Introduzca direccion: ");
        String direccion = scanner.nextLine();

        System.out.print("Introduzca ciudad: ");
        String ciudad = scanner.nextLine();

        System.out.print("Introduzca estado: ");
        String estado = scanner.nextLine();

        System.out.print("Introduzca zip: ");
        String zip = scanner.nextLine();

        var restaurante = new Restaurante(nombre, direccion, ciudad, estado, zip);

        escribirDatos("restaurantes.csv", false, restaurante);
    }

    private static void listar()  {
        var restaurantes = leerDatos();
        restaurantes.forEach(System.out::println);
    }

    private static void buscarPorNombre() {
        var sc = new Scanner(System.in);

        System.out.print("Introduce texto a buscar: ");
        String texto = sc.nextLine();

        var restaurantes = leerDatos();
        for (var restaurante : restaurantes) {
            if (restaurante.nombre().contains(texto)) {
                System.out.println(restaurante);
            }
        }
    }

    private static void ordenarPorCiudad() {
        var restaurantes = leerDatos();

        restaurantes.sort(Comparator.comparing(Restaurante::ciudad));

        System.out.println("¿Cómo desea imprimir la colección?");
        System.out.println("1. Por pantalla.");
        System.out.println("2. Por fichero.");
        System.out.print("Seleccione opción: ");
        var scanner = new Scanner(System.in);
        int respuesta = scanner.nextInt();

        switch (respuesta) {
            default -> System.out.println("Opción incorrecta.");
            case 1 -> restaurantes.forEach(System.out::println);
            case 2 ->
                    escribirDatos("restaurantesOrdenadosPorCiudad.csv", true, restaurantes.toArray(Restaurante[]::new));
        }
    }
}
