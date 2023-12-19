package BuisnessLayer;

import java.io.Serializable;

public class Comment implements Serializable {
    private static final long serialVersionUID = 574L;

    private String comment;
    private Annotation anno;

    public Comment() {
    }

    public Comment(String comment) {
        this.comment = comment;
    }

    public Comment(String comment, Annotation anno) {
        this.comment = comment;
        this.anno = anno;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Annotation getAnno() {
        return anno;
    }

    public void setAnno(Annotation anno) {
        this.anno = anno;
    }

    @Override
    public String toString() {
        return comment + " " + anno;
    }
}
