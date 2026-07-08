import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import 'data/local/database_helper.dart';
import 'data/repositories/ficha_repository.dart';
import 'data/repositories/jogador_repository.dart';
import 'data/repositories/mesa_repository.dart';
import 'logic/ficha_cubit.dart';
import 'logic/jogador_cubit.dart';
import 'logic/mesa_cubit.dart';
import 'presentation/screens/identidade_screen.dart';
import 'presentation/screens/minhas_mesas_screen.dart';

void main() {
  runApp(const AppJogador());
}

/// APK do jogador (Mesa D&D).
///
/// Injeção de dependência no padrão das aulas (MultiRepositoryProvider +
/// MultiBlocProvider). O banco local é o `DatabaseHelper` singleton (sqflite).
class AppJogador extends StatelessWidget {
  const AppJogador({super.key});

  @override
  Widget build(BuildContext context) {
    final dbHelper = DatabaseHelper.instance;

    return MultiRepositoryProvider(
      providers: [
        RepositoryProvider(create: (_) => JogadorRepository(dbHelper)),
        RepositoryProvider(create: (_) => MesaRepository(dbHelper)),
        RepositoryProvider(create: (_) => FichaRepository(dbHelper)),
      ],
      child: MultiBlocProvider(
        providers: [
          BlocProvider(
            create: (ctx) =>
                JogadorCubit(ctx.read<JogadorRepository>())..carregar(),
          ),
          BlocProvider(
            create: (ctx) => MesaCubit(ctx.read<MesaRepository>()),
          ),
          BlocProvider(
            create: (ctx) => FichaCubit(ctx.read<FichaRepository>()),
          ),
        ],
        child: MaterialApp(
          title: 'Mesa D&D — Jogador',
          debugShowCheckedModeBanner: false,
          theme: ThemeData(
            colorScheme:
                ColorScheme.fromSeed(seedColor: const Color(0xFF6D4C1E)),
            useMaterial3: true,
          ),
          home: const _Gate(),
        ),
      ),
    );
  }
}

/// Decide a tela inicial pela identidade: primeira execução → cadastro do
/// apelido; caso contrário → lista de mesas.
class _Gate extends StatelessWidget {
  const _Gate();

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<JogadorCubit, JogadorState>(
      builder: (context, state) => switch (state) {
        JogadorCarregando() =>
          const Scaffold(body: Center(child: CircularProgressIndicator())),
        SemJogador() => const IdentidadeScreen(),
        ComJogador() => const MinhasMesasScreen(),
      },
    );
  }
}
