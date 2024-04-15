package in.vi.user_management_app.repository;

import in.vi.user_management_app.entity.City;
import in.vi.user_management_app.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface CountryRepo extends JpaRepository<Country,Integer> {


}
