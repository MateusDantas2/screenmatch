package br.com.alura.screenmatch.domain.enumaration;

/**
 * @author Mateus Dantas
 */
public enum Category {
    ACAO("Action"),
    COMEDIA("Comedy"),
    ROMANCE("Romance"),
    TERROR("Terrible"),
    DRAMA("Drama"),
    CRIME("Crime"),
    FANTASIA("Fantasy");

    private String omdbCategory;

    Category(String omdbCategory) {
        this.omdbCategory = omdbCategory;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}


