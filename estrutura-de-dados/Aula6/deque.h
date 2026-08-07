typedef struct Deque_ Deque;

Deque* create_deque();
int insert_head(Deque* deque, int value);
int insert_tail(Deque* deque, int value);
int remove_head(Deque* deque, int *value);
int remove_tail(Deque* deque, int *value);
int get_head(Deque* deque, int *value);
int get_tail(Deque* deque, int *value);
void free_deque(Deque* deque);