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
    UserRepository userRepo;

    public User createUser(User user) {
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()){
            return handleCreateExistingUser(existingUser.get(), user);
        } else{
            if (user.getPassword() == null){
                throw new AuthenticationServiceException("Password was null");
            }
            return saveNewRegisteredUser(user);
        }
    }

    private User handleCreateExistingUser(User existingUser, User newUser){
        if (existingUser.isRegistered()){
            throw new AuthenticationServiceException("A registered user with that email already exists");
        }

        existingUser.setFirstName(newUser.getFirstName());
        existingUser.setLastName(newUser.getLastName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setPassword(newUser.getPassword());

        return saveNewRegisteredUser(existingUser);
    }

    private User saveNewRegisteredUser(User userToSave){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userToSave.getPassword());
        userToSave.setPassword(encodedPassword);
        userToSave.setRegistered(true);

        return userRepo.save(userToSave);
    }

    public User createUnregisteredUser(User user){
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()){
            throw new AuthenticationServiceException("A user with that email already exists");
        }
        user.setRegistered(false);

        return userRepo.save(user);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepo.findByEmail(username);

        if (user.isEmpty()){
            throw new UsernameNotFoundException("Could not find username with email: " + username);
        }

        return user.get();
    }
}
