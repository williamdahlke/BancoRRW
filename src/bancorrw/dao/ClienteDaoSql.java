/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rafae
 */
public class ClienteDaoSql implements ClienteDao{
    private ClienteDaoSql(){
    }
    private static ClienteDaoSql dao;
    public static ClienteDaoSql getClienteDaoSql(){
        if (dao == null)
        {
            dao = new ClienteDaoSql();
        }
        return dao;
    }  
    private String selectAll = 
        "SELECT "+ 
            "id_cliente, " +
            "nome, " +
            "cpf, " +
            "data_nascimento, " +
            "cartao_credito " +
        "FROM " +
            "clientes ";
    private String selectById = selectAll + " " + 
            "WHERE "+
                "id_cliente=?";
    private String insertCliente = 
        "INSERT INTO " +
            "clientes " +
            "(nome," +
            "cpf," +
            "data_nascimento, " +
            "cartao_credito) " +
        "VALUES" +
            "(?,?,?,?)";
    private String updateCliente = 
        "UPDATE " +
            "clientes " +
        "SET " + 
            "nome=?, " +
            "cpf=?, " +
            "data_nascimento=?, " +
            "cartao_credito=? " +
        "WHERE id_cliente = ?";
    private String deleteById = 
        "DELETE FROM "+
            "clientes "+
        "WHERE id_cliente=?";
    private String deleteAll = 
        "DELETE FROM "+
            "clientes ";
    private final String ressetAIPessoas = "ALTER TABLE clientes AUTO_INCREMENT =1";
    private final String ressetAIContas = "ALTER TABLE contas AUTO_INCREMENT =1";
    @Override
    public void add(Cliente cliente) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtAdiciona = connection.prepareStatement(insertCliente, Statement.RETURN_GENERATED_KEYS);)
        {            
            stmtAdiciona.setString(1, cliente.getNome());
            stmtAdiciona.setString(2, cliente.getCpf());
            stmtAdiciona.setDate(3,Date.valueOf(cliente.getDataNascimento()));
            stmtAdiciona.setString(4,cliente.getCartaoCredito());
            stmtAdiciona.execute();
            
            ResultSet rs = stmtAdiciona.getGeneratedKeys();
            rs.next();
            long id = rs.getLong(1);
            cliente.setId(id);
        } 
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Cliente> getAll() throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtGetAll = connection.prepareStatement(selectAll);)
        {  
            try (ResultSet rs = stmtGetAll.executeQuery()) 
            {
                List<Cliente> clientes = new ArrayList<Cliente>();
                while (rs.next()) {
                    long id = rs.getLong("id_cliente");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String cartaoCredito = rs.getString("cartao_credito");

                    // adicionando o objeto à lista
                    clientes.add(new Cliente(id,nome,cpf,dataNascimento,cartaoCredito));
                }
                return clientes;
            } 
        } 
        catch (Exception e){
            throw new Exception(e.getMessage());
        }        
    }

    @Override
    public Cliente getById(long id) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtGetCliente = connection.prepareStatement(selectById);)
        {
            stmtGetCliente.setLong(1, id);
            try (ResultSet rs = stmtGetCliente.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String cartaoCredito = rs.getString("cartao_credito");

                    // adicionando o objeto à lista
                    return new Cliente(id,nome,cpf,dataNascimento, cartaoCredito);
                } 
                else {
                    throw new Exception("Cliente não encontrado com id=" + id);
                }
            }
        } 
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void update(Cliente cliente) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtAtualiza = connection.prepareStatement(updateCliente);)
        {
            stmtAtualiza.setString(1,cliente.getNome());
            stmtAtualiza.setString(2,cliente.getCpf());
            stmtAtualiza.setDate(3,Date.valueOf(cliente.getDataNascimento()));
            stmtAtualiza.setString(4,cliente.getCartaoCredito());
            stmtAtualiza.setLong(5,cliente.getId());
            stmtAtualiza.executeUpdate();         
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Cliente cliente) throws Exception {
        try (Connection connection=ConnectionFactory.getConnection();
             PreparedStatement stmtExcluir = connection.prepareStatement(deleteById);)
        {
            stmtExcluir.setLong(1, cliente.getId());
            stmtExcluir.executeUpdate();
            cliente.setId(-1);
        } 
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void deleteAll() throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtExcluir = connection.prepareStatement(deleteAll);
             PreparedStatement stmtResetAIPessoas = connection.prepareStatement(ressetAIPessoas);)
        {
            stmtExcluir.executeUpdate();
            stmtResetAIPessoas.executeUpdate();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
}
