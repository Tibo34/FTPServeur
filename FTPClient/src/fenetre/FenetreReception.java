package fenetre;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import clientFTP.ClientConnexion;

public class FenetreReception extends JFrame {
	
	private String name="Choix du fichier";
	private JButton submit=new JButton("validé");
	private  CheckboxGroup cbg = new CheckboxGroup();
	private ClientConnexion connexion;
	
	public FenetreReception(String[]files, ClientConnexion c){
		connexion=c;
		setTitle(name);
		setSize(400,400);
		setLayout(new GridLayout(files.length+1,1));
		addCheckBox(files);	
		add(submit);
		submit.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {	
				System.out.println(cbg.getSelectedCheckbox().getLabel());
				String file=cbg.getSelectedCheckbox().getLabel();						
				try {
					connexion.writeFile(file);
				} catch (IOException e1) {					
					e1.printStackTrace();
				}
				dispose();
			}
		});
		setVisible(true);
	}

	private void addCheckBox(String[] files) {
		for(String f : files) {
			add(new Checkbox(f, cbg, false));
		}	
	}

	

}
