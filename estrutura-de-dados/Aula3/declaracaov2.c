#include <stdio.h>
#include<stdlib.h>
int main() {
    int a;
    int *p;
    printf("a = %d, p = %d\n", a, p);
    a = 10;
    p = (int *) malloc(sizeof(int));
    *p = 20;
    printf("a = %d, p = %d\n", a, *p);
    return 0;
}