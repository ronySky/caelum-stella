package br.com.caelum.stella.boleto.bancos;

import java.net.URL;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Emissor;
import br.com.caelum.stella.boleto.exception.CriacaoBoletoException;

public class Caixa extends AbstractBanco implements Banco {

	private static final String NUMERO_CAIXA = "104";
	private static final String DIGITO_CAIXA = "0";

	@Override
	public String geraCodigoDeBarrasPara(Boleto boleto) {
		StringBuilder codigoDeBarras = new StringBuilder();
		codigoDeBarras.append(getNumeroFormatado());
		codigoDeBarras.append(String.valueOf(boleto.getCodigoEspecieMoeda()));
		// Digito Verificador sera inserido aqui.

		codigoDeBarras.append(boleto.getFatorVencimento());
		codigoDeBarras.append(boleto.getValorFormatado());

		Emissor emissor = boleto.getEmissor();

		codigoDeBarras.append(emissor.getCarteira());
		codigoDeBarras.append(getNossoNumeroDoEmissorFormatado(emissor));

		codigoDeBarras.append(emissor.getAgenciaFormatado());
		codigoDeBarras.append(getCodigoOperacaoFormatado(emissor));
		codigoDeBarras.append(getCodigoFornecidoPelaAgenciaFormatado(emissor));

		codigoDeBarras.insert(4, this.dvGenerator.geraDigitoMod11(codigoDeBarras.toString()));

		String result = codigoDeBarras.toString();

		if (result.length() != 44) {
			throw new CriacaoBoletoException(
					"Erro na geração do código de barras. Número de digitos diferente de 44. Verifique todos os dados."
							+ result.length());
		}

		return result;
	}

	@Override
	public String getNumeroFormatadoComDigito() {
		return NUMERO_CAIXA + "-" + DIGITO_CAIXA;
	}

	@Override
	public String getCarteiraDoEmissorFormatado(Emissor emissor) {
		return String.format("%02d", emissor.getCarteira());
	}

	@Override
	public String getContaCorrenteDoEmissorFormatado(Emissor emissor) {
		return String.format("%05d", emissor.getContaCorrente());
	}

	public String getCodigoFornecidoPelaAgenciaFormatado(Emissor emissor) {
		return String.format("%08d", emissor.getCodigoFornecidoPelaAgencia());
	}

	public String getCodigoOperacaoFormatado(Emissor emissor) {
		return String.format("%03d", emissor.getCodigoOperacao());
	}

	@Override
	public URL getImage() {
		return getClass().getResource(String.format("/br/com/caelum/stella/boleto/img/%s.png", getNumeroFormatado()));
	}

	@Override
	public String getNossoNumeroDoEmissorFormatado(Emissor emissor) {
		int length = 10 - (emissor.getCarteira() / 10);
		return String.format("%0" + (length - 1) + "d", emissor.getNossoNumero());
	}

	@Override
	public String getNumeroFormatado() {
		return NUMERO_CAIXA;
	}

}
