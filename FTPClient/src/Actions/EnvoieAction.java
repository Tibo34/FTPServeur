package Actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import clientFTP.ClientConnexion;
import fenetre.FenetreConnexion;

public class EnvoieAction implements ActionListener {

	private FenetreConnexion fenetre;
	private JFileChooser choose=new JFileChooser();
	
	public EnvoieAction(FenetreConnexion f) {
		fenetre=f;
	}

	
	/**
	 * envoie le fichier
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ClientConnexion connexion=fenetre.getConnexion();
		connexion.send("STOR");
		int returnVal = choose.showOpenDialog(fenetre);// TODO Auto-generated method stub
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = choose.getSelectedFile(); // ouvre une explorateur de fichier      
            try {
            	connexion.sendFile(file);//envoie le fichier
			} catch (IOException e) {
				e.printStackTrace();
			}	
        }
		
		

	}

}
