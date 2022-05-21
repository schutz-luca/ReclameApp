package com.example.reclameapp.model.pgAdmin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//Thread -> mini processo que roda em paralelo, sem acesso a User Interface(UI)
//Runnable clase que cria o thread
public class Db implements Runnable {

    public static Connection conexao = null;
    public PreparedStatement comando = null;
    public ResultSet tabela = null;
    public String erro = "";

    public Db() throws Exception {
        conectar();
        if (erro.length() > 0)
            throw new Exception(this.erro);

    }

    public void conectar() {
        Thread tConexao;
        try {
            if ((conexao == null) || (conexao.isClosed())) {
                tConexao = new Thread(this);
                tConexao.start(); //inicia a execução do thread
                try {
                    tConexao.join(); // espera a conclusão do thread
                } catch (Exception ex) {
                    this.erro = "Erro ao conectar: " + ex.getMessage();
                }
            }
        } catch (Exception ex) {
            this.erro = "Erro ao conectar: " + ex.getMessage();
        }
    }

    public void desconectar() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (Exception ex) {
                this.erro = "Erro ao desconectar: " + ex.getMessage();
            } finally {
                conexao = null;
            }
        }
    }

    @Override
    public void run() { // método executado quando o thread.start() é chamado
        try {
            Class.forName("org.postgresql.Driver");
            if ((conexao == null) || (conexao.isClosed())) {
                conexao= DriverManager.getConnection("jdbc:postgresql://10.0.2.2:5432/reclameapp","postgres","admin");
            }
        } catch (Exception ex) {
            this.erro = "Erro de conexao run:" + ex.getMessage();
        }
    }
}