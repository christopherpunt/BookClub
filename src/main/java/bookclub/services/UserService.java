package bookclub.services;

import bookclub.enums.UserRoleEnum;
import bookclub.models.User;
import bookclub.models.UserRole;
import bookclub.repositories.UserRepository;
import bookclub.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    UserRoleRepository userRoleRepo;

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

    public void registerAsRole(String username, UserRoleEnum roleEnum){
        Optional<User> userOptional = userRepo.findByEmail(username);

        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("A user with that email could not be found");
        }

        User user = userOptional.get();

        if (!userRoleRepo.existsByUserAndUserRole(user, roleEnum)){
            UserRole role = user.addUserRole(roleEnum);
            userRoleRepo.save(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(username);

        if (userOptional.isEmpty()){
            throw new UsernameNotFoundException("Could not find username with email: " + username);
        }
        User user = userOptional.get();

        List<UserRole> userRoles = userRoleRepo.findAllUserRolesByUser(user);

        Collection<? extends GrantedAuthority> authorities = userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getUserRole().name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
