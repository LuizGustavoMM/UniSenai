#include <stdio.h> 
#include <client.h>
#include <list.h>
typedef struct Sample_ {
    char name[60];
    char phone[60];
    char email[60];
} Sample;
int main() {
    Sample array_clients[10] = {
        { "Joao Cabral de Melo Neto e Silva", "11987654321", "joao.silva@email.com" },
        { "Maria dos Passos Oliveira Dantas", "11923456789", "maria.oliveira@email.com" },
        { "Carlos Antonio de Almeida Souza", "11876543210", "carlos.souza@email.com" },
        { "Ana Paula de Oliveira Guerios", "11765432109", "anapaula@email.com" },
        { "Bruno Barreto de Lima", "11654321098", "bruno.lima@email.com" },
        { "Fernanda Abreu Costa", "11543210987", "fernanda.costa@email.com" },
        { "Pedro Alves de Carmonato", "11432109876", "pedro.alves@email.com" },
        { "Juliana Maria Dias", "11321098765", "juliana.dias@email.com" },
        { "Ricardo Jose de Orleans Martins", "11210987654", "ricardo.martins@email.com" },
        { "Beatriz Cristina de Souza Rocha", "11109876543", "beatriz.rocha@email.com" }
    };


    for(int i = 0; i < 10; i++) {
        printf("Nome: %s, Telefone: %s, email: %s\n", array_clients[i].name, array_clients[i].phone, array_clients[i].email);
    }

    Client* list = create_list();
    int i;
    for(i = 0; i < 10; i++) {
        insert_client_end(list, array_clients[i].name, array_clients[i].phone, array_clients[i].email);
    }
    sort_list_for_name(list);

    show_list();

    Client* client = search_by_phone(list, "11654321098");
    if(client) {
        show_client(client);
    } else {
        perror("Client not found");
    }

    client = search_by_phone(list, "11012987346");
    if(client) {
        show_client(client);
    } else {
        perror("Client not found");
    }
    
    free_list(list);
    return 0;
}