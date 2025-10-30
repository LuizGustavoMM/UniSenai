// Faca um programa que uttilize 5 numeros reais (123.25; 3.141592; 1.27; 1.931; 1.010101) e apresente-os da seguinte forma:
// um do lado do outro com 4 digitos antes da virgula e dois digitos depois da virgula devem estar distantes uns dos outros 
// por dois espaços e o ultimo a ser apresentado deve ser a divisão consecutiva desses valores.
// Exemplo: 123.25  3.14  1.27  1.93  1.01
#include <stdio.h>
#define num 123.25

int main () {
    float l[5] = {123.25, 3.141592, 1.27, 1.931, 1.010101};
    int i;
    for (i = 0; i < 5; i++) {
        printf("\n%.2f", l[i]);
    }
    return 0;
}

