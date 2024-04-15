package in.vi.user_management_app.repository;


import in.vi.user_management_app.entity.UserDetails;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailsRepo extends JpaRepository<UserDetails,Integer> {


    public UserDetails findByEmail(String email);

    public UserDetails findByEmailAndPwd(String email, String pwd);

}
