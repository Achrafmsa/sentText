package BuisnessLayer;

import java.io.Serializable;
import java.util.ArrayList;

public class Comments implements Serializable {
    private static final long serialVersionUID = 7875574L;
    private ArrayList<Comment> Acomm;
    private ArrayList<String> StringAcoom;
    private String nom;

    public Comments() {
    }

    public Comments(ArrayList<Comment> Acomm, String nom) {
        this.Acomm = Acomm;
        this.nom = nom;
    }
    public Comments(ArrayList<String> StringAcoom, String nom,boolean trueOrFalseNoProblem){
        this.StringAcoom = StringAcoom;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Comment> getAcomm() {
        return Acomm;
    }


    public void setAcomm(ArrayList<Comment> acomm) {
        if (!Acomm.isEmpty())
            Acomm.addAll(acomm);
        else
            Acomm = acomm;
    }

    public ArrayList<String> getStringAcoom() {
        return StringAcoom;
    }

    public void setStringAcoom(ArrayList<String> acomm) {
        if (!StringAcoom.isEmpty())
            StringAcoom.addAll(acomm);
        else
            StringAcoom = acomm;
    }



    public void addComment(Comment comm) {
        Acomm.add(comm);
    }

    @Override
    public String toString() {
        return nom;
    }
}
