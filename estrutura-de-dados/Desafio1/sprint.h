#ifndef SPRINT_H
#define SPRINT_H

#include "task.h"

typedef struct Backlog Backlog;

Backlog* create_backlog();
int add(Backlog* backlog, Task* task); // Retorna 1 para sucesso, 0 para falha
Task* next(Backlog* backlog); // Retorna a próxima tarefa ou NULL se vazio
int del(Backlog* backlog); // Remove a última tarefa. Retorna 1 para sucesso, 0 para falha
void free_backlog(Backlog* backlog);

#endif