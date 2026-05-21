import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  static final String DIR_ARRIBADA = "C:\\tmp";
  private ObjectOutputStream output;
  private ObjectInputStream input;

  private Socket socket;

  //conecta amb el servidor localhost:9999
  public void connectar() {
    try {
      socket = new Socket(Servidor.HOST, Servidor.PORT);
      System.out.println("Connectant a -> " + Servidor.HOST + ":" + Servidor.PORT);

      output = new ObjectOutputStream(socket.getOutputStream());
      input = new ObjectInputStream(socket.getInputStream());

      System.out.println("Connexio acceptada: " + socket.getInetAddress());
    } catch (IOException e) {
      System.out.println("Error al connectar: " + e.getMessage());
    }
  }

  //
  public void rebreFitxers() {
    try {
      Scanner scanner = new Scanner(System.in);   //per llegir el nom per la consola

      while (true) {
        System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
        String nomFitxer = scanner.nextLine(); // escriure el nom del fitxer

        output.writeObject(nomFitxer);  //s'envia al servidor
        output.flush();

        if(nomFitxer.equalsIgnoreCase("sortir")) { //comprovació cada cop que escriu el client
          System.out.println("Sortint...");
          break;
        }

        byte[] contingut = (byte[]) input.readObject(); 

        String nomGuardar = DIR_ARRIBADA + "\\" + new File(nomFitxer).getName(); // agafem nomes el nom del fitxer i no la ruta sencera
        System.out.println("Nom del fitxer a guardar: " + nomGuardar);

        FileOutputStream sortida = new FileOutputStream(nomGuardar);
        sortida.write(contingut);
        sortida.close();
        System.out.println("Fitxer rebut i guardat com: " + nomGuardar);
      }
      scanner.close();
    } catch (IOException | ClassNotFoundException e){
      System.out.println("Error rebent el fitxer: " + e.getMessage());
    }
  }

  public void tancarConnexio() {
    try{
      System.out.println("Connexio tancada.");
      socket.close();

    } catch (IOException e){
      System.out.println("Error al tancar connexio: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    Client client = new Client();

    client.connectar();
    client.rebreFitxers();
    client.tancarConnexio();
  }
}
