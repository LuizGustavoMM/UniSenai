import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../logic/mesa_cubit.dart';

/// Entrar em uma mesa: informa o endereço do servidor (IP do notebook) e o
/// código da mesa fornecido pelo mestre.
class EntrarMesaScreen extends StatefulWidget {
  const EntrarMesaScreen({super.key});

  @override
  State<EntrarMesaScreen> createState() => _EntrarMesaScreenState();
}

class _EntrarMesaScreenState extends State<EntrarMesaScreen> {
  final _ipController =
      TextEditingController(text: 'http://192.168.137.1:8000');
  final _codigoController = TextEditingController();
  bool _entrando = false;

  @override
  void dispose() {
    _ipController.dispose();
    _codigoController.dispose();
    super.dispose();
  }

  Future<void> _entrar() async {
    final ip = _ipController.text.trim();
    final codigo = _codigoController.text.trim();
    if (ip.isEmpty || codigo.isEmpty) return;

    setState(() => _entrando = true);
    final mesa = await context
        .read<MesaCubit>()
        .entrar(ip: ip, codigo: codigo);
    if (!mounted) return;
    setState(() => _entrando = false);

    if (mesa != null) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Entrou na mesa "${mesa.nome}"!')),
      );
      Navigator.of(context).pop();
    } else {
      final state = context.read<MesaCubit>().state;
      final msg = state is MesaErro ? state.mensagem : 'Falha ao entrar.';
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text(msg)));
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Entrar em mesa'),
        backgroundColor: theme.colorScheme.inversePrimary,
      ),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            TextField(
              controller: _ipController,
              keyboardType: TextInputType.url,
              decoration: const InputDecoration(
                labelText: 'Endereço do servidor',
                hintText: 'http://192.168.137.1:8000',
                helperText: 'IP do notebook do mestre na rede Wi-Fi',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _codigoController,
              textCapitalization: TextCapitalization.characters,
              decoration: const InputDecoration(
                labelText: 'Código da mesa',
                hintText: 'Ex.: A7K2QP',
                border: OutlineInputBorder(),
              ),
              onSubmitted: (_) => _entrar(),
            ),
            const SizedBox(height: 24),
            FilledButton.icon(
              onPressed: _entrando ? null : _entrar,
              icon: _entrando
                  ? const SizedBox(
                      width: 18,
                      height: 18,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    )
                  : const Icon(Icons.login),
              label: const Text('Entrar'),
            ),
          ],
        ),
      ),
    );
  }
}
