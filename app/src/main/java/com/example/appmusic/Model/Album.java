package com.example.appmusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Album implements Serializable {

@SerializedName("id_album")
@Expose
private String idAlbum;
@SerializedName("name_album")
@Expose
private String nameAlbum;
@SerializedName("name_singer")
@Expose
private String nameSinger;
@SerializedName("image_album")
@Expose
private String imageAlbum;

public String getIdAlbum() {
return idAlbum;
}

public void setIdAlbum(String idAlbum) {
this.idAlbum = idAlbum;
}

public String getNameAlbum() {
return nameAlbum;
}

public void setNameAlbum(String nameAlbum) {
this.nameAlbum = nameAlbum;
}

public String getNameSinger() {
return nameSinger;
}

public void setNameSinger(String nameSinger) {
this.nameSinger = nameSinger;
}

public String getImageAlbum() {
return imageAlbum;
}

public void setImageAlbum(String imageAlbum) {
this.imageAlbum = imageAlbum;
}

}