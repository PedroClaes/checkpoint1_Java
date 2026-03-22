package br.com.fiap.repository;

import br.com.fiap.annotations.Coluna;
import br.com.fiap.annotations.Descricao;
import br.com.fiap.model.Funcionario;
import jakarta.persistence.EntityManager;

import java.lang.reflect.Field;
import java.util.List;

public class FuncionarioRepository {

    private final EntityManager em;

    public FuncionarioRepository(EntityManager em) {
        this.em = em;
    }

    // CREATE

    public void salvar(Funcionario funcionario) {
        System.out.println("\n[SQL - INSERT]");
        System.out.println(gerarSqlInsert(funcionario));

        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();

        System.out.println(">> Registro inserido com ID: " + funcionario.getId());
    }


    // READ

    public Funcionario buscarPorId(Long id) {
        System.out.println("\n[SQL - SELECT por ID]");
        System.out.println(gerarSqlSelectPorId(Funcionario.class, id));

        Funcionario f = em.find(Funcionario.class, id);
        if (f == null) {
            System.out.println(">> Nenhum funcionário encontrado com ID: " + id);
        }
        return f;
    }

    public List<Funcionario> listarTodos() {
        System.out.println("\n[SQL - SELECT TODOS]");
        System.out.println(gerarSqlSelectAll(Funcionario.class));

        List<Funcionario> lista = em
                .createQuery("SELECT f FROM Funcionario f", Funcionario.class)
                .getResultList();

        System.out.println(">> Total encontrado: " + lista.size() + " funcionário(s).");
        return lista;
    }


    // UPDATE

    public void atualizar(Funcionario funcionario) {
        System.out.println("\n[SQL - UPDATE]");
        System.out.println(gerarSqlUpdate(funcionario));

        em.getTransaction().begin();
        em.merge(funcionario);
        em.getTransaction().commit();

        System.out.println(">> Registro atualizado: " + funcionario.getNome());
    }

    // DELETE
    public void deletar(Long id) {
        System.out.println("\n[SQL - DELETE]");
        System.out.println(gerarSqlDelete(Funcionario.class, id));

        em.getTransaction().begin();
        Funcionario f = em.find(Funcionario.class, id);
        if (f != null) {
            em.remove(f);
            System.out.println(">> Registro removido: " + f.getNome());
        } else {
            System.out.println(">> Nenhum funcionário com ID " + id + " para remover.");
        }
        em.getTransaction().commit();
    }

    // GERAÇÃO DE SQL

    public String gerarSqlSelectAll(Class<?> classe) {
        return "SELECT * FROM " + obterNomeTabela(classe) + ";";
    }

    public String gerarSqlSelectPorId(Class<?> classe, Object id) {
        return "SELECT * FROM " + obterNomeTabela(classe)
                + " WHERE " + obterNomePk(classe) + " = " + id + ";";
    }

    public String gerarSqlInsert(Object objeto) {
        Class<?> superClasse = obterSuperclasseComDescricao(objeto.getClass());
        String nomeTabela    = obterNomeTabela(superClasse);

        StringBuilder colunas = new StringBuilder();
        StringBuilder valores  = new StringBuilder();

        adicionarCamposInsert(objeto, superClasse.getDeclaredFields(), colunas, valores);

        if (!objeto.getClass().equals(superClasse)) {
            adicionarCamposInsert(objeto, objeto.getClass().getDeclaredFields(), colunas, valores);
        }

        return "INSERT INTO " + nomeTabela
                + " (" + colunas + ")"
                + " VALUES (" + valores + ");";
    }

    public String gerarSqlUpdate(Object objeto) {
        Class<?> superClasse = obterSuperclasseComDescricao(objeto.getClass());
        String nomeTabela    = obterNomeTabela(superClasse);
        String pk            = obterNomePk(superClasse);
        Object pkValor       = obterValorPk(objeto, superClasse);

        StringBuilder set = new StringBuilder();
        adicionarCamposUpdate(objeto, superClasse.getDeclaredFields(), set);
        if (!objeto.getClass().equals(superClasse)) {
            adicionarCamposUpdate(objeto, objeto.getClass().getDeclaredFields(), set);
        }

        return "UPDATE " + nomeTabela
                + " SET " + set
                + " WHERE " + pk + " = " + pkValor + ";";
    }

    public String gerarSqlDelete(Class<?> classe, Object id) {
        Class<?> superClasse = obterSuperclasseComDescricao(classe);
        return "DELETE FROM " + obterNomeTabela(superClasse)
                + " WHERE " + obterNomePk(superClasse) + " = " + id + ";";
    }

    // Helpers internos

    private Class<?> obterSuperclasseComDescricao(Class<?> classe) {
        Class<?> atual = classe;
        while (atual != null) {
            if (atual.isAnnotationPresent(Descricao.class)) return atual;
            atual = atual.getSuperclass();
        }
        return classe;
    }

    private String obterNomeTabela(Class<?> classe) {
        if (classe.isAnnotationPresent(Descricao.class)) {
            return classe.getAnnotation(Descricao.class).descricao();
        }
        return classe.getSimpleName().toUpperCase();
    }

    private String obterNomePk(Class<?> classe) {
        for (Field f : classe.getDeclaredFields()) {
            if (f.isAnnotationPresent(Coluna.class) && f.getAnnotation(Coluna.class).primaryKey()) {
                return f.getAnnotation(Coluna.class).nome();
            }
        }
        return "ID";
    }

    private Object obterValorPk(Object objeto, Class<?> classe) {
        for (Field f : classe.getDeclaredFields()) {
            if (f.isAnnotationPresent(Coluna.class) && f.getAnnotation(Coluna.class).primaryKey()) {
                f.setAccessible(true);
                try { return f.get(objeto); } catch (IllegalAccessException e) { e.printStackTrace(); }
            }
        }
        return null;
    }

    private void adicionarCamposInsert(Object objeto, Field[] campos,
                                       StringBuilder colunas, StringBuilder valores) {
        for (Field field : campos) {
            if (!field.isAnnotationPresent(Coluna.class)) continue;
            Coluna coluna = field.getAnnotation(Coluna.class);
            if (coluna.primaryKey()) continue;

            field.setAccessible(true);
            try {
                Object valor = field.get(objeto);
                if (colunas.length() > 0) { colunas.append(", "); valores.append(", "); }
                colunas.append(coluna.nome());
                valores.append(valor instanceof String ? "'" + valor + "'" : valor);
            } catch (IllegalAccessException e) { e.printStackTrace(); }
        }
    }

    private void adicionarCamposUpdate(Object objeto, Field[] campos, StringBuilder set) {
        for (Field field : campos) {
            if (!field.isAnnotationPresent(Coluna.class)) continue;
            Coluna coluna = field.getAnnotation(Coluna.class);
            if (coluna.primaryKey()) continue;

            field.setAccessible(true);
            try {
                Object valor = field.get(objeto);
                if (set.length() > 0) set.append(", ");
                set.append(coluna.nome()).append(" = ");
                set.append(valor instanceof String ? "'" + valor + "'" : valor);
            } catch (IllegalAccessException e) { e.printStackTrace(); }
        }
    }
}
