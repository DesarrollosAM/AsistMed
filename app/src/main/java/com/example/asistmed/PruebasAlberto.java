package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class PruebasAlberto extends AppCompatActivity {

    private TextView txtPanel;
    private LinkedList<Tratamiento> listaTratamientos;
    private Statement st;
    private Connection cn;
    private ResultSet rs;
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://remotemysql.com:3306/AgZCNnxy2b";
    private String user = "AgZCNnxy2b";
    private String password = "LezDBm5oos";
    private BBDD b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_alberto);

        txtPanel = (TextView)findViewById(R.id.txtPanel);

        try{
            b = new BBDD(st, cn, rs, url, user, password, driver);
            listaTratamientos = b.consultaTablaTratamiento();
            txtPanel.setText(listaTratamientos.size());
        } catch (IOException e){

        }


    }
}