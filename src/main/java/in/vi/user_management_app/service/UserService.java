package in.vi.user_management_app.service;

import in.vi.user_management_app.dto.LoginDto;
import in.vi.user_management_app.dto.RegisterDto;
import in.vi.user_management_app.dto.ResetPwdDto;
import in.vi.user_management_app.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {

    public Map<Integer,String> getCountries();

    public Map<Integer,String> getStates(Integer cid);

    public Map<Integer,String> getCities(Integer cid);

    public UserDto getUser(String email);

    public boolean registerUser(RegisterDto registerDto);

    public UserDto getUser(LoginDto loginDto);

    public boolean resetPwd(ResetPwdDto pwdDto);

    public String getQuote();
}
