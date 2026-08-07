// Arquivo: programa_media.c
// Programa para calcular a média de 5 notas de um aluno usando uma lista
// e demonstrando todas as funções de list.h

#include <stdio.h>
#include "list.h"

// Função auxiliar para imprimir a lista (não faz parte de list.h)
void print_list(List* list) {
    if (empty(list)) {
        printf("Lista: [] (vazia)\n");
        return;
    }
    printf("Lista: [");
    int value;
    for (int i = 0; ; i++) {
        if(get_position(list, i, &value)) {
            printf("%d", value);
            // Verifica se é o último elemento para não imprimir a vírgula
            if(!get_position(list, i + 1, &value)) {
                break;
            }
            printf(", ");
        } else {
            break;
        }
    }
    printf("]\n");
}


int main() {
    printf("--- DEMONSTRACAO DAS FUNCOES DA LISTA ---\n");

    // 1. create_list() e empty()
    List* notas = create_list();
    printf("Lista criada. A lista esta vazia? %s\n", empty(notas) ? "Sim" : "Nao");
    
    // 2. Funções de Inserção
    printf("\n--- Fase 1: Inserindo as 5 notas ---\n");
    insert_head(notas, 10);      // Insere a nota 10 no início
    printf("insert_head(10): "); print_list(notas);
    
    insert_tail(notas, 7);       // Insere a nota 7 no final
    printf("insert_tail(7):  "); print_list(notas);
    
    insert_head(notas, 8);       // Insere a nota 8 no início
    printf("insert_head(8):  "); print_list(notas);
    
    insert_tail(notas, 9);       // Insere a nota 9 no final
    printf("insert_tail(9):  "); print_list(notas);
    
    insert_position(notas, 2, 6); // Insere a nota 6 na posição 2 (0-indexed)
    printf("insert_position(2, 6): "); print_list(notas);

    printf("A lista esta vazia? %s\n", empty(notas) ? "Sim" : "Nao");

    // 3. Funções de Obtenção (Get)
    printf("\n--- Fase 2: Inspecionando as notas ---\n");
    int valor;
    get_head(notas, &valor);
    printf("get_head(): A primeira nota e %d\n", valor);
    
    get_tail(notas, &valor);
    printf("get_tail(): A ultima nota e %d\n", valor);

    get_position(notas, 3, &valor);
    printf("get_position(3): A nota na posicao 3 e %d\n", valor);

    // 4. Lógica Principal: Cálculo da Média
    printf("\n--- Fase 3: Calculando a media do aluno ---\n");
    float soma = 0;
    int nota_atual;
    for (int i = 0; i < 5; i++) {
        get_position(notas, i, &nota_atual);
        soma += nota_atual;
    }
    float media = soma / 5.0;
    printf("A media das notas ");
    print_list(notas);
    printf("e: %.2f\n", media);

    // 5. Funções de Remoção
    printf("\n--- Fase 4: Demonstrando a remocao de notas ---\n");
    int nota_removida;
    remove_head(notas, &nota_removida);
    printf("remove_head(): removeu a nota %d. ", nota_removida);
    print_list(notas);

    remove_tail(notas, &nota_removida);
    printf("remove_tail(): removeu a nota %d. ", nota_removida);
    print_list(notas);

    remove_position(notas, 1, &nota_removida);
    printf("remove_position(1): removeu a nota %d. ", nota_removida);
    print_list(notas);

    // 6. free_list()
    printf("\n--- Fase 5: Liberando a memoria da lista ---\n");
    free_list(notas);
    printf("Memoria da lista liberada com sucesso.\n");

    return 0;
}