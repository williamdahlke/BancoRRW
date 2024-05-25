/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.conta;

import bancorrw.cliente.Cliente;

/**
 *
 * @author rafae
 */
public class ContaCorrente extends Conta{
    private double limite;
    private double taxaJurosLimite;

    public ContaCorrente(double limite, double taxaJurosLimite, long id, Cliente cliente, double saldo) {
        super(id, cliente, saldo);
        this.limite = limite;
        this.taxaJurosLimite = taxaJurosLimite;
        this.getCliente().setContaCorrente(this);
    }
           
    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public double getTaxaJurosLimite() {
        return taxaJurosLimite;
    }

    public void setTaxaJurosLimite(double taxaJurosLimite) {
        this.taxaJurosLimite = taxaJurosLimite;
    }
    
    @Override
    public void aplicaJuros() {
        if (this.getSaldo() < 0){
        }
    }    
    
    public void saca(double valor) throws RuntimeException{
        if (valor < 0){
            throw new RuntimeException(
            "Valor do saque não pode ser negativo ou zero. Valor=" + valor);
        }
                
        if (valor > this.limite)            
        {                        
            throw new RuntimeException(
                """
                Saldo insuficiente na conta.
                Valor saque=""" + valor +
                "\nSaldo=" + this.getSaldo()+
                "\nLimite=" + this.getLimite()
            );
        }
        super.saca(valor);
    }


    
}
