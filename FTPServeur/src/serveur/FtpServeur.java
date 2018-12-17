package serveur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpServeur {
	
	private String iP="127.0.0.1";
	private int port=1500;
	private ServerSocket server;
	private int fileAttente = 100;
	
	
	public FtpServeur() {
		port=getPortFree();
		server=setSocket(port,fileAttente);		
	}
	
	
	public FtpServeur(int p) {
		port=p;	
		server=setSocket(port,fileAttente);
	}
	
	
	public int getPortFree() {
		int p=port;
		ServerSocket Socket=null;
		for( ; p <= 65535&&Socket==null; p++){

	         try {

	        	 Socket= new ServerSocket(p);

	         } catch (IOException e) {

	            System.err.println("Le port " + port + " est déjà utilisé ! ");

	         }

	      }
		System.out.println(p);
		return p-1;
	}
	
	public ServerSocket setSocket(int p,int file) {
		ServerSocket sock=null;
		try{
			sock=new ServerSocket(p,file);
		}
		catch(IOException e) {
			
		}
		return sock;
	}
	
	public String toString() {
		return "Serveur id: "+iP+" port : "+port;
	}
	
	 public void open(){

	      //Toujours dans un thread à part vu qu'il est dans une boucle infinie
		 boolean isRunning=true;
	      Thread t = new Thread(new Runnable(){

	         public void run(){

	            while(isRunning){               

	               try {
	                  //On attend une connexion d'un client
	                  Socket client = server.accept();              
	                  //Une fois reçue, on la traite dans un thread séparé
	                  System.out.println("Connexion cliente reçue.");             
	                  Thread t = new Thread(new ClientProcessor(client));
	                  t.start();              

	               } catch (IOException e) {
	                  e.printStackTrace();
	               }
	            }
	            try {
	               server.close();
	            } catch (IOException e) {
	               e.printStackTrace();
	               server = null;
	               }
	         }

	      });      

	      t.start();

	   }
	 
	 
	
	
	public static void main(String[]args) {
		FtpServeur serveur=new FtpServeur();
		System.out.println(serveur);
	}
}
