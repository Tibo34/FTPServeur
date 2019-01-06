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
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ClientProcessor implements Runnable {
	

	   private Socket sock;
	   private PrintWriter writer = null;
	   private BufferedInputStream reader = null;
	   private String pathCourant="D:\\Java-other";
	   private ArrayList<String> excludeFile=new ArrayList<>();
	   
	   
	   public ClientProcessor(Socket s) {
		   sock=s;
		   excludeFile.add(".git");
		   excludeFile.add(".metadata");
		   excludeFile.add(".recommenders");
		   excludeFile.add("FTPClient");
		   excludeFile.add("FTPServeur");
		   excludeFile.add("README.md");
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
		            send(toSend);
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
		send(files+" \n Quel fichier supprimé? \n");
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
		send("envoie texte");
		String fileText=read();
		return fileText;
	}

	private String getFileName() throws IOException {		
		send("Saisir un  fichier : ");
		String fileName=read();		
		return fileName;
	}

	private void sendFile() {		   
		   String answer="";
		   String strFile="";
		   String toSend=displayFile();
		   File file=null;
		   send(toSend);
		   try {			   
			  answer=read();
			  file=isExist(answer,getFileCourant());                 
		      strFile=readFile(file);     
			  
			} catch (IOException e) {
				System.out.println("Erreur de connection");
				e.printStackTrace();
			}		  
           send(strFile);
           if(file!=null) {
        	   System.out.println("Fichier envoyé");
           }           
	}

	private void send(String str) {
		writer.write(str);
        writer.flush();
	}

	private String displayFile() {		
		   File[] files=getFileCourant();
		   String str="";
		   for(File f : files) {
			   if(!excludeFile.contains(f.getName())) {
				   str+=f.getName()+"\n";
			   }
		   }		  
		return str;
	}
	

	private File[] getFileCourant() {
		File repertoire = new File(pathCourant);
		File[] files=repertoire.listFiles();
		return files;
	}
	
	private File isExist(String answer, File[] files) {
		System.out.println(answer);
		for(File f : files) {
			System.out.println(answer);
			System.out.println(f.getName());
			if(f.getName().equals(answer)) {
				System.out.println(f.getName());
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
		      byte[] b = new byte[4096];
		      int stream= reader.read(b);
		     String response = new String(b, 0, stream);
		      return response;
	}


}
