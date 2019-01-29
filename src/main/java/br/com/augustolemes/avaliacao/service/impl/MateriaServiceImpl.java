package br.com.augustolemes.avaliacao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.repository.MateriaRepository;
import br.com.augustolemes.avaliacao.service.MateriaService;

@Service
public class MateriaServiceImpl implements MateriaService{
	
	@Autowired
	private MateriaRepository materiaRepository;
	
	public List<MateriaDTO> findAll(){
		Iterable<MateriaDTO> iterable =  materiaRepository.findAll();
		return returnAll(iterable);
	}
	
	public MateriaDTO findById(Long id) {
		return materiaRepository.findById(id);
	}

	private List<MateriaDTO> returnAll(Iterable<MateriaDTO> iterable){
		List<MateriaDTO> lista = new ArrayList<>();
		iterable.forEach(lista::add);
		if(lista.isEmpty() || lista.size() < 5) {
			populaMaterias(lista);
			return returnAll(materiaRepository.findAll());
		}
		
		return lista;
	}
	
	
	private void populaMaterias(List<MateriaDTO> lista) {
		/*
		 * insert into materiadto values (1, 'Português');
insert into materiadto values (2, 'História');
insert into materiadto values (3, 'Física');
insert into materiadto values (4, 'Química');
insert into materiadto values (5, 'Filosofia');
insert into materiadto values (6, 'Sociologia');
insert into materiadto values (7, 'Geografia');
insert into materiadto values (8, 'Arte');
insert into materiadto values (9, 'Ed. Física');
insert into materiadto values (10, 'Inglês');
		 */
		
		if(lista.isEmpty()) {
			MateriaDTO portugues = new MateriaDTO();
			portugues.setId(1l);
			portugues.setNome("Português");
			materiaRepository.save(portugues);
			MateriaDTO historia = new MateriaDTO();
			historia.setId(2l);
			historia.setNome("História");
			materiaRepository.save(historia);
		}
		//materiaRepository.deleteAll();
		if(lista.size() < 5) {
			MateriaDTO fisica = new MateriaDTO();
			fisica.setId(3l);
			fisica.setNome("Física");
			materiaRepository.save(fisica);
			MateriaDTO qumica = new MateriaDTO();
			qumica.setId(4l);
			qumica.setNome("Química");
			materiaRepository.save(qumica);
			
			MateriaDTO filosofia = new MateriaDTO();
			filosofia.setId(5l);
			filosofia.setNome("Filosofia");
			materiaRepository.save(filosofia);
			MateriaDTO sociologia = new MateriaDTO();
			sociologia.setId(6l);
			sociologia.setNome("Sociologia");
			materiaRepository.save(sociologia);
			
			MateriaDTO geografia = new MateriaDTO();
			geografia.setId(7l);
			geografia.setNome("Geografia");
			materiaRepository.save(geografia);
			MateriaDTO arte = new MateriaDTO();
			arte.setId(8l);
			arte.setNome("Arte");
			materiaRepository.save(arte);
			
			MateriaDTO edFisica = new MateriaDTO();
			edFisica.setId(9l);
			edFisica.setNome("Ed. Física");
			materiaRepository.save(edFisica);
			MateriaDTO ingles = new MateriaDTO();
			ingles.setId(10l);
			ingles.setNome("Inglês");
			materiaRepository.save(ingles);
			
			MateriaDTO matematica = new MateriaDTO();
			matematica.setId(11l);
			matematica.setNome("Matemática");
			materiaRepository.save(matematica);
			MateriaDTO biologia = new MateriaDTO();
			biologia.setId(11l);
			biologia.setNome("Biologia");
			materiaRepository.save(matematica);
			
		}


	}
	
}
