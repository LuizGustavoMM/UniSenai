#include <stdio.h>
#include <stdlib.h>
#include <string.h> // Necessário para a função strcpy
#include "task.h"

// Esta é a definição real da struct. Como está no .c, ela fica "escondida".
struct Task {
    int id;
    char nome[21];       // 20 caracteres + 1 para o terminador nulo que strcpy adiciona
    char descricao[101]; // 100 caracteres + 1 para o terminador nulo
};

// Exatamente a lógica que você sugeriu.
Task* create_task(int id, const char* nome, const char* descricao) {
    Task* task = (Task*) malloc(sizeof(Task));
    if (task) { // Uma forma mais curta de verificar se task != NULL
        task->id = id;
        strcpy(task->nome, nome);           // Copia a string de nome
        strcpy(task->descricao, descricao); // Copia a string de descricao
        return task;
    }
    return NULL;
}

void show_task(Task* task) {
    // Verifica se o ponteiro não é nulo antes de tentar usá-lo
    if (task) {
        printf("Task ID: %d | Nome: %s | Descricao: %s\n", task->id, task->nome, task->descricao);
    }
}

void free_task(Task* task) {
    // A função free já verifica se o ponteiro é nulo, mas é uma boa prática fazer também.
    if (task) {
        free(task);
    }
}