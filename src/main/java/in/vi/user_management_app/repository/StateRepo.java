package in.vi.user_management_app.repository;

import in.vi.user_management_app.entity.Country;
import in.vi.user_management_app.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StateRepo extends JpaRepository<State,Integer> {

    /*
    @Query(
            value = "select * from State where countryId=:cid", nativeQuery = true
    )
    public List<State> getStates(Integer cid);

     */


}
