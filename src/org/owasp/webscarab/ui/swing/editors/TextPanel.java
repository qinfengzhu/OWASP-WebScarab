/*
 * HexEditor.java
 *
 * Created on November 4, 2003, 8:23 AM
 */

package org.owasp.webscarab.ui.swing.editors;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Event;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Keymap;
import java.awt.Container;
import javax.swing.JFrame;

/**
 *
 * @author  rdawes
 */
public class TextPanel extends javax.swing.JPanel implements ByteArrayEditor {
    
    private boolean _editable = false;
    // we assume it is modified - we need to add a keystroke listener to do this properly
    private boolean _modified = false;
    
    private byte[] _data = new byte[0];
    
    private SearchDialog _searchDialog = null;
    
    /** Creates new form HexEditor */
    public TextPanel() {
        initComponents();
        Keymap keymap = textTextArea.addKeymap("MySearchBindings",
                                           textTextArea.getKeymap());
        //Ctrl-f to open the search dialog
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK), new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if (_searchDialog == null) {
                    Container c = textScrollPane;
                    while (! (c instanceof JFrame) && c.getParent() != null) {
                        c = c.getParent();
                    }
                    if (c instanceof JFrame) {
                        _searchDialog = new SearchDialog((JFrame) c, false);
                        _searchDialog.setSearchTextComponent(textTextArea);
                    } else {
                        System.err.println("No JFrame parent found!");
                        return;
                    }
                }
                _searchDialog.show();
            }
        });
        
        textTextArea.setKeymap(keymap);
        
        textTextArea.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent evt) {
		_modified = true;
            }
            public void removeUpdate(DocumentEvent evt) {
		_modified = true;
            }
            public void insertUpdate(DocumentEvent evt) {
		_modified = true;
            }
        });
    }
    
    public String getName() {
        return "Text";
    }
    
    public String[] getContentTypes() {
        return new String[] { "text/.*", "application/x-javascript", "application/x-www-form-urlencoded" };
    }

    public void setEditable(boolean editable) {
        _editable = editable;
        textTextArea.setEditable(editable);
        if (editable) {
            textTextArea.setBackground(new java.awt.Color(255, 255, 255));
        } else {
            textTextArea.setBackground(new java.awt.Color(204, 204, 204));
        }
        // we could do things like make buttons visible and invisible here
    }
    
    public void setBytes(byte[] bytes) {
        if (bytes != null) {
            textTextArea.setText(new String(bytes));
        } else {
            textTextArea.setText("");
        }
        textTextArea.setCaretPosition(0);
        // always set _modified false AFTER setting the text, since the Document listener
        // will set it to true when adding the text
        _modified = false;
    }
    
    public boolean isModified() {
        return _editable && _modified;
    }

    public byte[] getBytes() {
        return textTextArea.getText().getBytes();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        textScrollPane = new javax.swing.JScrollPane();
        textTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        textTextArea.setBackground(new java.awt.Color(204, 204, 204));
        textTextArea.setEditable(false);
        textScrollPane.setViewportView(textTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(textScrollPane, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane textScrollPane;
    private javax.swing.JTextArea textTextArea;
    // End of variables declaration//GEN-END:variables
    
    
    public static void main(String[] args) {
        javax.swing.JFrame top = new javax.swing.JFrame("Text Editor");
        top.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        
        TextPanel tp = new TextPanel();
        top.getContentPane().add(tp);
        top.setBounds(100,100,600,400);
        try {
            tp.setBytes(new byte[] {0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f});
            tp.setEditable(false);
            // he.setModel(new DefaultHexDataModel(new byte[0], true));
            top.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
}
