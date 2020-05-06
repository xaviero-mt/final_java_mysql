package logica;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import model.Multa;
import model.Respuesta;

public class Servicio {
    
    public ArrayList<String> getTiposMulta() {
        ArrayList<String> lstTiposMultas = new ArrayList<>();
        try {
            Connection con = Conexion.startConeccion();
            Statement statement = con.createStatement();
            String query = "SELECT desc_tipo_multa FROM tipo_multa ORDER BY desc_tipo_multa";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                lstTiposMultas.add(rs.getString("desc_tipo_multa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstTiposMultas;
    }
    
    public ArrayList<Multa> getMultas() {
        ArrayList<Multa> lstMultas = new ArrayList<>();
        try {
            Connection con = Conexion.startConeccion();
            Statement statement = con.createStatement();
            String query = "SELECT * FROM multa ORDER BY dni DESC";
            ResultSet rs = statement.executeQuery(query);
            
            Multa objMulta;
            while(rs.next()) { // Se ejecuta la misma cantidad de veces que filas tiene la tabla
                objMulta = new Multa();
                int idMulta      = rs.getInt("id_multa");
                String dni       = rs.getString("dni");
                String tipoMulta = rs.getString("tipo_multa");
                Double multa     = rs.getDouble("monto");
                String correo    = rs.getString("correo");
                int punto        = rs.getInt("punto");
                
                objMulta.setIdMulta(idMulta);
                objMulta.setDni(dni);
                objMulta.setMulta(tipoMulta);
                objMulta.setMonto(multa);
                objMulta.setCorreo(correo);
                objMulta.setPunto(punto);
                lstMultas.add(objMulta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstMultas;
    }
    
        public ArrayList<Multa> getMultas1() {
        ArrayList<Multa> lstMultas = new ArrayList<>();
        try {
            Connection con = Conexion.startConeccion();
            Statement statement = con.createStatement();
            String query = "SELECT * FROM tb_borrados ORDER BY dni DESC";
            ResultSet rs = statement.executeQuery(query);
            
            Multa objMulta;
            while(rs.next()) { // Se ejecuta la misma cantidad de veces que filas tiene la tabla
                objMulta = new Multa();
                int idMulta      = rs.getInt("id_multa");
                String dni       = rs.getString("dni");
                String tipoMulta = rs.getString("tipo_multa");
                Double multa     = rs.getDouble("monto");
                String correo    = rs.getString("correo");
                int punto        = rs.getInt("punto");
                
                objMulta.setIdMulta(idMulta);
                objMulta.setDni(dni);
                objMulta.setMulta(tipoMulta);
                objMulta.setMonto(multa);
                objMulta.setCorreo(correo);
                objMulta.setPunto(punto);
                lstMultas.add(objMulta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstMultas;
    }
    
      public Respuesta validar(Multa multa) {
        Respuesta rpta = new Respuesta();
        rpta.setCodigo(0);
        if(multa.getMonto() > 1000) {
            rpta.setCodigo(1);
            rpta.setMsj("La multa no puede ser mayor a 1000 soles");
            return rpta;
        }
        if(multa.getMulta().equalsIgnoreCase("Alta velocidad") && (multa.getMonto()<400 | multa.getMonto()>570) ){
            rpta.setCodigo(3);
            rpta.setMsj("La cantidad no debe ser menor a 400 ni mayor a 570");   
             return rpta;
        }
         
        if(multa.getMulta().equalsIgnoreCase("Luz roja") && (multa.getMonto()<130 | multa.getMonto()>250) ){
            rpta.setCodigo(4);
            rpta.setMsj("La cantidad no debe ser menor a 130 ni mayor a 250");           
           return rpta;
        }
         
        if(multa.getMulta().equalsIgnoreCase("mal estacionado") && (multa.getMonto()<100 | multa.getMonto()>190) ){
            rpta.setCodigo(5);
            rpta.setMsj("La cantidad no debe ser menor a 100 ni mayor a 190");   
             return rpta;
        }
        if(multa.getMulta().equalsIgnoreCase("Pico y placa") && (multa.getMonto()<130 | multa.getMonto()>330) ){
            rpta.setCodigo(6);
            rpta.setMsj("La cantidad no debe ser menor a 130 ni mayor a 330");
            return rpta;
        }
        return rpta;
    }
            //
  
    public Respuesta insertarMultaProcedure(Multa multa) {
        Respuesta rpta = new Respuesta();
        try {
            
            rpta = validar(multa);
            if(rpta.getCodigo() != 0) {
                return rpta;
            }
            int cantidadMultas = getCantidadMultasByDNI_Fecha(multa.getDni(), multa.getFecha());
            if(cantidadMultas == -1) {
                rpta.setCodigo(-1);
                rpta.setMsj("Hubo un error al contabilizar las multas");
                return rpta;
            }
            System.err.println("cantidadMultas::: "+cantidadMultas);
            if(cantidadMultas >= 2) {
                rpta.setCodigo(3);
                rpta.setMsj("No se puede registrar mas de 2 multas por día");
                return rpta;
            }
            int cantidadPuntos = getTotalPuntosByDNI(multa.getDni());
            if(cantidadPuntos == -1) {
                rpta.setCodigo(-1);
                rpta.setMsj("Hubo un error al contabilizar los puntos");
                return rpta;
            }
            if(cantidadPuntos + multa.getPunto() > 100) {
                rpta.setCodigo(-1);
                rpta.setMsj("Con "+multa.getPunto()+" puntos, supera los 100 puntos maximos");
                return rpta;
            }
            
            // calcular el incremento dependiendo del puntaje
            if(cantidadPuntos + multa.getPunto() >= 50 && cantidadPuntos + multa.getPunto() < 80 ) {
                multa.setMonto(multa.getMonto() - (multa.getMonto() * 0.1) ); // INCREMENTOS
            } else if(cantidadPuntos + multa.getPunto() >= 80) {
                multa.setMonto(multa.getMonto() - (multa.getMonto() * 0.2) ); // INCREMENTOS
            } else { // DESCUENTO
                int arrCantMultUltMeses[] = getCantidadMultasUltMesByDNI(multa.getDni());
                if(arrCantMultUltMeses == null) {
                    rpta.setCodigo(4);
                    rpta.setMsj("Hubo un error al calcular los puntos.");
                    return rpta;
                }
                if(arrCantMultUltMeses[1] == 0) { // 0 = cant multas ult mes //// 1 = cant multas ult 2 meses
                    multa.setMonto(multa.getMonto() - (multa.getMonto() * 0.15) );
                } else if(arrCantMultUltMeses[0] == 0) {
                    multa.setMonto(multa.getMonto() - (multa.getMonto() * 0.05) );
                }
            }
            
            Connection con = Conexion.startConeccion();
            String sql = "call insertar_multa(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            CallableStatement callableSt = con.prepareCall(sql);
            // PARAMETROS DE ENTRADA (IN)
            callableSt.setString(1, multa.getDni());
            callableSt.setDate(2, new java.sql.Date(multa.getFecha().getTime()));
            callableSt.setInt(3, multa.getPunto());
            callableSt.setDouble(4, multa.getMonto());
            callableSt.setString(5, multa.getCorreo());
            callableSt.setString(6, multa.getMulta());
            // PARAMETROS DE SALIDA (OUT)
            callableSt.registerOutParameter(7, Types.INTEGER);
            callableSt.registerOutParameter(8, Types.VARCHAR);
            callableSt.registerOutParameter(9, Types.INTEGER);
            //Call Stored Procedure
            callableSt.executeUpdate(); // MANDA EL QUERY A BD
            rpta.setCodigo(callableSt.getInt(7));
            rpta.setMsj(callableSt.getString(8));
            rpta.setIdGenerado(callableSt.getInt(9));
            return rpta;
        } catch (Exception e) {
            e.printStackTrace();
            rpta.setCodigo(-1);
            rpta.setMsj("Hubo un error desconocido");
            rpta.setIdGenerado(-1);
            System.out.println(e.getMessage());
            return rpta;
        }
    }
    
    public Respuesta insertarMultarestaurada(Multa multa) {
        Respuesta rpta = new Respuesta();
        try {
            
            rpta = validar(multa);
            if(rpta.getCodigo() != 0) {
                return rpta;
            }
            int cantidadMultas = getCantidadMultasByDNI_Fecha(multa.getDni(), multa.getFecha());
            if(cantidadMultas == -1) {
                rpta.setCodigo(-1);
                rpta.setMsj("Hubo un error al contabilizar las multas");
                return rpta;
            }
            System.err.println("cantidadMultas::: "+cantidadMultas);
            if(cantidadMultas >= 2) {
                rpta.setCodigo(3);
                rpta.setMsj("No se puede registrar mas de 2 multas por día");
                return rpta;
            }
            int cantidadPuntos = getTotalPuntosByDNI(multa.getDni());
            if(cantidadPuntos == -1) {
                rpta.setCodigo(-1);
                rpta.setMsj("Hubo un error al contabilizar los puntos");
                return rpta;
            }
            if(cantidadPuntos + multa.getPunto() > 100) {
                rpta.setCodigo(-1);
                rpta.setMsj("Con "+multa.getPunto()+" puntos, supera los 100 puntos maximos");
                return rpta;
            }
            
            Connection con = Conexion.startConeccion();
            String sql = "call insertar_multa_restaurada(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            CallableStatement callableSt = con.prepareCall(sql);
            // PARAMETROS DE ENTRADA (IN)
            callableSt.setString(1, multa.getDni());
            callableSt.setDate(2, new java.sql.Date(multa.getFecha().getTime()));
            callableSt.setInt(3, multa.getPunto());
            callableSt.setDouble(4, multa.getMonto());
            callableSt.setString(5, multa.getCorreo());
            callableSt.setString(6, multa.getMulta());
            // PARAMETROS DE SALIDA (OUT)
            callableSt.registerOutParameter(7, Types.INTEGER);
            callableSt.registerOutParameter(8, Types.VARCHAR);
            callableSt.registerOutParameter(9, Types.INTEGER);
            //Call Stored Procedure
            callableSt.executeUpdate(); // MANDA EL QUERY A BD
            rpta.setCodigo(callableSt.getInt(7));
            rpta.setMsj(callableSt.getString(8));
            rpta.setIdGenerado(callableSt.getInt(9));
            return rpta;
        } catch (Exception e) {
            e.printStackTrace();
            rpta.setCodigo(-1);
            rpta.setMsj("Hubo un error desconocido");
            rpta.setIdGenerado(-1);
            System.out.println(e.getMessage());
            return rpta;
        }
    }
    
    public Respuesta actualizarMulta(Multa multa) {
        Respuesta rpta = new Respuesta();
        try {
            
            if(rpta.getCodigo() != 0) {
                return rpta;
            }
            Connection con = Conexion.startConeccion();
            String sql = "call actualizar_multa(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            CallableStatement callableSt = con.prepareCall(sql);
            // PARAMETROS DE ENTRADA (IN)
            callableSt.setInt(1, multa.getIdMulta());
            callableSt.setString(2, multa.getDni());
            callableSt.setDate(3, new java.sql.Date(multa.getFecha().getTime()));
            callableSt.setInt(4, multa.getPunto());
            callableSt.setDouble(5, multa.getMonto());
            callableSt.setString(6, multa.getCorreo());
            callableSt.setString(7, multa.getMulta());
            // PARAMETROS DE SALIDA (OUT)
            callableSt.registerOutParameter(8, Types.INTEGER);
            callableSt.registerOutParameter(9, Types.VARCHAR);
            callableSt.registerOutParameter(10, Types.INTEGER);
            //Call Stored Procedure
            callableSt.executeUpdate(); // MANDA EL QUERY A BD
            rpta.setCodigo(callableSt.getInt(8));
            rpta.setMsj(callableSt.getString(9));
            rpta.setIdGenerado(callableSt.getInt(10));
            return rpta;
        } catch (Exception e) {
            e.printStackTrace();
            rpta.setCodigo(-1);
            rpta.setMsj("Hubo un error al actualizar la multa." + e.getMessage());
        }
        return rpta;
    }
            
    public Respuesta borrarMulta(int idMulta) {
        Respuesta rpta = new Respuesta();
        try {
            //Connection con = Conexion.startConeccion();
           // String sql="delete from multa where id_multa = ?";
            //PreparedStatement ps = con.prepareCall(sql);
            //ps.setInt(1 , idMulta);
           // ps.executeUpdate();
            //JOptionPane.showMessageDialog(null , "se ha borrado");
            Connection con = Conexion.startConeccion();
            String sql = "call borrar_multa(? ,?, ? ,?)";
            CallableStatement callableSt = con.prepareCall(sql);
            // PARAMETROS DE ENTRADA (IN)
            callableSt.setInt(1, idMulta);
            callableSt.registerOutParameter(2, Types.INTEGER);
            callableSt.registerOutParameter(3, Types.VARCHAR);
            callableSt.registerOutParameter(4, Types.INTEGER);
            //Call Stored Procedure
            callableSt.executeUpdate(); // MANDA EL QUERY A BD
            rpta.setCodigo(callableSt.getInt(2));
            rpta.setMsj(callableSt.getString(3));
            rpta.setIdGenerado(callableSt.getInt(4));
            return rpta;
        } catch (Exception e) {
            e.printStackTrace();
            rpta.setCodigo(-1);
            rpta.setMsj("Hubo un error al borrar la multa.");
        }
        return rpta;
    }
    
        public Respuesta borrarMultas(int idMulta) {
        Respuesta rpta = new Respuesta();
        try {
            Connection con = Conexion.startConeccion();
            String query = "DELETE FROM `sat`.`tb_borrados` WHERE id_multa = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idMulta);
            ps.executeUpdate();
            rpta.setCodigo(0); // 0 = no error
            rpta.setMsj("Se borró la multa.");
        } catch (Exception e) {
            e.printStackTrace();
            rpta.setCodigo(-1);
            rpta.setMsj("Hubo un error al borrar la multa.");
        }
        return rpta;
    }
    
    public int getCantidadMultasByDNI_Fecha(String dni, Date fecha) {
        try {
            //
            Connection con = Conexion.startConeccion();
            String query = "SELECT COUNT(1) AS cantidad FROM `multa` WHERE dni = ? AND DATE(fec_regi) = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, dni);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            System.out.println("QUERY A EJECUTAR: "+ps);
            ResultSet rs = ps.executeQuery();
            int cantidadMultas = -1;
            while(rs.next()) {
                cantidadMultas = rs.getInt("cantidad");
            }
            return cantidadMultas;
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public int getTotalPuntosByDNI(String dni) {
        try {
            //
            Connection con = Conexion.startConeccion();
            String query = "SELECT COALESCE(SUM(punto), 0) AS cantidad FROM multa WHERE dni = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            int cantidadPuntos = -1;
            while(rs.next()) {
                cantidadPuntos = rs.getInt("cantidad");
            }
            return cantidadPuntos;
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
        public int[] getCantidadMultasUltMesByDNI(String dni) {
        try {
            //
            Connection con = Conexion.startConeccion();
            String query = "SELECT (SELECT COUNT(1) FROM `multa` WHERE dni = ? AND fec_Regi BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH) AND CURRENT_DATE) AS ultimo_mes,"
                         + "       (SELECT COUNT(1) FROM `multa` WHERE dni = ? AND fec_Regi BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH) AND CURRENT_DATE) AS ultimo_dos_meses";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, dni);
            ps.setString(2, dni);
            System.out.println("QUERY A EJECUTAR: "+ps);
            ResultSet rs = ps.executeQuery();
            int array_rpta[] = new int[2];
            while(rs.next()) {
                array_rpta[0] = rs.getInt("ultimo_mes");
                array_rpta[1] = rs.getInt("ultimo_dos_meses");
            }
            return array_rpta;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}