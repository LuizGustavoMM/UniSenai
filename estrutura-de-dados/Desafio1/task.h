// Arquivo de cabeçalho para o TAD Task

// Usamos um 'typedef' para criar um tipo opaco.
typedef struct Task Task;

/**
 * Cria uma nova tarefa na memória.
 * @param id Um número de identificação para a tarefa.
 * @param nome O nome da tarefa (será copiado).
 * @param descricao A descrição da tarefa (será copiada).
 * @return Um ponteiro para a nova tarefa criada, ou NULL se a memória não puder ser alocada.
 */
Task* create_task(int id, const char* nome, const char* descricao);

/**
 * Mostra os detalhes de uma tarefa na tela.
 * @param task Um ponteiro para a tarefa que deve ser exibida.
 */
void show_task(Task* task);

/**
 * Libera a memória usada por uma tarefa.
 * @param task Um ponteiro para a tarefa que deve ser destruída.
 */
void free_task(Task* task);