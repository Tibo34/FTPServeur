import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;





public class Main {


   public static void main(String[] args) {


      try {

         Scanner sc = new Scanner(System.in);
         File f=new File("texte.txt");
         Properties p=LoadFileProperties();
         String ip=extractIP(p);
         int port=Integer.parseInt(extractPort(p));
         String user=extractUser(p);
         String pwd=extractPWD(p);
         System.out.println(ip+" "+port+" "+user+" "+pwd);
         Ftp ftp = new Ftp();
         //ftp.setFile(f);
        
         try{
        	 ftp.connect();
         }
         catch(Exception e) {
        	 System.out.println(e);
         }

         ftp.debugMode(true);

         

         System.out.println("-------------------------------------------------------");

         System.out.println("Vous êtes maintenant connecté au FTP");

         System.out.println("Vous avez le droit aux commandes PWD, CWD, LIST, upload et QUIT");

         System.out.println("-------------------------------------------------------\n\n");

         String reponse = "";

         boolean encore = true;

         while (encore) {

            reponse = sc.nextLine().toUpperCase();


            switch (reponse) {

            case "PWD":

               System.out.println(ftp.pwd());

               break;

            case "CWD":

               System.out.println(">> Saisissez le nom du répertoire où vous voulez aller : ");

               String dir = sc.nextLine();

               System.out.println(ftp.cwd(dir));

               break;

            case "LIST":

               String list = ftp.list();

               System.out.println(list);

               break;
            case "UPLOAD":
            	String rep=ftp.upload();
            	System.out.println(rep);
            	break;

            case "QUIT":

               ftp.quit();

               encore = false;

               break;

             default : 

                System.err.println("Commande inconnue !");

                break;

            }


         }

      } catch (IOException e) {

         e.printStackTrace();

         System.exit(0);

      }

      

      System.out.println("Ciao ! ");

   }
   
   



private static String extractPWD(Properties p) {
	for(Entry<Object, Object> entre: p.entrySet()) {		
		if(entre.getKey().equals("motDePasse")) {
			System.out.println(entre.getKey());
			return (String) entre.getValue();
		}
	}
	return null;
}



private static String extractUser(Properties p) {
	for(Entry<Object, Object> entre: p.entrySet()) {		
		if(entre.getKey().equals("user")) {
			System.out.println(entre.getKey());
			return (String) entre.getValue();
		}
	}
	return null;
}


private static String extractPort(Properties p) {
	for(Entry<Object, Object> entre: p.entrySet()) {		
		if(entre.getKey().equals("port")) {
			System.out.println(entre.getKey());
			return (String) entre.getValue();
		}
	}
	return null;
}


private static String extractIP(Properties p) {	
	for(Entry<Object, Object> entre: p.entrySet()) {		
		if(entre.getKey().equals("ip")) {
			System.out.println(entre.getKey());
			return (String) entre.getValue();
		}
	}
	return null;	
}







	
	public static Properties LoadFileProperties() {
		String file="texte.txt";
		Properties p=new Properties();
		 try(InputStream in=new FileInputStream(file)){
			 p.load(in);
		 }
		 catch(IOException e) {
			 e.printStackTrace();
		 }
		 return p;
	}

	private static int getQte(String value) {		
		return Integer.parseInt(value.split(" ")[0].split(":")[1]);
	}

	private static double getPrix(String value) {		
		return Double.parseDouble(value.split(" ")[1].split(":")[1]);
	}

}
