package br.com.fiap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada que mapeia um atributo a uma coluna do banco de dados.
 *
 * Parâmetros:
 *   nome        → nome da coluna no BD          ex: "NM_FUNCIONARIO"
 *   tipo        → tipo SQL da coluna            ex: "VARCHAR2(100)"
 *   obrigatorio → gera NOT NULL na DDL          padrão: false
 *   primaryKey  → indica chave primária         padrão: false
 *
 * Uso: @Coluna(nome = "NM_FUNCIONARIO", tipo = "VARCHAR2(100)", obrigatorio = true)
 */
@Target(ElementType.FIELD)          // só pode ser aplicada em atributos
@Retention(RetentionPolicy.RUNTIME) // disponível em tempo de execução para a API Reflection
public @interface Coluna {
    String  nome();
    String  tipo()        default "VARCHAR2(255)";
    boolean obrigatorio() default false;
    boolean primaryKey()  default false;
}
