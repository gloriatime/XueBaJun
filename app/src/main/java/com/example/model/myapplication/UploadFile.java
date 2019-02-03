package com.example.model.myapplication;

import java.io.File;

public class UploadFile {
	private String name;
    File image;
    
    public File getImage() {
        return image;
    }
 
    public void setImage(File file) {
        this.image = file;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
