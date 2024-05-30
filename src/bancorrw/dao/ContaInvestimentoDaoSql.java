/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import bancorrw.conta.ContaInvestimento;
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
public class ContaInvestimentoDaoSql implements ContaInvestimentoDao{
    private ContaInvestimentoDaoSql(){
    }
    private static ContaInvestimentoDaoSql dao;
    public static ContaInvestimentoDaoSql getContaInvestimentoDaoSql(){
        if (dao == null)
            return dao = new ContaInvestimentoDaoSql();
        else
            return dao;
    } 
    private String insertContaInvstimento = 
        "INSERT INTO " +
            "contas_investimento " +
            "(id_conta," +
            "taxa_remuneracao_investimento," +
            "montante_minimo," +
            "deposito_minimo) " +
        "VALUES" +
            "(?,?,?,?)";
    private String insertConta = 
        "INSERT INTO " +
            "contas " +
            "(id_cliente," +
            "saldo) " +
        "VALUES" +
            "(?,?)";
    private String selectAll = 
        "SELECT "+
            "contas_investimento.id_conta, "+
            "saldo, "+
            "taxa_remuneracao_investimento, "+
            "montante_minimo, "+
            "deposito_minimo, "+
            "clientes.id_cliente,"+
            "nome, "+
            "cpf, "+
            "data_nascimento, "+
            "cartao_credito "+
        "FROM "+
            "contas "+
        "INNER JOIN "+
            "contas_investimento "+
        "ON "+
            "contas.id_conta=contas_investimento.id_conta "+
        "INNER JOIN "+
            "clientes "+
        "ON "+
            "contas.id_cliente=clientes.id_cliente ";
    private String selectById = 
        "SELECT "+
            "contas_investimento.id_conta, "+
            "saldo, "+
            "taxa_remuneracao_investimento, "+
            "montante_minimo, "+
            "deposito_minimo, "+
            "clientes.id_cliente,"+
            "nome, "+
            "cpf, "+
            "data_nascimento, "+
            "cartao_credito "+
        "FROM "+
            "contas "+
        "INNER JOIN "+
            "contas_investimento "+
        "ON "+
            "contas.id_conta=contas_investimento.id_conta "+
        "INNER JOIN "+
            "clientes "+
        "ON "+
            "contas.id_cliente=clientes.id_cliente "+
        "WHERE "+
            "contas.id_conta=?";
    private String selectByCliente = 
        "SELECT "+
            "contas_investimento.id_conta, "+
            "saldo, "+
            "taxa_remuneracao_investimento, "+
            "montante_minimo, "+
            "deposito_minimo, "+
            "clientes.id_cliente,"+
            "nome, "+
            "cpf, "+
            "data_nascimento, "+
            "cartao_credito "+
        "FROM "+
            "contas "+
        "INNER JOIN "+
            "contas_investimento "+
        "ON "+
            "contas.id_conta=contas_investimento.id_conta "+
        "INNER JOIN "+
            "clientes "+
        "ON "+
            "contas.id_cliente=clientes.id_cliente "+
        "WHERE "+
            "contas.id_cliente=?";

    private String updateContaInvestimento = 
        "UPDATE " +
            "contas_investimento " +
        "SET " + 
            "taxa_remuneracao_investimento=? ," +
            "montante_minimo=? ," +            
            "deposito_minimo=? " +
        "WHERE id_conta = ?";    
    private String updateConta = 
        "UPDATE " +
            "contas " +
        "SET " + 
            "saldo=? " +
        "WHERE id_conta = ?";   
    private String deleteById = 
                        "DELETE FROM "+
                            "contas " +
                        "WHERE " +
                            "id_conta=?";
    private String deleteAll = 
                        "DELETE " +
                            "contas,contas_investimento "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_investimento "+
                        "ON "+
                            "contas.id_conta=contas_investimento.id_conta ";     
    private final String ressetAIContas = "ALTER TABLE contas AUTO_INCREMENT =1";

    @Override
    public void add(ContaInvestimento conta) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtAddConta = connection.prepareStatement(insertConta, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtAddInv = connection.prepareStatement(insertContaInvstimento))
        {
            stmtAddConta.setLong(1, conta.getCliente().getId());
            stmtAddConta.setDouble(2, conta.getSaldo());
            stmtAddConta.execute();
            
            ResultSet rs = stmtAddConta.getGeneratedKeys();
            rs.next();
            conta.setId(rs.getLong(1));
                                    
            stmtAddInv.setLong(1, conta.getId());
            stmtAddInv.setDouble(2, conta.getTaxaRemuneracaoInvestimento());
            stmtAddInv.setDouble(3, conta.getMontanteMinimo());
            stmtAddInv.setDouble(4, conta.getDepositoMinimo());
            stmtAddInv.execute();
        } 
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ContaInvestimento> getAll() throws Exception {
        throw new RuntimeException("Não implementado. Implemente aqui");
    }

    @Override
    public ContaInvestimento getById(long id) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtGet = connection.prepareStatement(selectById)){            
            
            stmtGet.setLong(1, id);
            try (ResultSet rs = stmtGet.executeQuery()){
                if (rs.next()){
                    long idConta = rs.getLong("id_conta");
                    double saldo = rs.getDouble("saldo");
                    double taxaRemuneracaoInvest = rs.getDouble("taxa_remuneracao_investimento");
                    double montanteMinimo = rs.getDouble("montante_minimo");
                    double depositoMinimo = rs.getDouble("deposito_minimo");
                    long idCliente = rs.getLong("id_cliente");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dtNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String cartaoCredito = rs.getString("cartao_credito");

                    Cliente cliente = new Cliente(idCliente, nome, cpf, dtNascimento, cartaoCredito);                    
                    ContaInvestimento conta = new ContaInvestimento(taxaRemuneracaoInvest, montanteMinimo, depositoMinimo, saldo, idConta, cliente);
                    return conta;
                } 
                else{
                    throw new Exception("Conta não encontrada para o id:" + id);
                }                
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
    public void update(ContaInvestimento conta) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtUpdate = connection.prepareStatement(updateConta);
             PreparedStatement stmtUpdateCI = connection.prepareStatement(updateContaInvestimento))
        {
            stmtUpdate.setDouble(1, conta.getSaldo());
            stmtUpdate.setLong(2, conta.getId());
            stmtUpdate.executeUpdate();
            
            stmtUpdateCI.setDouble(1, conta.getTaxaRemuneracaoInvestimento());
            stmtUpdateCI.setDouble(2, conta.getMontanteMinimo());
            stmtUpdateCI.setDouble(3, conta.getDepositoMinimo());
            stmtUpdateCI.setLong(4, conta.getId());
            stmtUpdateCI.executeUpdate();
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(ContaInvestimento conta) throws Exception {
        throw new RuntimeException("Não implementado. Implemente aqui");
    }

    @Override
    public void deleteAll() throws Exception {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmtDelete = connection.prepareStatement(deleteAll);
             PreparedStatement stmtResetAI = connection.prepareStatement(ressetAIContas))
        {
            stmtDelete.executeUpdate();
            stmtResetAI.executeUpdate();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ContaInvestimento> getContasInvestimentoByCliente(Cliente cliente) throws Exception  {
        throw new RuntimeException("Não implementado. Implemente aqui");
    }
    
}
