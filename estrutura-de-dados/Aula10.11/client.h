#ifndef CLIENT_H
#define CLIENT_H

#include <stddef.h>

typedef struct Client {
    char name[60];
    char phone[60];
    char email[60];
    struct Client* next;
} Client;

/* Print a single client to stdout */
void show_client(const Client* c);

#endif