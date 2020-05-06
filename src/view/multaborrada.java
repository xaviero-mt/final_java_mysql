/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Multa;

/**
 *
 * @author juan ivan
 */
public class multaborrada extends javax.swing.JFrame {
        ArrayList<Multa> lstMultasBorradas = new ArrayList<>();

    public multaborrada() {
        initComponents();
        
    }
  private void llenarTabla(ArrayList<Multa> lista){
        DefaultTableModel model = new DefaultTableModel(new String[]{"#", "DNI", "Multa", "Monto", "Correo", "Punto", "Id"}, 0);
        
//        DefaultTableModel model = (DefaultTableModel) tbMultas.getModel();
//        model.setNumRows(0);
        int i = 1;
        for(Multa m : lista) {
            model.addRow(new Object[]{i, m.getDni(), m.getMulta(), m.getMonto(), m.getCorreo(), m.getPunto(), m.getIdMulta()});
            i++;
        }
        tbBorrados = new JTable(model);
        tbBorrados.removeColumn(tbBorrados.getColumnModel().getColumn(6));
        ListSelectionListener lel = new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting()) {
                    setearDatos();
                }
            }
        };
        tbBorrados.getSelectionModel().addListSelectionListener(lel);
        jScrollPane1.setViewportView(tbBorrados);
    }
      Multa multaRestaurar = new Multa();
   int idMulta=0;
    
    private void setearDatos() {
        String dni    = tbBorrados.getModel().getValueAt(tbBorrados.getSelectedRow(), 1  )+"";
        String multa  = tbBorrados.getModel().getValueAt(tbBorrados.getSelectedRow(), 2  )+"";
        String monto  = tbBorrados.getModel().getValueAt(tbBorrados.getSelectedRow(), 3 )+"";
        String correo = tbBorrados.getModel().getValueAt(tbBorrados.getSelectedRow(), 4  )+"";
        String punto  = tbBorrados.getModel().getValueAt(tbBorrados.getSelectedRow(), 5  )+"";
        String id     = tbBorrados.getModel().getValueAt(tbBorrados.getSelectedRow(), 6)+"";
        idMulta = Integer.parseInt(id);
        System.err.println(dni+" - "+multa+" - "+monto+" - "+correo+" - "+punto);
        
        multaRestaurar.setCorreo(correo);
        multaRestaurar.setDni(dni);
        multaRestaurar.setMonto(Double.parseDouble(monto));
        multaRestaurar.setMulta(multa);
        multaRestaurar.setPunto(Integer.parseInt(punto));
        multaRestaurar.setFecha(new Date());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbBorrados = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbBorrados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbBorrados);

        jButton1.setText("restaurar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("elimiinar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(multaborrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(multaborrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(multaborrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(multaborrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new multaborrada().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbBorrados;
    // End of variables declaration//GEN-END:variables
}
