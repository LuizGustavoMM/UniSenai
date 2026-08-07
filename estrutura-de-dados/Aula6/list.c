// Arquivo: list.c
// Implementação das funções de lista encadeada.

#include <stdio.h>
#include <stdlib.h>
#include "list.h"

// Estrutura do Nó (elemento da lista)
typedef struct Node_ {
    int value;
    struct Node_* next;
} Node;

// Estrutura da Lista (contém o ponteiro para o início)
typedef struct List_ {
    Node* head;
    int size;
} List;

List* create_list() {
    List* list = (List*) malloc(sizeof(List));
    if (list != NULL) {
        list->head = NULL;
        list->size = 0;
    }
    return list;
}

int empty(List* list) {
    if (list == NULL) return 1; // Considera nulo como vazio
    return list->size == 0;
}

int insert_head(List* list, int value) {
    if (list == NULL) return 0;
    Node* new_node = (Node*) malloc(sizeof(Node));
    if (new_node == NULL) return 0;

    new_node->value = value;
    new_node->next = list->head;
    list->head = new_node;
    list->size++;
    return 1;
}

int insert_tail(List* list, int value) {
    if (list == NULL) return 0;
    Node* new_node = (Node*) malloc(sizeof(Node));
    if (new_node == NULL) return 0;
    new_node->value = value;
    new_node->next = NULL;

    if (empty(list)) {
        list->head = new_node;
    } else {
        Node* current = list->head;
        while (current->next != NULL) {
            current = current->next;
        }
        current->next = new_node;
    }
    list->size++;
    return 1;
}

int insert_position(List* list, int position, int value) {
    if (list == NULL || position < 0 || position > list->size) return 0;
    if (position == 0) {
        return insert_head(list, value);
    }

    Node* current = list->head;
    for (int i = 0; i < position - 1; i++) {
        current = current->next;
    }

    Node* new_node = (Node*) malloc(sizeof(Node));
    if (new_node == NULL) return 0;
    new_node->value = value;
    new_node->next = current->next;
    current->next = new_node;
    list->size++;
    return 1;
}

int remove_head(List* list, int* value) {
    if (list == NULL || empty(list)) return 0;
    
    Node* to_remove = list->head;
    *value = to_remove->value;
    list->head = to_remove->next;
    
    free(to_remove);
    list->size--;
    return 1;
}

int remove_tail(List* list, int* value) {
    if (list == NULL || empty(list)) return 0;

    if (list->size == 1) {
        return remove_head(list, value);
    }
    
    Node* current = list->head;
    // Vai até o penúltimo nó
    while(current->next->next != NULL) {
        current = current->next;
    }
    
    Node* to_remove = current->next;
    *value = to_remove->value;
    current->next = NULL;
    
    free(to_remove);
    list->size--;
    return 1;
}

int remove_position(List* list, int position, int* value) {
    if (list == NULL || empty(list) || position < 0 || position >= list->size) return 0;
    if (position == 0) {
        return remove_head(list, value);
    }
    
    Node* current = list->head;
    for(int i = 0; i < position - 1; i++) {
        current = current->next;
    }
    
    Node* to_remove = current->next;
    *value = to_remove->value;
    current->next = to_remove->next;

    free(to_remove);
    list->size--;
    return 1;
}

int get_head(List* list, int* value) {
    if (list == NULL || empty(list)) return 0;
    *value = list->head->value;
    return 1;
}

int get_tail(List* list, int* value) {
    if (list == NULL || empty(list)) return 0;
    Node* current = list->head;
    while(current->next != NULL) {
        current = current->next;
    }
    *value = current->value;
    return 1;
}

int get_position(List* list, int position, int* value) {
    if (list == NULL || empty(list) || position < 0 || position >= list->size) return 0;
    Node* current = list->head;
    for (int i = 0; i < position; i++) {
        current = current->next;
    }
    *value = current->value;
    return 1;
}

void free_list(List* list) {
    if (list == NULL) return;
    Node* current = list->head;
    while (current != NULL) {
        Node* temp = current;
        current = current->next;
        free(temp);
    }
    free(list);
}