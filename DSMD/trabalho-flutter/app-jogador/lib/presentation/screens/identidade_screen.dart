import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../logic/jogador_cubit.dart';

/// Primeira execução: o jogador escolhe um apelido. O app gera um UUID interno
/// (sem login) que acompanha as fichas enviadas ao servidor.
class IdentidadeScreen extends StatefulWidget {
  const IdentidadeScreen({super.key});

  @override
  State<IdentidadeScreen> createState() => _IdentidadeScreenState();
}

class _IdentidadeScreenState extends State<IdentidadeScreen> {
  final _apelidoController = TextEditingController();

  @override
  void dispose() {
    _apelidoController.dispose();
    super.dispose();
  }

  void _continuar() {
    final apelido = _apelidoController.text.trim();
    if (apelido.isEmpty) return;
    context.read<JogadorCubit>().definirApelido(apelido);
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Bem-vindo'),
        backgroundColor: theme.colorScheme.inversePrimary,
      ),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.shield_moon_outlined,
                size: 72, color: theme.colorScheme.primary),
            const SizedBox(height: 16),
            Text('Como quer ser chamado?', style: theme.textTheme.titleLarge),
            const SizedBox(height: 8),
            Text(
              'Escolha um apelido para entrar nas mesas de RPG.',
              textAlign: TextAlign.center,
              style: theme.textTheme.bodyMedium,
            ),
            const SizedBox(height: 24),
            TextField(
              controller: _apelidoController,
              textCapitalization: TextCapitalization.words,
              decoration: const InputDecoration(
                labelText: 'Apelido',
                hintText: 'Ex.: Gandalf',
                border: OutlineInputBorder(),
              ),
              onSubmitted: (_) => _continuar(),
            ),
            const SizedBox(height: 16),
            FilledButton.icon(
              onPressed: _continuar,
              icon: const Icon(Icons.arrow_forward),
              label: const Text('Continuar'),
            ),
          ],
        ),
      ),
    );
  }
}
