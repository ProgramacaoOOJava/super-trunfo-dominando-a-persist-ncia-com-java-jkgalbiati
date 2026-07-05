import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SuperTrunfoJDBC {
    // URL de conexão para o banco Apache Derby em modo embutido (embedded)
    private static final String URL = "jdbc:derby:escola;create=true";

    public static void main(String[] args) {
        inicializarBanco();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        System.out.println("=== BEM-VINDO AO SUPER TRUNFO: PERSISTÊNCIA JBDC ===");

        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Cadastrar Aluno (Carta)");
            System.out.println("2. Listar todas as Cartas");
            System.out.println("3. Excluir Aluno (Carta)");
            System.out.println("4. Iniciar Batalha entre Cartas");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        cadastrarAluno(scanner);
                        break;
                    case 2:
                        listarAlunos();
                        break;
                    case 3:
                        excluirAluno(scanner);
                        break;
                    case 4:
                        batalhar(scanner);
                        break;
                    case 5:
                        System.out.println("Saindo do jogo... Até a próxima!");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        } while (opcao != 5);

        scanner.close();
    }

    // Cria a tabela caso ela não exista no Derby
    private static void inicializarBanco() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            
            // Verifica se a tabela já existe antes de criar
            DatabaseMetaData dbmd = conn.getMetaData();
            try (ResultSet rs = dbmd.getTables(null, null, "ALUNO", null)) {
                if (!rs.next()) {
                    String sql = "CREATE TABLE aluno (" +
                                 "matricula VARCHAR(20) PRIMARY KEY, " +
                                 "nome VARCHAR(100), " +
                                 "entrada INT)";
                    stmt.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    // Operação CREATE usando PreparedStatement
    private static void cadastrarAluno(Scanner scanner) {
        System.out.print("Digite a matrícula (ex: A2026001): ");
        String matricula = scanner.nextLine();
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o ano de entrada: ");
        int entrada = Integer.parseInt(scanner.nextLine());

        String sql = "INSERT INTO aluno (matricula, nome, entrada) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareCall(sql)) {
            
            pstmt.setString(1, matricula);
            pstmt.setString(2, nome);
            pstmt.setInt(3, entrada);
            pstmt.executeUpdate();
            
            System.out.println("Carta cadastrada com sucesso no banco!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar carta: " + e.getMessage());
        }
    }

    // Operação READ usando Statement e ResultSet
    private static List<Aluno> listarAlunos() {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT * FROM aluno";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- CARTAS CADASTRADAS ---");
            while (rs.next()) {
                Aluno aluno = new Aluno(
                    rs.getString("matricula"),
                    rs.getString("nome"),
                    rs.getInt("entrada")
                );
                aluno.exibirCartaASCII();
                lista.add(aluno);
            }
            if (lista.isEmpty()) {
                System.out.println("Nenhuma carta cadastrada ainda.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar registros: " + e.getMessage());
        }
        return lista;
    }

    // Operação DELETE usando PreparedStatement
    private static void excluirAluno(Scanner scanner) {
        System.out.print("Digite a matrícula da carta que deseja excluir: ");
        String matricula = scanner.nextLine();

        String sql = "DELETE FROM aluno WHERE matricula = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, matricula);
            int linhasAfetadas = pstmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("Carta removida com sucesso!");
            } else {
                System.out.println("Nenhuma carta encontrada com essa matrícula.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir carta: " + e.getMessage());
        }
    }

    // Lógica da Batalha de Super Trunfo
    private static void batalhar(Scanner scanner) {
        List<Aluno> cartas = listarAlunos();
        if (cartas.size() < 2) {
            System.out.println("É necessário ter pelo menos 2 cartas cadastradas para batalhar!");
            return;
        }

        System.out.print("\nDigite a matrícula da 1ª Carta: ");
        String mat1 = scanner.nextLine();
        System.out.print("Digite a matrícula da 2ª Carta: ");
        String mat2 = scanner.nextLine();

        Aluno carta1 = buscarPorMatricula(mat1, cartas);
        Aluno carta2 = buscarPorMatricula(mat2, cartas);

        if (carta1 == null || carta2 == null) {
            System.out.println("Uma ou ambas as matrículas não foram encontradas na lista ativa.");
            return;
        }

        System.out.println("\n--- CONFRONTO ---");
        System.out.println(carta1.getNome() + " (Força: " + carta1.getEntrada() + ") VS " + carta2.getNome() + " (Força: " + carta2.getEntrada() + ")");
        
        if (carta1.getEntrada() > carta2.getEntrada()) {
            System.out.println("🏆 VENCEDOR: " + carta1.getNome() + "!");
        } else if (carta2.getEntrada() > carta1.getEntrada()) {
            System.out.println("🏆 VENCEDOR: " + carta2.getNome() + "!");
        } else {
            System.out.println("🤝 EMPATE! Ambas as cartas possuem o mesmo ano de entrada.");
        }
    }

    private static Aluno buscarPorMatricula(String matricula, List<Aluno> cartas) {
        for (Aluno a : cartas) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                return a;
            }
        }
        return null;
    }
}