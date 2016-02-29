package ve.gob.cnti.windows.swing;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JTextField;
import ve.gob.cnti.utils.Utils;

public class ConfigForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// Variables para el parseo
	private String rutaPorleth = "";

	private JPanel contentPane;
	private JTextField textRutaPorlet;

	private JButton btnGuardar, btnRutaPortlet;

	public ConfigForm() {
		super("Configuración Inicial");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 590, 175);
		this.definirVentana();
	}

	public ConfigForm(String rutaPorleth) throws HeadlessException {
		super("Configuración Inicial");
		this.rutaPorleth = rutaPorleth;
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
		setBounds(100, 100, 590, 175);
		this.definirVentana();
		textRutaPorlet.setText(rutaPorleth);
	}

	private void definirVentana() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnRutaPortlet = new JButton("Ruta a la Bandeja");
		btnRutaPortlet.setBounds(392, 22, 184, 25);
		contentPane.add(btnRutaPortlet);

		btnGuardar = new JButton("Guardar Configuración");
		btnGuardar.setBounds(165, 71, 246, 25);
		contentPane.add(btnGuardar);

		textRutaPorlet = new JTextField();
		textRutaPorlet.setEditable(false);
		textRutaPorlet.setColumns(10);
		textRutaPorlet.setBounds(12, 25, 361, 19);
		contentPane.add(textRutaPorlet);

		btnRutaPortlet.addActionListener(this);
		btnGuardar.addActionListener(this);

		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnRutaPortlet) {
			this.obtenerRutaPortlet();
		}

		if (e.getSource() == btnGuardar) {
			if (this.formIsValid()) {
				try {
					new Utils().writeConfig(new Config(this.rutaPorleth));
					this.startAplication();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				setVisible(false);
			}
		}
	}

	private boolean formIsValid() {
		if (this.rutaPorleth.isEmpty()) {
			new Utils().ventanaDeMensaje(this, "Indique la ruta al portlet", "Error!!", (short) 0);
			return false;
		} else if (!this.pathExist(rutaPorleth)) {
			new Utils().ventanaDeMensaje(this, "La ruta indicada no contiene el portlet, por favor indique la ruta correcta.", "Error!!", (short) 0);
			return false;
		}
		return true;
	}

	private void startAplication() throws ClassNotFoundException, IOException {
		Config cfg = new Utils().readConfig();
		if (cfg != null) {
			new Utils().ventanaDeMensaje(this, "Configuración registrada.", "Configuración Inicial", (short) 1);
			ParserForm pf = new ParserForm(cfg.getRutaBandeja());
			pf.setVisible(true);
		}
	}

	private void obtenerRutaPortlet() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.showOpenDialog(this);
		rutaPorleth = chooser.getSelectedFile().getAbsolutePath();
		textRutaPorlet.setText(rutaPorleth);

	}

	private boolean pathExist(String rootPath) {
		String path = rootPath + "/src/ve/gob/cnti/gestion/resources";
		if (new File(path).isDirectory()) {
			return true;
		}
		return false;
	}

}
