#include "dlist.h"
#include "double_node.h"
#include <stdlib.h>
#include <stdio.h>

struct List_ {
  int quantity;
  Double_node* begin;
  Double_node* end;
};

List* create_list() {
  List* list = (List*) malloc(sizeof(struct List_));
  if(list != NULL) {
    list->quantity = 0;
    list->begin = NULL;
    list->end = NULL;
  }
  return list;
}

int insert_head(List* list, int value) {
  Double_node* node = creade_double_node(value);
  if(node == NULL) return EXIT_FAILURE;
  if(list->begin == NULL) {
    list->begin = node;
    list->end = node;
  } else {
    set_next(node, list->begin);
    set_previous(list->begin, node);
    list->begin = node;
  }  
  list->quantity++;	
  return EXIT_SUCCESS;
}

int insert_tail(List* list, int value) {
  Double_node* node = creade_double_node(value);
  if(node == NULL) return EXIT_FAILURE;
  if(list->end == NULL) {
    list->begin = node;
    list->end = node;
  } else {
    set_next(list->end, node);
    set_previous(node, list->end);
    list->end = node;
  }  
  list->quantity++;	
  return EXIT_SUCCESS;
}

int insert_position(List* list, int position, int value) {
  if(list->quantity < position && position < 1) return EXIT_FAILURE;
  if(position == 1) {
    insert_head(list, value);
  } else if(position == (list->quantity+1)) {
    insert_tail(list, value);  
  } else {
    Double_node* node = creade_double_node(value);
    if(node == NULL) return 0;
    Double_node* cursor = list->begin;
    while(position > 2) {
      cursor = get_next(cursor);
      position--;
    }    
    printf("posicao do cursor = %d\n", get_data(cursor));
    set_next(node, get_next(cursor)); 
    set_next(cursor, node);
    set_previous(get_next(cursor), node);
    set_previous(node, cursor); 
    list->quantity++; 
  }
  return EXIT_SUCCESS;
}

int remove_head(List* list, int* value) {
  if(list->quantity > 0) {
    *value = get_data(list->begin);
    Double_node* removed = list->begin;
    list->begin = get_next(list->begin);
    if(list->quantity == 1) {
      list->end = NULL;
      list->begin = NULL;
    } else {
      set_previous(list->begin, NULL);
    }
    free(removed);
    list->quantity--;
    return EXIT_SUCCESS;
  }
  return EXIT_FAILURE;
}

int remove_tail(List* list, int* value) {
  if(list->quantity > 0) {
    *value = get_data(list->end);
    Double_node* removed = list->end;
    list->end = get_previous(list->end);
    if(list->quantity == 1) {
      list->end = NULL;
      list->begin = NULL;
    } else {
      set_next(list->end, NULL);
    }
    free(removed);
    list->quantity--;
    return EXIT_SUCCESS;
  }
  return EXIT_FAILURE;
}

int remove_position(List* list, int position, int* value) {
  if(list->quantity < position || position < 1) return EXIT_FAILURE;
  if(position == 1) {
    remove_head(list, value);
  } else if(position == (list->quantity)) {
    remove_tail(list, value);  
  } else {
    Double_node* cursor = list->begin;
    while(position > 1) {
      cursor = get_next(cursor);
      position--;
    }
    Double_node *removed = cursor;
    *value = get_data(cursor);
    set_next(get_previous(cursor), get_next(cursor));
    set_previous(get_next(cursor), get_previous(cursor));
    free(removed);
    list->quantity--; 
  }
  return EXIT_SUCCESS;
}


int get_head(List* list, int *value) {
  if(list->quantity > 0) {
    *value = get_data(list->begin);
    return EXIT_SUCCESS;
  } else {
    return EXIT_FAILURE;
  }
}

int get_tail(List* list, int *value) {
  if(list->quantity > 0) {
    *value = get_data(list->end);
    return EXIT_SUCCESS;
  } else {
    return EXIT_FAILURE;
  }
}

void free_list(List* list) {
  int i;
  Double_node* removed = list->begin;
  for(i = 0; i < list->quantity; i++) {
    Double_node* cursor = get_next(removed);
    free(removed);
    removed = cursor;		
  }
  free(list);
}

int empty(List* list) {
	return list->quantity == 0 ? 1 : 0;
}

void show(List* list) {
  printf("direto\n")
  Double_node *cursor = list->begin;
  while(cursor != NULL) {
    printf("%d, ", get_data(cursor));
    cursor = get_next(cursor);
  }
  printf("\nreverso\n");
  cursor = list->end;
  while(cursor != NULL) {
    printf("%d, ", get_data(cursor));
    cursor = get_previous(cursor);
  }
  printf("\n");
}