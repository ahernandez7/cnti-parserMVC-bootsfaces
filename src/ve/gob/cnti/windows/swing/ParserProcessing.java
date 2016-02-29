package ve.gob.cnti.windows.swing;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.jdom2.JDOMException;

import ve.gob.cnti.App;
import ve.gob.cnti.utils.CustomOutputStream;
import ve.gob.cnti.utils.Utils;

public class ParserProcessing extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	JTextArea textArea = new JTextArea();
	JScrollPane outScroll = new JScrollPane(textArea);
	JButton btnCerrar = new JButton("Cerrar");
	JButton btnIniciar = new JButton("Iniciar Proceso");
	JLabel lblProcesar;

	private Utils util = new Utils();
	// Variables para el parseo
	private String rutaPorleth = "";
	private File[] archivos;

	private String pathTemp;

	private PrintStream standardOut;
	
	public ParserProcessing(){			
	}

	public ParserProcessing(String rutaPorleth, File[] archivos) {

		super("Ventana de Procesamiento");

		this.rutaPorleth = rutaPorleth;
		this.archivos = archivos;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Config cfg;
				try {
					cfg = new Utils().readConfig();
					ParserForm pf = new ParserForm(cfg.getRutaBandeja());
					pf.setVisible(true);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		setBounds(100, 100, 850, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		outScroll.setBounds(12, 35, 820, 394);
		contentPane.add(outScroll);
		PrintStream printStream;
		
		try {
			printStream = new PrintStream(new CustomOutputStream(textArea),true, "UTF-8");
			System.setOut(printStream);
			System.setErr(printStream);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		standardOut = System.out;
		
		btnCerrar.setBounds(719, 436, 117, 25);
		contentPane.add(btnCerrar);
		btnCerrar.addActionListener(this);

		btnIniciar.setBounds(569, 436, 138, 25);
		contentPane.add(btnIniciar);
		btnIniciar.addActionListener(this);

		lblProcesar = new JLabel();
		lblProcesar.setBounds(296, -2, 192, 25);
		contentPane.add(lblProcesar);

		setResizable(false);
		setLocationRelativeTo(null);

		this.updateTextArea("\nRuta del Proyecto de Bandeja = " + this.rutaPorleth);
		
		this.updateTextArea("\nCreación de carpetas temporales");
		this.createPathTemp();
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCerrar) {
			Config cfg;
			try {
				cfg = new Utils().readConfig();
				ParserForm pf = new ParserForm(cfg.getRutaBandeja());
				pf.setVisible(true);
				this.dispose();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == btnIniciar) {
			this.startProcess(this);
		}

	}

	private void startProcess(final ParserProcessing pp) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				pp.procesar();
			}
		});
		thread.start();
	}

	public void procesar() {

		lblProcesar.setText("Procesando la petición...");

		for (int i = 0; i < archivos.length; i++) {
			this.unzipFileBarAndParserApp(archivos[i].getAbsolutePath(), i + 1);
		}		
		btnIniciar.setVisible(false);
		util.executeCommand("rm -R " + pathTemp);
	}

	private void unzipFileBarAndParserApp(String archivo, int n) {

		App paser = new App(pathTemp + "MVC_APPS/");
		
		util.executeCommand("mkdir " + pathTemp + "MVC_APPS");
		util.executeCommand("mkdir " + pathTemp + "MVC_APPS/beansANDcontrollers");
		util.executeCommand("mkdir " + pathTemp + "MVC_APPS/views");

		// copiando bar a carpeta temporal
		this.updateTextArea("\nCopiando " + archivo + " a carpeta temporal " + pathTemp + "bars/file" + n);
		this.copyFile(archivo, pathTemp + "bars/file" + n);

		// Descomprimiendo bar
		this.updateTextArea("\nDescomprimiendo  " + pathTemp + "bars/file" + n + " a carpeta temporal " + pathTemp + "bars/bar" + n);
		util.executeCommand("mkdir " + pathTemp + "bars/bar" + n);
		util.executeCommand("unzip " + pathTemp + "bars/file" + n + " -d " + pathTemp + "bars/bar" + n);
		
		// Parseando Aplicación		
		if(paser.isFormValid(pathTemp + "bars/bar" + n + "/resources/forms/forms.xml",(pathTemp + "bars/bar" + n + "/process-design.xml"),this)){
			
			this.updateTextArea("\n\nParseando archivo bar\n");
			try {
				paser.generateFiles((pathTemp + "bars/bar" + n + "/resources/forms/forms.xml"));
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// moviendo las vistas
			this.updateTextArea("\nCopiando las vistas");
			util.executeCommand("cp -r " + pathTemp + "MVC_APPS/views/tramites/ " + rutaPorleth + "/WebContent/views/");
	
			// moviendo los modelos y controladores
			this.updateTextArea("\nCopiando los modelos y controladores");
			util.executeCommand("cp -r " + pathTemp + "MVC_APPS/beansANDcontrollers/ve/ " + rutaPorleth + "/src/");
			
			util.ventanaDeMensaje(this, "El procesamiento ha terminado exitosamente", "Generación de MVC", (short) 1);
		}

	}
	
	public void saveLogErrors(String log,ParserProcessing pp,String name) throws IOException {
		
		util.ventanaDeMensaje(pp, "El procesamiento se ha detenido, verifique los mensajes de error en la consola..", "Error en Generación de MVC", (short) 0);
	    JFileChooser chooser = new JFileChooser("Guardar log de errores");
	    
	    File f = new File(new File("ERRORS_LOG_"+name+".txt").getCanonicalPath());	    
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto", "txt");
	    
		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);	
		chooser.setSelectedFile(f);	
		chooser.setDialogTitle("Guardar log de errores");
	    int retrival = chooser.showSaveDialog(null);
	    
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	        try {
	        	PrintWriter writer = new PrintWriter(chooser.getSelectedFile()+".txt", "UTF-8");
	        	writer.append(log);
	        	writer.close();	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	
	private void createPathTemp() {
		this.pathTemp = "/tmp/cntiParserApp/";
		util.executeCommand("mkdir " + pathTemp);
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
		System.out.println(text);
	}

	@Override
	public String toString() {
		return "ParserProcessing [contentPane=" + contentPane + ", textArea=" + textArea + ", outScroll=" + outScroll + ", btnCerrar=" + btnCerrar + ", btnIniciar=" + btnIniciar + ", lblProcesar=" + lblProcesar + ", util=" + util + ", rutaPorleth=" + rutaPorleth + ", archivos="
				+ Arrays.toString(archivos) + ", pathTemp=" + pathTemp + ", standardOut=" + standardOut + "]";
	}

}
