#include <stdio.h>

int main () {
    //Sempre colocar a variavel dentro da função, fora sera uma variavel global
    const int const1 = 25;
    const int const2 = 80;
    printf("A multiplicacacao de %d e %d eh: %d\n", const1, const2, const1 * const2);
}