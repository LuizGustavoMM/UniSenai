#include <stdio.h>
#include "tdacabecalho.h"

int main() {
    Aluno *aluno;
    //Aluno a;
    aluno = cria_aluno(1000);
    define_notas(aluno, 9.5, 8.3, 10);
    //aluno->notas[0] = 5.0;
    float nota1, nota2, nota3, media;
    obtem_notas(aluno, &nota1, &nota2, &nota3, &media);
    libera(aluno);
    printf("nota1 = %.2f\n", nota1);
    printf("nota2 = %.2f\n", nota2);
    printf("nota3 = %.2f\n", nota3);
    printf("media = %.2f\n", media);
    return 0;
}