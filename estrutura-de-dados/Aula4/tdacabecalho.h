#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct Aluno_ Aluno;

Aluno* cria_aluno(int matricula);
void define_notas(Aluno* aluno, float n1, float n2, float n3);
void define_nota(Aluno* aluno, float nota, int sequencia);
float obtem_media(Aluno* aluno);
void obtem_notas(Aluno* aluno, float* n1, float* n2, float* n3,
float* media);
void libera(Aluno* aluno);