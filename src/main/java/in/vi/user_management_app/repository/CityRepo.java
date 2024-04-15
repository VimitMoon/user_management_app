package in.vi.user_management_app.repository;

import in.vi.user_management_app.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CityRepo extends JpaRepository<City,Integer> {


    @Query(
            value = "select * from City where stateId=:sid",
            nativeQuery = true
    )
    public List<City> getCities(Integer sid);
}
