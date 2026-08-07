#include <limits.h>
#include <stdio.h>

int main () {
    printf("Inteiro minimo: %d, maximo: %d\n", INT_MIN, INT_MAX);
    printf("Inteiro sem sinal maximo: %u\n", UINT_MAX);
    printf("Numero de bits por byte: %d\n", CHAR_BIT);
    printf("Char minimo: %d, maximo: %d\n", CHAR_MIN, CHAR_MAX);
    return 0;
}