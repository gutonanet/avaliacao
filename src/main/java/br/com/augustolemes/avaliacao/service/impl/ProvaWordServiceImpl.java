package br.com.augustolemes.avaliacao.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.service.ProvaWordService;

@Service
public class ProvaWordServiceImpl implements ProvaWordService {

	@Value("${prova.caminho}")
	private String caminhoProva;

	@Value("${imagem.caminho}")
	private String caminhoImagem;

	public void readDocxFile(ProvaDTO prova, String fileName) {
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			List<QuestaoDTO> questoes = prova.getQuestoes();
			preencheProva(document, "TURMA", "TURMA: "+prova.getTurma(),null);
			for (int i = 1; i <= questoes.size(); i++) {
				String questaoLocalizar = "QUESTAO" + i;
				String questaoLocalizar2 = "HABILIDADE" + i;
				QuestaoDTO questao = questoes.get(i - 1);
				preencheProva(document, questaoLocalizar2, questao.getHabilidade(),null);
				boolean atualizou = preencheProva(document, questaoLocalizar, questao.getQuestao(), questao.getId());
				if (atualizou) {
					List<RespostaDTO> respostas = questao.getRespostasFormatted();
					for (int ii = 1; ii <= respostas.size(); ii++) {
						RespostaDTO resposta = respostas.get(ii - 1);
						String respostaLocalizar = "ALTERNATIVA" + ii;
						preencheProva(document, respostaLocalizar, resposta.getRespostaFormatted(ii));
					}

				}

			}
			removeTabela(document, "QUESTAO");
			removeLinha(document, "REMOVER");

			document.write(
					new FileOutputStream(caminhoProva + prova.getTurma()+"_" +prova.getMateria().getNome() + ".docx"));
			document.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean addImage(XWPFRun run, Long codigoQuestao) throws IOException, InvalidFormatException{
		File arquivo = locateFile(codigoQuestao);
		if(arquivo == null) {
			return false;
		}
		FileInputStream is = new FileInputStream(arquivo);
		run.addBreak();
		run.addPicture(is, getTypeImage(arquivo.getName()), arquivo.getAbsolutePath(), Units.toEMU(200), Units.toEMU(200)); // 200x200
																											// pixels
		is.close();
		
		return true;
	}

	private int getTypeImage(String filename) {
		if(filename.toLowerCase().contains("jpg")) {
			return XWPFDocument.PICTURE_TYPE_JPEG;
		}
		
		if(filename.toLowerCase().contains("png")) {
			return XWPFDocument.PICTURE_TYPE_PNG;
		}
		
		if(filename.toLowerCase().contains("bmp")) {
			return XWPFDocument.PICTURE_TYPE_BMP;
		}
		
		return -1;
		
		
	}
	
	private File locateFile(Long codigoQuestao) {
		File[] files = new File(caminhoImagem).listFiles();
		String partName = codigoQuestao+"__";
		for(File arquivo: files) {
			if(arquivo.getName().contains(partName) == true) {
				return arquivo;	
			}
			
		}
		return null;
	}

	public boolean preencheProva(XWPFDocument document, String textoLocalizar, String textoSubstituir) throws IOException, InvalidFormatException{
		return preencheProva(document, textoLocalizar, textoSubstituir, null);
	}

	public boolean preencheProva(XWPFDocument document, String textoLocalizar, String textoSubstituir,
			Long codigoQuestao) throws IOException, InvalidFormatException{
		for (XWPFTable tbl : document.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
							if (text != null && text.contains(textoLocalizar)) {
								text = text.replace(textoLocalizar, textoSubstituir);
								r.setText(text, 0);
								if(codigoQuestao != null) {
									addImage(r, codigoQuestao);
								}
								return true;
							}
						}
					}
				}
			}
		}
		return false;

	}

	public void removeTabela(XWPFDocument document, String textoLocalizar) {
		List<Integer> listaRemover = new ArrayList<>();
		for (XWPFTable tbl : document.getTables()) {
			boolean removeRow = false;
			for (int i = 0; i < tbl.getRows().size(); i++) {
				XWPFTableRow row = tbl.getRows().get(i);
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
							if (text != null && text.contains(textoLocalizar)) {
								removeRow = true;
							}
						}

					}
				}
				if (removeRow) {
					listaRemover.add(i);
				}
			}

			for (int i = listaRemover.size() - 1; i >= 0; i--) {
				Integer indice = listaRemover.get(i);
				tbl.removeRow(indice);
			}

		}
		if (listaRemover.size() > 0)
			removeTabela(document, textoLocalizar);
	}

	public void removeLinha(XWPFDocument document, String textoLocalizar) {
		for (XWPFTable tbl : document.getTables()) {
			for (int i = 0; i < tbl.getRows().size(); i++) {
				XWPFTableRow row = tbl.getRows().get(i);
				

				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						boolean encontrou = false;
						int ii = 0;
						for (XWPFRun r : p.getRuns()) {							
							String text = r.getText(0);
							if (text != null && text.contains(textoLocalizar)) {
								r.setText("",0);
								encontrou = true;
								break;
							}
							ii++;
						}
						if(encontrou) {
							p.removeRun(ii);						}

					}
					
		
				}
				

			}


		}
		
	}	
	
	/*
	 * public static void main(String[] args) { readDocxFile(
	 * "/home/gutonanet/desenvolvimento/Documentos/Avaliacao/Layout_avaEF_teste.docx"
	 * ); }
	 */

}
