#include <stdio.h>

int main() {
    int a[10] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int b[10];
    int *c;
    for(int i = 20; i < 31; i++) {
        b[i-20] = i;
    }
    c = b;
    for(int i = 0; i < 10; i++) {
        printf("a[%d]=%d,b[%d]=%d,c[%d]=%d\n", i, a[i], i, b[i], i, c[i]);
    }
}