package Actions;

import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import clientFTP.ClientConnexion;
import fenetre.FenetreConnexion;
import fenetre.FenetreReception;

public class RecevoirAction implements ActionListener {
	
	private FenetreConnexion fenetre;
	private ClientConnexion connexion;

	public RecevoirAction(FenetreConnexion f) {
		fenetre=f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		connexion=fenetre.getConnexion();
		connexion.send("RETR");	
		String reponse="";
		try {
			reponse=connexion.read();
			String[]files=ExtractFiles(reponse);
			FenetreReception reception=new FenetreReception(files,connexion);					
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private String[] ExtractFiles(String reponse) {
		String[]split=reponse.split("\n");		
		return split;
	}

}
