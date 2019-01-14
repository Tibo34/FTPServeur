package clientFTP;

import java.awt.Checkbox;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import fenetre.FenetreConnexion;

public class ClientConnexion implements Runnable{

	   private Socket connexion = null;
	   private PrintWriter writer = null;
	   private BufferedInputStream reader = null;
	   //saisie clavier
	   private Scanner sc = new Scanner(System.in); 
	   //Notre liste de commandes. Le serveur nous répondra différemment selon la commande utilisée.
	   private ArrayList<String> listCommands = new ArrayList<String>() ;	   
	   private static int count = 0;
	   private String name = "Client-"; 
	   private String path="D:\\Documents";
	   private FenetreConnexion fenetre;
	   
	   
	   public ClientConnexion(String host, int port,FenetreConnexion f){
		   fenetre=f;
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
 		  listCommands.add("RETR");
 		  listCommands.add("STOR");
 		  listCommands.add("DELE");
		  listCommands.add("QUIT");					
 	   }

 	   
 	   public void run(){      
 		   //limite de connexion 10.
	      for(int i =0; i < 10; i++){
	         try {
	            Thread.currentThread().sleep(1000);
	         } catch (InterruptedException e) {
	            e.printStackTrace();
	         }
	         try {       
	            writer = new PrintWriter(connexion.getOutputStream(), true);
	            reader = new BufferedInputStream(connexion.getInputStream());
	            
	         } catch (IOException e1) {
	            e1.printStackTrace();
	         }
	         try {
	            Thread.currentThread().sleep(1000);
	         } catch (InterruptedException e) {
	        	 e.printStackTrace();
	         }
	      }    
	     send("CLOSE");	      
	      writer.close();
	   }
 	   
 	   public void send(String str) {
 		   writer.write(str);
 		   writer.flush();
 	   }
 	   
 	   public void displayMessage(String str) {
 		   fenetre.getInfoConnexion().setText(str);
 	   }
 	   
 	 public void deleteFile() throws IOException { 
 		String file="";
		boolean r=true;
		while(r) {
			file=sc.nextLine();
			if(!file.equals("")) {
				r=false;
			}
		}
		deleteFile(file);
	}

	public void deleteFile(String file) {
		send(file);		
	}

	public void displayEndMess() throws IOException {
		String endMess=read();
		displayMessage(endMess);		
	}

	/**
 	    * Envoie de fichier au serveur
 	    * @throws IOException
 	    */
 	  public void sendFile() throws IOException {		   
		   File file=getFileSend();//envoie du chemin complet	 		     
		   String toSend= readFile(file);			
		 send(toSend);		   	 
          if(!toSend.equals("")) {
       	   		System.out.println("Fichier envoyé");
          }
          else {
        	  System.out.println("Fichier  non envoyé");
          }          
	}
 	 
 	  
 	  /**
 	   * Envoie le fichier par la socket
 	   * @param file
 	   * @throws IOException
 	   */
 	  public void sendFile(File file) throws IOException {
 		 send(file.getName());
 		 String toSend=readFile(file); 				   	 
 		 send(toSend);
		
	}

 	  /**
	   * lecture du contenut du fichier pour l'envoie
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

	/*private boolean isImage(File file) {
		String[]fileName=file.getName().split(".");
		ArrayList<String> images=new ArrayList<String>();
		images.add("png");
		images.add("jpg");
		images.add("gif");
		return images.contains(fileName[1]);
	}*/

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
		 send(file.getName());		 
	      return file;		
	}

	/**
 	  * récupération du texte du fichier
 	  * @return
 	  * @throws IOException
 	  */
	 private String getFileText() throws IOException {
		String strTextFile="";		
	   	strTextFile=read();		
		return strTextFile;
	}

	 /**
	  * récupération du non
	  * @return
	  */
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

	
	private void writeFile() throws IOException {
	   	String fileName=getFile();
	   	writeFile(fileName);	   		
	 }

	public void writeFile(String fileName) throws IOException {
		send(fileName);
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

	
	
	

	//Méthode qui permet d'envoyer des commandeS de façon aléatoire

	   private String getCommand(){
		   boolean encore=true;
		   String reponse = "";	         
	         while (encore) {
	        	displayMessage("Vous avez le droit aux commande "+getListCommandes());
	            reponse ="";//récupération de commande	            
	          }
	      return reponse;
	   }

	   

	   public String getListCommandes() {
		   String com="";
		   for(String str : listCommands) {
			   com+=str+", ";
		   }
		   com+="\n\n";
		   return com;
	   }

	//Méthode pour lire les réponses du serveur

	   public String read() throws IOException{  
		  byte[] b = new byte[4096];		  
		  int stream = reader.read(b);
		  System.out.println(new String(b, 0, stream));
	      return new String(b, 0, stream);
	   }   
	   
	   
	 
	   
	   

	}