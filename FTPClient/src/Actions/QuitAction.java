package Actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import clientFTP.ClientConnexion;
import fenetre.FenetreConnexion;

public class QuitAction implements ActionListener {
	
	private FenetreConnexion fenetre;

	public QuitAction(FenetreConnexion f) {
		fenetre=f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ClientConnexion connexion=fenetre.getConnexion();
		connexion.send("QUIT");	
		try {
			connexion.endSocket();
			connexion.displayEndMess();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
