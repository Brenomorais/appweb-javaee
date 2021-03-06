package br.com.brenomorais.usuario.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.brenomorais.repository.PessoaRepository;
import br.com.brenomorais.repository.filter.PessoaFilter;
import br.com.brenomorais.repository.model.PessoaModel;
 
@Named(value="consultarPessoaController")
@ViewScoped
public class ConsultarPessoaController implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	@Inject transient
	private PessoaModel pessoaModel;
 
	@Produces 
	private List<PessoaModel> pessoas;
 
	@Inject transient
	private PessoaRepository pessoaRepository;
	
	private PessoaFilter filtro;
	
	private List<PessoaModel> pessoasFiltradas;
	
	public void pesquisar() {
		pessoas = pessoaRepository.filtrados(filtro);
	}
		
	public ConsultarPessoaController() {
		filtro = new PessoaFilter();
	}
	
	/***
	 * CARREGA LISTA DE PESSOAS NA INICIALIZA��O 
	 */
	
	@PostConstruct
	public void init(){ 
		//RETORNAR AS PESSOAS CADASTRADAS
		this.pessoas = pessoaRepository.GetPessoas();
	}	
	
	/***
	 * CARREGA INFORMA��ES DE UMA PESSOA PARA SER EDITADA
	 * @param pessoaModel
	 */
	
	public void Editar(PessoaModel pessoaModel){
 
		/*PEGA APENAS A PRIMEIRA LETRA DO SEXO PARA SETAR NO CAMPO(M OU F)*/
		pessoaModel.setSexo(pessoaModel.getSexo().substring(0, 1));
 
		this.pessoaModel = pessoaModel; 
	}	
	
	
 
	/***
	 * ATUALIZA O REGISTRO QUE FOI ALTERADO
	 */
	
	public void AlterarRegistro(){ 
		this.pessoaRepository.AlterarRegistro(this.pessoaModel);	 
 
		/*RECARREGA OS REGISTROS*/
		this.init();
	}
	
	/***
	 * EXCLUINDO UM REGISTRO
	 * @param pessoaModel
	 */
	public void ExcluirPessoa(PessoaModel pessoaModel){
 
		//EXCLUI A PESSOA DO BANCO DE DADOS
		this.pessoaRepository.ExcluirRegistro(pessoaModel.getCodigo());
 
		//REMOVENDO A PESSOA DA LISTA
		//ASSIM QUE � A PESSOA � REMOVIDA DA LISTA O DATATABLE � ATUALIZADO
		this.pessoas.remove(pessoaModel);
 
	}
 
	public List<PessoaModel> getPessoas() {
		return pessoas;
	}
	public void setPessoas(List<PessoaModel> pessoas) {
		this.pessoas = pessoas;
	}		
	public PessoaModel getPessoaModel() {
		return pessoaModel;
	}
	public void setPessoaModel(PessoaModel pessoaModel) {
		this.pessoaModel = pessoaModel;
	} 
	public PessoaFilter getFiltro() {
		return filtro;
	}
	public List<PessoaModel> getPessoasFiltradas() {
		return pessoasFiltradas;
	}
}