package br.com.fiap.model;

import br.com.fiap.annotations.Coluna;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ESTAGIARIO")
public class Estagiario extends Funcionario {

    private static final double LIMITE_HORAS_SEMANAIS = 20.0;

    @Coluna(nome = "VL_BOLSA_AUXILIO", tipo = "NUMBER(10,2)")
    @Column(name = "VL_BOLSA_AUXILIO")
    private double bolsaAuxilio;

    @Coluna(nome = "VL_VALE_TRANSPORTE", tipo = "NUMBER(10,2)")
    @Column(name = "VL_VALE_TRANSPORTE")
    private double valeTransporte;

    // Construtores

    public Estagiario() {}

    public Estagiario(String nome, double horasTrabalhadas, double valorHora,
                      double bolsaAuxilio, double valeTransporte) {
        super(nome, horasTrabalhadas, valorHora);
        this.bolsaAuxilio   = bolsaAuxilio;
        this.valeTransporte = valeTransporte;
    }

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public double getBolsaAuxilio()         { return bolsaAuxilio; }
    public void setBolsaAuxilio(double b)   { this.bolsaAuxilio = b; }

    public double getValeTransporte()       { return valeTransporte; }
    public void setValeTransporte(double v) { this.valeTransporte = v; }

    // Métodos sobrescritos

    @Override
    public double calcularSalario() {
        double adicional = 0;
        if (getHorasTrabalhadas() > LIMITE_HORAS_SEMANAIS) {
            double horasExtras = getHorasTrabalhadas() - LIMITE_HORAS_SEMANAIS;
            adicional = horasExtras * getValorHora();
        }
        return bolsaAuxilio + valeTransporte + adicional;
    }

    @Override
    public void imprimirInformacao() {
        double horasExtras = 0;
        double adicional   = 0;
        if (getHorasTrabalhadas() > LIMITE_HORAS_SEMANAIS) {
            horasExtras = getHorasTrabalhadas() - LIMITE_HORAS_SEMANAIS;
            adicional   = horasExtras * getValorHora();
        }

        System.out.println("========================================");
        System.out.println(" ESTAGIÁRIO");
        System.out.println("========================================");
        System.out.println(" Nome              : " + getNome());
        System.out.println(" Horas trabalhadas : " + getHorasTrabalhadas() + "h");
        System.out.println(" Limite semanal    : " + LIMITE_HORAS_SEMANAIS + "h");
        System.out.println(" Horas excedentes  : " + horasExtras + "h");
        System.out.println(" Bolsa-auxílio     : R$ " + String.format("%.2f", bolsaAuxilio));
        System.out.println(" Vale-transporte   : R$ " + String.format("%.2f", valeTransporte));
        System.out.println(" Adicional horas   : R$ " + String.format("%.2f", adicional));
        System.out.println(" Salário final     : R$ " + String.format("%.2f", calcularSalario()));
        System.out.println("========================================");
    }
}
