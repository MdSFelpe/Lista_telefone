package org.example.lista_telefone_sm.controller;


import jakarta.validation.Valid;
import org.example.lista_telefone_sm.model.Contato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.lista_telefone_sm.service.ContatoService;

import java.util.List;

@RestController // Anotação que combina @Controller e @ResponseBody, simplificando a criação de APIs REST.
@RequestMapping("/contatos")// Define que todos os endpoints nesta classe começarão com a URL /contatos.
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    // Endpoint para CRIAR um novo contato (HTTP POST)
    @PostMapping
    public ResponseEntity<Contato> criarContato(@Valid @RequestBody Contato contato) {
        Contato novoContato = contatoService.criarcontato(contato);
        return new ResponseEntity<>(novoContato, HttpStatus.CREATED); // Retorna o objeto criado e o status 201 Created.
    }

    // Endpoint para LER (listar) todos os contatos (HTTP GET)
    @GetMapping
    public ResponseEntity<List<Contato>> listarTodos() {
        List<Contato> contatos = contatoService.listartodos();
        return ResponseEntity.ok(contatos); // Retorna a lista e o status 200 OK.
    }

    // Endpoint para LER (buscar) um contato pelo seu ID (HTTP GET)
    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarPorId(@PathVariable Long id) {
        Contato contato = contatoService.buscarPorId(id);
        return ResponseEntity.ok(contato);
    }

    // Endpoint para ATUALIZAR um contato existente (HTTP PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Contato> updateContact(@PathVariable Long id, @Valid @RequestBody Contato contatoAtualizado) {
        Contato contato = contatoService.updateContact(id, contatoAtualizado);
        return ResponseEntity.ok(contato);
    }

    // Endpoint para DELETAR um contato (HTTP DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContato(@PathVariable Long id) {
        contatoService.deletarContato(id);
        return ResponseEntity.noContent().build(); // Retorna uma resposta vazia com o status 204 No Content.
    }

}
