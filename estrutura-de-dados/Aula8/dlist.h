typedef struct List_ List;

List* create_list();
int insert_head(List* list, int value);
int insert_tail(List* list, int value);
int insert_position(List* list, int position, int value);
int remove_head(List* list, int* value);
int remove_tail(List* list, int* value);
int remove_position(List* list, int position, int* value);
int get_head(List* list, int* value);
int get_tail(List* list, int* value);
int get_position(List* list, int position, int* value);
void free_list(List* list);
int empty(List* list);