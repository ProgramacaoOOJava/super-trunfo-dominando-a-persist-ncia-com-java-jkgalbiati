import java.util.List;
import java.util.Scanner;

public class SuperTrunfoAventureiro {
    public static void main(String[] args) {
        AlunoDAO dao = new AlunoDAO();
        Scanner scanner = new Scanner(System.in);
        int operacoesValidas = 0;
        int opcao = 0;

        System.out.println("✨🃏 BEM-VINDO AO SUPER TRUNFO AVENTUREIRO (JPA) 🃏✨");
        System.out.println("🎯 Objetivo: Conclua 5 operações com sucesso para fechar o baralho!");

        while (operacoesValidas < 5 && opcao != 6) {
            System.out.println("\n-------------------------------------------");
            System.out.printf("📊 Progresso Atual: [%d/5] Operações Válidas Concluídas\n", operacoesValidas);
            System.out.println("-------------------------------------------");
            System.out.println("1. 📥 Inserir aluno");
            System.out.println("2. ❌ Remover aluno");
            System.out.println("3. 📝 Alterar dados de aluno");
            System.out.println("4. 📋 Listar todos os alunos");
            System.out.println("5. 🔍 Obter aluno por matrícula");
            System.out.println("6. 🚪 Sair do jogo");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        System.out.print("Matrícula: ");
                        String mat = scanner.nextLine();
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Ano de entrada: ");
                        int ano = Integer.parseInt(scanner.nextLine());
                        
                        dao.incluir(new Aluno(mat, nome, ano));
                        System.out.println("✅ Card inserido com sucesso via JPA!");
                        operacoesValidas++;
                        break;

                    case 2:
                        System.out.print("Digite a matrícula para remover: ");
                        String matRemover = scanner.nextLine();
                        if (dao.obter(matRemover) != null) {
                            dao.excluir(matRemover);
                            System.out.println("🔥 Card removido com sucesso da base!");
                            operacoesValidas++;
                        } else {
                            System.out.println("⚠️ Aluno não encontrado.");
                        }
                        break;

                    case 3:
                        System.out.print("Digite a matrícula do aluno para alterar: ");
                        String matAlterar = scanner.nextLine();
                        Aluno existente = dao.obter(matAlterar);
                        if (existente != null) {
                            System.out.print("Novo Nome: ");
                            existente.setNome(scanner.nextLine());
                            System.out.print("Novo Ano de Entrada: ");
                            existente.setAno(Integer.parseInt(scanner.nextLine()));
                            
                            dao.alterar(existente);
                            System.out.println("🔄 Card atualizado com sucesso!");
                            operacoesValidas++;
                        } else {
                            System.out.println("⚠️ Aluno não encontrado para alteração.");
                        }
                        break;

                    case 4:
                        List<Aluno> lista = dao.obterTodos();
                        System.out.println("\n=== 🃏 SEU BARALHO ATUAL 🃏 ===");
                        for (Aluno a : lista) {
                            a.exibirCartaVisual();
                        }
                        if (lista.isEmpty()) System.out.println("Baralho vazio.");
                        break;

                    case 5:
                        System.out.print("Digite a matrícula: ");
                        String matBuscar = scanner.nextLine();
                        Aluno buscado = dao.obter(matBuscar);
                        if (buscado != null) {
                            buscado.exibirCartaVisual();
                        } else {
                            System.out.println("⚠️ Carta não encontrada!");
                        }
                        break;

                    case 6:
                        System.out.println("👋 Saindo da partida...");
                        break;

                    default:
                        System.out.println("❌ Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("❌ Ocorreu um erro na operação: " + e.getMessage());
            }
        }

        // Finalização Automática por Pontuação
        if (operacoesValidas >= 5) {
            System.out.println("\n🎉🏆 PARABÉNS! VOCÊ ALCANÇOU O SCORE MÁXIMO DE OPERAÇÕES VÁLIDAS! 🏆🎉");
            System.out.println("📊 Pontuação Final: " + operacoesValidas + " pontos.");
            
            System.out.println("\n📋 EXIBINDO SEU BARALHO FINAL CONSOLIDADO:");
            List<Aluno> finalLista = dao.obterTodos();
            for (Aluno a : finalLista) {
                a.exibirCartaVisual();
            }
            System.out.println("\n🎮 Partida encerrada de forma automatizada. Obrigado por jogar!");
        }

        scanner.close();
        AlunoDAO.fecharFabrica(); // Libera os recursos
    }
}