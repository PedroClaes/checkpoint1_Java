package br.com.fiap.main;

import br.com.fiap.model.Estagiario;
import br.com.fiap.model.Funcionario;
import br.com.fiap.model.FuncionarioSenior;
import br.com.fiap.repository.FuncionarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("fiap-checkpoint1");
        EntityManager em         = emf.createEntityManager();
        FuncionarioRepository repo = new FuncionarioRepository(em);

        separador("CHECKPOINT 1");

        separador("INFORMAÇÕES DOS FUNCIONÁRIOS (Polimorfismo)");

        Funcionario base    = new Funcionario("Carlos Silva", 160, 25.00);
        FuncionarioSenior s = new FuncionarioSenior("Ana Oliveira", 45, 40.00, 200.00);
        Estagiario est      = new Estagiario("Lucas Ferreira", 22, 12.00, 800.00, 150.00);

        base.imprimirInformacao();
        s.imprimirInformacao();
        est.imprimirInformacao();

        // CREATE: persistir no Oracle
        separador("CREATE — Inserindo no banco de dados");

        repo.salvar(base);
        repo.salvar(s);
        repo.salvar(est);

        // READ: listar todos e buscar por ID
        separador("READ — Listando todos");

        List<Funcionario> todos = repo.listarTodos();
        for (Funcionario f : todos) {
            f.imprimirInformacao();
        }

        separador("READ — Busca por ID (" + base.getId() + ")");

        Funcionario encontrado = repo.buscarPorId(base.getId());
        if (encontrado != null) {
            encontrado.imprimirInformacao();
        }


        // UPDATE: alterar dados do funcionário base
        separador("UPDATE — Atualizando funcionário base");

        base.setNome("Carlos Silva (Atualizado)");
        base.setValorHora(30.00);
        repo.atualizar(base);

        System.out.println(">> Dados após atualização:");
        base.imprimirInformacao();


        // DELETE: remover o estagiário
        separador("DELETE — Removendo estagiário (ID " + est.getId() + ")");

        repo.deletar(est.getId());

        // Listar após remoção para confirmar
        separador("READ — Lista final após DELETE");

        List<Funcionario> final_ = repo.listarTodos();
        for (Funcionario f : final_) {
            System.out.println("  >> " + f);
        }

        // Fechar a conexão
        em.close();
        emf.close();

        separador("CRUD concluído com sucesso!");
    }

    private static void separador(String titulo) {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.printf( "║  %-44s║%n", titulo);
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
