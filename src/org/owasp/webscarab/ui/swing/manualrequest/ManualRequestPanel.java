/*
 * ManualRequestPanel.java
 *
 * Created on September 15, 2003, 11:16 AM
 */

package org.owasp.webscarab.ui.swing.manualrequest;

import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;

import org.owasp.webscarab.plugin.manualrequest.ManualRequest;

import org.owasp.webscarab.ui.swing.SwingPlugin;
import org.owasp.webscarab.ui.swing.SwingWorker;
import org.owasp.webscarab.ui.swing.RequestPanel;
import org.owasp.webscarab.ui.swing.ResponsePanel;

import javax.swing.border.TitledBorder;

/**
 *
 * @author  rdawes
 */
public class ManualRequestPanel extends javax.swing.JPanel implements SwingPlugin {
    
    private ManualRequest _manualRequest;
    private SwingWorker _sw = null;
    
    private RequestPanel requestPanel;
    private ResponsePanel responsePanel;
    
    /** Creates new form ManualRequestPanel */
    public ManualRequestPanel(ManualRequest manualRequest) {
        _manualRequest = manualRequest;
        
        initComponents();

        requestPanel = new RequestPanel();
        requestPanel.setEditable(true);
        requestPanel.setBorder(new TitledBorder("Request"));
        conversationSplitPane.setLeftComponent(requestPanel);

        responsePanel = new ResponsePanel();
        responsePanel.setBorder(new TitledBorder("Response"));
        conversationSplitPane.setRightComponent(responsePanel);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        conversationSplitPane = new javax.swing.JSplitPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        getCookieButton = new javax.swing.JButton();
        fetchResponseButton = new javax.swing.JButton();
        updateCookiesButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        conversationSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jLabel1.setText("jLabel1");
        conversationSplitPane.setLeftComponent(jLabel1);

        jLabel2.setText("jLabel2");
        conversationSplitPane.setRightComponent(jLabel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(conversationSplitPane, gridBagConstraints);

        getCookieButton.setText("Get Cookies");
        getCookieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getCookieButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        add(getCookieButton, gridBagConstraints);

        fetchResponseButton.setText("Fetch Response");
        fetchResponseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fetchResponseButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        add(fetchResponseButton, gridBagConstraints);

        updateCookiesButton.setText("Update CookieJar");
        updateCookiesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCookiesButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        add(updateCookiesButton, gridBagConstraints);

    }//GEN-END:initComponents

    private void updateCookiesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCookiesButtonActionPerformed
        _manualRequest.updateCookies();
    }//GEN-LAST:event_updateCookiesButtonActionPerformed

    private void getCookieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getCookieButtonActionPerformed
        Request request = requestPanel.getRequest();
        if (request != null) {
            _manualRequest.addRequestCookies(request);
            requestPanel.setRequest(request);
        }
    }//GEN-LAST:event_getCookieButtonActionPerformed

    private void fetchResponseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fetchResponseButtonActionPerformed
        final Request request = requestPanel.getRequest();
        if (request != null) {
            fetchResponseButton.setEnabled(false);
            responsePanel.setResponse(null);
            new SwingWorker() {
                public Object construct() {
                    return _manualRequest.fetchResponse(request);
                }

                //Runs on the event-dispatching thread.
                public void finished() {
                    Response response = (Response) getValue();
                    if (response != null) {
                        responsePanel.setResponse(response);
                    }
                    fetchResponseButton.setEnabled(true);
                }
            }.start();
        } else {
            System.err.println("Can't fetch a null request");
        }
    }//GEN-LAST:event_fetchResponseButtonActionPerformed

    public javax.swing.JPanel getPanel() {
        return this;
    }    
    
    public String getPluginName() {
        return new String("Manual Request");
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane conversationSplitPane;
    private javax.swing.JButton fetchResponseButton;
    private javax.swing.JButton getCookieButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton updateCookiesButton;
    // End of variables declaration//GEN-END:variables
    
}
