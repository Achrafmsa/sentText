package PresentationLayer;

import BuisnessLayer.Annotation;
import BuisnessLayer.Comments;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class Project implements Serializable {
    private static final long serialVersionUID = 111111L;

    private Vector<Comments> vc;
    private Vector<Annotation> anno;

    public Project(Vector<Comments> vc, Vector<Annotation> anno) {
        this.vc = vc;
        this.anno = anno;
    }

    public Vector<Annotation> getAnno() {
        return anno;
    }

    public Vector<Comments> getVc() {
        return vc;
    }

    public void SaveProject(File file, Project proj) {
        ObjectOutputStream oos;
        try {
            // Toujours on va effacer le fichier avant de le remplir
            BufferedWriter fw = new BufferedWriter(new FileWriter(file));
            fw.write("");
            fw.close();
            if (!(proj == null)) {
                oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(proj);
                oos.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Le Fichier " + file.getName() + " est introuvable!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Project OpenProject(File file) {
        Project proj = null;
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            proj = (Project) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("Le Fichier " + file.getName() + " est introuvable!");
        } catch (EOFException e) {
            System.out.println("Fin de fichier ou fichier vide!\n");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proj;
    }
}
