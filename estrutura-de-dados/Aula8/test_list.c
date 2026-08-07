#include <stdio.h>
#include "list.h"
int main() {
    int i;
    List* list = create_list();
    if(list == NULL) {
        fprintf(stderr, "Nao foi possivel criar a lista\n");
    }
    int j = 10;
    for(i = 0; i < 5; i++, j++) {
        insert_position(list, i+1, j);
    }
    for(i = 0; i < 5; i++) {
        insert_position(list, 1, 4 - i);
    }
    j = 5;
    for(i = 0; i < 4; i++, j++) {
        insert_position(list, j, j);
    }
    insert_position(list, 9, 9);
    show(list);
    int value;
    remove_begin(list, &value);
    printf("retirado %d\n", value);
    show(list);
    remove_begin(list, &value);
    printf("retirado %d\n", value);
    show(list);    
    remove_end(list, &value);
    printf("retirado %d\n", value);
    show(list);    
    remove_end(list, &value);
    printf("retirado %d\n", value);
    show(list);    
    remove_position(list, 9, &value);
    printf("retirado posicao %d %d\n", 9, value);
    show(list);
    remove_position(list, 3, &value);
    printf("retirado posicao %d %d\n", 3, value);
    show(list);
    printf("navegacao reversa\n");
    show(list);
    int get_value;
    int status = get_begin(list, &get_value);
    if(status) {
        printf("inicio = %d\n", get_value);
    } else {
        printf("lista vazia\n");
    }
    status = get_end(list, &get_value);
    if(status) {
        printf("fim = %d\n", get_value);
    } else {
        printf("lista vazia\n");
    }
    status = get_position(list, 6, &get_value);
    if(status) {
        printf("posicao %d = %d\n", 6, get_value);
    } else {
        printf("lista vazia\n");
    }
    status = get_position(list, 2, &get_value);
    if(status) {
        printf("posicao %d = %d\n", 2, get_value);
    } else {
        printf("lista vazia\n");
    }   
}