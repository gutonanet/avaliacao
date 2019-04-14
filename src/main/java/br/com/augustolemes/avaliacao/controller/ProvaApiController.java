package br.com.augustolemes.avaliacao.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.augustolemes.avaliacao.dto.DadosProvaTO;
import br.com.augustolemes.avaliacao.dto.DadosQuestaoTO;
import br.com.augustolemes.avaliacao.dto.DadosRespostaTO;
import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.PosicaoEnum;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.dto.TipoProvaEnum;
import br.com.augustolemes.avaliacao.dto.TipoProvaTO;
import br.com.augustolemes.avaliacao.exception.BusinessException;
import br.com.augustolemes.avaliacao.service.ImagemService;
import br.com.augustolemes.avaliacao.service.MateriaService;
import br.com.augustolemes.avaliacao.service.ProvaService;
import br.com.augustolemes.avaliacao.service.ProvaWordService;
import br.com.augustolemes.avaliacao.service.QuestaoService;
import br.com.augustolemes.avaliacao.service.RespostaService;

@RestController
@RequestMapping("/api")
public class ProvaApiController {

	@Autowired
	private MateriaService materiaService;
	
	@Autowired
	private ProvaService provaService;

	@Autowired 
	private QuestaoService questaoService;
	
	@Autowired
	private RespostaService respostaService;
	
	
	@Autowired
	private ImagemService imagemService;
	
	@GetMapping("/materias")
	public Iterable<MateriaDTO> listaMaterias(){
		return materiaService.findAll();
	}
	
	@GetMapping("/tiposProva")
	public List<TipoProvaTO> tiposProva(){
		List<TipoProvaTO> lista = new ArrayList<>();
		for(TipoProvaEnum e:TipoProvaEnum.values()) {
			lista.add(e.getTO());
		}
		return lista;
	}
	
	@PostMapping("/findProva")
	public DadosProvaTO findProva(@RequestParam String tipoProva, @RequestParam String materia, @RequestParam String turma, @RequestParam String frase ) throws BusinessException{
		
		TipoProvaEnum tipo = TipoProvaEnum.getNome(tipoProva);
		List<MateriaDTO> materias = materiaService.findByNome(materia);
		DadosProvaTO dados = new DadosProvaTO();
		dados.setFrase(frase);
		dados.setTurma(turma);
		dados.setIdMateria(materias.get(0).getId());
		dados.setIdTipoProva(tipo.getId());
    	
    	String mensagemErro = provaService.validarProva(dados);
    	if(mensagemErro!= null && !"".equals(mensagemErro)) {
    	   	throw new BusinessException(mensagemErro);
    	       		
    	}

    	ProvaDTO prova = provaService.save(dados);
    	dados.setIdProva(prova.getId());
    	
		return dados;
	}
	
	@PostMapping("/listarQuestoes")
	public List<DadosQuestaoTO> listarQuestoes(@RequestParam String idProva){
		ProvaDTO p = new ProvaDTO();
		Long id = Long.valueOf(idProva);
		p.setId(id);
		List<QuestaoDTO> listaTemp = questaoService.findByProva(p);
		List<DadosQuestaoTO> lista = new ArrayList<>();
		for(QuestaoDTO q: listaTemp) {
			DadosQuestaoTO d = questaoService.converter(q);
			lista.add(d);		
		}
		return lista;
	}
	
	@PostMapping("/salvarQuestao")
	public DadosQuestaoTO questaoSalvar(@RequestParam String idQuestao, @RequestParam String questao, @RequestParam String habilidade, @RequestParam String idProva) throws BusinessException{
		Long idP = Long.valueOf(idProva);
		Long idQ = Long.valueOf(idQuestao);
    	QuestaoDTO questaoDTO = null;
    	QuestaoDTO q = null;
    			
    	if(idQ != null && !idQ.equals(0L)) {
    		questaoDTO = questaoService.findById(idQ);
    	}else {
    		questaoDTO = new QuestaoDTO();
    		ProvaDTO prova = new ProvaDTO();
    		prova.setId(idP);
    		questaoDTO.setProva(prova);
    	}
    	questaoDTO.setQuestao(questao);
    	questaoDTO.setHabilidade(habilidade);
    	String mensagemErro = questaoService.validarQuestionario(questaoDTO);
    	if(mensagemErro != null && !"".equals(mensagemErro)) {
    		throw new BusinessException(mensagemErro);
    	}else {
    		q = questaoService.save(questaoDTO);	
    	}
  
    	DadosQuestaoTO qt = questaoService.converter(q);
    	return qt;
    	
	}
	
	@PostMapping("/listarRespostas")
	public List<DadosRespostaTO> listarRespostas(@RequestParam String idQuestao){
		QuestaoDTO q = new QuestaoDTO();
		Long id = Long.valueOf(idQuestao);
		q.setId(id);
		List<RespostaDTO> listaTemp = respostaService.findByQuestao(q);
		List<DadosRespostaTO> lista = new ArrayList<>();
		for(RespostaDTO r: listaTemp) {
			DadosRespostaTO dr = respostaService.converter(r);
			lista.add(dr);		
		}
		return lista;
	}

	@PostMapping("/salvarResposta")
	public DadosRespostaTO respostaSalvar(@RequestParam String idQuestao, @RequestParam String resposta,  @RequestParam String idResposta) throws BusinessException{
		Long idR = Long.valueOf(idResposta);
		Long idQ = Long.valueOf(idQuestao);
    	RespostaDTO respostaDTO = null;
    	RespostaDTO r = null;
    			
    	if(idR != null && !idR.equals(0L)) {
    		respostaDTO = respostaService.findById(idR);
    	}else {
    		respostaDTO = new RespostaDTO();
    		QuestaoDTO questao = new QuestaoDTO();
    		questao.setId(idQ);
    		respostaDTO.setQuestao(questao);
    	}
    	respostaDTO.setResposta(resposta);
    	String mensagemErro = respostaService.validarResposta(respostaDTO);
    	if(mensagemErro != null && !"".equals(mensagemErro)) {
    		throw new BusinessException(mensagemErro);
    	}else {
    		r = respostaService.save(respostaDTO);	
    	}
  
    	DadosRespostaTO dr = respostaService.converter(r);
    	return dr;
    	
	}
	
	
    @GetMapping("/questaoDelete/{id}")
    public void questaoDelete(@PathVariable Long id) {
    	QuestaoDTO questao = questaoService.findById(id);
    	questaoService.deleteQuestao(questao);
    	
    }

    
    
    @GetMapping("/respostaDelete/{id}")
    public void respostaDelete(@PathVariable Long id) {
    	RespostaDTO resposta = respostaService.findById(id);
    	respostaService.delete(resposta);
    }   

	
}
