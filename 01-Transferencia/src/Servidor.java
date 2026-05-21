import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
  static final int PORT = 9999;
  static final String HOST = "localhost";

  private Socket socket;

  //per obrir la connexió
  public Socket connectar(){
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);
      
      System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
      System.out.println("Esperant connexio...");
      
      this.socket = serverSocket.accept();
      System.out.println("Connexio acceptada: " + socket.getInetAddress());
      return socket;
    } catch (IOException e) {
      System.out.println("Error al connectar: " + e.getMessage());
      return null;
    }
  }

  //tanca el socket que rep com a parametre
  public void tancarConnecio(Socket socket){
    try {
      System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
      socket.close();
    } catch (IOException e) {
      System.out.println("Error al tancar connexio: " + e.getMessage());
    }
  }

  //rep del client el nom del fitxer
  public void enviarFitxers(){
    try {
      ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

      while (true) {
        System.out.println("Esperant el nom del fitxer del client...");
        String nomFitxer = (String) input.readObject(); //pasem el nom que ens arriba a string

        if (nomFitxer == null || nomFitxer.isEmpty()) {
          System.out.println("Nom del fitxer buit o nul. Sortint...");
          break;  //si es null o buit es trenca el bucle
        }

        System.out.println("Nomfitxer rebut: " + nomFitxer);
        
        Fitxer fitxer = new Fitxer(nomFitxer);
        byte[] contingut = fitxer.getContingut();
        if(contingut == null) {   //abans d'enviar-lo mirem si es null
          System.out.println("Nom del fitxer buit o nul. Sortint...");
          break;
        }
        System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
        output.writeObject(contingut);
        output.flush();
        System.out.println("Fitxer enviat al client: " + nomFitxer);
      }

    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Error llegint el fitxer del client: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    Servidor srv = new Servidor();

    Socket socket = srv.connectar();    //perque el metode retorna un socket

    srv.enviarFitxers();
    srv.tancarConnecio(socket);
  }
}
