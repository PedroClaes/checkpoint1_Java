package br.com.fiap.model;

import br.com.fiap.annotations.Coluna;
import br.com.fiap.annotations.Descricao;
import jakarta.persistence.*;

@Descricao(descricao = "TB_FUNCIONARIO")
@Entity
@Table(name = "TB_FUNCIONARIO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TP_FUNCIONARIO", discriminatorType = DiscriminatorType.STRING)
public class Funcionario {

    // ── Chave primária gerada por sequence no Oracle ──────────────────────────
    @Coluna(nome = "ID_FUNCIONARIO", tipo = "NUMBER(10)", obrigatorio = true, primaryKey = true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_func")
    @SequenceGenerator(name = "seq_func", sequenceName = "SEQ_FUNCIONARIO", allocationSize = 1)
    @Column(name = "ID_FUNCIONARIO")
    private Long id;

    @Coluna(nome = "NM_FUNCIONARIO", tipo = "VARCHAR2(100)", obrigatorio = true)
    @Column(name = "NM_FUNCIONARIO", nullable = false, length = 100)
    private String nome;

    @Coluna(nome = "NR_HORAS_TRABALHADAS", tipo = "NUMBER(10,2)", obrigatorio = true)
    @Column(name = "NR_HORAS_TRABALHADAS", nullable = false)
    private double horasTrabalhadas;

    @Coluna(nome = "VL_HORA", tipo = "NUMBER(10,2)", obrigatorio = true)
    @Column(name = "VL_HORA", nullable = false)
    private double valorHora;

    // Construtores

    public Funcionario() {}

    public Funcionario(String nome, double horasTrabalhadas, double valorHora) {
        this.nome             = nome;
        this.horasTrabalhadas = horasTrabalhadas;
        this.valorHora        = valorHora;
    }

    // Getters e Setters

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getNome()                   { return nome; }
    public void setNome(String nome)          { this.nome = nome; }

    public double getHorasTrabalhadas()       { return horasTrabalhadas; }
    public void setHorasTrabalhadas(double h) { this.horasTrabalhadas = h; }

    public double getValorHora()              { return valorHora; }
    public void setValorHora(double v)        { this.valorHora = v; }

    // Métodos de negócio

    public double calcularSalario() {
        return horasTrabalhadas * valorHora;
    }

    public void imprimirInformacao() {
        System.out.println("========================================");
        System.out.println(" FUNCIONÁRIO");
        System.out.println("========================================");
        System.out.println(" Nome              : " + nome);
        System.out.println(" Horas trabalhadas : " + horasTrabalhadas + "h");
        System.out.println(" Valor por hora    : R$ " + String.format("%.2f", valorHora));
        System.out.println(" Salário final     : R$ " + String.format("%.2f", calcularSalario()));
        System.out.println("========================================");
    }

    @Override
    public String toString() {
        return "Funcionario{id=" + id
                + ", nome='" + nome + "'"
                + ", salario=R$ " + String.format("%.2f", calcularSalario())
                + "}";
    }
}
