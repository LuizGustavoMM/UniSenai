public class TiposDados {
	public static void main(String[] args) {
		int v1 = 10;
		v1 = 20;
		int v2; // não fazer
		//int v3, v4 = 8, 9; // não fazer
		
		char exChar = 'C';
		float exFloat = 5.2f;
		double exDouble = 7.2d;
		boolean exBoolean = true; // false

		String joao = "Joao";
		String joao_pedro_maria_joaquina = "Joao";
		String nomeClienteVenda = "Joao";
		System.out.println("Nome: " + nomeClienteVenda);

		String nula = null;
		String vazia = "";

		int x = 10;
		int y = 99;
		int i = 5;
		int j = 5;
		int zero = 0;

		//int calculo = x / zero;
		int calculo2 = i + (y / x);

		int incremento = 0;
		incremento = incremento + 1;
		incremento += 1;
		incremento++;

		int calculoIncremento = incremento++ + 10;
		System.out.println("RESULTADO: " + calculoIncremento);

		
	}
}