package br.com.brenomorais.usuario.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.brenomorais.repository.PessoaRepository;
import br.com.brenomorais.repository.model.PessoaModel;;

@Named(value = "exportarRegistrosExcelController")
@RequestScoped
public class ExportarRegistrosExcelController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	transient PessoaRepository pessoaRepository;

	private StreamedContent arquivoDownload;

	private HSSFWorkbook workbook;

	/***
	 * REALIZA O DOWNLOAD DO ARQUIVO XML
	 * 
	 * @return
	 */
	public StreamedContent getArquivoDownload() {

		this.DownlaodArquivoPlanilhaPessoa();

		return arquivoDownload;
	}

	/***
	 * GERANDO ARQUIVO Planilha PARA EXPORTAÇÃO.
	 * 
	 * @return
	 */
	private File GerarPlanilhaPessoas() {

		workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("Pessoas");		

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		List<PessoaModel> lista = pessoaRepository.GetPessoas();
		
		int rownum = 0;
		int cellnum = 0;
		
		Cell cell;
		Row row;
		
		// Definindo alguns padroes de layout
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeight((short)400);		

		//Configurando estilos de células (Cores, alinhamento, formatação, etc..)
		HSSFDataFormat numberFormat = workbook.createDataFormat();
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setAlignment(CellStyle.ALIGN_CENTER);
		textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
		numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		// Configurando Header
		row = sheet.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Código");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Nome");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Sexo");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Data Cadastro");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("E-mail");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Endereço");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Origem");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Usuário");
			
		       
		for (PessoaModel pessoa : lista) {

			row = sheet.createRow(rownum++);			
			cellnum = 0;

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(pessoa.getCodigo());
			
			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(pessoa.getNome());
			
			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(pessoa.getSexo());
			
			String dataCadastroFormatada = pessoa.getDataCadastro().format(dateTimeFormatter);

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(dataCadastroFormatada);
			
			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(pessoa.getEmail());			
			
			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(pessoa.getEndereco());
			
			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(pessoa.getOrigemCadastro());
			
			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(pessoa.getUsuarioModel().getUsuario());
		
		} // fim do for
		
		try {	
						 
			/*GERANDO O NOME DO ARQUIVO*/
			String nomeArquivo =  "pessoas_".concat(java.util.UUID.randomUUID().toString()).concat(".xls");
 
			//CAMINHO ONDE VAI SER GERADO O ARQUIVO XML
			File arquivo = new File("cadastro_".concat(nomeArquivo));
 
            FileOutputStream fileOut =  new FileOutputStream(arquivo); 
 
			workbook.write(fileOut);
			
			fileOut.close();
 
			return arquivo;
 
		} catch (Exception ex) {
 
			ex.printStackTrace();
		}
		
		return null;	

	}

	/***
	 * PREPARA O ARQUIVO PARA DOWNLOAD
	 */
	public void DownlaodArquivoPlanilhaPessoa() {

		File arquivoExl = this.GerarPlanilhaPessoas();

		InputStream inputStream;

		try {

			inputStream = new FileInputStream(arquivoExl.getPath());

			arquivoDownload = new DefaultStreamedContent(inputStream, "application/xls", arquivoExl.getName());

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}
}