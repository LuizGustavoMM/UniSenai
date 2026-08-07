#ifndef LIST_H
#define LIST_H

#include "client.h"

/* Create a list with a sentinel head node. Returns pointer to head. */
Client* create_list(void);

/* Insert a client at the end of the list (uses sentinel head) */
void insert_client_end(Client* head, const char* name, const char* phone, const char* email);

/* Sort list by name (lexicographic) */
void sort_list_for_name(Client* head);

/* Print all clients in the list */
void show_list(const Client* head);

/* Find first client by phone, returns pointer or NULL */
Client* search_by_phone(const Client* head, const char* phone);

/* Free all nodes including sentinel head */
void free_list(Client* head);

#endif