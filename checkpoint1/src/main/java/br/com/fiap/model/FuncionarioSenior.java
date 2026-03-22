package br.com.fiap.model;

import br.com.fiap.annotations.Coluna;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SENIOR")
public class FuncionarioSenior extends Funcionario {

    @Coluna(nome = "VL_BONUS_SENIOR", tipo = "NUMBER(10,2)")
    @Column(name = "VL_BONUS_SENIOR")
    private double bonusPor15Horas;

    // Construtores

    public FuncionarioSenior() {}

    public FuncionarioSenior(String nome, double horasTrabalhadas,
                             double valorHora, double bonusPor15Horas) {
        super(nome, horasTrabalhadas, valorHora);
        this.bonusPor15Horas = bonusPor15Horas;
    }

    // Getters e Setters
    public double getBonusPor15Horas()           { return bonusPor15Horas; }
    public void setBonusPor15Horas(double bonus) { this.bonusPor15Horas = bonus; }

    // Métodos sobrescritos


    @Override
    public double calcularSalario() {
        int    gruposDe15 = (int) (getHorasTrabalhadas() / 15);
        double totalBonus = gruposDe15 * bonusPor15Horas;
        return super.calcularSalario() + totalBonus;
    }

    @Override
    public void imprimirInformacao() {
        int    gruposDe15 = (int) (getHorasTrabalhadas() / 15);
        double totalBonus = gruposDe15 * bonusPor15Horas;

        System.out.println("========================================");
        System.out.println(" FUNCIONÁRIO SÊNIOR");
        System.out.println("========================================");
        System.out.println(" Nome              : " + getNome());
        System.out.println(" Horas trabalhadas : " + getHorasTrabalhadas() + "h");
        System.out.println(" Valor por hora    : R$ " + String.format("%.2f", getValorHora()));
        System.out.println(" Bônus por 15h     : R$ " + String.format("%.2f", bonusPor15Horas));
        System.out.println(" Grupos de 15h     : " + gruposDe15);
        System.out.println(" Total bônus       : R$ " + String.format("%.2f", totalBonus));
        System.out.println(" Salário final     : R$ " + String.format("%.2f", calcularSalario()));
        System.out.println("========================================");
    }
}
