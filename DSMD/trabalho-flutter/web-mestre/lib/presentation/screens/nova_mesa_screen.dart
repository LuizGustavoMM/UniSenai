import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../data/models/mesa_model.dart';
import '../../logic/mesa_cubit.dart';

/// Tela inicial do mestre: cria uma nova mesa de RPG.
///
/// Ao criar, o servidor gera o código (mostrado em destaque na tela) e o id
/// interno; o código é o que o mestre passa aos jogadores.
class NovaMesaScreen extends StatefulWidget {
  const NovaMesaScreen({super.key});

  @override
  State<NovaMesaScreen> createState() => _NovaMesaScreenState();
}

class _NovaMesaScreenState extends State<NovaMesaScreen> {
  final _nomeController = TextEditingController();
  bool _criando = false;

  @override
  void dispose() {
    _nomeController.dispose();
    super.dispose();
  }

  Future<void> _criarMesa() async {
    final nome = _nomeController.text.trim();
    if (nome.isEmpty) return;

    setState(() => _criando = true);
    final mesa = await context.read<MesaCubit>().criar(nome);
    if (!mounted) return;
    setState(() => _criando = false);

    if (mesa != null) {
      _nomeController.clear();
      await _mostrarCodigo(mesa);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Não foi possível criar a mesa.')),
      );
    }
  }

  /// Mostra o código gerado em destaque, para o mestre repassar aos jogadores.
  Future<void> _mostrarCodigo(MesaModel mesa) {
    return showDialog<void>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Mesa criada!'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text('"${mesa.nome}"'),
            const SizedBox(height: 16),
            const Text('Código para os jogadores entrarem:'),
            const SizedBox(height: 8),
            SelectableText(
              mesa.codigo,
              style: const TextStyle(
                fontSize: 40,
                fontWeight: FontWeight.bold,
                letterSpacing: 6,
              ),
            ),
          ],
        ),
        actions: [
          TextButton.icon(
            onPressed: () {
              Clipboard.setData(ClipboardData(text: mesa.codigo));
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('Código copiado!')),
              );
            },
            icon: const Icon(Icons.copy),
            label: const Text('Copiar'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Pronto'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Mesa D&D | Mestre'),
        backgroundColor: theme.colorScheme.inversePrimary,
      ),
      body: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 420),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.casino_outlined,
                  size: 72, color: theme.colorScheme.primary),
              const SizedBox(height: 16),
              Text('Criar nova mesa', style: theme.textTheme.headlineSmall),
              const SizedBox(height: 8),
              Text(
                'Dê um nome à mesa. O servidor gera um código que você '
                'repassa aos jogadores.',
                textAlign: TextAlign.center,
                style: theme.textTheme.bodyMedium,
              ),
              const SizedBox(height: 24),
              TextField(
                controller: _nomeController,
                decoration: const InputDecoration(
                  labelText: 'Nome da mesa',
                  hintText: 'Ex.: Tumba de Horrores',
                  border: OutlineInputBorder(),
                ),
                onSubmitted: (_) => _criarMesa(),
              ),
              const SizedBox(height: 16),
              FilledButton.icon(
                onPressed: _criando ? null : _criarMesa,
                icon: _criando
                    ? const SizedBox(
                        width: 18,
                        height: 18,
                        child: CircularProgressIndicator(strokeWidth: 2),
                      )
                    : const Icon(Icons.add),
                label: const Text('Criar mesa'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
