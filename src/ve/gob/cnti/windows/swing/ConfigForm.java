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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.JLabel;

import ve.gob.cnti.utils.JTextFieldLimit;
import ve.gob.cnti.utils.Utils;

public class ConfigForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// Variables para el parseo
	private String inicialesInst = "", rutaPorleth = "";

	private JPanel contentPane;
	private JTextField textIniciales, textRutaPorlet;
	private JLabel lblInicialesDeLa;

	private JButton btnGuardar, btnRutaPortlet;

	public ConfigForm() {
		super("Configuración Inicial");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 590, 175);
		this.definirVentana();
	}

	public ConfigForm(String inicialesInst, String rutaPorleth) throws HeadlessException {
		super("Configuración Inicial");
		this.inicialesInst = inicialesInst;
		this.rutaPorleth = rutaPorleth;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Config cfg;
				try {
					cfg = new Utils().readConfig();
					ParserForm pf = new ParserForm(cfg.getInicialesinstitucion(), cfg.getRutaBandeja());
					pf.setVisible(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
		setBounds(100, 100, 590, 175);
		this.definirVentana();
		textIniciales.setText(inicialesInst);
		textRutaPorlet.setText(rutaPorleth);
	}

	private void definirVentana() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textIniciales = new JTextField(15);
		textIniciales.setBounds(12, 12, 361, 19);
		contentPane.add(textIniciales);
		textIniciales.setColumns(10);
		textIniciales.setDocument(new JTextFieldLimit(12));

		btnRutaPortlet = new JButton("Ruta a la Bandeja");
		btnRutaPortlet.setBounds(391, 53, 184, 25);
		contentPane.add(btnRutaPortlet);

		btnGuardar = new JButton("Guardar Configuración");
		btnGuardar.setBounds(135, 98, 246, 25);
		contentPane.add(btnGuardar);

		textRutaPorlet = new JTextField();
		textRutaPorlet.setEditable(false);
		textRutaPorlet.setColumns(10);
		textRutaPorlet.setBounds(12, 56, 361, 19);
		contentPane.add(textRutaPorlet);

		lblInicialesDeLa = new JLabel("Iniciales de la Institución");
		lblInicialesDeLa.setBounds(391, 14, 214, 15);
		contentPane.add(lblInicialesDeLa);

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
			this.inicialesInst = textIniciales.getText();
			if (this.formIsValid()) {
				try {
					new Utils().writeConfig(new Config(this.inicialesInst, this.rutaPorleth));
					this.startAplication();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				setVisible(false);
			}
		}
	}

	private boolean formIsValid() {
		// TODO VERIFICAR INICIALES Y RUTA DEL PORTLET
		if (this.inicialesInst.isEmpty() && this.rutaPorleth.isEmpty()) {
			new Utils().ventanaDeMensaje(this, "Todos los campos son obligatorios", "Error!!", (short) 0);
			return false;
		} else if (this.inicialesInst.isEmpty()) {
			new Utils().ventanaDeMensaje(this, "Indique las Iniciales de la institución", "Error!!", (short) 0);
			return false;
		} else if (this.rutaPorleth.isEmpty()) {
			new Utils().ventanaDeMensaje(this, "Indique la ruta al portlet", "Error!!", (short) 0);
			return false;
		} else if (!this.pathExist(rutaPorleth)) {
			new Utils().ventanaDeMensaje(this, "La ruta indicada no contiene el portlet, por favor indique la ruta correcta.", "Error!!", (short) 0);
			return false;
		} else if (!this.inicialesCorrectas(inicialesInst)) {
			return false;
		}
		return true;
	}

	private void startAplication() throws ClassNotFoundException, IOException {
		Config cfg = new Utils().readConfig();
		if (cfg != null) {
			new Utils().ventanaDeMensaje(this, "Configuración registrada.", "Configuración Inicial", (short) 1);
			ParserForm pf = new ParserForm(cfg.getInicialesinstitucion(), cfg.getRutaBandeja());
			pf.setVisible(true);
		}
	}

	// TODO Crear lógica que verifique que la ruta del proyecto de bandeja es correcto
	private void obtenerRutaPortlet() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.showOpenDialog(this);
		rutaPorleth = chooser.getSelectedFile().getAbsolutePath();
		textRutaPorlet.setText(rutaPorleth);

	}

	private boolean pathExist(String rootPath) {
		String path = rootPath + "/docroot/WEB-INF/src/ve/gob/cnti/gestion/resources";
		if (new File(path).isDirectory()) {
			return true;
		}
		return false;
	}

	private boolean inicialesCorrectas(String iniciales) {

		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("^([A-Za-z]+)$");
		matcher = pattern.matcher(iniciales);
		if (!matcher.matches()) {
			new Utils().ventanaDeMensaje(this, "Las iniciales solo deben estar conformadas por letras.", "Error!!", (short) 0);
			return false;
		}

		return true;
	}

}
