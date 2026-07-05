import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @Column(length = 20)
    private String matricula; // Chave primária

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "entrada_ano")
    private int ano;

    // O JPA exige um construtor padrão vazio
    public Aluno() {}

    public Aluno(String matricula, String nome, int ano) {
        this.matricula = matricula;
        this.nome = nome;
        this.ano = ano;
    }

    // Regra de raridade baseada na primeira letra
    public String getRaridade() {
        if (matricula != null && !matricula.isEmpty()) {
            char primeiraLetra = Character.toUpperCase(matricula.charAt(0));
            if (primeiraLetra >= 'A' && primeiraLetra <= 'M') return "Comum";
            if (primeiraLetra >= 'N' && primeiraLetra <= 'Z') return "Rara";
        }
        return "Desconhecida";
    }

    public void exibirCartaVisual() {
        System.out.println("🃏 .---------------------------------. 🃏");
        System.out.printf("   | Nome: %-25s |\n", nome);
        System.out.printf("   | Matrícula: %-20s |\n", matricula);
        System.out.printf("   | Força (Ano): %-17d |\n", ano);
        System.out.printf("   | Raridade: %-21s |\n", getRaridade());
        System.out.println("   '---------------------------------' ");
    }

    // Getters e Setters
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
}