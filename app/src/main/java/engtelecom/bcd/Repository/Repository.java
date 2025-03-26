package engtelecom.bcd.Repository;

import java.util.List;

/**
 * Interface para classes que implementam repositórios de entidades.
 * 
 * @param <T> Tipo da entidade.
 */
public interface Repository<T> {

    /**
     * Insere uma nova entidade no repositório.
     * 
     * @param entity Entidade a ser inserida.
     * @return true se a inserção for bem-sucedida.
     */
    boolean insert(T entity);

    /**
     * Busca uma entidade pelo ID.
     * 
     * @param id Identificador único.
     * @return A entidade correspondente ou null se não encontrada.
     */
    T selectById(String id);

    /**
     * Retorna todas as entidades do repositório.
     * 
     * @return Lista com todas as entidades.
     */
    List<T> selectAll();

    /**
     * Verifica se uma entidade com o ID existe.
     * 
     * @param id Identificador único.
     * @return true se a entidade existir.
     */
    boolean exists(String id);

    /**
     * Remove uma entidade pelo ID.
     * 
     * @param id Identificador único.
     * @return true se a entidade foi removida com sucesso.
     */
    boolean deleteById(String id);

    /**
     * Atualiza o valor de um campo específico de uma entidade.
     * 
     * @param id ID da entidade.
     * @param fieldName Nome do campo a ser alterado.
     * @param newValue Novo valor.
     * @return true se a atualização for bem-sucedida.
     */
    boolean updateField(String id, String fieldName, String newValue);
}
