package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "aluno")
@NamedQueries({
    @NamedQuery(name = "Aluno.buscarTodos", query = "SELECT a FROM Aluno a")
})
public class Aluno {

    @Id
    @Column(length = 20)
    private String matricula;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "entrada_ano")
    private int ano;

    public Aluno() {}

    public Aluno(String matricula, String nome, int ano) {
        this.matricula = matricula;
        this.nome = nome;
        this.ano = ano;
    }

    public String getRaridade() {
        if (ano == 2030) return "👑 Lendária (Recompensa)";
        if (matricula != null && !matricula.isEmpty()) {
            char primeiraLetra = Character.toUpperCase(matricula.charAt(0));
            if (primeiraLetra >= 'A' && primeiraLetra <= 'M') return "Comum";
            if (primeiraLetra >= 'N' && primeiraLetra <= 'Z') return "Rara";
        }
        return "Comum";
    }

    public void exibirCartaVisual() {
        String borda = (ano == 2030) ? "✨=================================✨" : "🃏 .---------------------------------. 🃏";
        System.out.println(borda);
        System.out.printf("   | Nome: %-25s |\n", nome);
        System.out.printf("   | Matrícula: %-20s |\n", matricula);
        System.out.printf("   | Entrada: %-21d |\n", ano);
        System.out.printf("   | Raridade: %-20s |\n", getRaridade());
        System.out.println((ano == 2030) ? "✨=================================✨" : "   '---------------------------------' ");
    }

    // Getters e Setters
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
}