package fenetre;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clientFTP.ClientConnexion;

public class FenetreConnexion extends JFrame {
	
	private int width=800;
	private int height=800;
	private int port;
	private String adressIP;
	private JPanel serveur=new JPanel();
	private JPanel info=new JPanel();
	private JPanel commande=new JPanel();
	private Label nomServeur=new Label("Nom du serveur:");
	private Label labPort=new Label("Port:");
	private Label labAdresseIP=new Label("Adresse IP:");
	private JTextField txtServeur=new JTextField(10);	
	private JTextField txtPort=new JTextField(10);
	private JTextField txtIP=new JTextField(10);	
	private JButton boutonConnexion=new JButton("connexion");
	private JButton boutonEnvoie=new JButton("Evnoie");
	private JButton boutonRecevoir=new JButton("Télécharger");
	private JButton boutonSuppr=new JButton("Supprimer fichier");
	private JButton boutonQuit=new JButton("Quitter");
	private Label infoConnexion=new Label("information serveur.");
	private ClientConnexion connexion=null;
	
	public FenetreConnexion() {
		super("Connexion serveur FTP");
		setSize(width, height);
		add(serveur);
		serveur.setSize(width, 100);
		add(info);
		boutonConnexion.addActionListener(new connexionFTPServeur(this));
		boutonEnvoie.addActionListener(new EnvoieAction(this));
		boutonRecevoir.addActionListener(new RecevoirAction(this));
		boutonSuppr.addActionListener(new SuppressionAction(this));
		boutonQuit.addActionListener(new QuitAction(this));
		commande.add(boutonEnvoie);
		commande.add(boutonRecevoir);
		commande.add(boutonSuppr);
		commande.add(boutonQuit);
		
		info.add(commande);
		commande.setLayout(new FlowLayout());
		
		info.setLayout(new GridLayout(2,1));
		serveur.setLayout(new FlowLayout());		
		serveur.add(nomServeur);
		serveur.add(txtServeur);
		serveur.add(labPort);
		serveur.add(txtPort);
		serveur.add(labAdresseIP);
		serveur.add(txtIP);
		serveur.add(boutonConnexion);
		serveur.add(infoConnexion);
		
		setLayout(new GridLayout(2, 1));
		setVisible(true);		
	}


	


	public Label getInfoConnexion() {
		return infoConnexion;
	}


	public void setInfoConnexion(Label infoConnexion) {
		this.infoConnexion = infoConnexion;
	}


	public JTextField getTxtServeur() {
		return txtServeur;
	}


	public void setTxtServeur(JTextField txtServeur) {
		this.txtServeur = txtServeur;
	}


	public JTextField getTxtPort() {
		return txtPort;
	}


	public void setTxtPort(JTextField txtPort) {
		this.txtPort = txtPort;
	}


	public JTextField getTxtIP() {
		return txtIP;
	}


	public void setTxtIP(JTextField txtIP) {
		this.txtIP = txtIP;
	}


	public ClientConnexion getConnexion() {
		return connexion;
	}


	public void setConnexion(ClientConnexion connexion) {
		this.connexion = connexion;
	}
	
	

}
