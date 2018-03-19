package be.ehb.xplorebxl.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Q on 18-3-2018.
 */

@Dao
public interface StreetArtDAO {

    @Query("SELECT * FROM StreetArt")
    List<StreetArt>getAllStreetArt();

    @Insert
    void insertStreetArt(StreetArt s);

    @Update
    void updateStreetArt(StreetArt s);

}
