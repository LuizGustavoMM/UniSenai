#include <stdio.h>
#include <stdlib.h>

int main() {
    int a[10] = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    int *b;
    b = (int *) malloc(10*sizeof(int));
    for(int i = 0; i < 10; i++) {
        *b = a[i] + 1;
        b++;
    }
    b -= 10;
    for(int i = 0; i < 10; i++) {
        printf("a[%d] = %d, b[%d] = %d\n", i, a[i], i, *b++);
    }
}