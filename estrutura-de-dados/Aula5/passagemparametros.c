#include <stdio.h>

void funcaoParametroPorValor(int porValor) {
    porValor += 100;
    printf("Valor do parametro na funcao = %d\n", porValor);
}
void funcaoParametroPorReferencia(int *porReferencia) {
    *porReferencia += 200;
    printf("Valor do parametro na funcao = %d\n", *porReferencia);
}
int main() {
    int valor; valor = 10;
    printf("valor = %d\n", valor);
    funcaoParametroPorValor(valor);
    printf("valor = %d\n", valor);
    funcaoParametroPorReferencia(&valor);
    printf("valor = %d\n", valor);
}