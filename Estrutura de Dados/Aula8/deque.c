#include "deque.h"
#include "double_node.h"
#include <stdlib.h>
#define TRUE 1
#define FALSE 0

struct Deque_ {
    Double_node* begin;
    Double_node* end;
};

Deque* create_deque() {
    Deque* deque = (Deque *) malloc(sizeof(struct Deque_));
    if(deque != NULL) {
        deque->begin = NULL;
        deque->end = NULL;
    }
    return deque;
}

int insert_begin(Deque* deque, int data) {
    Double_node* node = create_double_node(data);
    if(node == NULL) return EXIT_FAILURE;
    if(deque->begin == NULL) {
        deque->end = node;
    } else {
        set_next(node, deque->begin);
        set_previous(deque->begin, node);
    }
    deque->begin = node;
    return EXIT_SUCCESS;
}

int insert_end(Deque* deque, int data) {
    Double_node* node = create_double_node(data);
    if(node == NULL) return EXIT_FAILURE;
    if(deque->begin == NULL) {
        deque->begin = node;
    } else {
        set_previous(node, deque->end);
        set_next(deque->end, node);
    }
    deque->end = node;
    return EXIT_SUCCESS;
}

int remove_begin(Deque* deque, int *data) {
    if(deque->begin == NULL) return EXIT_FAILURE;
    *data = get_data(deque->begin);
    Double_node* removed = deque->begin;
    deque->begin = get_next(deque->begin);
    if(deque->begin == NULL) {
        deque->end = NULL;
    } else {
        set_previous(deque->begin, NULL);
    }    
    free(removed);
    return EXIT_SUCCESS;
}

int remove_end(Deque* deque, int *data) {
    if(deque->begin == NULL) return EXIT_FAILURE;
    *data = get_data(deque->end);
    Double_node* removed = deque->end;
    deque->end = get_previous(deque->end);
    if(deque->end == NULL) {
        deque->begin = NULL;
    } else {
        set_next(deque->end, NULL);
    }
    free(removed);
    return EXIT_SUCCESS;
}

int get_begin(Deque* deque, int *data) {
    if(deque->begin == NULL) return EXIT_FAILURE;
    *data = get_data(deque->begin);
    return EXIT_SUCCESS;
}

int get_end(Deque* deque, int *data) {    
    if(deque->begin == NULL) return EXIT_FAILURE;
    *data = get_data(deque->end);
    return EXIT_SUCCESS;
}

void free_deque(Deque* deque) {
    Double_node* removed; 
    while(deque->begin != NULL) {
        removed = deque->begin;
        deque->begin = get_next(deque->begin);
        free(removed);
    }
    free(deque);
}