/*
 * ConversationEditorFrame.java
 *
 * Created on June 5, 2003, 8:43 PM
 */

package org.owasp.webscarab.ui.swing.proxy;

import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;
import org.owasp.webscarab.plugin.proxy.module.ConversationEditor;

import org.owasp.webscarab.ui.swing.RequestPanel;
import org.owasp.webscarab.ui.swing.ResponsePanel;

import javax.swing.SwingUtilities;
import java.lang.Runnable;

/**
 *
 * @author  rdawes
 */
public class ManualEditFrame extends javax.swing.JFrame implements ConversationEditor {
    private Request _request = null;
    private RequestPanel _requestPanel = null;
    private Response _response = null;
    private ResponsePanel _responsePanel = null;
    
    /** Creates new form ConversationEditorFrame */
    public ManualEditFrame() {
        initComponents();
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        _requestPanel = new RequestPanel();
        getContentPane().add(_requestPanel, gridBagConstraints);
        _responsePanel = new ResponsePanel();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 1;
        _responsePanel.setVisible(false);
        getContentPane().add(_responsePanel, gridBagConstraints);
    }

    public synchronized Request editRequest(Request request) {
        synchronized (this) {
            _request = request;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    _requestPanel.setRequest(_request, true);
                    setVisible(true);
                }
            });
            try {
                this.wait();
            } catch (InterruptedException ie) {
                System.out.println("Wait interrupted");
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setVisible(false);
                }
            });
            return _request;
        }
    }
    
    public Response editResponse(Request request, Response response) {
        synchronized (this) {
            _requestPanel.setRequest(request, false);
            return editResponse(response);
        }
    }
    
    public Response editResponse(Response response) {
        synchronized (this) {
            _response = response;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    _responsePanel.setResponse(_response, true);
                    _responsePanel.setVisible(true);
                    show();
                }
            });
            try {
                this.wait();
            } catch (InterruptedException ie) {
                System.out.println("Wait interrupted");
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setVisible(false);
                }
            });
            return _response;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();
        abortButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Intercept");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        cancelButton.setText("Cancel edits");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelButton, new java.awt.GridBagConstraints());

        acceptButton.setText("Accept edits");
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        jPanel1.add(acceptButton, new java.awt.GridBagConstraints());

        abortButton.setText("Abort request");
        abortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abortButtonActionPerformed(evt);
            }
        });

        jPanel1.add(abortButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-400)/2, (screenSize.height-500)/2, 400, 500);
    }//GEN-END:initComponents

    private void abortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abortButtonActionPerformed
        _request = null;
        _response = null;
        synchronized (this) {
            this.notify();
        }
    }//GEN-LAST:event_abortButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        if (_response != null) {
            _response = _responsePanel.getResponse();
        } else if (_request != null) {
            _request = _requestPanel.getRequest();
        }
        synchronized (this) {
            this.notify();
        }
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        synchronized (this) {
            this.notify();
        }
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        synchronized (this) {
            this.notify();
        }
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            ManualEditFrame mef = new ManualEditFrame();
            Request request = new Request();
            request.setMethod("GET");
            request.setURL("http://localhost:8080/");
            request.setVersion("HTTP/1.0");
            request.setHeader("Accept","iamge/gif");
            Request r2 = mef.editRequest(request);
            System.out.println("Got " + r2.toString());
            Response response = new Response();
            response.setVersion("HTTP/1.0");
            response.setStatus("302");
            response.setMessage("Moved");
            response.setHeader("Location","http://localhost:8080/index.html");
            Response r3 = mef.editResponse(response);
            System.out.println("Got response " + r3.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abortButton;
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
