//package com.example.asistmed;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.logging.FileHandler;
//import java.util.logging.Handler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;
//
//public class BBDD extends AppCompatActivity {
//
//    //TODO: Revisar funcionamiento de los logger.
//
//
//    //Datos para BBDD del proyecto final AsistMed
//    /*
//    String driver = "com.mysql.jdbc.Driver";
//    String url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
//    String user = "AgZCNnxy2b"
//    String password = "LezDBm5oos";
//    */
//    private final Logger LOGGER = Logger.getLogger("com.example.asistmed.BBDD");
//
//    private Statement st;
//    private Connection cn;
//    private ResultSet rs;
//    private String driver;
//    private String url;
//    private String user;
//    private String password;
//    //private SharedPreferences sp = getApplicationContext().getSharedPreferences("bbdd", Context.MODE_PRIVATE);
//    //private SharedPreferences shared = getApplicationContext().getSharedPreferences("usuarios",Context.MODE_PRIVATE);
//    //private SharedPreferences.Editor editor = sp.edit();
//
//
//    /**
//     * Método constructor sin parámetros.
//     */
//    public BBDD() {
//    }
//
//    /**
//     * Método contructor con parámetros.
//     *
//     * @param st       Variable de tipo Statement.
//     * @param cn       Variable de tipo Connection.
//     * @param rs       Variable de tipo Result Set.
//     * @param url      Variable de tipo String en la que asignaremos la dirección de
//     *                 la BBDD.
//     * @param user     Variable de tipo String en la que asignaremos el nombre
//     *                 del usuario.
//     * @param password Variable de tipo String en la que asignaremos la
//     *                 contraseña.
//     * @param driver   Variable de tipo String en la que asignaremos el driver.
//     */
//    public BBDD(Statement st, Connection cn, ResultSet rs, String url, String user, String password, String driver) throws IOException {
//        this.st = st;
//        this.cn = cn;
//        this.rs = rs;
//        this.url = url;
//        this.user = user;
//        this.password = password;
//        this.driver = driver;
//    }
//
//    /**
//     * Método con el que realizamos la conexión a la BBDD.
//     */
//    public void conectar() {
//        try {
//            //creacionLogger();
//            driver = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
//            user = "AgZCNnxy2b";
//            password = "LezDBm5oos";
//            Class.forName(driver);
//            cn = DriverManager.getConnection(url, user, password);
//        } catch (SQLException | ClassNotFoundException sce) {
//            //LOGGER.log(Level.INFO, sce.getMessage());
//        }
//    }
//
//    /**
//     * Método por el que realizamos la desconexión de la BBDD tras una consulta.
//     */
//    public void desconectarTrasConsulta() {
//        try {
//            //creacionLogger();
//            cn.close();
//            st.close();
//            rs.close();
//        } catch (SQLException e) {
//            //LOGGER.log(Level.INFO, e.getMessage());
//        }
//    }
//
//    /**
//     * Método por el que realizamos la desconexión de la BBDD tras una modificación.
//     */
//    public void desconectarTrasModificacion() {
//        try {
//            //creacionLogger();
//            cn.close();
//            st.close();
//        } catch (SQLException e) {
//            //LOGGER.log(Level.INFO, e.getMessage());
//        }
//    }
//
//    //**TABLA TRATAMIENTO**
//
//    /**
//     * Método por el que realizamos una consulta de toda la tabla tratamiento para añadir a una
//     * lista los registros que se obtengan.
//     */
//    public LinkedList<Tratamiento> consultaTablaTratamiento() {
//
//        //Declaramos las variables necesarias.
//        int total = 0, id_trat, cantidadMed, id_usuario;
//        String nombreTrat, duracionTrat;
//        LinkedList<Tratamiento> listaTr = new LinkedList<>();
//
//        try {
//            //creacionLogger();
//            //Realizamos una consulta para obtener el total de registros.
//            //conectar();
//            driver = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
//            user = "AgZCNnxy2b";
//            password = "LezDBm5oos";
//            Class.forName(driver);
//            cn = DriverManager.getConnection(url, user, password);
//            st = cn.createStatement();
//            rs = st.executeQuery("SELECT COUNT(*) FROM TRATAMIENTO;");
//            while (rs.next()) {
//                total = rs.getInt(1);
//            }
//            cn.close();
//            st.close();
//            rs.close();
//            //desconectarTrasConsulta();
//            System.out.println("TOTI = " + total);
//            //Si existen registros, continuamos con la ejecución.
//            if (total != 0) {
//                //Realizamos la consulta a la tabla.
//                conectar();
//                st = cn.createStatement();
//                rs = st.executeQuery("SELECT * FROM TRATAMIENTO;");
//                while (rs.next()) {
//                /*
//                Obtenemos los datos de la bbdd en cada vuelta, creamos un nuevo objeto Tratamiento
//                con ellos y los añadimos a una lista.
//                 */
//                    for (int i = 1; i > total; i++) {
//                        id_trat = rs.getInt(1);
//                        nombreTrat = rs.getString(2);
//                        duracionTrat = rs.getString(3);
//                        cantidadMed = rs.getInt(4);
//                        id_usuario = rs.getInt(5);
//                        Tratamiento t = new Tratamiento(nombreTrat, duracionTrat, cantidadMed, id_usuario);
//                        listaTr.add(i, t);
//                    }
//                }
//                desconectarTrasConsulta();
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            //LOGGER.log(Level.INFO, e.getMessage());
//        }
//        return listaTr;
//    }
//
//    /**
//     * Método por el que comprobamos si existe un tratamiento en concreto y si no, lo insertamos a
//     * la BBDD.
//     *
//     * @param nombre     Variable de tipo String.
//     * @param duracion   Variable de tipo entero.
//     * @param cantidad   Variable de tipo entero.
//     * @param id_usuario Variable de tipo entero.
//     */
//    public Tratamiento insertarTratamiento(String nombre, String duracion, int cantidad, int id_usuario) {
//
//        Tratamiento t = new Tratamiento(nombre, duracion, cantidad, id_usuario);
//        //Declaramos las variables necesarias.
//        String nombreTratamiento;
//        boolean existe = false;
//
//        try {
//            //Realizamos una consulta para comprobar el nombre de los tratamientos.
//            //creacionLogger();
//            //conectar();
//            driver = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
//            user = "AgZCNnxy2b";
//            password = "LezDBm5oos";
//            Class.forName(driver);
//            cn = DriverManager.getConnection(url, user, password);
//            st = cn.createStatement();
//            rs = st.executeQuery("SELECT nombre_tratamiento FROM TRATAMIENTO;");
//            while (rs.next()) {
//                nombreTratamiento = rs.getString(1);
//                existe = nombre.equalsIgnoreCase(nombreTratamiento);
//                if (existe) {
//                    break;
//                }
//            }
//            cn.close();
//            st.close();
//            rs.close();
//            //desconectarTrasConsulta();
//
//            //Si no existe el nombre, insertamos el nuevo tratamiento.
//            if (!existe) {
//                //conectar();
//                driver = "com.mysql.jdbc.Driver";
//                url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
//                user = "AgZCNnxy2b";
//                password = "LezDBm5oos";
//                Class.forName(driver);
//                cn = DriverManager.getConnection(url, user, password);
//                st = cn.createStatement();
//                st.executeUpdate("INSERT INTO TRATAMIENTO (nombre_tratamiento, duracion, cantidad_med, id_usuario) VALUES ('" + nombre + "','" + duracion + "'," + cantidad + "," + id_usuario + ");");
//                //desconectarTrasModificacion();
//                cn.close();
//                st.close();
//            } else {
//                //Lo que sea. Enviar mensaje informativo o actualizar tratamiento.
//                Toast toast = Toast.makeText(this, "Inserción no realizada.", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            //LOGGER.log(Level.INFO, e.getMessage());
//        }
//        return t;
//    }
//
//    //**TABLA MEDICAMENTO**
//
//
//    //**TABLA USUARIO**
//
//    public boolean compruebaUsuario(String usuario){
//
//        String  compruebaUsuario;
//        boolean existeUsuario = false;
//
//        try{
//            //conectar();
//            driver = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
//            user = "AgZCNnxy2b";
//            password = "LezDBm5oos";
//            Class.forName(driver);
//            cn = DriverManager.getConnection(url, user, password);
//            st = cn.createStatement();
//            rs = st.executeQuery("SELECT nombre_usuario FROM USUARIO;");
//
//            while (rs.next()) {
//                compruebaUsuario = rs.getString(1);
//                existeUsuario = usuario.equalsIgnoreCase(compruebaUsuario);
//                if (existeUsuario) {
//                    existeUsuario = true;
//                    break;
//                }else {
//                    existeUsuario = false;
//                }
//            }
//            desconectarTrasConsulta();
//
//
//        } catch (SQLException | ClassNotFoundException exception) {
//
//        }
//
//        return existeUsuario;
//
//    }
//
//    /**
//     * Método INSERTAR USUARIO por el que comprobamos si existe un nuevo usuario, y si no, lo insertamos en la BBDD.
//     *
//     * @param nombre Variable de tipo String.
//     * @param pass   Variable de tipo String.
//     * @return Devuelve un valor booleano dependiendo de si existe el registro o no.
//     */
//    public boolean insertarUsuario(String nombre, String pass) {
//
//        //Declaramos las variables necesarias.
//        String nombreUsuario;
//        boolean existe = false;
//
//        try {
//            creacionLogger();
//            //Realizamos una consulta para comprobar el nombre de los usuarios.
//
//            conectar();
//            st = cn.createStatement();
//            rs = st.executeQuery("SELECT nombre_usuario FROM USUARIO;");
//            while (rs.next()) {
//                nombreUsuario = rs.getString(1);
//                existe = nombre.equalsIgnoreCase(nombreUsuario);
//                if (existe) {
//                    break;
//                }
//            }
//            desconectarTrasConsulta();
//
//            //Si no existe el nombre de usuario, insertamos el nuevo usuario.
//            if (!existe) {
//                conectar();
//                st = cn.createStatement();
//                st.executeUpdate("INSERT INTO USUARIO (nombre_usuario, password) VALUES ('" + nombre + "','" + pass + "');");
//                desconectarTrasModificacion();
//
//            } else {
//                //Lo que sea
//            }
//        } catch (SQLException | IOException e) {
//            LOGGER.log(Level.INFO, e.getMessage());
//        }
//        return existe;
//    }
//
//
//
//    public void eliminarUsuario(String nombre, String motivoBaja) {
//
//        try {
//            creacionLogger();
//
//            conectar();
//            st = cn.createStatement();
//            st.executeUpdate("DELETE FROM USUARIO WHERE nombre_usuario = '" + nombre + "';");
//            desconectarTrasModificacion();
//        } catch (SQLException | IOException e) {
//            LOGGER.log(Level.INFO, e.getMessage());
//        }
//    }
//
//
//    //**LOGGER**
//    public void creacionLogger() throws IOException {
//
//        //TODO: Comprobar si existe o no un filehandler.
//        Handler fil;
//        Handler fileHandler = new FileHandler("./bitacora.log", true);
//        LOGGER.setUseParentHandlers(false);
//        //Le damos formato.
//        SimpleFormatter simpleFormatter = new SimpleFormatter();
//        fileHandler.setFormatter(simpleFormatter);
//        //Lo configuramos para que inserte en todos los niveles
//        fileHandler.setLevel(Level.ALL);
//        LOGGER.addHandler(fileHandler);
//    }
//
//
//}
