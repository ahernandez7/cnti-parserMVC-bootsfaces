package ve.gob.cnti.windows.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.jdom2.JDOMException;

import ve.gob.cnti.App;
import ve.gob.cnti.utils.Utils;

public class ParserProcessing extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	JTextArea textArea = new JTextArea();
	JScrollPane outScroll = new JScrollPane(textArea);
	JButton btnCerrar = new JButton("Cerrar");
	JButton btnIniciar = new JButton("Iniciar Proceso");

	private Utils util = new Utils();
	// Variables para el parseo
	private String inicialesInst = "", rutaPorleth = "";
	private File[] archivos;

	private String pathTemp;

	public ParserProcessing(String inicialesInst, String rutaPorleth, File[] archivos) {

		this.inicialesInst = inicialesInst;
		this.rutaPorleth = rutaPorleth;
		this.archivos = archivos;

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		outScroll.setBounds(12, 35, 620, 394);
		contentPane.add(outScroll);
		
		btnCerrar.setBounds(519, 436, 117, 25);
		contentPane.add(btnCerrar);
		btnCerrar.addActionListener(this);
		
		btnIniciar.setBounds(371, 436, 138, 25);
		contentPane.add(btnIniciar);
		btnIniciar.addActionListener(this);

		JLabel lblProcesar = new JLabel("Procesando la petición...");
		lblProcesar.setBounds(126, 12, 192, 25);
		contentPane.add(lblProcesar);
		
		setLocationRelativeTo(null);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCerrar) {
			this.dispose();
		}
		if (e.getSource() == btnIniciar) {
			this.procesar();
		}
		
	}

	public void procesar() {
		this.updateTextArea("Procesando...\n");
		this.updateTextArea("\nRuta del Proyecto de Bandeja = " + this.rutaPorleth);
		this.updateTextArea("\nIniciales de la Institución = " + this.inicialesInst);

		// creando carpeta temporal
		this.updateTextArea("\nCreacion de carpetas temporales");
		this.createPathTemp();

		this.updateTextArea("\nArchivos: ");
		for (int i = 0; i < archivos.length; i++) {
			this.unzipFileBarAndParserApp(archivos[i].getAbsolutePath(), i + 1);
		}
		util.ventanaDeMensaje(this, "El procesamiento ha terminado exitosamente", "Generación de MVC", (short) 1);
		btnIniciar.setVisible(false);
		System.out.println(pathTemp);
		util.executeCommand("rm -R " + pathTemp);
	}

	private void unzipFileBarAndParserApp(String archivo, int n) {

		App paser = new App(pathTemp + "MVC_APPS/");
		util.executeCommand("mkdir " + pathTemp + "MVC_APPS");
		util.executeCommand("mkdir " + pathTemp + "MVC_APPS/beansANDcontrollers");
		util.executeCommand("mkdir " + pathTemp + "MVC_APPS/views");

		// copiando bar a carpeta temporal
		this.updateTextArea("\n\tCopiando " + archivo + " a carpeta temporal " + pathTemp + "bars/file" + n);
		this.copyFile(archivo, pathTemp + "bars/file" + n);

		// Descomprimiendo bar
		this.updateTextArea("\n\tDescomprimiendo  " + pathTemp + "bars/file" + n + " a carpeta temporal " + pathTemp + "bars/bar" + n);
		util.executeCommand("mkdir " + pathTemp + "bars/bar" + n);
		this.updateTextArea(util.executeCommand("unzip " + pathTemp + "bars/file" + n + " -d " + pathTemp + "bars/bar" + n));

		// Parseando Aplicación
		try {
			paser.generateFiles((pathTemp + "bars/bar" + n + "/resources/forms/forms.xml"), inicialesInst);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// moviendo las vistas
		this.updateTextArea("\nCopiando las vistas");
		util.executeCommand("cp -r " + pathTemp + "MVC_APPS/views/"+inicialesInst+"/ " + rutaPorleth + "/docroot/views/");

		//moviendo los modelos y controladores
		this.updateTextArea("\nCopiando los modelos y controladores");
		util.executeCommand("cp -r " + pathTemp + "MVC_APPS/beansANDcontrollers/ve/ " + rutaPorleth + "/docroot/WEB-INF/src/");

	}

	private void createPathTemp() {
		this.pathTemp = "/tmp/cntiParserApp/";
		this.pathTemp += util.MD5(new Date().toString()) + "/";
		util.executeCommand("mkdir " + pathTemp);
		util.executeCommand("mkdir " + pathTemp + "bars");
	}

	private void copyFile(String origen, String destino) {
		try {
			util.copyFile_Java7(origen, destino);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(text);
			}
		});
	}

}
