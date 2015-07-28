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
	private String inicialesInst = "",rutaPorleth="";
	private File[] archivos;

	private JPanel contentPane;
	private JTextField textIniciales,textRutaPorlet;
	private JLabel lblInicialesDeLa;
		
	private final String[] titulos = { "Nombre", "Archivos" };

	private DefaultTableModel dtm = new DefaultTableModel();
	private JTable table = new JTable(dtm);
	private JScrollPane scroll;
	
	private JButton btnObtenerArchivos,btnParsear,btnRutaPortlet;
	
	private Utils util = new Utils();

	public ParserForm(String inicialesInst,String rutaPorlet) {
		this.inicialesInst=inicialesInst;
		this.rutaPorleth=rutaPorlet;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 590, 380);
		this.definirVentana();
		textRutaPorlet.setText(this.rutaPorleth);
		textIniciales.setText(this.inicialesInst);
	}
	
	private void definirVentana() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textIniciales = new JTextField();
		textIniciales.setBounds(12, 12, 361, 19);
		contentPane.add(textIniciales);
		textIniciales.setColumns(10);
		
		btnRutaPortlet = new JButton("Ruta a la Bandeja");
		btnRutaPortlet.setBounds(391, 53, 184, 25);
		contentPane.add(btnRutaPortlet);
		
		btnParsear = new JButton("Generar MVC");
		btnParsear.setBounds(391, 315, 184, 25);
		contentPane.add(btnParsear);
		
		textRutaPorlet = new JTextField();
		textRutaPorlet.setEditable(false);
		textRutaPorlet.setColumns(10);
		textRutaPorlet.setBounds(12, 56, 361, 19);
		contentPane.add(textRutaPorlet);
		
		lblInicialesDeLa = new JLabel("Iniciales de la Institución");
		lblInicialesDeLa.setBounds(391, 14, 214, 15);
		contentPane.add(lblInicialesDeLa);
		
		dtm.setColumnIdentifiers(titulos);
		scroll = new JScrollPane(table);	
		scroll.setBounds(12, 95, 361, 188);	
		contentPane.add(scroll);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 300, 614, 15);
		contentPane.add(separator);
		
		btnObtenerArchivos = new JButton("Obtener archivos");
		btnObtenerArchivos.setBounds(391, 178, 184, 25);
		contentPane.add(btnObtenerArchivos);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{textIniciales, btnRutaPortlet, btnObtenerArchivos}));
		
		btnObtenerArchivos.addActionListener(this);
		btnParsear.addActionListener(this);
		btnRutaPortlet.addActionListener(this);
		
		setLocationRelativeTo(null);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnObtenerArchivos) {
			this.obtenerArchivos();
		}

		if (e.getSource() == btnRutaPortlet) {
			this.obtenerRutaPortlet();
		}

		if (e.getSource() == btnParsear) {
			this.generarMVC();
			//TODO Limpiar formulario
		}
	}
	
	private void generarMVC() {
		if(this.validar()){
			ParserProcessing pp = new ParserProcessing(inicialesInst, rutaPorleth, archivos);
			pp.setVisible(true);
		}

	}
	
	private boolean validar(){
		
		this.obtenerIniciales();
		
		if (inicialesInst.length() == 0 && archivos == null && rutaPorleth.length()==0){
			util.ventanaDeMensaje(this, "Debe indicar Las iniciales de la institución, seleccionar"
					+ " al menos un modelado(bar) para generar la aplicación e indicar la ruta del "
					+ " proyecto de bandeja", "Error", (short) 0);
			return false;
		}else if (inicialesInst.length() == 0 && archivos == null){
			util.ventanaDeMensaje(this, "Debe indicar Las iniciales de la institución, seleccionar"
					+ " al menos un modelado(bar) para generar la aplicación", "Error", (short) 0);
			return false;
		}else if (archivos == null && rutaPorleth.length()==0){
			util.ventanaDeMensaje(this, "Debe seleccionar al menos un modelado(bar) para generar"
					+ " la aplicación e indicar la ruta del proyecto de bandeja", "Error", (short) 0);
			return false;
		}else if (inicialesInst.length() == 0 && rutaPorleth.length()==0){
			util.ventanaDeMensaje(this, "Debe indicar Las iniciales de la institución e indicar la ruta del "
					+ " proyecto de bandeja", "Error", (short) 0);
			return false;
		}else if (inicialesInst.length()==0){
			util.ventanaDeMensaje(this, "Debe indicar Las iniciales de la institución", "Error", (short) 0);
			return false;
		}else if (archivos == null){
			util.ventanaDeMensaje(this, "Debe seleccionar al menos un modelado(bar) para generar la aplicación "
					, "Error", (short) 0);
			return false;
		}else if (rutaPorleth.length()==0){
			util.ventanaDeMensaje(this, "Debe indicar la ruta del proyecto de bandeja", "Error", (short) 0);
			return false;
		}
		return true;
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

	//TODO Crear lógica que verifique que la ruta del proyecto de bandeja es correcto
	private void obtenerRutaPortlet() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);		
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.showOpenDialog(this);
		rutaPorleth = chooser.getSelectedFile().getAbsolutePath();
		textRutaPorlet.setText(rutaPorleth);
		
	}
	
	private void obtenerIniciales(){
		
		inicialesInst = textIniciales.getText();
		
	}
}
