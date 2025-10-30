#include <stdlib.h>
#include <stdio.h>
#include "tdacabecalho.h"

struct Aluno_ {
    int matricula;
    float notas[3];
};
Aluno* cria_aluno(int matricula) {
    struct Aluno_* a = (struct Aluno_*) malloc(sizeof(struct
Aluno_));
    if(a != NULL) {
        a->matricula = matricula;
        a->notas[0] = 0;
        a->notas[1] = 0;
        a->notas[2] = 0;
    }
    return a;
}
void define_notas(Aluno* aluno, float nota1, float
                  nota2, float nota3) {
    aluno->notas[0] = nota1;
    aluno->notas[1] = nota2;
    aluno->notas[2] = nota3;
}

void define_nota(Aluno* aluno, float nota, int
                 sequencia) {
    if (sequencia < 1 || sequencia > 3) {
        printf("Erro: a sequencia tem que ser entre 1 e 3.\n");
    } else {
        aluno->notas[sequencia - 1] = nota;
    }
}
float obtem_media(Aluno* aluno) {
    float media;
    media = (aluno->notas[0] + aluno->notas[1] + aluno->notas[2]) / 3;
    return media;
}

void obtem_notas(Aluno* aluno, float* nota1, float*
                 nota2, float* nota3, float* media) {
    *nota1 = aluno->notas[0];
    *nota2 = aluno->notas[1];
    *nota3 = aluno->notas[2];
    *media = obtem_media(aluno);
}

void libera(Aluno* aluno) {
    free(aluno);
}