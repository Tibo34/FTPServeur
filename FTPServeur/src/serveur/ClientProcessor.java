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
import java.util.Scanner;

public class ClientProcessor implements Runnable {
	

	   private Socket sock;
	   private PrintWriter writer = null;
	   private BufferedInputStream reader = null;
	   private String pathCourant="D:\\Java-other";
	   
	   
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
		            	   System.out.println("Commande RETR");
		            	   sendFile();              
                           break;
		               case "STOR"://Demande de transfert de fichier
		                 loadFile();
		                  break;
		               case "DELE"://Demande de suppression de fichier
		                  deleteFile();               
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
	   
	  private void deleteFile() throws IOException {
		String files=displayFile();
		writer.write(files+" \n Quel fichier supprimé? \n");
		writer.flush();
		String answer=read();		
		File file=isExist(answer, getFileCourant());
		
		boolean r=file.delete();
		if(r) {
			System.out.println("Réussi");
			writer.write("Réussi");
			
		}
		else {
			System.out.println("Echec");
			writer.write("Echec");
		}
		writer.flush();
	}

	private void loadFile() throws IOException {
		    String fileName=getFileName();
     		String fileText=getFileText();
     		String path="D:\\Java-other";
		   	 File file=new File(path+"/"+fileName);
			 FileOutputStream output;			
			output = new FileOutputStream(file);
			for(int i=0;i<fileText.length();i++) {			
				 int c=(int) fileText.charAt(i);			
				 output.write(c);
			 }
			System.out.println("File reçu");
			 output.close();		
	}

	

	private String getFileText() throws IOException {	
		writer.write("envoie texte");
		writer.flush();
		String fileText=read();
		return fileText;
	}

	private String getFileName() throws IOException {		
		writer.write("Saisir un  fichier : ");
		writer.flush();		
		String fileName=read();		
		return fileName;
	}

	private void sendFile() {
		   String toSend ="Téléchargement de: \n\n Quel fichier vouler-vous téléchargé ? ";
		   String answer="";
		   String strFile="";
		   toSend+=displayFile();
		   File file=null;
		   writer.write(toSend);
		   writer.flush();
		   try {			   
			  answer=read();
			  file=isExist(answer,getFileCourant());                 
		      strFile=readFile(file);          
			  
			} catch (IOException e) {
				System.out.println("Erreur de connection");
				e.printStackTrace();
			}
		  
           writer.write(strFile);
           writer.flush();
           if(file!=null) {
        	   System.out.println("Fichier envoyé");
           }           
	}

	private String displayFile() {		
		   File[] files=getFileCourant();
		   String str="";
		   for(File f : files) {
			   str+="\n "+f.getName();
		   }		  
		return str;
	}

	private File[] getFileCourant() {
		File repertoire = new File(pathCourant);
		   File file;
		   File[] files=repertoire.listFiles();
		   return files;
	}
	
	private File isExist(String answer, File[] files) {
		for(File f : files) {
			if(f.getName().equals(answer)) {
				return f;
			}
		}
		return null;
	}

	/**
	    * lit le fichier
	    * @param f
	    * @return
	    * @throws IOException
	    */
	   private String readFile(File f) throws IOException {		   
		   String str="";		   	   
		   if(f.exists()) {			   
			   FileInputStream file=new FileInputStream(f);			   
			   int lettre;
			   while((lettre=file.read())!=-1) {
				    str+=(char)lettre;
			   }
           }
		return str;
	  }
	   
	 private void writeFile(File f,String text) throws IOException {
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
