import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fitxer {
  String nom;
  byte[] contingut;

  public Fitxer(String nom) {
    this.nom = nom;
  }

  // comprova si el fitxer existeix i retorna els seus bytes
  public byte[] getContingut() {
    File file = new File(nom);

    if (!file.exists()) {
      System.out.println("El fitxer no existeix: " + nom);
      return null;
    }

    try {
      contingut = Files.readAllBytes(file.toPath());
      return contingut;
      
    } catch (IOException e) {
      System.out.println("Error llegint el fitxer: " + e.getMessage());
      return null;
    }
  }
}
