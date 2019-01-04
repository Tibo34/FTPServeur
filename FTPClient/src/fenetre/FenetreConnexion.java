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
	private Label nomServeur=new Label("Nom du serveur:");
	private Label labPort=new Label("Port:");
	private Label labAdresseIP=new Label("Adresse IP:");
	private JTextField txtServeur=new JTextField(10);	
	private JTextField txtPort=new JTextField(10);
	private JTextField txtIP=new JTextField(10);
	private JTextField txtcommande=new JTextField(20);
	private JButton bouton=new JButton("connexion");
	private Label infoConnexion=new Label("information serveur.");

	public FenetreConnexion() {
		super("Connexion serveur FTP");
		setSize(width, height);
		add(serveur);
		serveur.setSize(width, 100);
		add(info);
		info.add(txtcommande);
		info.add(infoConnexion);
		info.setLayout(new GridLayout(2, 1));
		serveur.setLayout(new FlowLayout());		
		serveur.add(nomServeur);
		serveur.add(txtServeur);
		serveur.add(labPort);
		serveur.add(txtPort);
		serveur.add(labAdresseIP);
		serveur.add(txtIP);
		serveur.add(bouton);
		bouton.addActionListener(new connexionFTPServeur(this));
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
	
	

}
