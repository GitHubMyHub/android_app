package com.toolbardemo.mytestapp.data;

/**
 * Created by V-Windows on 25.04.2017.
 *
 */

public class Certificate {

    // Certificate
    private String zertificateImagePath;
    private String zertificateName;
    private String zertificateDescription;

    public Certificate(){
        // leerer Konstruktor
    }

    // Certificate
    public String getZertificateImagePath(){
        return this.zertificateImagePath;
    }

    public void setZertificateImagePath(String zertificateImagePath){
        this.zertificateImagePath = zertificateImagePath;
    }

    public String getZertificateName(){
        return this.zertificateName;
    }

    public void setZertificateName(String zertificateName){
        this.zertificateName = zertificateName;
    }

    public String getZertificateDescription(){
        return this.zertificateDescription;
    }

    public void setZertificateDescription(String zertificateDescription){
        this.zertificateDescription = zertificateDescription;
    }

}
