#include <stdio.h>
#include <stdlib.h>
#include "sprint.h" // Inclui nossa própria interface
#include "task.h"   // Inclui a interface da Task

#define MAX_TASKS 10

// A estrutura interna usa apenas um vetor e um contador.
struct Backlog {
    Task* tasks[MAX_TASKS];
    int quantidade;
};

Backlog* create_backlog() {
    Backlog* backlog = (Backlog*) malloc(sizeof(Backlog));
    if (backlog) {
        backlog->quantidade = 0;
    }
    return backlog;
}

// Adiciona uma tarefa no final do vetor.
int add(Backlog* backlog, Task* task) {
    if (backlog && backlog->quantidade < MAX_TASKS) {
        backlog->tasks[backlog->quantidade] = task;
        backlog->quantidade++;
        return 1; // Sucesso
    }
    return 0; // Falha (backlog nulo ou cheio)
}

// Pega a primeira tarefa e desloca as outras.
Task* next(Backlog* backlog) {
    if (!backlog || backlog->quantidade == 0) {
        return NULL; // Falha (backlog nulo ou vazio)
    }

    Task* proxima_task = backlog->tasks[0]; // Guarda a primeira tarefa

    // Desloca todos os elementos restantes uma posição para a esquerda
    for (int i = 0; i < backlog->quantidade - 1; i++) {
        backlog->tasks[i] = backlog->tasks[i + 1];
    }

    backlog->quantidade--;
    return proxima_task;
}

// Deleta a última tarefa do vetor.
int del(Backlog* backlog) {
    if (!backlog || backlog->quantidade == 0) {
        return 0; // Falha
    }

    backlog->quantidade--; // Diminui o contador, "esquecendo" a última tarefa
    
    // Libera a memória da tarefa que foi removida
    free_task(backlog->tasks[backlog->quantidade]);
    backlog->tasks[backlog->quantidade] = NULL; // Limpa o ponteiro no vetor

    return 1; // Sucesso
}

void free_backlog(Backlog* backlog) {
    if (backlog) {
        // Libera a memória de todas as tarefas que ainda estão no backlog
        for (int i = 0; i < backlog->quantidade; i++) {
            free_task(backlog->tasks[i]);
        }
        // Libera a memória da própria estrutura do backlog
        free(backlog);
    }
}