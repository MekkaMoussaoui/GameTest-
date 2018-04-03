package dz.atelier.ntic.remplircases.game.model;

import java.io.Serializable;

/**
 * Created by Mékkah on 22/03/2018.
 */

public class RowData implements Serializable{
    private String main_title;
    private int img_title;

    public RowData (String main_title, int img_title){
        this.main_title=main_title;
        this.img_title=img_title;
    }
    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public int getImg_title() {
        return img_title;
    }

    public void setImg_title(int img_title) {
        this.img_title = img_title;
    }



}
