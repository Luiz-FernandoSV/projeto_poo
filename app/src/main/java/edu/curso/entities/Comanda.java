package edu.curso.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.curso.enums.EStatusComanda;

public class Comanda {

    private long idComanda;
    private long numero;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private double valorTotal;
    private EStatusComanda status;
    private List<ItemComanda> itens = new ArrayList<>();

    public Comanda() {
    }

    public long getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(long idComanda) {
        this.idComanda = idComanda;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public EStatusComanda getStatus() {
        return status;
    }

    public void setStatus(EStatusComanda status) {
        this.status = status;
    }

    public List<ItemComanda> getItens() {
        return itens;
    }

    public void setItens(List<ItemComanda> itens) {
        this.itens = itens;
    }
}
