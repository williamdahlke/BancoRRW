/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import bancorrw.conta.ContaCorrente;
import java.sql.Connection;
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
public class ContaCorrenteDaoSql implements ContaCorrenteDao{
    private ContaCorrenteDaoSql(){
    }
    private static ContaCorrenteDaoSql dao;
    public static ContaCorrenteDaoSql getContaCorrenteDaoSql(){
        if (dao == null){
            dao = new ContaCorrenteDaoSql();
        }
        return dao;
    } 
    private String insertContaCorrente = 
        "INSERT INTO " +
            "contas_corrente " +
            "(id_conta," +
            "limite," +
            "taxa_juros_limite) " +
        "VALUES" +
            "(?,?,?)";
    private String insertConta = 
        "INSERT INTO " +
            "contas " +
            "(id_cliente," +
            "saldo) " +
        "VALUES" +
            "(?,?)";
    
    private String updateClienteIdContaCorrente = 
        "UPDATE " +
            "clientes " +
        "SET " + 
            "id_conta_corrente=? " +
        "WHERE id_cliente = ?";
    private String updateContaCorrente = 
        "UPDATE " +
            "contas_corrente " +
        "SET " + 
            "limite=? ," +
            "taxa_juros_limite=? " +
        "WHERE id_conta = ?";    
    private String updateConta = 
        "UPDATE " +
            "contas " +
        "SET " + 
            "saldo=? " +
        "WHERE id_conta = ?";    
    private String selectByCliente = 
                        "SELECT "+
                            "contas_corrente.id_conta, "+
                            "saldo, "+
                            "limite, "+
                            "taxa_juros_limite, "+
                            "clientes.id_cliente,"+
                            "nome, "+
                            "cpf, "+
                            "data_nascimento, "+
                            "cartao_credito "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta "+
                        "INNER JOIN "+
                            "clientes "+
                        "ON "+
                            "contas.id_conta=clientes.id_conta_corrente "+
                        "WHERE "+
                            "contas.id_cliente=?";
        private String selectById = 
                        "SELECT "+
                            "contas_corrente.id_conta, "+
                            "saldo, "+
                            "limite, "+
                            "taxa_juros_limite, "+
                            "clientes.id_cliente,"+
                            "nome, "+
                            "cpf, "+
                            "data_nascimento, "+
                            "cartao_credito "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta "+
                        "INNER JOIN "+
                            "clientes "+
                        "ON "+
                            "contas.id_conta=clientes.id_conta_corrente "+
                        "WHERE "+
                            "contas.id_conta=?";
    private String selectAll = 
                        "SELECT "+
                            "contas_corrente.id_conta, "+
                            "saldo, "+
                            "limite, "+
                            "taxa_juros_limite, "+
                            "clientes.id_cliente,"+
                            "nome, "+
                            "cpf, "+
                            "data_nascimento, "+
                            "cartao_credito "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta "+
                        "INNER JOIN "+
                            "clientes "+
                        "ON "+
                            "contas.id_conta=clientes.id_conta_corrente ";   
    private String deleteById = 
                        "DELETE FROM "+
                            "contas " +
                        "WHERE " +
                            "id_conta=?";
    private String deleteAll = 
                        "DELETE " +
                            "contas,contas_corrente "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta ";            
    private final String ressetAIContas = "ALTER TABLE contas AUTO_INCREMENT =1";
    @Override
    public void add(ContaCorrente contaCorrente) throws Exception {
                
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtAdiciona = connection.prepareStatement(insertConta, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtAdicionaCC = connection.prepareStatement(insertContaCorrente);
             PreparedStatement stmtUpdateIdContaCliente = connection.prepareStatement(updateClienteIdContaCorrente))
        {            
            stmtAdiciona.setLong(1, contaCorrente.getCliente().getId());
            stmtAdiciona.setDouble(2, contaCorrente.getSaldo());
            stmtAdiciona.execute();
            
            ResultSet rs = stmtAdiciona.getGeneratedKeys();
            rs.next();
            contaCorrente.setId(rs.getLong(1));
                        
            stmtAdicionaCC.setLong(1, contaCorrente.getId());
            stmtAdicionaCC.setDouble(2, contaCorrente.getLimite());
            stmtAdicionaCC.setDouble(3, contaCorrente.getTaxaJurosLimite());
            stmtAdicionaCC.execute();            
            
            stmtUpdateIdContaCliente.setLong(1, contaCorrente.getId());
            stmtUpdateIdContaCliente.setLong(2, contaCorrente.getCliente().getId());
            stmtUpdateIdContaCliente.executeUpdate();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }               
    }

    @Override
    public List<ContaCorrente> getAll() throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtGetAll = connection.prepareStatement(selectAll))
        {
            try(ResultSet rs = stmtGetAll.executeQuery()){
                List<ContaCorrente> contas = new ArrayList<ContaCorrente>();
                while (rs.next()){
                    
                    long idConta = rs.getLong("id_conta");
                    double saldo = rs.getDouble("saldo");
                    double limite = rs.getDouble("limite");
                    double taxaJurosLimite = rs.getDouble("taxa_juros_limite");
                    long idCliente = rs.getLong("id_cliente");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String cartaoCredito = rs.getString("cartao_credito");
                    
                    Cliente cliente = new Cliente(idCliente, nome, cpf, dataNascimento, cartaoCredito);
                    contas.add(new ContaCorrente(limite, taxaJurosLimite, idConta, cliente, saldo));                    
                }
                return contas;
            } 
            catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }   
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ContaCorrente getById(long id) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtGetContaC = connection.prepareStatement(selectById);)
        {
            stmtGetContaC.setLong(1, id);
            try (ResultSet rs = stmtGetContaC.executeQuery()){
                if (rs.next()){
                    long idConta = rs.getLong("id_conta");
                    double saldo = rs.getDouble("saldo");
                    double limite = rs.getDouble("limite");
                    double taxaJurosLimite = rs.getDouble("taxa_juros_limite");                    
                    long idCliente = rs.getLong("id_cliente");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dtNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String cartaoCredito = rs.getString("cartao_credito");
                    
                    Cliente cliente = new Cliente(idCliente, nome, cpf, dtNascimento, cartaoCredito);
                    return new ContaCorrente(limite, taxaJurosLimite, idConta, cliente, saldo);
                } 
                else {
                    throw new Exception("Conta corrente não encontrada com id=" + id);
                }
            }
        } 
        catch(Exception e){
           throw new Exception(e.getMessage()); 
        }
    }

    @Override
    public void update(ContaCorrente contaCorrente) throws Exception {
        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtAttConta = connection.prepareStatement(updateConta);
            PreparedStatement stmtAttCC = connection.prepareStatement(updateContaCorrente))
        {
            stmtAttConta.setDouble(1, contaCorrente.getSaldo());
            stmtAttConta.setLong(2, contaCorrente.getId());
            stmtAttConta.executeUpdate();
                                   
            stmtAttCC.setDouble(1, contaCorrente.getLimite());
            stmtAttCC.setDouble(2, contaCorrente.getTaxaJurosLimite());
            stmtAttCC.setLong(3, contaCorrente.getId());
            stmtAttCC.executeUpdate();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(ContaCorrente contaCorrente) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtDelete = connection.prepareStatement(deleteById))
        {
            stmtDelete.setLong(1, contaCorrente.getId());
            stmtDelete.executeUpdate();
            contaCorrente.setId(-1);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void deleteAll() throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtExcluir = connection.prepareStatement(deleteAll);
             PreparedStatement stmtResetAIConta = connection.prepareStatement(ressetAIContas))
        {
            stmtExcluir.executeUpdate();
            stmtResetAIConta.executeUpdate();
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }        
    }

    @Override
    public ContaCorrente getContaCorrenteByCliente(Cliente cliente) throws Exception{
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtGetConta = connection.prepareStatement(selectByCliente))
        {
            stmtGetConta.setLong(1, cliente.getId());
            try (ResultSet rs = stmtGetConta.executeQuery()){
                if (rs.next()){
                    long idConta = rs.getLong("id_conta");
                    double saldo = rs.getDouble("saldo");
                    double limite = rs.getDouble("limite");
                    double taxaJurosLimite = rs.getDouble("taxa_juros_limite");
                    long idCliente = rs.getLong("id_cliente");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dtNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String cartaoCredito = rs.getString("cartao_credito");
                    
                    Cliente clienteBd = new Cliente(idCliente, nome, cpf, dtNascimento, cartaoCredito);
                    return new ContaCorrente(limite, taxaJurosLimite, idConta, clienteBd, saldo);                    
                } 
                else{
                    throw new Exception("Não foi encontrado nenhuma conta corrente para o cliente com o id:" + cliente.getId());
                }
            }
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }            
}
