package bookclub.services;

import bookclub.models.User;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userDao;

    public User createUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.isRegistered = true;

        Optional<User> existingUser = userDao.findByEmail(user.getEmail());

        if (existingUser.isPresent()){
            throw new AuthenticationServiceException("A user with that email already exists");
        }

        return userDao.save(user);
    }

    public User createUnregisteredUser(User user){
        Optional<User> existingUser = userDao.findByEmail(user.getEmail());

        if (existingUser.isPresent()){
            throw new AuthenticationServiceException("A user with that email already exists");
        }

        user.isRegistered = false;

        return userDao.save(user);
    }

    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userDao.findByEmail(username);

        if (user.isEmpty()){
            throw new UsernameNotFoundException("Could not find username with email: " + username);
        }

        return user.get();
    }
}
