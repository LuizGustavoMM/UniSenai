#include <stdio.h>
int main() {
    int v1, v2;
    char c1, c2;
    printf("v1=%d, v2=%d, c1(numerico)=%d, c1(char)=%c, c2(numerico)=%d, c2(char)=%c\n", v1, v2, c1, c1, c2, c2);
    v1 = 10; v2 = 20;
    c1 = 'm'; c2 = '9';
    printf("v1=%d, v2=%d, c1(numerico)=%d, c1(char)=%c, c2(numerico)=%d, c2(char)=%c\n", v1, v2, c1, c1, c2, c2);
    printf("A soma de %d e %d eh: %d\n", v1, v2, v1 + v2);
    printf("A soma de %d e %d eh: %d\n", c1, c2, c1 + c2);
    printf("A soma de %c e %c eh: %c\n", c1, c2, c1 + c2);
    return 0;
}