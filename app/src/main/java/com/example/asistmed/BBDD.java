package com.example.asistmed;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

public class BBDD extends AppCompatActivity {

    //TODO: añadir logger a los catch.


    //Datos para BBDD del proyecto final AsistMed
    /*
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
    String user = "AgZCNnxy2b"
    String password = "LezDBm5oos";
    */

    private Statement st;
    private Connection cn;
    private ResultSet rs;
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
    private String user = "AgZCNnxy2b";
    private String password = "LezDBm5oos";
    private String tablaTratamiento = "TRATAMIENTO";
    private String tablaMedicamento = "MEDICAMENTO";
    private SharedPreferences sp = getApplicationContext().getSharedPreferences("bbdd", Context.MODE_PRIVATE);
    //private SharedPreferences shared = getApplicationContext().getSharedPreferences("usuarios",Context.MODE_PRIVATE);
    private SharedPreferences.Editor editor = sp.edit();


    /**
     * Método constructor sin parámetros.
     */
    public BBDD() {
    }

    /**
     * Método contructor con parámetros.
     *
     * @param st       Variable de tipo Statement.
     * @param cn       Variable de tipo Connection.
     * @param rs       Variable de tipo Result Set.
     * @param url      Variable de tipo String en la que asignaremos la dirección de
     *                 la BBDD.
     * @param user     Variable de tipo String en la que asignaremos el nombre
     *                 del usuario.
     * @param password Variable de tipo String en la que asignaremos la
     *                 contraseña.
     * @param driver   Variable de tipo String en la que asignaremos el driver.
     */
    public BBDD(Statement st, Connection cn, ResultSet rs, String url, String user, String password, String driver) {
        this.st = st;
        this.cn = cn;
        this.rs = rs;
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
    }

    /**
     * Método con el que realizamos la conexión a la BBDD.
     */
    public void conectar() {
        try {
            Class.forName(driver);
            cn = DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException sce) {
            System.out.println(sce.getCause());
        }
    }

    /**
     * Método por el que realizamos la desconexión de la BBDD tras una consulta.
     */
    public void desconectarTrasConsulta() {
        try {
            cn.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
    }

    /**
     * Método por el que realizamos la desconexión de la BBDD tras una modificación.
     */
    public void desconectarTrasModificacion() {
        try {
            cn.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
    }

    /**
     * Método por el que realizamos una consulta de toda la tabla tratamiento.
     */
    public LinkedList<Tratamiento> consultaTablaTratamiento() {

        //Declaramos las variables necesarias.
        int total = 0, id_trat, duracionTrat, cantidadMed, id_usuario;
        String nombreTrat;
        LinkedList<Tratamiento> listaTr = new LinkedList<>();

        try {
            //Realizamos una consulta para obtener el total de registros.
            conectar();
            st = cn.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM " + tablaTratamiento + ";");
            while (rs.next()) {
                total = rs.getInt(1);
            }
            desconectarTrasConsulta();

            //Si existen registros, continuamos con la ejecución.
            if (total != 0) {
                //Realizamos la consulta a la tabla.
                conectar();
                st = cn.createStatement();
                rs = st.executeQuery("SELECT * FROM " + tablaTratamiento + ";");
                while (rs.next()) {
                /*
                Obtenemos los datos de la bbdd en cada vuelta, creamos un nuevo objeto Tratamiento
                con ellos y los añadimos a una lista.
                 */
                    for (int i = 1; i > total; i++) {
                        id_trat = rs.getInt(1);
                        nombreTrat = rs.getString(2);
                        duracionTrat = rs.getInt(3);
                        cantidadMed = rs.getInt(4);
                        id_usuario = rs.getInt(5);
                        Tratamiento t = new Tratamiento(id_trat, nombreTrat, duracionTrat, cantidadMed, id_usuario);
                        listaTr.add(i, t);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
        return listaTr;
    }


}
