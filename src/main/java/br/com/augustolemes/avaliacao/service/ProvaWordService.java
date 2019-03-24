package br.com.augustolemes.avaliacao.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.augustolemes.avaliacao.dto.ProvaDTO;

public interface ProvaWordService {
	
	void readDocxFile(ProvaDTO prova, String template, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
