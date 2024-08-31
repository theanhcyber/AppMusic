package com.example.appmusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Banner implements Serializable {

@SerializedName("id_banner")
@Expose
private String idBanner;
@SerializedName("image_banner")
@Expose
private String imageBanner;
@SerializedName("text")
@Expose
private String text;
@SerializedName("id_song")
@Expose
private String idSong;
@SerializedName("name_song")
@Expose
private String nameSong;
@SerializedName("image_song")
@Expose
private String imageSong;

public String getIdBanner() {
return idBanner;
}

public void setIdBanner(String idBanner) {
this.idBanner = idBanner;
}

public String getImageBanner() {
return imageBanner;
}

public void setImageBanner(String imageBanner) {
this.imageBanner = imageBanner;
}

public String getText() {
return text;
}

public void setText(String text) {
this.text = text;
}

public String getIdSong() {
return idSong;
}

public void setIdSong(String idSong) {
this.idSong = idSong;
}

public String getNameSong() {
return nameSong;
}

public void setNameSong(String nameSong) {
this.nameSong = nameSong;
}

public String getImageSong() {
return imageSong;
}

public void setImageSong(String imageSong) {
this.imageSong = imageSong;
}

}