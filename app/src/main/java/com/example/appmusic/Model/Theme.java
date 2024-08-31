package com.example.appmusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Theme implements Serializable {

@SerializedName("id_theme")
@Expose
private String idTheme;
@SerializedName("name_theme")
@Expose
private String nameTheme;
@SerializedName("image_theme")
@Expose
private String imageTheme;

public String getIdTheme() {
return idTheme;
}

public void setIdTheme(String idTheme) {
this.idTheme = idTheme;
}

public String getNameTheme() {
return nameTheme;
}

public void setNameTheme(String nameTheme) {
this.nameTheme = nameTheme;
}

public String getImageTheme() {
return imageTheme;
}

public void setImageTheme(String imageTheme) {
this.imageTheme = imageTheme;
}

}