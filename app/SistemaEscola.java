package app;

import manager.AlunoJpaController;
import model.Aluno;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SistemaEscola {
    public static void main(String[] args) {
        AlunoJpaController controller = new AlunoJpaController();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Random random = new Random();
        int opcao = 0;

        System.out.println("👑🏆 BEM-VINDO AO SUPER TRUNFO ACADÊMICO - NÍVEL MESTRE 🏆👑");

        while (opcao != 6) {
            System.out.println("\n=================================");
            System.out.println("1. 📥 Inserir Aluno (Carta)");
            System.out.println("2. 📋 Listar Alunos");
            System.out.println("3. ❌ Excluir Aluno");
            System.out.println("4. ⚔️ Jogar Mini-Torneio");
            System.out.println("6. 🚪 Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            try {
                String entradaOpcao = reader.readLine();
                if (entradaOpcao == null) break;
                opcao = Integer.parseInt(entradaOpcao.trim());

                switch (opcao) {
                    case 1:
                        System.out.print("Matrícula: ");
                        String mat = reader.readLine();
                        System.out.print("Nome: ");
                        String nome = reader.readLine();
                        System.out.print("Ano de Entrada: ");
                        int ano = Integer.parseInt(reader.readLine().trim());

                        controller.create(new Aluno(mat, nome, ano));
                        System.out.println("✅ Carta persistida com sucesso via JPA Avançado!");
                        break;

                    case 2:
                        List<Aluno> lista = controller.findAlunoEntities();
                        System.out.println("\n=== 🃏 SEU BARALHO TOTAL 🃏 ===");
                        for (Aluno a : lista) {
                            a.exibirCartaVisual();
                        }
                        if (lista.isEmpty()) System.out.println("Nenhuma carta no deck.");
                        break;

                    case 3:
                        System.out.print("Digite a matrícula para deletar: ");
                        String matDel = reader.readLine();
                        controller.destroy(matDel);
                        System.out.println("🔥 Carta desintegrada com sucesso!");
                        break;

                    case 4:
                        List<Aluno> deck = controller.findAlunoEntities();
                        if (deck.size() < 3) {
                            System.out.println("⚠️ Você precisa de pelo menos 3 cartas salvas para iniciar um Torneio!");
                            break;
                        }
                        
                        // Sorteia 3 cartas para a mão do jogador
                        Collections.shuffle(deck);
                        System.out.println("\n🎲 Sorteando 3 cartas para a sua Mão...");
                        for (int i = 0; i < 3; i++) {
                            System.out.printf("[%d] ", i + 1);
                            System.out.println(deck.get(i).getNome() + " (Entrada: " + deck.get(i).getAno() + ")");
                        }

                        System.out.print("Escolha a carta que deseja usar (1, 2 ou 3): ");
                        int escolhaCard = Integer.parseInt(reader.readLine().trim()) - 1;
                        Aluno cartaJogador = deck.get(escolhaCard);

                        System.out.println("\nAtributos Disponíveis:");
                        System.out.println("1. Nome (Critério alfabético)");
                        System.out.println("2. Matrícula (Tamanho do texto)");
                        System.out.println("3. Entrada (Ano mais recente vence!)");
                        System.out.print("Escolha o atributo para apostar: ");
                        int atributo = Integer.parseInt(reader.readLine().trim());

                        // Sorteia carta aleatória do oponente (que sobrou no deck original)
                        Aluno cartaOponente = deck.get(3 + random.nextInt(deck.size() - 3));

                        System.out.println("\n⚔️ CONFRONTO DIRETO DE CARDS ⚔️");
                        System.out.println("SUA CARTA:");
                        cartaJogador.exibirCartaVisual();
                        System.out.println("CARTA DO OPONENTE:");
                        cartaOponente.exibirCartaVisual();

                        boolean jogadorVenceu = false;

                        if (atributo == 1) {
                            jogadorVenceu = cartaJogador.getNome().compareToIgnoreCase(cartaOponente.getNome()) > 0;
                        } else if (atributo == 2) {
                            jogadorVenceu = cartaJogador.getMatricula().length() > cartaOponente.getMatricula().length();
                        } else {
                            jogadorVenceu = cartaJogador.getAno() > cartaOponente.getAno();
                        }

                        if (jogadorVenceu) {
                            System.out.println("🎉 VITÓRIA! Sua carta superou a banca!");
                            String lendariaMat = "LEND-" + random.nextInt(9000);
                            Aluno recompensa = new Aluno(lendariaMat, "Mestre " + cartaJogador.getNome(), 2030);
                            controller.create(recompensa);
                            System.out.println("👑 RECOMPENSA CONCEDIDA: Uma Nova Carta Lendária de Ano 2030 foi forjada e salva!");
                        } else {
                            System.out.println("😢 DERROTA! A banca levou a melhor dessa vez.");
                        }
                        break;

                    case 6:
                        System.out.println("👋 Fechando a aplicação do Nível Mestre...");
                        break;

                    default:
                        System.out.println("❌ Opção Inválida!");
                }
            } catch (Exception e) {
                System.out.println("❌ Erro operacional: " + e.getMessage());
            }
        }

        try { reader.close(); } catch (Exception e) {}
        AlunoJpaController.fecharFabrica();
    }
}