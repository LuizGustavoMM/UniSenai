/// Endereço da API do servidor (contêiner `servidor`, publicado no host:8000).
///
/// Pode ser sobrescrito no build com:
///   flutter build web --dart-define=API_URL=http://192.168.137.1:8000
const String apiBaseUrl = String.fromEnvironment(
  'API_URL',
  defaultValue: 'http://localhost:8000',
);
