public class Aluno {
    private String matricula;
    private String nome;
    private int entrada;

    public Aluno(String matricula, String nome, int entrada) {
        this.matricula = matricula;
        this.nome = nome;
        this.entrada = entrada;
    }

    // Método para calcular raridade: A-M -> Comum, N-Z -> Rara
    public String getRaridade() {
        if (matricula != null && !matricula.isEmpty()) {
            char primeiraLetra = Character.toUpperCase(matricula.charAt(0));
            if (primeiraLetra >= 'A' && primeiraLetra <= 'M') {
                return "Comum";
            } else if (primeiraLetra >= 'N' && primeiraLetra <= 'Z') {
                return "Rara";
            }
        }
        return "Desconhecida";
    }

    // Exibe a carta em formato ASCII decorado
    public void exibirCartaASCII() {
        System.out.println(".---------------------------------.");
        System.out.println("|           SUPER TRUNFO          |");
        System.out.println("|---------------------------------|");
        System.out.printf("| Nome: %-25s |\n", nome);
        System.out.printf("| Matrícula: %-20s |\n", matricula);
        System.out.printf("| Força (Ano Entrada): %-10d |\n", entrada);
        System.out.printf("| Raridade: %-21s |\n", getRaridade());
        System.out.println("'---------------------------------'");
    }

    // Getters e Setters
    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public int getEntrada() { return entrada; }
}