package fenetre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import clientFTP.ClientConnexion;

public class connexionFTPServeur implements ActionListener {
	
	private FenetreConnexion fenetre;
	private String host = "127.0.0.1";
    private int port = 2345;
	private String serveur="nom serveur";
    
	public connexionFTPServeur(FenetreConnexion f){
		fenetre=f;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		host=fenetre.getTxtIP().getText();
		port=Integer.parseInt(fenetre.getTxtPort().getText());
		serveur=fenetre.getTxtServeur().getText();
		connexionFTP();
	}
	
	private void connexionFTP() {
		ClientConnexion c=new ClientConnexion(host, port,fenetre);
		fenetre.setConnexion(c);
	      Thread t = new Thread(c);
	       t.start();
	}

}
