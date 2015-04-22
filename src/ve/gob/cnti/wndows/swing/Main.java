package ve.gob.cnti.wndows.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel jlbl_label;
	private JButton jbtn_boton,jbtn_abrirArchivo;
	private JTextField jtxt_texto;

	Main() {

		super("Main Window!!");

		this.definirVentana();

		this.setLocationRelativeTo(null);
		this.setSize(800, 500);// 400 width and 500 height
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);// making the frame visible

	}

	public void definirVentana() {
		this.setLayout(new FlowLayout());
//		jtxt_texto = new JTextField(20);
//		jbtn_boton = new JButton("Enviar");
		jbtn_abrirArchivo = new JButton("Abrir Archivo");
		jlbl_label = new JLabel();
//		this.add(jtxt_texto);
//		this.add(jbtn_boton);
		this.add(jbtn_abrirArchivo);
		this.add(jlbl_label);
//		jbtn_boton.addActionListener(this);
		jbtn_abrirArchivo.addActionListener(this);
	}

	public static void main(String[] args) {
		new Main();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbtn_boton) {
			jlbl_label.setText(jtxt_texto.getText());
		}
		if (e.getSource() == jbtn_abrirArchivo) {
			jlbl_label.setText(this.obtenerRutaArchivo());
		}
	}

	private String obtenerRutaArchivo() {
		
		JFileChooser file = new JFileChooser();
		file.showOpenDialog(this);
		File archivo = file.getSelectedFile();
		String path = archivo.getAbsolutePath();
		archivo.delete();
		
		return path;
	}
}
