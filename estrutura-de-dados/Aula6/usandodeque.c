#include <stdio.h>
#include "deque.h"

int main() {
    Deque *d = create_deque();

    insert_head(d, 10);
    insert_tail(d, 20);

    int valor;
    get_head(d, &valor);
    printf("Head: %d\n,", valor);

    get_tail(d, &valor);
    printf("Tail: %d\n", valor);

    remove_head(d, &valor);
    printf("Removido do inicio: %d\n", valor);

    remove_tail(d, &valor);
    printf("Removido do final: %d\n", valor);

    free_deque(d);

    return 0;
}