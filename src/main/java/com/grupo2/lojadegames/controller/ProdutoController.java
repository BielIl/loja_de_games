package com.grupo2.lojadegames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.grupo2.lojadegames.model.Produto;
import com.grupo2.lojadegames.repository.ProdutoRepository;

@RestController
@RequestMapping("/produto")
@CrossOrigin("*")
public class ProdutoController {
	
	@Autowired
	ProdutoRepository repository;
	
	//BUSCAS
	@GetMapping("/all")
	public ResponseEntity<List<Produto>> getAll(){
		List<Produto> list = repository.findAll();
		
		if(list.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Lista Vazia!");
		} else {
			return ResponseEntity.status(200).body(list);
		}
	}
	
	@GetMapping("/id/{id_produto}")
	public ResponseEntity<Produto> getById (@PathVariable(value = "id_produto") Long id){
		return repository.findById(id).map(resp -> ResponseEntity.status(200).body(resp))
				.orElseGet(() -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id não encontrado!");
				});
	}
	
	@GetMapping("/nome/{nome_produto}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable(value = "nome_produto") String nome){
		List<Produto> list = repository.findAllByNomeContainingIgnoreCase(nome);
		
		if(list.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nome não encotrado!");
		} else {
			return ResponseEntity.status(200).body(list);
		}
	}
	
	
	//SAVE
	@PostMapping("/save")
	public ResponseEntity<Produto> saveProduto(@RequestBody Produto produto){
		return ResponseEntity.status(201).body(repository.save(produto));
	} 
	
	
	//UPDATE
	@PutMapping("/update")
	public ResponseEntity<Produto> updateProduto(@RequestBody Produto produto){
		return repository.findById(produto.getId()).map(resp -> ResponseEntity.status(200).body(repository.save(produto)))
				.orElseGet(() -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id não encontrado!");
				});
	}
	
	//DELETE
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		Optional<Produto> optional = repository.findById(id);
			if (optional.isPresent()) {
				
				repository.deleteById(id);
				return ResponseEntity.status(200).build();
				
			} else {
				
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id não encontrado!");
				
			}
	}
}