package com.example.appmusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Songs implements Parcelable {

    @SerializedName("id_song")
    @Expose
    private String idSong;

    @SerializedName("name_song")
    @Expose
    private String nameSong;

    @SerializedName("image_song")
    @Expose
    private String imageSong;

    @SerializedName("singer")
    @Expose
    private String singer;

    @SerializedName("link_song")
    @Expose
    private String linkSong;

    @SerializedName("feedback")
    @Expose
    private String feedback;

    @SerializedName("id_playlist") // Foreign key id
    @Expose
    private int idPlaylist;

    @SerializedName("id_album") // Foreign key id
    @Expose
    private int idAlbum;

    @SerializedName("id_type") // Foreign key id
    @Expose
    private int idType;

    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;

    public Songs() {
    }

    protected Songs(Parcel in) {
        idSong = in.readString();
        nameSong = in.readString();
        imageSong = in.readString();
        singer = in.readString();
        linkSong = in.readString();
        feedback = in.readString();
        idPlaylist = in.readInt();
        idAlbum = in.readInt();
        idType = in.readInt();
        isLiked = in.readByte() != 0;
    }

    public static final Creator<Songs> CREATOR = new Creator<Songs>() {
        @Override
        public Songs createFromParcel(Parcel in) {
            return new Songs(in);
        }

        @Override
        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };

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

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idSong);
        parcel.writeString(nameSong);
        parcel.writeString(imageSong);
        parcel.writeString(singer);
        parcel.writeString(linkSong);
        parcel.writeString(feedback);
        parcel.writeInt(idPlaylist);
        parcel.writeInt(idAlbum);
        parcel.writeInt(idType);
        parcel.writeByte((byte) (isLiked ? 1 : 0));
    }
}
