package serveur;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.util.Date;

public class ClientProcessor implements Runnable {
	

	   private Socket sock;
	   private PrintWriter writer = null;
	   private BufferedInputStream reader = null;
	   
	   public ClientProcessor(Socket s) {
		   sock=s;
	   }
	   
	   public void run(){

		      System.err.println("Lancement du traitement de la connexion cliente");


		      boolean closeConnexion = false;

		      //tant que la connexion est active, on traite les demandes

		      while(!sock.isClosed()){
		         try {
		            //Ici, nous n'utilisons pas les mêmes objets que précédemment
		            //Je vous expliquerai pourquoi ensuite
		            writer = new PrintWriter(sock.getOutputStream());
		            reader = new BufferedInputStream(sock.getInputStream());           
		            //On attend la demande du client            
		            String response = read();
		            InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();		            
		            //On affiche quelques infos, pour le débuggage
		            String debug = "";
		            debug = "Thread : " + Thread.currentThread().getName() + ". ";
		            debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
		            debug += " Sur le port : " + remote.getPort() + ".\n";
		            debug += "\t -> Commande reçue : " + response + "\n";
		            System.err.println("\n" + debug);    
		            //On traite la demande du client en fonction de la commande envoyée
		            String toSend = "";		            
		            switch(response.toUpperCase()){
		               case "RETR"://Demande de récupération de fichier
		                  toSend ="Téléchargement : ";
		                  File f=new File("texte.txt");
		                  File copy=new File("d:/Documents/copie.txt");		                 
		                  Files.copy(f.toPath(),copy.toPath());
		                 toSend=ReadFile(f);
//		                  String filetext=ReadFile(f);		                 
//		                 WriteFile(copy,filetext);
		                 //System.out.println(filetext);
		                  break;
		               case "STOR"://Demande de transfert de fichier
		                  toSend = "Fichier tranferet";
		                  String dest="d:/Documents/";
		                  File download=new File("d:/Documents/copie.txt");
		                  File file=new File("copie.txt");
		                  Files.copy(download.toPath(), file.toPath());
		                  break;
		               case "DELE"://Demande de suppression de fichier
		                  toSend = "Fichier supprimé";
		                  File d=new File("d:/Documents/copie.txt");
		                  Files.deleteIfExists(d.toPath());		               
		                  break;
		               case "QUIT"://Fin de transfert ou abandon
		                  toSend = "Communication terminée"; 
		                  closeConnexion = true;
		                 break;
		               default : 
		                  toSend = "Commande inconnu !";                     
		                  break;
		            }
		            //On envoie la réponse au client
		            writer.write(toSend);
		            //envoie de la requete
		            writer.flush();
		            if(closeConnexion){
		               System.err.println("COMMANDE CLOSE DETECTEE ! ");
		               writer = null;
		               reader = null;
		               sock.close();
		               break;
		            }
		         }catch(SocketException e){
		            System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
		            break;
		         } catch (IOException e) {
		            e.printStackTrace();
		         }         
		      }
		   }
	   
	   /**
	    * lit le fichier
	    * @param f
	    * @return
	    * @throws IOException
	    */
	   private String ReadFile(File f) throws IOException {		   
		   String str="";		  
		   System.out.println(f.exists());		   
		   if(f.exists()) {			   
			   FileInputStream file=new FileInputStream(f);			   
			   int lettre;
			   while((lettre=file.read())!=-1) {
				    str+=(char)lettre;
			   }
           }
		return str;
	  }
	   
	 private void WriteFile(File f,String text) throws IOException {
		 FileOutputStream output=new FileOutputStream(f);
		 for(int i=0;i<text.length();i++) {			
			 int c=(int) text.charAt(i);			
			 output.write(c);
		 }
	 }

	private String read() throws IOException{    
		      String response = "";
		      int stream;
		      byte[] b = new byte[4096];
		      stream = reader.read(b);
		      response = new String(b, 0, stream);
		      return response;
		   }


}
