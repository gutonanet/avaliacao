package br.com.augustolemes.avaliacao.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.PosicaoEnum;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.service.ProvaWordService;

@Service
public class ProvaWordServiceImpl implements ProvaWordService {

	   @Value( "${imagem.url}" )
	    private String imagemURL;
	
	public void readDocxFile(ProvaDTO prova, String template, HttpServletRequest request, HttpServletResponse response) throws Exception{
			Resource resource = new ClassPathResource(template);
			File file = resource.getFile();
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			List<QuestaoDTO> questoes = prova.getQuestoes();
			preencheProva(document, "TURMA", "TURMA: "+prova.getTurma(),null);
			for (int i = 1; i <= questoes.size(); i++) {
				String questaoLocalizar = "QT" + i;
				String questaoLocalizar2 = "HB" + i;
				QuestaoDTO questao = questoes.get(i - 1);
				preencheProva(document, questaoLocalizar2, questao.getHabilidade(),null);
				//preencheProva(document, questaoLocalizar3, questao.getContinuacaoQuestao(),null);
				boolean atualizou = preencheProva(document, questaoLocalizar, questao.getQuestao(), null);
				if (atualizou) {
					
					adicionarImagens(document,questao, i, request.getRequestURI());
					adicionarRespostas(document,questao, i);


				}

			}
			 response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		     response.setHeader("Content-Disposition", "attachment; filename="+ prova.getTurma()+"_" +prova.getMateria().getNome() + ".docx");
			//removeTabela(document, "QUESTAO");
			//removeTabela(document, "CONTINUACAOQUESTAO");
			//removeLinha(document, "REMOVER");

			document.write(response.getOutputStream());
			document.close();
			fis.close();
		

	}

	private void adicionarRespostas(XWPFDocument document, QuestaoDTO questao, Integer questaoIndice) throws InvalidFormatException, IOException {
		List<RespostaDTO> respostas = questao.getRespostasFormatted();
		for (int ii = 1; ii <= respostas.size(); ii++) {
			RespostaDTO resposta = respostas.get(ii - 1);
			String respostaLocalizar = "ALT"+(questaoIndice+"") + (ii+"");
			preencheProva(document, respostaLocalizar, resposta.getRespostaFormatted(ii));
		}
	}
	
	private void adicionarImagens(XWPFDocument document, QuestaoDTO questao, Integer questaoIndice, String url) throws InvalidFormatException, IOException {
		List<ImagemDTO> imagens = questao.getImagens();
		List<ImagemDTO> superiores = new ArrayList<>();
		List<ImagemDTO> inferiores = new ArrayList<>();

		
		for (int ii = 1; ii <= 5; ii++) {
			if(ii <= imagens.size() ) {
				ImagemDTO img = imagens.get(ii - 1);
				if(img.getPosicao() == PosicaoEnum.SUPERIOR.getId()) {
					img.setUrl(url);
					superiores.add(img);
				}
			}else {
				ImagemDTO img = new ImagemDTO();
				img.setLegenda("REMOVER");
				superiores.add(img);
			}
		}
		
		for (int ii = 1; ii <= 5; ii++) {
			if(ii <= imagens.size() ) {
				ImagemDTO img = imagens.get(ii - 1);
				if(img.getPosicao() == PosicaoEnum.INFERIOR.getId()) {
					img.setUrl(url);
					inferiores.add(img);
				}
			}else {
				ImagemDTO img = new ImagemDTO();
				img.setLegenda("REMOVER");
				inferiores.add(img);
			}
		}
		
		for (int ii = 1; ii <= superiores.size(); ii++) {
			ImagemDTO imagem = superiores.get(ii - 1);
			String textoLocalizar = "SP"+(questaoIndice+"") + (ii+"");
			preencheProva(document, textoLocalizar, imagem.getLegenda(),imagem );
		}
		
		for (int ii = 1; ii <= inferiores.size(); ii++) {
			ImagemDTO imagem = inferiores.get(ii - 1);
			String textoLocalizar = "IF"+(questaoIndice+"") + (ii+"");
			preencheProva(document, textoLocalizar, imagem.getLegenda(),imagem );
		}
		
	}
	
	public boolean addImage(XWPFRun run, ImagemDTO imagem) throws IOException, InvalidFormatException{
	    String imageUrl = imagemURL+"/"+imagem.getId();
	    URL url = new URL(imageUrl);
	    InputStream is = url.openStream();
		run.addBreak();
		run.addPicture(is, getTypeImage(imagem.getNome()), imagem.getNome(), Units.toEMU(70), Units.toEMU(70)); // 200x200
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
		
		return -1;
		
		
	}
	
	/*
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
	*/

	public boolean preencheProva(XWPFDocument document, String textoLocalizar, String textoSubstituir) throws IOException, InvalidFormatException{
		return preencheProva(document, textoLocalizar, textoSubstituir, null);
	}

	public boolean preencheProva(XWPFDocument document, String textoLocalizar, String textoSubstituir,
			ImagemDTO imagem) throws IOException, InvalidFormatException{
		for (XWPFTable tbl : document.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
							if (text != null && text.contains(textoLocalizar)) {
								text = text.replace(textoLocalizar, textoSubstituir);
								r.setText(text, 0);
								if(imagem != null && imagem.getId()!= null) {
									addImage(r, imagem);
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
