import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.simpleframework.http.Query;
import org.simpleframework.http.Request;

public final class EstoqueService {
	private Estoque estoque;

	public String adicionarProduto(Request request) {
		String descricao;
		float preco;
		int quant, tipo;
		LocalDateTime dataFabricacao;
		Produto p = null;

		Query query = request.getQuery();

		descricao = query.get("descricao");
		preco = query.getFloat("preco");
		quant = query.getInteger("quant");
	    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
	    LocalDate date = LocalDate.parse(query.get("dataFabricacao"),formatter);
	 	dataFabricacao = date.atStartOfDay();
		tipo = query.getInteger("tipo");

		switch (tipo) {
		case 1:
			p = new BemDuravel(descricao, preco, quant, dataFabricacao, 12);
			break;
		case 2:
			p = new BemDeConsumo(descricao, preco, quant, dataFabricacao, dataFabricacao.plusMonths(12).toLocalDate());
			break;
		}
		if (p != null) {
			estoque.adicionar(p);
		}

		return p.toString();

	}

	public String consultarProduto(Request request) {

		Query query = request.getQuery();
		String descricao = query.get("descricao");
		Produto p = estoque.consultar(descricao);

		return p.toString();

	}

	public String removerProduto(Request request) {
		int num = estoque.getNumProdutos();
		Query query = request.getQuery();
		String descricao = query.get("descricao");
		estoque.remover(descricao);
		
		if (num < estoque.getNumProdutos())
			return "Removido";
		else
			return null;

	}

	public String totalEmEstoque(Request request) {
		return Integer.toString(estoque.totalEmEstoque());

	}

	public String valorEmEstoque(Request request) {
		return Float.toString(estoque.valorEmEstoque());

	}

	public EstoqueService() {
		estoque = new Estoque();
	}

}
