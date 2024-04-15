package in.vi.user_management_app.controller;

import in.vi.user_management_app.dto.LoginDto;
import in.vi.user_management_app.dto.RegisterDto;
import in.vi.user_management_app.dto.ResetPwdDto;
import in.vi.user_management_app.entity.UserDetails;
import in.vi.user_management_app.repository.CityRepo;
import in.vi.user_management_app.repository.CountryRepo;
import in.vi.user_management_app.repository.StateRepo;
import in.vi.user_management_app.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private StateRepo stateRepo;

    @Autowired
    private CountryRepo countryRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo;
    @GetMapping("/register")
        public String registerPage (Model model)

        {
            model.addAttribute("userDetails", new UserDetails());
            return "registerView";
        }

        @GetMapping("/states/{cid}")
        public Map<Integer, String> getStates (@PathVariable("cid") Integer cid)
        {
            return null;
        }

        @GetMapping("/cities/{sid}")
        public Map<Integer, String> getCities (@PathVariable("sid")Integer sid)
        {
            return null;
        }


        @PostMapping("/register")
    public String register(RegisterDto regDto, Model model)
    {
        return "registerView";
    }

    @GetMapping("/")
    public String loginPage(Model model){
        model.addAttribute("userDetails",new UserDetails());
        return "index";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto, Model model) {
        return "index";
    }

    @PostMapping("/resetPwd")
    public String resetPwd(ResetPwdDto pwdDto, Model model)
    {
        return "";


    }

    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout()
    {

        return "index";
    }

 /*












*/
}
