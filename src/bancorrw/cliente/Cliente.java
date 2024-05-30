/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.cliente;

import bancorrw.conta.ContaCorrente;
import bancorrw.conta.ContaInvestimento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author rafae
 */
public class Cliente extends Pessoa{

    private List<ContaInvestimento> contasInvestimento = new ArrayList<ContaInvestimento>();
    private ContaCorrente contaCorrente;
    private String cartaoCredito;
    
    public Cliente(long id, String nome, String cpf, LocalDate dataNascimento, String cartaoCredito) {
        super(id, nome, cpf, dataNascimento);
        this.cartaoCredito = cartaoCredito;
    }

    public ContaCorrente getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrente contaCorrente) throws RuntimeException {
        if (this.contaCorrente != null && this.contaCorrente.getSaldo() > 0){
            throw new RuntimeException("Não pode modificar a conta corrente, pois saldo da original não está zerado. " +
                    "Para fazer isso primeiro zere o saldo da conta do cliente. Saldo=" + this.contaCorrente.getSaldo());                        
        }
        this.contaCorrente = contaCorrente;
    }    
    
    public List<ContaInvestimento> getContasInvestimento() {
        return contasInvestimento;
    }
      
    public void addContaInvestimento(ContaInvestimento contaInvestimento) {
        this.contasInvestimento.add(contaInvestimento);
    }

    public double getSaldoTotalCliente(){                
        final double[] somaInvestimento = {0};        
        
        if (this.contasInvestimento != null){
            this.contasInvestimento.forEach(conta -> somaInvestimento[0] += conta.getSaldo());    
        }
        
        double saldoCorrente = 0;
        if (this.contaCorrente != null){
            saldoCorrente = this.contaCorrente.getSaldo();
        }
        
        return saldoCorrente + somaInvestimento[0];
    }

    public String getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(String cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }
    
    
    



}
