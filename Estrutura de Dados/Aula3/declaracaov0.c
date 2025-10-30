#include <stdio.h>
int main () {
    int a;
    int  *p;
    printf("a = %d, p = %d\n", a, p);
    a = 10;
    *p = 20; //isso pode?
    printf("a = %d, p = %d\n", a, *p);
    return 0;
}