package br.com.augustolemes.avaliacao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.augustolemes.avaliacao.dto.DadosProvaTO;
import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.repository.ProvaRepository;
import br.com.augustolemes.avaliacao.service.ImagemService;
import br.com.augustolemes.avaliacao.service.MateriaService;
import br.com.augustolemes.avaliacao.service.ProvaService;
import br.com.augustolemes.avaliacao.service.QuestaoService;
import br.com.augustolemes.avaliacao.service.RespostaService;

@Service
public class ProvaServiceImpl implements ProvaService {

	@Autowired
	private ProvaRepository provaRepository;
	
	@Autowired
	private QuestaoService questaoService;
	
	@Autowired
	private MateriaService materiaService;
	
	@Autowired
	private RespostaService respostaService; 
	
	@Autowired
	private ImagemService imagemService;
	
	 public ProvaDTO findById(Long id) {
		 return provaRepository.findById(id);
	 }
	
	public ProvaDTO findProva(DadosProvaTO dados){
		List<ProvaDTO> lista =  provaRepository.findByTurma(dados.getTurma());
    	if(lista!= null && !lista.isEmpty()) {
    		for(ProvaDTO prova:lista) {
    			if(prova.getMateria().getId().equals(dados.getIdMateria())&&
    				prova.getTipoProva().equals(dados.getIdTipoProva())	) {
    		    	List<QuestaoDTO> questoes = questaoService.findByProva(prova);
    		    	prova.setQuestoes(questoes);
    		    	return prova;
    			}
    		}
    	}
    	
    	ProvaDTO prova = save(dados);
    	
		return prova;
	}
	
	public ProvaDTO findProvaAllData(Long id) {
		ProvaDTO prova = provaRepository.findById(id);
		List<QuestaoDTO> questoes = questaoService.findByProva(prova);
		for(QuestaoDTO questao:questoes) {
			List<RespostaDTO> respostas = respostaService.findByQuestao(questao);
			if(respostas!= null && !respostas.isEmpty()) {
				questao.setRespostas(respostas);
			}
			
			List<ImagemDTO> imagens = imagemService.findByQuestao(questao);
			if(imagens != null && !imagens.isEmpty()) {
				questao.setImagens(imagens);
			}
		}
		prova.setQuestoes(questoes);
		
		return prova;
		
	}
	
	public DadosProvaTO converter(ProvaDTO prova) {
		DadosProvaTO dados = new DadosProvaTO();
		dados.setIdProva(prova.getId());
		//dados.setAnoLetivo(prova.getAnoLetivo());
		dados.setIdMateria(prova.getMateria().getId());
		dados.setIdTipoProva(prova.getTipoProva());
		dados.setTurma(prova.getTurma());
		return dados;
	}

	public ProvaDTO converter(DadosProvaTO dados) {
		ProvaDTO prova = new ProvaDTO();
		prova.setId(dados.getIdProva());
		prova.setTipoProva(dados.getIdTipoProva());
		prova.setTurma(dados.getTurma());
		MateriaDTO materia = materiaService.findById(dados.getIdMateria());
		prova.setMateria(materia);
		return prova;
	}
	
	
	private ProvaDTO save(DadosProvaTO item) {
		ProvaDTO prova = new ProvaDTO();
		//prova.setAnoLetivo(item.getAnoLetivo());
		prova.setTipoProva(item.getIdTipoProva());
		MateriaDTO materia = new MateriaDTO();
		materia.setId(item.getIdMateria());
		prova.setMateria(materia);
		prova.setTurma(item.getTurma());
		return provaRepository.save(prova);
	}
	
	
}
