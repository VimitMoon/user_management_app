package in.vi.user_management_app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.vi.user_management_app.dto.*;
import in.vi.user_management_app.entity.City;
import in.vi.user_management_app.entity.Country;
import in.vi.user_management_app.entity.State;
import in.vi.user_management_app.entity.UserDetails;
import in.vi.user_management_app.repository.CityRepo;
import in.vi.user_management_app.repository.CountryRepo;
import in.vi.user_management_app.repository.StateRepo;
import in.vi.user_management_app.repository.UserDetailsRepo;
import in.vi.user_management_app.utils.EmailUtils;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private StateRepo stateRepo;

    @Autowired
    private CountryRepo countryRepo;

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private EmailUtils emailUtils;

    private QuoteDto[] quotations = null;

    @Override
    public Map<Integer, String> getCountries() {

        List<Country> countryList = countryRepo.findAll();

        Map<Integer, String> countryMap = new HashMap<>();

        countryList.forEach(c -> {
            countryMap.put(c.getCountryId(),c.getCountryName());
                }
                );

        return countryMap;
    }

    @Override
    public Map<Integer, String> getStates(Integer cid) {

        Map<Integer, String> stateMap = new HashMap<>();

        Country country = new Country();
        country.setCountryId(cid);

        State s = new State();
        s.setCountry(country);

        Example<State> of = Example.of(s);

        List<State> stateList = stateRepo.findAll(of);



        stateList.forEach(st -> {
                    stateMap.put(st.getStateId(),st.getStateName());
                }
        );

        return stateMap;
    }

    @Override
    public Map<Integer, String> getCities(Integer sid) {
        Map<Integer, String> cityMap = new HashMap<>();
        List<City> cityList = cityRepo.getCities(sid);
        cityList.forEach(ct -> {
                    cityMap.put(ct.getCityId(),ct.getCityName());
                }
        );

        return cityMap;
    }

    @Override
    public UserDto getUser(String email) {

        UserDetails userDetails = userDetailsRepo.findByEmail(email);

    /*
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails,userDto);

    */
        if (userDetails != null) {
            ModelMapper mapper = new ModelMapper();
            UserDto userDto = mapper.map(userDetails, UserDto.class);

            return userDto;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean registerUser(RegisterDto regDto) {

        // mapper to copy data from entity to dto
        // 1. create new object of Model mapper
        // 2. nap to entity class
        // 3. use repository methods to find and set data and then save the dto object

        ModelMapper mapper = new ModelMapper();

        UserDetails userDetails = mapper.map(regDto,UserDetails.class);

        // association mapping
        Country country = countryRepo.findById(regDto.getCountryId()).orElseThrow();

        State state = stateRepo.findById(regDto.getStateId()).orElseThrow();

        City city = cityRepo.findById(regDto.getCityId()).orElseThrow();

        userDetails.setCountry(country);
        userDetails.setState(state);
        userDetails.setCity(city);

        userDetails.setPwd(generateRandomPwd());
        userDetails.setPwdUpdated("NO");

        // user registration
        UserDetails savedDetails = userDetailsRepo.save(userDetails);

        String subject = "User Registration";

        String body = "Your temporary password is : "+ userDetails.getPwd();

        emailUtils.sendEmail(regDto.getEmail(),subject,body);

        return savedDetails.getUser_id() != null;
    }

    @Override
    public UserDto getUser(LoginDto loginDto) {

        UserDetails userDetails = userDetailsRepo.findByEmailAndPwd(loginDto.getEmail(), loginDto.getPwd());

        if(userDetails == null ) {
            return null;
        }

        ModelMapper mapper = new ModelMapper();

        return mapper.map(userDetails,UserDto.class);
    }

    @Override
    public boolean resetPwd(ResetPwdDto pwdDto) {

        UserDetails userDetails =
                userDetailsRepo.findByEmailAndPwd(pwdDto.getEmail(), pwdDto.getOldPwd());

        if (userDetails != null) {

            userDetails.setPwd(pwdDto.getNewPwd());

            userDetails.setPwdUpdated("YES");

            userDetailsRepo.save(userDetails);

            return true;

        /*
        ModelMapper mapper = new ModelMapper();

        ResetPwdDto loggedUser = mapper.map(userDetails,ResetPwdDto.class);
         */
        }

        return false;
    }

    @Override
    public String getQuote() {

        if (quotations==null) {
            // 3rd party url
            String url = "https://type.fit/api/quotes";


            // web service call
            RestTemplate rt = new RestTemplate();

            // return response body
            ResponseEntity<String> forEntity = rt.getForEntity(url,String.class);

            String responseBody = forEntity.getBody();

            // converting text data into java object by using ObjectMapper provided by Jackson Api
            ObjectMapper mapper = new ObjectMapper();
            try {
                // converting response body into quoteDto Array
                quotations = mapper.readValue(responseBody, QuoteDto[].class);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

            Random r = new Random();

            int index = r.nextInt(quotations.length-1);

            return quotations[index].getText();

    }
    private static String generateRandomPwd() {
        String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for(int i=0; i<6; i++)
        {
            int randIndex = rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }
}
