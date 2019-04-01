package br.com.augustolemes.avaliacao.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.augustolemes.avaliacao.dto.DadosQuestaoTO;
import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.repository.QuestaoRepository;
import br.com.augustolemes.avaliacao.service.ImagemService;
import br.com.augustolemes.avaliacao.service.QuestaoService;
import br.com.augustolemes.avaliacao.service.RespostaService;

@Service
public class QuestaoServiceImpl implements QuestaoService{
	
	@Autowired
	private QuestaoRepository questaoRepository;
	
	@Autowired
	private RespostaService respostaService;
	
	private ImagemService imagemService;
	
	public List<QuestaoDTO> findByProva(ProvaDTO prova){
		return questaoRepository.findByProva(prova);
		
	}
	
	public QuestaoDTO save(QuestaoDTO questao) {
		return questaoRepository.save(questao);
	}

	public QuestaoDTO findById(Long id) {
		return questaoRepository.findOne(id);
	}
	
	public void deleteQuestao(QuestaoDTO questao) {
		List<RespostaDTO> lista = respostaService.findByQuestao(questao);
		for(RespostaDTO r:lista) {
			respostaService.delete(r);
		}
		
		questaoRepository.delete(questao);
	}
	
	public DadosQuestaoTO converter(QuestaoDTO questao) {
		DadosQuestaoTO dados = new DadosQuestaoTO();
		dados.setIdQuestao(questao.getId());
		dados.setHabilidade(questao.getHabilidade());
		dados.setQuestao(questao.getQuestao());
		dados.setIdProva(questao.getProva().getId());
		//dados.setContinuacaoQuestao(questao.getContinuacaoQuestao());
		return dados;
	}
	
	public QuestaoDTO converter(DadosQuestaoTO dados, ProvaDTO prova) {
		QuestaoDTO questao = new QuestaoDTO();
		questao.setId(dados.getIdQuestao());
		questao.setQuestao(dados.getQuestao());
		questao.setHabilidade(dados.getHabilidade());
		questao.setTipoQuestao(dados.getTipoQuestao());
		//questao.setContinuacaoQuestao(dados.getContinuacaoQuestao());
		questao.setProva(prova);
		return questao;

	}
	
    
    public String validarQuestionario(DadosQuestaoTO questao) {
    	String retorno = null;
    	if(questao.getHabilidade() == null || "".equals(questao.getHabilidade())) {
    		retorno = " O Campo habilidade deve ser preenchido.";
    	}
    	if(questao.getQuestao() == null || "".equals(questao.getQuestao())) {
    		retorno += " O Campo Questão deve ser preenchido.";
    	}
    	return retorno;
    }
    
    
    public String singleFileUpload(MultipartFile file, String legenda, Integer posicao, QuestaoDTO questaoDTO, String mensagemErro) throws IOException { 	
		List<ImagemDTO> imagens = imagemService.findByQuestao(questaoDTO);
		
		if(imagens.size()>=5) {
			mensagemErro+=" Cada questão só pode ter no máximo 5 imagens.";
	    	return mensagemErro;
		}else {
	    	ImagemDTO img = new ImagemDTO();
	 		byte[] imagem = file.getBytes();
	        img.setImagem(imagem);
	        img.setNome(file.getOriginalFilename());
	        img.setLegenda(legenda);
	        img.setPosicao(posicao);
	        img.setQuestao(questaoDTO);
	        imagemService.save(img);
		}         

		
		
		return mensagemErro;
     }
	
    public String salvarRespostas(String mensagemErro, QuestaoDTO questaoDTO, String respostaTexto) {
		List<RespostaDTO> respostas = respostaService.findByQuestao(questaoDTO);
		
		if(respostas.size()>=5) {
			mensagemErro+=" Cada questão só pode ter no máximo 5 respostas.";
	    	return mensagemErro;
			
		}else {
    		RespostaDTO resposta = new RespostaDTO();
    		resposta.setQuestao(questaoDTO);
    		resposta.setResposta(respostaTexto);
    		respostaService.save(resposta);
			
		}

		return mensagemErro;
	}
    
	public QuestaoRepository getQuestaoRepository() {
		return questaoRepository;
	}

	public void setQuestaoRepository(QuestaoRepository questaoRepository) {
		this.questaoRepository = questaoRepository;
	}

	
	
}
