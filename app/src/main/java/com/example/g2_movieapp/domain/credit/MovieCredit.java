
package id.vn.minhlamdev.movieapp.domain.credit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieCredit {
    @SerializedName("id")
    @Expose
    private Integer id; // movie id
    @SerializedName("cast")
    @Expose
    private List<Actor> actor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Actor> getCast() {
        return actor;
    }

    public void setCast(List<Actor> actor) {
        this.actor = actor;
    }
}
