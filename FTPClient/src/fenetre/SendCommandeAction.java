package fenetre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import clientFTP.ClientConnexion;

public class SendCommandeAction implements ActionListener {
	
	private FenetreConnexion fenetre;
	private ClientConnexion connexion;
	
	public SendCommandeAction(FenetreConnexion f,ClientConnexion c) {
		fenetre=f;
		connexion=c;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		connexion.setReponse(fenetre.getTxtcommande().getText());

	}

}
