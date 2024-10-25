
package id.vn.minhlamdev.movieapp.domain.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tmdb {

    @SerializedName("avatar_path")
    @Expose
    private String avatarPath;

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

}
