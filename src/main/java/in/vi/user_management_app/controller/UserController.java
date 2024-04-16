package in.vi.user_management_app.controller;

import in.vi.user_management_app.dto.LoginDto;
import in.vi.user_management_app.dto.RegisterDto;
import in.vi.user_management_app.dto.ResetPwdDto;
import in.vi.user_management_app.dto.UserDto;
import in.vi.user_management_app.entity.Country;
import in.vi.user_management_app.entity.UserDetails;
import in.vi.user_management_app.repository.CityRepo;
import in.vi.user_management_app.repository.CountryRepo;
import in.vi.user_management_app.repository.StateRepo;
import in.vi.user_management_app.repository.UserDetailsRepo;
import in.vi.user_management_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    private UserService userService;

    // method responsible for loading the register page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());

        // form binding object
            /*
            Map<Integer,String> countriesMap = userService.getCountries();
             */

        // form binding object
        model.addAttribute("countries", userService.getCountries());

        return "registerView";
    }

    @GetMapping("/states/{cid}")
    @ResponseBody // @ResponseBody annotation used for returning the direct response through controller
    public Map<Integer, String> getStates(@PathVariable("cid") Integer cid) {
        return userService.getStates(cid);
    }

    @GetMapping("/cities/{sid}")
    @ResponseBody // @ResponseBody annotation used for returning the direct response through controller
    public Map<Integer, String> getCities(@PathVariable("sid") Integer sid) {
        return userService.getCities(sid);
    }


    @PostMapping("/register")
    public String register(RegisterDto regDto, Model model) {

        // retrieve the user email to check the user is not duplicate
        UserDto userDto = userService.getUser(regDto.getEmail());
        if (userDto != null) {
            model.addAttribute("emsg", "User already registered with this email id..");
            return "registerView";
        }
        boolean registerUser = userService.registerUser(regDto);
        if (registerUser) {
            model.addAttribute("smsg", "User Successfully Registered.");
        } else {
            model.addAttribute("emsg", "User Registration Failed.");
        }

        return "registerView";
    }

    @GetMapping("/")
    public String loginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "index";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto, Model model) {

        UserDto userDto = userService.getUser(loginDto);
        if (userDto == null) {
            model.addAttribute("emsg", "User Not Logged In. Not registered, Register first");
            return "index";
        }
        if ("YES".equals(userDto.getPwdUpdated()))
        // password already updated so go to dashboard
        {
            return "redirect:dashboard";
        }
        // password not updated goto reset password page
        else {
            ResetPwdDto resetPwdDto = new ResetPwdDto();
            resetPwdDto.setEmail(userDto.getEmail());
       //     model.addAttribute("smsg", "User Successfully Logged In.");
            model.addAttribute("resetPwdDto",resetPwdDto);
            return "resetPwdView";

        }

    }

    @PostMapping("/resetPwd")
    public String resetPwd(ResetPwdDto pwdDto, Model model) {
        if (!(pwdDto.getNewPwd().equals(pwdDto.getConfirmPwd()))) {
            model.addAttribute("emsg", "New and Confirm password should be same.");
            return "resetPwdView";
        }
        UserDto userDto = userService.getUser(pwdDto.getEmail());

        if (userDto.getPwd().equals(pwdDto.getOldPwd())) {
            boolean resetPwd = userService.resetPwd(pwdDto);
            if (resetPwd) {
                return "redirect:dashboard";
            } else {
                model.addAttribute("emsg", "password Update Failed.");
                return "resetPwdView";
            }
        } else {
            model.addAttribute("emsg", "Given old password is wrong");
            return "resetPwdView";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        String quote = userService.getQuote();
        model.addAttribute("quote",quote);
        return "dashboardView";
    }

    @GetMapping("/logout")
    public String logout()
    {
        return "redirect:/";
    }
}
