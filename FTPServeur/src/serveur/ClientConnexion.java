package serveur;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ClientConnexion implements Runnable{

	   private Socket connexion = null;
	   private PrintWriter writer = null;
	   private BufferedInputStream reader = null;
	   //saisie clavier
	   private Scanner sc = new Scanner(System.in); 
	   //Notre liste de commandes. Le serveur nous répondra différemment selon la commande utilisée.
	   private String[] commandes= {"RETR", "STOR", "DELE", "QUIT"};
	   private ArrayList<String> listCommands = new ArrayList<String>() ;	   
	   private static int count = 0;
	   private String name = "Client-"; 
	   private String path="D:\\Documents";
	   

	   public ClientConnexion(String host, int port){
		   FillCommandes();
	      name += ++count;
	     try {
	         connexion = new Socket(host, port);
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }

 	   private void FillCommandes() {
			for(String commande :commandes ) {
				listCommands.add(commande);
			}		
 	   }

 	   public void run(){
	      //nous n'allons faire que 10 demandes par thread...

	      for(int i =0; i < 10; i++){
	         try {
	            Thread.currentThread().sleep(1000);

	         } catch (InterruptedException e) {
	            e.printStackTrace();
	         }

	         try {        

	            writer = new PrintWriter(connexion.getOutputStream(), true);
	            reader = new BufferedInputStream(connexion.getInputStream());
	            //On envoie la commande au serveur     
	            String commande = getCommand();
	            //String file=getFile();
	            writer.write(commande);         
	            writer.flush();  
	           System.out.println("Commande " + commande + " envoyée au serveur");           
	            //On attend la réponse
	            String response = read();
	            System.out.println("\t * " + name + " : Réponse reçue " + response);
	           switch(commande) {
	           	case "RETR"://récupération de fichier du serveur fts	           		
	           		writeFile();
	           		break;
	           	case "STOR": // envoie de fichier sur le serveur
	           		sendFile();
	           		break;
	           	case "DELE":
	           		deleteFile();
	           		break;
	           	case "QUIT":
	           		displayEndMess();
	           		break;
	            default : 
	                 // toSend = "Commande inconnu !";                     
	                  break;
	           }
	         } catch (IOException e1) {
	            e1.printStackTrace();
	         }
	         try {
	            Thread.currentThread().sleep(1000);
	         } catch (InterruptedException e) {
	        	 e.printStackTrace();
	         }
	      }    
	      writer.write("CLOSE");
	      writer.flush();
	      writer.close();
	   }
 	   
 	   private void deleteFile() throws IOException {
 		   String answer=read();
		System.out.println(answer);
		String file=sc.nextLine();
		writer.write(file);
		writer.flush();
		System.out.println(read());
		
	}

	private void displayEndMess() throws IOException {
		String endMess=read();
		System.out.println(endMess);
		
	}

	/**
 	    * Envoie de fichier au serveur
 	    * @throws IOException
 	    */
 	  private void sendFile() throws IOException {		   
		   File file=getFileSend();//envoie du chemin complet	 		     
		   String toSend= readFile(file);			
		   writer.write(toSend);
		   writer.flush();		 
          if(!toSend.equals("")) {
       	   		System.out.println("Fichier envoyé");
          }
          else {
        	  System.out.println("Fichier  non envoyé");
          }          
	}
 	  
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

	 private String getFileText() throws IOException {
		 String strTextFile="";		
	   	strTextFile=read();		
		return strTextFile;
	}

	private File getFileSend() {
		
			String reponse = "";
	         boolean encore = true;
	         while (encore) {
	        	 System.out.println("Saisir un fichier");
	            reponse = sc.nextLine();
	            if(!reponse.equals("")) {
	            	encore=false;
	            }
	       }
         File file = new File(reponse);
		  writer.write(file.getName());
		  writer.flush();
	      return file;		
	}
	
	private String getFile() {
		
			String reponse = "";
	         boolean encore = true;
	         while (encore) {
	        	 System.out.println("Saisir un fichier");
	            reponse = sc.nextLine();
	            if(!reponse.equals("")) {
	            	encore=false;
	            }
	       }        
		  writer.write(reponse);
		  writer.flush();
	      return reponse;		
	}

	//Méthode qui permet d'envoyer des commandeS de façon aléatoire

	   private String getCommand(){
		   String reponse = "";
	         boolean encore = true;
	         while (encore) {
	        	System.out.println("Vous avez le droit aux commande "+getListCommanades());
	            reponse = sc.nextLine().toUpperCase();
	            if(listCommands.contains(reponse)) {
	            	encore=false;
	            }
	          }
	      return reponse;
	   }

	   

	   private String getListCommanades() {
		   String com="";
		   for(String str : listCommands) {
			   com+=str+", ";
		   }
		   com+="\n\n";
		   return com;
	   }

	//Méthode pour lire les réponses du serveur

	   private String read() throws IOException{  
	      String response = "";
	      int stream;
	      byte[] b = new byte[4096];
	      stream = reader.read(b);
	      response = new String(b, 0, stream);
	      return response;
	   }   
	   
	   
	   private void writeFile() throws IOException {
		   	String fileName=getFile();
		   	String fileText=getFileText();
		   	 File file=new File(path+"/"+fileName);		   	 
			 FileOutputStream output;			
			output = new FileOutputStream(file);
			for(int i=0;i<fileText.length();i++) {			
				 int c=(int) fileText.charAt(i);			
				 output.write(c);
			 }
			 output.close();			
		 }

	}