package engtelecom.bcd.Repository;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação de repositório de entidades baseado em arquivos CSV.
 *
 * @param <T> Tipo da entidade.
 */
public class RepositoryCSV<T> implements Repository<T> {
    private final String fileName;
    private final Class<T> type;
    private final String idField;

    public RepositoryCSV(String fileName, Class<T> type, String idField) {
        this.fileName = fileName;
        this.type = type;
        this.idField = idField;
        ensureFileExists();
    }

    /**
     * Garante que o arquivo CSV exista. Se não existir, cria o arquivo com o cabeçalho baseado nos campos da classe.
     */
    private void ensureFileExists() {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(getHeaders());
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException("Erro ao criar arquivo CSV", e);
            }
        }
    }

    /**
     * Retorna a linha de cabeçalho (nomes dos campos) com base na classe da entidade.
     *
     * @return String contendo os nomes dos campos separados por vírgula.
     */
    private String getHeaders() {
        return Arrays.stream(type.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    @Override
    public boolean insert(T entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String entityData = convertEntityToCSV(entity); 
            writer.write(entityData);
            writer.newLine(); 
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    /**
     * Converte uma entidade em uma linha CSV.
     *
     * @param entity A entidade a ser convertida.
     * @return Representação CSV da entidade.
     */
    private String convertEntityToCSV(T entity) {
        StringBuilder csvBuilder = new StringBuilder();
        Field[] fields = entity.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object fieldValue = fields[i].get(entity);
    
                if (fieldValue instanceof byte[]) {
                    fieldValue = Base64.getEncoder().encodeToString((byte[]) fieldValue);
                }
    
                csvBuilder.append(fieldValue != null ? fieldValue : "");
                if (i < fields.length - 1) {
                    csvBuilder.append(",");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return csvBuilder.toString();
    }
    

    @Override
    public T selectById(String id) {
        return selectAll().stream()
                .filter(e -> getFieldValue(e, idField).equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna a primeira entidade que corresponde ao valor de um campo específico.
     *
     * @param fieldName Nome do campo a ser buscado.
     * @param value Valor esperado.
     * @return A primeira entidade que atende à condição ou null.
     */
    public T selectFirstWhere(String fieldName, String value) {
        return selectAll().stream()
            .filter(e -> getFieldValue(e, fieldName).equals(value))
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean exists(String id) {
        return selectById(id) != null;
    }

    @Override
    public boolean deleteById(String id) {
        List<T> entities = selectAll();
        boolean removed = entities.removeIf(e -> getFieldValue(e, idField).equals(id));
        if (removed) {
            writeAll(entities);
        }
        return removed;
    }

    @Override
    public List<T> selectAll() {
        List<T> entities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String header = reader.readLine(); // skip header
            if (header == null) return entities;

            String line;
            while ((line = reader.readLine()) != null) {
                entities.add(fromCSV(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    /**
     * Escreve todas as entidades no arquivo, sobrescrevendo o conteúdo anterior.
     *
     * @param entities Lista de entidades a serem escritas.
     */
    private void writeAll(List<T> entities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(getHeaders());
            writer.newLine();
            for (T entity : entities) {
                writer.write(toCSV(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converte uma entidade para uma string no formato CSV.
     *
     * @param entity Entidade a ser convertida.
     * @return String no formato CSV.
     */
    private String toCSV(T entity) {
        return Arrays.stream(type.getDeclaredFields())
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return String.valueOf(field.get(entity));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(","));
    }

    /**
     * Converte uma linha CSV em um objeto da entidade.
     *
     * @param line Linha do CSV.
     * @return Objeto instanciado da entidade.
     */
    private T fromCSV(String line) {
        try {
            String[] values = line.split(",");
            T instance = type.getDeclaredConstructor().newInstance();
            Field[] fields = type.getDeclaredFields();
            for (int i = 0; i < values.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(instance, parseValue(fields[i].getType(), values[i]));
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter CSV para objeto", e);
        }
    }

    /**
     * Converte uma string para o tipo correspondente da propriedade da entidade.
     *
     * @param type Tipo esperado.
     * @param value Valor em string.
     * @return Valor convertido para o tipo correto.
     */
    private Object parseValue(Class<?> type, String value) {
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        if (type == byte[].class) return Base64.getDecoder().decode(value); 
        return value;
    }

    /**
     * Retorna o valor de um campo da entidade usando reflexão.
     *
     * @param entity Instância da entidade.
     * @param fieldName Nome do campo desejado.
     * @return Valor do campo como String.
     */
    private String getFieldValue(T entity, String fieldName) {
        try {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            return String.valueOf(field.get(entity));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar campo", e);
        }
    }

    @Override
    public boolean updateField(String id, String fieldName, String newValue) {
        List<T> entities = selectAll();
        T entityToUpdate = entities.stream()
                .filter(e -> getFieldValue(e, idField).equals(id))
                .findFirst()
                .orElse(null);

        if (entityToUpdate != null) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object parsedValue = parseValue(field.getType(), newValue);
                field.set(entityToUpdate, parsedValue);

                writeAll(entities);
                return true;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
