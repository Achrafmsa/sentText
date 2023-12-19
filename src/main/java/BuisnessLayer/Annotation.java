package BuisnessLayer;

import java.io.Serializable;

public class Annotation implements Serializable {
    private static final long serialVersionUID = 744574L;

    private String nom;

    public Annotation(String nom){
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public static Comment Annoter(String Comment,Annotation anno){
        Comment com ;
        return  com = new Comment(Comment,anno);
    }

    @Override
    public String toString() {
        return nom;
    }
}
