package repository;

import model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
    // A mágica acontece aqui!
    // Todos os métodos de CRUD básicos (save, findById, findAll, deleteById, etc.)
    // já estão disponíveis automaticamente por causa do JpaRepository.


    /**
     * Verifica se um contato com o e-mail fornecido já existe no banco de dados.
     * O Spring Data JPA cria a consulta SQL automaticamente com base no nome do método.
     * @param email O e-mail a ser verificado.
     * @return true se o e-mail já existir, false caso contrário.
     */
    boolean existsByemail(String email);


    /**
     * Busca um contato pelo seu endereço de e-mail.
     * Retorna um Optional, que é uma forma segura de lidar com a possibilidade
     * de o contato não ser encontrado (evitando NullPointerException).
     * @param email O e-mail do contato a ser buscado.
     * @return um Optional contendo o Contato se encontrado, ou um Optional vazio.
     */
    Optional<Contato> findByemail(String email);

    Optional<Contato> findById(Long id);

    Optional<Contato> existsContatoByID(Long id);
}
