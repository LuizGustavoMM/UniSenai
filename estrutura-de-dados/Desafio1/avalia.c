#include "task.h"
#include "sprint.h"
#include <stdlib.h>

int main() {
    Backlog* backlog = create_backlog();
    if(!backlog) {
        perror("Nao foi possivel criar o backlog");
    }
    int i;
    Task *task;
    //Inserindo tarefas de 0 a 9
    for(i = 0; i < 10; i++) {
        task = create_task(i, "Nome tarefa", "Descricao tarefa");
        if(!add(backlog, task)) { 
            perror("Deveria ter incluido a tarefa - primeira insercao");
        }
    }
    //Tentando inserir a tarefa 10 - alem do limite
    Task *task_extra = create_task(10, "Tarefa extra", "Descricao tarefa extra");
    if(!task_extra) {
        perror("Erro na criacao da tarefa extra!");
        return 2;
    }
    if(add(backlog, task_extra)) perror("Deveria ter rejeitado a tarefa extra - primeira");
    //Pegando a tarefa 0
    task = next(backlog);
    show_task(task);
    free_task(task);
    //Incluindo a tarefa extra 10
    if(!add(backlog, task_extra)) perror("Deveria ter inserido a tarefa extra");
    //Pegando as tarefas de 1 a 5
    for(i = 0; i < 5; i++) {
        task = next(backlog);
        show_task(task);
        free_task(task);
    }
    //Retirando as tarefas de 8 a 10
    for(i = 0; i < 3; i++) {
        if(!del(backlog)) perror("Deveria ter removido a tarefa");
    }
    //Inserindo as tarefas 8 a 10
    for(i = 8; i < 16; i++) {
        task = create_task(i, "Nome tarefa nova", "Descricao tarefa nova");
        if(!task) {
            perror("Erro na criacao da tarefa!");
            return 2;
        }
        if(!add(backlog, task)) perror("Deveria ter incluido a tarefa - segunda insercao");
    }
    //Insercao alem do limite
    task_extra = create_task(99, "Tarefa extra 1", "Descricao tarefa extra 1");
    if(!task_extra) {
        perror("Erro na criacao da tarefa extra!");
        return 2;
    }
    if(add(backlog, task_extra)) perror("Deveria ter rejeitado a tarefa extra - segunda");
    for(i = 0; i < 10; i++) {
        task = next(backlog);
        show_task(task);
        free_task(task);
    }
    task = next(backlog);
    if(task) {
        perror("Deveria ter rejeitado o next, o backlog deveria estar vazio");
        free_task(task);
    }
    free_backlog(backlog);
    return 0;
}
