package serveur;

public class MainServeur {
	
	   public static void main(String[] args) {   
		     
		      int port = 2345;
		      
		      //initialisation du serveur
		      FtpServeur ts = new FtpServeur(port);
		      ts.open();
		      
		      System.out.println("Serveur initialisé.");
		      //gestion client
		      /*for(int i = 0; i < 1; i++){
		         Thread t = new Thread(new ClientConnexion(host, port));
		         t.start();
		      }*/
		   }

}
