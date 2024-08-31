package com.example.appmusic.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChuDeVaTheLoai {

@SerializedName("Types")
@Expose
private List<Type> types;
@SerializedName("Theme")
@Expose
private List<Theme> theme;

public List<Type> getTypes() {
return types;
}

public void setTypes(List<Type> types) {
this.types = types;
}

public List<Theme> getTheme() {
return theme;
}

public void setTheme(List<Theme> theme) {
this.theme = theme;
}

}