package com.alura.desafioliteralura.principal;

import com.alura.desafioliteralura.model.Autores;
import com.alura.desafioliteralura.model.DadosLivros;
import com.alura.desafioliteralura.model.Livros;
import com.alura.desafioliteralura.model.ResultadosDados;
import com.alura.desafioliteralura.repositorio.LivrosRepositorio;
import com.alura.desafioliteralura.service.ConsumoApi;
import com.alura.desafioliteralura.service.ConverteDados;

import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private final LivrosRepositorio repositorio;

    public Principal(LivrosRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n############################################
                                        
                    1 - Buscar Livro
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em determinado ano
                    5 - Listar livros em determinado idioma
                                                                         
                    0 - SaiR                
                                       
                    ############################################
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroWeb();
                    break;
                case 2:
                    listarLivroRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosDeterminadoAno();
                    break;
                case 5:
                    listarLivroPorIdioma();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void listarLivroRegistrados() {
        List<Livros> livros = repositorio.findAll();
        System.out.println("- Lista de livros registrados.");
        if (livros.size() > 0) {
            livros.forEach(livro -> System.out.println(
                            "\nTítulo: " + livro.getTitle() +
                            "\nAutor: " + livro.getAutores() +
                            "\nIdioma: " + livro.getLanguages()[0] +
                            "\n" +
                            "\n************************************* \n"
            ));
        } else {
            System.out.println("*************     Nenhum livro registrado!     *************");
        }
    }

    private void listarLivroPorIdioma() {
        System.out.println(
                "\n############################################\n" +
                        "Escolha uma das opções: \n" +
                        "Es - Espanhol\n" +
                        "En - Inglês\n" +
                        "Fr - Francês\n" +
                        "Pt - Português\n");

        var idiomaget = leitura.nextLine();
        String[] idioma = {idiomaget};
        List<Livros> livros1 = repositorio.findLivrosPorIdioma(idioma);
        livros1.stream().forEach(livro -> System.out.println(
                "************************************* \n" +
                        "Titulo: " + livro.getTitle() +
                        "\nAutor: " + livro.getPrimeiroAutor() +
                        "\nIdioma: " + livro.getLanguages()[0] +
                        "\n************************************* \n"));
    }

    private void listarAutoresVivosDeterminadoAno() {
        System.out.println("Digite o ano para verificar os autores vivos nessa data: ");
        var ano = leitura.nextInt();
        leitura.nextLine();
        List<Autores> autoresVivos = repositorio.findAutoresVivosDeterminadoAno(ano);
        System.out.println("Autores vivos no ano de: " + ano);
        autoresVivos.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autores> listaAutores = repositorio.findAllAutores();
        listaAutores.forEach(System.out::println);
    }

    private void buscarLivroWeb() {
        Livros livro = getDadosLivros();
        if (livro != null) {
            System.out.println(
                    "*************************************" +
                    "\n" +
                    "\nTítulo: " + livro.getTitle() +
                    "\nAutor: " + livro.getAutores() +
                    "\nIdioma: " + livro.getLanguages()[0] +
                    "\nRegistrado no banco de dados!" +
                    "\n" +
                    "\n************************************* \n");
        } else {
            System.out.println("*************     Livro não encontrado!     *************");
        }
    }

    private Livros getDadosLivros() {
        System.out.println(" - Digite o nome do livro para busca: ");
        var nomeLivros = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeLivros.toLowerCase().replace(" ", "%20"));
        System.out.println("");
        ResultadosDados dados = conversor.obterDados(json, ResultadosDados.class);
        List<DadosLivros> livros = dados.results().stream().toList();
        for (DadosLivros dadosLivro : livros)
            if (dadosLivro.titulo().toLowerCase()
                    .contains(nomeLivros.toLowerCase())) {
                Livros livro = new Livros(dadosLivro);
                repositorio.save(livro);
                return livro;
            } else {
                System.out.println("*************     Livro não encontrado!     *************");
            }

        return null;
    }
}


