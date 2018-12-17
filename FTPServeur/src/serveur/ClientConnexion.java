package serveur;

import java.io.BufferedInputStream;
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
	            String file=getFile();
	            writer.write(commande);	            
	            writer.write(file);
	            writer.flush();  
	           System.out.println("Commande " + commande + " envoyée au serveur");           
	            //On attend la réponse
	            String response = read();
	            System.out.println("\t * " + name + " : Réponse reçue " + response);          

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

	}