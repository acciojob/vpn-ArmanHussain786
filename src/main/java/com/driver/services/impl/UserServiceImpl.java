package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        //create a user of given country. The originalIp of the user should be "countryCode.userId" and return the user. Note that right now user is not connected and thus connected would be false and maskedIp would be null
        //Note that the userId is created automatically by the repository layer
        String countryNameCaps = countryName.toUpperCase();
        if (!countryNameCaps.equals("IND") && !countryNameCaps.equals("USA") && !countryNameCaps.equals("AUS") && !countryNameCaps.equals("CHI") && !countryNameCaps.equals("JPN")) throw new Exception("Country not found");


        Country country = new Country();

//        switch (countryNameCaps) {
//            case "IND":
//                country.setCountryName(CountryName.IND);
//                country.setCode(CountryName.IND.toCode());
//                break;
//            case "USA":
//                country.setCountryName(CountryName.USA);
//                country.setCode(CountryName.USA.toCode());
//                break;
//            case "AUS":
//                country.setCountryName(CountryName.AUS);
//                country.setCode(CountryName.AUS.toCode());
//                break;
//            case "CHI":
//                country.setCountryName(CountryName.CHI);
//                country.setCode(CountryName.CHI.toCode());
//                break;
//            case "JPN":
//                country.setCountryName(CountryName.JPN);
//                country.setCode(CountryName.JPN.toCode());
//                break;
//            default :
//                throw new Exception("Country not found");
//        }


        country.setCountryName(CountryName.valueOf(countryNameCaps));
        country.setCode(CountryName.valueOf(countryNameCaps).toCode());

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);
        user.setMaskedIp(null);

        country.setUser(user);

        user.setOriginalCountry(country);

        userRepository3.save(user);
        user.setOriginalIp(user.getOriginalCountry().getCode()+"."+user.getId());

        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
        User user = userRepository3.findById(userId).get();

        List<User> userList = serviceProvider.getUsers();
        userList.add(user);
        serviceProvider.setUsers(userList);

        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
        serviceProviderList.add(serviceProvider);
        user.setServiceProviderList(serviceProviderList);

        serviceProviderRepository3.save(serviceProvider);

        return user;
    }
}
