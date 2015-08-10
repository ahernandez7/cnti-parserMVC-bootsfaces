package ve.gob.cnti.utils;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
 

public class CustomOutputStream extends OutputStream {
	
    private JTextArea textArea;
     
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }
    
    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException
    {
        final String text = new String (buffer, offset, length);
       
        textArea.append (text);
        
        textArea.setCaretPosition(textArea.getDocument().getLength());       
    }

    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
}
