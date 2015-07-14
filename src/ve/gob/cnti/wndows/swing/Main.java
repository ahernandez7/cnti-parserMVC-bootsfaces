package ve.gob.cnti.wndows.swing;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final String[] titulos = {"Nombre","Archivos"};

	private DefaultTableModel dtm = new DefaultTableModel();
	private JTable jtbl_table = new JTable(dtm);
	private JScrollPane scroll;
	
	private JTextField jtext_pathPorlet,jtext_nombreInst;
	private JLabel jlbl_nombreInst;
	private JButton jbtn_abrirArchivos,jbtn_pathPorlet;
	
	Main() {

		super("Main Window!!");

		this.definirVentana();
		this.setLocationRelativeTo(null);
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void definirVentana() {
		this.setLayout(new GridBagLayout());
		
		//Botones
		jbtn_abrirArchivos = new JButton("Obtener Archivos");
		jlbl_nombreInst = new JLabel("Iniciales de la institucion");
		jbtn_pathPorlet = new JButton("Ruta de la maquina de tramites");
		
		//Tabla		
		dtm.setColumnIdentifiers(titulos);
		jtbl_table.setEnabled(false);
		scroll = new JScrollPane(jtbl_table);
		scroll.setBounds(0, 0,700, 200);
		
		//Campos de texto
		jtext_nombreInst = new JTextField(41);jtext_nombreInst.setEditable(true);
		jtext_pathPorlet = new JTextField(41);jtext_pathPorlet.setEditable(false);
		
		//primera fila
		this.add(jtext_nombreInst,this.limitarComponente(0, 0, 1, 1));
		this.add(jlbl_nombreInst,this.limitarComponente(1, 0, 1, 1));
		
		//Segunda Fila
		this.add(jtext_pathPorlet,this.limitarComponente(0, 1, 1, 1));
		this.add(jbtn_pathPorlet,this.limitarComponente(1, 1, 1, 1));
		
		//Tercera Fila
		this.add(scroll,this.limitarComponente(0, 2, 1, 1));
		this.add(jbtn_abrirArchivos,this.limitarComponente(1, 2, 1, 1));
		
		
		jbtn_abrirArchivos.addActionListener(this);
	}

	public static void main(String[] args) {
		new Main();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbtn_abrirArchivos) {
			this.obtenerArchivos();
		}
	}

	private void obtenerArchivos() {
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Bar Proccess", "bar");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(this);
		File[] archivos = chooser.getSelectedFiles();
		boolean contieneArchivosInvalidos = false;
		for(File archivo:archivos){
			if(archivo.getName().toLowerCase().endsWith("bar")){
//				Object[] aux = {(dtm.getRowCount()+1),archivo.getName(),archivo.getAbsolutePath()};
				Object[] aux = {archivo.getName(),archivo.getAbsolutePath()};
				dtm.addRow(aux);
			}else
				contieneArchivosInvalidos = true;
		}
		
		if(contieneArchivosInvalidos)
			JOptionPane.showMessageDialog(this,"Los archivos a parsear deben ser de extencion bar", "Error",
				    JOptionPane.ERROR_MESSAGE);
	}
	
	private GridBagConstraints limitarComponente(int x,int y, int w,int h){
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		
		return constraints;
	}
}
