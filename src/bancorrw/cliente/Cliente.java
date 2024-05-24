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

    private List<ContaInvestimento> contasInvestimento;
    private ContaCorrente contaCorrente;
    private String cartaoCredito;
    
    public Cliente(long id, String nome, String cpf, LocalDate dataNascimento, String cartaoCredito) {
        super(id, nome, cpf, dataNascimento);
        this.cartaoCredito = cartaoCredito;
    }

    public ContaCorrente getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrente contaCorrente) {
        this.contaCorrente = contaCorrente;
    }    
    
    public List<ContaInvestimento> getContasInvestimento() {
        return contasInvestimento;
    }

    public void addContaInvestimento(ContaInvestimento contaInvestimento) {
        this.contasInvestimento.add(contaInvestimento);
    }

    public double getSaldoTotalCliente(){
        return 0;
    }

    public String getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(String cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }
    
    
    



}
