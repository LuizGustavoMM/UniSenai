typedef struct Deque_ Deque;

Deque* create_deque();
int insert_begin(Deque* deque, int data);
int insert_end(Deque* deque, int data);
int remove_begin(Deque* deque, int *data);
int remove_end(Deque* deque, int *data);
int get_begin(Deque* deque, int *data);
int get_end(Deque* deque, int *data);
void free_deque(Deque* deque);
