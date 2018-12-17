

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileOutputStream;



public class Ftp {


   private String user;
   private Socket socket = null, socketData = null;
   private boolean DEBUG = true;
   private String host;
   private int port;
   private BufferedWriter writer,writerData;
   private BufferedInputStream readerData;
   private BufferedInputStream reader;
   private String dataIP;
   private int dataPort;
   private String passwd;
   private File file;

   

   public Ftp(String ipAddress, int pPort, String pUser,String pwd){

      user = pUser;
      port = pPort;
      host = ipAddress;
      passwd=pwd;

   }

   public Ftp(String pUser,String pwd){

	   this("212.27.63.3", 21, pUser,pwd);      
	   //this("127.0.0.1",1500,"molina","123");
   }

   public void setFile(File f) {
	   file=f;
   }
   
   public File getFile() {
	   return file;
   }

   /**

    * M�thode de connexion au FTP

    * @throws IOException

    */

   public void connect() throws IOException{

      //Si la socket est d�j� initialis�e

      if(socket != null) {
           throw new IOException("La connexion au FTP est d�j� activ�e");
           }   

      //On se connecte

      socket = new Socket(host, port);

      

      //On cr�e nos objets pour pouvoir communiquer

      reader = new BufferedInputStream(socket.getInputStream());

      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      

      String response = read();      


      if(!response.startsWith("220")){  

         throw new IOException("Erreur de connexion au FTP : \n" + response);

      }

      send("USER " + user);

      response = read();

      if(!response.startsWith("331"))  

         throw new IOException("Erreur de connexion avec le compte utilisateur : \n" + response);

      

      //Pas de gestion du mot de passe dans notre cas, mais vous pouvez en pr�ciser un dans le string passwd si vous le souhaitez

      

      send("PASS " + passwd);

      response = read();

      if(!response.startsWith("230"))  

         throw new IOException("Erreur de connexion avec le compte utilisateur : \n" + response);


      //Nous sommes maintenant connect�s

   }

   

   public void quit(){

      try {

         send("QUIT");

      } catch (IOException e) {

         e.printStackTrace();

      }  finally{

         if(socket != null){

            try {

               socket.close();

            } catch (IOException e) {

               e.printStackTrace();

            }

            finally{

               socket = null;

            }

         }

      }

   }

   

   /**

    * M�thode permettant d'initialiser le mode passif

    * et ainsi de pouvoir communiquer avec le canal d�di� aux donn�es

    * @throws IOException

    */

   private void enterPassiveMode() throws IOException{


     send("PASV");

     String response = read();

     if(DEBUG)

        log(response);

     String ip = null;

     int port = -1;

     

     //On d�cortique ici la r�ponse retourn�e par le serveur pour r�cup�rer

     //l'adresse IP et le port � utiliser pour le canal data

     int debut = response.indexOf('(');

     int fin = response.indexOf(')', debut + 1);

     if(debut > 0){

        String dataLink = response.substring(debut + 1, fin);

        StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");

        try {

           //L'adresse IP est s�par�e par des virgules

           //on les remplace donc par des points...

           ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."

                   + tokenizer.nextToken() + "." + tokenizer.nextToken();

          

           //Le port est un entier de type int

           //mais cet entier est d�coup� en deux

           //la premi�re partie correspond aux 4 premiers bits de l'octet

           //la deuxi�me au 4 derniers

           //Il faut donc multiplier le premier nombre par 256 et l'additionner au second

           //pour avoir le num�ro de ports d�fini par le serveur

           port = Integer.parseInt(tokenizer.nextToken()) * 256

                + Integer.parseInt(tokenizer.nextToken());

           dataIP = ip;

           dataPort = port; 

          

        } catch (Exception e) {

          throw new IOException("SimpleFTP received bad data link information: "

              + response);

        }        

     }

   }

   

   /**

    * M�thode initialisant les flux de communications

    * @throws UnknownHostException

    * @throws IOException

    */

   private void createDataSocket() throws UnknownHostException, IOException{

      socketData = new Socket(dataIP, dataPort);
      System.out.println(dataPort);

      readerData = new BufferedInputStream(socketData.getInputStream());

      writerData = new BufferedWriter(new OutputStreamWriter(socketData.getOutputStream()));
      

   }
   
   
   

   /**

    * Retourne l'endroit o� nous nous trouvons dur le FTP

    * @return

    * @throws IOException

    */

   public String pwd() throws IOException{

      //On envoie la commande

      send("PWD");

      //On lit la r�ponse

      return read();

   }

   

   /**

    * Permet de changer de r�pertoire (Change Working Directory)

    * @return

    * @throws IOException

    */

   public String cwd(String dir) throws IOException{

      //On envoie la commande

      send("CWD " + dir);

      //On lit la r�ponse

      return read();

   }

   

   public String list() throws IOException{

      send("TYPE ASCII");      

      read();

      

      enterPassiveMode();

      createDataSocket();

      send("LIST");

      

      return readData();

   }

  public String upload() throws IOException{
	
	  enterPassiveMode();

      createDataSocket();
      BufferedInputStream readerUp = new BufferedInputStream(socketData.getInputStream());

      BufferedWriter writerUp = new BufferedWriter(new OutputStreamWriter(socketData.getOutputStream()));
      
	 Socket soc=new Socket(host,port-1);
      FileOutputStream fis = new FileOutputStream(file);
      if(DEBUG) {
         log(file.toString());
       }
      byte b=new Byte("Hello");
      fis.write(b);

    

      writerData.flush();
      return "Fichier envoy�";
	
   }

   private void createDataSocketFile() {
	// TODO Auto-generated method stub
	
}

/**

    * M�thode permettant d'envoyer les commandes au FTP

    * @param command

    * @throws IOException

    */

   private void send(String command) throws IOException{

      command += "\r\n";

      if(DEBUG)

         log(command);

      writer.write(command);

      writer.flush();

   }
   
 

   

   /**

    * M�thode permettant de lire les r�ponses du FTP

    * @return

    * @throws IOException

    */

   private String read() throws IOException{      

      String response = "";

      int stream;

      byte[] b = new byte[4096];

      stream = reader.read(b);

      response = new String(b, 0, stream);

      

      if(DEBUG)

         log(response);

      return response;

   }
   
   

      

   /**

    * M�thode permettant de lire les r�ponses du FTP

    * @return

    * @throws IOException

    */

   private String readData() throws IOException{

      

      String response = "";

      byte[] b = new byte[1024];

      int stream;

      

      while((stream = readerData.read(b)) != -1){

         response += new String(b, 0, stream);

      }

      

      if(DEBUG)

         log(response);

      return response;

   }

   

   

   public void debugMode(boolean active){

      DEBUG = active;

   }

   

   private void log(String str){

      System.out.println(">> " + str);

   }

   

}