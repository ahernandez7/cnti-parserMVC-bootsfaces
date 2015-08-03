package ve.gob.cnti.windows.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JSeparator;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import ve.gob.cnti.utils.Utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ParserForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// Variables para el parseo
	private String rutaPorleth = "";
	private File[] archivos;

	private JPanel contentPane;
	private JTextField textRutaPorlet;

	private final String[] titulos = { "Nombre", "Archivos" };

	private DefaultTableModel dtm = new DefaultTableModel();
	private JTable table = new JTable(dtm);
	private JScrollPane scroll;

	private JButton btnObtenerArchivos, btnParsear, btnResetConfig;

	private Utils util = new Utils();

	public ParserForm(String rutaPorlet) {
		super("Generador MVC de trámites");
		this.rutaPorleth = rutaPorlet;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 590, 350);
		this.definirVentana();
		textRutaPorlet.setText(this.rutaPorleth);
		JLabel lblRutaDelPortlet = new JLabel("Ruta del Portlet");
		lblRutaDelPortlet.setBounds(391, 26, 184, 15);
		contentPane.add(lblRutaDelPortlet);
	}

	private void definirVentana() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnResetConfig = new JButton("Cambiar Configuración");
		btnResetConfig.setBounds(12, 277, 214, 25);
		contentPane.add(btnResetConfig);

		btnParsear = new JButton("Generar MVC");
		btnParsear.setBounds(391, 277, 184, 25);
		contentPane.add(btnParsear);

		textRutaPorlet = new JTextField();
		textRutaPorlet.setEditable(false);
		textRutaPorlet.setColumns(10);
		textRutaPorlet.setBounds(12, 24, 361, 19);
		contentPane.add(textRutaPorlet);

		dtm.setColumnIdentifiers(titulos);
		scroll = new JScrollPane(table);
		scroll.setBounds(12, 63, 361, 188);
		contentPane.add(scroll);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 263, 563, 15);
		contentPane.add(separator);

		btnObtenerArchivos = new JButton("Obtener archivos");
		btnObtenerArchivos.setBounds(391, 136, 184, 25);
		contentPane.add(btnObtenerArchivos);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] {btnResetConfig, btnObtenerArchivos }));

		btnObtenerArchivos.addActionListener(this);
		btnParsear.addActionListener(this);
		btnResetConfig.addActionListener(this);

		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnObtenerArchivos) {
			this.obtenerArchivos();
		}

		if (e.getSource() == btnResetConfig) {
			this.setVisible(false);
			new ConfigForm(this.rutaPorleth);
		}

		if (e.getSource() == btnParsear) {
			if(this.isFormValid()){
				this.setVisible(false);
				this.generarMVC();
			}
		}
	}

	private void generarMVC() {

		ParserProcessing pp = new ParserProcessing(rutaPorleth, archivos);
		pp.setVisible(true);

	}

	private void obtenerArchivos() {

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Bar Proccess", "bar");
		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(this);
		archivos = chooser.getSelectedFiles();
		boolean contieneArchivosInvalidos = false;
		for (File archivo : archivos) {
			if (archivo.getName().toLowerCase().endsWith("bar")) {
				Object[] aux = { archivo.getName(), archivo.getAbsolutePath() };
				dtm.addRow(aux);
			} else
				contieneArchivosInvalidos = true;
		}

		if (contieneArchivosInvalidos)
			util.ventanaDeMensaje(this, "Los archivos a parsear deben ser de extencion bar", "Error", (short) 0);
	}
	
	private boolean isFormValid(){
		if(this.archivos==null){
			util.ventanaDeMensaje(this, "Debe seleccionar al menos un archivo bar para procesar", "Error", (short) 0);
			return false;
		}
		return true;
	}

}
